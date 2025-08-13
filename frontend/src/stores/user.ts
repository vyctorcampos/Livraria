import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User, LoginRequest, RegisterRequest, LoginResponse } from '@/types'
import { authApi } from '@/services/api'

export const useUserStore = defineStore('user', () => {
  const user = ref<string | null>(null)
  const token = ref<string | null>(localStorage.getItem('token'))

  const isAuthenticated = computed(() => !!token.value && !!user.value)

  const login = async (credentials: LoginRequest) => {
    try {
      const response = await authApi.login(credentials)
      const { token: newToken, name: userData } = response.data
      
      token.value = newToken
      user.value = userData
      
      localStorage.setItem('token', newToken)
      localStorage.setItem('user', userData)
      
      return { success: true }
    } catch (error: any) {
      return { 
        success: false, 
        error: error.response?.data?.message || 'Erro ao fazer login' 
      }
    }
  }

  const register = async (userData: RegisterRequest) => {
    try {
      const response = await authApi.register(userData)
      return { success: true, data: response.data }
    } catch (error: any) {
      return { 
        success: false, 
        error: error.response?.data?.message || 'Erro ao fazer registro' 
      }
    }
  }

  const logout = () => {
    user.value = null
    token.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  const initializeAuth = () => {
    const savedToken = localStorage.getItem('token')
    // const savedUser = localStorage.getItem('user')
    
    if (savedToken) {
      token.value = savedToken
    }
  }

  return {
    user,
    token,
    isAuthenticated,
    login,
    register,
    logout,
    initializeAuth
  }
})
