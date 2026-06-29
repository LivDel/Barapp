package com.foreach.barapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

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
}
