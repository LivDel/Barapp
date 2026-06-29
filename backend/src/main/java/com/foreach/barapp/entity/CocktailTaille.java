package com.foreach.barapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entité de jointure entre Cocktail et Taille, avec un attribut supplémentaire
 * : "prix".
 */
@Entity
@Table(name = "\"Cocktail_Taille\"")
@Data
@NoArgsConstructor
public class CocktailTaille {

    // @EmbeddedId indique qu'on utilise une clé composite définie dans la classe
    // CocktailTailleId.
    @EmbeddedId
    private CocktailTailleId id = new CocktailTailleId();

    // @MapsId indique que les relations ManyToOne ci-dessous remplissent les
    // attributs de la clé composite 'id'
    @JsonIgnore // Empêche la boucle infinie (Cocktail -> Prix -> Cocktail -> Prix...) lors de
                // la génération du JSON
    @ManyToOne
    @MapsId("idCocktail")
    @JoinColumn(name = "id_cocktail")
    private Cocktail cocktail;

    @ManyToOne
    @MapsId("idTaille")
    @JoinColumn(name = "id_taille")
    private Taille taille;

    private Double prix;
}
