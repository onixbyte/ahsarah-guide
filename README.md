# Delta Force Guide Server

REST API backend for managing **Delta Force** game firearm builds and modifications. Provides endpoints to browse, create, and share weapon configurations including attachments, tuning setups, tags, and build reviews.


## Tech Stack

| Layer     | Technology                                                 |
|-----------|------------------------------------------------------------|
| Language  | Java 21 (Amazon Corretto)                                  |
| Framework | Spring Boot 3.x (Web, Security, Data JPA, Cache, Actuator) |
| Build     | Gradle (Kotlin DSL)                                        |
| Database  | PostgreSQL (Flyway migrations, JSONB columns)              |
| Cache     | Redis (2-hour TTL)                                         |
| Auth      | Custom JWT via httpOnly cookies + BCrypt                   |
| API Docs  | springdoc-openapi (Swagger UI, dev profile only)           |
| Container | Multi-stage Docker image (Amazon Corretto 21 → Alpine)     |
| CI/CD     | GitHub Actions — builds on release publish                 |


## Quick Start

### Prerequisites

- JDK 21 (Amazon Corretto recommended)
- PostgreSQL
- Redis

### Configure

Copy and customise the development config:

```bash
cp config/application-prod.yaml.example \
   config/application-dev.yaml
```

Fill in your datasource and Redis connection details, then place the production config at `config/`, then enable it by environment variable `SPRING_PROFILES_ACTIVE`.

### Build

```bash
# Compile (skip tests)
./gradlew build -x test

# Run all tests
./gradlew test

# Build executable JAR
./gradlew bootJar
```

### Run

```bash
SPRING_PROFILES_ACTIVE=prod java -jar build/libs/delta-force-guide-server-$version.jar
```

Swagger UI is available at `http://localhost:8080/swagger-ui.html` when the `dev` profile is active.

## Data Model

- **Firearm** — weapon base stats (name, type, level, calibre, fire rate, armour/body damage, review)
- **Modification** — a build attached to a firearm, with name, code, tags (JSONB), accessories including nested tuning objects (JSONB), author notes, and video links
- **App User** — registered user (username, email) with hashed credentials via BCrypt

Tags and accessories are stored as PostgreSQL JSONB columns using [Hypersistence Utils](https://github.com/vladmihalcea/hypersistence-utils), enabling flexible per-build metadata and filtering with the `@>` operator.


## Architecture

```
Controller  →  Service  →  Manager  →  Repository / Mapper
   (HTTP)      (logic)     (@Transactional)  (data access)
```

The call chain is strictly enforced — skipping layers is not permitted. All request/response objects are Java records with static `from()` factory methods for entity-to-DTO conversion.


## Docker

```bash
# Build image
docker pull registry.onixbyte.cn/onixbyte/delta-force-guide-server:latest

# Run container
docker run -p 8080:8080 \
  -v /path/to/config:/app/config \
  -e SPRING_PROFILES_ACTIVE=$your_active_profiles
  delta-force-guide-server
```

Pre-built images are published to **Self-hosted GitLab Container Registry** (`registry.onixbyte.cn/onixbyte/delta-force-guide-server`) on every release.


## CI/CD

GitLab CI triggers on **tags**. The pipeline:

1. Builds the boot JAR with the release tag as the version
2. Uploads the JAR as a release asset
3. Builds and pushes a multi-arch Docker image to GHCR tagged with both `:latest` and `:<version>`

No tests are run in CI by design — tests are expected to pass locally before a release is cut.


## Profiles

| Profile | Purpose                                                               |
|---------|-----------------------------------------------------------------------|
| `dev`   | Enables Swagger UI, connects to dev DB/Redis at `dfguide.onixbyte.cn` |
| default | Production mode, no Swagger, uses production datasource               |


## Project Structure

```
src/main/java/com/onixbyte/deltaforceguide/
├── client/          External service HTTP clients
├── config/          Spring bean definitions (Security, CORS, Cache, Jackson, MyBatis)
├── controller/      REST controllers
├── domain/
│   ├── converter/   JPA attribute converters
│   ├── dto/         Request/response records
│   └── entity/      JPA entities
├── enumeration/     Enums (FirearmType)
├── exception/       Custom BizException with HTTP status mapping
├── filter/          TokenAuthenticationFilter (JWT through OncePerRequestFilter)
├── manager/         @Transactional wrappers around repositories
├── mapper/          MyBatis mapper interfaces (reserved for future use)
├── properties/      @ConfigurationProperties records
├── repository/      Spring Data JPA repositories
├── security/
│   ├── authentication/  Custom UsernamePasswordAuthentication
│   └── provider/        UsernamePasswordAuthenticationProvider
├── service/         Business logic
├── shared/          Constants and utility classes
└── utils/           General-purpose helpers
```


## Versioning

The application exposes its version at `/versions` (GET).


## Licence

This project is licensed under the [MIT Licence](LICENCE). Copyright &copy; 2026 OnixByte.

