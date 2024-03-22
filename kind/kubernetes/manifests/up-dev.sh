#!/bin/bash

current_path=$(dirname "$0")

AVALIABLE_SERVICES=("backend" "frontend" "telegram" "redis")

up() {
    IFS=',' read -ra SERVICES <<< "$1"
    for service in "${SERVICES[@]}"; do
        found=false
        for avaliable_service in "${AVALIABLE_SERVICES[@]}"; do
            if [ "$service" == "$avaliable_service" ]; then
                found=true
                echo "Subindo serviço: $service"
                kubectl apply -f "${current_path}/staging/${service}.yaml"
            fi
        done
        if [ "$found" == false ]; then
            echo "Serviço '$service' não encontrado."
        fi
    done
}

echo "serviços disponíveis: ${AVALIABLE_SERVICES[@]}"
echo "Insira os serviços que deseja subir separados por vírgula (ex: backend,frontend,telegram):"
read services

up "$services"