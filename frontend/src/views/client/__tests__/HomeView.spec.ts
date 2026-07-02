import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'

const localStorageMock = {
  getItem: vi.fn(),
  setItem: vi.fn(),
  clear: vi.fn()
};
vi.stubGlobal('localStorage', localStorageMock);

import HomeView from '../HomeView.vue'
import { store } from '../../../store'
import * as vueRouter from 'vue-router'

// Mocker vue-router
const pushMock = vi.fn()

vi.mock('vue-router', () => ({
  useRoute: vi.fn(),
  useRouter: vi.fn(() => ({
    push: pushMock
  }))
}))

describe('HomeView.vue', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    store.numeroTable = null
    vi.useFakeTimers()
  })

  it('affiche le message demandant de scanner le QR Code si aucune table n\'est fournie', async () => {
    vi.mocked(vueRouter.useRoute).mockReturnValue({
      query: {}
    } as any)

    const wrapper = mount(HomeView)
    await wrapper.vm.$nextTick()
    
    expect(wrapper.text()).toContain('Veuillez scanner le QR Code')
    expect(store.numeroTable).toBeNull()
  })

  it('identifie la table et redirige vers /menu si un numéro de table est dans l\'URL', async () => {
    vi.mocked(vueRouter.useRoute).mockReturnValue({
      query: { table: '5' }
    } as any)

    const wrapper = mount(HomeView)
    await wrapper.vm.$nextTick()
    
    expect(wrapper.text()).toContain('Table identifiée (N°5)')
    expect(store.numeroTable).toBe(5)

    // Vérifier la redirection automatique après 1500ms
    vi.advanceTimersByTime(1500)
    expect(pushMock).toHaveBeenCalledWith('/menu')
  })
})
