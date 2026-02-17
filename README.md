# 🗂️ Fórum Hub API — Desafio Alura

API REST para gerenciamento de tópicos de fórum, construída com **Spring Boot 3**, **Spring Security** e **JWT**.

---

## 📋 Tecnologias
- Java 17
- Spring Boot 3.2.5
- Spring Security + JWT (Auth0)
- Spring Data JPA + Hibernate
- Flyway Migration
- MySQL 8+
- Lombok
- Maven

---

## ⚙️ Configuração Inicial

### 1. Banco de Dados
Crie um banco MySQL (o Flyway cria as tabelas automaticamente):
```sql
CREATE DATABASE forumhub;
```

### 2. application.properties
Edite `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/forumhub?createDatabaseIfNotExist=true
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

### 3. Variável de Ambiente JWT (opcional)
```bash
# Linux/Mac
export JWT_SECRET=sua-chave-secreta-super-segura

# Windows PowerShell
$env:JWT_SECRET="sua-chave-secreta-super-segura"
```

### 4. Executar
```bash
mvn spring-boot:run
```
A API sobe em `http://localhost:8080`

---

## 🗄️ Migrations Flyway (execução automática)

| Arquivo | Descrição |
|---------|-----------|
| `V1__create-table-usuarios-e-cursos.sql` | Cria tabelas `usuarios` e `cursos` |
| `V2__create-table-topicos.sql` | Cria tabela `topicos` com FK e unique |
| `V3__insert-dados-iniciais.sql` | Insere admin e cursos de exemplo |

**Usuário padrão criado pelo seed:**
- Email: `admin@forumhub.com`
- Senha: `Admin@1234`

---

## 🔐 Autenticação

### POST /auth/login
```json
{
  "email": "admin@forumhub.com",
  "senha": "Admin@1234"
}
```
**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Use o token em todas as requisições:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## 📌 Endpoints

### Tópicos

| Método | URI | Descrição | Auth |
|--------|-----|-----------|------|
| POST | `/topicos` | Criar tópico | ✅ |
| GET | `/topicos` | Listar tópicos (paginado) | ✅ |
| GET | `/topicos/{id}` | Detalhar tópico | ✅ |
| PUT | `/topicos/{id}` | Atualizar tópico | ✅ |
| DELETE | `/topicos/{id}` | Excluir tópico | ✅ |

---

### POST /topicos — Criar Tópico
```json
{
  "titulo": "Dúvida sobre Spring Security",
  "mensagem": "Como configurar o filtro JWT?",
  "autorId": 1,
  "cursoId": 1
}
```

### GET /topicos — Listar (com filtros opcionais)
```
GET /topicos
GET /topicos?curso=Spring&ano=2025
GET /topicos?page=0&size=5
```

### PUT /topicos/{id} — Atualizar
```json
{
  "titulo": "Novo título",
  "mensagem": "Nova mensagem",
  "status": "SOLUCIONADO"
}
```
> Status possíveis: `ABERTO`, `FECHADO`, `SOLUCIONADO`

### DELETE /topicos/{id}
Retorna `204 No Content` em caso de sucesso.

---

## 🚀 Testando com Insomnia / Postman

1. Faça `POST /auth/login` e copie o token
2. Adicione header `Authorization: Bearer <token>` em todas as requisições
3. Teste o CRUD de tópicos

---

## 🏗️ Estrutura do Projeto

```
src/main/java/com/forumhub/api/
├── ForumHubApplication.java
├── config/
│   └── SecurityConfigurations.java
├── controller/
│   ├── AutenticacaoController.java
│   └── TopicosController.java
├── dto/
│   ├── DadosAutenticacao.java
│   ├── DadosTokenJWT.java
│   ├── DadosCadastroTopico.java
│   ├── DadosAtualizacaoTopico.java
│   ├── DadosListagemTopico.java
│   └── DadosDetalhamentoTopico.java
├── entity/
│   ├── Curso.java
│   ├── Topico.java
│   ├── Usuario.java
│   └── StatusTopico.java
├── infra/
│   └── TratadorDeErros.java
├── repository/
│   ├── CursoRepository.java
│   ├── TopicoRepository.java
│   └── UsuarioRepository.java
├── security/
│   └── SecurityFilter.java
└── service/
    ├── AutenticacaoService.java
    └── TokenService.java
src/main/resources/
├── application.properties
└── db/migration/
    ├── V1__create-table-usuarios-e-cursos.sql
    ├── V2__create-table-topicos.sql
    └── V3__insert-dados-iniciais.sql
```

---

## ⚠️ Regras de Negócio Implementadas

- ✅ Todos os campos obrigatórios validados com `@Valid`
- ✅ Tópicos duplicados (mesmo título + mensagem) rejeitados com HTTP 422
- ✅ ID inválido retorna HTTP 404
- ✅ Apenas usuários autenticados (token JWT) podem usar a API
- ✅ Token expira em 24 horas (configurável)
- ✅ Listagem paginada, ordenada por data ASC
- ✅ Filtros por nome do curso e ano
