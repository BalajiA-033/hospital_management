package com.hospital.service;

import com.hospital.dao.EmergencyDAO;
import com.hospital.model.EmergencyCase;

import java.sql.SQLException;
import java.util.List;

public class EmergencyService {

    private final EmergencyDAO emergencyDAO;

    public EmergencyService() {
        this.emergencyDAO = new EmergencyDAO();
    }

    public EmergencyService(EmergencyDAO emergencyDAO) {
        this.emergencyDAO = emergencyDAO;
    }

    public List<EmergencyCase> getAllEmergencyCases() throws SQLException {
        return emergencyDAO.findAll();
    }

    public boolean registerEmergency(EmergencyCase eCase) throws SQLException {
        if (eCase.getPatientNameTemp() == null || eCase.getPatientNameTemp().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient description or name is required.");
        }
        if (eCase.getConditionDescription() == null || eCase.getConditionDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Condition description is required.");
        }
        return emergencyDAO.save(eCase);
    }

    public boolean updateCaseStatus(int emergencyId, String status) throws SQLException {
        return emergencyDAO.updateStatus(emergencyId, status);
    }
}
