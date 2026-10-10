package com.hospital.model;

import java.sql.Timestamp;

public class PharmacyTransaction {
    private int transactionId;
    private int medicineId;
    private Integer patientId;
    private String transactionType; // DISPENSE, RESTOCK
    private int quantity;
    private double totalPrice;
    private Timestamp transactionDate;
    private int performedByUserId;
    private String notes;

    // Display fields
    private String medicineName;
    private String patientName;
    private String performedByUserName;

    public PharmacyTransaction() {}

    public PharmacyTransaction(int transactionId, int medicineId, Integer patientId, String transactionType, 
                               int quantity, double totalPrice, Timestamp transactionDate, int performedByUserId, String notes) {
        this.transactionId = transactionId;
        this.medicineId = medicineId;
        this.patientId = patientId;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.transactionDate = transactionDate;
        this.performedByUserId = performedByUserId;
        this.notes = notes;
    }

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public Timestamp getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Timestamp transactionDate) { this.transactionDate = transactionDate; }

    public int getPerformedByUserId() { return performedByUserId; }
    public void setPerformedByUserId(int performedByUserId) { this.performedByUserId = performedByUserId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPerformedByUserName() { return performedByUserName; }
    public void setPerformedByUserName(String performedByUserName) { this.performedByUserName = performedByUserName; }
}
