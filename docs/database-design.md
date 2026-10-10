# Database Design & Schema Specifications

## 1. Database Specifications
- **DBMS:** MySQL 8.0+ / H2 In-Memory DB
- **Character Set:** `utf8mb4`
- **Collation:** `utf8mb4_unicode_ci`
- **Engine:** InnoDB (ACID compliant)

## 2. Table Schemas & Constraints

### 2.1 `users`
- `user_id` INT AUTO_INCREMENT PRIMARY KEY
- `username` VARCHAR(50) NOT NULL UNIQUE
- `password_hash` VARCHAR(255) NOT NULL
- `role` ENUM('ADMIN', 'DOCTOR', 'STAFF') NOT NULL
- `full_name` VARCHAR(100) NOT NULL
- `email` VARCHAR(100) NOT NULL UNIQUE
- `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP

### 2.2 `patients`
- `patient_id` INT AUTO_INCREMENT PRIMARY KEY
- `first_name` VARCHAR(50) NOT NULL
- `last_name` VARCHAR(50) NOT NULL
- `gender` ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL
- `dob` DATE NOT NULL
- `phone` VARCHAR(15) NOT NULL UNIQUE
- `email` VARCHAR(100) UNIQUE
- `address` TEXT
- `blood_group` VARCHAR(5) NOT NULL
- `emergency_contact` VARCHAR(15) NOT NULL

### 2.3 `doctors`
- `doctor_id` INT AUTO_INCREMENT PRIMARY KEY
- `user_id` INT UNIQUE NULL (FK -> users.user_id ON DELETE SET NULL)
- `first_name` VARCHAR(50) NOT NULL
- `last_name` VARCHAR(50) NOT NULL
- `specialization` VARCHAR(100) NOT NULL
- `phone` VARCHAR(15) NOT NULL UNIQUE
- `email` VARCHAR(100) NOT NULL UNIQUE
- `qualification` VARCHAR(100) NOT NULL
- `consultation_fee` DECIMAL(10,2) CHECK (consultation_fee >= 0)
- `availability_status` ENUM('AVAILABLE', 'ON_LEAVE', 'BUSY', 'OFF_DUTY')

### 2.4 `appointments`
- `appointment_id` INT AUTO_INCREMENT PRIMARY KEY
- `patient_id` INT NOT NULL (FK -> patients.patient_id ON DELETE CASCADE)
- `doctor_id` INT NOT NULL (FK -> doctors.doctor_id ON DELETE CASCADE)
- `appointment_date` DATE NOT NULL
- `time_slot` VARCHAR(20) NOT NULL
- `status` ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED', 'NO_SHOW')
- `reason` VARCHAR(255) NOT NULL

### 2.5 `hospital_beds`
- `bed_id` INT AUTO_INCREMENT PRIMARY KEY
- `bed_number` VARCHAR(20) NOT NULL UNIQUE
- `ward_name` VARCHAR(50) NOT NULL
- `bed_type` ENUM('ICU', 'GENERAL', 'PRIVATE', 'SEMI_PRIVATE')
- `daily_charge` DECIMAL(10,2) CHECK (daily_charge >= 0)
- `is_occupied` BOOLEAN NOT NULL DEFAULT FALSE

### 2.6 `bed_allocations`
- `allocation_id` INT AUTO_INCREMENT PRIMARY KEY
- `bed_id` INT NOT NULL (FK -> hospital_beds.bed_id ON DELETE RESTRICT)
- `patient_id` INT NOT NULL (FK -> patients.patient_id ON DELETE RESTRICT)
- `allocation_date` DATETIME DEFAULT CURRENT_TIMESTAMP
- `release_date` DATETIME NULL
- `status` ENUM('ACTIVE', 'RELEASED')

### 2.7 `emergency_cases`
- `emergency_id` INT AUTO_INCREMENT PRIMARY KEY
- `patient_id` INT NULL (FK -> patients.patient_id ON DELETE SET NULL)
- `patient_name_temp` VARCHAR(100) NOT NULL
- `triage_priority` ENUM('CRITICAL', 'HIGH', 'MEDIUM', 'LOW')
- `condition_description` TEXT NOT NULL
- `assigned_doctor_id` INT NULL (FK -> doctors.doctor_id ON DELETE SET NULL)
- `bed_id` INT NULL (FK -> hospital_beds.bed_id ON DELETE SET NULL)
- `status` ENUM('ADMITTED', 'TREATED', 'TRANSFERRED', 'CLOSED')

### 2.8 `medicines`
- `medicine_id` INT AUTO_INCREMENT PRIMARY KEY
- `name` VARCHAR(100) NOT NULL UNIQUE
- `category` VARCHAR(50) NOT NULL
- `unit_price` DECIMAL(10,2) CHECK (unit_price >= 0)
- `stock_quantity` INT NOT NULL CHECK (stock_quantity >= 0)
- `reorder_level` INT NOT NULL DEFAULT 10 CHECK (reorder_level >= 0)
- `manufacturer` VARCHAR(100) NOT NULL
- `expiry_date` DATE NOT NULL

### 2.9 `pharmacy_transactions`
- `transaction_id` INT AUTO_INCREMENT PRIMARY KEY
- `medicine_id` INT NOT NULL (FK -> medicines.medicine_id ON DELETE RESTRICT)
- `patient_id` INT NULL (FK -> patients.patient_id ON DELETE SET NULL)
- `transaction_type` ENUM('DISPENSE', 'RESTOCK') NOT NULL
- `quantity` INT NOT NULL CHECK (quantity > 0)
- `total_price` DECIMAL(10,2) CHECK (total_price >= 0)
- `performed_by_user_id` INT NOT NULL (FK -> users.user_id ON DELETE RESTRICT)
