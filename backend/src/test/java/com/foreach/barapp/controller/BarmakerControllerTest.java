package com.foreach.barapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foreach.barapp.dto.request.LoginRequestDto;
import com.foreach.barapp.entity.Barmaker;
import com.foreach.barapp.entity.CocktailCommande;
import com.foreach.barapp.entity.Commande;
import com.foreach.barapp.service.BarmakerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BarmakerController.class)
class BarmakerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BarmakerService barmakerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetCommandesATraiter() throws Exception {
        Commande commande = new Commande();
        commande.setIdCommande(10);
        commande.setStatutGlobal("En cours de préparation");

        when(barmakerService.getCommandesActives()).thenReturn(Arrays.asList(commande));

        mockMvc.perform(get("/api/barmaker/commandes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCommande").value(10))
                .andExpect(jsonPath("$[0].statutGlobal").value("En cours de préparation"));
    }

    @Test
    void testFaireAvancerStatutCocktail() throws Exception {
        CocktailCommande ligne = new CocktailCommande();
        ligne.setId(1);
        ligne.setStatutPreparation("Assemblage");

        when(barmakerService.faireAvancerStatut(1)).thenReturn(ligne);

        mockMvc.perform(put("/api/barmaker/cocktails-commandes/1/statut"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.statutPreparation").value("Assemblage"));
    }

    @Test
    void testLogin() throws Exception {
        LoginRequestDto request = new LoginRequestDto();
        request.setIdentifiant("admin");
        request.setMotDePasse("pass");

        Barmaker barmaker = new Barmaker();
        barmaker.setIdentifiant("admin");
        barmaker.setRole("BARMAN");

        when(barmakerService.login("admin", "pass")).thenReturn(barmaker);

        mockMvc.perform(post("/api/barmaker/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identifiant").value("admin"))
                .andExpect(jsonPath("$.role").value("BARMAN"));
    }
}
