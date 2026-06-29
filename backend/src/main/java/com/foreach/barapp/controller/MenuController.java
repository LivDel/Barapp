package com.foreach.barapp.controller;

import com.foreach.barapp.entity.Categorie;
import com.foreach.barapp.entity.Cocktail;
import com.foreach.barapp.entity.Ingredient;
import com.foreach.barapp.entity.Taille;
import com.foreach.barapp.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Ce Controller gère les requêtes HTTP (GET, POST) liées à la carte du bar.
 * @RestController indique que les méthodes renvoient directement du JSON.
 * @RequestMapping définit le préfixe de l'URL (/api/menu) pour tous les endpoints.
 */
@RestController
@RequestMapping("/api/menu")
// L'annotation suivante permet d'autoriser le Front-end VueJS (ex: sur le port 5173) à requêter l'API sans erreur de sécurité CORS.
@CrossOrigin(origins = "*") 
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // --- CATÉGORIES ---
    @GetMapping("/categories")
    public List<Categorie> getCategories() {
        return menuService.getAllCategories();
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED) // Renvoie 201 Created au lieu de 200 OK
    public Categorie addCategorie(@RequestBody Categorie categorie) {
        return menuService.createCategorie(categorie);
    }

    // --- INGRÉDIENTS ---
    @GetMapping("/ingredients")
    public List<Ingredient> getIngredients() {
        return menuService.getAllIngredients();
    }

    @PostMapping("/ingredients")
    @ResponseStatus(HttpStatus.CREATED)
    public Ingredient addIngredient(@RequestBody Ingredient ingredient) {
        return menuService.createIngredient(ingredient);
    }

    // --- TAILLES ---
    @GetMapping("/tailles")
    public List<Taille> getTailles() {
        return menuService.getAllTailles();
    }

    @PostMapping("/tailles")
    @ResponseStatus(HttpStatus.CREATED)
    public Taille addTaille(@RequestBody Taille taille) {
        return menuService.createTaille(taille);
    }

    // --- COCKTAILS ---
    @GetMapping("/cocktails")
    public List<Cocktail> getCocktails() {
        return menuService.getAllCocktails();
    }

    @PostMapping("/cocktails")
    @ResponseStatus(HttpStatus.CREATED)
    public Cocktail addCocktail(@RequestBody Cocktail cocktail) {
        return menuService.createCocktail(cocktail);
    }
}
