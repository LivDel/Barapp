package com.foreach.barapp.service;

import com.foreach.barapp.dto.request.CommandeRequestDto;
import com.foreach.barapp.entity.*;
import com.foreach.barapp.exception.ResourceNotFoundException;
import com.foreach.barapp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service gérant la logique métier des clients (Prise de commande et suivi).
 */
@Service
public class ClientService {

    private final CommandeRepository commandeRepository;
    private final CocktailCommandeRepository cocktailCommandeRepository;
    private final CocktailRepository cocktailRepository;
    private final TailleRepository tailleRepository;

    public ClientService(CommandeRepository commandeRepository, CocktailCommandeRepository cocktailCommandeRepository, CocktailRepository cocktailRepository, TailleRepository tailleRepository) {
        this.commandeRepository = commandeRepository;
        this.cocktailCommandeRepository = cocktailCommandeRepository;
        this.cocktailRepository = cocktailRepository;
        this.tailleRepository = tailleRepository;
    }

    /**
     * Logique de création d'une nouvelle commande à partir du panier du client.
     * @Transactional garantit que si une taille ou un cocktail n'existe pas, 
     * toute la création de la commande est annulée (rollback).
     */
    @Transactional
    public Commande lancerCommande(CommandeRequestDto request) {
        // 1. Création et initialisation de la commande globale
        Commande commande = new Commande();
        commande.setNumeroTable(request.getNumeroTable());
        commande.setDateHeureCreation(LocalDateTime.now());
        commande.setStatutGlobal("Commandée"); // Statut initial

        // On la sauvegarde pour que PostgreSQL génère son ID (id_commande)
        Commande savedCommande = commandeRepository.save(commande);

        // 2. Traitement de chaque ligne du panier (les cocktails)
        for (CommandeRequestDto.CocktailCommandeDto dto : request.getCocktails()) {
            
            // On vérifie que le cocktail existe en base, sinon on lance notre erreur 404 personnalisée
            Cocktail cocktail = cocktailRepository.findById(dto.getIdCocktail())
                    .orElseThrow(() -> new ResourceNotFoundException("Cocktail introuvable : " + dto.getIdCocktail()));
            
            // Pareil pour la taille
            Taille taille = tailleRepository.findById(dto.getIdTaille())
                    .orElseThrow(() -> new ResourceNotFoundException("Taille introuvable : " + dto.getIdTaille()));

            // On crée la ligne de commande (Cocktail_Commande)
            CocktailCommande ligneCommande = new CocktailCommande();
            ligneCommande.setCommande(savedCommande);
            ligneCommande.setCocktail(cocktail);
            ligneCommande.setTaille(taille);
            ligneCommande.setStatutPreparation("En attente"); // Le barmaker ne l'a pas encore pris en charge

            // Sauvegarde en base
            cocktailCommandeRepository.save(ligneCommande);
            
            // Ajout à la liste en mémoire pour que la réponse HTTP contienne toutes les lignes dès la création
            savedCommande.getCocktailsCommandes().add(ligneCommande);
        }

        return savedCommande;
    }

    /**
     * Permet au client de consulter sa commande (avec l'avancée globale et détaillée de ses cocktails).
     */
    public Commande getCommandeDetails(Integer idCommande) {
        return commandeRepository.findById(idCommande)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable : " + idCommande));
    }
}
