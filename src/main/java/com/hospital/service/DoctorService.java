package com.hospital.service;

import com.hospital.dao.DoctorDAO;
import com.hospital.model.Doctor;
import com.hospital.util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

public class DoctorService {

    private final DoctorDAO doctorDAO;

    public DoctorService() {
        this.doctorDAO = new DoctorDAO();
    }

    public DoctorService(DoctorDAO doctorDAO) {
        this.doctorDAO = doctorDAO;
    }

    public List<Doctor> getAllDoctors() throws SQLException {
        return doctorDAO.findAll();
    }

    public List<Doctor> searchDoctors(String query) throws SQLException {
        if (!ValidationUtil.isNotEmpty(query)) {
            return doctorDAO.findAll();
        }
        return doctorDAO.search(query);
    }

    public boolean addDoctor(Doctor doctor) throws SQLException {
        validateDoctorData(doctor);
        return doctorDAO.save(doctor);
    }

    public boolean updateDoctor(Doctor doctor) throws SQLException {
        validateDoctorData(doctor);
        return doctorDAO.update(doctor);
    }

    public boolean setDoctorAvailability(int doctorId, String status) throws SQLException {
        return doctorDAO.updateAvailability(doctorId, status);
    }

    private void validateDoctorData(Doctor d) {
        if (!ValidationUtil.isNotEmpty(d.getFirstName()) || !ValidationUtil.isNotEmpty(d.getLastName())) {
            throw new IllegalArgumentException("Doctor first and last names are required.");
        }
        if (!ValidationUtil.isNotEmpty(d.getSpecialization())) {
            throw new IllegalArgumentException("Specialization is required.");
        }
        if (!ValidationUtil.isValidPhone(d.getPhone())) {
            throw new IllegalArgumentException("Valid phone number required.");
        }
        if (!ValidationUtil.isValidEmail(d.getEmail())) {
            throw new IllegalArgumentException("Valid email required.");
        }
        if (d.getConsultationFee() < 0) {
            throw new IllegalArgumentException("Consultation fee cannot be negative.");
        }
    }
}
