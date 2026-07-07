# 📇 Client API — Sistema de Cadastro de Clientes

## 🎯 Sobre o Projeto

API REST desenvolvida como solução para o **Desafio Backend** da SEA Tecnologia, com o objetivo de gerenciar o cadastro de clientes contendo dados pessoais, endereço, telefones e e-mails.

O sistema conta com autenticação obrigatória via **Basic Auth**, validação de regras de negócio (formato de CPF, telefone, e-mail e endereço), persistência com **MySQL** versionada por **Flyway** e integração com o serviço público **ViaCEP** para consulta de endereços a partir do CEP informado.

---

## ☕ Decisão Técnica: Java 21 em vez de Java 8

O desafio original especificava **Java 8** como requisito de stack. Optei conscientemente por utilizar **Java 21** pelos seguintes motivos:

- **Compatibilidade obrigatória**: o projeto utiliza **Spring Boot 4.1.0**, cujo baseline mínimo de linguagem é **Java 17** — ou seja, não é possível rodar essa versão do Spring Boot em Java 8.
- **LTS mais recente**: o Java 21 é a versão *Long-Term Support* mais atual disponível, garantindo suporte de longo prazo e acesso a recursos modernos da linguagem.
- **Records para DTOs**: todos os DTOs do projeto (`ClienteRequestDTO`, `ClienteResponseDTO`, `EnderecoDTO`, `TelefoneDTO`, `ViaCepResponseDTO`) são implementados como `record`, tornando o código mais **enxuto, imutável e livre de boilerplate** (sem getters, construtores e `equals/hashCode` escritos manualmente).
- **`Stream.toList()`**: em substituição a `.collect(Collectors.toList())`, utilizado em todos os mapeamentos de listas no `ClienteService`, deixando o código mais conciso e legível.

Essa decisão prioriza aderência a uma stack moderna e sustentável, mantendo a essência do desafio (Java + Spring + JPA + Maven).

---

## ✅ Funcionalidades Implementadas

- Cadastro de cliente com nome, CPF, endereço, telefones e e-mails.
- Consulta de cliente por ID.
- Listagem de todos os clientes cadastrados.
- Atualização parcial de cliente (campos não enviados são preservados).
- Remoção de cliente.
- Validação de nome (obrigatório, 3 a 100 caracteres, apenas letras, números e espaços).
- Validação de CPF (obrigatório, aceita formato `999.999.999-99` ou 11 dígitos).
- Persistência do CPF **sem máscara** no banco e retorno **com máscara** na resposta da API.
- Validação de e-mail (obrigatório ao menos um, formato válido).
- Suporte a múltiplos e-mails por cliente.
- Suporte a múltiplos telefones por cliente, com tipo (`CELULAR`, `RESIDENCIAL` ou `COMERCIAL`).
- Validação de quantidade de dígitos do telefone conforme o tipo (11 dígitos para celular, 10 para os demais).
- Persistência do telefone **sem máscara** e retorno **com máscara** (ex.: `(11) 91234-5678`).
- Persistência do CEP **sem máscara** e retorno **com máscara** (ex.: `01310-100`).
- Bloqueio de cadastro/atualização com CPF duplicado.
- Consulta de endereço por CEP via integração com a API pública do **ViaCEP**.
- Tratamento de erro quando o CEP não é encontrado.
- Autenticação HTTP Basic com dois perfis de usuário (administrador e padrão), com permissões distintas.
- Tratamento de exceções de negócio com status HTTP apropriados (`404` e `409`).
- Controle de CORS liberado para um frontend rodando em `http://localhost:5173`.
- Versionamento e criação automática do schema do banco via Flyway.

---

## 🛠️ Tecnologias Utilizadas

Levantadas diretamente do `pom.xml`:

- **Java 21**
- **Spring Boot 4.1.0** (`spring-boot-starter-parent`)
- **Spring Web MVC** (`spring-boot-starter-webmvc`) — construção da API REST
- **Spring Data JPA** (`spring-boot-starter-data-jpa`) — persistência e repositórios
- **Spring Security** (`spring-boot-starter-security`) — autenticação e autorização
- **Spring Validation** (`spring-boot-starter-validation`) — validação de DTOs com Bean Validation
- **Spring Boot RestClient** (`spring-boot-starter-restclient`) — suporte a `RestTemplate` para chamadas HTTP externas
- **Flyway** (`spring-boot-starter-flyway` + `flyway-mysql`) — versionamento e migração do banco de dados
- **MySQL Connector/J** (`mysql-connector-j`) — driver JDBC para MySQL
- **Lombok** — redução de boilerplate nas entidades JPA (getters/setters/construtores)
- **Spring Boot DevTools** — hot reload em ambiente de desenvolvimento
- **Maven** — gerenciador de build e dependências
- Dependências de teste: `spring-boot-starter-data-jpa-test`, `spring-boot-starter-flyway-test`, `spring-boot-starter-security-test`, `spring-boot-starter-validation-test`, `spring-boot-starter-webmvc-test` (JUnit 5 e Mockito, via starter de testes do Spring Boot)

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas tradicional do ecossistema Spring:

