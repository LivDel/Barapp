package com.foreach.barapp.repository;

import com.foreach.barapp.entity.CocktailTaille;
import com.foreach.barapp.entity.CocktailTailleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour accéder aux prix des tailles d'un cocktail.
 * La clé primaire est composite (CocktailTailleId).
 */
@Repository
public interface CocktailTailleRepository extends JpaRepository<CocktailTaille, CocktailTailleId> {
}
