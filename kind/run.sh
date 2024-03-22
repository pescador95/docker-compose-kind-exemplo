#!/bin/bash

run() {
    echo "Iniciando trabalhos no modo de ambiente escolhido: $1"
        make teardown
        make setup
        ./kubernetes/secrets/secrets.sh
    if [ "$1" == "dev" ]; then
        ./kubernetes/manifests/up-dev.sh <<< "redis,backend,frontend,telegram"
    elif [ "$1" == "prod" ]; then
        ./kubernetes/manifests/up.sh <<< "redis,backend,frontend,telegram,whatsapp"
    else
        echo "Modo de ambiente inválido."
    fi
    ./kubernetes/ingress/ingress.sh
}

echo " "
echo "Modos de ambiente disponíveis: "
echo " "
echo "dev"
echo "prod"
echo " "
echo "Por favor, escolha o modo de ambiente a iniciar:"
echo " "
read input_string
echo " "

if [ "$input_string" != "dev" ] && [ "$input_string" != "prod" ]; then
    echo "Modo de ambiente inválido."
    exit 1
fi

run "$input_string"