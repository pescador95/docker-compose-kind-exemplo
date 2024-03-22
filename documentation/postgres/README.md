
# PostgreSQL

O PostgreSQL é um sistema de gerenciamento de banco de dados relacional de código aberto e poderoso. É conhecido por sua confiabilidade, robustez, conformidade com padrões e recursos avançados.

A documentação geral sobreo PostgreSQL pode ser encontrada em: [Documentação Oficial](https://www.postgresql.org/docs/)


## Conectar-se ao PostgreSQL

Após a instalação, você pode conectar-se ao PostgreSQL usando a linha de comando psql ou através de clientes de banco de dados GUI como pgAdmin ou DBeaver.

- `psql -U <nome-do-usuario> -d <nome-do-banco-de-dados>`

## Principais Comandos do PostgreSQL

Aqui estão alguns dos principais comandos do PostgreSQL que você pode achar úteis:

- `\l`: Lista todos os bancos de dados.

- `\c <nome-do-banco-de-dados>`: Conecta-se a um banco de dados específico.

- `\dt`: Lista todas as tabelas no banco de dados atual.

- `\d <nome-da-tabela>`: Exibe a estrutura de uma tabela específica.

- `CREATE DATABASE <nome-do-banco-de-dados>`: Cria um novo banco de dados.

- `CREATE TABLE <nome-da-tabela> (...)`: Cria uma nova tabela.

- `SELECT * FROM <nome-da-tabela>`: Recupera todos os dados de uma tabela.

[voltar](../../README.md)