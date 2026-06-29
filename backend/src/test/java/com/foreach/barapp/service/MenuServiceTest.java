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
}
