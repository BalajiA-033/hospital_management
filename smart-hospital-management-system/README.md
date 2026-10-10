# Smart Hospital Management System
> **Database-Driven Real-Time Desktop Application with Java Swing GUI Integration**
> 
> *DBMS College Practical Assignment & Demonstration Case Study*

---

## 📌 Project Overview
The **Smart Hospital Management System (SHMS)** is a full-featured desktop application designed to streamline core hospital operations, including patient registration, doctor directory scheduling, appointment booking, hospital bed availability allocation, emergency triage management, and pharmacy inventory control.

The system connects a modern **Java Swing GUI** to a **MySQL 8.x relational database** (with automatic fallback to an embedded **H2 database** for offline execution), implementing atomic transactions, BCrypt security, and real-time periodic database polling.

---

## 🚀 Build and Run Instructions

### Compile the Project
```bash
mvn clean compile
```

### Run Unit Tests
```bash
mvn test
```

### Package the Application
```bash
mvn package
```

### Launch the Application
```bash
java -jar target/smart-hospital-management-system-1.0.0.jar
```

---

## 🔑 Default Credentials for Testing
- **Admin User:** `admin` / `admin123`
- **Doctor User:** `dr_john` / `doctor123`
- **Staff User:** `staff_mary` / `staff123`
