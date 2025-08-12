-- Script de inicialização do banco de dados Livraria
-- Este script é executado automaticamente quando o container PostgreSQL é criado

-- Cria o schema biblioteca se não existir
CREATE SCHEMA IF NOT EXISTS biblioteca;

-- Define o schema padrão para o usuário postgres
ALTER USER postgres SET search_path TO biblioteca, public;

-- Cria extensões úteis
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Cria tabelas se não existirem (backup para caso o Hibernate não crie)
-- As tabelas serão criadas automaticamente pelo Hibernate com ddl-auto=update

-- Comentário sobre as tabelas que serão criadas:
-- - Autores: para armazenar informações dos autores
-- - Generos: para armazenar os gêneros literários
-- - Livros: para armazenar os livros com relacionamentos
-- - Users: para autenticação e autorização

-- Verifica se as tabelas existem e cria se necessário
DO $$
BEGIN
    -- Cria tabela Users se não existir
    IF NOT EXISTS (SELECT FROM information_schema.tables WHERE table_schema = 'biblioteca' AND table_name = 'users') THEN
        CREATE TABLE biblioteca.users (
            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
            login VARCHAR(255) UNIQUE NOT NULL,
            password VARCHAR(255) NOT NULL,
            role VARCHAR(50) NOT NULL DEFAULT 'READ'
        );
    END IF;
    
    -- Cria tabela Autores se não existir
    IF NOT EXISTS (SELECT FROM information_schema.tables WHERE table_schema = 'biblioteca' AND table_name = 'autores') THEN
        CREATE TABLE biblioteca.autores (
            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
            nome VARCHAR(255) UNIQUE NOT NULL,
            email VARCHAR(255),
            idade INTEGER
        );
    END IF;
    
    -- Cria tabela Generos se não existir
    IF NOT EXISTS (SELECT FROM information_schema.tables WHERE table_schema = 'biblioteca' AND table_name = 'generos') THEN
        CREATE TABLE biblioteca.generos (
            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
            nome VARCHAR(255) UNIQUE NOT NULL
        );
    END IF;
    
    -- Cria tabela Livros se não existir
    IF NOT EXISTS (SELECT FROM information_schema.tables WHERE table_schema = 'biblioteca' AND table_name = 'livros') THEN
        CREATE TABLE biblioteca.livros (
            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
            titulo VARCHAR(255) UNIQUE NOT NULL,
            autor_id UUID NOT NULL,
            genero_id UUID NOT NULL,
            CONSTRAINT fk_livro_autor FOREIGN KEY (autor_id) REFERENCES biblioteca.autores(id),
            CONSTRAINT fk_livro_genero FOREIGN KEY (genero_id) REFERENCES biblioteca.generos(id)
        );
    END IF;
    
END $$;

-- Insere dados de exemplo se as tabelas estiverem vazias
DO $$
BEGIN
    -- Insere usuário padrão se não existir
    IF NOT EXISTS (SELECT 1 FROM biblioteca.users WHERE login = 'admin') THEN
        INSERT INTO biblioteca.users (login, password, role) 
        VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'WRITE');
        -- Senha: admin123 (hash BCrypt)
    END IF;
    
    -- Insere gêneros de exemplo se não existirem
    IF NOT EXISTS (SELECT 1 FROM biblioteca.generos WHERE nome = 'Fantasia') THEN
        INSERT INTO biblioteca.generos (nome) VALUES ('Fantasia');
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM biblioteca.generos WHERE nome = 'Terror') THEN
        INSERT INTO biblioteca.generos (nome) VALUES ('Terror');
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM biblioteca.generos WHERE nome = 'Romance') THEN
        INSERT INTO biblioteca.generos (nome) VALUES ('Romance');
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM biblioteca.generos WHERE nome = 'Ficção Científica') THEN
        INSERT INTO biblioteca.generos (nome) VALUES ('Ficção Científica');
    END IF;
    
    -- Insere autor de exemplo se não existir
    IF NOT EXISTS (SELECT 1 FROM biblioteca.autores WHERE nome = 'J.R.R. Tolkien') THEN
        INSERT INTO biblioteca.autores (nome, email, idade) 
        VALUES ('J.R.R. Tolkien', 'tolkien@example.com', 81);
    END IF;
    
    -- Insere livro de exemplo se não existir
    IF NOT EXISTS (SELECT 1 FROM biblioteca.livros WHERE titulo = 'O Senhor dos Anéis') THEN
        INSERT INTO biblioteca.livros (titulo, autor_id, genero_id)
        SELECT 'O Senhor dos Anéis', a.id, g.id
        FROM biblioteca.autores a, biblioteca.generos g
        WHERE a.nome = 'J.R.R. Tolkien' AND g.nome = 'Fantasia';
    END IF;
    
END $$;

-- Cria índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_autores_nome ON biblioteca.autores(nome);
CREATE INDEX IF NOT EXISTS idx_generos_nome ON biblioteca.generos(nome);
CREATE INDEX IF NOT EXISTS idx_livros_titulo ON biblioteca.livros(titulo);
CREATE INDEX IF NOT EXISTS idx_livros_autor_id ON biblioteca.livros(autor_id);
CREATE INDEX IF NOT EXISTS idx_livros_genero_id ON biblioteca.livros(genero_id);
CREATE INDEX IF NOT EXISTS idx_users_login ON biblioteca.users(login);

-- Comentários sobre as tabelas
COMMENT ON TABLE biblioteca.users IS 'Tabela de usuários para autenticação e autorização';
COMMENT ON TABLE biblioteca.autores IS 'Tabela de autores dos livros';
COMMENT ON TABLE biblioteca.generos IS 'Tabela de gêneros literários';
COMMENT ON TABLE biblioteca.livros IS 'Tabela de livros com relacionamentos para autores e gêneros';

-- Log de inicialização
DO $$
BEGIN
    RAISE NOTICE 'Banco de dados Livraria inicializado com sucesso!';
    RAISE NOTICE 'Schema: biblioteca';
    RAISE NOTICE 'Usuário padrão: admin / admin123';
    RAISE NOTICE 'Tabelas criadas e dados de exemplo inseridos.';
END $$;