- **Controller** (`controller/`): expõe os endpoints REST (`ClienteController`, `ConsultaCepController`), recebe as requisições HTTP, delega a regra de negócio para a camada de serviço e devolve o `ResponseEntity` apropriado.
- **Service** (`service/`): concentra as regras de negócio (`ClienteService`) — normalização e validação de CPF/telefone/CEP, verificação de duplicidade, mapeamento entre entidade e DTO, e orquestração das operações de CRUD.
- **Repository** (`repository/`): interface `ClienteRepository`, que estende `JpaRepository`, responsável pelo acesso a dados e por consultas derivadas (`existsByCpf`, `existsByCpfAndIdNot`).
- **Entity** (`entity/`): classes mapeadas com JPA (`Cliente`, `ClienteEndereco`, `ClienteTelefone`, `ClienteEmail`), representando as tabelas do banco e seus relacionamentos (`@OneToMany`, `@OneToOne`, `@ManyToOne`).
- **DTO** (`DTO/`): `record`s imutáveis usados para entrada e saída de dados (`ClienteRequestDTO`, `ClienteResponseDTO`, `EnderecoDTO`, `TelefoneDTO`, `ViaCepResponseDTO`), isolando o modelo de domínio do contrato exposto pela API.
- **Config** (`config/`): classes de configuração do Spring — `SecurityConfig` (autenticação, autorização e CORS) e `RestTemplateConfig` (bean de `RestTemplate` usado nas chamadas externas).
- **Exception** (`exception/`): exceções de negócio customizadas (`ClienteNaoEncontradoException` e `CpfDuplicadoException`), anotadas com `@ResponseStatus` para mapear automaticamente o código HTTP de resposta.
- **Client** (`client/`): `CepClient`, responsável por consumir a API externa do ViaCEP usando `RestTemplate`, isolando a integração externa do restante da aplicação.

---

## ▶️ Como Executar

### Pré-requisitos

- JDK 21
- Maven (ou usar o wrapper `mvnw`/`mvnw.cmd` incluído no projeto)
- MySQL 8+ em execução localmente (ou acessível pela rede)

### 1. Clonar o repositório

```bash
git clone <url-do-repositorio>
cd client-api
```

### 2. Criar o banco de dados

Crie um schema vazio no MySQL com o nome utilizado na aplicação:

```sql
CREATE DATABASE clientapi;
```

As tabelas (`cliente`, `cliente_telefone`, `cliente_email`, `cliente_endereco`) são criadas automaticamente na primeira execução, através da migration do Flyway localizada em `src/main/resources/db/migration/V1__create_table_cliente.sql`.

### 3. Configurar o `application.properties`

Ajuste as credenciais de acesso ao banco em `src/main/resources/application.properties` conforme o seu ambiente:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/clientapi?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=SUA_SENHA
```

### 4. Executar a aplicação

```bash
./mvnw spring-boot:run
```

A API sobe por padrão em `http://localhost:8080`.

### 5. Autenticação

