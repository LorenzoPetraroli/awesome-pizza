# Awesome Pizza Backend

Awesome Pizza is a Quarkus backend that manages customer pizza orders without registration.
Customers create an order and receive a public tracking code. The kitchen can inspect the FIFO
queue, start one order at a time, mark it as ready, and complete it when the workflow ends.

## Tech stack

- Java 21
- Quarkus
- PostgreSQL
- Flyway
- Hibernate ORM with Panache
- MapStruct
- JUnit 5

## Backend features

- Create a new customer order
- Track an order by public code
- Retrieve the available pizza menu with names, ingredients, and prices
- Return order pricing details, including line totals and total price
- Retrieve a kitchen summary for frontend polling
- Start a selected queued order
- Mark an order as ready
- Complete a ready order
- Enforce the rule that only one order can be in preparation

## Run locally

Start PostgreSQL locally, then run Quarkus in dev mode:

```powershell
docker compose -f compose.postgres.yaml up -d
./mvnw.cmd quarkus:dev
```

The API runs on `http://localhost:8081`.
Swagger UI is available at `http://localhost:8081/q/swagger-ui`.

The local development setup uses the default PostgreSQL settings defined in `application.properties`.
You only need environment variables if you want to override those defaults.

The kitchen workflow is fully manual, as required by the project brief:

- the pizza chef sees the queued orders
- the pizza chef chooses which queued order to start
- the pizza chef manually marks the order as ready
- the pizza chef manually completes the order

## Run tests

```powershell
./mvnw.cmd test
```

## Demo with Docker Compose

Build and start the full demo stack:

```powershell
docker compose up --build
```

The compose stack starts:

- PostgreSQL on `localhost:5432`
- Awesome Pizza API on `http://localhost:8081`

Flyway migrations run automatically when the backend starts.

The backend image is built from [src/main/docker/Dockerfile.jvm](C:\apps\personal\awesome-pizza\awesome-pizza\src\main\docker\Dockerfile.jvm).

## Useful endpoints

- `POST /api/orders`
- `GET /api/orders/{code}`
- `GET /api/pizzas`
- `GET /api/kitchen/summary`
- `POST /api/kitchen/orders/{code}/start`
- `PATCH /api/kitchen/orders/{code}/ready`
- `PATCH /api/kitchen/orders/{code}/complete`

The `GET /api/pizzas` endpoint returns the menu metadata required by the frontend:

- pizza type
- display name
- ingredients description
- unit price

Order responses include:

- ordered items
- unit price per item
- line total per item
- total order price

## Architecture flow

1. The client calls the REST API.
2. The API layer validates input, maps DTOs, and delegates the request.
3. The application layer executes the use case and orchestrates the workflow.
4. The domain layer contains the business core: models, enums, factories, validators, and repository contracts.
5. The infrastructure layer implements persistence and other technical concerns.

Dependency direction:

- `api -> application -> domain`
- `infrastructure -> domain`
