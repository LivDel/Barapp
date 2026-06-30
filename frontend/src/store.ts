import { reactive } from 'vue'
import type { Cocktail } from './types'

/**
 * STORE GLOBAL (État partagé de l'application)
 * 
 * Plutôt que d'installer une grosse librairie externe complexe (Vuex, Pinia), 
 * j'utilise la fonction 'reactive' native de Vue 3. 
 * C'est l'approche la plus simple, moderne et légère pour stocker les données 
 * (Panier + Numéro de table) qui doivent rester en mémoire quand on change de page.
 */
export const store = reactive({
  numeroTable: null as number | null,

  // Le panier contient la liste des cocktails sélectionnés par le client
  cart: [] as Cocktail[],

  // Session du barman (s'il est connecté)
  barmakerUser: null as { identifiant: string, nom: string } | null
})

// --- FONCTIONS CLIENT ---

// Fonctions utilitaires pour manipuler le panier depuis n'importe quel composant
export const addToCart = (cocktail: Cocktail) => {
  store.cart.push(cocktail)
}

export const clearCart = () => {
  store.cart = []
}

// --- FONCTIONS BARMAKER ---
export const setBarmakerUser = (identifiant: string, nom: string) => {
  store.barmakerUser = { identifiant, nom }
}

export const logoutBarmaker = () => {
  store.barmakerUser = null
}
