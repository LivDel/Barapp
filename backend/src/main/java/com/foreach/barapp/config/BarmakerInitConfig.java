package com.foreach.barapp.config;

import com.foreach.barapp.entity.Barmaker;
import com.foreach.barapp.repository.BarmakerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration pour initialiser des données de test au lancement.
 */
@Configuration
public class BarmakerInitConfig {

    @Bean
    CommandLineRunner initDatabase(BarmakerRepository repository) {
        return args -> {
            // Créer un barman par défaut si la table est vide pour faciliter les tests
            if (repository.count() == 0) {
                Barmaker barmaker = new Barmaker();
                barmaker.setNom("Livio");
                barmaker.setIdentifiant("barmaker1");
                barmaker.setMotDePasse("password123");
                repository.save(barmaker);
                System.out.println("✅ Barman de test créé : barmaker1 / password123");
            }
        };
    }
}
