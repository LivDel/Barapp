import api from './api'
import type { Cocktail, Categorie } from '../types'

/**
 * Service gérant la récupération des données de la carte.
 * 
 * Isoler les requêtes HTTP dans ce service permet 
 * de garder les vues (composants Vue) très propres et uniquement concentrées sur l'affichage.
 * On respecte le principe de "Séparation des responsabilités".
 */
export const MenuService = {
  // Récupère la liste de tous les cocktails depuis l'API Java
  async getAllCocktails(): Promise<Cocktail[]> {
    const response = await api.get<Cocktail[]>('/menu/cocktails')
    return response.data
  },

  // Récupérer les catégories
  async getCategories(): Promise<Categorie[]> {
    const response = await api.get<Categorie[]>('/menu/categories')
    return response.data
  },

  // Ajouter une catégorie (Admin)
  async createCategorie(categorie: Partial<Categorie>): Promise<Categorie> {
    const response = await api.post<Categorie>('/menu/categories', categorie)
    return response.data
  },

  // Récupérer les tailles
  async getTailles(): Promise<Taille[]> {
    const response = await api.get<Taille[]>('/menu/tailles')
    return response.data
  },

  // Ajouter une taille (Admin)
  async createTaille(taille: Partial<Taille>): Promise<Taille> {
    const response = await api.post<Taille>('/menu/tailles', taille)
    return response.data
  },

  // Récupérer les ingrédients
  async getIngredients(): Promise<Ingredient[]> {
    const response = await api.get<Ingredient[]>('/menu/ingredients')
    return response.data
  },

  // Ajouter un cocktail (Admin)
  async createCocktail(cocktail: Partial<Cocktail>): Promise<Cocktail> {
    const response = await api.post<Cocktail>('/menu/cocktails', cocktail)
    return response.data
  }
}
