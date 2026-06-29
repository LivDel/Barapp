/**
 * Types TypeScript (Interfaces)
 * 
 * Explication soutenance : Ces interfaces garantissent que le Front-end s'attend
 * exactement au même format de données que celui renvoyé par l'API Spring Boot.
 * C'est la force de TypeScript : s'assurer qu'on ne demande pas 'cocktail.title' 
 * si la BDD l'a appelé 'cocktail.nom'.
 */

export interface Categorie {
  idCategorie: number;
  nom: string;
}

export interface Taille {
  idTaille: number;
  nom: string;
}

export interface Cocktail {
  idCocktail: number;
  nom: string;
  recette: string;
  illustration: string;
  categorie: Categorie;
}

// Ligne de commande : un verre spécifique dans une commande
export interface CocktailCommande {
  idCocktailCommande: number;
  statutPreparation: string; // "En attente", "Préparation des Ingrédients", "Assemblage", "Dressage", "Terminée"
  cocktail?: Cocktail; 
  taille?: Taille;
}

export interface Commande {
  idCommande: number;
  numeroTable: number;
  dateHeureCreation: string; // Les dates Java ressortent souvent sous forme de chaîne de caractères (String ISO)
  statutGlobal: string; // "Commandée", "En cours de préparation", "Terminée"
  cocktailsCommandes: CocktailCommande[]; // La liste magique qu'on a créée avec @OneToMany côté Java !
}

// Le format exact (DTO) attendu par notre API Java pour passer commande
export interface CommandeRequestDto {
  numeroTable: number;
  cocktails: Array<{
    idCocktail: number;
    idTaille: number;
  }>;
}
