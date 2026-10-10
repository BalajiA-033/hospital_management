package com.hospital.service;

import com.hospital.dao.PharmacyDAO;
import com.hospital.model.Medicine;
import com.hospital.model.PharmacyTransaction;
import com.hospital.util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

public class PharmacyService {

    private final PharmacyDAO pharmacyDAO;

    public PharmacyService() {
        this.pharmacyDAO = new PharmacyDAO();
    }

    public PharmacyService(PharmacyDAO pharmacyDAO) {
        this.pharmacyDAO = pharmacyDAO;
    }

    public List<Medicine> getAllMedicines() throws SQLException {
        return pharmacyDAO.findAllMedicines();
    }

    public List<Medicine> getLowStockMedicines() throws SQLException {
        return pharmacyDAO.findLowStockMedicines();
    }

    public List<PharmacyTransaction> getTransactionHistory() throws SQLException {
        return pharmacyDAO.findAllTransactions();
    }

    public boolean addMedicine(Medicine medicine) throws SQLException {
        if (!ValidationUtil.isNotEmpty(medicine.getName())) {
            throw new IllegalArgumentException("Medicine name is required.");
        }
        if (medicine.getUnitPrice() < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative.");
        }
        if (medicine.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative.");
        }
        return pharmacyDAO.saveMedicine(medicine);
    }

    public boolean processStockTransaction(PharmacyTransaction tx) throws SQLException {
        if (tx.getMedicineId() <= 0) {
            throw new IllegalArgumentException("Medicine must be selected.");
        }
        if (tx.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (!"DISPENSE".equalsIgnoreCase(tx.getTransactionType()) && !"RESTOCK".equalsIgnoreCase(tx.getTransactionType())) {
            throw new IllegalArgumentException("Invalid transaction type.");
        }

        // Perform transactional update in DAO
        return pharmacyDAO.processTransactionTransactional(tx);
    }
}
