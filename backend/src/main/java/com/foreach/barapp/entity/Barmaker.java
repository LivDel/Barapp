package com.foreach.barapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité représentant un barman (employé).
 * @Data de Lombok génère automatiquement les getters, setters, toString, etc.
 * @NoArgsConstructor génère le constructeur vide obligatoire pour JPA.
 */
@Entity
@Table(name = "\"Barmaker\"") // Les guillemets forcent PostgreSQL à respecter la casse exacte de ton script SQL
@Data
@NoArgsConstructor
public class Barmaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_barmaker")
    private Integer idBarmaker;

    private String nom;
    private String identifiant;
    
    @Column(name = "mot_de_passe")
    private String motDePasse;
}
