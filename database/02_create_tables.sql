-- ============================================================================
-- Smart Hospital Management System - Table Schema Script
-- DBMS: MySQL 8.x
-- Script 02: Table Schema Definition with Integrity Constraints and Indexes
-- ============================================================================

USE smart_hospital_db;

-- Disable Foreign Key checks temporarily during DDL setup
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS pharmacy_transactions;
DROP TABLE IF EXISTS medicines;
DROP TABLE IF EXISTS emergency_cases;
DROP TABLE IF EXISTS bed_allocations;
DROP TABLE IF EXISTS hospital_beds;
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS doctors;
DROP TABLE IF EXISTS patients;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------------------------------------------------------
-- 1. USERS TABLE (Authentication & Role-Based Access Control)
-- ----------------------------------------------------------------------------
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'DOCTOR', 'STAFF') NOT NULL DEFAULT 'STAFF',
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_username (username),
    INDEX idx_user_role (role)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 2. PATIENTS TABLE (Patient Registration and Demographic Records)
-- ----------------------------------------------------------------------------
CREATE TABLE patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    dob DATE NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    email VARCHAR(100) UNIQUE,
    address TEXT,
    blood_group VARCHAR(5) NOT NULL,
    emergency_contact VARCHAR(15) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_patient_name (last_name, first_name),
    INDEX idx_patient_phone (phone)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 3. DOCTORS TABLE (Doctor Profile and Availability Records)
-- ----------------------------------------------------------------------------
CREATE TABLE doctors (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    qualification VARCHAR(100) NOT NULL,
    consultation_fee DECIMAL(10, 2) NOT NULL DEFAULT 500.00 CHECK (consultation_fee >= 0),
    availability_status ENUM('AVAILABLE', 'ON_LEAVE', 'BUSY', 'OFF_DUTY') NOT NULL DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_doctor_specialization (specialization),
    INDEX idx_doctor_status (availability_status)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 4. APPOINTMENTS TABLE (Booking and Doctor Availability Tracking)
-- ----------------------------------------------------------------------------
CREATE TABLE appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    time_slot VARCHAR(20) NOT NULL,
    status ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED', 'NO_SHOW') NOT NULL DEFAULT 'SCHEDULED',
    reason VARCHAR(255) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_appt_date_doc (doctor_id, appointment_date),
    INDEX idx_appt_patient (patient_id),
    INDEX idx_appt_status (status)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 5. HOSPITAL_BEDS TABLE (Bed Inventory & Status Tracking)
-- ----------------------------------------------------------------------------
CREATE TABLE hospital_beds (
    bed_id INT AUTO_INCREMENT PRIMARY KEY,
    bed_number VARCHAR(20) NOT NULL UNIQUE,
    ward_name VARCHAR(50) NOT NULL,
    bed_type ENUM('ICU', 'GENERAL', 'PRIVATE', 'SEMI_PRIVATE') NOT NULL DEFAULT 'GENERAL',
    daily_charge DECIMAL(10, 2) NOT NULL CHECK (daily_charge >= 0),
    is_occupied BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_bed_occupied (is_occupied),
    INDEX idx_bed_ward (ward_name)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 6. BED_ALLOCATIONS TABLE (Patient Bed Occupancy Records & History)
-- ----------------------------------------------------------------------------
CREATE TABLE bed_allocations (
    allocation_id INT AUTO_INCREMENT PRIMARY KEY,
    bed_id INT NOT NULL,
    patient_id INT NOT NULL,
    allocation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    release_date DATETIME NULL,
    status ENUM('ACTIVE', 'RELEASED') NOT NULL DEFAULT 'ACTIVE',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (bed_id) REFERENCES hospital_beds(bed_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_alloc_bed_status (bed_id, status),
    INDEX idx_alloc_patient (patient_id)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 7. EMERGENCY_CASES TABLE (Emergency & Triage Case Management)
-- ----------------------------------------------------------------------------
CREATE TABLE emergency_cases (
    emergency_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NULL,
    patient_name_temp VARCHAR(100) NOT NULL,
    triage_priority ENUM('CRITICAL', 'HIGH', 'MEDIUM', 'LOW') NOT NULL DEFAULT 'HIGH',
    condition_description TEXT NOT NULL,
    assigned_doctor_id INT NULL,
    bed_id INT NULL,
    status ENUM('ADMITTED', 'TREATED', 'TRANSFERRED', 'CLOSED') NOT NULL DEFAULT 'ADMITTED',
    admission_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (assigned_doctor_id) REFERENCES doctors(doctor_id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (bed_id) REFERENCES hospital_beds(bed_id) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_emerg_priority (triage_priority),
    INDEX idx_emerg_status (status)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 8. MEDICINES TABLE (Pharmacy Inventory and Reorder Tracking)
-- ----------------------------------------------------------------------------
CREATE TABLE medicines (
    medicine_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL CHECK (unit_price >= 0),
    stock_quantity INT NOT NULL CHECK (stock_quantity >= 0),
    reorder_level INT NOT NULL DEFAULT 10 CHECK (reorder_level >= 0),
    manufacturer VARCHAR(100) NOT NULL,
    expiry_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_medicine_name (name),
    INDEX idx_medicine_stock (stock_quantity, reorder_level)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 9. PHARMACY_TRANSACTIONS TABLE (Dispense & Restock Transactions)
-- ----------------------------------------------------------------------------
CREATE TABLE pharmacy_transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    medicine_id INT NOT NULL,
    patient_id INT NULL,
    transaction_type ENUM('DISPENSE', 'RESTOCK') NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    total_price DECIMAL(10, 2) NOT NULL CHECK (total_price >= 0),
    transaction_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    performed_by_user_id INT NOT NULL,
    notes TEXT,
    FOREIGN KEY (medicine_id) REFERENCES medicines(medicine_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (performed_by_user_id) REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_pharm_trans_med (medicine_id),
    INDEX idx_pharm_trans_date (transaction_date)
) ENGINE=InnoDB;
