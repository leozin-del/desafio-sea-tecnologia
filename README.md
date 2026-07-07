# 📇 Client API
### Sistema de Cadastro de Clientes

<div align="center">

[![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot_4.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL_8-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)

</div>

---

## 🚀 Sobre o Projeto

O **Client API** é um sistema backend desenvolvido com **Spring Boot** como solução para o **Desafio Backend** do processo seletivo da **SEA Tecnologia**, com o objetivo de gerenciar o cadastro de clientes de forma completa e segura.

A aplicação permite o controle de:

| Módulo | Descrição |
|--------|-----------|
| 👤 **Clientes** | Cadastro e gestão completa de clientes |
| 📞 **Telefones** | Múltiplos telefones por cliente com validação por tipo |
| 📧 **E-mails** | Múltiplos e-mails por cliente com validação de formato |
| 🏠 **Endereços** | Preenchimento automático via integração com **ViaCEP** |
| 🔐 **Segurança** | Controle de acesso com perfis `ADMIN` e `PADRAO` |

> 🖥️ **Este repositório contém apenas o back-end.**
> O front-end em React está disponível em:
> **[👉 desafio-sea-tecnologia-front](https://github.com/leozin-del/desafio-sea-tecnologia-front)**

---

## ☕ Decisão Técnica: Java 21 em vez de Java 8

O desafio original especifica **Java 8** como requisito de stack. Optei por utilizar **Java 21** pelos seguintes motivos:

> **Esta decisão foi alinhada previamente com a equipe de recrutamento da SEA Tecnologia.**

| Motivo | Explicação |
|--------|-----------|
| **Compatibilidade** | Spring Boot 4.1 exige no mínimo Java 17 — Java 8 não é compatível |
| **LTS mais recente** | Java 21 é a versão Long-Term Support mais atual |
| **Records para DTOs** | Código mais limpo, imutável e sem boilerplate |
| **`Stream.toList()`** | Alternativa mais concisa ao `.collect(Collectors.toList())` |

---

## 🛠️ Tecnologias Utilizadas

- ☕ **Java 21**
- 🌱 **Spring Boot 4.1.0**
- 🔒 **Spring Security** — autenticação e autorização
- 🗄️ **Spring Data JPA / Hibernate**
- 🐬 **MySQL 8**
- 🔧 **Maven**
- ✈️ **Flyway** — versionamento do banco de dados
- 🌐 **RestTemplate** — integração com API do ViaCEP
- 🧪 **JUnit 5 + Mockito** — testes unitários

---

## 📦 Arquitetura

A aplicação segue um padrão de camadas bem definido:

```
Controller   →   recebe as requisições HTTP
    ↓
Service      →   aplica as regras de negócio
    ↓
Repository   →   realiza o acesso ao banco de dados
    ↓
Entity       →   representa o mapeamento das tabelas
```

| Camada | Responsabilidade |
|--------|-----------------|
| **Controller** | Recebe requisições, delega ao Service e retorna a resposta HTTP |
| **Service** | Regras de negócio, validações, normalização de CPF/CEP/telefone |
| **Repository** | Acesso a dados via Spring Data JPA |
| **Entity** | Modelo de domínio mapeado via JPA/Hibernate |
| **DTO** | Records imutáveis para contrato de entrada/saída da API |
| **Config** | Configurações de segurança e RestTemplate |
| **Exception** | Exceções de negócio com status HTTP automático |
| **Client** | Integração HTTP com a API do ViaCEP |

---

## ▶️ Como Executar/ PASSO A PASSO

### 🔹 Pré-requisitos

- JDK 21 instalado
- MySQL 8+ rodando localmente
- Maven *(já incluso no wrapper `./mvnw` do projeto)*

### 🔹 Passo a Passo — Back-end

```bash
# 1. Clonar o repositório
git clone https://github.com/leozin-del/desafio-sea-tecnologia.git

# 2. Entrar na pasta do projeto
cd desafio-sea-tecnologia
```

**3. Criar o banco de dados no MySQL:**
```sql
CREATE DATABASE clientapi;
```

> As tabelas são criadas **automaticamente pelo Flyway** na primeira execução.

**4. Configurar o `src/main/resources/application.properties`:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/clientapi?useSSL=false&serverTimezone=UTC
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

```bash
# 5. Executar a aplicação
./mvnw spring-boot:run
```

✅ API disponível em `http://localhost:8080`

---

### 🔹 Passo a Passo — Front-end (opcional)

```bash
# 1. Clonar o repositório do front-end
git clone https://github.com/leozin-del/desafio-sea-tecnologia-front.git

# 2. Entrar na pasta
cd desafio-sea-tecnologia-front

# 3. Instalar dependências
npm install

# 4. Executar
npm run dev
```

✅ Front-end disponível em `http://localhost:5173`

> ⚠️ **Importante:** suba o back-end **antes** de iniciar o front-end.

---

### 🔹 Acessar o Sistema

Com os dois rodando, abra `http://localhost:5173` e faça login:

| Usuário | Senha | Acesso |
|---------|-------|--------|
| `admin` | `123qwe!@#` | Total |
| `padrao` | `123qwe123` | Apenas leitura |

---

## 🧪 Testando a API

Como este projeto é um **backend REST**, você pode testar os endpoints diretamente utilizando:

- 🟠 [Postman](https://www.postman.com/)
- 🟣 [Insomnia](https://insomnia.rest/)

---

## 📊 Estrutura do Banco de Dados

O sistema cria automaticamente as seguintes tabelas via Flyway:

```
📁 clientapi
 ├── 👤 cliente
 ├── 📞 cliente_telefone
 ├── 📧 cliente_email
 └── 🏠 cliente_endereco
```

---

## 📡 Endpoints da API

### 🟢 `POST /clientes` — Criar cliente *(ADMIN)*
### 🔵 `GET /clientes` — Listar todos *(ADMIN ou PADRAO)*
### 🔵 `GET /clientes/{id}` — Buscar por ID *(ADMIN ou PADRAO)*
### 🟡 `PUT /clientes/{id}` — Atualizar cliente *(ADMIN)*
### 🔴 `DELETE /clientes/{id}` — Remover cliente *(ADMIN)*
### 🔵 `GET /cep/{cep}` — Consultar CEP via ViaCEP *(ADMIN)*

---

## 🔐 Segurança

A API utiliza **HTTP Basic Authentication** via Spring Security, com dois usuários em memória e senhas criptografadas com **BCrypt**:

| Usuário | Papel | Permissão |
|---------|-------|-----------|
| `admin` | `ADMIN` | Acesso total: criar, listar, buscar, atualizar, excluir e consultar CEP |
| `padrao` | `PADRAO` | Apenas leitura: `GET /clientes` e `GET /clientes/{id}` |

- ✅ CSRF desabilitado *(API stateless)*
- ✅ CORS liberado para `http://localhost:5173`
- ✅ Preflight `OPTIONS` liberado sem autenticação

---

## 🔐 Regras de Negócio

- CPF é persistido **sem máscara** no banco e retornado **com máscara** na resposta
- Telefone é persistido **sem máscara** e retornado **com máscara** conforme o tipo
- CEP é persistido **sem máscara** e retornado **com máscara**
- Endereço é preenchido automaticamente via **ViaCEP** ao informar o CEP
- CPF duplicado retorna `409 Conflict`
- Cliente não encontrado retorna `404 Not Found`

---

## 👨‍💻 Autor

<div align="center">

Desenvolvido por **Leonardo Grillo** 🧙‍♂️

</div>

---

<div align="center">

📇 **Client API** — *Cadastro de clientes com segurança e boas práticas*

</div>
