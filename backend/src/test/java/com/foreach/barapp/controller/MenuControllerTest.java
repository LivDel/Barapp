package com.foreach.barapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foreach.barapp.entity.Categorie;
import com.foreach.barapp.entity.Cocktail;
import com.foreach.barapp.entity.Ingredient;
import com.foreach.barapp.entity.Taille;
import com.foreach.barapp.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuController.class)
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @Autowired
    private ObjectMapper objectMapper;

    // --- CATEGORIES ---
    @Test
    void testGetCategories() throws Exception {
        Categorie cat = new Categorie();
        cat.setLibelle("Softs");
        when(menuService.getAllCategories()).thenReturn(Arrays.asList(cat));

        mockMvc.perform(get("/api/menu/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].libelle").value("Softs"));
    }

    @Test
    void testAddCategorie() throws Exception {
        Categorie cat = new Categorie();
        cat.setLibelle("Softs");
        when(menuService.createCategorie(any())).thenReturn(cat);

        mockMvc.perform(post("/api/menu/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cat)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.libelle").value("Softs"));
    }

    // --- INGREDIENTS ---
    @Test
    void testGetIngredients() throws Exception {
        Ingredient ing = new Ingredient();
        ing.setNom("Sucre");
        when(menuService.getAllIngredients()).thenReturn(Arrays.asList(ing));

        mockMvc.perform(get("/api/menu/ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Sucre"));
    }

    @Test
    void testAddIngredient() throws Exception {
        Ingredient ing = new Ingredient();
        ing.setNom("Sucre");
        when(menuService.createIngredient(any())).thenReturn(ing);

        mockMvc.perform(post("/api/menu/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ing)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Sucre"));
    }

    // --- TAILLES ---
    @Test
    void testGetTailles() throws Exception {
        Taille taille = new Taille();
        taille.setLibelle("S");
        when(menuService.getAllTailles()).thenReturn(Arrays.asList(taille));

        mockMvc.perform(get("/api/menu/tailles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].libelle").value("S"));
    }

    @Test
    void testAddTaille() throws Exception {
        Taille taille = new Taille();
        taille.setLibelle("S");
        when(menuService.createTaille(any())).thenReturn(taille);

        mockMvc.perform(post("/api/menu/tailles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taille)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.libelle").value("S"));
    }

    // --- COCKTAILS ---
    @Test
    void testGetCocktails() throws Exception {
        Cocktail cocktail = new Cocktail();
        cocktail.setNom("Pina Colada");
        when(menuService.getAllCocktails()).thenReturn(Arrays.asList(cocktail));

        mockMvc.perform(get("/api/menu/cocktails"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Pina Colada"));
    }

    @Test
    void testAddCocktail() throws Exception {
        Cocktail cocktail = new Cocktail();
        cocktail.setNom("Pina Colada");
        when(menuService.createCocktail(any())).thenReturn(cocktail);

        mockMvc.perform(post("/api/menu/cocktails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cocktail)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Pina Colada"));
    }
}
