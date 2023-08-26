#!/bin/bash

git update-index --assume-unchanged run.sh

current_path=$(dirname "$0")

cd "$current_path"

if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    terminal="konsole"
    elif [[ "$OSTYPE" == "msys"* ]]; then
    terminal="start"
fi

arch=$(uname -m)

if [ "$arch" = "x86_64" ]; then
    compose_file="docker-compose-x86x64.yml"
    elif [ "$arch" = "aarch64" ]; then
    compose_file="docker-compose.arm64.yml"
else
    echo "Arquitetura não suportada: $arch"
fi

current_branch=$(git branch --show-current)
echo "Recebendo atualizações da main..."
git switch main
git pull
echo "Executando o script na branch: $current_branch"
echo "Build and Run - Projeto: Docker Compose Exemplo... ##############"
echo "Executando docker-compose down..."
echo "Apagando imagens e containers..."
docker-compose -p compose-example down --volumes
docker-compose -p compose-example rm -f
if [[ $(docker images -q compose-example:db) ]]; then
    docker image rm compose-example:db
fi
docker image rm compose-example:quarkus
docker image rm compose-example:node

echo "Arquitetura detectada: $arch"
echo "Criando imagens e containers..."
docker-compose -p compose-example -f "$compose_file" build
echo "Executando docker-compose up..."
docker-compose -p compose-example -f "$compose_file" up -d

echo "Containers em execução: ##################################"
docker ps
echo "##########################################################"
echo "docker-compose Executado com sucesso!"
read -p "Pressione Enter para sair..."
git update-index --no-assume-unchanged run.sh