# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Coding Standards

- **Style**: Follow the Google Java Coding Style as the foundation.
- **Indentation**: Use 4 spaces — no tabs.
- **Line length**: Maximum 100 characters per line.
- **Comments**: All code comments must use British English spelling (e.g. "colour" not "color", "behaviour" not "behavior", "serialise" not "serialize", "analyse" not "analyze", "traveller" not "traveler").

## Build & Test Commands

```bash
# Build the project (skip tests)
./gradlew build -x test

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.onixbyte.deltaforceguide.service.PasswordEncoderTest"

# Run a specific test method
./gradlew test --tests "com.onixbyte.deltaforceguide.service.PasswordEncoderTest.generatePassword"

# Build the full JAR
./gradlew bootJar
```

The project uses Gradle with Java 21 (Amazon Corretto). Tests use JUnit 5 with the Spring Boot test framework, H2 in-memory database for test runtime, and Spring Security test support. Tests require an active `dev` profile.

## Code Architecture

**Delta Force Guide Server** — A REST API backend for managing Delta Force game firearm builds/modifications.

### Package structure

```
com.onixbyte.deltaforceguide
├── client/           # External service clients (TokenClient for JWT)
├── config/           # Spring beans: Security, CORS, Cache/Redis, Jackson, MyBatis, Spring Data
├── controller/       # REST controllers (Firearm, Modification, Tag, Auth)
├── domain/
│   ├── converter/    # JPA attribute converters (FirearmTypeConverter)
│   ├── dto/          # Request/response records (FirearmRequest, ModificationResponse, etc.)
│   └── entity/       # JPA entities (Firearm, Modification, User, Accessory, Tuning)
├── enumeration/      # Enums (FirearmType)
├── exeption/         # BizException (custom runtime exception with HTTP status)
├── filter/           # TokenAuthenticationFilter (JWT auth via OncePerRequestFilter)
├── manager/          # Thin @Transactional wrappers around repositories
├── mapper/           # MyBatis mappers (configured but currently unused)
├── properties/       # @ConfigurationProperties records (Cors, Token, Cookie)
├── repository/       # Spring Data JPA repositories
├── security/
│   ├── authentication/  # Custom UsernamePasswordAuthentication impl
│   └── provider/        # UsernamePasswordAuthenticationProvider
├── service/          # Business logic layer (FirearmService, ModificationService, AuthService, etc.)
├── shared/           # Constants and utility classes (CookieName, CredentialProvider, JacksonModules)
└── utils/            # Helpers (DateTimeUtil)
```

### Key design decisions

- **JPA + native queries**: Most CRUD uses Spring Data JPA. Native queries (in `ModificationRepository`) handle JSONB tag filtering with Postgres `@>` operator.
- **Custom auth flow**: JWT tokens in httpOnly cookies (`AccessToken`). Spring Security with a custom `UsernamePasswordAuthenticationProvider` and `TokenAuthenticationFilter`. Tokens are auto-renewed within 5 min of expiry.
- **JSONB storage**: `Modification.tags` and `Modification.accessories` (including nested `Tuning` objects) are stored as JSONB columns using Hypersistence Utils `JsonType`.
- **Manager layer**: `UserManager` and `UserCredentialManager` sit between service and repository, adding `@Transactional` boundaries without mixing concerns.
- **DTOs as Java records**: All request/response objects are immutable records with static `from()` factory methods for entity→DTO conversion.
- **Flyway migrations**: SQL migrations in `src/main/resources/db/migration/` — V2 (init), V3 (bullet/damage fields), V4 (user), V5 (accessories JSONB column).

### Data model

- `firearm` table: id, name, type (int→FirearmType enum), level, calibre, fire_rate, armour_damage, body_damage, review
- `modification` table: id, firearm_id (FK→firearm), name, code, tags (jsonb), accessories (jsonb), note, author, video_url
- `app_user` table: id, username, email
- `user_credential` table: user_id, provider, credential (hashed)

### API endpoints

| Path | Methods | Auth |
|---|---|---|
| `/firearms` | GET, POST | GET public, POST requires auth |
| `/firearms/{id}` | GET, PUT, DELETE | GET public, PUT/DELETE requires auth |
| `/modifications` | GET, POST | GET public, POST requires auth |
| `/modifications/{id}` | GET, PUT, DELETE | GET public, PUT/DELETE requires auth |
| `/modifications/batch` | POST | Requires auth |
| `/modifications/batch-delete` | DELETE | Requires auth |
| `/tags` | GET | Public |
| `/auth/login` | POST | Public |
| `/auth/logout` | POST | Authenticated |

### Commit convention

Conventional commits: `feat:`, `chore:`, `fix:`. Messages are in English, present tense imperative style.

### External dependencies

- **DB**: PostgreSQL (via Flyway migrations), H2 in test
- **Cache**: Redis (via Spring Cache + RedisTemplate with GenericJackson2JsonRedisSerializer)
- **Auth**: java-jwt (auth0), BCrypt
- **Docs**: springdoc-openapi (Swagger UI) on dev profile
- **Onixbyte internal libs**: version-catalogue, tuple, common-toolbox, math-toolbox, identity-generator, captcha, regions
- **AWS**: S3 SDK

### Profiles

- `dev`: Enables Swagger UI, connects to dev DB/Redis at `dfguide.onixbyte.cn`. Config in `config/application-dev.yaml`.
- Default profile: Used for production, no Swagger, connects to production datasource.
