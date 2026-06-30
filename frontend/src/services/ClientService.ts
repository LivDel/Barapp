import api from './api'
import type { CommandeRequestDto, Commande } from '../types'

/**
 * Service gérant les actions du client (Prise de commande, Suivi)
 */
export const ClientService = {
  // Envoie le DTO (le panier) au backend pour créer la commande dans la base de données
  async lancerCommande(request: CommandeRequestDto): Promise<Commande> {
    const response = await api.post<Commande>('/client/commandes', request)
    return response.data
  },

  // Récupère une commande par son ID pour voir l'évolution de ses statuts (Suivi en temps réel)
  async getCommandeDetails(idCommande: number): Promise<Commande> {
    const response = await api.get<Commande>(`/client/commandes/${idCommande}`)
    return response.data
  }
}
