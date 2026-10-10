package com.hospital.service;

import com.hospital.dao.PatientDAO;
import com.hospital.model.Patient;
import com.hospital.util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

public class PatientService {

    private final PatientDAO patientDAO;

    public PatientService() {
        this.patientDAO = new PatientDAO();
    }

    public PatientService(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    public List<Patient> getAllPatients() throws SQLException {
        return patientDAO.findAll();
    }

    public List<Patient> searchPatients(String query) throws SQLException {
        if (!ValidationUtil.isNotEmpty(query)) {
            return patientDAO.findAll();
        }
        return patientDAO.search(query);
    }

    public boolean registerPatient(Patient patient) throws SQLException {
        validatePatientData(patient);
        return patientDAO.save(patient);
    }

    public boolean updatePatient(Patient patient) throws SQLException {
        if (patient.getPatientId() <= 0) {
            throw new IllegalArgumentException("Invalid Patient ID for update.");
        }
        validatePatientData(patient);
        return patientDAO.update(patient);
    }

    public boolean deletePatient(int patientId) throws SQLException {
        return patientDAO.delete(patientId);
    }

    private void validatePatientData(Patient p) {
        if (!ValidationUtil.isNotEmpty(p.getFirstName()) || !ValidationUtil.isNotEmpty(p.getLastName())) {
            throw new IllegalArgumentException("Patient first name and last name are required.");
        }
        if (!ValidationUtil.isValidPhone(p.getPhone())) {
            throw new IllegalArgumentException("Invalid 10-15 digit phone number provided.");
        }
        if (ValidationUtil.isNotEmpty(p.getEmail()) && !ValidationUtil.isValidEmail(p.getEmail())) {
            throw new IllegalArgumentException("Invalid email format provided.");
        }
        if (!ValidationUtil.isNotEmpty(p.getBloodGroup())) {
            throw new IllegalArgumentException("Blood group is required.");
        }
    }
}
