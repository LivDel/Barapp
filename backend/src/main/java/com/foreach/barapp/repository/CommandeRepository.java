package com.foreach.barapp.repository;

import com.foreach.barapp.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository pour accéder aux Commandes globales.
 */
@Repository
public interface CommandeRepository extends JpaRepository<Commande, Integer> {
    
    /**
     * Méthode générée automatiquement par Spring.
     * Utile pour le Barmaker qui veut lister les commandes "à traiter" (donc celles qui ne sont pas "Terminées").
     */
    List<Commande> findByStatutGlobalNot(String statut);
}
