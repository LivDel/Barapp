package com.foreach.barapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité représentant un ingrédient possible pour un cocktail.
 */
@Entity
@Table(name = "\"Ingredient\"")
@Data
@NoArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingredient")
    private Integer idIngredient;

    private String nom;
}
