# Ahsarah Guide (阿萨拉向导)

Ahsarah Guide is a full-stack web application for browsing and managing firearm modification guides.

## Monorepo Structure

| Directory | Stack | Description |
|-----------|-------|-------------|
| `server/` | Java 21 + Spring Boot 3.5 + Gradle | REST API backend |
| `web/` | React 19 + Vite 8 + TypeScript + Ant Design 6 | Web frontend |

## Quick Start

### Server

Prerequisites: JDK 21 (Amazon Corretto), PostgreSQL, Redis

```bash
cd server
cp config/application-prod.yaml.example config/application-dev.yaml
# Edit config/application-dev.yaml with your database and Redis credentials
./gradlew bootJar
java -jar build/libs/ahsarah-guide-server-*.jar
```

### Web

Prerequisites: Node.js 20+, pnpm

```bash
cd web
pnpm install
pnpm dev
```

## Licence

MIT. See [LICENCE](LICENCE) for details.
