# Chronyqor — Enterprise Full-Stack Aufgabenverwaltung

Chronyqor ist eine umfassende, produktionsreife Full-Stack-Anwendung. Sie bietet eine sichere Spring Boot REST-API, eine hochperformante Redis-Caching-Schicht und ein hochwertiges Frontend im Glassmorphism-Design. Basierend auf modernen Architekturmustern legt sie besonderen Wert auf Sicherheit, Skalierbarkeit und ein erstklassiges Benutzererlebnis.

---

## 🚀 Hauptmerkmale

### 🛡️ Erweiterte Sicherheit
- **JWT & Refresh Tokens**: Sichere zustandslose Authentifizierung mit rotierenden Refresh-Tokens für langlebige Sitzungen.
- **Account Lockout**: Schutz gegen Brute-Force-Angriffe durch Sperrung des Kontos nach 5 aufeinanderfolgenden fehlgeschlagenen Login-Versuchen.
- **Mandantentrennung**: Kryptografisch sichere Datentrennung; Benutzer haben ausschließlich Zugriff auf ihre eigenen Daten.

### ⚡ Performance & Skalierbarkeit
- **Redis Caching**: Hochgeschwindigkeits-Caching für Aufgabenabfragen zur Minimierung der Datenbanklast.
- **Pagination & Sortierung**: Effiziente Datenverarbeitung durch Spring Data Pagination.
- **Optimiertes Docker**: Multi-Stage-Builds und Layered JARs für schnelle Bereitstellung.

### 📋 Enterprise-Logik
- **Vollständiges Audit-Logging**: Automatische Nachverfolgung von `createdBy` und `lastModifiedAt` mittels JPA Auditing.
- **E-Mail-Benachrichtigungen**: Integrierter Mail-Service für Willkommensnachrichten und Systembenachrichtigungen.
- **Standardisierte API**: RFC 9457 konforme Fehlerbehandlung und umfassende OpenAPI/Swagger-Dokumentation.

### 🎨 Premium-Frontend
- **Modernes UI**: Eine beeindruckende Single-Page-Anwendung (SPA), erstellt mit Tailwind CSS und Glassmorphism-Prinzipien.
- **Dark Mode**: Augenfreundliches, kontrastreiches dunkles Design standardmäßig aktiviert.
- **Interaktiv**: Echtzeit-Feedback, flüssige Übergänge und intuitive Aufgabenverwaltung.

---

## 🛠️ Technologie-Stack

| Kategorie | Technologie |
|-----------|-------------|
| **Frontend** | HTML5, Tailwind CSS, Vanilla JS (ES6+) |
| **Backend** | Java 21, Spring Boot 3.4.x |
| **Sicherheit** | Spring Security, JWT, Refresh Tokens |
| **Datenbank** | PostgreSQL 16 (Persistenz), H2 (Entwicklung/Test) |
| **Caching** | Redis 7 |
| **Infrastruktur**| Nginx (Reverse Proxy), Docker, Docker Compose |
| **Dokumentation** | Swagger UI (OpenAPI 3.0) |

---

## 🏗️ Projektstruktur

```text
chronyqor/
├── frontend/           # Premium SPA Quelldateien
├── src/main/java/      # Spring Boot Backend
│   ├── config/         # Cache-, JPA- und App-Konfigurationen
│   ├── controller/     # REST-Endpunkte (Auth, Tasks, Health)
│   ├── domain/         # Entitäten (User, Task, RefreshToken)
│   ├── security/       # JWT-Logik und Sicherheitsfilter
│   └── service/        # Geschäftslogik und Benachrichtigungen
├── src/main/resources/
│   └── db/migration/   # Flyway SQL-Migrationen (V1 bis V4)
├── nginx.conf          # Nginx Proxy-Konfiguration
└── docker-compose.yml  # Full-Stack Orchestrierung
```

---

## 🚀 Erste Schritte

### Voraussetzungen
- **Docker & Docker Compose**

### Schnellstart (Docker)
Der einfachste Weg, den gesamten Stack auszuführen, ist die Verwendung von Docker Compose:

```bash
docker-compose up --build
```

**Zugriffspunkte:**
- **Frontend**: [http://localhost](http://localhost) (Port 80)
- **Swagger Docs**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **API Basis**: `http://localhost:8080/api/v1`

### Lokale Backend-Entwicklung
Um das Backend unabhängig mit einer H2 In-Memory-Datenbank auszuführen:
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

---

## 🧪 Tests

Chronyqor nutzt **Testcontainers** für robuste Integrationstests gegen eine echte PostgreSQL-Instanz.

```bash
./gradlew test
```

---

## 📊 Monitoring & Status

- **Health Status**: `GET /actuator/health`
- **Metriken**: `GET /actuator/metrics`
- **Prometheus**: `GET /actuator/prometheus`

---

## 📖 Dokumentation
Detaillierte Anweisungen finden Sie im [User Guide](USER_GUIDE.md).

## 📄 Lizenz
Dieses Projekt ist unter der MIT-Lizenz lizenziert.
die GitHub Issues.

---

> 💡 **Tipp**: Stellen Sie vor dem ersten Start sicher, dass alle Abhängigkeiten installiert sind und die Umgebungsvariablen korrekt konfiguriert sind.