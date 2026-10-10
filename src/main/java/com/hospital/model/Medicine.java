package com.hospital.model;

import java.sql.Date;

public class Medicine {
    private int medicineId;
    private String name;
    private String category;
    private double unitPrice;
    private int stockQuantity;
    private int reorderLevel;
    private String manufacturer;
    private Date expiryDate;

    public Medicine() {}

    public Medicine(int medicineId, String name, String category, double unitPrice, int stockQuantity, 
                    int reorderLevel, String manufacturer, Date expiryDate) {
        this.medicineId = medicineId;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.reorderLevel = reorderLevel;
        this.manufacturer = manufacturer;
        this.expiryDate = expiryDate;
    }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public int getReorderLevel() { return reorderLevel; }
    public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public boolean isLowStock() {
        return stockQuantity <= reorderLevel;
    }
}
