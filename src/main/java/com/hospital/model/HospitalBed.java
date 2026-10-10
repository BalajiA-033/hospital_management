package com.hospital.model;

import java.sql.Timestamp;

public class HospitalBed {
    private int bedId;
    private String bedNumber;
    private String wardName;
    private String bedType; // ICU, GENERAL, PRIVATE, SEMI_PRIVATE
    private double dailyCharge;
    private boolean isOccupied;
    private Timestamp createdAt;

    public HospitalBed() {}

    public HospitalBed(int bedId, String bedNumber, String wardName, String bedType, double dailyCharge, boolean isOccupied) {
        this.bedId = bedId;
        this.bedNumber = bedNumber;
        this.wardName = wardName;
        this.bedType = bedType;
        this.dailyCharge = dailyCharge;
        this.isOccupied = isOccupied;
    }

    public int getBedId() { return bedId; }
    public void setBedId(int bedId) { this.bedId = bedId; }

    public String getBedNumber() { return bedNumber; }
    public void setBedNumber(String bedNumber) { this.bedNumber = bedNumber; }

    public String getWardName() { return wardName; }
    public void setWardName(String wardName) { this.wardName = wardName; }

    public String getBedType() { return bedType; }
    public void setBedType(String bedType) { this.bedType = bedType; }

    public double getDailyCharge() { return dailyCharge; }
    public void setDailyCharge(double dailyCharge) { this.dailyCharge = dailyCharge; }

    public boolean isOccupied() { return isOccupied; }
    public void setOccupied(boolean isOccupied) { this.isOccupied = isOccupied; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
