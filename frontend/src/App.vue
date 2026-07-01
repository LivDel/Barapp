<template>
  <div class="mobile-frame">
    <!-- Fond décoratif (maquette Figma) -->
    <div class="festive-blobs">
      <div class="blob-purple"></div>
      <div class="blob-red"></div>
      <div class="blob-yellow"></div>
    </div>

    <!-- Contenu dynamique (Vues) -->
    <div class="screen-content">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </div>

    <!-- Navigation pour le Client uniquement (si pas sur la page barmaker) -->
    <nav class="bottom-nav glass" v-if="!isBarmakerRoute">
      <router-link to="/menu" class="nav-item">
        <span class="icon">🍸</span>
        <span>Carte</span>
      </router-link>
      <router-link to="/cart" class="nav-item cart-btn">
        <span class="icon">🛒</span>
        <span>Panier</span>
        <div class="cart-badge" v-if="cartCount > 0">{{ cartCount }}</div>
      </router-link>
      <router-link :to="store.lastOrderId ? `/tracking/${store.lastOrderId}` : '/tracking'" class="nav-item">
        <span class="icon">📍</span>
        <span>Suivi</span>
      </router-link>
    </nav>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { store } from './store'

const route = useRoute()

// Vérifie si l'on est dans la section barman
const isBarmakerRoute = computed(() => {
  return route.path.startsWith('/barmaker')
})

const cartCount = computed(() => store.cart.length)
</script>

<style scoped>
.bottom-nav {
  position: relative;
  z-index: 30;
  display: flex;
  align-items: center;
  padding: 6px;
  margin: 10px 20px 20px 20px;
  border-radius: 20px;
}

.nav-item {
  position: relative;
  display: flex;
  flex: 1 1 0%;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  color: var(--muted-foreground);
  font-size: 0.72rem;
  font-weight: 600;
  text-decoration: none;
  transition: all 0.3s ease;
  padding: 8px 0;
  border-radius: 14px;
  z-index: 1;
}

.nav-item.router-link-active {
  color: white;
}

.nav-item.router-link-active::before {
  content: '';
  position: absolute;
  inset: 0;
  background: var(--grad-sunset);
  border-radius: 14px;
  z-index: -1;
  box-shadow: 0 4px 12px rgba(255, 59, 92, 0.2);
}

.nav-item .icon {
  font-size: 1.2rem;
}

.cart-btn {
  position: relative;
}

.cart-badge {
  position: absolute;
  top: 0px;
  right: 5px;
  background-color: var(--destructive);
  color: var(--destructive-foreground);
  font-size: 0.6rem;
  font-weight: bold;
  padding: 2px 6px;
  border-radius: 10px;
  border: 2px solid var(--glass-bg);
}
</style>
