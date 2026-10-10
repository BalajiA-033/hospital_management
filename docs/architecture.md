# System Architecture & Layered Design

## Overview
The Smart Hospital Management System uses a clean **Layered (N-Tier) Architecture** to separate user interface presentation, business rules, database access, and persistence.

```mermaid
flowchart TD
    subgraph Presentation Layer
        UI["Swing GUI (MainDashboardFrame)"]
        Panels["DashboardPanel | PatientPanel | DoctorPanel | BedPanel | PharmacyPanel"]
    end

    subgraph Service Layer
        Services["AuthService | PatientService | DoctorService | BedService | PharmacyService"]
    end

    subgraph Data Access Layer (DAO)
        DAOs["UserDAO | PatientDAO | DoctorDAO | BedDAO | PharmacyDAO"]
    end

    subgraph Infrastructure Layer
        Config["DatabaseConfig (JDBC Connection Manager)"]
        DB[("MySQL 8.x Primary / H2 In-Memory Fallback")]
    end

    UI --> Panels
    Panels --> Services
    Services --> DAOs
    DAOs --> Config
    Config --> DB
```

## Architectural Highlights
- **Decoupled Architecture:** Presentation elements do not issue SQL statements directly; all database interaction passes through DAO classes.
- **Transactional Safety:** Critical operations (bed allocation, medicine dispensing) execute inside explicit database transactions with `conn.setAutoCommit(false)`, `conn.commit()`, and rollback handling.
- **Asynchronous Execution:** Long-running database calls run off the Swing Event Dispatch Thread (EDT) using `SwingWorker<T, V>`.
- **Database Fallback Strategy:** If a MySQL 8.x instance is unreachable, `DatabaseConfig` seamlessly initializes an embedded H2 database in memory.
