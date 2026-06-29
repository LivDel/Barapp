package com.foreach.barapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité représentant la catégorie d'un cocktail (ex: Alcoolisé, Sans Alcool).
 */
@Entity
@Table(name = "\"Categorie\"")
@Data
@NoArgsConstructor
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categorie")
    private Integer idCategorie;

    private String libelle;
}
