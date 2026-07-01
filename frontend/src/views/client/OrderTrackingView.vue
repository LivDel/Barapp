<template>
  <div class="tracking-wrapper" :class="bgClass">
    <div class="tracking-page">
      <header class="header">
        <div class="header-subtitle">COMMANDE</div>
        <h1 class="header-title" v-if="commande">BAR-{{ commande.idCommande }}</h1>
        <p class="header-time" v-if="commande">Passée à {{ formatTime(commande.dateHeureCreation) }}</p>
      </header>

      <div v-if="loading" class="loading">
        Recherche de votre commande... ⏳
      </div>

      <div v-else-if="error" class="error-msg">
        {{ error }}
      </div>

      <div v-else-if="commande" class="tracking-content">
        
        <!-- Timeline Card -->
        <div class="glass-card timeline-card">
          <div class="timeline">
            <!-- Step 1 -->
            <div class="timeline-step" :class="{ active: step >= 1 }">
              <div class="step-icon">
                <span v-if="step >= 1">✓</span>
              </div>
              <div class="step-content">
                <div class="step-title">Commandée</div>
              </div>
              <div class="timeline-line" :class="{ filled: step >= 2 }"></div>
            </div>

            <!-- Step 2 -->
            <div class="timeline-step" :class="{ active: step >= 2 }">
              <div class="step-icon">
                <span v-if="step >= 2">✓</span>
              </div>
              <div class="step-content">
                <div class="step-title">En cours de préparation</div>
              </div>
              <div class="timeline-line" :class="{ filled: step >= 3 }"></div>
            </div>

            <!-- Step 3 -->
            <div class="timeline-step" :class="{ active: step >= 3 }">
              <div class="step-icon party">
                <span v-if="step >= 3">🎉</span>
              </div>
              <div class="step-content">
                <div class="step-title">Terminée</div>
                <div class="step-subtitle" v-if="step >= 3">Votre commande est prête ! 🎉</div>
              </div>
            </div>
          </div>
        </div>

        <!-- Order details -->
        <div class="glass-card order-details">
          <h3 class="details-title">Votre commande</h3>
          <ul class="cocktails-list">
            <li v-for="(item, idx) in groupedCocktails" :key="idx" class="cocktail-item">
              <div class="item-left">
                <span class="quantity-circle">{{ item.quantite }}</span>
                <span class="item-name">{{ item.nom }}</span>
              </div>
              <div class="item-right">
                <span class="size-circle">{{ item.taille }}</span>
              </div>
            </li>
          </ul>
        </div>

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

const idParam = route.params.id
const idCommande = idParam ? Number(idParam) : null

const fetchCommande = async () => {
  if (!idCommande) {
    error.value = "Aucune commande en cours."
    loading.value = false
    return
  }
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
  fetchCommande()
  refreshInterval = window.setInterval(fetchCommande, 3000)
})

onUnmounted(() => {
  if (refreshInterval) clearInterval(refreshInterval)
})

// Format time
const formatTime = (dateString: string) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' })
}

// Logic for step 1, 2, 3
const step = computed(() => {
  if (!commande.value) return 0
  const s = commande.value.statutGlobal
  if (s === 'Commandée') return 1
  if (s === 'En cours de préparation') return 2
  if (s === 'Terminée') return 3
  return 1
})

const bgClass = computed(() => {
  if (step.value === 1) return 'bg-step-1'
  if (step.value === 2) return 'bg-step-2'
  if (step.value === 3) return 'bg-step-3'
  return 'bg-step-1'
})

// Group identical cocktails
const groupedCocktails = computed(() => {
  if (!commande.value || !commande.value.cocktailsCommandes) return []
  const groups: Record<string, any> = {}
  
  commande.value.cocktailsCommandes.forEach(cc => {
    const nom = cc.cocktail?.nom || 'Cocktail inconnu'
    const taille = cc.taille?.libelle?.charAt(0) || 'S'
    const key = `${nom}-${taille}`
    
    if (groups[key]) {
      groups[key].quantite++
    } else {
      groups[key] = { nom, taille, quantite: 1 }
    }
  })
  
  return Object.values(groups)
})
</script>

