<template>
  <div class="login-page">
    <div class="login-card">
      <h1>Espace Barman 🔐</h1>
      <p>Veuillez vous identifier pour accéder au tableau de bord.</p>

      <!-- Le @submit.prevent empêche la page de se recharger (comportement par défaut d'un form HTML) -->
      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="identifiant">Identifiant</label>
          <input type="text" id="identifiant" v-model="identifiant" required placeholder="ex: barmaker1" />
        </div>

        <div class="form-group">
          <label for="password">Mot de passe</label>
          <input type="password" id="password" v-model="motDePasse" required />
        </div>

        <button type="submit" class="btn-login" :disabled="loading">
          {{ loading ? 'Connexion en cours...' : 'Se connecter' }}
        </button>
      </form>

      <p v-if="errorMsg" class="error-msg">{{ errorMsg }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { BarmakerService } from '../../services/BarmakerService'
import { setBarmakerUser } from '../../store'

const router = useRouter()
const identifiant = ref('')
const motDePasse = ref('')
const errorMsg = ref('')
const loading = ref(false)

const handleLogin = async () => {
  loading.value = true
  errorMsg.value = ''
  
  try {
    // 1. On appelle notre API Java
    const barmaker = await BarmakerService.login(identifiant.value, motDePasse.value)
    
    // 2. Si c'est un succès, on mémorise le barman dans le store global
    setBarmakerUser(barmaker.identifiant, barmaker.nom)
    
    // 3. On le redirige vers sa page sécurisée
    router.push('/barmaker/dashboard')
    
  } catch (error) {
    errorMsg.value = "Identifiants incorrects. Veuillez réessayer."
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background: #2f3542; /* Fond sombre pour bien marquer la différence avec le côté client clair */
}

.login-card {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.5);
  width: 100%;
  max-width: 400px;
  text-align: center;
}

h1 {
  color: #2f3542;
  margin-bottom: 10px;
}

p {
  color: #57606f;
  margin-bottom: 30px;
}

.form-group {
  text-align: left;
  margin-bottom: 20px;
}

label {
  display: block;
  font-weight: bold;
  color: #2f3542;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 12px;
  border: 1px solid #dfe4ea;
  border-radius: 6px;
  font-size: 1rem;
}

.btn-login {
  background: #ff4757; /* Rouge dynamique */
  color: white;
  border: none;
  padding: 15px;
  width: 100%;
  border-radius: 8px;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s;
}

.btn-login:hover {
  background: #ff6b81;
}

.btn-login:disabled {
  background: #a4b0be;
  cursor: not-allowed;
}

.error-msg {
  color: #ff4757;
  margin-top: 20px;
  font-weight: bold;
}
</style>
