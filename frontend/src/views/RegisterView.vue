<template>
  <div class="register-page">
    <div class="register-background">
      <div class="register-container">
        <v-card class="register-card" elevation="24">
          <div class="register-header">
            <v-icon size="64" color="primary" class="mb-4">
              mdi-account-plus
            </v-icon>
            <h1 class="text-h3 font-weight-bold text-primary mb-2">
              Criar Conta
            </h1>
            <p class="text-body-1 text-medium-emphasis">
              Registre-se para acessar o sistema
            </p>
          </div>
          
          <v-form @submit.prevent="handleRegister" class="register-form">
            <v-text-field
              v-model="form.login"
              label="Login"
              prepend-inner-icon="mdi-account"
              type="text"
              variant="outlined"
              :rules="[rules.required, rules.minLength]"
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
              :rules="[rules.required, rules.minLength]"
              required
              class="mb-4"
              color="primary"
              bg-color="surface"
              :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
              @click:append-inner="showPassword = !showPassword"
            />
            
            <v-text-field
              v-model="confirmPassword"
              label="Confirmar Senha"
              name="confirmPassword"
              prepend-inner-icon="mdi-lock-check"
              :type="showConfirmPassword ? 'text' : 'password'"
              variant="outlined"
              :rules="[rules.required, rules.passwordMatch]"
              required
              class="mb-4"
              color="primary"
              bg-color="surface"
              :append-inner-icon="showConfirmPassword ? 'mdi-eye-off' : 'mdi-eye'"
              @click:append-inner="showConfirmPassword = !showConfirmPassword"
            />
            
            <v-select
              v-model="form.role"
              label="Tipo de Usuário"
              :items="roleOptions"
              prepend-inner-icon="mdi-shield-account"
              variant="outlined"
              :rules="[rules.required]"
              required
              class="mb-6"
              color="primary"
              bg-color="surface"
            />
            
            <v-btn
              type="submit"
              color="primary"
              size="large"
              block
              @click="handleRegister"
              :loading="loading"
              :disabled="loading"
              class="register-button"
              elevation="4"
            >
              <v-icon left>mdi-account-plus</v-icon>
              {{ loading ? 'Registrando...' : 'Registrar' }}
            </v-btn>
          </v-form>
          
          <div class="register-footer">
            <v-btn
              variant="text"
              @click="$router.push('/login')"
              color="primary"
              class="login-link"
            >
              <v-icon left>mdi-login</v-icon>
              Já tem conta? Faça login
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

const router = useRouter()
const userStore = useUserStore()

const form = reactive({
  login: '',
  password: '',
  role: 'WRITE' as const
})

const confirmPassword = ref('')
const loading = ref(false)
const showPassword = ref(false)
const showConfirmPassword = ref(false)
const snackbar = ref({
  show: false,
  message: '',
  color: 'success'
})

const roleOptions = [
  { title: 'Leitura', value: 'READ' },
  { title: 'Escrita', value: 'WRITE' }
]

const rules = {
  required: (value: string) => !!value || 'Campo obrigatório',
  minLength: (value: string) => value.length >= 6 || 'Mínimo de 6 caracteres',
  passwordMatch: (value: string) => value === form.password || 'Senhas não coincidem'
}

const handleRegister = async () => {
  loading.value = true
  
  try {
    const result = await userStore.register(form)
    
    if (result.success) {
      snackbar.value.color = 'success'
      snackbar.value.message = 'Registro realizado com sucesso!'
      snackbar.value.show = true
      
      setTimeout(() => {
        router.push('/login')
      }, 2000)
    } else {
      snackbar.value.color = 'error'
      snackbar.value.message = result.error || 'Erro ao fazer registro'
      snackbar.value.show = true
    }
  } catch (error: any) {
    console.error('Erro no registro:', error)
    snackbar.value.color = 'error'
    snackbar.value.message = error.response?.data?.message || 'Erro inesperado ao fazer registro'
    snackbar.value.show = true
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  width: 100%;
}

.register-background {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
}

.register-container {
  width: 100%;
  max-width: 500px;
}

.register-card {
  border-radius: 16px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.register-header {
  text-align: center;
  padding: 3rem 2rem 2rem;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.register-form {
  padding: 0 2rem 2rem;
}

.register-button {
  border-radius: 8px;
  font-weight: 600;
  text-transform: none;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
}

.register-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.register-footer {
  padding: 1rem 2rem 2rem;
  text-align: center;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  background: rgba(0, 0, 0, 0.02);
}

.login-link {
  font-weight: 500;
  text-transform: none;
}

/* Responsividade */
@media (max-width: 600px) {
  .register-background {
    padding: 1rem;
  }
  
  .register-header {
    padding: 2rem 1.5rem 1.5rem;
  }
  
  .register-form {
    padding: 0 1.5rem 1.5rem;
  }
  
  .register-footer {
    padding: 1rem 1.5rem 1.5rem;
  }
}
</style>
