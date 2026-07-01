import { reactive, watch } from 'vue'
import type { Cocktail, CartItem } from './types'

/**
 * STORE GLOBAL (État partagé de l'application)
 * 
 * Plutôt que d'installer une grosse librairie externe complexe (Vuex, Pinia), 
 * j'utilise la fonction 'reactive' native de Vue 3. 
 * C'est l'approche la plus simple, moderne et légère pour stocker les données 
 * (Panier + Numéro de table) qui doivent rester en mémoire quand on change de page.
 */
// 1. On essaye de récupérer les données sauvegardées (s'il y en a)
const savedState = localStorage.getItem('barapp-store')
const initialState = savedState ? JSON.parse(savedState) : {
  numeroTable: null,
  cart: [],
  barmakerUser: null,
  lastOrderId: null
}

export const store = reactive({
  numeroTable: initialState.numeroTable as number | null,
  lastOrderId: initialState.lastOrderId as number | null,

  // Le panier contient la liste des cocktails sélectionnés par le client avec leur taille et prix
  cart: initialState.cart as CartItem[],

  // Session du barman (s'il est connecté)
  barmakerUser: initialState.barmakerUser as { identifiant: string, nom: string } | null
})

// 2. Magie de Vue : à chaque fois que le store change, on le sauvegarde dans le navigateur !
// Comme ça, même si on actualise la page (F5), on ne perd ni le numéro de table ni le panier.
watch(() => store, (newState) => {
  localStorage.setItem('barapp-store', JSON.stringify(newState))
}, { deep: true })

// --- FONCTIONS CLIENT ---

// Fonctions utilitaires pour manipuler le panier depuis n'importe quel composant
export const addToCart = (item: CartItem) => {
  store.cart.push(item)
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
