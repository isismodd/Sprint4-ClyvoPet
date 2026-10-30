# 🐾 ClyvoPet

Sistema web para gerenciamento de clínica veterinária desenvolvido em **Java com Spring Boot**, com foco no controle de animais, veterinários, consultas e lembretes.

O projeto possui autenticação e autorização por perfil de usuário, persistência em banco de dados Oracle, versionamento do banco com Flyway, validação de dados, envio de notificações por e-mail e deploy em nuvem utilizando Microsoft Azure.

---

## Aplicação publicada

O ClyvoPet está publicado no **Microsoft Azure App Service** e pode ser acessado diretamente pelo navegador:

### 🔗 [Acessar ClyvoPet](https//:clyvopet-rm561497.azurewebsites.net)

> 🔐 As funcionalidades internas do sistema são protegidas por autenticação e exigem login.

---

## Sobre o projeto

O **ClyvoPet** foi desenvolvido para auxiliar no gerenciamento das principais atividades de uma clínica veterinária.

A aplicação centraliza informações relacionadas aos animais atendidos, profissionais veterinários, consultas realizadas e lembretes associados aos atendimentos.

Além das operações de cadastro, consulta, edição e exclusão, o sistema implementa regras de negócio, controle de acesso baseado em perfis, validação dos dados recebidos e envio de notificações por e-mail.

A aplicação possui interface web responsiva desenvolvida com **Thymeleaf e Bootstrap**, integrada ao backend Spring Boot e ao banco de dados Oracle.

---

## Principais funcionalidades

### Gerenciamento de animais

O sistema permite realizar o gerenciamento dos animais cadastrados na clínica, incluindo:

- Cadastro de novos animais;
- Visualização dos animais cadastrados;
- Atualização das informações;
- Exclusão de registros;
- Armazenamento dos dados do tutor;
- Visualização de detalhes como espécie, raça, idade, peso, sexo e observações.

---

### Gerenciamento de veterinários

Administradores possuem acesso ao gerenciamento dos profissionais veterinários.

Entre as operações disponíveis estão:

- Cadastro de veterinários;
- Consulta dos profissionais cadastrados;
- Atualização de informações;
- Exclusão de veterinários;
- Controle de CRMV;
- Especialidade;
- Telefone e e-mail;
- Controle de perfil de acesso.

A administração dos veterinários é uma funcionalidade exclusiva para usuários com perfil **ADMIN**.

---

### Gerenciamento de consultas

O ClyvoPet permite organizar os atendimentos realizados pela clínica.

As consultas relacionam:

- Animal;
- Veterinário responsável;
- Data e horário;
- Motivo da consulta;
- Observações;
- Status do atendimento.

O sistema também possui regras para preservar a consistência das consultas caso um veterinário seja excluído.

Quando necessário, as consultas associadas são canceladas e permanecem registradas no sistema, preservando o histórico do atendimento.

---

### Lembretes

A aplicação possui gerenciamento de lembretes relacionados aos atendimentos veterinários.

Os lembretes auxiliam no acompanhamento das consultas e permitem que informações importantes sejam enviadas aos responsáveis.

---

### Envio de e-mails

O ClyvoPet possui integração com serviço de e-mail utilizando **Spring Mail**.

A funcionalidade permite o envio de notificações relacionadas aos lembretes diretamente pela aplicação.

O serviço utiliza comunicação SMTP autenticada e suas credenciais são armazenadas de forma segura por meio de variáveis de ambiente no ambiente de produção.

---

## Autenticação e autorização

A segurança da aplicação é implementada utilizando **Spring Security**.

O sistema possui autenticação de usuários e controle de acesso baseado em funções (*roles*).

### 👑 ADMIN

O administrador possui acesso às funcionalidades de:

- Gerenciamento de veterinários;
- Gerenciamento de animais;
- Gerenciamento de consultas;
- Gerenciamento de lembretes;
- Funcionalidades administrativas.

### 👩‍⚕️ VETERINARIO

O usuário veterinário possui acesso às funcionalidades necessárias para operação da clínica, como:

- Animais;
- Consultas;
- Lembretes.

O gerenciamento de outros veterinários permanece restrito ao perfil **ADMIN**.

As rotas protegidas são controladas pelo Spring Security tanto na camada web quanto nos endpoints da aplicação.

---

## Validação de dados

O projeto utiliza **Jakarta Bean Validation** para garantir a integridade dos dados recebidos.

Entre as validações implementadas estão:

- Campos obrigatórios;
- Validação de e-mail;
- Limites de tamanho;
- Valores numéricos positivos ou iguais a zero;
- Validação de dados dos animais;
- Validação de dados dos veterinários.

