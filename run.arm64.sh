#!/bin/bash

current_path=$(dirname "$0")

cd "$current_path"

if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    terminal="konsole"
    elif [[ "$OSTYPE" == "msys"* ]]; then
    terminal="start"
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
if [[ $(docker images -q ${github_username}/compose-example:db) ]]; then
    docker image rm ${github_username}/compose-example:db
fi
docker image rm ${github_username}/compose-example:quarkus

echo "Criando imagens e containers..."
docker-compose -p compose-example -f docker-compose.arm64.yml build
echo "Executando docker-compose up..."
docker-compose -p compose-example -f docker-compose.arm64.yml up -d

echo "Containers em execução: ##################################"
docker ps
echo "##########################################################"
echo "docker-compose Executado com sucesso!"