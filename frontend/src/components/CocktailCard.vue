<template>
  <div class="cocktail-card">
    <!-- Image (placeholder générique si aucune illustration n'est fournie par la BDD) -->
    <div class="image-container">
      <img :src="cocktail.illustration || 'https://via.placeholder.com/300x200?text=Cocktail'" :alt="cocktail.nom" />
      
      <!-- Petit badge (Picto) pour indiquer la catégorie visuellement -->
      <span class="badge" :class="{'sans-alcool': cocktail.categorie.nom === 'Sans Alcool'}">
        {{ cocktail.categorie.nom === 'Sans Alcool' ? '🍹 Sans Alcool' : '🍷 Alcoolisé' }}
      </span>
    </div>

    <div class="content">
      <h3>{{ cocktail.nom }}</h3>
      <p class="recette">{{ cocktail.recette }}</p>
      
      <!-- Bouton d'ajout au panier. L'événement est remonté au parent (MenuView) via $emit -->
      <button @click="$emit('ajouter', cocktail)" class="btn-add">
        ➕ Ajouter
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Cocktail } from '../../types'

// "defineProps" permet de dire à Vue : Ce composant a besoin de recevoir un "cocktail" pour s'afficher.
// C'est fortement typé avec TypeScript !
defineProps<{
  cocktail: Cocktail
}>()

// "defineEmits" permet de prévenir le composant parent (MenuView) quand on clique sur le bouton ajouter.
defineEmits<{
  (e: 'ajouter', cocktail: Cocktail): void
}>()
</script>

<style scoped>
/* Design Mobile-First : Par défaut, la carte prend toute la largeur disponible */
.cocktail-card {
  background: white;
  border-radius: 12px; /* Bords arrondis modernes */
  box-shadow: 0 4px 6px rgba(0,0,0,0.05); /* Légère ombre pour le relief */
  overflow: hidden;
  display: flex;
  flex-direction: column;
  transition: transform 0.2s ease;
}

.cocktail-card:hover {
  transform: translateY(-4px); /* Micro-animation au survol (Exigence UI/UX) */
}

.image-container {
  position: relative;
  height: 180px;
}

.image-container img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* Le badge permet au client de repérer le type de boisson en 1 coup d'oeil */
.badge {
  position: absolute;
  top: 10px;
  right: 10px;
  background: #ff4757; /* Rouge par défaut (Alcool) */
  color: white;
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: bold;
}

.badge.sans-alcool {
  background: #2ed573; /* Vert rassurant pour le sans alcool */
}

.content {
  padding: 16px;
  display: flex;
  flex-direction: column;
  flex-grow: 1; /* Permet au bouton de toujours s'aligner en bas de carte */
}

h3 {
  margin: 0 0 8px 0;
  font-size: 1.2rem;
  color: #2f3542;
}

.recette {
  color: #747d8c;
  font-size: 0.9rem;
  flex-grow: 1;
  margin-bottom: 16px;
  line-height: 1.4;
}

.btn-add {
  background: #2f3542;
  color: white;
  border: none;
  padding: 12px;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s;
}

.btn-add:hover {
  background: #57606f;
}
</style>
