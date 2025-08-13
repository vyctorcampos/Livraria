<template>
  <div class="home-page">
    <div class="home-background">
      <div class="home-container">
        <!-- Header com título e botão de logout -->
        <div class="home-header">
          <div class="header-content">
            <div class="header-left">
              <v-icon size="48" color="primary" class="mr-4">
                mdi-book-open-variant
              </v-icon>
              <div>
                <h1 class="text-h3 font-weight-bold text-primary mb-1">
                  Livraria
                </h1>
                <p class="text-body-1 text-medium-emphasis">
                  Bem-vindo, {{ currentUser }}!
                </p>
              </div>
            </div>
            <v-btn
              color="error"
              prepend-icon="mdi-logout"
              @click="logout"
              variant="outlined"
              size="large"
              class="logout-button"
            >
              Sair
            </v-btn>
          </div>
        </div>

        <!-- 3 Cards principais para cadastro -->
        <div class="cards-section">
          <h2 class="text-h4 font-weight-bold text-center mb-6">
            Cadastros Rápidos
          </h2>
          <div class="cards-grid">
            <v-card
              class="action-card"
              @click="abrirDialogoLivro"
              hover
              elevation="8"
            >
              <v-card-text class="text-center pa-6">
                <v-icon size="80" color="primary" class="mb-4">
                  mdi-book-plus
                </v-icon>
                <h3 class="text-h5 mb-2 font-weight-bold">Cadastrar Livro</h3>
                <p class="text-body-2 text-medium-emphasis">
                  Adicionar novo livro ao acervo
                </p>
              </v-card-text>
            </v-card>

            <v-card
              class="action-card"
              @click="abrirDialogoAutor"
              hover
              elevation="8"
            >
              <v-card-text class="text-center pa-6">
                <v-icon size="80" color="success" class="mb-4">
                  mdi-account-plus
                </v-icon>
                <h3 class="text-h5 mb-2 font-weight-bold">Cadastrar Autor</h3>
                <p class="text-body-2 text-medium-emphasis">
                  Adicionar novo autor ao sistema
                </p>
              </v-card-text>
            </v-card>

            <v-card
              class="action-card"
              @click="abrirDialogoGenero"
              hover
              elevation="8"
            >
              <v-card-text class="text-center pa-6">
                <v-icon size="80" color="warning" class="mb-4">
                  mdi-tag-plus
                </v-icon>
                <h3 class="text-h5 mb-2 font-weight-bold">Cadastrar Gênero</h3>
                <p class="text-body-2 text-medium-emphasis">
                  Adicionar novo gênero literário
                </p>
              </v-card-text>
            </v-card>
          </div>
        </div>

        <!-- Botões para visualizar listas -->
        <div class="lists-section">
          <h2 class="text-h4 font-weight-bold text-center mb-6">
            Visualizar Listas
          </h2>
          <div class="lists-grid">
            <v-btn
              color="primary"
              prepend-icon="mdi-format-list-bulleted"
              @click="mostrarListaLivros"
              variant="outlined"
              size="x-large"
              class="list-button"
              block
            >
              Ver Lista de Livros
            </v-btn>
            
            <v-btn
              color="success"
              prepend-icon="mdi-format-list-bulleted"
              @click="mostrarListaAutores"
              variant="outlined"
              size="x-large"
              class="list-button"
              block
            >
              Ver Lista de Autores
            </v-btn>
            
            <v-btn
              color="warning"
              prepend-icon="mdi-format-list-bulleted"
              @click="mostrarListaGeneros"
              variant="outlined"
              size="x-large"
              class="list-button"
              block
            >
              Ver Lista de Gêneros
            </v-btn>
          </div>
        </div>

        <!-- Dialog para Cadastrar Livro -->
        <v-dialog v-model="dialogLivro" max-width="600px" persistent>
          <v-card class="dialog-card">
            <v-card-title class="text-h5 dialog-title">
              <v-icon left color="primary">mdi-book-plus</v-icon>
              Cadastrar Novo Livro
            </v-card-title>
            <v-card-text class="dialog-content">
              <v-form @submit.prevent="salvarLivro">
                <v-text-field
                  v-model="formLivro.titulo"
                  label="Nome do Livro"
                  variant="outlined"
                  :rules="[rules.required]"
                  required
                  class="mb-4"
                  color="primary"
                  bg-color="surface"
                />
                
                <v-select
                  v-model="formLivro.autorId"
                  label="Autor"
                  :items="autores"
                  item-title="nome"
                  item-value="id"
                  variant="outlined"
                  :rules="[rules.required]"
                  required
                  class="mb-4"
                  color="primary"
                  bg-color="surface"
                />
                
                <v-select
                  v-model="formLivro.generoId"
                  label="Gênero"
                  :items="generos"
                  item-title="nome"
                  item-value="id"
                  variant="outlined"
                  :rules="[rules.required]"
                  required
                  class="mb-4"
                  color="primary"
                  bg-color="surface"
                />
              </v-form>
            </v-card-text>
            <v-card-actions class="dialog-actions">
              <v-spacer />
              <v-btn color="grey" variant="text" @click="dialogLivro = false">
                Cancelar
              </v-btn>
              <v-btn color="primary" @click="salvarLivro" :loading="loading">
                Salvar
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <!-- Dialog para Cadastrar Autor -->
        <v-dialog v-model="dialogAutor" max-width="500px" persistent>
          <v-card class="dialog-card">
            <v-card-title class="text-h5 dialog-title">
              <v-icon left color="success">mdi-account-plus</v-icon>
              Cadastrar Novo Autor
            </v-card-title>
            <v-card-text class="dialog-content">
              <v-form @submit.prevent="salvarAutor">
                  <v-text-field
                    v-model="formAutor.nome"
                    label="Nome do Autor"
                    variant="outlined"
                    :rules="[rules.required]"
                    required
                    class="mb-4"
                    color="primary"
                    bg-color="surface"
                  />
                  <v-text-field
                    v-model="formAutor.email"
                    label="Email do Autor"
                    variant="outlined"
                    :rules="[rules.required]"
                    required
                    class="mb-4"
                    color="primary"
                    bg-color="surface"
                  />
                  <v-text-field
                    v-model="formAutor.idade"
                    label="Idade do Autor"
                    variant="outlined"
                    :rules="[rules.required]"
                    required
                    class="mb-4"
                    color="primary"
                    bg-color="surface"
                  />
              </v-form>
            </v-card-text>
            <v-card-actions class="dialog-actions">
              <v-spacer />
              <v-btn color="grey" variant="text" @click="dialogAutor = false">
                Cancelar
              </v-btn>
              <v-btn color="success" @click="salvarAutor" :loading="loading">
                Salvar
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <!-- Dialog para Cadastrar Gênero -->
        <v-dialog v-model="dialogGenero" max-width="500px" persistent>
          <v-card class="dialog-card">
            <v-card-title class="text-h5 dialog-title">
              <v-icon left color="warning">mdi-tag-plus</v-icon>
              Cadastrar Novo Gênero
            </v-card-title>
            <v-card-text class="dialog-content">
              <v-form @submit.prevent="salvarGenero">
                <v-text-field
                  v-model="formGenero.nome"
                  label="Nome do Gênero"
                  variant="outlined"
                  :rules="[rules.required]"
                  required
                  class="mb-4"
                  color="primary"
                  bg-color="surface"
                />
              </v-form>
            </v-card-text>
            <v-card-actions class="dialog-actions">
              <v-spacer />
              <v-btn color="grey" variant="text" @click="dialogGenero = false">
                Cancelar
              </v-btn>
              <v-btn color="warning" @click="salvarGenero" :loading="loading">
                Salvar
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <!-- Snackbar para mensagens -->
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
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { livrosApi, autoresApi, generosApi } from '@/services/api'
import type { Livro, Autor, Genero } from '@/types'

