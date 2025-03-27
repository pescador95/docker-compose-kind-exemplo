#!/bin/bash

echo "🔐 Gerando novas chaves JWT..."

mkdir -p src/main/resources/META-INF/resources

openssl genrsa -out src/main/resources/privateKey.pem 2048

openssl rsa -in src/main/resources/privateKey.pem -pubout -out src/main/resources/META-INF/resources/publicKey.pem

echo "✅ Chaves geradas com sucesso!"
echo "⚠️  NÃO use essas chaves em produção. Gere novas chaves seguras."
