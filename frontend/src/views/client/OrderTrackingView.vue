<template>
  <div class="tracking-page">
    <header class="header">
      <h1>Suivi de Commande 📦</h1>
      <p v-if="commande">Commande N°{{ commande.idCommande }} - Table {{ commande.numeroTable }}</p>
    </header>

    <div v-if="loading" class="loading">
      Recherche de votre commande... ⏳
    </div>

    <div v-else-if="error" class="error-msg">
      {{ error }}
    </div>

    <div v-else-if="commande" class="tracking-content">
      <!-- Statut Global de la Commande -->
      <div class="global-status-card" :class="statusGlobalClass">
        <h2>État général : {{ commande.statutGlobal }}</h2>
        <p v-if="commande.statutGlobal === 'Commandée'">Votre commande est en attente de prise en charge.</p>
        <p v-else-if="commande.statutGlobal === 'En cours de préparation'">Le barman prépare vos boissons ! 🍸</p>
        <p v-else-if="commande.statutGlobal === 'Terminée'">C'est prêt ! Un serveur vous l'apporte. 🎉</p>
      </div>

      <!-- Détail de l'avancement par verre -->
      <h3>Détail de vos boissons</h3>
      <ul class="cocktails-list">
        <!-- On boucle sur la liste générée par l'API (grâce au @OneToMany Java) -->
        <li v-for="cc in commande.cocktailsCommandes" :key="cc.idCocktailCommande" class="cocktail-item">
          <div class="cocktail-info">
            <strong>{{ cc.cocktail?.nom || 'Cocktail inconnu' }}</strong>
          </div>
          <div class="status-badge" :class="getBadgeClass(cc.statutPreparation)">
            {{ cc.statutPreparation }}
          </div>
        </li>
      </ul>

      <div class="auto-refresh-notice">
        Actualisation automatique en temps réel ⏱️
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ClientService } from '../../services/ClientService'
import type { Commande } from '../../types'

const route = useRoute()
const commande = ref<Commande | null>(null)
const loading = ref(true)
const error = ref('')
let refreshInterval: number | undefined

// On récupère l'ID passé dans l'URL par le routeur (ex: /tracking/5 => ID 5)
const idCommande = Number(route.params.id)

// Fonction qui interroge l'API
const fetchCommande = async () => {
  try {
    commande.value = await ClientService.getCommandeDetails(idCommande)
  } catch (e) {
    error.value = "Impossible de trouver cette commande."
    console.error(e)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  // 1er chargement immédiat
  fetchCommande()

  // Pour simuler du "temps réel" simplement, on rafraîchit toutes les 3 secondes.
  refreshInterval = window.setInterval(fetchCommande, 3000)
})

// Nettoyage les fuites de mémoire (memory leak) quand le client quitte la page
onUnmounted(() => {
  if (refreshInterval) {
    clearInterval(refreshInterval)
  }
})

// --- LOGIQUE VISUELLE (Couleurs dynamiques selon le statut) ---

const statusGlobalClass = computed(() => {
  if (!commande.value) return ''
  switch (commande.value.statutGlobal) {
    case 'Commandée': return 'status-ordered'
    case 'En cours de préparation': return 'status-preparing'
    case 'Terminée': return 'status-ready'
    default: return ''
  }
})

const getBadgeClass = (statut: string) => {
  switch (statut) {
    case 'En attente': return 'badge-waiting'
    case 'Préparation des Ingrédients': return 'badge-step1'
    case 'Assemblage': return 'badge-step2'
    case 'Dressage': return 'badge-step3'
    case 'Terminée': return 'badge-ready'
    default: return ''
  }
}
</script>

<style scoped>
.tracking-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.header {
  text-align: center;
  margin-bottom: 30px;
}

.header h1 {
  color: #2f3542;
  font-size: 2rem;
  margin: 0 0 10px 0;
}

.header p {
  color: #57606f;
  font-weight: bold;
}

.global-status-card {
  padding: 20px;
  border-radius: 12px;
  text-align: center;
  margin-bottom: 30px;
  color: white;
  transition: background-color 0.5s ease;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.status-ordered {
  background: #ffa502;
}

/* Orange */
.status-preparing {
  background: #1e90ff;
}

/* Bleu */
.status-ready {
  background: #2ed573;
}

/* Vert */

.global-status-card h2 {
  margin: 0 0 10px 0;
}

h3 {
  color: #2f3542;
  margin-bottom: 15px;
}

.cocktails-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.cocktail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}

.status-badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: bold;
  color: white;
}

/* Code couleur pour l'avancement de chaque verre */
.badge-waiting {
  background: #a4b0be;
}

/* Gris */
.badge-step1 {
  background: #ff6b81;
}

/* Rose */
.badge-step2 {
  background: #70a1ff;
}

/* Bleu clair */
.badge-step3 {
  background: #eccc68;
  color: #2f3542;
}

/* Jaune */
.badge-ready {
  background: #2ed573;
}

/* Vert */

.auto-refresh-notice {
  text-align: center;
  margin-top: 30px;
  font-size: 0.9rem;
  color: #a4b0be;
  font-style: italic;
}

.loading,
.error-msg {
  text-align: center;
  padding: 40px;
  font-weight: bold;
}

.error-msg {
  color: #ff4757;
}
</style>