const router = useRouter()
const userStore = useUserStore()
const currentUser = localStorage.getItem('user');

// Estados dos diálogos
const dialogLivro = ref(false)
const dialogAutor = ref(false)
const dialogGenero = ref(false)
const loading = ref(false)

// Dados para os formulários
const formLivro = reactive<Livro>({
  titulo: '',
  isbn: 'ISBN-' + Date.now(), // ISBN automático
  anoPublicacao: new Date().getFullYear(),
  preco: 0,
  autorId: 0,
  generoId: 0
})

const formAutor = reactive<Autor>({
  nome: '',
  email: '',
  idade: 0
})

const formGenero = reactive<Genero>({
  nome: ''
})

// Listas para os selects
const autores = ref<Autor[]>([])
const generos = ref<Genero[]>([])

// Snackbar
const snackbar = reactive({
  show: false,
  message: '',
  color: 'success'
})

// Regras de validação
const rules = {
  required: (value: any) => !!value || 'Campo obrigatório'
}

// Carregar dados iniciais
const carregarDados = async () => {
  try {
    const [autoresResponse, generosResponse] = await Promise.all([
      autoresApi.getAll(),
      generosApi.getAll()
    ])

    if (autoresResponse.status === 200) {
      autores.value = autoresResponse.data
    }
    if (generosResponse.status === 200) {
      generos.value = generosResponse.data
    }
  } catch (error) {
    mostrarMensagem('Erro ao carregar dados', 'error')
  }
}

