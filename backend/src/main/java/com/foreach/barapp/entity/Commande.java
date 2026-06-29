package com.foreach.barapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant une commande globale d'un client.
 */
@Entity
@Table(name = "\"Commande\"")
@Data
@NoArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_commande")
    private Integer idCommande;

    @Column(name = "date_heure_creation")
    private LocalDateTime dateHeureCreation;

    @Column(name = "statut_global")
    private String statutGlobal; // On stockera : "Commandée", "En cours de préparation", "Terminée"

    @Column(name = "numero_table")
    private Integer numeroTable;

    // Ajout d'une relation pour qu'en appelant une Commande, Spring nous donne automatiquement la liste des cocktails dedans.
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<CocktailCommande> cocktailsCommandes = new ArrayList<>();
}
