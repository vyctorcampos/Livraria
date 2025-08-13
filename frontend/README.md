# Livraria - Frontend

Sistema de gerenciamento de livraria desenvolvido com Vue 3, TypeScript e Vuetify.

## 🚀 Tecnologias

- **Vue 3** - Framework JavaScript progressivo
- **TypeScript** - Superset do JavaScript com tipagem estática
- **Vuetify 3** - Framework de componentes Material Design para Vue
- **Vue Router 4** - Roteamento oficial para Vue
- **Pinia** - Store de estado para Vue
- **Axios** - Cliente HTTP para requisições à API
- **Vite** - Build tool e dev server

## 📋 Funcionalidades

### 🔐 Autenticação
- Login de usuário
- Registro de usuário
- Controle de acesso baseado em roles
- Persistência de sessão

### 📚 Gerenciamento de Livros
- Criar, editar, excluir e listar livros
- Busca por título
- Filtros por autor e gênero
- Relacionamentos com autores e gêneros

### 👨‍💼 Gerenciamento de Autores
- CRUD completo de autores
- Busca por nome
- Biografia e data de nascimento

### 🏷️ Gerenciamento de Gêneros
- CRUD completo de gêneros
- Busca por nome
- Descrição detalhada

### 📊 Dashboard
- Estatísticas gerais
- Ações rápidas
- Últimos livros adicionados

## 🏗️ Estrutura do Projeto

```
src/
├── components/          # Componentes reutilizáveis
├── views/              # Páginas da aplicação
│   ├── LoginView.vue   # Tela de login
│   ├── RegisterView.vue # Tela de registro
│   ├── DashboardView.vue # Dashboard principal
│   ├── LivrosView.vue  # Gerenciamento de livros
│   ├── AutoresView.vue # Gerenciamento de autores
│   └── GenerosView.vue # Gerenciamento de gêneros
├── stores/             # Stores Pinia
│   └── user.ts         # Store de usuário
├── services/           # Serviços de API
│   └── api.ts          # Configuração e endpoints da API
├── types/              # Definições TypeScript
│   └── index.ts        # Interfaces e tipos
├── router/             # Configuração de rotas
│   └── index.ts        # Definição das rotas
├── App.vue             # Componente raiz
└── main.ts             # Ponto de entrada
```

## 🔌 Endpoints da API

### Autenticação
- `POST /auth/login` - Login de usuário
- `POST /auth/register` - Registro de usuário

### Autores
- `POST /autores` - Criar autor
- `GET /autores/{id}` - Buscar autor por ID
- `GET /autores` - Listar autores
- `PUT /autores/{id}` - Atualizar autor
- `DELETE /autores/{id}` - Remover autor
- `GET /autores/buscar` - Buscar autor por nome

### Gêneros
- `POST /api/generos` - Criar gênero
- `GET /api/generos/{id}` - Buscar gênero por ID
- `GET /api/generos` - Listar gêneros
- `PUT /api/generos/{id}` - Atualizar gênero
- `DELETE /api/generos/{id}` - Remover gênero
- `GET /api/generos/buscar` - Buscar gênero por nome

### Livros
- `POST /api/livros` - Criar livro
- `GET /api/livros/{id}` - Buscar livro por ID
- `GET /api/livros` - Listar livros
- `PUT /api/livros/{id}` - Atualizar livro
- `DELETE /api/livros/{id}` - Remover livro
- `GET /api/livros/buscar-por-titulo` - Buscar livro por título

## 📱 Regras de Negócio

- Um gênero pode possuir N livros
- Um autor pode possuir N livros
- Cada livro pertence a apenas um autor e um gênero
- Usuários com role "WRITE" podem criar, editar e excluir registros

## 🚀 Como Executar

### Pré-requisitos
- Node.js 16+ 
- npm ou yarn

### Instalação
```bash
# Instalar dependências
npm install

# Executar em modo desenvolvimento
npm run dev

# Build para produção
npm run build

# Preview da build
npm run preview
```

### Configuração
O projeto está configurado para fazer proxy das requisições para `http://localhost:8080`. 
Para alterar a URL da API, edite o arquivo `vite.config.ts`.

## 🔧 Configurações

### Vite
- Proxy configurado para evitar problemas de CORS
- Alias `@` para pasta `src`
- Porta padrão: 3000

### TypeScript
- Configuração estrita para melhor qualidade de código
- Path mapping para imports limpos
- Suporte completo a Vue 3

### Vuetify
- Tema claro como padrão
- Ícones Material Design
- Componentes responsivos

## 📝 Formato dos Dados

### Login
```json
{
  "login": "usuario",
  "password": "senha123"
}
```

### Registro
```json
{
  "login": "usuario",
  "password": "senha123",
  "role": "WRITE"
}
```

## 🎨 Interface

- Design Material Design com Vuetify
- Layout responsivo para desktop e mobile
- Navegação lateral colapsável
- Tabelas com ordenação e filtros
- Formulários com validação
- Mensagens de feedback com snackbars

## 🔒 Segurança

- Interceptor para adicionar token JWT automaticamente
- Redirecionamento automático para login em caso de token expirado
- Validação de formulários no frontend
- Controle de acesso baseado em rotas

## 🚧 Desenvolvimento

### Adicionando Novas Funcionalidades
1. Crie os tipos em `src/types/index.ts`
2. Adicione os endpoints em `src/services/api.ts`
3. Crie a view correspondente em `src/views/`
4. Adicione a rota em `src/router/index.ts`

### Estrutura de Componentes
- Use Composition API com `<script setup>`
- Implemente validação de formulários
- Adicione loading states para operações assíncronas
- Use snackbars para feedback do usuário

## 📄 Licença

Este projeto é de uso interno para fins educacionais.
