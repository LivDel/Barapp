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

  // Récupère la liste des catégories (ex: Alcoolisé, Sans alcool) pour les filtres
  async getCategories(): Promise<Categorie[]> {
    const response = await api.get<Categorie[]>('/menu/categories')
    return response.data
  }
}
