<template>
  <div class="cart-page">
    <header class="header">
      <button class="back-btn" @click="router.push('/menu')">⬅ Retour au menu</button>
      <h1>Votre Panier 🛒</h1>
    </header>

    <div v-if="store.cart.length === 0" class="empty-cart">
      <p>Votre panier est vide.</p>
      <button @click="router.push('/menu')" class="btn-primary">Voir la carte</button>
    </div>

    <div v-else class="cart-content">
      <div class="table-info">
        Table N° {{ store.numeroTable }}
      </div>

      <ul class="cart-list">
        <!-- On itère sur le state global (store) pour afficher le contenu -->
        <li v-for="(cocktail, index) in store.cart" :key="index" class="cart-item">
          <span>🍹 {{ cocktail.nom }}</span>
          <!-- On permet de retirer un article en passant son index dans le tableau -->
          <button @click="removeFromCart(index)" class="btn-remove" title="Retirer cet article">❌</button>
        </li>
      </ul>

      <button class="btn-primary btn-order" @click="validerCommande" :disabled="isSubmitting">
        {{ isSubmitting ? 'Envoi en cours...' : 'Valider la commande' }}
      </button>

      <p v-if="errorMessage" class="error-msg">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { store, clearCart } from '../../store'
import { ClientService } from '../../services/ClientService'
import type { CommandeRequestDto } from '../../types'

const router = useRouter()
const isSubmitting = ref(false)
const errorMessage = ref('')

const removeFromCart = (index: number) => {
  store.cart.splice(index, 1)
}

// Fonction centrale pour l'examen !
const validerCommande = async () => {
  if (!store.numeroTable || store.cart.length === 0) return

  isSubmitting.value = true
  errorMessage.value = ''

  try {
    // 1. On fabrique le DTO attendu par Spring Boot
    // On "map" notre panier front-end vers le modèle strict de l'API.
    const requestDto: CommandeRequestDto = {
      numeroTable: store.numeroTable,
      cocktails: store.cart.map(c => ({
        idCocktail: c.idCocktail,
        idTaille: 1 // On met la taille Standard (ID 1) par défaut pour la démo
      }))
    }

    // 2. On envoie la requête POST !
    const commandeCree = await ClientService.lancerCommande(requestDto)

    // 3. Succès : On vide le panier local et on redirige vers l'URL de suivi (ex: /tracking/5)
    clearCart()
    router.push(`/tracking/${commandeCree.idCommande}`)

  } catch (error) {
    console.error("Erreur de commande :", error)
    errorMessage.value = "Impossible de valider la commande. Le serveur est-il allumé ?"
  } finally {
    isSubmitting.value = false
  }
}
</script>

<style scoped>
.cart-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.header {
  margin-bottom: 30px;
}

.back-btn {
  background: none;
  border: none;
  color: #57606f;
  font-weight: bold;
  cursor: pointer;
  padding: 0;
  margin-bottom: 15px;
  font-size: 1rem;
}

h1 {
  color: #2f3542;
  font-size: 2rem;
  margin: 0;
}

.table-info {
  background: #dfe4ea;
  color: #2f3542;
  padding: 15px;
  border-radius: 8px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
}

.cart-list {
  list-style: none;
  padding: 0;
  margin: 0 0 30px 0;
  border: 1px solid #f1f2f6;
  border-radius: 8px;
  background: white;
}

.cart-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #f1f2f6;
  font-size: 1.1rem;
}

.cart-item:last-child {
  border-bottom: none;
}

.btn-remove {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.2rem;
  opacity: 0.7;
  transition: opacity 0.2s;
}

.btn-remove:hover {
  opacity: 1;
}

.btn-primary {
  background: #2ed573;
  color: white;
  border: none;
  padding: 15px 30px;
  border-radius: 8px;
  font-weight: bold;
  font-size: 1.1rem;
  cursor: pointer;
  width: 100%;
  transition: background 0.2s;
  box-shadow: 0 4px 6px rgba(46, 213, 115, 0.3);
}

.btn-primary:hover {
  background: #26de81;
}

.btn-primary:disabled {
  background: #a4b0be;
  cursor: not-allowed;
  box-shadow: none;
}

.empty-cart {
  text-align: center;
  padding: 60px 0;
  color: #747d8c;
}

.empty-cart p {
  font-size: 1.2rem;
  margin-bottom: 20px;
}

.error-msg {
  color: #ff4757;
  text-align: center;
  margin-top: 15px;
  font-weight: bold;
}
</style>
