# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Monorepo Structure

- `server/` — Java 21 + Spring Boot 3.5 + Gradle (Kotlin DSL) REST API backend
- `web/` — React 19 + Vite 8 + TypeScript + Ant Design 6 + Tailwind CSS 4 frontend

## Server

### Coding Standards

- **Style**: Follow the Google Java Coding Style as the foundation.
- **Indentation**: Use 4 spaces — no tabs.
- **Line length**: Maximum 100 characters per line.
- **Comments**: All code comments must use British English spelling (e.g. "colour" not "color", "behaviour" not "behavior", "serialise" not "serialize", "analyse" not "analyze", "traveller" not "traveler").

### Build & Test Commands

```bash
cd server

# Build the project (skip tests)
./gradlew build -x test

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.onixbyte.ahsarahguide.service.PasswordEncoderTest"

# Build the full JAR
./gradlew bootJar
```

The project uses Gradle with Java 21 (Amazon Corretto). Tests use JUnit 5 with the Spring Boot test framework, H2 in-memory database for test runtime, and Spring Security test support. Tests require an active `dev` profile.

### Code Architecture

**Ahsarah Guide Server** — A REST API backend for managing firearm builds/modifications.

### Package structure

```
com.onixbyte.ahsarahguide
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
- **Strict layering**: The call chain must follow `Controller → Service → Manager → Repository/Mapper`. Skipping layers (e.g. Controller calling Manager directly, Service calling Repository directly) is not permitted.
- **DTOs as Java records**: All request/response objects are immutable records with static `from()` factory methods for entity→DTO conversion.
- **Flyway migrations**: SQL migrations in `src/main/resources/db/migration/`.

### API endpoints

| Path                          | Methods          | Auth                                 |
|-------------------------------|------------------|--------------------------------------|
| `/firearms`                   | GET, POST        | GET public, POST requires auth       |
| `/firearms/{id}`              | GET, PUT, DELETE | GET public, PUT/DELETE requires auth |
| `/modifications`              | GET, POST        | GET public, POST requires auth       |
| `/modifications/{id}`         | GET, PUT, DELETE | GET public, PUT/DELETE requires auth |
| `/modifications/batch`        | POST             | Requires auth                        |
| `/modifications/batch-delete` | DELETE           | Requires auth                        |
| `/tags`                       | GET              | Public                               |
| `/auth/login`                 | POST             | Public                               |
| `/auth/logout`                | POST             | Authenticated                        |

### Commit convention

Conventional commits: `feat:`, `chore:`, `fix:`. Messages are in English, present tense imperative style.

---

## Web

### Commands

```bash
cd web

pnpm install          # Install dependencies (pnpm required)
pnpm dev              # Start Vite dev server
pnpm build            # TypeScript check + Vite production build
pnpm build:tar        # Build + tar.gz archive (used by CI)
pnpm preview          # Preview production build locally
pnpm lint             # ESLint
```

No test suite exists in this project.

### Architecture

Chinese-language SPA for browsing and managing Ahsarah Guide (阿萨拉向导). The frontend talks to the Spring Boot backend via REST APIs.

**App shell** (`src/main.tsx`): React 19 + React Router 7 + Redux Toolkit + Ant Design 6 + Tailwind CSS 4. Wraps the router in Redux `Provider` → `PersistGate` (Redux Persist) → Ant Design `StyleProvider`/`ConfigProvider` (locale `zh_CN`).

**Routing** (`src/router/index.tsx`): Two layout groups:
- `HeroLayout` (nav header + footer) for `/`, `/firearms`, `/mod-codes`
- `EmptyLayout` (minimal) for `/login`
All page components are lazy-loaded via `createBrowserRouter` + `lazy()`.

**State** (`src/store/`): Redux Toolkit with two slices — `auth` (current user) and `firearms` (paginated firearm list). State is persisted to `localStorage` or `sessionStorage` based on the `VITE_REDUX_STORAGE` env var. Use typed hooks from `src/hooks/store.ts` (`useAppDispatch`, `useAppSelector`).

**API layer** (`src/api/`): Axios instance (`src/shared/web-client/`) with base URL from `VITE_API_BASE_URL`, 10s timeout, and credentials. API modules: `FirearmApi`, `ModificationApi`, `TagApi`, `AuthApi`.

**Pages**:
- `FirearmsPage` — paginated card grid with type filter, create/edit modals (admin-only), delete with popconfirm
- `ModCodesPage` — paginated list with tag multi-select and firearmId query param filter, create/edit modals with nested accessory/tuning form lists
- `LoginPage` — simple username/password form, dispatches `setCurrentUser` on success

**Shared form components**: `FirearmForm` and `ModificationForm` are reused by both create and edit modals. `ModificationForm` fetches all firearms for its weapon selector and supports a `lockFirearmId` prop that disables the selector (used when navigating from a specific firearm).

**Type system** (`src/types/`): `Firearm` with weapon stats, `Modification` with nested `Accessory[]` → `Tuning[]`, `Page<T>` for paginated API responses, `User` for auth.

**Vite config**: Alias `@` → `./src`. Plugins: React, Tailwind CSS 4, port checker. Build uses rolldown with manual chunk splitting for React, Redux, Ant Design, React Router, and rc-* packages.

**Styling**: Tailwind CSS 4 with CSS layers (`theme, base, antd, components, utilities`). Responsive grid for mod code cards (1→2→3→4 columns). Prettier: 100 print width, no semicolons, double quotes, trailing commas ES5.

### Environment variables

```
VITE_API_BASE_URL=/api        # Backend API base URL
VITE_REDUX_STORAGE=local      # "local" or "session" for Redux persistence
```

### Contributing conventions

- User-facing copy, documentation, and code comments in British English
- Commit messages use `chore:` prefix for dependency updates (per Dependabot config)
