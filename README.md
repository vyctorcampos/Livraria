# Livraria

Sistema de gerenciamento de livraria .

## Tecnologias

- **Vue 3** 
- **TypeScript** 
- **Vuetify 3** 
- **Vue Router 4** 
- **Pinia** 
- **Axios** 
- **Vite**
- **Spring boot 3.5.4**
- **java 17**
- **junit**
- **swagger**
- **postgresql**

  

##  Funcionalidades

### Autenticação Login

### Gerenciamento de Livros

### Gerenciamento de Autores

### Gerenciamento de Gêneros



## Estrutura do Projeto Frontend

```
src/
├── components/          
├── views/              
│   ├── LoginView.vue   
│   ├── RegisterView.vue 
│   ├── DashboardView.vue 
│   ├── LivrosView.vue  
│   ├── AutoresView.vue 
│   └── GenerosView.vue 
├── stores/             
│   └── user.ts         
├── services/           
│   └── api.ts          
├── types/              
│   └── index.ts        
├── router/             
│   └── index.ts        
├── App.vue             
└── main.ts             
```

## Estrutura do Projeto backend

```
src/
├── main/
│   ├── java/
│   │   └── com/desafio/livraria/
│   │       ├── config/
│   │       │   └── controllers/
│   │       ├── dto/
│   │       ├── exception/
│   │       ├── mapper/
│   │       ├── model/
│   │       ├── repository/
│   │       ├── service/
│   │       │   ├── impl/
│   │       │   ├── AutorService.java
│   │       │   ├── GeneroService.java
│   │       │   └── LivroService.java
│   │       ├── util/
│   │       ├── validates/
│   │       └── LivrariaApplication.java
│   └── resources/
├── test/
│   ├── java/
│   │   └── com/desafio/livraria/
│   │       ├── config/
│   │       ├── controllers/
│   │       ├── mapper/
│   │       ├── service/
│   │       ├── validates/
│   │       └── LivrariaApplicationTests.java
│   └── resources/
```

##  Endpoints da API

### Autenticação
- `POST /auth/login` - Login de usuário
- `POST /auth/register` - Registro de usuário

### Autores
- `POST /api/autores` - Criar autor
- `GET /api/autores/{id}` - Buscar autor por ID
- `GET /api/autores` - Listar autores
- `PUT /api/autores/{id}` - Atualizar autor
- `DELETE /api/autores/{id}` - Remover autor
- `GET /api/autores/buscar` - Buscar autor por nome

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

## Regras de Negócio

- Um gênero pode possuir N livros
- Um autor pode possuir N livros
- Cada livro pertence a apenas um autor e um gênero
- Usuários com role "WRITE" podem criar, editar e excluir registros

## Como Executar

### rodar docker compose
- cd pasta/do/projeto/  #entrar na rapiz do projeto
- docker-compose up --build -d #rodar esse comando

### Instalação
```bash
# Instalar dependências
npm install

# Executar em modo desenvolvimento frontend
npm run dev



