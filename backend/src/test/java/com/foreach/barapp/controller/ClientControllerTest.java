package com.foreach.barapp.controller;

import com.foreach.barapp.dto.request.CommandeRequestDto;
import com.foreach.barapp.entity.Commande;
import com.foreach.barapp.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Test
    void testLancerCommande() throws Exception {
        Commande commande = new Commande();
        commande.setIdCommande(1);
        commande.setStatutGlobal("Commandée");
        commande.setNumeroTable(5);

        // On simule le service qui renvoie une commande créée avec succès
        when(clientService.lancerCommande(any(CommandeRequestDto.class))).thenReturn(commande);

        // Le faux JSON envoyé par le client (VueJS)
        String jsonRequest = "{ \"numeroTable\": 5, \"cocktails\": [ {\"idCocktail\": 1, \"idTaille\": 1} ] }";

        // Simulation de la requête HTTP
        mockMvc.perform(post("/api/client/commandes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
               .andExpect(status().isCreated()) // 201 Created
               .andExpect(jsonPath("$.statutGlobal").value("Commandée"))
               .andExpect(jsonPath("$.numeroTable").value(5));
    }
}
