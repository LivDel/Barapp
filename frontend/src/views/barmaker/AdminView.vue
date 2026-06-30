<template>
  <div class="admin-page">
    <!-- Header -->
    <header class="header">
      <div class="header-inner glass-panel">
        <div class="header-left">
          <span class="icon-wrapper">⚙️</span>
          <div class="header-text">
            <span class="title">Administration</span>
            <span class="subtitle">Barman : {{ store.barmakerUser?.nom }}</span>
          </div>
        </div>
        <button @click="logout" class="btn-logout" aria-label="Déconnexion">
          <span class="logout-icon">🚪</span>
        </button>
      </div>

      <nav class="barmaker-nav">
        <button class="nav-btn" @click="router.push('/barmaker/dashboard')">Commandes</button>
        <button class="nav-btn active">Carte</button>
      </nav>
    </header>

    <div class="admin-accordion">
      
      <!-- FORMULAIRE : AJOUTER UN COCKTAIL (EN PREMIER) -->
      <div class="admin-card glass-panel" :class="{ 'is-open': openSection === 'cocktail' }">
        <div class="card-header" @click="toggleSection('cocktail')">
          <div class="card-title-group">
            <span class="card-icon">🍸</span>
            <h2>Nouveau Cocktail</h2>
          </div>
          <span class="chevron">{{ openSection === 'cocktail' ? '▲' : '▼' }}</span>
        </div>
        <div class="card-content" v-show="openSection === 'cocktail'">
          <form @submit.prevent="submitCocktail" class="cocktail-form">
            <div class="form-row">
              <div class="form-group">
                <label>Nom du cocktail</label>
                <input type="text" v-model="newCocktail.nom" required placeholder="ex: Mojito Fraise" />
              </div>
              
              <div class="form-group">
                <label>Prix de base (€)</label>
                <input type="number" step="0.1" v-model="newCocktail.prix" required placeholder="ex: 8.5" />
              </div>
            </div>

            <div class="form-group">
              <label>Description & Recette</label>
              <textarea v-model="newCocktail.description" required placeholder="La recette parfaite..."></textarea>
            </div>

            <div class="form-group">
              <label>Catégorie</label>
              <select v-model="newCocktail.categorie" required>
                <option value="" disabled>Sélectionnez une catégorie</option>
                <option v-for="cat in categories" :key="cat.idCategorie" :value="cat">
                  {{ cat.libelle }}
                </option>
              </select>
            </div>
            
            <div class="form-group">
              <label>URL de l'image (Optionnel)</label>
              <input type="text" v-model="newCocktail.image" placeholder="https://..." />
            </div>

            <button type="submit" class="btn-submit cocktail-submit" :disabled="loadingCocktail">
              {{ loadingCocktail ? 'Création en cours...' : 'Ajouter à la carte' }}
            </button>
          </form>
        </div>
      </div>

      <!-- FORMULAIRE : AJOUTER UNE CATÉGORIE -->
      <div class="admin-card glass-panel" :class="{ 'is-open': openSection === 'categorie' }">
        <div class="card-header" @click="toggleSection('categorie')">
          <div class="card-title-group">
            <span class="card-icon">🗂️</span>
            <h2>Nouvelle Catégorie</h2>
          </div>
          <span class="chevron">{{ openSection === 'categorie' ? '▲' : '▼' }}</span>
        </div>
        <div class="card-content" v-show="openSection === 'categorie'">
          <form @submit.prevent="submitCategorie">
            <div class="form-group">
              <label>Nom de la catégorie</label>
              <input type="text" v-model="newCategorie.libelle" required placeholder="ex: Signatures" />
            </div>
            <button type="submit" class="btn-submit" :disabled="loadingCat">
              {{ loadingCat ? 'Ajout...' : 'Ajouter' }}
            </button>
          </form>
        </div>
      </div>

      <!-- FORMULAIRE : AJOUTER UNE TAILLE -->
      <div class="admin-card glass-panel" :class="{ 'is-open': openSection === 'taille' }">
        <div class="card-header" @click="toggleSection('taille')">
          <div class="card-title-group">
            <span class="card-icon">📏</span>
            <h2>Nouvelle Taille</h2>
          </div>
          <span class="chevron">{{ openSection === 'taille' ? '▲' : '▼' }}</span>
        </div>
        <div class="card-content" v-show="openSection === 'taille'">
          <form @submit.prevent="submitTaille">
            <div class="form-group">
              <label>Nom de la taille</label>
              <input type="text" v-model="newTaille.libelle" required placeholder="ex: XL (50cl)" />
            </div>
            <button type="submit" class="btn-submit" :disabled="loadingTaille">
              {{ loadingTaille ? 'Ajout...' : 'Ajouter' }}
            </button>
          </form>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { MenuService } from '../../services/MenuService'
