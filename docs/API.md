# Documentação da API

API REST para cadastro de usuários e gerenciamento de tarefas.

## Acesso

Por padrão, a aplicação fica disponível em `http://localhost:8080`.

As rotas de tarefas exigem um token JWT no cabeçalho:

```http
Authorization: Bearer <token>
```

O token é obtido em `POST /auth/login`. Cada usuário só consegue consultar e
alterar as próprias tarefas.

## Rotas de autenticação

### `POST /auth/register`

Cria um usuário. Essa rota é pública e não exige token.

**Corpo:**

```json
{
  "email": "usuario@exemplo.com",
  "password": "senha123"
}
```

Regras:

- `email` é obrigatório e deve ter formato válido;
- `password` é obrigatório;
- o e-mail não pode estar cadastrado.

**Respostas:**

- `201 Created`: usuário criado; não há conteúdo no corpo;
- `400 Bad Request`: dados inválidos;
- `409 Conflict`: o e-mail já existe.

### `POST /auth/login`

Autentica um usuário e retorna um token JWT. Essa rota é pública.

**Corpo:**

```json
{
  "email": "usuario@exemplo.com",
  "password": "senha123"
}
```

**Resposta `200 OK`:**

```json
{
  "token": "<jwt>"
}
```

Também pode retornar `400 Bad Request` quando os dados são inválidos ou
`401 Unauthorized` quando as credenciais não são válidas.

## Rotas de tarefas

Todas as rotas desta seção exigem autenticação.

### `POST /tasks`

Cria uma tarefa para o usuário autenticado. A tarefa é criada como pendente.

**Corpo:**

```json
{
  "title": "Estudar Spring Boot",
  "description": "Revisar controllers e serviços"
}
```

Regras:

- `title` é obrigatório e aceita até 100 caracteres;
- `description` é opcional e aceita até 500 caracteres.

**Resposta `201 Created`:**

```json
{
  "id": 1,
  "title": "Estudar Spring Boot",
  "description": "Revisar controllers e serviços",
  "completed": false,
  "createdAt": "2026-10-22T10:00:00",
  "updatedAt": "2026-10-22T10:00:00"
}
```

Pode retornar `400 Bad Request` para dados inválidos ou `401 Unauthorized`
quando o token não for informado ou não for válido.

### `GET /tasks`

Lista as tarefas do usuário autenticado com filtros, paginação e ordenação.

**Filtros opcionais:**

| Parâmetro | Tipo | Descrição |
| --- | --- | --- |
| `completed` | boolean | Filtra por status (`true` ou `false`). |
| `title` | string | Procura o texto informado em qualquer parte do título, sem diferenciar maiúsculas e minúsculas. |
| `page` | inteiro | Número da página, começando em `0`. |
| `size` | inteiro | Quantidade de itens por página. O padrão é `3`. |
| `sort` | string | Campo e direção de ordenação, por exemplo `createdAt,desc`. O padrão é `createdAt,desc`. |

**Exemplo:**

```http
GET /tasks?completed=false&title=spring&page=0&size=10&sort=createdAt,desc
```

**Resposta `200 OK`:**

```json
{
  "content": [
    {
      "id": 1,
      "title": "Estudar Spring Boot",
      "description": "Revisar controllers e serviços",
      "completed": false,
      "createdAt": "2026-10-22T10:00:00",
      "updatedAt": "2026-10-22T10:00:00"
    }
  ],
  "page": 0,
  "size": 10,
  "totalPages": 1,
  "totalElements": 1,
  "first": true,
  "last": true
}
```

Retorna `401 Unauthorized` sem autenticação.

### `GET /tasks/{taskId}`

Busca uma tarefa pelo ID. A tarefa precisa pertencer ao usuário autenticado.

**Exemplo:**

```http
GET /tasks/1
```

**Resposta `200 OK`:** retorna uma tarefa no mesmo formato usado em
`POST /tasks`.

Retorna `401 Unauthorized` sem autenticação ou `404 Not Found` quando a tarefa
não existe ou pertence a outro usuário.

### `PUT /tasks/{taskId}`

Atualiza o título e a descrição de uma tarefa existente. O status de conclusão
não é alterado por esta rota.

**Corpo:**

```json
{
  "title": "Estudar Spring Boot atualizado",
  "description": "Revisar Spring Security e JPA"
}
```

**Resposta `200 OK`:** retorna a tarefa atualizada.

Retorna `400 Bad Request` para dados inválidos, `401 Unauthorized` sem token ou
`404 Not Found` quando a tarefa não for encontrada para o usuário.

### `DELETE /tasks/{taskId}`

Exclui uma tarefa do usuário autenticado.

**Resposta `204 No Content`:** exclusão realizada sem conteúdo no corpo.

Retorna `401 Unauthorized` sem autenticação ou `404 Not Found` quando a tarefa
não existe ou pertence a outro usuário.

### `PATCH /tasks/{taskId}/completed`

Marca uma tarefa pendente como concluída.

**Resposta `200 OK`:** retorna a tarefa com `completed: true`.

Retorna `401 Unauthorized` sem token, `404 Not Found` para uma tarefa
inexistente ou de outro usuário e `409 Conflict` se ela já estiver concluída.

### `PATCH /tasks/{taskId}/incomplete`

Reabre uma tarefa concluída.

**Resposta `200 OK`:** retorna a tarefa com `completed: false`.

Retorna `401 Unauthorized` sem token, `404 Not Found` para uma tarefa
inexistente ou de outro usuário e `409 Conflict` se ela já estiver pendente.

## Formato dos erros

Erros gerados pela aplicação usam uma destas estruturas:

```json
{
  "message": "Tarefa não encontrada"
}
```

Para falhas de validação:

```json
{
  "errors": {
    "title": "O título é obrigatório."
  }
}
```

## Swagger / OpenAPI

Os endpoints públicos de documentação são:

- Interface: `GET /swagger-ui.html`
- Especificação OpenAPI: `GET /v3/api-docs`

Na interface do Swagger, use **Authorize** e informe `Bearer <token>` para
testar as rotas protegidas.

