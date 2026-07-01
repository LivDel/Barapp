<template>
  <div class="login-page">
    <div class="login-card glass-panel">
      <div class="icon-wrapper">🍸</div>
      <h1>Espace Barman</h1>
      <p class="subtitle">Veuillez vous identifier pour accéder au tableau de bord.</p>

      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="identifiant">Identifiant</label>
          <input type="text" id="identifiant" class="glass-input" v-model="identifiant" required placeholder="ex: barmaker1" />
        </div>

        <div class="form-group">
          <label for="password">Mot de passe</label>
          <input type="password" id="password" class="glass-input" v-model="motDePasse" required placeholder="••••••••" />
        </div>

        <button type="submit" class="btn-login" :disabled="loading">
          {{ loading ? 'Connexion en cours...' : 'Se connecter ❯' }}
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
    const barmaker = await BarmakerService.login(identifiant.value, motDePasse.value)
    setBarmakerUser(barmaker.identifiant, barmaker.nom)
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
  min-height: 100vh;
  padding: 20px;
}

.glass-panel {
  background: rgba(58, 29, 110, 0.45);
  backdrop-filter: blur(20px) saturate(140%);
  -webkit-backdrop-filter: blur(20px) saturate(140%);
  border: 1px solid rgba(255, 255, 255, 0.14);
  border-radius: 24px;
}

.login-card {
  padding: 40px;
  width: 100%;
  max-width: 420px;
  text-align: center;
  box-shadow: 0 10px 40px rgba(0,0,0,0.3);
}

.icon-wrapper {
  width: 60px;
  height: 60px;
  margin: 0 auto 20px auto;
  display: grid;
  place-items: center;
  border-radius: 16px;
  background: var(--grad-sunset, linear-gradient(135deg, #ff4d6d 0%, #ff8c00 100%));
  color: white;
  font-size: 1.8rem;
  box-shadow: 0 4px 15px rgba(255, 77, 109, 0.3);
}

h1 {
  font-family: var(--font-display, "Fraunces", serif);
  font-size: 1.8rem;
  font-weight: 700;
  color: var(--foreground, #ffffff);
  margin: 0 0 8px 0;
}

.subtitle {
  color: var(--muted-foreground, #a4b0be);
  font-size: 0.95rem;
  margin: 0 0 30px 0;
}

.form-group {
  text-align: left;
  margin-bottom: 20px;
}

label {
  display: block;
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--muted-foreground, #a4b0be);
  margin-bottom: 8px;
  padding-left: 4px;
}

.glass-input {
  width: 100%;
  padding: 14px 16px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  color: white;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.glass-input:focus {
  outline: none;
  border-color: rgba(255, 77, 109, 0.5);
  background: rgba(255, 255, 255, 0.08);
  box-shadow: 0 0 0 4px rgba(255, 77, 109, 0.1);
}

.glass-input::placeholder {
  color: rgba(255, 255, 255, 0.3);
}

.btn-login {
  background: var(--grad-sunset, linear-gradient(135deg, #ff4d6d 0%, #ff8c00 100%));
  color: white;
  border: none;
  padding: 16px;
  width: 100%;
  border-radius: 14px;
  font-size: 1.05rem;
  font-weight: bold;
  cursor: pointer;
  margin-top: 10px;
  box-shadow: 0 6px 15px rgba(255, 77, 109, 0.25);
  transition: transform 0.1s, opacity 0.3s, box-shadow 0.3s;
}

.btn-login:hover:not(:disabled) {
  box-shadow: 0 8px 20px rgba(255, 77, 109, 0.35);
}

.btn-login:active:not(:disabled) {
  transform: scale(0.98);
}

.btn-login:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  box-shadow: none;
}

.error-msg {
  background: rgba(255, 71, 87, 0.15);
  color: #ff4757;
  padding: 12px;
  border-radius: 10px;
  margin-top: 24px;
  font-size: 0.9rem;
  font-weight: bold;
  border: 1px solid rgba(255, 71, 87, 0.3);
}
</style>
