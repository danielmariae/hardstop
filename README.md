# 🖥️ HARDSTOP — E-commerce de Hardware

**HARDSTOP** é um e-commerce especializado na venda de **placas de vídeo** e **processadores** de computadores.
O sistema foi desenvolvido com foco em **performance**, **segurança** e **modularidade**, integrando frontend, backend e banco de dados de forma coesa.

---

## 🧩 Estrutura do Projeto

O projeto é composto por três principais módulos:

| Módulo                                      | Descrição                                                                                            | Tecnologias                            |
| ------------------------------------------- | ---------------------------------------------------------------------------------------------------- | -------------------------------------- |
| **Backend (API)**                           | Responsável pela lógica de negócio, autenticação, gestão de clientes, produtos, pedidos e logística. | Quarkus • Java 17 • PostgreSQL • Maven |
| **Frontend (Painel Administrativo e Loja)** | Interface para clientes e administradores.                                                           | AngularJS • NodeJS                     |
| **Banco de Dados**                          | Armazena e gerencia os dados da aplicação.                                                           | PostgreSQL 14+                         |

---

## ⚙️ Pré-requisitos

Antes de executar o projeto, verifique se as seguintes dependências estão instaladas:

* **Java JDK** 11 ou 17 (recomendado: 17)
* **NodeJS** (versão mais recente)
* **PostgreSQL** 14 ou superior
* **Maven Wrapper** (`./mvnw`) incluído no projeto

---

## 🗃️ Configuração do Banco de Dados

1. **Criar o usuário no PgAdmin**:

```bash
Usuário: topicos2
Senha: 123456
```

2. **Criar o banco de dados**:

```bash
Nome: topicos2db
Dono: topicos2
```

3. **Configurar o arquivo `application.properties`**:

Local:

```
backend/src/main/resources/application.properties
```

Exemplo de configuração:

```properties
quarkus.datasource.username=topicos2
quarkus.datasource.password=123456
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/topicos2db
```

---

## 🚀 Execução do Backend (Quarkus)

1. **Verifique a versão do Java:**

```bash
java --version
```

2. **Compile e inicie o servidor Quarkus:**

```bash
cd ./backend
./mvnw compile quarkus:dev
```

O servidor será iniciado em:

```
http://localhost:8080
```

---

## 💻 Execução do Frontend (Angular)

1. **Verifique a versão do NodeJS:**

```bash
node --version
```

2. **Instale as dependências:**

```bash
cd ./frontend
npm install
```

3. **Execute o servidor Angular:**

```bash
ng serve
```

O frontend será iniciado em:

```
http://localhost:4200
```

---

## 📚 Documentação da API

A API RESTful está documentada conforme o padrão **OpenAPI 3.0**.
Principais grupos de endpoints disponíveis:

| Grupo                   | Função                                       | Exemplos de Endpoints                                                    |
| ----------------------- | -------------------------------------------- | ------------------------------------------------------------------------ |
| **Auth Resource**       | Autenticação de usuários                     | `/auth/loginF`, `/auth/loginU`                                           |
| **Cliente Resource**    | CRUD de clientes, imagens e lista de desejos | `/cliente`, `/cliente/upload/imagem`, `/cliente/insert/desejos`          |
| **Produto Resource**    | Gestão de produtos e categorias              | `/produtos`, `/produtos/insert/processador`, `/produtos/insert/placaMae` |
| **Fornecedor Resource** | Gerenciamento de fornecedores                | `/fornecedores`, `/fornecedores/{id}`                                    |
| **Pedido Resource**     | Criação e controle de pedidos                | `/pedidos/insert`, `/pedidos/patch/status`                               |
| **Logística Resource**  | Controle de entrega e transporte             | `/logistica`, `/logistica/put/{id}`                                      |
| **Enum Resource**       | Retorna modelos auxiliares                   | `/enum/perfil`, `/enum/statusPedido`                                     |

Para consultar a documentação completa, após o deploy acesse:

```
http://localhost:8080/q/dev-ui/io.quarkus.quarkus-smallrye-openapi/schema-json
[Caso esteja usando outra porta, substitua pela que você estiver usando.]
```

Você pode importar esse arquivo em ferramentas como **Postman** ou **Swagger UI** para explorar os endpoints.

---

## 🧠 Tecnologias Utilizadas

* **Backend:** Java 17, Quarkus, Maven
* **Frontend:** AngularJS, NodeJS
* **Banco de Dados:** PostgreSQL
* **Ferramentas Auxiliares:** Swagger (OpenAPI), PgAdmin

---

## 📦 Estrutura de Diretórios

```
HARDSTOP/
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── application.properties
├── frontend/
│   ├── src/
│   ├── package.json
│   └── angular.json
└── README.md
```

---

## 👥 Autores

Projeto desenvolvido por **Lucas Daniel Rodrigues dos Santos** e equipe, como parte da disciplina de **Tópicos em Programação II**.
Este sistema tem como foco aprimorar o aprendizado em **arquiteturas web modernas** e **integração full-stack**.

---

## 🛡️ Licença

Este projeto é distribuído sob a licença **MIT**.
Consulte o arquivo `LICENSE` para mais detalhes.