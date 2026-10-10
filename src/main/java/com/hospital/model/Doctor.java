package com.hospital.model;

import java.sql.Timestamp;

public class Doctor {
    private int doctorId;
    private Integer userId;
    private String firstName;
    private String lastName;
    private String specialization;
    private String phone;
    private String email;
    private String qualification;
    private double consultationFee;
    private String availabilityStatus; // AVAILABLE, ON_LEAVE, BUSY, OFF_DUTY
    private Timestamp createdAt;

    public Doctor() {}

    public Doctor(int doctorId, Integer userId, String firstName, String lastName, String specialization, 
                  String phone, String email, String qualification, double consultationFee, String availabilityStatus) {
        this.doctorId = doctorId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.qualification = qualification;
        this.consultationFee = consultationFee;
        this.availabilityStatus = availabilityStatus;
    }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return "Dr. " + firstName + " " + lastName; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }

    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
