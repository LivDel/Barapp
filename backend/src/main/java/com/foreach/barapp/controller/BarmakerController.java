package com.foreach.barapp.controller;

import com.foreach.barapp.entity.Barmaker;
import com.foreach.barapp.entity.CocktailCommande;
import com.foreach.barapp.entity.Commande;
import com.foreach.barapp.dto.request.LoginRequestDto;
import com.foreach.barapp.service.BarmakerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller gérant le tableau de bord du Barmaker.
 */
@RestController
@RequestMapping("/api/barmaker")
@CrossOrigin(origins = "*") // Indispensable pour la communication avec VueJS
public class BarmakerController {

    private final BarmakerService barmakerService;

    public BarmakerController(BarmakerService barmakerService) {
        this.barmakerService = barmakerService;
    }

    /**
     * Affiche les commandes à traiter pour le barmaker (celles qui ne sont pas Terminées).
     * Exemple : GET /api/barmaker/commandes
     */
    @GetMapping("/commandes")
    public List<Commande> getCommandesATraiter() {
        return barmakerService.getCommandesActives();
    }

    /**
     * Fait avancer l'étape de préparation d'un cocktail.
     * On utilise PUT car on modifie l'état d'une ressource existante.
     * Exemple : PUT /api/barmaker/cocktails-commandes/4/statut
     */
    @PutMapping("/cocktails-commandes/{idCocktailCommande}/statut")
    public CocktailCommande faireAvancerStatutCocktail(@PathVariable Integer idCocktailCommande) {
        // Le controller se contente d'appeler le service, toute l'intelligence métier y est cachée.
        return barmakerService.faireAvancerStatut(idCocktailCommande);
    }

    /**
     * Authentifie le barman.
     * Exemple : POST /api/barmaker/login
     */
    @PostMapping("/login")
    public Barmaker login(@RequestBody LoginRequestDto request) {
        return barmakerService.login(request.getIdentifiant(), request.getMotDePasse());
    }
}
