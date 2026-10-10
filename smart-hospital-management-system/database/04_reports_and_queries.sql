-- ============================================================================
-- Smart Hospital Management System - SQL Queries and Analytical Reports
-- DBMS: MySQL 8.x
-- Script 04: Practical DBMS Analytical Queries & CRUD Demonstrations
-- ============================================================================

USE smart_hospital_db;

-- ----------------------------------------------------------------------------
-- 1. BASIC CRUD OPERATIONS DEMONSTRATION
-- ----------------------------------------------------------------------------

-- 1.1 SELECT with WHERE and ORDER BY
SELECT patient_id, first_name, last_name, blood_group, phone
FROM patients
WHERE blood_group IN ('O+', 'O-', 'A+')
ORDER BY last_name ASC, first_name ASC;

-- 1.2 INSERT a new patient record
INSERT INTO patients (first_name, last_name, gender, dob, phone, email, address, blood_group, emergency_contact)
VALUES ('David', 'Miller', 'MALE', '1990-05-20', '9876543999', 'david.m@example.com', '555 Pine St', 'B+', '9876543998');

-- 1.3 UPDATE doctor consultation fee and availability status
UPDATE doctors
SET consultation_fee = 850.00, availability_status = 'AVAILABLE'
WHERE doctor_id = 1;

-- 1.4 DELETE cancelled appointments older than 30 days (Demonstration structure)
-- DELETE FROM appointments WHERE status = 'CANCELLED' AND appointment_date < DATE_SUB(CURDATE(), INTERVAL 30 DAY);


-- ----------------------------------------------------------------------------
-- 2. JOIN QUERIES (INNER JOIN & LEFT JOIN)
-- ----------------------------------------------------------------------------

-- 2.1 INNER JOIN: Appointment Schedule with Patient & Doctor Details
SELECT 
    a.appointment_id,
    a.appointment_date,
    a.time_slot,
    CONCAT(p.first_name, ' ', p.last_name) AS patient_name,
    p.phone AS patient_phone,
    CONCAT('Dr. ', d.first_name, ' ', d.last_name) AS doctor_name,
    d.specialization,
    a.status
FROM appointments a
INNER JOIN patients p ON a.patient_id = p.patient_id
INNER JOIN doctors d ON a.doctor_id = d.doctor_id
ORDER BY a.appointment_date DESC, a.time_slot ASC;

-- 2.2 LEFT JOIN: Hospital Bed Occupancy & Current Patient Details
SELECT 
    b.bed_id,
    b.bed_number,
    b.ward_name,
    b.bed_type,
    b.daily_charge,
    b.is_occupied,
    ba.allocation_date,
    CONCAT(p.first_name, ' ', p.last_name) AS occupied_by_patient,
    p.blood_group
FROM hospital_beds b
LEFT JOIN bed_allocations ba ON b.bed_id = ba.bed_id AND ba.status = 'ACTIVE'
LEFT JOIN patients p ON ba.patient_id = p.patient_id
ORDER BY b.ward_name, b.bed_number;

-- 2.3 LEFT JOIN: Emergency Cases with Doctor & Allocated Bed
SELECT 
    e.emergency_id,
    COALESCE(CONCAT(p.first_name, ' ', p.last_name), e.patient_name_temp) AS patient_name,
    e.triage_priority,
    e.condition_description,
    e.status,
    CONCAT('Dr. ', d.first_name, ' ', d.last_name) AS assigned_doctor,
    b.bed_number AS allocated_bed
FROM emergency_cases e
LEFT JOIN patients p ON e.patient_id = p.patient_id
LEFT JOIN doctors d ON e.assigned_doctor_id = d.doctor_id
LEFT JOIN hospital_beds b ON e.bed_id = b.bed_id
ORDER BY FIELD(e.triage_priority, 'CRITICAL', 'HIGH', 'MEDIUM', 'LOW'), e.admission_time DESC;


-- ----------------------------------------------------------------------------
-- 3. AGGREGATE QUERIES & GROUP BY / HAVING
-- ----------------------------------------------------------------------------

-- 3.1 Doctor Appointment Workload Summary
SELECT 
    d.doctor_id,
    CONCAT('Dr. ', d.first_name, ' ', d.last_name) AS doctor_name,
    d.specialization,
    COUNT(a.appointment_id) AS total_appointments,
    SUM(CASE WHEN a.status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed_appointments,
    SUM(CASE WHEN a.status = 'SCHEDULED' THEN 1 ELSE 0 END) AS scheduled_appointments
FROM doctors d
LEFT JOIN appointments a ON d.doctor_id = a.doctor_id
GROUP BY d.doctor_id, doctor_name, d.specialization
HAVING COUNT(a.appointment_id) >= 0
ORDER BY total_appointments DESC;

-- 3.2 Hospital Bed Utilization Breakdown by Ward
SELECT 
    ward_name,
    COUNT(*) AS total_beds,
    SUM(CASE WHEN is_occupied = TRUE THEN 1 ELSE 0 END) AS occupied_beds,
    SUM(CASE WHEN is_occupied = FALSE THEN 1 ELSE 0 END) AS available_beds,
    ROUND((SUM(CASE WHEN is_occupied = TRUE THEN 1 ELSE 0 END) * 100.0 / COUNT(*)), 2) AS occupancy_rate_percent
FROM hospital_beds
GROUP BY ward_name;


-- ----------------------------------------------------------------------------
-- 4. SUBQUERIES & ADVANCED REPORTS
-- ----------------------------------------------------------------------------

-- 4.1 Low Stock Medicine Report (Stock <= Reorder Level)
SELECT 
    medicine_id,
    name,
    category,
    stock_quantity,
    reorder_level,
    unit_price,
    manufacturer
FROM medicines
WHERE stock_quantity <= reorder_level
ORDER BY stock_quantity ASC;

-- 4.2 Patients with Total Pharmacy Revenue higher than the Average Dispense Transaction
SELECT 
    p.patient_id,
    CONCAT(p.first_name, ' ', p.last_name) AS patient_name,
    SUM(pt.total_price) AS total_pharmacy_spent
FROM patients p
JOIN pharmacy_transactions pt ON p.patient_id = pt.patient_id
WHERE pt.transaction_type = 'DISPENSE'
GROUP BY p.patient_id, patient_name
HAVING SUM(pt.total_price) > (
    SELECT AVG(total_price) 
    FROM pharmacy_transactions 
    WHERE transaction_type = 'DISPENSE'
)
ORDER BY total_pharmacy_spent DESC;

-- 4.3 Total Pharmacy Revenue and Dispensed Quantity Summary by Medicine
SELECT 
    m.medicine_id,
    m.name AS medicine_name,
    m.category,
    COALESCE(SUM(CASE WHEN pt.transaction_type = 'DISPENSE' THEN pt.quantity ELSE 0 END), 0) AS total_quantity_dispensed,
    COALESCE(SUM(CASE WHEN pt.transaction_type = 'DISPENSE' THEN pt.total_price ELSE 0 END), 0.00) AS total_revenue
FROM medicines m
LEFT JOIN pharmacy_transactions pt ON m.medicine_id = pt.medicine_id
GROUP BY m.medicine_id, m.name, m.category
ORDER BY total_revenue DESC;