<style scoped>
.tracking-wrapper {
  position: absolute; /* absolute au lieu de fixed pour rester dans le cadre mobile */
  top: 0;
  left: 0;
  right: 0;
  min-height: 100%;
  z-index: 10;
  transition: background 1s ease;
  font-family: 'Inter', sans-serif;
  color: white;
  overflow-y: auto;
}

/* Backgrounds adapting to state */
.bg-step-1 {
  background: linear-gradient(135deg, #8A2387, #E94057, #F27121);
}
.bg-step-2 {
  background: linear-gradient(135deg, #4A00E0, #8E2DE2);
}
.bg-step-3 {
  background: linear-gradient(135deg, #0ba360, #3cba92);
}

.tracking-page {
  max-width: 500px;
  margin: 0 auto;
  padding: 60px 20px 120px 20px;
}

.header {
  text-align: center;
  margin-bottom: 40px;
}

.header-subtitle {
  font-size: 0.8rem;
  letter-spacing: 2px;
  text-transform: uppercase;
  opacity: 0.8;
  margin-bottom: 5px;
}

.header-title {
  font-size: 2.5rem;
  font-weight: 800;
  margin: 0 0 5px 0;
  letter-spacing: 1px;
}

.header-time {
  font-size: 0.9rem;
  opacity: 0.9;
  margin: 0;
}

.glass-card {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  padding: 25px;
  margin-bottom: 25px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

/* TIMELINE */
.timeline {
  position: relative;
  display: flex;
  flex-direction: column;
}

.timeline-step {
  display: flex;
  align-items: flex-start;
  position: relative;
  margin-bottom: 35px;
}
.timeline-step:last-child {
  margin-bottom: 0;
}

.step-icon {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2;
  font-size: 0.9rem;
  color: white;
  transition: all 0.3s ease;
}

.timeline-step.active .step-icon {
  background: linear-gradient(135deg, #FF416C, #FF4B2B);
  box-shadow: 0 0 10px rgba(255, 75, 43, 0.5);
}

.step-icon.party {
  font-size: 1.1rem;
}

.step-content {
  margin-left: 20px;
  padding-top: 5px;
}

.step-title {
  font-size: 1.1rem;
  font-weight: 600;
  opacity: 0.6;
  transition: opacity 0.3s;
}

.timeline-step.active .step-title {
  opacity: 1;
}

.step-subtitle {
  font-size: 0.85rem;
  color: #FFEAA7;
  margin-top: 5px;
  font-weight: 500;
}

/* Connecting Line */
.timeline-line {
  position: absolute;
  top: 32px;
  left: 15px;
  width: 3px;
  height: calc(100% + 3px);
  background: rgba(255, 255, 255, 0.15);
  z-index: 1;
}

.timeline-line.filled {
  background: linear-gradient(to bottom, #FF4B2B, #FF416C);
}

/* ORDER DETAILS */
.details-title {
  font-size: 1.2rem;
  font-weight: 700;
  margin: 0 0 20px 0;
  opacity: 0.9;
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
  margin-bottom: 15px;
}
.cocktail-item:last-child {
  margin-bottom: 0;
}

.item-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.quantity-circle, .size-circle {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 0.85rem;
}

.quantity-circle {
  background: rgba(255, 255, 255, 0.2);
  color: white;
}

.size-circle {
  background: rgba(255, 255, 255, 0.1);
  color: #FFA07A;
  border: 1px solid rgba(255, 160, 122, 0.3);
}

.item-name {
  font-size: 1.05rem;
  font-weight: 500;
}

/* REFRESH BTN */
.btn-refresh {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: white;
  padding: 12px 24px;
  border-radius: 30px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  margin: 30px auto 0 auto;
  transition: all 0.2s ease;
}

.btn-refresh:hover {
  background: rgba(255, 255, 255, 0.3);
}

.refresh-icon {
  font-size: 1.2rem;
}

.loading, .error-msg {
  text-align: center;
  padding: 50px 20px;
  font-size: 1.2rem;
  font-weight: bold;
}
</style>
