# Use uma imagem base adequada para o Redis
FROM redis:latest

# Copie arquivos de configuração personalizados, se necessário
COPY redis.conf /usr/local/etc/redis/redis.conf

# Se você precisar realizar alguma configuração específica, pode fazê-lo aqui

# Defina as variáveis de ambiente, se necessário
ENV REDIS_USER ${REDIS_USER}
ENV REDIS_PASSWORD ${REDIS_PASSWORD}

# Exponha a porta do Redis (6379 é a porta padrão)
EXPOSE 6379

# Comando para iniciar o serviço Redis
CMD ["redis-server"]
