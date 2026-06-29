<template>
  <!-- Le composant RouterView affiche dynamiquement la page correspondant à l'URL actuelle -->
  <RouterView />
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
</script>

<style>
/* CSS global minimal pour nettoyer le style par défaut du navigateur */
body {
  margin: 0;
  font-family: 'Inter', 'Roboto', sans-serif;
  background-color: #fdfdfd;
  color: #333;
}
</style>
