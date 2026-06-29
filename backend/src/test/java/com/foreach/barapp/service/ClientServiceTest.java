package com.foreach.barapp.service;

import com.foreach.barapp.dto.request.CommandeRequestDto;
import com.foreach.barapp.entity.Cocktail;
import com.foreach.barapp.entity.CocktailCommande;
import com.foreach.barapp.entity.Commande;
import com.foreach.barapp.entity.Taille;
import com.foreach.barapp.repository.CocktailCommandeRepository;
import com.foreach.barapp.repository.CocktailRepository;
import com.foreach.barapp.repository.CommandeRepository;
import com.foreach.barapp.repository.TailleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private CommandeRepository commandeRepository;
    @Mock
    private CocktailCommandeRepository cocktailCommandeRepository;
    @Mock
    private CocktailRepository cocktailRepository;
    @Mock
    private TailleRepository tailleRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void testLancerCommande() {
        // 1. ARRANGE : On prépare le faux panier (DTO)
        CommandeRequestDto request = new CommandeRequestDto();
        request.setNumeroTable(5);
        
        CommandeRequestDto.CocktailCommandeDto ccDto = new CommandeRequestDto.CocktailCommandeDto();
        ccDto.setIdCocktail(1);
        ccDto.setIdTaille(1);
        request.setCocktails(Collections.singletonList(ccDto));

        // On prépare les faux objets retournés par les fausses requêtes BDD
        Commande fakeCommande = new Commande();
        fakeCommande.setIdCommande(10);
        fakeCommande.setStatutGlobal("Commandée");

        when(commandeRepository.save(any(Commande.class))).thenReturn(fakeCommande);
        when(cocktailRepository.findById(1)).thenReturn(Optional.of(new Cocktail()));
        when(tailleRepository.findById(1)).thenReturn(Optional.of(new Taille()));
        when(cocktailCommandeRepository.save(any(CocktailCommande.class))).thenReturn(new CocktailCommande());

        // 2. ACT : On lance la vraie méthode
        Commande result = clientService.lancerCommande(request);

        // 3. ASSERT : On vérifie que tout s'est bien passé
        assertNotNull(result);
        assertEquals("Commandée", result.getStatutGlobal());
        verify(commandeRepository, times(1)).save(any(Commande.class));
        verify(cocktailCommandeRepository, times(1)).save(any(CocktailCommande.class));
    }
}
