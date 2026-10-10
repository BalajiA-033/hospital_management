-- ============================================================================
-- Smart Hospital Management System - Sample Data Insertion Script
-- DBMS: MySQL 8.x
-- Script 03: Fictional Sample Data for Testing and Demonstration
-- ============================================================================

USE smart_hospital_db;

-- ----------------------------------------------------------------------------
-- 1. USERS
-- Passwords: admin123, doctor123, staff123 (hashed with BCrypt)
-- ----------------------------------------------------------------------------
INSERT INTO users (user_id, username, password_hash, role, full_name, email) VALUES
(1, 'admin', '$2a$10$e8WvB2rY1bH5L9u8K7j6e.7N/Q7J/vW7Z4m6b5a4c3d2e1f0g9h8i', 'ADMIN', 'System Administrator', 'admin@smarthospital.org'),
(2, 'dr_john', '$2a$10$k8WvB2rY1bH5L9u8K7j6e.7N/Q7J/vW7Z4m6b5a4c3d2e1f0g9h8i', 'DOCTOR', 'Dr. John Watson', 'john.watson@smarthospital.org'),
(3, 'staff_mary', '$2a$10$m8WvB2rY1bH5L9u8K7j6e.7N/Q7J/vW7Z4m6b5a4c3d2e1f0g9h8i', 'STAFF', 'Mary Jenkins', 'mary.jenkins@smarthospital.org');

-- ----------------------------------------------------------------------------
-- 2. PATIENTS
-- ----------------------------------------------------------------------------
INSERT INTO patients (patient_id, first_name, last_name, gender, dob, phone, email, address, blood_group, emergency_contact) VALUES
(1, 'John', 'Doe', 'MALE', '1985-04-12', '9876543210', 'john.doe@example.com', '123 Maple Street, Cityville', 'O+', '9876543211'),
(2, 'Jane', 'Smith', 'FEMALE', '1992-08-25', '9876543220', 'jane.smith@example.com', '456 Oak Avenue, Metropolis', 'A+', '9876543221'),
(3, 'Robert', 'Brown', 'MALE', '1978-11-03', '9876543230', 'robert.brown@example.com', '789 Pine Road, Gotham', 'B-', '9876543231'),
(4, 'Emily', 'Davis', 'FEMALE', '1999-01-18', '9876543240', 'emily.davis@example.com', '101 Elm Boulevard, Star City', 'AB+', '9876543241'),
(5, 'Michael', 'Wilson', 'MALE', '1965-06-30', '9876543250', 'michael.wilson@example.com', '202 Birch Lane, Central City', 'O-', '9876543251'),
(6, 'Sarah', 'Taylor', 'FEMALE', '2001-09-14', '9876543260', 'sarah.taylor@example.com', '303 Cedar Court, Coast City', 'A-', '9876543261');

-- ----------------------------------------------------------------------------
-- 3. DOCTORS
-- ----------------------------------------------------------------------------
INSERT INTO doctors (doctor_id, user_id, first_name, last_name, specialization, phone, email, qualification, consultation_fee, availability_status) VALUES
(1, 2, 'Alice', 'Walker', 'Cardiology', '9123456781', 'alice.walker@smarthospital.org', 'MD, DM (Cardiology)', 800.00, 'AVAILABLE'),
(2, NULL, 'Bob', 'Martinez', 'Neurology', '9123456782', 'bob.martinez@smarthospital.org', 'MD, MCh (Neurosurgery)', 1000.00, 'AVAILABLE'),
(3, NULL, 'Carol', 'Jenkins', 'Pediatrics', '9123456783', 'carol.jenkins@smarthospital.org', 'MD (Pediatrics)', 600.00, 'ON_LEAVE'),
(4, NULL, 'David', 'Miller', 'Orthopedics', '9123456784', 'david.miller@smarthospital.org', 'MS (Orthopedics)', 750.00, 'AVAILABLE');

-- ----------------------------------------------------------------------------
-- 4. HOSPITAL BEDS
-- ----------------------------------------------------------------------------
INSERT INTO hospital_beds (bed_id, bed_number, ward_name, bed_type, daily_charge, is_occupied) VALUES
(1, 'ICU-101', 'Critical Care Ward', 'ICU', 3500.00, TRUE),
(2, 'ICU-102', 'Critical Care Ward', 'ICU', 3500.00, FALSE),
(3, 'GEN-201', 'General Ward A', 'GENERAL', 800.00, TRUE),
(4, 'GEN-202', 'General Ward A', 'GENERAL', 800.00, FALSE),
(5, 'PRIV-301', 'Deluxe Private Wing', 'PRIVATE', 2200.00, TRUE),
(6, 'PRIV-302', 'Deluxe Private Wing', 'PRIVATE', 2200.00, FALSE),
(7, 'SEMI-401', 'Semi-Private Wing', 'SEMI_PRIVATE', 1400.00, FALSE),
(8, 'SEMI-402', 'Semi-Private Wing', 'SEMI_PRIVATE', 1400.00, FALSE);

