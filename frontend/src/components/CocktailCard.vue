<template>
  <div class="cocktail-card glass-panel">
    <div class="card-top">
      <!-- Image with glow effect -->
      <div class="image-wrapper">
        <div class="glow-bg"></div>
        <div class="image-container">
          <img :src="cocktail.image?.startsWith('http') ? cocktail.image : '/images/cocktails/' + cocktail.image" :alt="cocktail.nom" @error="handleImageError" />
        </div>
      </div>

      <!-- Info -->
      <div class="info-wrapper">
        <h3 class="cocktail-title">{{ cocktail.nom }}</h3>
        <p class="cocktail-tagline">{{ cocktail.description }}</p>

        <!-- Ingredients Chips -->
        <div class="ingredients-list" v-if="cocktail.ingredients && cocktail.ingredients.length">
          <span class="ingredient-chip" v-for="ing in cocktail.ingredients.slice(0, 3)" :key="ing.idIngredient">
            <span class="ing-icon">{{ getIngredientIcon(ing.nom) }}</span>
            {{ ing.nom }}
          </span>
        </div>
      </div>
    </div>

    <!-- Sizes & Add Button -->
    <div class="card-bottom">
      <div class="sizes-container">
        <button 
          v-for="pt in sortedPrixTailles" 
          :key="pt.taille.idTaille"
          class="size-btn"
          :class="{ active: selectedTaille?.idTaille === pt.taille.idTaille }"
          @click="selectedTaille = pt.taille"
        >
          <span class="size-label">{{ formatSizeLetter(pt.taille.libelle) }}</span>
          <span class="size-price">{{ pt.prix }}€</span>
        </button>
      </div>

      <button class="add-btn" @click="ajouterAuPanier">
        +
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import type { Cocktail, Taille } from '../types'

const props = defineProps<{
  cocktail: Cocktail
}>()

const emit = defineEmits<{
  (e: 'ajouter', cocktail: Cocktail, taille: Taille, prix: number): void
}>()

const handleImageError = (e: Event) => {
  const img = e.target as HTMLImageElement
  img.src = '/images/cocktails/default.jpg'
}

// Sort prices sizes (S, M, L or by ID)
const sortedPrixTailles = computed(() => {
  if (!props.cocktail || !props.cocktail.prixTailles) return []
  return [...props.cocktail.prixTailles].sort((a, b) => a.taille.idTaille - b.taille.idTaille)
})

const selectedTaille = ref<Taille | null>(null)

// Auto-select Medium (idTaille 2) or the first available
watch(sortedPrixTailles, (newVal) => {
  if (newVal.length > 0 && !selectedTaille.value) {
    const medium = newVal.find(pt => pt.taille.idTaille === 2)
    selectedTaille.value = medium ? medium.taille : newVal[0].taille
  }
}, { immediate: true })

const formatSizeLetter = (libelle: string) => {
  return libelle ? libelle.charAt(0).toUpperCase() : ''
}

const getIngredientIcon = (nom: string) => {
  if (!nom) return '✨'
  const lower = nom.toLowerCase()
  if (lower.includes('citron') || lower.includes('orange')) return '🍋'
  if (lower.includes('framboise') || lower.includes('fraise') || lower.includes('cerise')) return '🍓'
  if (lower.includes('menthe') || lower.includes('violette')) return '🌿'
  if (lower.includes('vodka') || lower.includes('rhum') || lower.includes('liqueur') || lower.includes('curaçao')) return '🥃'
  if (lower.includes('eau') || lower.includes('limonade') || lower.includes('jus')) return '💧'
  if (lower.includes('sucre') || lower.includes('sirop') || lower.includes('grenadine')) return '🍯'
  if (lower.includes('glace')) return '🧊'
  return '✨'
}

const ajouterAuPanier = () => {
  if (selectedTaille.value) {
    const pt = props.cocktail.prixTailles.find(p => p.taille.idTaille === selectedTaille.value!.idTaille)
    if (pt) {
      emit('ajouter', props.cocktail, selectedTaille.value, pt.prix)
    }
  }
}
</script>

<style scoped>
.glass-panel {
  background: rgba(58, 29, 110, 0.45);
  backdrop-filter: blur(20px) saturate(140%);
  -webkit-backdrop-filter: blur(20px) saturate(140%);
  border: 1px solid rgba(255, 255, 255, 0.14);
  border-radius: 24px;
}

.cocktail-card {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  animation: fadeIn 0.4s ease backwards;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.card-top {
  display: flex;
  gap: 16px;
}

.image-wrapper {
  position: relative;
  flex-shrink: 0;
  width: 110px;
  height: 110px;
}

.glow-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  border-radius: 50%;
  background: var(--grad-sunset);
  opacity: 0.6;
  filter: blur(20px);
  z-index: 0;
}

.image-container {
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: 16px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.1);
  z-index: 1;
}

.image-container img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info-wrapper {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.cocktail-title {
  font-family: var(--font-display, "Fraunces", serif);
  font-size: 1.15rem;
  font-weight: 700;
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: var(--foreground);
}

.cocktail-tagline {
  font-size: 0.8rem;
  color: var(--muted-foreground);
  margin: 4px 0 8px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.ingredients-list {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.ingredient-chip {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  background: rgba(255, 255, 255, 0.1);
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 0.7rem;
  color: rgba(255, 255, 255, 0.9);
}

.ing-icon {
  font-size: 0.7rem;
  color: var(--accent);
}

.card-bottom {
  display: flex;
  align-items: center;
  gap: 8px;
}

.sizes-container {
  display: flex;
  flex: 1;
  gap: 6px;
}

.size-btn {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 16px;
  padding: 6px 0;
  color: rgba(255, 255, 255, 0.8);
  cursor: pointer;
  transition: all 0.2s ease;
}

.size-btn.active {
  background: var(--grad-sunset);
  border-color: transparent;
  color: white;
  box-shadow: 0 4px 12px rgba(255, 59, 92, 0.3);
}

.size-label {
  font-size: 0.9rem;
  font-weight: 600;
  line-height: 1;
}

.size-price {
  font-size: 0.75rem;
  opacity: 0.9;
  margin-top: 2px;
}

.add-btn {
  width: 44px;
  height: 44px;
  flex-shrink: 0;
  border-radius: 16px;
  background: var(--grad-sunset);
  border: none;
  color: white;
  font-size: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(255, 59, 92, 0.3);
  transition: transform 0.1s ease;
}

.add-btn:active {
  transform: scale(0.9);
}
</style>
