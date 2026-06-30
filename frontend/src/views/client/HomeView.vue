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
.home-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  padding: 20px;
  background: linear-gradient(135deg, #fdfbfb 0%, #ebedee 100%);
  /* Joli fond dégradé */
}

.card {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
  text-align: center;
  max-width: 400px;
}

h1 {
  color: #2f3542;
  margin-bottom: 20px;
  font-size: 1.8rem;
}

p {
  color: #57606f;
  font-size: 1.1rem;
  line-height: 1.5;
}

.success-text {
  color: #2ed573;
  font-weight: bold;
}
</style>
