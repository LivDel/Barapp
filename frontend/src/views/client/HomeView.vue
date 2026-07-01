<template>
  <div class="home-container">
    <div class="card">
      <h1>🍹 Bienvenue au Bar'app</h1>

      <!-- Si aucun numéro de table n'a été détecté dans l'URL -->
      <p v-if="!hasTable">
        Veuillez scanner le <strong>QR Code</strong> situé sur votre table pour accéder à la carte.
      </p>

      <!-- Si un numéro de table a été détecté -->
      <p v-else class="success-text">
        ✅ Table identifiée (N°{{ store.numeroTable }}).<br>
        Redirection vers la carte en cours...
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { store } from '../../store'

const route = useRoute()
const router = useRouter()
const hasTable = ref(false)

// La logique du QR Code :
// onMounted s'exécute à l'ouverture de la page. 
// Le QR Code de la table contiendra une URL du type : http://mon-app.com/?table=5
onMounted(() => {
  // On extrait le paramètre "table" de l'URL
  const tableParam = route.query.table

  if (tableParam) {
    // Si on a un numéro, on le sauvegarde dans notre "store" global
    store.numeroTable = Number(tableParam)
    hasTable.value = true

    // On redirige automatiquement le client vers la carte après 1,5 seconde
    // (Léger délai pour que l'UX soit fluide et qu'il ait le temps de lire "Table identifiée")
    setTimeout(() => {
      router.push('/menu')
    }, 1500)
  }
})
</script>

<style scoped>
.home-page {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  height: 100%;
  padding: 20px;
}

.hero {
  margin-bottom: 40px;
}

.hero h1 {
  font-size: 3rem;
  margin-bottom: 10px;
  color: var(--foreground);
}

.hero p {
  font-size: 1.2rem;
  color: var(--muted-foreground);
}

.table-input-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  background: var(--glass-bg);
  padding: 30px;
  border-radius: var(--radius);
  border: 1px solid var(--glass-border);
  backdrop-filter: blur(10px);
  width: 100%;
  max-width: 300px;
}

.table-input-section label {
  font-weight: bold;
  font-size: 1.1rem;
  color: var(--foreground);
}

.table-input-section input {
  padding: 10px;
  font-size: 1.2rem;
  text-align: center;
  width: 100px;
  border: 1px solid var(--glass-border);
  border-radius: 8px;
  background: rgba(0, 0, 0, 0.2);
  color: white;
  outline: none;
}

.btn-start {
  background: var(--grad-sunset);
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 25px;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
  transition: transform 0.2s;
}

.btn-start:hover {
  transform: scale(1.05);
}
</style>
