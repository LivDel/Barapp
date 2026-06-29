package com.foreach.barapp.repository;

import com.foreach.barapp.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour accéder aux données des Ingrédients.
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
}
