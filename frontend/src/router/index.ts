import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/client/HomeView.vue'
import { store } from '../store'

// Le routeur est le chef d'orchestre de la navigation Front-end.
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    /* =======================================
       ESPACE CLIENT (Mobile-First)
       ======================================= */
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/menu',
      name: 'menu',
      component: () => import('../views/client/MenuView.vue')
    },
    {
      path: '/cart',
      name: 'cart',
      component: () => import('../views/client/CartView.vue')
    },
    {
      path: '/tracking/:id?',
      name: 'tracking',
      component: () => import('../views/client/OrderTrackingView.vue')
    },

    /* =======================================
       ESPACE BARMAKER (Protégé)
       ======================================= */
    {
      path: '/barmaker/login',
      name: 'barmaker-login',
      component: () => import('../views/barmaker/BarmakerLoginView.vue')
    },
    {
      path: '/barmaker/dashboard',
      name: 'barmaker-dashboard',
      component: () => import('../views/barmaker/DashboardView.vue'),
      // Méta-donnée pour indiquer que cette route nécessite une connexion
      meta: { requiresAuth: true }
    },
    {
      path: '/barmaker/admin',
      name: 'barmaker-admin',
      component: () => import('../views/barmaker/AdminView.vue'),
      meta: { requiresAuth: true }
    }
  ]
})

// GARDIEN DE ROUTE (Navigation Guard)
router.beforeEach((to, from) => {
  // Si la route visée nécessite d'être authentifié
  if (to.meta.requiresAuth) {
    // On vérifie dans le store global si on a un barmaker connecté
    if (!store.barmakerUser) {
      // Pas connecté ? Hop, on le renvoie brutalement à la page de connexion.
      return { name: 'barmaker-login' }
    } else {
      // Connecté ? On le laisse passer.
      return true
    }
  } else {
    // Si la route est publique (comme le menu client), on laisse passer
    return true
  }
})

export default router
