# Employee Management System – Backend

This is the **backend service** for the Employee Management System, built using **Java 21** and **Spring Boot**, with **Keycloak authentication** and role-based access control.

---

## 🚀 Tech Stack

- Java 21
- Spring Boot
- Spring Security
- Keycloak (Authentication & Authorization)
- Hibernate / JPA
- MySQL
- Maven
- REST APIs

---

## 🔐 Authentication (Keycloak)

The backend uses **Keycloak** for secure authentication and authorization.

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

---

## 📁 Project Structure
src/main/java
└── com.example.employeebackend
├── controller
├── service
├── repository
├── entity
├── security
└── config


---

## ⚙️ Configuration

Update `application.properties`:

```properties
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/employee_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update

keycloak.realm=employee-realm
keycloak.auth-server-url=http://localhost:8080
keycloak.resource=employee-backend
keycloak.public-client=true
