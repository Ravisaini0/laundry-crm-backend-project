# 🧺 Laundry Management System - Backend

A Spring Boot based Laundry Management System backend application.  
This project handles authentication, user management, admin operations, and delivery boy operations.

---

## 🚀 Tech Stack

- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- Hibernate
- MySQL
- Maven

---

## 📊 Project Status

| Module          | Status |
|---------------|--------|
| Auth Module   | ✅ 100% |
| User Module   | ✅ 80% |
| Admin Module  | 🟡 70% |
| Delivery Boy  | 🔄 In Progress |
| Overall       | 🔥 75% Complete |

---

## 📁 Project Structure
com.laundry
│
├── config
├── controller
├── dto
├── entity
├── repository
├── security
├── exception
└── LaundryProjectApplication

---

## 🔐 Features

### ✅ Authentication
- User Login
- Role-based access
- JWT Token generation
- Secure endpoints

### 👤 User Module
- Place Order
- View Orders
- Update Profile

### 🛠 Admin Module
- Manage Users
- Manage Orders
- Assign Delivery Boy

### 🚚 Delivery Boy Module
- View Assigned Orders
- Update Order Status

---

## 🗄 Database Configuration

Update  `application.properties`:

spring.datasource.url=******
spring.datasource.username=root
spring.datasource.password=*****

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true


---

## ▶️ How To Run

1. Clone the repository
2. Open in Spring Tool Suite / IntelliJ
3. Update database credentials
4. Run:

mvn clean install
mvn spring-boot:run


Or run `LaundryProjectApplication.java`

---

## 🧪 Run Tests

mvn test

---

## 🔒 API Base URL

http://localhost:8080/api


---

## 📌 Future Improvements

- Payment Integration
- Order Tracking System
- Email Notifications
- Admin Dashboard UI
- Docker Deployment

---

## 👨‍💻 Developed By

Ravi  
Backend Developer 🚀
