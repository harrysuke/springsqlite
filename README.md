# Spring Boot (Kotlin) + JTE + Vanilla JS Fetch

A modern, lightweight full-stack web application starter using **Spring Boot**, **Kotlin**, **JTE (Java Template Engine)**, and frontend interactions powered by native **Vanilla JavaScript Fetch API**.

This stack eliminates heavy frontend build pipelines (like Webpack, Vite, or npm dependencies) by rendering fast, type-safe HTML templates on the server and handling dynamic page interactions using native browser APIs.

> 📘 **Technical documentation:** see [TECHNICAL.md](TECHNICAL.md) for the system architecture, the SQLite entity diagram, the HTTP API reference, the security model, and the Docker/CI-CD setup.

## 🚀 Features
* **Kotlin Backend:** Clean, idiomatic backend development using Spring Boot and Kotlin coroutines/idioms.
* **JTE Templates:** High-performance, compile-time checked server-side rendering with Java Template Engine.
* **Zero-Dependency Frontend:** Client-side updates, form submissions, and API interactions handle smoothly via Vanilla JS `fetch()`.
* **SQLite Database:** Embedded file-based database (`springdb.db`) — no MySQL/server setup required; schema and data managed by Flyway migrations.
* **Gradle Wrapper:** Build and run cleanly out of the box without requiring local Gradle installations.

## 🛠️ Tech Stack
| **Layer** | **Technology** |
|---|---|
| **Backend Framework** | Spring Boot |
| **Language** | Kotlin |
| **Template Engine** | JTE (Java Template Engine) |
| **Database** | SQLite (embedded, file-based — no server required) |
| **Schema Migration** | Flyway |
| **Build System** | Gradle (Kotlin DSL - `build.gradle.kts`) |
| **Frontend Utilities** | HTML5, Vanilla JavaScript (Fetch API) |

## 📁 Repository Structure
```text
├── .github/workflows/      # CI/CD Automation workflows
├── gradle/wrapper/         # Gradle Wrapper files
├── src/
│   ├── main/
│   │   ├── kotlin/         # Spring Boot Application code & controllers
│   │   └── resources/
│   │       ├── db/migration/ # Flyway SQL migrations (schema + seed data)
│   │       ├── templates/  # JTE templates (.jte files)
│   │       └── application.properties / application.yml
├── build.gradle.kts        # Project dependencies and build scripts
└── settings.gradle.kts     # Gradle project configuration
```

## 🗄️ Database

The application uses **SQLite** — an embedded, file-based database with zero server setup. The database file (`springdb.db`) is created in the project root automatically on first run and is excluded from version control via `.gitignore`.

### Configuration
```properties
spring.datasource.url=jdbc:sqlite:springdb.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.flyway.enabled=true
```

> **Note:** SQLite allows only a single writer at a time, so HikariCP is configured with `maximum-pool-size=1`.

### Schema Migration (Flyway)
Migrations live in `src/main/resources/db/migration/` and run automatically at startup:

| Version | File | Description |
|---|---|---|
| V1 | `V1__create_users.sql` | Creates the `users` table (SQLite dialect) |
| V2 | `V2__import_mysql_users.sql` | Imports existing users from the legacy MySQL database |

V2 preserves the original user IDs, emails, and BCrypt password hashes, so all existing logins continue to work unchanged. A default admin account is seeded on first run by `DataInitializer`:

* **Email:** `admin@example.com`
* **Password:** `admin123`

### Starting Fresh
Delete the database file and restart the app — Flyway will rebuild the schema and re-import the users:
```bash
rm springdb.db
./gradlew bootRun
```

## 🐳 Docker

The project ships with a multi-stage `Dockerfile` (build with Gradle, run on Temurin JRE 25) and a `docker-compose.yml`.

### Build & run with Docker Compose
```bash
docker compose up --build
```
The app will be available at `http://localhost:8080/` (context path `/` inside the container, so login is at `http://localhost:8080/login`).

### Build & run with plain Docker
```bash
docker build -t springsqlite .
docker run -d --name springsqlite -p 8080:8080 -v springsqlite-data:/data springsqlite
```

### Data persistence
The SQLite database is stored at `/data/springdb.db` inside the container and is mounted on the named volume `springsqlite-data`, so data survives container restarts/rebuilds. To reset:
```bash
docker compose down -v   # removes the volume
docker compose up --build
```

### Configuration
Override the database location (or any Spring property) via environment variables:
```yaml
environment:
  DB_URL: jdbc:sqlite:/data/springdb.db
```

---
## ⚙️ Getting Started

### Prerequisites
Ensure you have the following installed on your machine:
* **Java Development Kit (JDK):** Version 17 or higher (recommended)
* **IDE:** IntelliJ IDEA (highly recommended for Kotlin and JTE syntax support)

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/harrysuke/springjte
   cd springjte
   ```
2. Build the project using the Gradle wrapper:
   ```bash
   ./gradlew build
   ```

### Running the Application
Start the Spring Boot server directly from your terminal:
```bash
./gradlew bootRun
```
Once started, open your web browser and navigate to `http://localhost:8080`.

## 💡 Development Patterns Used

### 1. Server-Side Rendering (JTE)
Templates are located in `src/main/resources/templates`. JTE compiles these templates directly into Java bytecode, making it incredibly fast.

Example Kotlin Controller integration:
```kotlin
@Controller
class HomeController {
    @GetMapping("/")
    fun index(model: Model): String {
        model.addAttribute("title", "Welcome to SpringJTE")
        return "index" // Renders index.jte
    }
}
```

### 2. Client-Side Interactions (Vanilla JS Fetch)
Instead of importing a massive JavaScript framework, asynchronous state updates use native browser features:
```javascript
// Example frontend fetch pattern inside the HTML scripts
fetch('/api/data', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({ message: "Hello Server" })
})
.then(response => response.json())
.then(data => {
    console.log('Success:', data);
    // Dynamically update DOM here
})
.catch(error => console.error('Error:', error));
```
