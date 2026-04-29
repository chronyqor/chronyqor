# Chronyqor User Guide

Welcome to the Chronyqor User Guide. This document provides detailed instructions on how to use, configure, and maintain the Chronyqor Task Management System.

---

## 1. Using the Web Application

### Registration and Login
1.  Navigate to `http://localhost`.
2.  Click **Create Account** if you don't have one.
3.  Enter your username, email, and a secure password.
4.  Once registered, you will receive a welcome email (if the mail server is configured).
5.  Login with your credentials to access your personal dashboard.

### Managing Tasks
*   **Add Task**: Click the **New Task** button. Fill in the title, description, status, and due date.
*   **Edit Task**: Click the pencil icon on any task card to update its details.
*   **Delete Task**: Click the trash icon to remove a task permanently.
*   **Filter/Sort**: Tasks are automatically sorted by creation date (newest first) and paginated (10 per page).

### Security Features
*   **Session Management**: The app uses JWT for short-term access and Refresh Tokens for long-term sessions. If your session expires, the app will try to refresh it automatically.
*   **Account Lockout**: If you enter the wrong password 5 times in a row, your account will be locked. Contact an administrator to unlock it (or reset the `account_non_locked` flag in the database).

---

## 2. API Reference (Developers)

The API follows REST principles and is documented via Swagger.

### Authentication
All requests (except auth) require a Bearer Token:
`Authorization: Bearer <your_jwt_token>`

### Common Endpoints
*   `POST /api/v1/auth/login`: Authenticate and get tokens.
*   `POST /api/v1/auth/refresh-token`: Exchange a refresh token for a new access token.
*   `GET /api/v1/tasks`: Get paginated tasks. Supports query params: `page`, `size`, `sort`.
*   `GET /actuator/health`: Check system health (DB, Redis, etc.).

---

## 3. Deployment & Configuration

### Environment Variables
You can customize the deployment using environment variables in `docker-compose.yml`:

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_HOST` | Database host | `db` |
| `DB_NAME` | Database name | `chronyqor` |
| `MAIL_HOST` | SMTP server host | `smtp.example.com` |
| `JWT_SECRET` | Secret key for JWT | (provided in yml) |

### Database Migrations
Migrations are handled automatically by **Flyway** on startup.
*   `V1`: Initial schema.
*   `V2`: User management.
*   `V3`: Advanced security (Lockout & Refresh Tokens).
*   `V4`: Audit fields.

### Caching
The application uses **Redis** for caching task data. If you update or delete a task, the cache is automatically evicted to ensure data consistency.

---

## 4. Troubleshooting

*   **Cannot Login**: Check if the backend container is running and the DB is healthy.
*   **Email not sending**: Verify SMTP settings in `application.yml` or environment variables.
*   **Slow performance**: Ensure the Redis container is active.

---

## 5. Support
For technical issues, please open an issue in the repository or contact the development team at `dev@chronyqor.org`.
