package com.hospital.dao;

import com.hospital.config.DatabaseConfig;
import com.hospital.model.EmergencyCase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmergencyDAO {

    public List<EmergencyCase> findAll() throws SQLException {
        List<EmergencyCase> cases = new ArrayList<>();
        String sql = "SELECT e.emergency_id, e.patient_id, e.patient_name_temp, e.triage_priority, e.condition_description, "
                   + "e.assigned_doctor_id, e.bed_id, e.status, e.admission_time, "
                   + "CONCAT('Dr. ', d.first_name, ' ', d.last_name) AS doctor_name, "
                   + "b.bed_number "
                   + "FROM emergency_cases e "
                   + "LEFT JOIN doctors d ON e.assigned_doctor_id = d.doctor_id "
                   + "LEFT JOIN hospital_beds b ON e.bed_id = b.bed_id "
                   + "ORDER BY FIELD(e.triage_priority, 'CRITICAL', 'HIGH', 'MEDIUM', 'LOW'), e.admission_time DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cases.add(mapResultSetToEmergencyCase(rs));
            }
        }
        return cases;
    }

    public boolean save(EmergencyCase emergencyCase) throws SQLException {
        String sql = "INSERT INTO emergency_cases (patient_id, patient_name_temp, triage_priority, condition_description, assigned_doctor_id, bed_id, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (emergencyCase.getPatientId() != null) {
                stmt.setInt(1, emergencyCase.getPatientId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, emergencyCase.getPatientNameTemp());
            stmt.setString(3, emergencyCase.getTriagePriority());
            stmt.setString(4, emergencyCase.getConditionDescription());
            if (emergencyCase.getAssignedDoctorId() != null) {
                stmt.setInt(5, emergencyCase.getAssignedDoctorId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            if (emergencyCase.getBedId() != null) {
                stmt.setInt(6, emergencyCase.getBedId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.setString(7, emergencyCase.getStatus() != null ? emergencyCase.getStatus() : "ADMITTED");

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        emergencyCase.setEmergencyId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean updateStatus(int emergencyId, String status) throws SQLException {
        String sql = "UPDATE emergency_cases SET status = ? WHERE emergency_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, emergencyId);
            return stmt.executeUpdate() > 0;
        }
    }

    private EmergencyCase mapResultSetToEmergencyCase(ResultSet rs) throws SQLException {
        EmergencyCase ec = new EmergencyCase();
        ec.setEmergencyId(rs.getInt("emergency_id"));
        int pId = rs.getInt("patient_id");
        ec.setPatientId(rs.wasNull() ? null : pId);
        ec.setPatientNameTemp(rs.getString("patient_name_temp"));
        ec.setTriagePriority(rs.getString("triage_priority"));
        ec.setConditionDescription(rs.getString("condition_description"));
        int dId = rs.getInt("assigned_doctor_id");
        ec.setAssignedDoctorId(rs.wasNull() ? null : dId);
        int bId = rs.getInt("bed_id");
        ec.setBedId(rs.wasNull() ? null : bId);
        ec.setStatus(rs.getString("status"));
        ec.setAdmissionTime(rs.getTimestamp("admission_time"));
        ec.setAssignedDoctorName(rs.getString("doctor_name"));
        ec.setAllocatedBedNumber(rs.getString("bed_number"));
        return ec;
    }
}
