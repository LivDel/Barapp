package com.foreach.barapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité représentant la taille d'un verre (ex: S, M, L).
 */
@Entity
@Table(name = "\"Taille\"")
@Data
@NoArgsConstructor
public class Taille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_taille")
    private Integer idTaille;

    private String libelle;
}
