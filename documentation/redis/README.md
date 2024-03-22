# Redis

O Redis é um armazenamento de estrutura de dados em memória de código aberto, amplamente utilizado como banco de dados em cache, armazenamento de sessão e sistema de mensagens. Ele fornece uma variedade de estruturas de dados, incluindo strings, hashes, listas, conjuntos, conjuntos classificados e muito mais. Este README fornecerá uma breve introdução ao Redis, incluindo informações sobre sua instalação, principais comandos e links para a [Documentação Oficial do Redis](https://redis.io/documentation).

## Instalação

Para instalar o Redis em seu sistema, você pode seguir as instruções fornecidas na [documentação oficial do Redis](https://redis.io/download).

## Principais Comandos do Redis

Aqui estão alguns dos principais comandos do Redis que você pode achar úteis:

- `SET chave valor`: Define o valor da chave.
- `GET chave`: Obtém o valor da chave.
- `DEL chave`: Exclui uma chave.
- `HSET chave campo valor`: Define um campo e valor em um hash.
- `HGET chave campo`: Obtém o valor de um campo em um hash.
- `LPUSH chave valor [valor ...]`: Insere um ou mais valores no início de uma lista.
- `RPUSH chave valor [valor ...]`: Insere um ou mais valores no final de uma lista.
- `LPOP chave`: Remove e retorna o primeiro elemento de uma lista.
- `RPOP chave`: Remove e retorna o último elemento de uma lista.
- `SADD chave membro [membro ...]`: Adiciona um ou mais membros a um conjunto.
- `SMEMBERS chave`: Retorna todos os membros de um conjunto.
- `ZADD chave pontuação membro [pontuação membro ...]`: Adiciona um ou mais membros a um conjunto classificado.

Para uma lista completa de comandos e funcionalidades, consulte a [documentação oficial do Redis](https://redis.io/commands).

[voltar](../../README.md)