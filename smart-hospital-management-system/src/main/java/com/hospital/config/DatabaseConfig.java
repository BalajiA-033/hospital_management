package com.hospital.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles database connectivity for MySQL 8.x with automatic fallback to H2 embedded database
 * for testing and offline execution environments.
 */
public class DatabaseConfig {

    private static String dbHost = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
    private static String dbPort = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "3306";
    private static String dbName = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "smart_hospital_db";
    private static String dbUser = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
    private static String dbPassword = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "";

    private static boolean useH2Fallback = false;
    private static final String H2_URL = "jdbc:h2:mem:smart_hospital_db;DB_CLOSE_DELAY=-1;MODE=MySQL";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Connector/J Driver not found on classpath. Will attempt H2 driver fallback if needed.");
        }
    }

    public static void setCredentials(String host, String port, String name, String user, String password) {
        dbHost = host;
        dbPort = port;
        dbName = name;
        dbUser = user;
        dbPassword = password;
        useH2Fallback = false;
    }

    public static void enableH2Fallback() {
        useH2Fallback = true;
    }

    public static boolean isH2Fallback() {
        return useH2Fallback;
    }

    public static Connection getConnection() throws SQLException {
        if (useH2Fallback) {
            return DriverManager.getConnection(H2_URL, "sa", "");
        }

        String jdbcUrl = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                dbHost, dbPort, dbName);
        try {
            return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            // Fallback to H2 in-memory mode if MySQL is not running locally during development/demo
            System.err.println("Failed to connect to MySQL database at " + jdbcUrl + " (" + e.getMessage() + ").");
            System.err.println("Switching automatically to H2 In-Memory database mode for application stability.");
            useH2Fallback = true;
            Connection conn = DriverManager.getConnection(H2_URL, "sa", "");
            initializeH2Schema(conn);
            return conn;
        }
    }

    /**
     * Initializes schema and sample data in H2 fallback mode so application is immediately usable without MySQL installed.
     */
    public static synchronized void initializeH2Schema(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users ("
                    + "user_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "username VARCHAR(50) NOT NULL UNIQUE, "
                    + "password_hash VARCHAR(255) NOT NULL, "
                    + "role VARCHAR(20) NOT NULL DEFAULT 'STAFF', "
                    + "full_name VARCHAR(100) NOT NULL, "
                    + "email VARCHAR(100) NOT NULL UNIQUE);");

            stmt.execute("CREATE TABLE IF NOT EXISTS patients ("
                    + "patient_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "first_name VARCHAR(50) NOT NULL, "
                    + "last_name VARCHAR(50) NOT NULL, "
                    + "gender VARCHAR(10) NOT NULL, "
                    + "dob DATE NOT NULL, "
                    + "phone VARCHAR(15) NOT NULL UNIQUE, "
                    + "email VARCHAR(100), "
                    + "address TEXT, "
                    + "blood_group VARCHAR(5) NOT NULL, "
                    + "emergency_contact VARCHAR(15) NOT NULL);");

            stmt.execute("CREATE TABLE IF NOT EXISTS doctors ("
                    + "doctor_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "user_id INT, "
                    + "first_name VARCHAR(50) NOT NULL, "
                    + "last_name VARCHAR(50) NOT NULL, "
                    + "specialization VARCHAR(100) NOT NULL, "
                    + "phone VARCHAR(15) NOT NULL UNIQUE, "
                    + "email VARCHAR(100) NOT NULL UNIQUE, "
                    + "qualification VARCHAR(100) NOT NULL, "
                    + "consultation_fee DECIMAL(10, 2) NOT NULL DEFAULT 500.00, "
                    + "availability_status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE');");

            stmt.execute("CREATE TABLE IF NOT EXISTS hospital_beds ("
                    + "bed_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "bed_number VARCHAR(20) NOT NULL UNIQUE, "
                    + "ward_name VARCHAR(50) NOT NULL, "
                    + "bed_type VARCHAR(20) NOT NULL DEFAULT 'GENERAL', "
                    + "daily_charge DECIMAL(10, 2) NOT NULL, "
                    + "is_occupied BOOLEAN NOT NULL DEFAULT FALSE);");

            stmt.execute("CREATE TABLE IF NOT EXISTS bed_allocations ("
                    + "allocation_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "bed_id INT NOT NULL, "
                    + "patient_id INT NOT NULL, "
                    + "allocation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "release_date TIMESTAMP NULL, "
                    + "status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', "
                    + "notes TEXT);");

            stmt.execute("CREATE TABLE IF NOT EXISTS appointments ("
                    + "appointment_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "patient_id INT NOT NULL, "
                    + "doctor_id INT NOT NULL, "
                    + "appointment_date DATE NOT NULL, "
                    + "time_slot VARCHAR(20) NOT NULL, "
                    + "status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED', "
                    + "reason VARCHAR(255) NOT NULL, "
                    + "notes TEXT);");

            stmt.execute("CREATE TABLE IF NOT EXISTS emergency_cases ("
                    + "emergency_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "patient_id INT, "
                    + "patient_name_temp VARCHAR(100) NOT NULL, "
                    + "triage_priority VARCHAR(20) NOT NULL DEFAULT 'HIGH', "
                    + "condition_description TEXT NOT NULL, "
                    + "assigned_doctor_id INT, "
                    + "bed_id INT, "
                    + "status VARCHAR(20) NOT NULL DEFAULT 'ADMITTED', "
                    + "admission_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");

            stmt.execute("CREATE TABLE IF NOT EXISTS medicines ("
                    + "medicine_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(100) NOT NULL UNIQUE, "
                    + "category VARCHAR(50) NOT NULL, "
                    + "unit_price DECIMAL(10, 2) NOT NULL, "
                    + "stock_quantity INT NOT NULL, "
                    + "reorder_level INT NOT NULL DEFAULT 10, "
                    + "manufacturer VARCHAR(100) NOT NULL, "
                    + "expiry_date DATE NOT NULL);");

            stmt.execute("CREATE TABLE IF NOT EXISTS pharmacy_transactions ("
                    + "transaction_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "medicine_id INT NOT NULL, "
                    + "patient_id INT, "
                    + "transaction_type VARCHAR(20) NOT NULL, "
                    + "quantity INT NOT NULL, "
                    + "total_price DECIMAL(10, 2) NOT NULL, "
                    + "transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "performed_by_user_id INT NOT NULL, "
                    + "notes TEXT);");

            // Seed initial admin user if empty
            stmt.execute("MERGE INTO users (user_id, username, password_hash, role, full_name, email) KEY(username) VALUES "
                    + "(1, 'admin', '$2a$10$e8WvB2rY1bH5L9u8K7j6e.7N/Q7J/vW7Z4m6b5a4c3d2e1f0g9h8i', 'ADMIN', 'System Administrator', 'admin@smarthospital.org');");
            stmt.execute("MERGE INTO users (user_id, username, password_hash, role, full_name, email) KEY(username) VALUES "
                    + "(2, 'dr_john', '$2a$10$k8WvB2rY1bH5L9u8K7j6e.7N/Q7J/vW7Z4m6b5a4c3d2e1f0g9h8i', 'DOCTOR', 'Dr. John Watson', 'john.watson@smarthospital.org');");

            // Seed sample beds if empty
            stmt.execute("MERGE INTO hospital_beds (bed_id, bed_number, ward_name, bed_type, daily_charge, is_occupied) KEY(bed_number) VALUES "
                    + "(1, 'ICU-101', 'Critical Care Ward', 'ICU', 3500.00, FALSE);");
            stmt.execute("MERGE INTO hospital_beds (bed_id, bed_number, ward_name, bed_type, daily_charge, is_occupied) KEY(bed_number) VALUES "
                    + "(2, 'GEN-201', 'General Ward A', 'GENERAL', 800.00, FALSE);");

            // Seed sample medicines if empty
            stmt.execute("MERGE INTO medicines (medicine_id, name, category, unit_price, stock_quantity, reorder_level, manufacturer, expiry_date) KEY(name) VALUES "
                    + "(1, 'Paracetamol 500mg', 'Analgesic', 5.00, 250, 50, 'PharmaCorp', '2027-12-31');");
            stmt.execute("MERGE INTO medicines (medicine_id, name, category, unit_price, stock_quantity, reorder_level, manufacturer, expiry_date) KEY(name) VALUES "
                    + "(2, 'Insulin Glargine', 'Antidiabetic', 450.00, 8, 15, 'NovoCare', '2026-08-15');");

        } catch (SQLException ex) {
            System.err.println("Error initializing H2 in-memory schema: " + ex.getMessage());
        }
    }
}
