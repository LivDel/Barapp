import { mount, flushPromises } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import DashboardView from '../DashboardView.vue'
import { BarmakerService } from '../../../services/BarmakerService'
import * as vueRouter from 'vue-router'

const pushMock = vi.fn()
vi.mock('vue-router', () => ({
  useRouter: vi.fn(() => ({
    push: pushMock
  }))
}))

vi.mock('../../../services/BarmakerService', () => ({
  BarmakerService: {
    getCommandesATraiter: vi.fn(),
    faireAvancerStatut: vi.fn()
  }
}))

describe('DashboardView.vue', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.useFakeTimers()
  })
  
  afterEach(() => {
    vi.useRealTimers()
  })

  it('affiche le message de pause si aucune commande n\'est en attente', async () => {
    vi.mocked(BarmakerService.getCommandesATraiter).mockResolvedValue([])

    const wrapper = mount(DashboardView)
    await flushPromises()

    expect(wrapper.text()).toContain("C'est l'heure de la pause ! ☕")
  })

  it('affiche les commandes en attente', async () => {
    const fakeCommandes = [
      { idCommande: 42, numeroTable: 10, cocktailsCommandes: [] }
    ]
    vi.mocked(BarmakerService.getCommandesATraiter).mockResolvedValue(fakeCommandes as any)

    const wrapper = mount(DashboardView)
    await flushPromises()

    expect(wrapper.text()).toContain('BAR-42')
    expect(wrapper.text()).toContain('Table 10')
  })
})
