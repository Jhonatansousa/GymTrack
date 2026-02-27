# 🏋️‍♂️ GymTrack API

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.8-brightgreen?style=flat-square&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue?style=flat-square&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-blue?style=flat-square&logo=docker)
![JWT](https://img.shields.io/badge/JWT-black?style=flat-square&logo=json-web-tokens)

> RESTful API para gerenciamento completo de treinos, exercícios e séries, construída com foco em boas práticas de Engenharia de Software.

## 📖 Sobre o Projeto

O **GymTrack** é um sistema de gerenciamento de exercícios desenvolvido para facilitar o acompanhamento de treinos de musculação. Através desta API, os usuários podem se cadastrar de forma segura, organizar seus treinos em divisões (ex: Peito/Tríceps, Costas/Bíceps), cadastrar exercícios específicos e registrar as séries (carga e repetições) para cada exercício.

Este projeto backend foi construído para demonstrar conhecimentos em desenvolvimento de APIs robustas com o ecossistema Spring, utilizando padrões de projeto estruturais e boas práticas de segurança. Futuramente, a aplicação contará com um front-end desenvolvido em Angular.

## 🚀 Funcionalidades

- **Autenticação e Segurança:** - Cadastro e Login de usuários.
    - Segurança baseada em tokens JWT (Spring Security).
- **Gerenciamento de Treinos (Divisões):** - Criação, leitura, atualização e exclusão (CRUD) de divisões de treino.
    - Exclusão em cascata: deletar uma divisão remove automaticamente seus exercícios e séries.
- **Gerenciamento de Exercícios:** - Adição de exercícios a uma divisão específica.
    - Exclusão em cascata para as séries vinculadas.
- **Registro de Séries (Sets):** - Controle de carga e repetições por exercício.

## 🛠️ Tecnologias Utilizadas

**Core:**
- Java 21
- Spring Boot 3.5.8 (Web, Data JPA, Security, Validation)
- PostgreSQL (Banco de Dados Relacional)

**Segurança e Mapeamento:**
- Spring Security & JWT (Autenticação/Autorização)
- MapStruct (Mapeamento de Entidades para DTOs)
- Lombok (Redução de boilerplate)

**Infraestrutura e Testes:**
- Docker & Docker Compose (Containerização do banco de dados)
- JUnit 5 & Mockito (Testes Unitários)

## 🏗️ Arquitetura e Padrões Aplicados

Para garantir que o código seja escalável, testável e de fácil manutenção, os seguintes padrões foram aplicados:
- **Arquitetura em Camadas:** Separação clara entre `Controllers`, `Services` e `Repositories`.
- **Data Transfer Objects (DTOs):** Isolamento das entidades de banco de dados da camada de apresentação, garantindo segurança e controle sobre o que é exposto.
- **Facade Pattern:** Utilizado na camada de autenticação (`AuthFacade`) para simplificar a comunicação entre o registro do usuário e a geração do token.
- **Global Exception Handling:** Tratamento centralizado de erros com `@RestControllerAdvice`, garantindo respostas JSON padronizadas para o cliente (ex: `ResourceNotFoundException`, `DuplicatedContentException`).

## ⚙️ Como Executar Localmente

### Pré-requisitos
- [Java 21](https://jdk.java.net/21/)
- [Docker](https://www.docker.com/) e [Docker Compose](https://docs.docker.com/compose/)

### Passos

1. **Clone o repositório:**
   ```bash
   git clone [https://github.com/Jhonatansousa/gymtrack-backend.git](https://github.com/Jhonatansousa/gymtrack-backend.git)
   cd gymtrack-backend

    Suba o banco de dados via Docker:
    Isso iniciará um container PostgreSQL utilizando as credenciais definidas no docker-compose.yml.
    Bash

    docker-compose up -d

    Execute a aplicação utilizando o Maven Wrapper:
    Bash

    ./mvnw spring-boot:run

A API estará disponível em http://localhost:8080.
🛣️ Roadmap (Próximos Passos)

Este projeto está em evolução contínua. As próximas implementações incluem:

    [ ] Integração Front-end: Repositório separado utilizando Angular para consumo da API.

    [ ] Documentação da API: Implementação do Swagger (OpenAPI 3) para testar e documentar os endpoints.

    [ ] Qualidade de Código: Integração com SonarQube e JaCoCo para análise estática e cobertura de testes.

    [ ] Testes de Integração: Configuração do banco de dados em memória H2 para testes automatizados de ponta a ponta.

    [ ] Observabilidade: Implementação de logs estruturados utilizando SLF4J.

    [ ] CI/CD: Criação de pipelines com GitHub Actions para automação de testes e build.

    [ ] Deploy em Nuvem: Hospedagem da aplicação na AWS (EC2 para a aplicação e RDS para o banco de dados PostgreSQL).

👨‍💻 Autor

Jhonatan Estudante de Engenharia de Software e Desenvolvedor Backend Java.

(LinkedIn)[https://www.linkedin.com/in/jhonatansdasilva/]

(GitHub)[https://github.com/Jhonatansousa]
