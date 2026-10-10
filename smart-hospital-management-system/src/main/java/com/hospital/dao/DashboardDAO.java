package com.hospital.dao;

import com.hospital.config.DatabaseConfig;
import com.hospital.model.DashboardStats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDAO {

    public DashboardStats getDashboardStats() throws SQLException {
        DashboardStats stats = new DashboardStats();

        try (Connection conn = DatabaseConfig.getConnection()) {

            // 1. Total Patients
            try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM patients");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) stats.setTotalPatients(rs.getInt(1));
            }

            // 2. Total Doctors
            try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM doctors");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) stats.setTotalDoctors(rs.getInt(1));
            }

            // 3. Today's Appointments
            try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM appointments WHERE appointment_date = CURRENT_DATE()");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) stats.setTodaysAppointments(rs.getInt(1));
            }

            // 4. Bed Occupancy Counts
            try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(CASE WHEN is_occupied = FALSE THEN 1 ELSE 0 END) AS available_count, SUM(CASE WHEN is_occupied = TRUE THEN 1 ELSE 0 END) AS occupied_count FROM hospital_beds");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stats.setAvailableBeds(rs.getInt("available_count"));
                    stats.setOccupiedBeds(rs.getInt("occupied_count"));
                }
            }

            // 5. Low-Stock Medicines
            try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM medicines WHERE stock_quantity <= reorder_level");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) stats.setLowStockMedicines(rs.getInt(1));
            }
        }

        return stats;
    }
}
