<template>
  <div class="dashboard-page">
    <header class="header">
      <div class="header-top">
        <h1>Tableau de Bord 📋</h1>
        <div class="user-info">
          <span>Connecté : {{ store.barmakerUser?.nom }}</span>
          <button @click="logout" class="btn-logout">Déconnexion</button>
        </div>
      </div>

      <!-- Menu de navigation propre au Barman -->
      <nav class="barmaker-nav">
        <button class="nav-btn active">Commandes en cours</button>
        <button class="nav-btn" @click="router.push('/barmaker/admin')">Gérer la Carte (Admin)</button>
      </nav>
    </header>

    <div v-if="loading" class="loading">
      Récupération des commandes... ⏳
    </div>

    <!-- S'il n'y a aucune commande en attente -->
    <div v-else-if="commandes.length === 0" class="empty-state">
      Aucune commande en attente. C'est l'heure de la pause ! ☕
    </div>

    <!-- La grille affichant toutes les commandes actives -->
    <div v-else class="orders-grid">
      <!-- On boucle sur chaque commande -->
      <div v-for="commande in commandes" :key="commande.idCommande" class="order-card">
        <div class="order-header">
          <h2>Table N°{{ commande.numeroTable }}</h2>
          <span class="order-id">Cmd #{{ commande.idCommande }}</span>
        </div>

        <ul class="cocktails-list">
          <!-- On boucle sur chaque verre de la commande -->
          <li v-for="cc in commande.cocktailsCommandes" :key="cc.idCocktailCommande" class="cocktail-item">

            <div class="cocktail-info">
              <strong>{{ cc.cocktail?.nom }}</strong>
              <div class="status-badge" :class="getBadgeClass(cc.statutPreparation)">
                {{ cc.statutPreparation }}
              </div>
            </div>

            <!-- Le bouton pour faire avancer le statut -->
            <!-- On le cache si le verre est déjà "Terminée" -->
            <button v-if="cc.statutPreparation !== 'Terminée'" @click="avancerStatut(cc.idCocktailCommande)"
              class="btn-action">
              Étape suivante ➡️
            </button>
            <span v-else class="done-mark">✅ Fini</span>

          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { BarmakerService } from '../../services/BarmakerService'
import { store, logoutBarmaker } from '../../store'
import type { Commande } from '../../types'

const router = useRouter()
const commandes = ref<Commande[]>([])
const loading = ref(true)
let refreshInterval: number | undefined

// Cette fonction appelle notre API Java pour récupérer la liste des commandes.
const fetchCommandes = async () => {
  try {
    commandes.value = await BarmakerService.getCommandesATraiter()
  } catch (error) {
    console.error("Erreur de récupération des commandes", error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchCommandes()

  // Même principe de 'Polling' (interrogation) que pour le client.
  // Le Dashboard se rafraîchit tout seul toutes les 3 secondes pour voir les nouvelles commandes arriver "en direct".
  refreshInterval = window.setInterval(fetchCommandes, 3000)
})

// ettoyer le setInterval quand on quitte la page.
onUnmounted(() => {
  if (refreshInterval) clearInterval(refreshInterval)
})

// --- ACTIONS ---

const logout = () => {
  logoutBarmaker()
  router.push('/barmaker/login')
}

// Fonction métier principale du Barmaker !
const avancerStatut = async (idCocktailCommande: number) => {
  try {
    // 1. On appelle l'API PUT pour modifier le statut en Base de Données
    await BarmakerService.faireAvancerStatut(idCocktailCommande)

    // 2. On rafraîchit immédiatement la liste visuelle pour que le barman voie l'évolution
    await fetchCommandes()
  } catch (error) {
    alert("Impossible de mettre à jour le statut.")
  }
}

// --- LOGIQUE VISUELLE ---

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
.dashboard-page {
  margin: 0 auto;
}

.header {
  margin-bottom: 20px;
}

.header-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: var(--card);
  color: var(--card-foreground);
  padding: 15px;
  border-radius: var(--radius);
  margin-bottom: 15px;
  border: 1px solid var(--glass-border);
}

h1 { margin: 0; font-size: 1.5rem; color: var(--foreground); }

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.btn-logout {
  background: var(--destructive);
  color: var(--destructive-foreground);
  border: none;
  padding: 8px 15px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
}

.barmaker-nav {
  display: flex;
  gap: 10px;
}

.nav-btn {
  padding: 10px 20px;
  background: var(--muted);
  border: none;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
  color: var(--muted-foreground);
  transition: background 0.2s;
}

.nav-btn:hover { background: var(--secondary); }
.nav-btn.active { background: var(--primary); color: var(--primary-foreground); }

.grid-commandes {
  display: grid;
  gap: 20px;
  grid-template-columns: 1fr;
}

.commande-card {
  background: var(--card);
  border: 1px solid var(--glass-border);
  border-radius: var(--radius);
  padding: 20px;
}

.commande-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  border-bottom: 1px solid var(--glass-border);
  padding-bottom: 10px;
}

.commande-header h2 { margin: 0; color: var(--foreground); }

.status-badge {
  padding: 5px 10px;
  border-radius: 12px;
  font-weight: bold;
  font-size: 0.9rem;
}

.status-badge.status-commandee { background: var(--accent); color: var(--accent-foreground); }
.status-badge.status-encours { background: var(--primary); color: white; }

.cocktails-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.cocktail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: var(--glass-bg);
  padding: 12px;
  border-radius: 8px;
  border: 1px solid var(--glass-border);
}

.cocktail-info strong {
  font-size: 1.1rem;
  color: var(--foreground);
}
.cocktail-info div {
  font-size: 0.9rem;
  color: var(--muted-foreground);
}

.etat-pill {
  display: inline-block;
  padding: 3px 8px;
  background: var(--muted);
  color: var(--muted-foreground);
  border-radius: 10px;
  font-size: 0.8rem;
  margin-top: 5px;
}

.cocktail-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.btn-action {
  background: var(--primary);
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 6px;
  font-weight: bold;
  cursor: pointer;
  transition: transform 0.1s;
}

.btn-action:active {
  transform: scale(0.95);
  /* Petit effet d'enfoncement au clic */
}

.done-mark {
  color: #2ed573;
  font-weight: bold;
}

.loading,
.empty-state {
  text-align: center;
  padding: 40px;
  font-size: 1.2rem;
  color: #747d8c;
}
</style>
