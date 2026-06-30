package com.foreach.barapp.config;

import com.foreach.barapp.entity.Categorie;
import com.foreach.barapp.entity.Cocktail;
import com.foreach.barapp.entity.CocktailTaille;
import com.foreach.barapp.entity.Taille;
import com.foreach.barapp.repository.CategorieRepository;
import com.foreach.barapp.repository.CocktailRepository;
import com.foreach.barapp.repository.CocktailTailleRepository;
import com.foreach.barapp.repository.TailleRepository;
import com.foreach.barapp.entity.Ingredient;
import com.foreach.barapp.repository.IngredientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class DataInitConfig {

    @Bean
    public CommandLineRunner initMenuData(CategorieRepository categorieRepo,
                                          TailleRepository tailleRepo,
                                          CocktailRepository cocktailRepo,
                                          CocktailTailleRepository cocktailTailleRepo,
                                          IngredientRepository ingredientRepo) {
        return args -> {
            // On ne peuple la BDD que si la table des cocktails est vide
            if (cocktailRepo.count() == 0) {
                System.out.println("🍹 Initialisation de la carte du Bar'app...");

                // 1. Création des Catégories
                Categorie catSignature = new Categorie();
                catSignature.setLibelle("Signatures");
                catSignature = categorieRepo.save(catSignature);

                Categorie catClassique = new Categorie();
                catClassique.setLibelle("Classiques");
                catClassique = categorieRepo.save(catClassique);

                Categorie catSansAlcool = new Categorie();
                catSansAlcool.setLibelle("Sans Alcool");
                catSansAlcool = categorieRepo.save(catSansAlcool);

                // 2. Création des Tailles
                Taille tailleS = new Taille();
                tailleS.setLibelle("S (25cl)");
                tailleS = tailleRepo.save(tailleS);

                Taille tailleM = new Taille();
                tailleM.setLibelle("M (33cl)");
                tailleM = tailleRepo.save(tailleM);

                Taille tailleL = new Taille();
                tailleL.setLibelle("L (50cl)");
                tailleL = tailleRepo.save(tailleL);

                // Création des Ingrédients
                Ingredient vodka = ingredientRepo.save(creerIngredient("Vodka"));
                Ingredient rhum = ingredientRepo.save(creerIngredient("Rhum blanc"));
                Ingredient violette = ingredientRepo.save(creerIngredient("Liqueur de violette"));
                Ingredient curacao = ingredientRepo.save(creerIngredient("Curaçao bleu"));
                Ingredient limonade = ingredientRepo.save(creerIngredient("Limonade"));
                Ingredient citronVert = ingredientRepo.save(creerIngredient("Citron vert"));
                Ingredient menthe = ingredientRepo.save(creerIngredient("Menthe fraîche"));
                Ingredient framboise = ingredientRepo.save(creerIngredient("Framboises pilées"));
                Ingredient eauGazeuse = ingredientRepo.save(creerIngredient("Eau pétillante"));
                Ingredient jusOrange = ingredientRepo.save(creerIngredient("Jus d'orange pressé"));
                Ingredient grenadine = ingredientRepo.save(creerIngredient("Sirop de grenadine"));
                Ingredient zesteCitron = ingredientRepo.save(creerIngredient("Zeste de citron"));

                // 3. Création des Cocktails
                
                // Cocktail 1 : Purple Rain
                Cocktail purpleRain = new Cocktail();
                purpleRain.setNom("Purple Rain");
                purpleRain.setDescription("Notre signature visuelle : Vodka, Liqueur de violette, Curaçao bleu, Limonade fraîche.");
                purpleRain.setImage("https://images.unsplash.com/photo-1541546006121-5c3bd5816c4c?q=80&w=600&auto=format&fit=crop");
                purpleRain.setCategorie(catSignature);
                purpleRain.getIngredients().add(vodka);
                purpleRain.getIngredients().add(violette);
                purpleRain.getIngredients().add(curacao);
                purpleRain.getIngredients().add(limonade);
                purpleRain = cocktailRepo.save(purpleRain);

                // Cocktail 2 : Mojito Framboise
                Cocktail mojito = new Cocktail();
                mojito.setNom("Mojito Framboise");
                mojito.setDescription("Rhum blanc, Citron vert, Menthe fraîche, Framboises pilées, Eau pétillante.");
                mojito.setImage("https://images.unsplash.com/photo-1551024709-8f23befc6f87?q=80&w=600&auto=format&fit=crop");
                mojito.setCategorie(catClassique);
                mojito.getIngredients().add(rhum);
                mojito.getIngredients().add(citronVert);
                mojito.getIngredients().add(menthe);
                mojito.getIngredients().add(framboise);
                mojito.getIngredients().add(eauGazeuse);
                mojito = cocktailRepo.save(mojito);

                // Cocktail 3 : Virgin Sunrise
                Cocktail virgin = new Cocktail();
                virgin.setNom("Virgin Sunrise");
                virgin.setDescription("Jus d'orange pressé, Sirop de grenadine, Un zeste de citron. 100% de douceur.");
                virgin.setImage("https://images.unsplash.com/photo-1595981234058-a9302fc97ce8?q=80&w=600&auto=format&fit=crop");
                virgin.setCategorie(catSansAlcool);
                virgin.getIngredients().add(jusOrange);
                virgin.getIngredients().add(grenadine);
                virgin.getIngredients().add(zesteCitron);
                virgin = cocktailRepo.save(virgin);

                // 4. Association des prix par taille (CocktailTaille)
                
                // Purple Rain
                sauvegarderPrix(cocktailTailleRepo, purpleRain, tailleS, 8.0);
                sauvegarderPrix(cocktailTailleRepo, purpleRain, tailleM, 10.5);
                sauvegarderPrix(cocktailTailleRepo, purpleRain, tailleL, 13.0);

                // Mojito
                sauvegarderPrix(cocktailTailleRepo, mojito, tailleS, 7.5);
                sauvegarderPrix(cocktailTailleRepo, mojito, tailleM, 9.5);
                sauvegarderPrix(cocktailTailleRepo, mojito, tailleL, 12.0);

                // Virgin Sunrise
                sauvegarderPrix(cocktailTailleRepo, virgin, tailleS, 5.0);
                sauvegarderPrix(cocktailTailleRepo, virgin, tailleM, 7.0);
                sauvegarderPrix(cocktailTailleRepo, virgin, tailleL, 9.0);

                System.out.println("✅ Données initialisées avec succès !");
            }
        };
    }

    private void sauvegarderPrix(CocktailTailleRepository repo, Cocktail cocktail, Taille taille, Double prix) {
        CocktailTaille ct = new CocktailTaille();
        ct.setCocktail(cocktail);
        ct.setTaille(taille);
        ct.setPrix(prix);
        repo.save(ct);
    }

    private Ingredient creerIngredient(String nom) {
        Ingredient ing = new Ingredient();
        ing.setNom(nom);
        return ing;
    }
}
