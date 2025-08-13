import axios from 'axios'
import type { 
  LoginRequest, 
  RegisterRequest, 
  Autor, 
  Genero, 
  Livro,
  ApiResponse 
} from '@/types'

const api = axios.create({
  baseURL: '/',
  headers: {
    'Content-Type': 'application/json',
  },
})

// Interceptor para adicionar token em todas as requisições
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Interceptor para tratar erros de resposta
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return error;
  }
)

// API de Autenticação
export const authApi = {
  login: (data: LoginRequest) => 
    api.post<{ token: string; name: any }>('/auth/login', data),
  
  register: (data: RegisterRequest) => 
    api.post<any>('/auth/register', data),
}

// API de Autores
export const autoresApi = {
  create: (data: Autor) => 
    api.post<Autor>('/api/autores', data),
  
  getById: (id: number) => 
    api.get<Autor>(`/api/autores/${id}`),
  
  getAll: () => 
    api.get<Autor[]>('/api/autores'),
  
  update: (id: number, data: Autor) => 
    api.put<Autor>(`/api/autores/${id}`, data),
  
  delete: (id: number) => 
    api.delete<void>(`/api/autores/${id}`),
  
  searchByName: (nome: string) => 
    api.get<Autor[]>('/api/autores/buscar', { params: { nome } }),
}

// API de Gêneros
export const generosApi = {
  create: (data: Genero) => 
    api.post<Genero>('/api/generos', data),
  
  getById: (id: number) => 
    api.get<Genero>(`/api/generos/${id}`),
  
  getAll: () => 
    api.get<Genero[]>('/api/generos'),
  
  update: (id: number, data: Genero) => 
    api.put<Genero>(`/api/generos/${id}`, data),
  
  delete: (id: number) => 
    api.delete<void>(`/api/generos/${id}`),
  
  searchByName: (nome: string) => 
    api.get<Genero[]>('/api/generos/buscar', { params: { nome } }),
}

// API de Livros
export const livrosApi = {
  create: (data: Livro) => 
    api.post<Livro>('/api/livros', data),
  
  getById: (id: number) => 
    api.get<Livro>(`/api/livros/${id}`),
  
  getAll: () => 
    api.get<Livro[]>('/api/livros'),
  
  update: (id: number, data: Livro) => 
    api.put<Livro>(`/api/livros/${id}`, data),
  
  delete: (id: number) => 
    api.delete<void>(`/api/livros/${id}`),
  
  searchByTitle: (titulo: string) => 
    api.get<Livro[]>('/api/livros/buscar-por-titulo', { params: { titulo } }),
}

export default api
