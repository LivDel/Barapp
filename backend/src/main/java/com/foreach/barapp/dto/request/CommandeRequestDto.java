package com.foreach.barapp.dto.request;

import lombok.Data;
import java.util.List;

/**
 * Objet de Transfert de Données (DTO).
 * 
 * Ce DTO est utilisé UNIQUEMENT pour réceptionner le JSON envoyé par le client
 * VueJS
 * lorsqu'il valide son panier.
 * Plutôt que de recevoir des entités complexes entières, on ne demande que le
 * strict minimum :
 * le numéro de la table, et une liste contenant les identifiants des cocktails
 * choisis et leur taille.
 */
@Data
public class CommandeRequestDto {

    private Integer numeroTable;

    private List<CocktailCommandeDto> cocktails;

    /**
     * Sous-classe statique représentant un élément du panier
     */
    @Data
    public static class CocktailCommandeDto {
        private Integer idCocktail;
        private Integer idTaille;
    }
}
