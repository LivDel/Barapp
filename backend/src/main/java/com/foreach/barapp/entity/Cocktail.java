package com.foreach.barapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité principale représentant un Cocktail de la carte.
 */
@Entity
@Table(name = "\"Cocktail\"")
@Data
@NoArgsConstructor
public class Cocktail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cocktail")
    private Integer idCocktail;

    private String nom;
    private String description;
    private String image;

    // @ManyToOne indique que plusieurs cocktails peuvent appartenir à la même catégorie.
    @ManyToOne
    @JoinColumn(name = "id_categorie")
    private Categorie categorie;

    // @ManyToMany gère directement la table de jointure "Cocktail_Ingredient" car elle n'a pas d'autres attributs.
    @ManyToMany
    @JoinTable(
        name = "\"Cocktail_Ingredient\"",
        joinColumns = @JoinColumn(name = "id_cocktail"),
        inverseJoinColumns = @JoinColumn(name = "id_ingredient")
    )
    private List<Ingredient> ingredients = new ArrayList<>();

    // On ajoute cette relation pour qu'en chargeant un Cocktail, on ait aussi automatiquement ses prix selon la taille
    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL)
    private List<CocktailTaille> prixTailles = new ArrayList<>();
}
