#!/bin/bash

kubectl create secret generic database-user --from-literal=database-user="$DATABASE_USER"
kubectl create secret generic database-password --from-literal=database-password="$DATABASE_PASSWORD"
kubectl create secret generic database-url --from-literal=database-url="$DATABASE_URL"

kubectl create secret generic email-user --from-literal=email-user="$EMAIL_USER"
kubectl create secret generic email-password --from-literal=email-password="$EMAIL_PWD"

kubectl create secret generic telegram-token --from-literal=telegram-token="$TELEGRAM_TOKEN"