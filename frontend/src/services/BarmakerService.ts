import api from './api'
import type { Commande, CocktailCommande } from '../types'

/**
 * Service gérant les requêtes du Barman
 */
export const BarmakerService = {
  // Tente de connecter le barman
  async login(identifiant: string, motDePasse: string) {
    const response = await api.post('/barmaker/login', { identifiant, motDePasse })
    return response.data
  },

  // Récupère toutes les commandes actives (Non terminées) en temps réel
  async getCommandesATraiter(): Promise<Commande[]> {
    const response = await api.get<Commande[]>('/barmaker/commandes')
    return response.data
  },

  // Fait avancer le statut d'un verre spécifique (ex: En attente -> Préparation)
  async faireAvancerStatut(idCocktailCommande: number): Promise<CocktailCommande> {
    const response = await api.put<CocktailCommande>(`/barmaker/cocktails-commandes/${idCocktailCommande}/statut`)
    return response.data
  }
}
