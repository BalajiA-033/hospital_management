package com.hospital.dao;

import com.hospital.config.DatabaseConfig;
import com.hospital.model.BedAllocation;
import com.hospital.model.HospitalBed;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BedDAO {

    public List<HospitalBed> findAllBeds() throws SQLException {
        List<HospitalBed> beds = new ArrayList<>();
        String sql = "SELECT bed_id, bed_number, ward_name, bed_type, daily_charge, is_occupied, created_at FROM hospital_beds ORDER BY ward_name, bed_number";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                beds.add(mapResultSetToBed(rs));
            }
        }
        return beds;
    }

    public List<BedAllocation> findActiveAllocations() throws SQLException {
        List<BedAllocation> allocations = new ArrayList<>();
        String sql = "SELECT ba.allocation_id, ba.bed_id, ba.patient_id, ba.allocation_date, ba.release_date, ba.status, ba.notes, "
                   + "b.bed_number, CONCAT(p.first_name, ' ', p.last_name) AS patient_name "
                   + "FROM bed_allocations ba "
                   + "JOIN hospital_beds b ON ba.bed_id = b.bed_id "
                   + "JOIN patients p ON ba.patient_id = p.patient_id "
                   + "WHERE ba.status = 'ACTIVE' ORDER BY ba.allocation_id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                BedAllocation alloc = new BedAllocation();
                alloc.setAllocationId(rs.getInt("allocation_id"));
                alloc.setBedId(rs.getInt("bed_id"));
                alloc.setPatientId(rs.getInt("patient_id"));
                alloc.setAllocationDate(rs.getTimestamp("allocation_date"));
                alloc.setReleaseDate(rs.getTimestamp("release_date"));
                alloc.setStatus(rs.getString("status"));
                alloc.setNotes(rs.getString("notes"));
                alloc.setBedNumber(rs.getString("bed_number"));
                alloc.setPatientName(rs.getString("patient_name"));
                allocations.add(alloc);
            }
        }
        return allocations;
    }

    /**
     * Atomically allocates a bed to a patient using SQL transactions and conditional locking to prevent concurrent double allocation.
     */
    public boolean allocateBedTransactional(int bedId, int patientId, String notes) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            // Step 1: Check if bed is available and conditionally lock row
            String checkSql = DatabaseConfig.isH2Fallback()
                    ? "SELECT is_occupied FROM hospital_beds WHERE bed_id = ?"
                    : "SELECT is_occupied FROM hospital_beds WHERE bed_id = ? FOR UPDATE";
            
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, bedId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next() || rs.getBoolean("is_occupied")) {
                        conn.rollback();
                        throw new SQLException("Bed ID " + bedId + " is already occupied or does not exist.");
                    }
                }
            }

            // Step 2: Mark bed as occupied
            String updateBedSql = "UPDATE hospital_beds SET is_occupied = TRUE WHERE bed_id = ? AND is_occupied = FALSE";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateBedSql)) {
                updateStmt.setInt(1, bedId);
                int updatedRows = updateStmt.executeUpdate();
                if (updatedRows == 0) {
                    conn.rollback();
                    throw new SQLException("Concurrent allocation attempt failed for Bed ID " + bedId);
                }
            }

            // Step 3: Insert bed allocation record
            String insertAllocSql = "INSERT INTO bed_allocations (bed_id, patient_id, status, notes) VALUES (?, ?, 'ACTIVE', ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertAllocSql)) {
                insertStmt.setInt(1, bedId);
                insertStmt.setInt(2, patientId);
                insertStmt.setString(3, notes);
                insertStmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException e) { /* ignore */ }
            }
            throw ex;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { /* ignore */ }
            }
        }
    }

    /**
     * Atomically releases an occupied bed.
     */
    public boolean releaseBedTransactional(int allocationId, int bedId) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            // Step 1: Update allocation status to RELEASED
            String updateAllocSql = "UPDATE bed_allocations SET status = 'RELEASED', release_date = CURRENT_TIMESTAMP WHERE allocation_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateAllocSql)) {
                stmt.setInt(1, allocationId);
                stmt.executeUpdate();
            }

            // Step 2: Update bed status to not occupied
            String updateBedSql = "UPDATE hospital_beds SET is_occupied = FALSE WHERE bed_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateBedSql)) {
                stmt.setInt(1, bedId);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException e) { /* ignore */ }
            }
            throw ex;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { /* ignore */ }
            }
        }
    }

    private HospitalBed mapResultSetToBed(ResultSet rs) throws SQLException {
        HospitalBed b = new HospitalBed();
        b.setBedId(rs.getInt("bed_id"));
        b.setBedNumber(rs.getString("bed_number"));
        b.setWardName(rs.getString("ward_name"));
        b.setBedType(rs.getString("bed_type"));
        b.setDailyCharge(rs.getDouble("daily_charge"));
        b.setOccupied(rs.getBoolean("is_occupied"));
        b.setCreatedAt(rs.getTimestamp("created_at"));
        return b;
    }
}
