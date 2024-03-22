
# Docker e Docker Compose

O Docker é uma plataforma de código aberto que permite aos desenvolvedores empacotar, distribuir e executar aplicativos em contêineres. Um contêiner é uma unidade leve e portátil de software que inclui tudo o que é necessário para executar um aplicativo: código, runtime, bibliotecas, variáveis de ambiente e dependências. Ao usar o Docker, os desenvolvedores podem criar ambientes consistentes e isolados para seus aplicativos, independentemente do ambiente de execução. Consulte a [documentação oficial](https://docs.docker.com/).

## build and Run

Você pode executar o projeto containerizado através do script `run.sh`...

## Docker Compose

 Alguns Exemplos e instruções de comandos para usar com o docker.

## Criação Container

Para criar o container

- `docker build -t {containername} .`


## Execução Container

Para  rodar o container

- `docker run -it {containername} bash`

## Remoção Containers e Imagens

Para  apagar imagens 

docker image rm -f  `docker images -q\``

Para apagar containers 

docker container rm -f \`docker ps -aq\`

Para apagar imagens e containers

docker image rm -f  \`docker images -q\` && docker container rm -f \`docker ps -aq\`

## Localizar Container

Para pegar ip do container rodando

- `docker inspect {containername} | grep IPAddress`

## Executar Compose

Para subir docker compose

- `docker-compose up`

Para baixar docker compose

- `docker-compose down`

## Apagar Cache

Para remover cache do build

- `docker builder prune --all`

## Criar Rede no Docker

- `docker network create --driver bridge --label 'com.docker.compose.network=default' --label 'com.docker.compose.project={project_name}' internal-network`

- `docker network create --driver bridge --label 'com.docker.compose.network=default' --label 'com.docker.compose.project={project_name}' external-network`

[voltar](../../README.md)