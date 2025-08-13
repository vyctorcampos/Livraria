export interface User {
  id?: number
  login: string
  password?: string
  role: 'READ' | 'WRITE'
  nome?: string
}

export interface LoginRequest {
  login: string
  password: string
}

export interface RegisterRequest {
  login: string
  password: string
  role: 'WRITE'
}

export interface LoginResponse {
  token: string
  user: User
}

export interface Autor {
  id?: number
  nome: string
  email: string
  idade: number
  biografia?: string
  dataNascimento?: string
}

export interface Genero {
  id?: number
  nome: string
  descricao?: string
}

export interface Livro {
  id?: number
  titulo: string
  isbn: string
  anoPublicacao: number
  preco: number
  autorId: number
  generoId: number
  autor?: Autor
  genero?: Genero
}

export interface ApiResponse<T> {
  data: T
  message?: string
  success: boolean
}
