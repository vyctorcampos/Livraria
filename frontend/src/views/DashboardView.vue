<template>
  <v-container fluid>
    <v-row>
      <v-col cols="12">
        <h1 class="text-h4 mb-6">Dashboard - Livraria</h1>
      </v-col>
    </v-row>

    <!-- Cards de Estatísticas -->
    <v-row>
      <v-col cols="12" sm="6" md="3">
        <v-card class="mx-auto" max-width="400">
          <v-card-text>
            <div class="text-overline mb-1">Total de Livros</div>
            <div class="text-h4 mb-1">{{ stats.totalLivros }}</div>
            <v-icon color="primary" size="large">mdi-book-open-variant</v-icon>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" sm="6" md="3">
        <v-card class="mx-auto" max-width="400">
          <v-card-text>
            <div class="text-overline mb-1">Total de Autores</div>
            <div class="text-h4 mb-1">{{ stats.totalAutores }}</div>
            <v-icon color="success" size="large">mdi-account-edit</v-icon>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" sm="6" md="3">
        <v-card class="mx-auto" max-width="400">
          <v-card-text>
            <div class="text-overline mb-1">Total de Gêneros</div>
            <div class="text-h4 mb-1">{{ stats.totalGeneros }}</div>
            <v-icon color="warning" size="large">mdi-tag-multiple</v-icon>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" sm="6" md="3">
        <v-card class="mx-auto" max-width="400">
          <v-card-text>
            <div class="text-overline mb-1">Usuário Atual</div>
            <div class="text-h6 mb-1">{{ userStore.user?.login }}</div>
            <v-icon color="info" size="large">mdi-account</v-icon>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- Ações Rápidas -->
    <v-row class="mt-6">
      <v-col cols="12">
        <v-card>
          <v-card-title>Ações Rápidas</v-card-title>
          <v-card-text>
            <v-row>
              <v-col cols="12" sm="6" md="3">
                <v-btn
                  block
                  color="primary"
                  prepend-icon="mdi-plus"
                  @click="$router.push('/livros')"
                  variant="outlined"
                >
                  Novo Livro
                </v-btn>
              </v-col>
              
              <v-col cols="12" sm="6" md="3">
                <v-btn
                  block
                  color="success"
                  prepend-icon="mdi-plus"
                  @click="$router.push('/autores')"
                  variant="outlined"
                >
                  Novo Autor
                </v-btn>
              </v-col>
              
              <v-col cols="12" sm="6" md="3">
                <v-btn
                  block
                  color="warning"
                  prepend-icon="mdi-plus"
                  @click="$router.push('/generos')"
                  variant="outlined"
                >
                  Novo Gênero
                </v-btn>
              </v-col>
              
              <v-col cols="12" sm="6" md="3">
                <v-btn
                  block
                  color="info"
                  prepend-icon="mdi-magnify"
                  @click="$router.push('/livros')"
                  variant="outlined"
                >
                  Buscar Livros
                </v-btn>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- Últimos Livros Adicionados -->
    <v-row class="mt-6">
      <v-col cols="12">
        <v-card>
          <v-card-title>Últimos Livros Adicionados</v-card-title>
          <v-card-text>
            <v-data-table
              :headers="livrosHeaders"
              :items="ultimosLivros"
              :loading="loading"
              class="elevation-1"
            >
              <template v-slot:item.actions="{ item }">
                <v-btn
                  size="small"
                  color="primary"
                  variant="text"
                  @click="verLivro(item)"
                >
                  Ver
                </v-btn>
              </template>
            </v-data-table>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { livrosApi } from '@/services/api'
import type { Livro } from '@/types'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const stats = ref({
  totalLivros: 0,
  totalAutores: 0,
  totalGeneros: 0
})

const ultimosLivros = ref<Livro[]>([])

const livrosHeaders = [
  { title: 'Título', key: 'titulo' },
  { title: 'Autor', key: 'autor.nome' },
  { title: 'Gênero', key: 'genero.nome' },
  { title: 'Ano', key: 'anoPublicacao' },
  { title: 'Preço', key: 'preco' },
  { title: 'Ações', key: 'actions', sortable: false }
]

const carregarEstatisticas = async () => {
  loading.value = true
  try {
    const livrosResponse = await livrosApi.getAll()
    if (livrosResponse.data.success) {
      ultimosLivros.value = livrosResponse.data.data.slice(0, 5)
      stats.value.totalLivros = livrosResponse.data.data.length
    }
  } catch (error) {
    console.error('Erro ao carregar estatísticas:', error)
  } finally {
    loading.value = false
  }
}

const verLivro = (livro: Livro) => {
  router.push(`/livros/${livro.id}`)
}

onMounted(() => {
  carregarEstatisticas()
})
</script>
