package com.foreach.barapp.controller;

import com.foreach.barapp.entity.Categorie;
import com.foreach.barapp.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @WebMvcTest démarre uniquement la partie Web (les Controllers) sans charger
 *             toute l'application
 *             ni se connecter à la vraie base de données.
 */
@WebMvcTest(MenuController.class)
class MenuControllerTest {

    // MockMvc est un outil de Spring qui permet de simuler de vraies
    // requêtes HTTP
    // (comme si on cliquait sur un lien ou qu'on envoyait un formulaire depuis le
    // front-end)
    @Autowired
    private MockMvc mockMvc;

    // @MockBean remplace le VRAI MenuService par un FAUX (Mock) dans le contexte
    // Spring de ce test.
    @MockBean
    private MenuService menuService;

    @Test
    void testGetCategories() throws Exception {
        // 1. Préparation des fausses données
        Categorie categorie = new Categorie();
        categorie.setIdCategorie(1);
        categorie.setLibelle("Sans Alcool");

        // 2. On configure le faux service pour qu'il réponde ce qu'on veut
        when(menuService.getAllCategories()).thenReturn(Arrays.asList(categorie));

        // 3. On simule une requête HTTP GET vers l'URL /api/menu/categories
        mockMvc.perform(get("/api/menu/categories"))
                // On s'attend à ce que le code de statut HTTP soit 200 (OK)
                .andExpect(status().isOk())
                // On s'attend à ce que le format de la réponse soit bien du JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // On s'attend à ce que le premier élément JSON (l'index 0) ait le libellé "Sans
                // Alcool"
                .andExpect(jsonPath("$[0].libelle").value("Sans Alcool"));
    }
}
