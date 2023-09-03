# Define a imagem base
FROM node:18

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia os arquivos do projeto para o diretório de trabalho
COPY / package*.json ./

# Instala as dependências
RUN npm install

# Copia o restante dos arquivos do projeto para o diretório de trabalho
COPY / .

# Define as variáveis de ambiente
ENV QUARKUS_PORT=${QUARKUS_PORT}
ENV QUARKUS_URL=backend
ENV QUARKUS_BASEURL=http://backend:${QUARKUS_PORT}
ENV API_BACKEND_LOGIN=${API_BACKEND_LOGIN}
ENV API_BACKEND_PASSWORD=${API_BACKEND_PASSWORD}
ENV PORT=${NODE_PORT}

# Expõe a porta definida na variável PORT
EXPOSE $PORT

# Inicia o servidor da API
CMD ["npm", "start"]
