package com.foreach.barapp.service;

import com.foreach.barapp.entity.CocktailCommande;
import com.foreach.barapp.entity.Commande;
import com.foreach.barapp.exception.ResourceNotFoundException;
import com.foreach.barapp.repository.CommandeRepository;
import com.foreach.barapp.repository.CocktailCommandeRepository;
import com.foreach.barapp.repository.BarmakerRepository;
import com.foreach.barapp.entity.Barmaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service gérant la logique métier du Barmaker (le tableau de bord de
 * préparation).
 */
@Service
public class BarmakerService {

    private final CommandeRepository commandeRepository;
    private final CocktailCommandeRepository cocktailCommandeRepository;
    private final BarmakerRepository barmakerRepository;

    public BarmakerService(CommandeRepository commandeRepository,
            CocktailCommandeRepository cocktailCommandeRepository,
            BarmakerRepository barmakerRepository) {
        this.commandeRepository = commandeRepository;
        this.cocktailCommandeRepository = cocktailCommandeRepository;
        this.barmakerRepository = barmakerRepository;
    }

    /**
     * Authentifie un Barmaker.
     */
    public Barmaker login(String identifiant, String motDePasse) {
        return barmakerRepository.findByIdentifiantAndMotDePasse(identifiant, motDePasse)
                .orElseThrow(() -> new ResourceNotFoundException("Identifiant ou mot de passe incorrect."));
    }

    /**
     * Récupère la liste de toutes les commandes qui ne sont pas encore terminées.
     * C'est l'écran principal du Barmaker.
     */
    public List<Commande> getCommandesActives() {
        // La méthode "findByStatutGlobalNot" a été générée automatiquement par Spring
        // Data JPA dans le repository !
        return commandeRepository.findByStatutGlobalNot("Terminée");
    }

    /**
     * Fait avancer le statut d'un cocktail spécifique.
     * Vérifie ensuite si tous les cocktails de la commande sont terminés pour
     * mettre à jour le statut global.
     */
    @Transactional
    public CocktailCommande faireAvancerStatut(Integer idCocktailCommande) {
        // 1. Récupérer la ligne de commande (le cocktail en cours de préparation)
        CocktailCommande ligne = cocktailCommandeRepository.findById(idCocktailCommande)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Cocktail commandé introuvable : " + idCocktailCommande));

        // 2. Faire avancer le statut selon les règles métier strictes du cahier des
        // charges
        String statutActuel = ligne.getStatutPreparation();
        switch (statutActuel) {
            case "En attente":
                ligne.setStatutPreparation("Préparation des Ingrédients");
                break;
            case "Préparation des Ingrédients":
                ligne.setStatutPreparation("Assemblage");
                break;
            case "Assemblage":
                ligne.setStatutPreparation("Dressage");
                break;
            case "Dressage":
                ligne.setStatutPreparation("Terminée");
                break;
            case "Terminée":
                // Déjà terminé, on ne fait rien de plus
                return ligne;
            default:
                ligne.setStatutPreparation("Préparation des Ingrédients"); // Sécurité (Fallback)
        }

        // Sauvegarde de l'avancée du cocktail
        cocktailCommandeRepository.save(ligne);

        // 3. LOGIQUE MÉTIER : Vérifier si la commande globale est terminée
        Commande commandeGlobale = ligne.getCommande();

        // On parcourt tous les cocktails de cette commande
        boolean tousTermines = true;
        for (CocktailCommande cc : commandeGlobale.getCocktailsCommandes()) {
            if (!"Terminée".equals(cc.getStatutPreparation())) {
                tousTermines = false;
                break; // Dès qu'un seul cocktail n'est pas terminé, on arrête de vérifier, la commande
                       // n'est pas finie.
            }
        }

        // Si la boucle finit et que "tousTermines" est toujours "true", c'est que la
        // commande est complète !
        if (tousTermines) {
            commandeGlobale.setStatutGlobal("Terminée");
            commandeRepository.save(commandeGlobale);
        } else if ("Commandée".equals(commandeGlobale.getStatutGlobal())) {
            // Si la commande globale vient juste de commencer à être préparée
            commandeGlobale.setStatutGlobal("En cours de préparation");
            commandeRepository.save(commandeGlobale);
        }

        return ligne;
    }
}
