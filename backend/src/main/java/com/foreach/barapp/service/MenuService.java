package com.foreach.barapp.service;

import com.foreach.barapp.entity.*;
import com.foreach.barapp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service gérant toute la logique métier liée à la Carte (Catégories, Ingrédients, Tailles, Cocktails).
 * L'annotation @Service indique à Spring d'instancier cette classe automatiquement (Injection de dépendances).
 */
@Service
public class MenuService {

    private final CategorieRepository categorieRepository;
    private final IngredientRepository ingredientRepository;
    private final CocktailRepository cocktailRepository;
    private final TailleRepository tailleRepository;
    private final CocktailTailleRepository cocktailTailleRepository;

    // L'injection de dépendances se fait via le constructeur
    public MenuService(CategorieRepository categorieRepository, 
                       IngredientRepository ingredientRepository, 
                       CocktailRepository cocktailRepository, 
                       TailleRepository tailleRepository, 
                       CocktailTailleRepository cocktailTailleRepository) {
        this.categorieRepository = categorieRepository;
        this.ingredientRepository = ingredientRepository;
        this.cocktailRepository = cocktailRepository;
        this.tailleRepository = tailleRepository;
        this.cocktailTailleRepository = cocktailTailleRepository;
    }

    // --- CATÉGORIES ---
    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    public Categorie createCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    // --- INGRÉDIENTS ---
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient createIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    // --- TAILLES ---
    public List<Taille> getAllTailles() {
        return tailleRepository.findAll();
    }

    public Taille createTaille(Taille taille) {
        return tailleRepository.save(taille);
    }

    // --- COCKTAILS ---
    public List<Cocktail> getAllCocktails() {
        return cocktailRepository.findAll();
    }

    /**
     * @Transactional garantit que si une erreur survient, rien n'est sauvegardé en base (rollback).
     */
    @Transactional
    public Cocktail createCocktail(Cocktail cocktail) {
        // 1. On sauvegarde d'abord le cocktail (sans les prix) pour qu'il obtienne son ID généré par PostgreSQL
        Cocktail savedCocktail = cocktailRepository.save(cocktail);
        
        // 2. S'il y a des prix renseignés pour différentes tailles, on les relie au cocktail sauvegardé
        if (cocktail.getPrixTailles() != null && !cocktail.getPrixTailles().isEmpty()) {
            for (CocktailTaille prixTaille : cocktail.getPrixTailles()) {
                // On attache le cocktail manuellement pour la relation bidirectionnelle
                prixTaille.setCocktail(savedCocktail); 
                // Sauvegarde de l'entité de jointure avec le prix
                cocktailTailleRepository.save(prixTaille);
            }
        }
        return savedCocktail;
    }
}
