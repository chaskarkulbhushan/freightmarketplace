# Freight Marketplace

Freight Marketplace is a backend foundation for a freight capacity marketplace. The current application provides health checks, database migration infrastructure, API documentation, request validation, and shared error handling. Freight domain workflows and application login are not implemented yet.

## Technology stack

- Java 21
- Spring Boot 4.1.1 with Spring MVC, Spring Data JPA, Actuator, Validation, and Spring Security
- PostgreSQL with the PostgreSQL JDBC driver
- Flyway for database schema migrations
- SpringDoc OpenAPI for Swagger UI and OpenAPI documentation
- Maven

## Architecture

The application is a Spring Boot service with packages grouped by responsibility under `com.logix.freightmarketplace`. Shared functionality is organized into areas such as `common.health`, `common.exception`, `common.response`, and `common.config`; security configuration is under `secconfig`. This is a modular, domain-oriented foundation, but freight domain modules and business behavior have not been added.

PostgreSQL is the relational database. Flyway owns schema changes and runs versioned SQL migrations from `src/main/resources/db/migration`. Hibernate is configured with `ddl-auto=validate`, so it validates the schema rather than creating or updating it. Spring Security is present and protects routes other than the explicitly public health and API-documentation routes. SpringDoc provides the OpenAPI document and Swagger UI.

Authentication features and freight business-domain functionality are not yet implemented; they are outside the current Sprint 1 foundation.

## Prerequisites

- JDK 21
- PostgreSQL available locally
- A terminal in the project root

The Maven Wrapper is included, so a separate Maven installation is not required.

## PostgreSQL setup

The current default configuration connects to:

| Setting | Default |
| --- | --- |
| Host and port | `localhost:5432` |
| Database name | `postgres` |
| Username | `postgres` |
| Schema | `freightmarketplace` |

Ensure PostgreSQL is running and that the configured user can connect to the `postgres` database and create the `freightmarketplace` schema. The V1 migration creates that schema. Set the database password through the `SPRING_DATASOURCE_PASSWORD` environment variable; do not commit local credentials.

To use a dedicated database or different credentials, set Spring Boot's standard datasource environment variables before starting the application. For example:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/freightmarketplace
SPRING_DATASOURCE_USERNAME=freightmarketplace
SPRING_DATASOURCE_PASSWORD=<your-local-password>
```

Create that database and role in PostgreSQL before using these overrides. The database name and schema name are separate settings; the configured schema remains `freightmarketplace`.

## Run the application

From the project root, start the application with the Maven Wrapper:

```bash
./mvnw spring-boot:run
```

On Windows Command Prompt or PowerShell, use:

```powershell
.\mvnw.cmd spring-boot:run
```

The server listens on `http://localhost:8080` by default. Flyway applies pending migrations during application startup. It records successful migrations in its schema history and does not reapply already-applied versions. Hibernate validates the resulting schema on startup.

## Run the test suite

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

## Available endpoints

| Method | URL | Purpose |
| --- | --- | --- |
| `GET` | `http://localhost:8080/actuator/health` | Spring Boot Actuator application health |
| `GET` | `http://localhost:8080/api/v1/health` | Versioned application API health response |
| `GET` | `http://localhost:8080/swagger-ui/index.html` | Interactive Swagger UI |
| `GET` | `http://localhost:8080/v3/api-docs` | OpenAPI document in JSON |

Both health endpoints are public, but serve different purposes. `/actuator/health` is Spring Boot's operational health endpoint and reports the application's health status. `/api/v1/health` is an application-owned, versioned API endpoint that currently returns `{"status":"UP"}`.

Swagger UI and the OpenAPI JSON endpoint are also publicly accessible. Other requests require authentication; no application login or JWT authentication flow is currently implemented.
