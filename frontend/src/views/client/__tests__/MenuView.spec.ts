import { mount, flushPromises } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import MenuView from '../MenuView.vue'
import { store } from '../../../store'
import { MenuService } from '../../../services/MenuService'
import * as vueRouter from 'vue-router'

const pushMock = vi.fn()

vi.mock('vue-router', () => ({
  useRouter: vi.fn(() => ({
    push: pushMock
  }))
}))

vi.mock('../../../services/MenuService', () => ({
  MenuService: {
    getAllCocktails: vi.fn(),
    getCategories: vi.fn()
  }
}))

describe('MenuView.vue', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    store.numeroTable = 5 // Simuler qu'une table a été identifiée
    store.cart = []
  })

  it('redirige vers l\'accueil si le numéro de table n\'est pas défini', async () => {
    store.numeroTable = null
    const wrapper = mount(MenuView)
    await wrapper.vm.$nextTick()
    
    expect(pushMock).toHaveBeenCalledWith('/')
  })

  it('charge et affiche les catégories et cocktails', async () => {
    const fakeCategories = [{ idCategorie: 1, libelle: 'Classique' }]
    const fakeCocktails = [
      { idCocktail: 1, nom: 'Mojito', description: 'Frais', image: '', categorie: fakeCategories[0], prixTailles: [] }
    ]

    vi.mocked(MenuService.getCategories).mockResolvedValue(fakeCategories as any)
    vi.mocked(MenuService.getAllCocktails).mockResolvedValue(fakeCocktails as any)

    const wrapper = mount(MenuView, {
      global: {
        stubs: { CocktailCard: true }
      }
    })

    // Attendre la résolution des promesses du onMounted
    await flushPromises()

    // Vérifier l'affichage de la catégorie en bouton de filtre
    expect(wrapper.text()).toContain('Classique')
    // Vérifier qu'un composant CocktailCard (stubbé) a été rendu
    expect(wrapper.findAll('cocktail-card-stub').length).toBe(1)
  })
})
