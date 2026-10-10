package com.hospital.model;

import java.sql.Timestamp;

public class BedAllocation {
    private int allocationId;
    private int bedId;
    private int patientId;
    private Timestamp allocationDate;
    private Timestamp releaseDate;
    private String status; // ACTIVE, RELEASED
    private String notes;

    // Joined display fields
    private String bedNumber;
    private String patientName;

    public BedAllocation() {}

    public BedAllocation(int allocationId, int bedId, int patientId, Timestamp allocationDate, 
                         Timestamp releaseDate, String status, String notes) {
        this.allocationId = allocationId;
        this.bedId = bedId;
        this.patientId = patientId;
        this.allocationDate = allocationDate;
        this.releaseDate = releaseDate;
        this.status = status;
        this.notes = notes;
    }

    public int getAllocationId() { return allocationId; }
    public void setAllocationId(int allocationId) { this.allocationId = allocationId; }

    public int getBedId() { return bedId; }
    public void setBedId(int bedId) { this.bedId = bedId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public Timestamp getAllocationDate() { return allocationDate; }
    public void setAllocationDate(Timestamp allocationDate) { this.allocationDate = allocationDate; }

    public Timestamp getReleaseDate() { return releaseDate; }
    public void setReleaseDate(Timestamp releaseDate) { this.releaseDate = releaseDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getBedNumber() { return bedNumber; }
    public void setBedNumber(String bedNumber) { this.bedNumber = bedNumber; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
}
