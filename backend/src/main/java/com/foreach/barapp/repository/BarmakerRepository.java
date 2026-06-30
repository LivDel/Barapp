package com.foreach.barapp.repository;

import com.foreach.barapp.entity.Barmaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour accéder aux données des Barmakers dans la base.
 * JpaRepository fournit déjà save(), findById(), findAll(), deleteById(), etc.
 */
@Repository
public interface BarmakerRepository extends JpaRepository<Barmaker, Integer> {
    // Spring Data JPA génère automatiquement la requête SQL correspondant à ce nom de méthode !
    java.util.Optional<Barmaker> findByIdentifiantAndMotDePasse(String identifiant, String motDePasse);
}
