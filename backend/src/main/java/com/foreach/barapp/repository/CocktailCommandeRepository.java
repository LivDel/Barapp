package com.foreach.barapp.repository;

import com.foreach.barapp.entity.CocktailCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository pour accéder aux lignes de commande (les cocktails dans une commande).
 */
@Repository
public interface CocktailCommandeRepository extends JpaRepository<CocktailCommande, Integer> {
    
    /**
     * Récupère tous les cocktails liés à une commande précise.
     * Spring génère la requête SQL à partir du nom de la méthode.
     */
    List<CocktailCommande> findByCommandeIdCommande(Integer idCommande);
}