import { store, logoutBarmaker } from '../../store'
import type { Categorie, Taille, Cocktail } from '../../types'

const router = useRouter()
const categories = ref<Categorie[]>([])
const openSection = ref<string>('cocktail') // Ouvre "cocktail" par défaut

onMounted(async () => {
  try {
    categories.value = await MenuService.getCategories()
  } catch (error) {
    console.error(error)
  }
})

const toggleSection = (section: string) => {
  if (openSection.value === section) {
    openSection.value = '' // Referme si déjà ouvert
  } else {
    openSection.value = section // Ouvre la nouvelle section
  }
}

const logout = () => {
  logoutBarmaker()
  router.push('/barmaker/login')
}

// --- GESTION CATÉGORIE ---
const newCategorie = ref({ libelle: '' })
const loadingCat = ref(false)
const submitCategorie = async () => {
  loadingCat.value = true
  try {
    const created = await MenuService.createCategorie(newCategorie.value)
    categories.value.push(created)
    alert(`Catégorie "${created.libelle}" ajoutée avec succès !`)
    newCategorie.value.libelle = ''
  } catch (error) {
    alert("Erreur lors de l'ajout.")
  } finally {
    loadingCat.value = false
  }
}

// --- GESTION TAILLE ---
const newTaille = ref({ libelle: '' })
const loadingTaille = ref(false)
const submitTaille = async () => {
  loadingTaille.value = true
  try {
    const created = await MenuService.createTaille(newTaille.value)
    alert(`Taille "${created.libelle}" ajoutée avec succès !`)
    newTaille.value.libelle = ''
  } catch (error) {
    alert("Erreur lors de l'ajout.")
  } finally {
    loadingTaille.value = false
  }
}

// --- GESTION COCKTAIL ---
const newCocktail = ref({
  nom: '',
  description: '',
  prix: '',
  image: '',
  categorie: '' as any
})
const loadingCocktail = ref(false)
const submitCocktail = async () => {
  loadingCocktail.value = true
  try {
    const cocktailToSave: Partial<Cocktail> = {
      nom: newCocktail.value.nom,
      description: newCocktail.value.description,
      image: newCocktail.value.image,
      categorie: newCocktail.value.categorie
    }
    await MenuService.createCocktail(cocktailToSave)
    alert(`Cocktail "${cocktailToSave.nom}" ajouté avec succès à la carte !`)
    newCocktail.value = { nom: '', description: '', prix: '', image: '', categorie: '' }
  } catch (error) {
    alert("Erreur lors de l'ajout du cocktail.")
  } finally {
    loadingCocktail.value = false
  }
}
</script>

<style scoped>
.admin-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  padding-bottom: 40px;
}

/* HEADER */
.header {
  position: sticky;
  top: 0;
  z-index: 20;
  padding: 20px 16px 12px 16px;
  background: transparent;
}

.glass-panel {
  background: rgba(58, 29, 110, 0.45);
  backdrop-filter: blur(20px) saturate(140%);
  -webkit-backdrop-filter: blur(20px) saturate(140%);
  border: 1px solid rgba(255, 255, 255, 0.14);
  border-radius: 20px;
}

.header-inner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  margin-bottom: 16px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.icon-wrapper {
  width: 38px;
  height: 38px;
  display: grid;
  place-items: center;
  border-radius: 12px;
  background: var(--grad-sunset);
  color: white;
  font-size: 1.1rem;
  box-shadow: 0 4px 10px rgba(0,0,0,0.15);
}

