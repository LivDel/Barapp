<template>
  <div class="menu-page">
    <!-- Header -->
    <header class="header">
      <div class="header-inner glass">
        <div class="header-left">
          <span class="icon-wrapper">🍸</span>
          <div class="header-text">
            <span class="title">Le Bar'app</span>
            <span class="subtitle">Cocktails artisanaux</span>
          </div>
        </div>
        
        <button class="cart-icon-btn" @click="router.push('/cart')" aria-label="Panier">
          <span class="bag-icon">🛍️</span>
          <transition name="pop">
            <span v-if="cartCount > 0" class="cart-badge">{{ cartCount }}</span>
          </transition>
        </button>
      </div>
    </header>

    <!-- Filtres -->
    <div class="filters-container">
      <div class="filters">
        <button 
          class="filter-btn" 
          :class="{ active: currentFilter === null }" 
          @click="currentFilter = null"
        >
          <span class="filter-icon">✨</span> Tous
        </button>
        <button 
          v-for="cat in categories" 
          :key="cat.idCategorie"
          class="filter-btn"
          :class="{ active: currentFilter === cat.idCategorie }"
          @click="currentFilter = cat.idCategorie"
        >
          <span class="filter-icon">{{ getCategoryIcon(cat.libelle) }}</span> {{ cat.libelle }}
        </button>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="loading">
      Chargement de la carte... ⏳
    </div>

    <!-- Liste de cocktails -->
    <div v-else class="cocktails-list">
      <CocktailCard 
        v-for="cocktail in filteredCocktails" 
        :key="cocktail.idCocktail"
        :cocktail="cocktail"
        @ajouter="handleAddToCart"
      />
      
      <div v-if="filteredCocktails.length === 0" class="empty-state">
        Aucun cocktail trouvé.
      </div>
    </div>

    <!-- CTA Panier flottant -->
    <transition name="slide-up">
      <div class="floating-cta" v-if="cartCount > 0">
        <button class="cta-btn" @click="router.push('/cart')">
          <span class="cta-left">
            <span class="bag-icon-white">🛍️</span>
            Voir mon panier
          </span>
          <span class="cta-right">
            <span class="cta-count">{{ cartCount }}</span>
            <span class="cta-price">{{ cartTotal }}€</span>
          </span>
        </button>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import type { Cocktail, Categorie, Taille } from '../../types'
import { MenuService } from '../../services/MenuService'
import CocktailCard from '../../components/CocktailCard.vue'
import { store, addToCart } from '../../store'

const router = useRouter()
const cocktails = ref<Cocktail[]>([])
const categories = ref<Categorie[]>([])
const loading = ref(true)
const currentFilter = ref<number | null>(null)

const cartCount = computed(() => store.cart.length)
const cartTotal = computed(() => store.cart.reduce((total, item) => total + item.prix, 0))

onMounted(async () => {
  if (!store.numeroTable) {
    router.push('/')
    return
  }

  try {
    const [cocktailsData, categoriesData] = await Promise.all([
      MenuService.getAllCocktails(),
      MenuService.getCategories()
    ])
    cocktails.value = cocktailsData
    categories.value = categoriesData
  } catch (error) {
    console.error("Erreur API :", error)
  } finally {
    loading.value = false
  }
})

const getCategoryIcon = (libelle: string) => {
  if (!libelle) return '🍷'
  const lower = libelle.toLowerCase()
  if (lower.includes('fruit')) return '🍋'
  if (lower.includes('pétillant')) return '🫧'
  if (lower.includes('classique')) return '🍸'
  if (lower.includes('sans alcool')) return '🍹'
  return '🍷'
}

const filteredCocktails = computed(() => {
  if (currentFilter.value === null) {
    return cocktails.value
  }
  return cocktails.value.filter(c => c.categorie.idCategorie === currentFilter.value)
})

const handleAddToCart = (cocktail: Cocktail, taille: Taille, prix: number) => {
  addToCart({ cocktail, taille, prix })
}
</script>

<style scoped>
.menu-page {
  display: flex;
  flex-direction: column;
  height: 100%;
  position: relative;
}

/* HEADER */
.header {
  position: sticky;
  top: 0;
  z-index: 20;
  padding: 20px 16px 12px 16px;
  background: transparent;
}

.header-inner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-radius: 20px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.icon-wrapper {
  width: 36px;
  height: 36px;
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
  font-size: 0.7rem;
  color: var(--muted-foreground);
  margin-top: 2px;
}

.cart-icon-btn {
  position: relative;
  width: 40px;
  height: 40px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  display: grid;
  place-items: center;
  cursor: pointer;
  transition: background 0.2s;
}

.cart-icon-btn:hover {
  background: rgba(255, 255, 255, 0.15);
}

.bag-icon {
  font-size: 1.1rem;
}

.cart-badge {
  position: absolute;
  top: -4px;
  right: -4px;
  width: 20px;
  height: 20px;
  background: var(--destructive);
  color: white;
  border-radius: 50%;
  display: grid;
  place-items: center;
  font-size: 0.65rem;
  font-weight: bold;
  font-family: monospace;
}

/* FILTERS */
.filters-container {
  padding: 0 16px 12px 16px;
}

.filters {
  display: flex;
  gap: 8px;
  overflow-x: auto;
  scrollbar-width: none;
}
.filters::-webkit-scrollbar {
  display: none;
}

.filter-btn {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 14px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.15);
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.82rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.filter-btn.active {
  background: var(--grad-sunset);
  border-color: transparent;
  color: white;
  box-shadow: 0 4px 12px rgba(255, 59, 92, 0.3);
}

.filter-icon {
  font-size: 0.9rem;
}

/* LIST */
.cocktails-list {
  flex: 1;
  overflow-y: auto;
  padding: 0 16px 100px 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.cocktails-list::-webkit-scrollbar {
  display: none;
}

.loading, .empty-state {
  text-align: center;
  padding: 40px;
  color: var(--muted-foreground);
  font-style: italic;
}

/* FLOATING CTA */
.floating-cta {
  position: absolute;
  bottom: 20px;
  left: 16px;
  right: 16px;
  z-index: 30;
}

.cta-btn {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: var(--grad-sunset);
  border: none;
  border-radius: 16px;
  padding: 16px 20px;
  color: white;
  box-shadow: 0 10px 25px rgba(255, 59, 92, 0.4);
  cursor: pointer;
  transition: transform 0.1s;
}

.cta-btn:active {
  transform: scale(0.98);
}

.cta-left {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  font-size: 1rem;
}

.cta-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.cta-count {
  background: rgba(255, 255, 255, 0.25);
  padding: 2px 10px;
  border-radius: 12px;
  font-family: monospace;
  font-size: 0.85rem;
  font-weight: bold;
}

.cta-price {
  font-family: monospace;
  font-size: 1rem;
  font-weight: bold;
}

/* ANIMATIONS */
.pop-enter-active, .pop-leave-active {
  transition: transform 0.2s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}
.pop-enter-from, .pop-leave-to {
  transform: scale(0);
}

.slide-up-enter-active, .slide-up-leave-active {
  transition: all 0.3s ease;
}
.slide-up-enter-from, .slide-up-leave-to {
  opacity: 0;
  transform: translateY(40px);
}
</style>
