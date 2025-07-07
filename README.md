
# Candidate Management Microservices Suite

**Author:** Cristian Proaño  
**Role:** Senior Java Developer (Entry Test for Seek)

## Overview

This is a multi-module Spring Boot project designed to manage candidate (user and customer) data. It consists of three modules:

- **common**: Shared configuration, utilities, and database migrations (Flyway).
- **auth-service**: Authentication and authorization microservice.
- **customer-service**: Customer management microservice.

All modules are built with:

- **Java** 17
- **Spring Boot** 3.2.3
- **Maven Wrapper** (`mvnw.cmd` / `mvnw`)
- **MySQL 8.0** (production database)
- **H2** (in-memory for unit testing)

## Repository Structure

```
candidate-management/
.
├── .env                                            # Environment variables for Docker Compose
├── auth-service                                    # Microservice: authentication and authorization
│   ├── Dockerfile                                  # Docker image definition for auth-service
│   ├── entrypoint-auth.sh                          # Entrypoint script for the container
│   ├── pom.xml                                     # POM specific to auth-service
│   └── src
│       ├── main
│       │   └── java
│       │       └── com.example.auth_service       # Base package for auth-service
│       │           ├── controller                 # REST endpoints (login, register)
│       │           ├── dto                        # Data Transfer Objects
│       │           ├── exception                  # Custom exceptions
│       │           ├── model                      # Entities (User)
│       │           ├── repository                 # JPA repositories
│       │           ├── security                   # Filters, JWT, security config
│       │           └── service                    # Authentication business logic
│       └── test
│           └── com.example.auth_service           # Unit tests
├── build_common.bat                               # Script to build the common module (Windows)
├── common                                         # Shared module (config, utilities, Flyway migrations)
│   ├── Dockerfile                                 # Docker image to run Flyway migrations
│   ├── entrypoint-migrations.sh                   # Entrypoint script to run Flyway in container
│   ├── pom.xml                                    # POM for common module
│   └── src
│       ├── main
│       │   └── java
│       │       └── com.example.common             # Base package for common module
│       │           ├── config                     # Global configuration (CORS, Swagger, etc.)
│       │           ├── exception                  # Shared exceptions
│       │           └── util                       # Common utilities
│       └── test
│           └── com.example.common                 # Unit tests
├── container-def-customer.json                    # Container definition
├── customer-service                               # Microservice: customer management
│   ├── Dockerfile                                 # Docker image definition for customer-service
│   ├── entrypoint-customer.sh                     # Entrypoint script for customer-service
│   ├── pom.xml                                    # POM for customer-service
│   └── src
│       ├── main
│       │   └── java
│       │       └── com.example.customer_service   # Base package for customer-service
│       │           ├── controller                 # REST endpoints for customers
│       │           ├── dto                        # DTOs and request objects
│       │           ├── factory                    # Entity to DTO conversion
│       │           ├── model                      # JPA entities (Customer)
│       │           ├── repository                 # Data access interfaces
│       │           └── service                    # Business logic for customers
│       └── test
│           └── com.example.customer_service       # Unit tests
├── docker-compose.yml                             # Docker orchestration: auth, customer, and MySQL
├── mvnw                                           # Maven wrapper for Unix/macOS
├── mvnw.cmd                                       # Maven wrapper for Windows
├── pom.xml                                        # Parent POM managing modules and dependencies
├── readme.md                                      # Project documentation
├── run_all.bat                                    # Script to run all services locally
├── run_auth_service.bat                           # Script to run auth-service locally
├── run_common.bat                                 # Script to run common module
├── run_customer_service.bat                       # Script to run customer-service locally
├── setup-dockers.bat                              # Script to build and deploy containers
├── taskdef.json                                   # Task definition (e.g., for AWS ECS)
└── test-login.bat                                 # Login test script (likely using curl or Postman) 
```

## Prerequisites [Manual Execution]

- Java 17
- Maven 3.8+
- Local MySQL instance

```sql
CREATE DATABASE bdd_users;
```

## Flyway Migrations

All Flyway SQL scripts live in `common/src/main/resources/db/migration`.

To run migrations manually:
```bash
cd common
../mvnw flyway:migrate \
  -Dflyway.url="jdbc:mysql://localhost:3306/bdd_users?useSSL=false&serverTimezone=UTC" \
  -Dflyway.user=root \
  -Dflyway.password=root
```

## Docker-Based Deployment

The entire suite can be built and deployed with Docker Compose.

### ✅ How It Works

1. **Dockerfile** builds a multi-layer Spring Boot image for each microservice.
2. **docker-compose.yml**:
   - Launches a MySQL 8.0 database container
   - Deploys:
     - `auth-service` on port **8090**
     - `customer-service` on port **8091**
   - Injects necessary environment variables via `.env` or directly in the file
   - Automatically applies Flyway migrations via the `common` module
   - Healthchecks ensure services are available before proceeding

### ▶️ Start All Services

```bash
docker compose up --build -d
```

### 🛑 Stop All Services

```bash
docker compose down
```

### 🔁 Restart with Clean Volumes

```bash
docker compose down -v
docker compose up --build -d
```

### 🔍 Check Logs

```bash
docker compose logs -f auth-service
```

## Environment Variables

These are passed to the containers via `docker-compose.yml` or an external `.env` file:

```env
DB_HOST=bdd-users..
DB_PORT=3306
DB_NAME=bdd_users
DB_USER=root
DB_PASS=P#adad7#

JWT_SECRET=mySuperSecretHere..
JWT_EXPIRATION_MS=3600000
```

## Build & Run Locally [Without Docker]

```bash
./mvnw clean install -DskipTests -pl common,auth-service,customer-service -am
```

Then, in separate terminals:

```bash
./mvnw -pl common spring-boot:run
./mvnw -pl auth-service spring-boot:run
./mvnw -pl customer-service spring-boot:run
```

## Testing

- Run all tests:
  ```bash
  ./mvnw test
  ```
- Run tests in a specific module:
  ```bash
  ./mvnw -pl auth-service test
  ```
- Run specific test class:
  ```bash
  ./mvnw test -Dtest=CustomerFactoryTest
  ```

## OpenAPI / Swagger

Swagger UI is enabled on both services:

- **Auth Service**: `http://localhost:8090/swagger-ui.html`
- **Customer Service**: `http://localhost:8091/swagger-ui.html`

Swagger config:  
`common/src/main/java/com/example/common/config/OpenApiConfig.java`

## REST Endpoints

### Auth Service

| Method | Endpoint         | Description            |
|--------|------------------|------------------------|
| POST   | `/auth/login`    | User login             |
| POST   | `/auth/register` | Register new user      |

### Customer Service

| Method | Endpoint              | Description                  |
|--------|-----------------------|------------------------------|
| GET    | `/customers`          | List all customers           |
| POST   | `/customers`          | Create a customer            |
| GET    | `/customers/metrics`  | Age-based metrics calculation|

## Additional Notes

- **Exception Handling:** Custom handlers return consistent error formats
- **DTO Conversion:** Via `CustomerFactory` utility
- **Security:** JWT-based stateless auth
- **Testing:** Mockito + H2 used for isolated tests
- **Flyway:** Integrated in `common`, run on startup
- **Docker Healthchecks:** Help ECS/LB detect healthy containers

---

_This README is part of Cristian Proaño’s Java Spring Boot coding exercise for Seek._
