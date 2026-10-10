package com.hospital.dao;

import com.hospital.config.DatabaseConfig;
import com.hospital.model.Medicine;
import com.hospital.model.PharmacyTransaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PharmacyDAO {

    public List<Medicine> findAllMedicines() throws SQLException {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT medicine_id, name, category, unit_price, stock_quantity, reorder_level, manufacturer, expiry_date FROM medicines ORDER BY name ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                medicines.add(mapResultSetToMedicine(rs));
            }
        }
        return medicines;
    }

    public List<Medicine> findLowStockMedicines() throws SQLException {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT medicine_id, name, category, unit_price, stock_quantity, reorder_level, manufacturer, expiry_date FROM medicines WHERE stock_quantity <= reorder_level ORDER BY stock_quantity ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                medicines.add(mapResultSetToMedicine(rs));
            }
        }
        return medicines;
    }

    public Medicine findMedicineById(int medicineId) throws SQLException {
        String sql = "SELECT medicine_id, name, category, unit_price, stock_quantity, reorder_level, manufacturer, expiry_date FROM medicines WHERE medicine_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, medicineId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMedicine(rs);
                }
            }
        }
        return null;
    }

    public boolean saveMedicine(Medicine medicine) throws SQLException {
        String sql = "INSERT INTO medicines (name, category, unit_price, stock_quantity, reorder_level, manufacturer, expiry_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, medicine.getName());
            stmt.setString(2, medicine.getCategory());
            stmt.setDouble(3, medicine.getUnitPrice());
            stmt.setInt(4, medicine.getStockQuantity());
            stmt.setInt(5, medicine.getReorderLevel());
            stmt.setString(6, medicine.getManufacturer());
            stmt.setDate(7, medicine.getExpiryDate());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        medicine.setMedicineId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Atomically executes a pharmacy stock transaction (DISPENSE or RESTOCK).
     * Prevents stock from becoming negative by enforcing quantity validation in transaction.
     */
    public boolean processTransactionTransactional(PharmacyTransaction tx) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            // Step 1: Check current stock
            String checkSql = DatabaseConfig.isH2Fallback()
                    ? "SELECT stock_quantity, unit_price FROM medicines WHERE medicine_id = ?"
                    : "SELECT stock_quantity, unit_price FROM medicines WHERE medicine_id = ? FOR UPDATE";

            int currentStock = 0;
            double unitPrice = 0.0;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, tx.getMedicineId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        currentStock = rs.getInt("stock_quantity");
                        unitPrice = rs.getDouble("unit_price");
                    } else {
                        conn.rollback();
                        throw new SQLException("Medicine ID " + tx.getMedicineId() + " does not exist.");
                    }
                }
            }

            // Step 2: Validate stock availability for dispense
            if ("DISPENSE".equalsIgnoreCase(tx.getTransactionType())) {
                if (currentStock < tx.getQuantity()) {
                    conn.rollback();
                    throw new SQLException("Insufficient stock! Requested: " + tx.getQuantity() + ", Available: " + currentStock);
                }
            }

            // Step 3: Update stock quantity
            int newStock = "DISPENSE".equalsIgnoreCase(tx.getTransactionType())
                    ? currentStock - tx.getQuantity()
                    : currentStock + tx.getQuantity();

            String updateStockSql = "UPDATE medicines SET stock_quantity = ? WHERE medicine_id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateStockSql)) {
                updateStmt.setInt(1, newStock);
                updateStmt.setInt(2, tx.getMedicineId());
                updateStmt.executeUpdate();
            }

            // Step 4: Record transaction log
            double calculatedTotalPrice = tx.getTotalPrice() > 0 ? tx.getTotalPrice() : unitPrice * tx.getQuantity();
            String insertTxSql = "INSERT INTO pharmacy_transactions (medicine_id, patient_id, transaction_type, quantity, total_price, performed_by_user_id, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement txStmt = conn.prepareStatement(insertTxSql)) {
                txStmt.setInt(1, tx.getMedicineId());
                if (tx.getPatientId() != null) {
                    txStmt.setInt(2, tx.getPatientId());
                } else {
                    txStmt.setNull(2, Types.INTEGER);
                }
                txStmt.setString(3, tx.getTransactionType());
                txStmt.setInt(4, tx.getQuantity());
                txStmt.setDouble(5, calculatedTotalPrice);
                txStmt.setInt(6, tx.getPerformedByUserId());
                txStmt.setString(7, tx.getNotes());
                txStmt.executeUpdate();
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

    public List<PharmacyTransaction> findAllTransactions() throws SQLException {
        List<PharmacyTransaction> list = new ArrayList<>();
        String sql = "SELECT pt.transaction_id, pt.medicine_id, pt.patient_id, pt.transaction_type, pt.quantity, pt.total_price, pt.transaction_date, pt.performed_by_user_id, pt.notes, "
                   + "m.name AS medicine_name, "
                   + "CONCAT(p.first_name, ' ', p.last_name) AS patient_name, "
                   + "u.username AS performed_by_user_name "
                   + "FROM pharmacy_transactions pt "
                   + "JOIN medicines m ON pt.medicine_id = m.medicine_id "
                   + "LEFT JOIN patients p ON pt.patient_id = p.patient_id "
                   + "JOIN users u ON pt.performed_by_user_id = u.user_id "
                   + "ORDER BY pt.transaction_date DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                PharmacyTransaction tx = new PharmacyTransaction();
                tx.setTransactionId(rs.getInt("transaction_id"));
                tx.setMedicineId(rs.getInt("medicine_id"));
                int pId = rs.getInt("patient_id");
                tx.setPatientId(rs.wasNull() ? null : pId);
                tx.setTransactionType(rs.getString("transaction_type"));
                tx.setQuantity(rs.getInt("quantity"));
                tx.setTotalPrice(rs.getDouble("total_price"));
                tx.setTransactionDate(rs.getTimestamp("transaction_date"));
                tx.setPerformedByUserId(rs.getInt("performed_by_user_id"));
                tx.setNotes(rs.getString("notes"));
                tx.setMedicineName(rs.getString("medicine_name"));
                tx.setPatientName(rs.getString("patient_name"));
                tx.setPerformedByUserName(rs.getString("performed_by_user_name"));
                list.add(tx);
            }
        }
        return list;
    }

    private Medicine mapResultSetToMedicine(ResultSet rs) throws SQLException {
        Medicine m = new Medicine();
        m.setMedicineId(rs.getInt("medicine_id"));
        m.setName(rs.getString("name"));
        m.setCategory(rs.getString("category"));
        m.setUnitPrice(rs.getDouble("unit_price"));
        m.setStockQuantity(rs.getInt("stock_quantity"));
        m.setReorderLevel(rs.getInt("reorder_level"));
        m.setManufacturer(rs.getString("manufacturer"));
        m.setExpiryDate(rs.getDate("expiry_date"));
        return m;
    }
}
