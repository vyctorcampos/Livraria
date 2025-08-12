
param(
    [Parameter(Position=0)]
    [string]$Command = "start"
)


function Write-Info {
    param([string]$Message)
    Write-Host "[INFO] $Message" -ForegroundColor Green
}

function Write-Warning {
    param([string]$Message)
    Write-Host "[WARNING] $Message" -ForegroundColor Yellow
}

function Write-Error {
    param([string]$Message)
    Write-Host "[ERROR] $Message" -ForegroundColor Red
}

function Write-Header {
    Write-Host "================================" -ForegroundColor Blue
    Write-Host "  LIVRARIA DOCKER COMPOSE" -ForegroundColor Blue
    Write-Host "================================" -ForegroundColor Blue
}


function Test-Docker {
    try {
        docker info | Out-Null
        Write-Info "Docker está rodando"
        return $true
    }
    catch {
        Write-Error "Docker não está rodando. Inicie o Docker Desktop primeiro."
        return $false
    }
}

# Função para verificar se o Docker Compose está disponível
function Test-DockerCompose {
    try {
        docker-compose version | Out-Null
        Write-Info "Docker Compose está disponível"
        return $true
    }
    catch {
        Write-Error "Docker Compose não está disponível"
        return $false
    }
}

# Função para criar arquivo docker.env se não existir
function New-EnvFile {
    if (-not (Test-Path "docker.env")) {
        if (Test-Path "env.example") {
            Write-Info "Criando arquivo docker.env a partir de env.example"
            Copy-Item "env.example" "docker.env"
        }
        else {
            Write-Warning "Arquivo env.example não encontrado. Criando docker.env básico"
            @"
POSTGRES_DB=livraria
POSTGRES_USER=postgres
POSTGRES_PASSWORD=senha
JWT_SECRET=mt-secret-key-docker-production
"@ | Out-File -FilePath "docker.env" -Encoding UTF8
        }
    }
    else {
        Write-Info "Arquivo docker.env já existe"
    }
}

# Função para iniciar os serviços
function Start-Services {
    Write-Info "Iniciando serviços..."
    docker-compose up -d
    
    Write-Info "Aguardando serviços ficarem prontos..."
    Start-Sleep -Seconds 15
    
    # Verifica status dos serviços
    docker-compose ps
    
    Write-Info "Serviços iniciados com sucesso!"
    Write-Info "Backend: http://localhost:8080"
    Write-Info "Swagger UI: http://localhost:8080/swagger-ui.html"
    Write-Info "PostgreSQL: localhost:5432"
    Write-Info "pgAdmin: http://localhost:5050 (admin@livraria.com / admin123)"
}

# Função para parar os serviços
function Stop-Services {
    Write-Info "Parando serviços..."
    docker-compose down
    Write-Info "Serviços parados"
}

# Função para reiniciar os serviços
function Restart-Services {
    Write-Info "Reiniciando serviços..."
    docker-compose restart
    Write-Info "Serviços reiniciados"
}

# Função para mostrar logs
function Show-Logs {
    param([string]$Service = "")
    
    if ($Service -eq "") {
        Write-Info "Mostrando logs de todos os serviços..."
        docker-compose logs -f
    }
    else {
        Write-Info "Mostrando logs do serviço: $Service"
        docker-compose logs -f $Service
    }
}

# Função para mostrar status
function Show-Status {
    Write-Info "Status dos serviços:"
    docker-compose ps
}

# Função para limpar tudo
function Clear-All {
    Write-Warning "Esta ação irá remover todos os containers, volumes e imagens!"
    $confirmation = Read-Host "Tem certeza? (y/N)"
    if ($confirmation -eq "y" -or $confirmation -eq "Y") {
        Write-Info "Limpando tudo..."
        docker-compose down -v --rmi all
        docker system prune -f
        Write-Info "Limpeza concluída"
    }
    else {
        Write-Info "Operação cancelada"
    }
}

# Função para mostrar ajuda
function Show-Help {
    Write-Host "Uso: .\start-docker.ps1 [COMANDO]" -ForegroundColor White
    Write-Host ""
    Write-Host "Comandos disponíveis:" -ForegroundColor White
    Write-Host "  start     - Inicia todos os serviços" -ForegroundColor White
    Write-Host "  stop      - Para todos os serviços" -ForegroundColor White
    Write-Host "  restart   - Reinicia todos os serviços" -ForegroundColor White
    Write-Host "  logs      - Mostra logs de todos os serviços" -ForegroundColor White
    Write-Host "  logs [SERVIÇO] - Mostra logs de um serviço específico" -ForegroundColor White
    Write-Host "  status    - Mostra status dos serviços" -ForegroundColor White
    Write-Host "  clean     - Remove todos os containers, volumes e imagens" -ForegroundColor White
    Write-Host "  help      - Mostra esta ajuda" -ForegroundColor White
    Write-Host ""
    Write-Host "Exemplos:" -ForegroundColor White
    Write-Host "  .\start-docker.ps1 start" -ForegroundColor White
    Write-Host "  .\start-docker.ps1 logs backend" -ForegroundColor White
    Write-Host "  .\start-docker.ps1 status" -ForegroundColor White
}

# Função principal
function Main {
    Write-Header
    
    # Verificações iniciais
    if (-not (Test-Docker)) { exit 1 }
    if (-not (Test-DockerCompose)) { exit 1 }
    New-EnvFile
    
    # Processa comandos
    switch ($Command.ToLower()) {
        "start" {
            Start-Services
        }
        "stop" {
            Stop-Services
        }
        "restart" {
            Restart-Services
        }
        "logs" {
            Show-Logs
        }
        "status" {
            Show-Status
        }
        "clean" {
            Clear-All
        }
        "help" {
            Show-Help
        }
        default {
            Write-Error "Comando inválido: $Command"
            Show-Help
            exit 1
        }
    }
}

# Executa função principal
Main
