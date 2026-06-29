package com.foreach.barapp.repository;

import com.foreach.barapp.entity.Taille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour accéder aux données des Tailles.
 */
@Repository
public interface TailleRepository extends JpaRepository<Taille, Integer> {
}