As mensagens de validação também são apresentadas diretamente nos formulários da interface utilizando integração entre **Thymeleaf, Spring Validation e Bootstrap**.

---

## Banco de dados

O ClyvoPet utiliza **Oracle Database** para persistência das informações.

Entre as principais tabelas utilizadas estão:

| Tabela | Responsabilidade |
|---|---|
| `VETERINARIOS` | Armazena os profissionais e usuários do sistema |
| `ANIMAIS` | Armazena os animais e informações dos tutores |
| `CONSULTAS` | Armazena os atendimentos veterinários |
| `LEMBRETES` | Armazena os lembretes relacionados aos atendimentos |

O acesso aos dados é realizado utilizando **Spring JDBC / JdbcTemplate**, com separação entre as camadas de Repository, Service e Controller.

---

## Versionamento do banco com Flyway

O banco de dados é versionado utilizando **Flyway**.

As migrations são executadas automaticamente durante a inicialização da aplicação, permitindo que alterações na estrutura do banco sejam controladas e reproduzidas de maneira organizada.

Exemplos de responsabilidades das migrations utilizadas no projeto:

```
db/migration/
├── V1__create_table_initial.sql
├── V2__...
└── V3__allow_null_veterinario_consulta.sql
```

As migrations são responsáveis por tarefas como:

- Criação inicial das tabelas;
- Configuração da estrutura do banco;
- Inclusão de dados necessários para inicialização;
- Evolução das regras e relacionamentos entre as entidades.

---

## Arquitetura

O projeto utiliza uma arquitetura em camadas para separar as responsabilidades da aplicação.

```
Controller
    ↓
Service
    ↓
Repository
    ↓
JdbcTemplate
    ↓
Oracle Database
```

---

## 🛠️ Tecnologias utilizadas

### Backend

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

- Java 21;
- Spring Boot 3.4.1;
- Spring MVC;
- Spring Security;
- Spring JDBC;
- JdbcTemplate;
- Spring HATEOAS;
- Jakarta Validation;
- Spring Mail;
- Maven.

### Frontend

![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)

- HTML5;
- CSS3;
- Thymeleaf;
- Bootstrap;
- JavaScript.

### Banco de dados

![Oracle](https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)

- Oracle Database;
- Oracle JDBC Driver;
- Flyway;
- HikariCP.

### Cloud e DevOps

![Azure](https://img.shields.io/badge/Microsoft_Azure-0078D4?style=for-the-badge)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)

- Microsoft Azure App Service;
- GitHub;
- GitHub Actions;
- CI/CD;
- Application Insights;
- Variáveis de ambiente para gerenciamento de configurações sensíveis.

---

## ☁️ Deploy

A aplicação está hospedada no **Microsoft Azure App Service**, utilizando ambiente Linux com **Java 21**.

O processo de build e deploy é integrado ao **GitHub Actions**, permitindo a publicação da aplicação no Azure a partir do repositório do projeto.

```
GitHub
   │
   ▼
GitHub Actions
   │
   ├── Build Maven
   │
   ▼
Aplicação Spring Boot (.jar)
   │
   ▼
Microsoft Azure App Service
   │
   ├── Java 21
   ├── Application Insights
   └── Variáveis de ambiente
   │
   ▼
ClyvoPet
```

### 🌎 Ambiente de produção

**Plataforma:** Microsoft Azure  
**Serviço:** Azure App Service  
**Runtime:** Java 21  
**CI/CD:** GitHub Actions  
**Monitoramento:** Azure Application Insights  

🔗 **Aplicação:** [clyvo-pet-rm561497.azurewebsites.net](https//:clyvopet-rm561497.azurewebsites.net)

---

## 📁 Estrutura do projeto

A organização principal do projeto segue a estrutura:

```
src/
├── main/
│   ├── java/
│   │   └── br/com/fiap/ClyvoPet/
│   │       ├── config/
│   │       ├── controller/
│   │       │   ├── web/
│   │       │   └── api/
│   │       ├── model/
│   │       ├── repository/
│   │       ├── service/
│   │       └── ClyvoPetApplication.java
│   │
│   └── resources/
│       ├── db/
│       │   └── migration/
│       ├── static/
│       │   ├── css/
│       │   └── images/
│       ├── templates/
│       └── application.properties
│
└── test/
```

Essa separação facilita a manutenção do projeto e mantém as responsabilidades de cada camada organizadas.

---

## Desenvolvimento

Projeto desenvolvido como parte das atividades acadêmicas da **FIAP**, aplicando conceitos de desenvolvimento Java, Spring Boot, banco de dados, segurança e computação em nuvem.

---

<p align="center">
  🐾 <strong>ClyvoPet</strong><br>
  Cuidado, organização e tecnologia para a gestão veterinária.
</p>
