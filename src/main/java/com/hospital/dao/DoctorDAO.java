package com.hospital.dao;

import com.hospital.config.DatabaseConfig;
import com.hospital.model.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    public List<Doctor> findAll() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT doctor_id, user_id, first_name, last_name, specialization, phone, email, qualification, consultation_fee, availability_status, created_at FROM doctors ORDER BY doctor_id ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                doctors.add(mapResultSetToDoctor(rs));
            }
        }
        return doctors;
    }

    public List<Doctor> search(String query) throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT doctor_id, user_id, first_name, last_name, specialization, phone, email, qualification, consultation_fee, availability_status, created_at FROM doctors "
                   + "WHERE first_name LIKE ? OR last_name LIKE ? OR specialization LIKE ? ORDER BY doctor_id ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchTerm = "%" + query + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            stmt.setString(3, searchTerm);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    doctors.add(mapResultSetToDoctor(rs));
                }
            }
        }
        return doctors;
    }

    public Doctor findById(int id) throws SQLException {
        String sql = "SELECT doctor_id, user_id, first_name, last_name, specialization, phone, email, qualification, consultation_fee, availability_status, created_at FROM doctors WHERE doctor_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDoctor(rs);
                }
            }
        }
        return null;
    }

    public boolean save(Doctor doctor) throws SQLException {
        String sql = "INSERT INTO doctors (user_id, first_name, last_name, specialization, phone, email, qualification, consultation_fee, availability_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (doctor.getUserId() != null) {
                stmt.setInt(1, doctor.getUserId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, doctor.getFirstName());
            stmt.setString(3, doctor.getLastName());
            stmt.setString(4, doctor.getSpecialization());
            stmt.setString(5, doctor.getPhone());
            stmt.setString(6, doctor.getEmail());
            stmt.setString(7, doctor.getQualification());
            stmt.setDouble(8, doctor.getConsultationFee());
            stmt.setString(9, doctor.getAvailabilityStatus());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        doctor.setDoctorId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean update(Doctor doctor) throws SQLException {
        String sql = "UPDATE doctors SET first_name = ?, last_name = ?, specialization = ?, phone = ?, email = ?, qualification = ?, consultation_fee = ?, availability_status = ? WHERE doctor_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctor.getFirstName());
            stmt.setString(2, doctor.getLastName());
            stmt.setString(3, doctor.getSpecialization());
            stmt.setString(4, doctor.getPhone());
            stmt.setString(5, doctor.getEmail());
            stmt.setString(6, doctor.getQualification());
            stmt.setDouble(7, doctor.getConsultationFee());
            stmt.setString(8, doctor.getAvailabilityStatus());
            stmt.setInt(9, doctor.getDoctorId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateAvailability(int doctorId, String status) throws SQLException {
        String sql = "UPDATE doctors SET availability_status = ? WHERE doctor_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, doctorId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Doctor mapResultSetToDoctor(ResultSet rs) throws SQLException {
        Doctor d = new Doctor();
        d.setDoctorId(rs.getInt("doctor_id"));
        int uId = rs.getInt("user_id");
        d.setUserId(rs.wasNull() ? null : uId);
        d.setFirstName(rs.getString("first_name"));
        d.setLastName(rs.getString("last_name"));
        d.setSpecialization(rs.getString("specialization"));
        d.setPhone(rs.getString("phone"));
        d.setEmail(rs.getString("email"));
        d.setQualification(rs.getString("qualification"));
        d.setConsultationFee(rs.getDouble("consultation_fee"));
        d.setAvailabilityStatus(rs.getString("availability_status"));
        d.setCreatedAt(rs.getTimestamp("created_at"));
        return d;
    }
}
