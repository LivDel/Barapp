<template>
  <div class="dashboard-page">
    <header class="header">
      <div class="header-inner glass-panel">
        <div class="header-left">
          <span class="icon-wrapper">🍸</span>
          <div class="header-text">
            <span class="title">Poste Barmaker</span>
            <span class="subtitle">{{ commandes.length }} commandes en attente</span>
          </div>
        </div>
        
        <div class="barmaker-nav">
          <button class="nav-btn active">Commandes</button>
          <button class="nav-btn" @click="router.push('/barmaker/admin')">Carte</button>
          <button @click="logout" class="btn-logout" aria-label="Déconnexion">
            <span class="logout-icon">🚪</span>
          </button>
        </div>
      </div>
    </header>

    <div v-if="loading" class="loading">
      Récupération des commandes... ⏳
    </div>

    <div v-else-if="commandes.length === 0" class="empty-state">
      Aucune commande en attente. C'est l'heure de la pause ! ☕
    </div>

    <div v-else class="dashboard-content">
      <!-- HORIZONTAL SCROLL LIST -->
      <div class="orders-scroll-container">
        <button 
          v-for="commande in commandes" 
          :key="commande.idCommande"
          class="order-tab glass-panel"
          :class="{ active: selectedOrderId === commande.idCommande }"
          @click="selectedOrderId = commande.idCommande"
        >
          <div class="tab-top">
            <span class="order-id">BAR-{{ commande.idCommande }}</span>
          </div>
          <p class="table-name">Table {{ commande.numeroTable }}</p>
          
          <div class="progress-track">
            <div class="progress-fill" :style="{ width: getOrderProgress(commande) + '%' }"></div>
          </div>
          <p class="progress-text">{{ getOrderProgress(commande) }}%</p>
        </button>
      </div>

      <!-- SELECTED ORDER DETAILS -->
      <div class="order-details" v-if="selectedOrder">
        <div class="details-header">
          <h3>BAR-{{ selectedOrder.idCommande }} · Table {{ selectedOrder.numeroTable }}</h3>
          <span class="cocktails-count">{{ selectedOrder.cocktailsCommandes?.length || 0 }} cocktails</span>
        </div>

        <div class="cocktails-list">
          <div 
            v-for="cc in sortedCocktails(selectedOrder.cocktailsCommandes)" 
            :key="cc.idCocktailCommande"
            class="cocktail-card glass-panel"
          >
            <div class="cocktail-header">
              <div class="cocktail-name-group">
                <span class="cocktail-name">{{ cc.cocktail?.nom }}</span>
                <span class="cocktail-size">{{ cc.taille?.libelle?.charAt(0) || 'M' }}</span>
              </div>
              <span v-if="cc.statutPreparation === 'Terminée'" class="status-done">✅ Prêt</span>
            </div>

            <!-- Segments de progression -->
            <div class="stages-bar">
              <div 
                v-for="(stage, index) in STAGES" 
                :key="index"
                class="stage-segment"
                :class="{ 'is-active': getStageIndex(cc.statutPreparation) >= index }"
              ></div>
            </div>

            <div class="cocktail-actions">
              <div class="current-stage-info">
                <span class="stage-label">Étape actuelle</span>
                <span class="stage-value">{{ cc.statutPreparation }}</span>
              </div>

              <button 
                class="btn-advance" 
                :class="{ 'is-done': cc.statutPreparation === 'Terminée' }"
                :disabled="cc.statutPreparation === 'Terminée'"
                @click="avancerStatut(cc.idCocktailCommande)"
              >
                {{ cc.statutPreparation === 'Terminée' ? 'Terminé' : 'Étape suivante ❯' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { BarmakerService } from '../../services/BarmakerService'
import { store, logoutBarmaker } from '../../store'
import type { Commande } from '../../types'

const router = useRouter()
const commandes = ref<Commande[]>([])
const loading = ref(true)
let refreshInterval: number | undefined

const selectedOrderId = ref<number | null>(null)

const STAGES = [
  'En attente',
  'Préparation des Ingrédients',
  'Assemblage',
  'Dressage',
  'Terminée'
];

const getStageIndex = (statut: string) => {
  return STAGES.indexOf(statut);
}

const getOrderProgress = (commande: Commande) => {
  if (!commande.cocktailsCommandes || commande.cocktailsCommandes.length === 0) return 0;
  const totalStages = (STAGES.length - 1) * commande.cocktailsCommandes.length;
  const currentTotal = commande.cocktailsCommandes.reduce((acc, cc) => {
    const idx = getStageIndex(cc.statutPreparation);
    return acc + (idx >= 0 ? idx : 0);
  }, 0);
  return totalStages === 0 ? 0 : Math.round((currentTotal / totalStages) * 100);
}

const selectedOrder = computed(() => {
  if (!selectedOrderId.value) return commandes.value[0] || null;
  return commandes.value.find(c => c.idCommande === selectedOrderId.value) || commandes.value[0] || null;
})

const sortedCocktails = (cocktails: any[]) => {
  if (!cocktails) return [];
  // On trie par ID pour garantir que l'ordre reste toujours le même
  // même si le backend renvoie la liste dans le désordre après une mise à jour JPA
  return [...cocktails].sort((a, b) => a.idCocktailCommande - b.idCocktailCommande);
}

const fetchCommandes = async () => {
  try {
    const freshCommandes = await BarmakerService.getCommandesATraiter()
    commandes.value = freshCommandes
    
    // Auto-select first order if none selected or if selected order disappeared
    if (freshCommandes.length > 0) {
      if (!selectedOrderId.value || !freshCommandes.find(c => c.idCommande === selectedOrderId.value)) {
        selectedOrderId.value = freshCommandes[0].idCommande
      }
    } else {
      selectedOrderId.value = null
    }
  } catch (error) {
    console.error("Erreur de récupération des commandes", error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchCommandes()
  refreshInterval = window.setInterval(fetchCommandes, 3000)
})

onUnmounted(() => {
  if (refreshInterval) clearInterval(refreshInterval)
})

const logout = () => {
  logoutBarmaker()
  router.push('/barmaker/login')
}

const avancerStatut = async (idCocktailCommande: number) => {
  try {
    await BarmakerService.faireAvancerStatut(idCocktailCommande)
    await fetchCommandes()
  } catch (error) {
    alert("Impossible de mettre à jour le statut.")
  }
}
</script>

<style scoped>
.dashboard-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: hidden;
}

/* HEADER */
.header {
  padding: 20px 16px 12px 16px;
  background: transparent;
  flex-shrink: 0;
}

.glass-panel {
  background: rgba(58, 29, 110, 0.45);
  backdrop-filter: blur(20px) saturate(140%);
  -webkit-backdrop-filter: blur(20px) saturate(140%);
  border: 1px solid rgba(255, 255, 255, 0.14);
  border-radius: 20px;
}

.header-inner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  flex-wrap: wrap;
  gap: 10px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.icon-wrapper {
  width: 38px;
  height: 38px;
  display: grid;
  place-items: center;
  border-radius: 12px;
  background: var(--grad-sunset);
  color: white;
  font-size: 1.1rem;
  box-shadow: 0 4px 10px rgba(0,0,0,0.15);
}

.header-text {
  display: flex;
  flex-direction: column;
  line-height: 1.1;
}

.title {
  font-family: var(--font-display, "Fraunces", serif);
  font-size: 1.15rem;
  font-weight: 700;
  color: var(--foreground);
}

.subtitle {
  font-size: 0.75rem;
  color: var(--muted-foreground);
  margin-top: 2px;
}

.barmaker-nav {
  display: flex;
  gap: 8px;
  align-items: center;
}

.nav-btn {
  padding: 8px 12px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  font-weight: 600;
  font-size: 0.8rem;
  color: var(--muted-foreground);
  cursor: pointer;
  transition: all 0.3s ease;
}

.nav-btn.active {
  background: var(--grad-sunset);
  color: white;
  border-color: transparent;
}

.btn-logout {
  background: rgba(255, 59, 92, 0.15);
  border: 1px solid rgba(255, 59, 92, 0.3);
  color: #ff4d6d;
  width: 36px;
  height: 36px;
  border-radius: 12px;
  display: grid;
  place-items: center;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.2s;
}

/* HORIZONTAL SCROLL */
.dashboard-content {
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow: hidden;
}

.orders-scroll-container {
  display: flex;
  gap: 12px;
  overflow-x: auto;
  padding: 0 16px 16px 16px;
  flex-shrink: 0;
  scrollbar-width: none; /* Firefox */
}
.orders-scroll-container::-webkit-scrollbar {
  display: none; /* Chrome */
}

.order-tab {
  width: 170px;
  flex-shrink: 0;
  padding: 12px;
  border-radius: 16px;
  text-align: left;
  background: rgba(255, 255, 255, 0.05);
  cursor: pointer;
  transition: all 0.2s;
}

.order-tab.active {
  background: rgba(58, 29, 110, 0.65);
  border-color: rgba(255, 59, 92, 0.4);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.tab-top {
  display: flex;
  justify-content: space-between;
  margin-bottom: 4px;
}

.order-id {
  font-family: monospace;
  font-size: 0.85rem;
  font-weight: bold;
  color: var(--foreground);
}

.table-name {
  font-size: 0.75rem;
  color: var(--muted-foreground);
  margin: 0 0 10px 0;
}

.progress-track {
  height: 6px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  overflow: hidden;
  margin-bottom: 4px;
}

.progress-fill {
  height: 100%;
  background: var(--grad-sunset);
  border-radius: 10px;
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 0.68rem;
  color: #ff4d6d;
  margin: 0;
  font-weight: bold;
}

/* ORDER DETAILS */
.order-details {
  flex: 1;
  overflow-y: auto;
  padding: 0 16px 20px 16px;
}

.details-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.details-header h3 {
  margin: 0;
  font-size: 1.05rem;
  color: var(--foreground);
}

.cocktails-count {
  font-family: monospace;
  font-size: 0.8rem;
  color: var(--muted-foreground);
}

.cocktails-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.cocktail-card {
  padding: 16px;
}

.cocktail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.cocktail-name-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.cocktail-name {
  font-size: 1.1rem;
  font-weight: bold;
  color: var(--foreground);
}

.cocktail-size {
  background: rgba(255, 255, 255, 0.1);
  color: #ff4d6d;
  padding: 2px 8px;
  border-radius: 10px;
  font-family: monospace;
  font-size: 0.75rem;
  font-weight: bold;
}

.status-done {
  background: rgba(46, 213, 115, 0.15);
  color: #2ed573;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 0.75rem;
  font-weight: bold;
}

.stages-bar {
  display: flex;
  gap: 6px;
  margin-bottom: 16px;
}

.stage-segment {
  flex: 1;
  height: 6px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  transition: background 0.3s ease;
}

.stage-segment.is-active {
  background: var(--grad-sunset);
}

.cocktail-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.current-stage-info {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-width: 0;
}

.stage-label {
  font-size: 0.75rem;
  color: var(--muted-foreground);
}

.stage-value {
  font-size: 0.95rem;
  font-weight: bold;
  color: var(--foreground);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.btn-advance {
  background: var(--grad-sunset);
  color: white;
  border: none;
  border-radius: 16px;
  padding: 0 20px;
  height: 52px;
  font-weight: bold;
  font-size: 0.95rem;
  cursor: pointer;
  flex-shrink: 0;
  box-shadow: 0 4px 12px rgba(255, 59, 92, 0.3);
  transition: transform 0.1s, opacity 0.3s;
}

.btn-advance:active:not(:disabled) {
  transform: scale(0.95);
}

.btn-advance.is-done {
  opacity: 0.4;
  box-shadow: none;
  cursor: not-allowed;
}

.loading,
.empty-state {
  text-align: center;
  padding: 40px;
  font-size: 1.1rem;
  color: var(--muted-foreground);
}
</style>
