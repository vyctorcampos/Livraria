<template>
  <div class="livros-page">
    <div class="livros-background">
      <div class="livros-container">
        <!-- Header com título e botão voltar -->
        <div class="page-header">
          <div class="header-content">
            <div class="header-left">
              <v-btn
                icon="mdi-arrow-left"
                variant="text"
                @click="$router.push('/')"
                class="back-button"
                size="large"
              />
              <div>
                <h1 class="text-h3 font-weight-bold text-primary mb-1">
                  Lista de Livros
                </h1>
                <p class="text-body-1 text-medium-emphasis">
                  Gerencie o acervo de livros
                </p>
              </div>
            </div>
          </div>
        </div>

        <!-- Tabela de livros -->
        <div class="table-container">
          <v-card class="table-card" elevation="8">
            <v-data-table
              :headers="headers"
              :items="livros"
              :loading="loading"
              class="elevation-0"
              hover
            >
              <template v-slot:item.actions="{ item }">
                <div class="action-buttons">
                  <v-btn
                    size="small"
                    color="primary"
                    variant="text"
                    @click="editarLivro(item)"
                    icon="mdi-pencil"
                    title="Editar"
                    class="action-btn"
                  />
                  <v-btn
                    size="small"
                    color="error"
                    variant="text"
                    @click="confirmarExclusao(item)"
                    icon="mdi-delete"
                    title="Excluir"
                    class="action-btn"
                  />
                </div>
              </template>
            </v-data-table>
          </v-card>
        </div>

        <!-- Dialog de edição -->
        <v-dialog v-model="dialogEditar" max-width="600px" persistent>
          <v-card class="dialog-card">
            <v-card-title class="text-h5 dialog-title">
              <v-icon left color="primary">mdi-pencil</v-icon>
              Editar Livro
            </v-card-title>
            <v-card-text class="dialog-content">
              <v-form @submit.prevent="salvarEdicao" ref="formEditar">
                <v-text-field
                  v-model="livroEditando.titulo"
                  label="Nome do Livro"
                  variant="outlined"
                  :rules="[rules.required]"
                  required
                  class="mb-4"
                  color="primary"
                  bg-color="surface"
                />
                
                <v-select
                  v-model="livroEditando.autorId"
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
                  v-model="livroEditando.generoId"
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
              <v-btn color="grey" variant="text" @click="dialogEditar = false">
                Cancelar
              </v-btn>
              <v-btn color="primary" @click="salvarEdicao" :loading="salvando">
                Salvar
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <!-- Dialog de confirmação de exclusão -->
        <v-dialog v-model="dialogConfirmar" max-width="400px">
          <v-card class="dialog-card">
            <v-card-title class="text-h5 dialog-title">
              <v-icon left color="error">mdi-alert-circle</v-icon>
              Confirmar Exclusão
            </v-card-title>
            <v-card-text class="dialog-content">
              <p>Tem certeza que deseja excluir o livro "{{ livroParaExcluir?.titulo }}"?</p>
              <p class="text-caption text-medium-emphasis mt-2">
                Esta ação não pode ser desfeita.
              </p>
            </v-card-text>
            <v-card-actions class="dialog-actions">
              <v-spacer />
              <v-btn color="grey" variant="text" @click="dialogConfirmar = false">
                Cancelar
              </v-btn>
              <v-btn color="error" @click="excluirLivro" :loading="excluindo">
                Excluir
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
import { livrosApi, autoresApi, generosApi } from '@/services/api'
import type { Livro, Autor, Genero } from '@/types'

const loading = ref(false)
const salvando = ref(false)
const excluindo = ref(false)
const dialogEditar = ref(false)
const dialogConfirmar = ref(false)

const livros = ref<Livro[]>([])
const autores = ref<Autor[]>([])
const generos = ref<Genero[]>([])

const livroEditando = reactive<Livro>({
  titulo: '',
  isbn: '',
  anoPublicacao: 0,
  preco: 0,
  autorId: 0,
  generoId: 0
})

const livroParaExcluir = ref<Livro | null>(null)

const snackbar = reactive({
  show: false,
  message: '',
  color: 'success'
})

const rules = {
  required: (value: any) => !!value || 'Campo obrigatório'
}

const headers = [
  { title: 'Título', key: 'titulo', sortable: true },
  { title: 'Autor', key: 'autor.nome', sortable: true },
  { title: 'Gênero', key: 'genero.nome', sortable: true },
  { title: 'Ações', key: 'actions', sortable: false }
]

const carregarLivros = async () => {
  loading.value = true
  try {
    const response = await livrosApi.getAll()
    console.log(response);
    if (response.status === 200) {
      livros.value = response.data
    }
  } catch (error: any) {
    mostrarMensagem(
      error.response?.data?.message || 'Erro ao carregar livros',
      'error'
    )
  } finally {
    loading.value = false
  }
}

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

const editarLivro = (livro: Livro) => {
  Object.assign(livroEditando, livro)
  dialogEditar.value = true
}

const salvarEdicao = async () => {
  if (!livroEditando.id) return
  
  salvando.value = true
  try {
    await livrosApi.update(livroEditando.id, livroEditando)
    mostrarMensagem('Livro atualizado com sucesso!', 'success')
    dialogEditar.value = false
    await carregarLivros()
  } catch (error: any) {
    mostrarMensagem(
      error.response?.data?.message || 'Erro ao atualizar livro',
      'error'
    )
  } finally {
    salvando.value = false
  }
}

const confirmarExclusao = (livro: Livro) => {
  livroParaExcluir.value = livro
  dialogConfirmar.value = true
}

const excluirLivro = async () => {
  if (!livroParaExcluir.value?.id) return
  
  excluindo.value = true
  try {
    await livrosApi.delete(livroParaExcluir.value.id)
    mostrarMensagem('Livro excluído com sucesso!', 'success')
    dialogConfirmar.value = false
    livroParaExcluir.value = null
    await carregarLivros()
  } catch (error: any) {
    mostrarMensagem(
      error.response?.data?.message || 'Erro ao excluir livro',
      'error'
    )
  } finally {
    excluindo.value = false
  }
}

const mostrarMensagem = (message: string, color: string) => {
  snackbar.message = message
  snackbar.color = color
  snackbar.show = true
}

onMounted(() => {
  carregarLivros()
  carregarDados()
})
</script>

<style scoped>
.livros-page {
  min-height: 100vh;
  width: 100%;
}

.livros-background {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 2rem;
}

.livros-container {
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 2rem;
  margin-bottom: 2rem;
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

.back-button {
  margin-right: 1rem;
}

.table-container {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.table-card {
  background: transparent;
}

.action-buttons {
  display: flex;
  gap: 0.5rem;
}

.action-btn {
  transition: all 0.2s ease;
}

.action-btn:hover {
  transform: scale(1.1);
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
  .livros-background {
    padding: 1rem;
  }
  
  .page-header {
    padding: 1.5rem;
    margin-bottom: 1.5rem;
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
  
  .back-button {
    margin-right: 0;
    margin-bottom: 1rem;
  }
}
</style>
