#Employee Management System – Backend

This repository contains the backend service for the Employee Management System, developed using Java 21 and Spring Boot.
It exposes secure REST APIs with Keycloak-based authentication, role-based access control, and modular enterprise-level architecture.

---

## 🚀 Tech Stack

- Java 21
- Spring Boot
- Spring Security (OAuth2 Resource Server – JWT)
- Keycloak (Authentication & Authorization)
- Hibernate / JPA
- MySQL
- Maven
- REST APIs

---

## 🔐 Authentication & Authorization (Keycloak)

The backend uses **Keycloak** for secure authentication and authorization.

**Keycloak Setup**
-Keycloak URL: http://localhost:8080
-Realm: employee-realm
-Authentication: JWT (Bearer Token)
-Integration: Spring Security OAuth2 Resource Server

### Roles
- ADMIN
- MANAGER
- EMPLOYEE

### Security Features
- JWT token based authentication
- Role-based API access
- Centralized login using Keycloak
- Spring Security integration

---

## 📦 Backend Modules

### 👤 Employee Management
- Create, update, delete employees
- Manage employee profiles
- Assign roles and departments

### 🕒 Attendance Management
- Mark daily attendance
- Attendance status: PRESENT / ABSENT / LEAVE
- Manager & Admin approval flows

### 🏢 Department Management
- Create and manage departments
- Map employees to departments

### 📊 Dashboard
- Admin dashboard overview
- Attendance summaries
- Employee statistics

📁 Profile Management
-Personal information
-Education details
-Professional experience
-Bank details
-Document uploads

---

## 📁 Project Structure
src/main/java
└── com.example.employeebackend
    ├── attendance
    │   ├── controller
    │   ├── service
    │   ├── repository
    │   └── entity
    ├── auth
    ├── controller
    ├── dashboard
    ├── service
    ├── repository
    ├── entity
    ├── config
    ├── security
    └── exception


---

## ⚙️ Application Configuration

Server Configuration

```properties
server.port=8081

Database Configuration (MySQL)

spring.datasource.url=jdbc:mysql://localhost:3307/employee_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

JPA / Hibernate

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=true

File Upload Configuration

spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB

Keycloak (JWT Resource Server)

spring.security.oauth2.resource-server.jwt.issuer-uri=http://localhost:8080/realms/employee-realm

**How to Run the Backend**

*Prerequisites

-Java 21
-Maven
-MySQL running on port 3307
-Keycloak running on port 8080
-Realm created: employee-realm

*Steps
BASH
-mvn clean install
-mvn spring-boot:run

*Backend URL
-http://localhost:8081

🔗 Frontend Integration

This backend is designed to work with an Angular frontend secured by Keycloak.
All API requests must include a JWT token in the Authorization header:
-Authorization: Bearer <access_token>

🧪 API Testing

You can test APIs using:

-Postman
-Swagger (can be added)
-Angular frontend

Ensure Keycloak is running and a valid token is used.


