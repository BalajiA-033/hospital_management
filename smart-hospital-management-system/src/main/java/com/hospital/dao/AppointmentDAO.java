package com.hospital.dao;

import com.hospital.config.DatabaseConfig;
import com.hospital.model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public List<Appointment> findAll() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.appointment_date, a.time_slot, a.status, a.reason, a.notes, a.created_at, "
                   + "CONCAT(p.first_name, ' ', p.last_name) AS patient_name, "
                   + "CONCAT('Dr. ', d.first_name, ' ', d.last_name) AS doctor_name "
                   + "FROM appointments a "
                   + "JOIN patients p ON a.patient_id = p.patient_id "
                   + "JOIN doctors d ON a.doctor_id = d.doctor_id "
                   + "ORDER BY a.appointment_date DESC, a.time_slot ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        }
        return appointments;
    }

    public boolean isDoctorSlotBooked(int doctorId, Date date, String timeSlot) throws SQLException {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND time_slot = ? AND status = 'SCHEDULED'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            stmt.setDate(2, date);
            stmt.setString(3, timeSlot);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean save(Appointment appointment) throws SQLException {
        if (isDoctorSlotBooked(appointment.getDoctorId(), appointment.getAppointmentDate(), appointment.getTimeSlot())) {
            throw new SQLException("Doctor already has a scheduled appointment for slot " + appointment.getTimeSlot() + " on " + appointment.getAppointmentDate());
        }

        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, time_slot, status, reason, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctorId());
            stmt.setDate(3, appointment.getAppointmentDate());
            stmt.setString(4, appointment.getTimeSlot());
            stmt.setString(5, appointment.getStatus() != null ? appointment.getStatus() : "SCHEDULED");
            stmt.setString(6, appointment.getReason());
            stmt.setString(7, appointment.getNotes());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        appointment.setAppointmentId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean updateStatus(int appointmentId, String status) throws SQLException {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, appointmentId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appt = new Appointment();
        appt.setAppointmentId(rs.getInt("appointment_id"));
        appt.setPatientId(rs.getInt("patient_id"));
        appt.setDoctorId(rs.getInt("doctor_id"));
        appt.setAppointmentDate(rs.getDate("appointment_date"));
        appt.setTimeSlot(rs.getString("time_slot"));
        appt.setStatus(rs.getString("status"));
        appt.setReason(rs.getString("reason"));
        appt.setNotes(rs.getString("notes"));
        appt.setCreatedAt(rs.getTimestamp("created_at"));
        appt.setPatientName(rs.getString("patient_name"));
        appt.setDoctorName(rs.getString("doctor_name"));
        return appt;
    }
}