Todas as requisições exigem **Basic Auth**. Utilize um dos usuários pré-configurados (ver seção [🔐 Segurança](#-segurança)).

---

## 📂 Estrutura de Pastas

```
client-api/
├── src/
│   ├── main/
│   │   ├── java/com/seatecnologia/client_api/
│   │   │   ├── DTO/            # Records de entrada/saída (Request, Response, Endereco, Telefone, ViaCep)
│   │   │   ├── client/         # Integração externa (CepClient -> ViaCEP)
│   │   │   ├── config/         # SecurityConfig, RestTemplateConfig
│   │   │   ├── controller/     # ClienteController, ConsultaCepController
│   │   │   ├── entity/         # Cliente, ClienteEmail, ClienteEndereco, ClienteTelefone
│   │   │   ├── exception/      # ClienteNaoEncontradoException, CpfDuplicadoException
│   │   │   ├── repository/     # ClienteRepository
│   │   │   ├── service/        # ClienteService
│   │   │   └── ClientApiApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/   # Scripts Flyway (V1__create_table_cliente.sql)
│   └── test/
│       └── java/com/seatecnologia/client_api/
│           ├── ClientApiApplicationTests.java
│           └── service/ClienteServiceTest.java
├── pom.xml
└── mvnw / mvnw.cmd
```

---

## 🔄 Fluxo da Aplicação

1. **Autenticação**: toda requisição precisa informar credenciais via Basic Auth (usuário `admin` ou `padrao`). Sem autenticação válida, a API retorna `401 Unauthorized`.
2. **Consulta de CEP (opcional/apoio ao cadastro)**: antes de cadastrar um cliente, é possível chamar `GET /cep/{cep}` para obter logradouro, bairro, cidade e UF a partir do ViaCEP, preenchendo o endereço automaticamente (o usuário ainda pode alterar esses dados antes de enviar o cadastro).
3. **Criação do cliente**: o cliente autenticado envia `POST /clientes` com nome, CPF, endereço, telefones e e-mails. O `ClienteService` valida os dados, remove máscaras de CPF/CEP/telefone, checa duplicidade de CPF e persiste o cliente com seus relacionamentos (endereço, telefones e e-mails) em cascata.
4. **Consulta**: o cliente pode ser buscado individualmente (`GET /clientes/{id}`) ou listado por completo (`GET /clientes`), sempre retornando os dados sensíveis (CPF, telefone, CEP) já formatados com máscara.
5. **Atualização**: `PUT /clientes/{id}` permite atualização parcial — apenas os campos enviados no corpo da requisição são alterados; telefones e e-mails, quando enviados, substituem completamente a lista anterior.
6. **Remoção**: `DELETE /clientes/{id}` remove o cliente e, em cascata, seus telefones, e-mails e endereço.
7. **Tratamento de erros**: exceções de negócio (`ClienteNaoEncontradoException`, `CpfDuplicadoException`) e de validação (`@Valid`) são convertidas automaticamente em respostas HTTP com status e mensagem adequados.

---

## 📡 Documentação dos Endpoints

> Todos os endpoints exigem autenticação Basic Auth (ver seção [🔐 Segurança](#-segurança) para os perfis exigidos em cada um).

### Criar cliente

- **Método:** `POST`
- **URL:** `/clientes`
- **Permissão:** `ADMIN`
- **Descrição:** Cadastra um novo cliente com endereço, telefones e e-mails.

**Body:**
```json
{
  "nome": "Maria Silva",
  "cpf": "123.456.789-00",
  "telefones": [
    { "tipo": "CELULAR", "numero": "11987654321" }
  ],
  "emails": [
    "maria.silva@email.com"
  ],
  "endereco": {
    "cep": "01310-100",
    "logradouro": "Avenida Paulista",
    "bairro": "Bela Vista",
    "cidade": "São Paulo",
    "uf": "SP",
    "complemento": "Apto 12"
  }
}
```

**Respostas:**
- `201 Created` — retorna o cliente criado (com CPF, telefone e CEP mascarados) e header `Location`.
- `400 Bad Request` — dados inválidos (ex.: nome fora do padrão, telefone com quantidade de dígitos incorreta para o tipo).
- `409 Conflict` — já existe cliente cadastrado com o CPF informado.

---

### Buscar cliente por ID

- **Método:** `GET`
- **URL:** `/clientes/{id}`
- **Permissão:** `ADMIN` ou `PADRAO`
- **Descrição:** Retorna os dados de um cliente específico.

**Respostas:**
- `200 OK` — retorna o cliente encontrado.
- `404 Not Found` — cliente não encontrado para o ID informado.

---

### Listar todos os clientes

- **Método:** `GET`
- **URL:** `/clientes`
- **Permissão:** `ADMIN` ou `PADRAO`
- **Descrição:** Retorna a lista completa de clientes cadastrados.

**Respostas:**
- `200 OK` — lista de clientes (pode ser vazia).

---

### Atualizar cliente

- **Método:** `PUT`
- **URL:** `/clientes/{id}`
- **Permissão:** `ADMIN`
- **Descrição:** Atualiza parcialmente os dados de um cliente existente. Apenas os campos enviados são alterados; `telefones` e `emails`, quando enviados, substituem a lista anterior por completo.

**Body** (exemplo — todos os campos são opcionais, mas seguem as mesmas validações do cadastro quando enviados):
```json
{
  "nome": "Maria Silva Souza",
  "telefones": [
    { "tipo": "COMERCIAL", "numero": "1131234567" }
  ]
}
```

**Respostas:**
- `204 No Content` — atualização realizada com sucesso.
- `400 Bad Request` — dados inválidos.
- `404 Not Found` — cliente não encontrado.
- `409 Conflict` — CPF informado já pertence a outro cliente.

---

### Remover cliente

- **Método:** `DELETE`
- **URL:** `/clientes/{id}`
- **Permissão:** `ADMIN`
- **Descrição:** Remove o cliente e seus dados relacionados (endereço, telefones, e-mails).

**Respostas:**
- `204 No Content` — remoção realizada com sucesso.
- `404 Not Found` — cliente não encontrado.

---

### Consultar CEP

- **Método:** `GET`
- **URL:** `/cep/{cep}`
- **Permissão:** `ADMIN`
- **Descrição:** Consulta um CEP na API pública do ViaCEP e retorna logradouro, bairro, cidade e UF correspondentes.

**Respostas:**
- `200 OK` — dados do endereço encontrados.
- `404 Not Found` — CEP inexistente ou não encontrado no ViaCEP.

---

## 🔐 Segurança

A autenticação é feita via **HTTP Basic Auth**, configurada em `SecurityConfig`, com dois usuários mantidos **em memória** (`InMemoryUserDetailsManager`) e senhas criptografadas com **BCrypt**:

| Usuário  | Senha        | Papel (Role) | Permissões                                              |
|----------|--------------|--------------|----------------------------------------------------------|
| `admin`  | `123qwe!@#`  | `ADMIN`      | Acesso total: criar, listar, buscar, atualizar, remover clientes e consultar CEP |
| `padrao` | `123qwe123`  | `PADRAO`     | Acesso apenas de leitura: `GET /clientes` e `GET /clientes/{id}` |

Regras de autorização definidas em `SecurityConfig`:

- Requisições `OPTIONS` são liberadas (necessário para o preflight de CORS).
- `GET /clientes/**` é permitido para os papéis `ADMIN` e `PADRAO`.
- Qualquer outra requisição (`POST`/`PUT`/`DELETE` em `/clientes`, além de `GET /cep/{cep}`) exige o papel `ADMIN`.
- **CSRF** está desabilitado, por se tratar de uma API stateless consumida por um cliente externo (não há sessão de navegador).
- **CORS** está habilitado apenas para a origem `http://localhost:5173` (ambiente do frontend), com os métodos `GET`, `POST`, `PUT`, `DELETE` e `OPTIONS` e os headers `Authorization` e `Content-Type` permitidos.

---

## 🚀 Melhorias Futuras

- **Externalizar credenciais**: usuário/senha do banco (`application.properties`) e usuários da aplicação (`SecurityConfig`) estão hardcoded no código-fonte; o ideal seria movê-los para variáveis de ambiente ou um cofre de segredos.
- **Completar a suíte de testes**: a classe `ClienteServiceTest` já possui a estrutura dos casos de teste (`criarCliente`, `listarPorId`, `listarClientes`, `atualizarCliente`, `deletarCliente`), mas os métodos ainda estão vazios, sem asserções implementadas.
- **Tratamento global de exceções**: hoje as exceções de negócio usam `@ResponseStatus` por classe; um `@ControllerAdvice`/`@ExceptionHandler` centralizado permitiria padronizar o corpo de erro (ex.: incluir campo, timestamp, mensagem) para todas as exceções, inclusive as de validação (`MethodArgumentNotValidException`).
- **Paginação**: `GET /clientes` retorna a lista completa sem paginação, o que não escala para uma base grande de registros.
- **Documentação interativa**: adicionar Swagger/OpenAPI (`springdoc-openapi`) para expor e testar os endpoints via interface web.
- **Filtros de busca**: permitir busca de clientes por nome ou CPF, além da busca por ID.
- **Versionamento de API**: prefixar as rotas (ex.: `/api/v1/clientes`) para permitir evolução futura sem quebrar clientes existentes.
- **Frontend**: o desafio original previa, como diferencial opcional, um frontend em React consumindo essa API — ainda não implementado neste repositório.

---

## 👤 Autor

**Leonardo Grillo**
