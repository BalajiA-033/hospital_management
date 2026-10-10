# Functional & Non-Functional Requirements

## 1. Functional Requirements

### 1.1 Authentication & Role-Based Access Control (RBAC)
- System must authenticate users via BCrypt password verification.
- Roles supported: `ADMIN`, `DOCTOR`, `STAFF`.

### 1.2 Patient Management
- Register new patients with demographic details (Name, DOB, Phone, Email, Address, Blood Group, Emergency Contact).
- Search patient directory by name, phone number, or email.
- Edit existing patient records and delete records when business rules permit.

### 1.3 Doctor Management
- Maintain doctor directory with specializations, qualifications, and consultation fees.
- Update doctor availability status (`AVAILABLE`, `ON_LEAVE`, `BUSY`, `OFF_DUTY`).

### 1.4 Appointment Scheduling
- Book consultation appointments for registered patients with specific doctors on designated dates and time slots.
- Validate doctor availability and prevent overlapping scheduled appointments for the same doctor/time slot.

### 1.5 Hospital Bed Management
- View real-time bed inventory categorized by ward and bed type (`ICU`, `GENERAL`, `PRIVATE`, `SEMI_PRIVATE`).
- Allocate available beds to patients using database transactions.
- Release occupied beds upon patient discharge.

### 1.6 Emergency Case Management
- Intake emergency patients with assigned triage priority (`CRITICAL`, `HIGH`, `MEDIUM`, `LOW`).
- Assign doctors and emergency beds immediately.

### 1.7 Pharmacy & Inventory Management
- Track medicine stock levels, unit prices, reorder thresholds, and expiration dates.
- Execute dispense and restock stock transactions.
- Prevent stock quantities from falling below zero.
- Display visual alerts for low-stock medicines.

### 1.8 Real-time Dashboard & Analytics
- Display live metrics: Total Patients, Total Doctors, Today's Appointments, Available Beds, Occupied Beds, Low Stock Alerts.
- Periodically poll database every 5 seconds without locking the user interface.

---

## 2. Non-Functional Requirements
- **Performance:** GUI responses under 200ms for user actions.
- **Concurrency:** Transactional isolation preventing double allocation of beds or negative pharmacy stock under concurrent usage.
- **Portability:** Executable Fat JAR running on any Java 21+ environment with automatic H2 fallback mode.
