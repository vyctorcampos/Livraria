
set -e


RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' 


print_message() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE}  LIVRARIA DOCKER COMPOSE${NC}"
    echo -e "${BLUE}================================${NC}"
}

# Função para verificar se o Docker está rodando
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        print_error "Docker não está rodando. Inicie o Docker Desktop primeiro."
        exit 1
    fi
    print_message "Docker está rodando"
}

# Função para verificar se o Docker Compose está disponível
check_docker_compose() {
    if ! docker-compose version > /dev/null 2>&1; then
        print_error "Docker Compose não está disponível"
        exit 1
    fi
    print_message "Docker Compose está disponível"
}

# Função para criar arquivo docker.env se não existir
create_env_file() {
    if [ ! -f docker.env ]; then
        if [ -f env.example ]; then
            print_message "Criando arquivo docker.env a partir de env.example"
            cp env.example docker.env
        else
            print_warning "Arquivo env.example não encontrado. Criando docker.env básico"
            cat > docker.env << EOF
POSTGRES_DB=livraria
POSTGRES_USER=postgres
POSTGRES_PASSWORD=senha
JWT_SECRET=mt-secret-key-docker-production
EOF
        fi
    else
        print_message "Arquivo docker.env já existe"
    fi
}

# Função para iniciar os serviços
start_services() {
    print_message "Iniciando serviços..."
    docker-compose up -d
    
    print_message "Aguardando serviços ficarem prontos..."
    sleep 15
    
    # Verifica status dos serviços
    docker-compose ps
    
    print_message "Serviços iniciados com sucesso!"
    print_message "Backend: http://localhost:8080"
    print_message "Swagger UI: http://localhost:8080/swagger-ui.html"
    print_message "PostgreSQL: localhost:5432"
    print_message "pgAdmin: http://localhost:5050 (admin@livraria.com / admin123)"
}

# Função para parar os serviços
stop_services() {
    print_message "Parando serviços..."
    docker-compose down
    print_message "Serviços parados"
}

# Função para reiniciar os serviços
restart_services() {
    print_message "Reiniciando serviços..."
    docker-compose restart
    print_message "Serviços reiniciados"
}

# Função para mostrar logs
show_logs() {
    if [ -z "$1" ]; then
        print_message "Mostrando logs de todos os serviços..."
        docker-compose logs -f
    else
        print_message "Mostrando logs do serviço: $1"
        docker-compose logs -f "$1"
    fi
}

# Função para mostrar status
show_status() {
    print_message "Status dos serviços:"
    docker-compose ps
}

# Função para limpar tudo
clean_all() {
    print_warning "Esta ação irá remover todos os containers, volumes e imagens!"
    read -p "Tem certeza? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        print_message "Limpando tudo..."
        docker-compose down -v --rmi all
        docker system prune -f
        print_message "Limpeza concluída"
    else
        print_message "Operação cancelada"
    fi
}

# Função para mostrar ajuda
show_help() {
    echo "Uso: $0 [COMANDO]"
    echo ""
    echo "Comandos disponíveis:"
    echo "  start     - Inicia todos os serviços"
    echo "  stop      - Para todos os serviços"
    echo "  restart   - Reinicia todos os serviços"
    echo "  logs      - Mostra logs de todos os serviços"
    echo "  logs [SERVIÇO] - Mostra logs de um serviço específico"
    echo "  status    - Mostra status dos serviços"
    echo "  clean     - Remove todos os containers, volumes e imagens"
    echo "  help      - Mostra esta ajuda"
    echo ""
    echo "Exemplos:"
    echo "  $0 start"
    echo "  $0 logs backend"
    echo "  $0 status"
}

# Função principal
main() {
    print_header
    
    # Verificações iniciais
    check_docker
    check_docker_compose
    create_env_file
    
    # Processa argumentos
    case "${1:-start}" in
        start)
            start_services
            ;;
        stop)
            stop_services
            ;;
        restart)
            restart_services
            ;;
        logs)
            show_logs "$2"
            ;;
        status)
            show_status
            ;;
        clean)
            clean_all
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            print_error "Comando inválido: $1"
            show_help
            exit 1
            ;;
    esac
}

# Executa função principal
main "$@"
