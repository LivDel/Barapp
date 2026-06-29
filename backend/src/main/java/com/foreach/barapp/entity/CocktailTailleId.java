package com.foreach.barapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * Classe représentant la clé primaire composite de la table Cocktail_Taille.
 * @Embeddable indique que cette classe peut être imbriquée (embedded) comme clé primaire dans une autre entité.
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CocktailTailleId implements Serializable {

    @Column(name = "id_cocktail")
    private Integer idCocktail;

    @Column(name = "id_taille")
    private Integer idTaille;
}
