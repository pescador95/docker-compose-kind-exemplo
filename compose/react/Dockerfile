# Use a base image Node.js LTS (Long Term Support)
FROM node:lts-alpine as build

# Crie e defina o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie o arquivo package.json e package-lock.json para o diretório de trabalho
COPY /package-lock.json ./
COPY /package.json ./

# Instale as dependências
RUN npm install

# Copie os arquivos do aplicativo React para o diretório de trabalho
COPY . .

# Construa o aplicativo React
RUN npm run build

# Exponha a porta 6000 para acesso ao servidor Nginx
EXPOSE 6000

# Comando para iniciar o servidor Nginx quando o contêiner for iniciado
CMD ["npm", "run", "start"]
