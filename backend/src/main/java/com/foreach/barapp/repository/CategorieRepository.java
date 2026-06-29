package com.foreach.barapp.repository;

import com.foreach.barapp.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour accéder aux données des Catégories.
 */
@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Integer> {
}
