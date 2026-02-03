# Employee Leave Management System (ELMS) - Backend

This is the Spring Boot backend for the Employee Leave Management System.

## Technology Stack
- **Java**: 21
- **Framework**: Spring Boot 3.2.1
- **Database**: MySQL
- **Security**: Spring Security with JWT
- **Build Tool**: Maven

## Prerequisites
- JDK 21
- MySQL Server
- Maven

## Database Setup
1. Create a database named `elms_db`.
2. Update `src/main/resources/application.properties` with your MySQL credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/elms_db
   spring.datasource.username=YOUR_USERNAME
   spring.datasource.password=YOUR_PASSWORD
   ```

## Getting Started
To run the application locally:

```bash
mvn spring-boot:run
```

The server will start on `http://localhost:8080`.

## API Endpoints
- `POST /api/auth/signup`: User registration
- `POST /api/auth/signin`: User login
- `GET /api/leave-types`: List all leave types
- `GET /api/leaves/my-leaves`: Get current user's leaves
- `POST /api/leaves/apply`: Apply for leave
- `GET /api/statistics/admin-dashboard`: Admin statistics (Admin ONLY)
