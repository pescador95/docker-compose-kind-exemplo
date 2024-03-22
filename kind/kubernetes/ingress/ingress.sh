#!/bin/bash

current_path=$(dirname "$0")

cd $current_path

echo "Subindo ingress"
kubectl apply -f "ingress.yaml"