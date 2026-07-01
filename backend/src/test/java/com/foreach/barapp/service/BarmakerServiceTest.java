package com.foreach.barapp.service;

import com.foreach.barapp.entity.Barmaker;
import com.foreach.barapp.entity.CocktailCommande;
import com.foreach.barapp.entity.Commande;
import com.foreach.barapp.exception.ResourceNotFoundException;
import com.foreach.barapp.repository.BarmakerRepository;
import com.foreach.barapp.repository.CocktailCommandeRepository;
import com.foreach.barapp.repository.CommandeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarmakerServiceTest {

    @Mock
    private CommandeRepository commandeRepository;
    @Mock
    private CocktailCommandeRepository cocktailCommandeRepository;
    @Mock
    private BarmakerRepository barmakerRepository;

    @InjectMocks
    private BarmakerService barmakerService;

    @Test
    void testLogin_Success() {
        Barmaker barmaker = new Barmaker();
        barmaker.setIdentifiant("admin");
        when(barmakerRepository.findByIdentifiantAndMotDePasse("admin", "pass")).thenReturn(Optional.of(barmaker));

        Barmaker result = barmakerService.login("admin", "pass");

        assertNotNull(result);
        assertEquals("admin", result.getIdentifiant());
    }

    @Test
    void testLogin_Fail() {
        when(barmakerRepository.findByIdentifiantAndMotDePasse("admin", "wrong")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            barmakerService.login("admin", "wrong");
        });
    }

    @Test
    void testGetCommandesActives() {
        Commande cmd = new Commande();
        cmd.setStatutGlobal("Commandée");
        when(commandeRepository.findByStatutGlobalNot("Terminée")).thenReturn(Arrays.asList(cmd));

        List<Commande> result = barmakerService.getCommandesActives();

        assertEquals(1, result.size());
    }

    @Test
    void testFaireAvancerStatut_NotFound() {
        when(cocktailCommandeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            barmakerService.faireAvancerStatut(99);
        });
    }

    @Test
    void testFaireAvancerStatut_EnAttente_To_Preparation() {
        CocktailCommande ligne = new CocktailCommande();
        ligne.setId(1);
        ligne.setStatutPreparation("En attente");

        Commande commande = new Commande();
        commande.setStatutGlobal("Commandée");
        commande.setCocktailsCommandes(Arrays.asList(ligne));
        ligne.setCommande(commande);

        when(cocktailCommandeRepository.findById(1)).thenReturn(Optional.of(ligne));

        CocktailCommande result = barmakerService.faireAvancerStatut(1);

        assertEquals("Préparation des Ingrédients", result.getStatutPreparation());
        assertEquals("En cours de préparation", commande.getStatutGlobal());
        verify(cocktailCommandeRepository, times(1)).save(ligne);
        verify(commandeRepository, times(1)).save(commande);
    }

    @Test
    void testFaireAvancerStatut_Dressage_To_Terminee_Global() {
        CocktailCommande ligne = new CocktailCommande();
        ligne.setId(1);
        ligne.setStatutPreparation("Dressage");

        Commande commande = new Commande();
        commande.setStatutGlobal("En cours de préparation");
        commande.setCocktailsCommandes(new ArrayList<>(Arrays.asList(ligne)));
        ligne.setCommande(commande);

        when(cocktailCommandeRepository.findById(1)).thenReturn(Optional.of(ligne));

        CocktailCommande result = barmakerService.faireAvancerStatut(1);

        assertEquals("Terminée", result.getStatutPreparation());
        assertEquals("Terminée", commande.getStatutGlobal());
        verify(cocktailCommandeRepository, times(1)).save(ligne);
        verify(commandeRepository, times(1)).save(commande);
    }
}
