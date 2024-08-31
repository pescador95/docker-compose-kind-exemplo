#!/bin/bash

current_path=$(dirname "$0")

cd "$current_path"

if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    terminal="konsole"
    elif [[ "$OSTYPE" == "msys"* ]]; then
    terminal="start"
fi

arch=$(uname -m)

current_branch=$(git branch --show-current)
echo "Executando o script na branch: $current_branch"
echo "Build and Run - Projeto: Docker Compose Exemplo... ##############"
echo "Executando docker compose down..."
echo "Apagando imagens e containers..."
docker compose -p compose-example down --volumes
docker compose -p compose-example rm -f
if [[ $(docker images -q pescador95/compose-example:db) ]]; then
    docker image rm pescador95/compose-example:db
fi
docker image rm pescador95/compose-example:quarkus
docker image rm pescador95/compose-example:node

echo "Arquitetura detectada: $arch"
echo "Criando imagens e containers..."
docker compose -p compose-example -f docker-compose.yml build
echo "Executando docker compose up..."
docker compose -p compose-example -f docker-compose.yml up -d

echo "Containers em execução: ##################################"
docker ps
echo "##########################################################"
echo "docker compose Executado com sucesso!"
read -p "Pressione Enter para sair..."