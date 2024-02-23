<h1> Olá! Bem-vindo ao projeto HARDSTOP.</h1>

Este projeto foi feito usando a plataforma Quarkus e as linguagens: Java, SQL (através do SGBD PostgreSQL), AngularJS, NodeJS.

Como fazer para executar o projeto:

<h3>BANCO DE DADOS</h1>

*Importante ter o PostgreSQL instalado, a partir da versão 14.*

**1. Criar usuário no PgAdmin com as seguintes informações de usuário:**

```java
Nome de usuário: topicos2
Senha: 123456
```
**2. Criar o banco de dados informando o usuário 'topicos2' como admin.**

```
Nome da base de dados: topicos2db
```

**3. Atualizar endereço do Postgres no arquivo application.propeties no seguinte caminho:**
```
.\backend\src\main\resources\application.properties
```

<h3>BACKEND</h3>

**1. Verificar instalação do JDK 11 ou 17, os suportados pelo Quarkus (recomendamos o Java 17). Digite o comando:**

```
java --version
```
**2. Compilar o Quarkus e iniciar execução do servidor através dos comandos (usar Terminal ou PowerShell):**
```
cd ./backend
./mvnw compile quarkus:dev
```
<h3>FRONTEND</h3>

*Necessário ter o NodeJS na versão mais atualizada.*

**1. Verificar instalação do NodeJS através do comando:**
```
cd ./frontend
node --version
```

**2. Instalar os módulos do Node, através do comando:**
```
npm install
```
**3. Compilar o Angular e iniciar execução do servidor com o comando:**
```
ng serve
```