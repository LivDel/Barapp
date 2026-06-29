package com.foreach.barapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entité représentant un cocktail spécifique commandé au sein d'une commande (Ligne de commande).
 * Elle suit sa propre évolution d'état.
 */
@Entity
@Table(name = "\"Cocktail_Commande\"")
@Data
@NoArgsConstructor
public class CocktailCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cocktail_commande")
    private Integer idCocktailCommande;

    @Column(name = "statut_preparation")
    private String statutPreparation; // On stockera : "Préparation des Ingrédients", "Assemblage", "Dressage", "Terminée"

    @JsonIgnore // Empêche la boucle : Commande -> Ligne de Commande -> Commande -> ... dans le JSON
    @ManyToOne
    @JoinColumn(name = "id_commande")
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "id_cocktail")
    private Cocktail cocktail;

    @ManyToOne
    @JoinColumn(name = "id_taille")
    private Taille taille;
}
