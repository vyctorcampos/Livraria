<template>
  <div class="login-page">
    <div class="login-background">
      <div class="login-container">
        <v-card class="login-card" elevation="24">
          <div class="login-header">
            <v-icon size="64" color="primary" class="mb-4">
              mdi-book-open-variant
            </v-icon>
            <h1 class="text-h3 font-weight-bold text-primary mb-2">
              Livraria
            </h1>
            <p class="text-body-1 text-medium-emphasis">
              Faça login para acessar o sistema
            </p>
          </div>
          
          <v-form @submit.prevent="handleLogin" class="login-form">
            <v-text-field
              v-model="form.login"
              label="Login"
              name="login"
              prepend-inner-icon="mdi-account"
              type="text"
              variant="outlined"
              :rules="[rules.required]"
              required
              class="mb-4"
              color="primary"
              bg-color="surface"
            />
            
            <v-text-field
              v-model="form.password"
              label="Senha"
              name="password"
              prepend-inner-icon="mdi-lock"
              :type="showPassword ? 'text' : 'password'"
              variant="outlined"
              :rules="[rules.required]"
              required
              class="mb-6"
              color="primary"
              bg-color="surface"
              :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
              @click:append-inner="showPassword = !showPassword"
            />
            
            <v-btn
              type="submit"
              color="primary"
              size="large"
              block
              @click="handleLogin"
              :loading="loading"
              :disabled="loading"
              class="login-button"
              elevation="4"
            >
              <v-icon left>mdi-login</v-icon>
              {{ loading ? 'Entrando...' : 'Entrar' }}
            </v-btn>
          </v-form>
          
          <div class="login-footer">
            <v-btn
              variant="text"
              @click="$router.push('/register')"
              color="primary"
              class="register-link"
            >
              <v-icon left>mdi-account-plus</v-icon>
              Não tem conta? Registre-se
            </v-btn>
          </div>
        </v-card>
      </div>
    </div>
    
    <v-snackbar
      v-model="snackbar.show"
      :color="snackbar.color"
      :timeout="4000"
      location="top"
      elevation="8"
    >
      <div class="d-flex align-center">
        <v-icon left>{{ snackbar.color === 'success' ? 'mdi-check-circle' : 'mdi-alert-circle' }}</v-icon>
        {{ snackbar.message }}
      </div>
    </v-snackbar>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import type { LoginRequest } from '@/types'

const router = useRouter()
const userStore = useUserStore()

const form = reactive<LoginRequest>({
  login: '',
  password: ''
})

const loading = ref(false)
const showPassword = ref(false)
const snackbar = reactive({
  show: false,
  message: '',
  color: 'success'
})

const rules = {
  required: (value: string) => !!value || 'Campo obrigatório'
}

const handleLogin = async () => {
  loading.value = true
  
  try {
    const result = await userStore.login(form)
    
    if (result.success) {
      snackbar.color = 'success'
      snackbar.message = 'Login realizado com sucesso!'
      snackbar.show = true
      
      setTimeout(() => {
        router.push('/')
      }, 1500)
    } else {
      snackbar.color = 'error'
      snackbar.message = result.error || 'Erro ao fazer login'
      snackbar.show = true
    }
  } catch (error: any) {
    console.error('Erro no login:', error)
    snackbar.color = 'error'
    snackbar.message = error.response?.data?.message || 'Erro inesperado ao fazer login'
    snackbar.show = true
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  width: 100%;
}

.login-background {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
}

.login-container {
  width: 100%;
  max-width: 450px;
}

.login-card {
  border-radius: 16px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.login-header {
  text-align: center;
  padding: 3rem 2rem 2rem;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.login-form {
  padding: 0 2rem 2rem;
}

.login-button {
  border-radius: 8px;
  font-weight: 600;
  text-transform: none;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.login-footer {
  padding: 1rem 2rem 2rem;
  text-align: center;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  background: rgba(0, 0, 0, 0.02);
}

.register-link {
  font-weight: 500;
  text-transform: none;
}

/* Responsividade */
@media (max-width: 600px) {
  .login-background {
    padding: 1rem;
  }
  
  .login-header {
    padding: 2rem 1.5rem 1.5rem;
  }
  
  .login-form {
    padding: 0 1.5rem 1.5rem;
  }
  
  .login-footer {
    padding: 1rem 1.5rem 1.5rem;
  }
}
</style>
