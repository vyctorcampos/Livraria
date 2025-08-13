<template>
  <div class="generos-page">
    <div class="generos-background">
      <div class="generos-container">
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
                  Lista de Gêneros
                </h1>
                <p class="text-body-1 text-medium-emphasis">
                  Gerencie os gêneros literários
                </p>
              </div>
            </div>
          </div>
        </div>

        <!-- Tabela de gêneros -->
        <div class="table-container">
          <v-card class="table-card" elevation="8">
            <v-data-table
              :headers="headers"
              :items="generos"
              :loading="loading"
              class="elevation-0"
              hover
            >
              <template v-slot:item.nome="{ item }">
                {{ item.nome }}
              </template>
              <template v-slot:item.actions="{ item }">
                <div class="action-buttons">
                  <v-btn
                    size="small"
                    color="primary"
                    variant="text"
                    @click="editarGenero(item)"
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
        <v-dialog v-model="dialogEditar" max-width="500px" persistent>
          <v-card class="dialog-card">
            <v-card-title class="text-h5 dialog-title">
              <v-icon left color="warning">mdi-pencil</v-icon>
              Editar Gênero
            </v-card-title>
            <v-card-text class="dialog-content">
              <v-form @submit.prevent="salvarEdicao" ref="formEditar">
                <v-text-field
                  v-model="generoEditando.nome"
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
              <v-btn color="grey" variant="text" @click="dialogEditar = false">
                Cancelar
              </v-btn>
              <v-btn color="warning" @click="salvarEdicao" :loading="salvando">
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
              <p>Tem certeza que deseja excluir o gênero "{{ generoParaExcluir?.nome }}"?</p>
              <p class="text-caption text-medium-emphasis mt-2">
                Esta ação não pode ser desfeita.
              </p>
            </v-card-text>
            <v-card-actions class="dialog-actions">
              <v-spacer />
              <v-btn color="grey" variant="text" @click="dialogConfirmar = false">
                Cancelar
              </v-btn>
              <v-btn color="error" @click="excluirGenero" :loading="excluindo">
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
import { generosApi } from '@/services/api'
import type { Genero } from '@/types'

const loading = ref(false)
const salvando = ref(false)
const excluindo = ref(false)
const dialogEditar = ref(false)
const dialogConfirmar = ref(false)

const generos = ref<Genero[]>([])

const generoEditando = reactive<Genero>({
  nome: ''
})

const generoParaExcluir = ref<Genero | null>(null)

const snackbar = reactive({
  show: false,
  message: '',
  color: 'success'
})

const rules = {
  required: (value: any) => !!value || 'Campo obrigatório'
}

const headers = [
  { title: 'Nome', key: 'nome', sortable: true },
  { title: 'Ações', key: 'actions', sortable: false }
]

const carregarGeneros = async () => {
  loading.value = true
  try {
    const response = await generosApi.getAll();
    
    if (response.status === 200) {
      generos.value = response.data;
    }
  } catch (error: any) {
    mostrarMensagem(
      error.response?.data?.message || 'Erro ao carregar gêneros',
      'error'
    )
  } finally {
    loading.value = false
  }
}

const editarGenero = (genero: Genero) => {
  Object.assign(generoEditando, genero)
  dialogEditar.value = true
}

const salvarEdicao = async () => {
  if (!generoEditando.id) return
  
  salvando.value = true
  try {
    await generosApi.update(generoEditando.id, generoEditando)
    mostrarMensagem('Gênero atualizado com sucesso!', 'success')
    dialogEditar.value = false
    await carregarGeneros()
  } catch (error: any) {
    mostrarMensagem(
      error.response?.data?.message || 'Erro ao atualizar gênero',
      'error'
    )
  } finally {
    salvando.value = false
  }
}

const confirmarExclusao = (genero: Genero) => {
  generoParaExcluir.value = genero
  dialogConfirmar.value = true
}

const excluirGenero = async () => {
  if (!generoParaExcluir.value?.id) return
  
  excluindo.value = true
  try {
    await generosApi.delete(generoParaExcluir.value.id)
    mostrarMensagem('Gênero excluído com sucesso!', 'success')
    dialogConfirmar.value = false
    generoParaExcluir.value = null
    await carregarGeneros()
  } catch (error: any) {
    mostrarMensagem(
      error.response?.data?.message || 'Erro ao excluir gênero',
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
  carregarGeneros()
})
</script>

<style scoped>
.generos-page {
  min-height: 100vh;
  width: 100%;
}

.generos-background {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 2rem;
}

.generos-container {
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
  .generos-background {
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
