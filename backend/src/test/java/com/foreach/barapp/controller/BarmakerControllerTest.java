package com.foreach.barapp.controller;

import com.foreach.barapp.entity.Commande;
import com.foreach.barapp.service.BarmakerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BarmakerController.class)
class BarmakerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BarmakerService barmakerService;

    @Test
    void testGetCommandesATraiter() throws Exception {
        Commande commande = new Commande();
        commande.setIdCommande(1);
        commande.setStatutGlobal("En cours de préparation");

        when(barmakerService.getCommandesActives()).thenReturn(Collections.singletonList(commande));

        mockMvc.perform(get("/api/barmaker/commandes"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].statutGlobal").value("En cours de préparation"));
    }
}
