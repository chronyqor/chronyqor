# Chronyqor — Enterprise Full-Stack Task Management

Chronyqor is a comprehensive, production-ready full-stack application. It features a secure Spring Boot REST API, a high-performance Redis caching layer, and a premium glassmorphism-style frontend. Built with modern architectural patterns, it emphasizes security, scalability, and exceptional user experience.

---

## 🚀 Key Features

### 🛡️ Advanced Security
- **JWT & Refresh Tokens**: Secure stateless authentication with rotating refresh tokens for long-lived sessions.
- **Account Lockout**: Brute-force protection that locks accounts after 5 consecutive failed login attempts.
- **Multi-Tenant Isolation**: Cryptographically secure data ownership; users only access their own data.

### ⚡ Performance & Scalability
- **Redis Caching**: High-speed caching for task retrieval to minimize database load.
- **Pagination & Sorting**: Efficient data handling using Spring Data Pagination.
- **Optimized Docker**: Multi-stage builds and layered JARs for rapid deployment.

### 📋 Enterprise Logic
- **Full Audit Logging**: Automated tracking of `createdBy` and `lastModifiedAt` using JPA Auditing.
- **Email Notifications**: Integrated mail service for welcome messages and system notifications.
- **Standardized API**: RFC 9457 compliant error handling and comprehensive OpenAPI/Swagger documentation.

### 🎨 Premium Frontend
- **Modern UI**: A stunning single-page application built with Tailwind CSS and glassmorphism principles.
- **Dark Mode**: Eye-friendly, high-contrast dark theme by default.
- **Interactive**: Real-time feedback, smooth transitions, and intuitive task management.

---

## 🛠️ Technology Stack

| Category | Technology |
|----------|------------|
| **Frontend** | HTML5, Tailwind CSS, Vanilla JS (ES6+) |
| **Backend** | Java 21, Spring Boot 3.4.x |
| **Security** | Spring Security, JWT, Refresh Tokens |
| **Database** | PostgreSQL 16 (Persistence), H2 (Dev/Testing) |
| **Caching** | Redis 7 |
| **Infrastructure**| Nginx (Reverse Proxy), Docker, Docker Compose |
| **Documentation** | Swagger UI (OpenAPI 3.0) |

---

## 🏗️ Project Structure

```text
chronyqor/
├── frontend/           # Premium SPA source files
├── src/main/java/      # Spring Boot Backend
│   ├── config/         # Cache, JPA, and App configurations
│   ├── controller/     # REST Endpoints (Auth, Tasks, Health)
│   ├── domain/         # Entities (User, Task, RefreshToken)
│   ├── security/       # JWT logic and Security filters
│   └── service/        # Business logic and Notifications
├── src/main/resources/
│   └── db/migration/   # Flyway SQL migrations (V1 to V4)
├── nginx.conf          # Nginx proxy configuration
└── docker-compose.yml  # Full-stack orchestration
```

---

## 🚀 Getting Started

### Prerequisites
- **Docker & Docker Compose**

### Quick Launch (Docker)
The easiest way to run the entire stack is using Docker Compose:

```bash
docker-compose up --build
```

**Access Points:**
- **Frontend**: [http://localhost](http://localhost) (Port 80)
- **Swagger Docs**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **API Base**: `http://localhost:8080/api/v1`

### Local Backend Development
To run the backend independently with an H2 in-memory database:
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

---

## 🧪 Testing

Chronyqor uses **Testcontainers** for robust integration testing against a real PostgreSQL instance.

```bash
./gradlew test
```

---

## 📊 Monitoring & Health

- **Health Status**: `GET /actuator/health`
- **Metrics**: `GET /actuator/metrics`
- **Prometheus**: `GET /actuator/prometheus`

---

## 📖 Documentation
Detailed instructions can be found in the [User Guide](USER_GUIDE.md).

## 📄 License
This project is licensed under the MIT License.