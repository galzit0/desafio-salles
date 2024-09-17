# API de Contas a Pagar

Este projeto é uma API REST para o gerenciamento de contas a pagar. Ele permite o CRUD de contas a pagar, a alteração de sua situação quando o pagamento é efetuado, a consulta de contas cadastradas e a importação de contas através de um arquivo CSV.

## Funcionalidades
- **Cadastrar uma conta a pagar**.
- **Atualizar uma conta a pagar**.
- **Alterar a situação de uma conta** (quando paga ou pendente).
- **Consultar uma lista de contas** com filtro por data de vencimento e descrição.
- **Consultar uma conta por ID**.
- **Obter o valor total pago em um período**.
- **Importar contas a pagar de um arquivo CSV**.

## Tecnologias utilizadas

- **Linguagem**: Java 17.
- **Framework**: Spring Boot.
- **Banco de Dados**: PostgreSQL (para rodar localmente sem o docker).
- **Containerização**: Docker.
- **Orquestração**: Docker Compose.
- **Migração de Banco de Dados**: Flyway.

### Entidade: Conta

A tabela de contas contém os seguintes campos:

- `id`: Identificador único.
- `data_vencimento`: Data de vencimento da conta.
- `data_pagamento`: Data do pagamento.
- `valor`: Valor da conta.
- `descricao`: Descrição da conta.
- `situacao`: Situação da conta (PENDENTE, PAGA e ATRASADA).

## Configuração e Execução

### Pré-requisitos

- Docker
- Java 17+
- Intelij (Ou uma IDE de sua preferência)

### Passos para Executar o Projeto

1. **Clone o repositório**

   ```bash
   git clone https://github.com/galzit0/desafio-salles.git
   ```

2. **Configuração do ambiente**

Certifique-se de que os arquivos application.yml, application-dev.yml e application-prod.yml estão configurados corretamente. 
Aqui estão os parâmetros importantes para configurar:

- Banco de Dados: O arquivo application-prod.yml deve estar configurado com as credenciais e URL do banco de dados 
PostgreSQL rodando no Docker, para rodar localmente, basta criar um banco de dados e informar as credenciais bem como o 
nome do banco no application-dev.yml.


- Docker Compose: No arquivo docker-compose.yml, a aplicação e o banco de dados PostgreSQL estão configurados para 
rodar em containers separados.

3. **Buildado a aplicação e execute via Docker Compose**

Para buildar a aplicação, execute:

```bash
   mvn clean package 
   ```
   
O comando acima irá:

- Limpar a pasta target e gerar o .jar da aplicação.

Para rodar a aplicação com Docker Compose, execute:

 ```bash
   docker-compose up --build
   ```
O comando acima irá:

Criar e iniciar os containers da aplicação e do PostgreSQL.
Executar as migrações do Flyway para criar a tabela no banco de dados.

A aplicação estará disponível em http://localhost:8081/api/contas.


### Importação de Contas via CSV

Para importar contas a partir de um arquivo CSV, utilize a seguinte rota:

- URL: POST /api/contas/importar

- Descrição: Importa um lote de contas a pagar a partir de um arquivo CSV.

- Exemplo de CSV (DEVE SEGUIR ESSE PADRÃO DE LAYOUT):

```
   descricao,data_vencimento,data_pagamento,valor,situacao
   Conta de luz,2024-09-28,2024-09-30,100.00,PAGO
   Conta de água,2024-09-29,2024-10-01,50.00,PENDENTE
   ```
