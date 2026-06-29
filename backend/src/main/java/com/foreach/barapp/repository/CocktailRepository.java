package com.foreach.barapp.repository;

import com.foreach.barapp.entity.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour accéder aux données des Cocktails.
 */
@Repository
public interface CocktailRepository extends JpaRepository<Cocktail, Integer> {
}
