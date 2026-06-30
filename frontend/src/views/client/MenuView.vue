<template>
  <div class="menu-page">
    <header class="header">
      <div class="header-top">
        <h1>La Carte 🍸</h1>
        
        <!-- Le bouton Panier avec le nombre d'articles en temps réel -->
        <button class="cart-btn" @click="router.push('/cart')" v-if="store.numeroTable">
          🛒 Panier ({{ store.cart.length }})
        </button>
      </div>
      <p>Découvrez nos cocktails signatures (Table N°{{ store.numeroTable || '?' }})</p>
    </header>

    <div class="filters">
      <button 
        class="filter-btn" 
        :class="{ active: currentFilter === null }" 
        @click="currentFilter = null"
      >
        Tous
      </button>
      <button 
        v-for="cat in categories" 
        :key="cat.idCategorie"
        class="filter-btn"
        :class="{ active: currentFilter === cat.idCategorie }"
        @click="currentFilter = cat.idCategorie"
      >
        {{ cat.nom === 'Sans Alcool' ? '🍹' : '🍷' }} {{ cat.nom }}
      </button>
    </div>

    <div v-if="loading" class="loading">
      Chargement de la carte... ⏳
    </div>

    <div v-else class="cocktails-grid">
      <CocktailCard 
        v-for="cocktail in filteredCocktails" 
        :key="cocktail.idCocktail"
        :cocktail="cocktail"
        @ajouter="handleAddToCart"
      />
      
      <div v-if="filteredCocktails.length === 0" class="empty-state">
        Aucun cocktail trouvé dans cette catégorie pour le moment.
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import type { Cocktail, Categorie } from '../../types'
import { MenuService } from '../../services/MenuService'
import CocktailCard from '../../components/CocktailCard.vue'
import { store, addToCart } from '../../store'

const router = useRouter()

const cocktails = ref<Cocktail[]>([])
const categories = ref<Categorie[]>([])
const loading = ref(true)
const currentFilter = ref<number | null>(null)

onMounted(async () => {
  // Si le client atterrit ici sans numéro de table, on le renvoie à l'accueil
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

const filteredCocktails = computed(() => {
  if (currentFilter.value === null) {
    return cocktails.value
  }
  return cocktails.value.filter(c => c.categorie.idCategorie === currentFilter.value)
})

// Ajout effectif au panier via le store !
const handleAddToCart = (cocktail: Cocktail) => {
  addToCart(cocktail)
  // Petite notification UX non bloquante pourrait aller ici
}
</script>

<style scoped>
.menu-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.header-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.cart-btn {
  background: #2ed573;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 25px;
  font-weight: bold;
  font-size: 1rem;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(46, 213, 115, 0.3);
  transition: transform 0.2s;
}

.cart-btn:hover {
  transform: scale(1.05);
}

.header {
  margin-bottom: 24px;
}

.header h1 {
  font-size: 2rem;
  margin: 0;
  color: #2f3542;
}

.header p {
  color: #747d8c;
  margin-top: 5px;
}

.filters {
  display: flex;
  gap: 10px;
  overflow-x: auto;
  padding-bottom: 10px;
  margin-bottom: 24px;
}

.filter-btn {
  padding: 10px 20px;
  border: 2px solid #dfe4ea;
  background: white;
  border-radius: 25px;
  cursor: pointer;
  font-weight: bold;
  color: #57606f;
  white-space: nowrap;
  transition: all 0.3s ease;
}

.filter-btn.active {
  background: #2ed573;
  color: white;
  border-color: #2ed573;
}

.cocktails-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
}

@media (min-width: 768px) {
  .cocktails-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 1024px) {
  .cocktails-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

.loading, .empty-state {
  text-align: center;
  padding: 40px;
  color: #747d8c;
  font-style: italic;
}
</style>