// Funções para abrir diálogos
const abrirDialogoLivro = () => {
  limparFormularioLivro()
  dialogLivro.value = true
}

const abrirDialogoAutor = () => {
  limparFormularioAutor()
  dialogAutor.value = true
}

const abrirDialogoGenero = () => {
  limparFormularioGenero()
  dialogGenero.value = true
}

// Funções para salvar
const salvarLivro = async () => {
  loading.value = true
  try {
    await livrosApi.create(formLivro)
    mostrarMensagem('Livro cadastrado com sucesso!', 'success')
    dialogLivro.value = false
    limparFormularioLivro()
    await carregarDados() // Recarregar autores e gêneros
  } catch (error: any) {
    mostrarMensagem(
      error.response?.data?.message || 'Erro ao cadastrar livro', 
      'error'
    )
  } finally {
    loading.value = false
  }
}

const salvarAutor = async () => {
  loading.value = true
  try {
    await autoresApi.create(formAutor)
    mostrarMensagem('Autor cadastrado com sucesso!', 'success')
    dialogAutor.value = false
    limparFormularioAutor()
    await carregarDados() // Recarregar autores
  } catch (error: any) {
    mostrarMensagem(
      error.response?.data?.message || 'Erro ao cadastrar autor', 
      'error'
    )
  } finally {
    loading.value = false
  }
}

const salvarGenero = async () => {
  loading.value = true
  try {
    await generosApi.create(formGenero)
    mostrarMensagem('Gênero cadastrado com sucesso!', 'success')
    dialogGenero.value = false
    limparFormularioGenero()
    await carregarDados() // Recarregar gêneros
  } catch (error: any) {
    mostrarMensagem(
      error.response?.data?.message || 'Erro ao cadastrar gênero', 
      'error'
    )
  } finally {
    loading.value = false
  }
}

// Funções para limpar formulários
const limparFormularioLivro = () => {
  Object.assign(formLivro, {
    titulo: '',
    isbn: 'ISBN-' + Date.now(),
    anoPublicacao: new Date().getFullYear(),
    preco: 0,
    autorId: 0,
    generoId: 0
  })
}

const limparFormularioAutor = () => {
  Object.assign(formAutor, {
    nome: '',
    email: '',
    idade: 0
  })
}

const limparFormularioGenero = () => {
  Object.assign(formGenero, {
    nome: ''
  })
}

// Funções para mostrar listas
const mostrarListaLivros = () => {
  router.push('/livros')
}

const mostrarListaAutores = () => {
  router.push('/autores')
}

const mostrarListaGeneros = () => {
  router.push('/generos')
}

// Logout
const logout = () => {
  userStore.logout()
  router.push('/login')
}

// Função para mostrar mensagens
const mostrarMensagem = (message: string, color: string) => {
  snackbar.message = message
  snackbar.color = color
  snackbar.show = true
}

onMounted(() => {
  carregarDados()
})
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  width: 100%;
}

.home-background {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 2rem;
}

.home-container {
  max-width: 1200px;
  margin: 0 auto;
}

.home-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 2rem;
  margin-bottom: 3rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-left {
  display: flex;
  align-items: center;
}

.logout-button {
  border-radius: 8px;
  font-weight: 600;
  text-transform: none;
}

.cards-section {
  margin-bottom: 3rem;
}

.cards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-top: 2rem;
}

.action-card {
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 16px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.action-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 16px 40px rgba(0, 0, 0, 0.15);
}

.action-card .v-card-text {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
}

.lists-section {
  margin-bottom: 2rem;
}

.lists-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-top: 2rem;
}

.list-button {
  border-radius: 12px;
  font-weight: 600;
  text-transform: none;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
  height: 60px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.list-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.dialog-card {
  border-radius: 16px;
  overflow: hidden;
}

.dialog-title {
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 1.5rem 2rem;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.dialog-content {
  padding: 2rem;
}

.dialog-actions {
  padding: 1rem 2rem 2rem;
  background: rgba(0, 0, 0, 0.02);
}

/* Responsividade */
@media (max-width: 768px) {
  .home-background {
    padding: 1rem;
  }
  
  .home-header {
    padding: 1.5rem;
    margin-bottom: 2rem;
  }
  
  .header-content {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
  
  .header-left {
    flex-direction: column;
    text-align: center;
  }
  
  .cards-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
  
  .lists-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
}
</style>