.header-text {
  display: flex;
  flex-direction: column;
  line-height: 1.1;
}

.title {
  font-family: var(--font-display, "Fraunces", serif);
  font-size: 1.15rem;
  font-weight: 700;
  color: var(--foreground);
}

.subtitle {
  font-size: 0.75rem;
  color: var(--muted-foreground);
  margin-top: 2px;
}

.btn-logout {
  background: rgba(255, 59, 92, 0.15);
  border: 1px solid rgba(255, 59, 92, 0.3);
  color: #ff4d6d;
  width: 40px;
  height: 40px;
  border-radius: 12px;
  display: grid;
  place-items: center;
  font-size: 1.1rem;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-logout:hover {
  background: #ff4d6d;
  color: white;
}

/* NAVIGATION TABS */
.barmaker-nav {
  display: flex;
  gap: 8px;
  background: rgba(255, 255, 255, 0.05);
  padding: 6px;
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.nav-btn {
  flex: 1;
  padding: 10px 0;
  background: transparent;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  font-size: 0.85rem;
  color: var(--muted-foreground);
  cursor: pointer;
  transition: all 0.3s ease;
}

.nav-btn.active {
  background: var(--grad-sunset);
  color: white;
  box-shadow: 0 4px 12px rgba(255, 59, 92, 0.25);
}

/* ACCORDÉONS */
.admin-accordion {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 0 16px;
}

.admin-card {
  padding: 0; /* Padding is managed by inner elements for accordion */
  overflow: hidden;
  transition: all 0.3s ease;
}

.admin-card.is-open {
  background: rgba(58, 29, 110, 0.65);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  cursor: pointer;
  user-select: none;
}

.card-title-group {
  display: flex;
  align-items: center;
  gap: 12px;
}

.card-icon {
  font-size: 1.2rem;
}

.card-header h2 {
  font-family: var(--font-display, "Fraunces", serif);
  font-size: 1.1rem;
  margin: 0;
  color: var(--foreground);
}

.chevron {
  color: var(--muted-foreground);
  font-size: 0.8rem;
}

.card-content {
  padding: 0 20px 20px 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.05);
  margin-top: 5px;
  padding-top: 15px;
  animation: slideDown 0.3s ease;
}

.form-group {
  margin-bottom: 16px;
  display: flex;
  flex-direction: column;
}

.form-row {
  display: flex;
  flex-direction: column;
  gap: 0;
}

@media (min-width: 768px) {
  .form-row {
    flex-direction: row;
    gap: 16px;
  }
  .form-row .form-group {
    flex: 1;
  }
}

label {
  font-size: 0.85rem;
  font-weight: 600;
  margin-bottom: 6px;
  color: rgba(255, 255, 255, 0.8);
}

input, select, textarea {
  padding: 12px 14px;
  background: rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 12px;
  font-size: 0.95rem;
  color: white;
  font-family: inherit;
  transition: border-color 0.2s;
  width: 100%;
}

input::placeholder, textarea::placeholder {
  color: rgba(255, 255, 255, 0.3);
}

input:focus, select:focus, textarea:focus {
  outline: none;
  border-color: rgba(255, 255, 255, 0.4);
  background: rgba(0, 0, 0, 0.3);
}

textarea {
  resize: vertical;
  min-height: 100px;
}

select option {
  background: #2a164f;
  color: white;
}

.btn-submit {
  background: rgba(255, 255, 255, 0.1);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.2);
  padding: 12px;
  border-radius: 12px;
  font-weight: bold;
  font-size: 0.95rem;
  cursor: pointer;
  width: 100%;
  transition: all 0.2s;
}

.btn-submit:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.2);
}

.cocktail-submit {
  background: var(--grad-sunset);
  border: none;
  margin-top: 8px;
}
.cocktail-submit:hover:not(:disabled) {
  background: var(--grad-sunset);
  opacity: 0.9;
  box-shadow: 0 4px 15px rgba(255, 59, 92, 0.3);
}

.btn-submit:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

@keyframes slideDown {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
