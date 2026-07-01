package com.foreach.barapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foreach.barapp.dto.request.CommandeRequestDto;
import com.foreach.barapp.entity.Commande;
import com.foreach.barapp.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLancerCommande() throws Exception {
        CommandeRequestDto request = new CommandeRequestDto();
        request.setNumeroTable(5);
        CommandeRequestDto.CocktailCommandeDto ccDto = new CommandeRequestDto.CocktailCommandeDto();
        ccDto.setIdCocktail(1);
        ccDto.setIdTaille(1);
        request.setCocktails(Collections.singletonList(ccDto));

        Commande responseCommande = new Commande();
        responseCommande.setIdCommande(10);
        responseCommande.setNumeroTable(5);
        responseCommande.setStatutGlobal("Commandée");

        when(clientService.lancerCommande(any())).thenReturn(responseCommande);

        mockMvc.perform(post("/api/client/commandes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCommande").value(10))
                .andExpect(jsonPath("$.statutGlobal").value("Commandée"));
    }

    @Test
    void testGetStatutCommande() throws Exception {
        Commande responseCommande = new Commande();
        responseCommande.setIdCommande(10);
        responseCommande.setStatutGlobal("En préparation");

        when(clientService.getCommandeDetails(10)).thenReturn(responseCommande);

        mockMvc.perform(get("/api/client/commandes/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCommande").value(10))
                .andExpect(jsonPath("$.statutGlobal").value("En préparation"));
    }
}
