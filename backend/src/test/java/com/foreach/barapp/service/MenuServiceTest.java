package com.foreach.barapp.service;

import com.foreach.barapp.entity.Categorie;
import com.foreach.barapp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * L'annotation @ExtendWith(MockitoExtension.class) dit à JUnit (le moteur de
 * test)
 * d'activer Mockito (le créateur de faux objets).
 */
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    // @Mock crée un "faux" repository. Il ne se connecte pas à la vraie base de
    // données.
    // Il fera exactement ce qu'on lui dit de faire dans le test.
    @Mock
    private CategorieRepository categorieRepository;
    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private CocktailRepository cocktailRepository;
    @Mock
    private TailleRepository tailleRepository;
    @Mock
    private CocktailTailleRepository cocktailTailleRepository;

    // @InjectMocks crée le vrai MenuService, et y insère ("injecte") tous les @Mock
    // créés ci-dessus.
    @InjectMocks
    private MenuService menuService;

    // Une donnée qui va nous servir pour les tests
    private Categorie categorieAlcoolise;

    // @BeforeEach : Cette méthode s'exécute avant CHAQUE test. C'est idéal pour
    // préparer des données de test.
    @BeforeEach
    void preparerDonnees() {
        categorieAlcoolise = new Categorie();
        categorieAlcoolise.setIdCategorie(1);
        categorieAlcoolise.setLibelle("Alcoolisé");
    }

    // @Test : Indique que cette méthode est un test unitaire.
    @Test
    void testGetAllCategories() {
        // ÉTAPE 1 : ARRANGE (Préparer)
        // On dit à notre faux repository : "Quand on t'appelle avec findAll(), renvoie
        // une liste contenant 'categorieAlcoolise'."
        when(categorieRepository.findAll()).thenReturn(Arrays.asList(categorieAlcoolise));

        // ÉTAPE 2 : ACT (Agir)
        // On appelle la vraie méthode de notre service. Elle va utiliser le faux
        // repository sans le savoir.
        List<Categorie> resultat = menuService.getAllCategories();

        // ÉTAPE 3 : ASSERT (Vérifier)
        // On vérifie que le résultat est bien celui attendu.
        assertEquals(1, resultat.size()); // Il doit y avoir 1 seul élément
        assertEquals("Alcoolisé", resultat.get(0).getLibelle()); // Le nom doit être "Alcoolisé"

        // On vérifie que la méthode findAll() du faux repository a bien été appelée
        // exactement 1 fois.
        verify(categorieRepository, times(1)).findAll();
    }

    @Test
    void testCreateCategorie() {
        // ARRANGE
        // On dit : "Quand on te demande de sauvegarder n'importe quelle catégorie
        // (any), renvoie 'categorieAlcoolise'."
        when(categorieRepository.save(any(Categorie.class))).thenReturn(categorieAlcoolise);

        // ACT
        Categorie nouvelleCategorie = new Categorie();
        nouvelleCategorie.setLibelle("Alcoolisé");
        Categorie resultat = menuService.createCategorie(nouvelleCategorie);

        // ASSERT
        assertEquals("Alcoolisé", resultat.getLibelle());
        verify(categorieRepository, times(1)).save(any(Categorie.class));
    }

    @Test
    void testGetAllIngredients() {
        com.foreach.barapp.entity.Ingredient ingredient = new com.foreach.barapp.entity.Ingredient();
        ingredient.setNom("Menthe");
        when(ingredientRepository.findAll()).thenReturn(Arrays.asList(ingredient));

        List<com.foreach.barapp.entity.Ingredient> resultat = menuService.getAllIngredients();

        assertEquals(1, resultat.size());
        assertEquals("Menthe", resultat.get(0).getNom());
        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    void testCreateIngredient() {
        com.foreach.barapp.entity.Ingredient ingredient = new com.foreach.barapp.entity.Ingredient();
        ingredient.setNom("Rhum");
        when(ingredientRepository.save(any())).thenReturn(ingredient);

        com.foreach.barapp.entity.Ingredient resultat = menuService.createIngredient(ingredient);

        assertEquals("Rhum", resultat.getNom());
        verify(ingredientRepository, times(1)).save(any());
    }

    @Test
    void testGetAllTailles() {
        com.foreach.barapp.entity.Taille taille = new com.foreach.barapp.entity.Taille();
        taille.setLibelle("M");
        when(tailleRepository.findAll()).thenReturn(Arrays.asList(taille));

        List<com.foreach.barapp.entity.Taille> resultat = menuService.getAllTailles();

        assertEquals(1, resultat.size());
        assertEquals("M", resultat.get(0).getLibelle());
        verify(tailleRepository, times(1)).findAll();
    }

    @Test
    void testCreateTaille() {
        com.foreach.barapp.entity.Taille taille = new com.foreach.barapp.entity.Taille();
        taille.setLibelle("L");
        when(tailleRepository.save(any())).thenReturn(taille);

        com.foreach.barapp.entity.Taille resultat = menuService.createTaille(taille);

        assertEquals("L", resultat.getLibelle());
        verify(tailleRepository, times(1)).save(any());
    }

    @Test
    void testGetAllCocktails() {
        com.foreach.barapp.entity.Cocktail cocktail = new com.foreach.barapp.entity.Cocktail();
        cocktail.setNom("Mojito");
        when(cocktailRepository.findAll()).thenReturn(Arrays.asList(cocktail));

        List<com.foreach.barapp.entity.Cocktail> resultat = menuService.getAllCocktails();

        assertEquals(1, resultat.size());
        assertEquals("Mojito", resultat.get(0).getNom());
        verify(cocktailRepository, times(1)).findAll();
    }

    @Test
    void testCreateCocktail() {
        // Préparer un cocktail
        com.foreach.barapp.entity.Cocktail cocktail = new com.foreach.barapp.entity.Cocktail();
        cocktail.setNom("Mojito");

        // Préparer un prix pour une taille
        com.foreach.barapp.entity.CocktailTaille ct = new com.foreach.barapp.entity.CocktailTaille();
        ct.setPrix(8.5);
        cocktail.setPrixTailles(Arrays.asList(ct));

        com.foreach.barapp.entity.Cocktail savedCocktail = new com.foreach.barapp.entity.Cocktail();
        savedCocktail.setIdCocktail(1);
        savedCocktail.setNom("Mojito");
        savedCocktail.setPrixTailles(Arrays.asList(ct));

        when(cocktailRepository.save(any())).thenReturn(savedCocktail);

        com.foreach.barapp.entity.Cocktail resultat = menuService.createCocktail(cocktail);

        assertEquals("Mojito", resultat.getNom());
        // On vérifie que cocktailRepository.save a été appelé
        verify(cocktailRepository, times(1)).save(any());
        // On vérifie que cocktailTailleRepository.save a bien été appelé pour chaque prixTaille
        verify(cocktailTailleRepository, times(1)).save(any());
    }
}
