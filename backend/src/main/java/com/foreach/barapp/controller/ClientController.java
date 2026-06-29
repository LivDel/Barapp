package com.foreach.barapp.controller;

import com.foreach.barapp.dto.request.CommandeRequestDto;
import com.foreach.barapp.entity.Commande;
import com.foreach.barapp.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controller dédié aux actions des clients.
 */
@RestController
@RequestMapping("/api/client")
@CrossOrigin(origins = "*") // Autorise le Front-end VueJS à se connecter sans erreur CORS
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Endpoint pour lancer une commande.
     * Reçoit le panier sous forme de JSON correspondant au CommandeRequestDto.
     */
    @PostMapping("/commandes")
    @ResponseStatus(HttpStatus.CREATED) // Code HTTP 201 (Ressource créée)
    public Commande lancerCommande(@RequestBody CommandeRequestDto request) {
        return clientService.lancerCommande(request);
    }

    /**
     * Endpoint pour consulter l'avancée d'une commande.
     * Exemple : GET /api/client/commandes/4
     */
    @GetMapping("/commandes/{id}")
    public Commande getStatutCommande(@PathVariable Integer id) {
        return clientService.getCommandeDetails(id);
    }
}