-- ----------------------------------------------------------------------------
-- 5. BED ALLOCATIONS
-- ----------------------------------------------------------------------------
INSERT INTO bed_allocations (allocation_id, bed_id, patient_id, allocation_date, release_date, status, notes) VALUES
(1, 1, 1, DATE_SUB(NOW(), INTERVAL 3 DAY), NULL, 'ACTIVE', 'Patient admitted to ICU for post-cardiac surgery monitoring'),
(2, 3, 3, DATE_SUB(NOW(), INTERVAL 2 DAY), NULL, 'ACTIVE', 'General admission for severe gastroenteritis'),
(3, 5, 2, DATE_SUB(NOW(), INTERVAL 5 DAY), NULL, 'ACTIVE', 'Post-operative recovery in private suite');

-- ----------------------------------------------------------------------------
-- 6. APPOINTMENTS
-- ----------------------------------------------------------------------------
INSERT INTO appointments (appointment_id, patient_id, doctor_id, appointment_date, time_slot, status, reason, notes) VALUES
(1, 1, 1, CURDATE(), '09:30 AM', 'COMPLETED', 'Routine Cardiac Follow-up', 'ECG normal, blood pressure stable'),
(2, 2, 1, CURDATE(), '11:00 AM', 'SCHEDULED', 'Chest tightness evaluation', 'Scheduled for stress ECG'),
(3, 4, 2, CURDATE(), '02:00 PM', 'SCHEDULED', 'Chronic migraine consultation', 'Patient requested neuro-assessment'),
(4, 5, 4, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00 AM', 'SCHEDULED', 'Knee joint pain consultation', 'Initial assessment'),
(5, 6, 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '11:30 AM', 'CANCELLED', 'Pediatric checkup', 'Doctor on leave'),
(6, 3, 4, DATE_SUB(CURDATE(), INTERVAL 1 DAY), '04:00 PM', 'COMPLETED', 'Ankle sprain follow-up', 'X-ray confirmed clear fracture recovery');

-- ----------------------------------------------------------------------------
-- 7. EMERGENCY CASES
-- ----------------------------------------------------------------------------
INSERT INTO emergency_cases (emergency_id, patient_id, patient_name_temp, triage_priority, condition_description, assigned_doctor_id, bed_id, status, admission_time) VALUES
(1, 1, 'John Doe', 'CRITICAL', 'Acute Myocardial Infarction', 1, 1, 'ADMITTED', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(2, NULL, 'Unknown Male (Jane Doe Contact)', 'HIGH', 'Motor Vehicle Trauma - Fractured Femur', 4, 3, 'ADMITTED', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 4, 'Emily Davis', 'MEDIUM', 'Acute Severe Asthma Attack', 1, NULL, 'TREATED', DATE_SUB(NOW(), INTERVAL 1 DAY));

-- ----------------------------------------------------------------------------
-- 8. MEDICINES (Pharmacy Inventory)
-- ----------------------------------------------------------------------------
INSERT INTO medicines (medicine_id, name, category, unit_price, stock_quantity, reorder_level, manufacturer, expiry_date) VALUES
(1, 'Paracetamol 500mg', 'Analgesic', 5.00, 450, 50, 'PharmaCorp', '2027-12-31'),
(2, 'Amoxicillin 250mg', 'Antibiotic', 12.50, 120, 30, 'BioHealth Ltd', '2026-11-30'),
(3, 'Ibuprofen 400mg', 'Anti-inflammatory', 8.00, 15, 25, 'MediPlus', '2027-06-30'),
(4, 'Insulin Glargine 100IU', 'Antidiabetic', 450.00, 8, 15, 'NovoCare', '2026-08-15'),
(5, 'Atorvastatin 20mg', 'Cardiovascular', 18.00, 200, 40, 'HeartHealth Pharma', '2028-03-31'),
(6, 'Cetirizine 10mg', 'Antihistamine', 4.50, 5, 20, 'ClearAir Labs', '2027-01-15');

-- ----------------------------------------------------------------------------
-- 9. PHARMACY TRANSACTIONS
-- ----------------------------------------------------------------------------
INSERT INTO pharmacy_transactions (transaction_id, medicine_id, patient_id, transaction_type, quantity, total_price, transaction_date, performed_by_user_id, notes) VALUES
(1, 1, 1, 'DISPENSE', 10, 50.00, DATE_SUB(NOW(), INTERVAL 2 DAY), 3, 'Dispensed for inpatient recovery'),
(2, 2, 2, 'DISPENSE', 15, 187.50, DATE_SUB(NOW(), INTERVAL 1 DAY), 3, 'Post-op antibiotic prescription'),
(3, 4, NULL, 'RESTOCK', 20, 9000.00, DATE_SUB(NOW(), INTERVAL 5 DAY), 1, 'Bulk monthly restock from NovoCare supplier'),
(4, 3, 3, 'DISPENSE', 10, 80.00, DATE_SUB(NOW(), INTERVAL 1 DAY), 3, 'Pain management prescription'),
(5, 5, 1, 'DISPENSE', 30, 540.00, NOW(), 3, '30-day lipid management refill');
