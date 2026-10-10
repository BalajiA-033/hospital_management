package com.hospital.model;

import java.sql.Timestamp;

public class EmergencyCase {
    private int emergencyId;
    private Integer patientId;
    private String patientNameTemp;
    private String triagePriority; // CRITICAL, HIGH, MEDIUM, LOW
    private String conditionDescription;
    private Integer assignedDoctorId;
    private Integer bedId;
    private String status; // ADMITTED, TREATED, TRANSFERRED, CLOSED
    private Timestamp admissionTime;

    // Display fields
    private String assignedDoctorName;
    private String allocatedBedNumber;

    public EmergencyCase() {}

    public EmergencyCase(int emergencyId, Integer patientId, String patientNameTemp, String triagePriority, 
                         String conditionDescription, Integer assignedDoctorId, Integer bedId, String status, Timestamp admissionTime) {
        this.emergencyId = emergencyId;
        this.patientId = patientId;
        this.patientNameTemp = patientNameTemp;
        this.triagePriority = triagePriority;
        this.conditionDescription = conditionDescription;
        this.assignedDoctorId = assignedDoctorId;
        this.bedId = bedId;
        this.status = status;
        this.admissionTime = admissionTime;
    }

    public int getEmergencyId() { return emergencyId; }
    public void setEmergencyId(int emergencyId) { this.emergencyId = emergencyId; }

    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }

    public String getPatientNameTemp() { return patientNameTemp; }
    public void setPatientNameTemp(String patientNameTemp) { this.patientNameTemp = patientNameTemp; }

    public String getTriagePriority() { return triagePriority; }
    public void setTriagePriority(String triagePriority) { this.triagePriority = triagePriority; }

    public String getConditionDescription() { return conditionDescription; }
    public void setConditionDescription(String conditionDescription) { this.conditionDescription = conditionDescription; }

    public Integer getAssignedDoctorId() { return assignedDoctorId; }
    public void setAssignedDoctorId(Integer assignedDoctorId) { this.assignedDoctorId = assignedDoctorId; }

    public Integer getBedId() { return bedId; }
    public void setBedId(Integer bedId) { this.bedId = bedId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getAdmissionTime() { return admissionTime; }
    public void setAdmissionTime(Timestamp admissionTime) { this.admissionTime = admissionTime; }

    public String getAssignedDoctorName() { return assignedDoctorName; }
    public void setAssignedDoctorName(String assignedDoctorName) { this.assignedDoctorName = assignedDoctorName; }

    public String getAllocatedBedNumber() { return allocatedBedNumber; }
    public void setAllocatedBedNumber(String allocatedBedNumber) { this.allocatedBedNumber = allocatedBedNumber; }
}
