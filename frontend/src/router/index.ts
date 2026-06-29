import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/client/HomeView.vue'

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
      // Chargement immédiat pour la page d'accueil (plus rapide à l'ouverture)
      component: HomeView 
    },
    {
      path: '/menu',
      name: 'menu',
      // Chargement "lazy" (différé) : le fichier Vue n'est téléchargé que si on visite cette page
      component: () => import('../views/client/MenuView.vue') 
    },
    {
      path: '/cart',
      name: 'cart',
      component: () => import('../views/client/CartView.vue')
    },
    {
      // Le ":id" est un paramètre dynamique (ex: /tracking/5 pour la commande n°5)
      path: '/tracking/:id', 
      name: 'tracking',
      component: () => import('../views/client/OrderTrackingView.vue')
    },
    
    /* =======================================
       ESPACE BARMAKER (Plutôt orienté Tablette/Bureau)
       ======================================= */
    {
      path: '/barmaker',
      name: 'barmaker-dashboard',
      component: () => import('../views/barmaker/DashboardView.vue')
    }
  ]
})

export default router
