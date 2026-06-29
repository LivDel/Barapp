package com.foreach.barapp.service;

import com.foreach.barapp.entity.CocktailCommande;
import com.foreach.barapp.entity.Commande;
import com.foreach.barapp.repository.CocktailCommandeRepository;
import com.foreach.barapp.repository.CommandeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarmakerServiceTest {

    @Mock
    private CommandeRepository commandeRepository;
    @Mock
    private CocktailCommandeRepository cocktailCommandeRepository;

    @InjectMocks
    private BarmakerService barmakerService;

    @Test
    void testFaireAvancerStatut() {
        // 1. ARRANGE
        CocktailCommande ligne = new CocktailCommande();
        ligne.setIdCocktailCommande(1);
        ligne.setStatutPreparation("En attente");

        Commande globale = new Commande();
        globale.setStatutGlobal("Commandée");
        globale.getCocktailsCommandes().add(ligne);
        ligne.setCommande(globale);

        when(cocktailCommandeRepository.findById(1)).thenReturn(Optional.of(ligne));
        when(cocktailCommandeRepository.save(any(CocktailCommande.class))).thenReturn(ligne);
        when(commandeRepository.save(any(Commande.class))).thenReturn(globale);

        // 2. ACT : On demande de faire avancer le statut
        CocktailCommande result = barmakerService.faireAvancerStatut(1);

        // 3. ASSERT : Le statut doit être passé à l'étape suivante, et la commande globale passée 'En cours'
        assertEquals("Préparation des Ingrédients", result.getStatutPreparation());
        assertEquals("En cours de préparation", globale.getStatutGlobal());
        verify(cocktailCommandeRepository, times(1)).save(ligne);
        verify(commandeRepository, times(1)).save(globale);
    }
}
