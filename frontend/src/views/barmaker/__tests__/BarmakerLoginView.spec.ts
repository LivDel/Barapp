import { mount, flushPromises } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import BarmakerLoginView from '../BarmakerLoginView.vue'
import { BarmakerService } from '../../../services/BarmakerService'
import * as vueRouter from 'vue-router'
import { store } from '../../../store'

const pushMock = vi.fn()

vi.mock('vue-router', () => ({
  useRouter: vi.fn(() => ({
    push: pushMock
  }))
}))

vi.mock('../../../services/BarmakerService', () => ({
  BarmakerService: {
    login: vi.fn()
  }
}))

describe('BarmakerLoginView.vue', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    store.barmakerUser = null
  })

  it('affiche une erreur si les identifiants sont incorrects', async () => {
    vi.mocked(BarmakerService.login).mockRejectedValue(new Error('Unauthorized'))

    const wrapper = mount(BarmakerLoginView)

    await wrapper.find('input#identifiant').setValue('mauvais')
    await wrapper.find('input#password').setValue('mdp')
    await wrapper.find('form').trigger('submit.prevent')

    await flushPromises()

    expect(wrapper.text()).toContain('Identifiants incorrects. Veuillez réessayer.')
    expect(pushMock).not.toHaveBeenCalled()
  })

  it('connecte l\'utilisateur et redirige vers le dashboard', async () => {
    const fakeBarmaker = { identifiant: 'barmaker1', nom: 'Jean' }
    vi.mocked(BarmakerService.login).mockResolvedValue(fakeBarmaker as any)

    const wrapper = mount(BarmakerLoginView)

    await wrapper.find('input#identifiant').setValue('barmaker1')
    await wrapper.find('input#password').setValue('password')
    await wrapper.find('form').trigger('submit.prevent')

    await flushPromises()

    expect(store.barmakerUser?.identifiant).toBe('barmaker1')
    expect(pushMock).toHaveBeenCalledWith('/barmaker/dashboard')
    expect(wrapper.text()).not.toContain('Identifiants incorrects')
  })
})
