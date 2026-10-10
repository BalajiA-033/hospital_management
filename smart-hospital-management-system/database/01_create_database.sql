-- ============================================================================
-- Smart Hospital Management System - Database Creation Script
-- Experiment Title: Design and Development of a Real-Time Database-Driven Application
-- DBMS: MySQL 8.x
-- Script 01: Create Database and User Privileges
-- ============================================================================

-- Drop database if exists (Use with caution in production environment)
DROP DATABASE IF EXISTS smart_hospital_db;

-- Create Database with UTF-8 support for unicode character handling
CREATE DATABASE smart_hospital_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE smart_hospital_db;

-- Display creation confirmation
SELECT 'Database smart_hospital_db created successfully!' AS status_message;

-- Optional: Create dedicated application user and grant permissions
-- CREATE USER IF NOT EXISTS 'hospital_user'@'localhost' IDENTIFIED BY 'Hospital@2026';
-- GRANT ALL PRIVILEGES ON smart_hospital_db.* TO 'hospital_user'@'localhost';
-- FLUSH PRIVILEGES;
