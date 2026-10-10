package com.hospital.service;

import com.hospital.dao.BedDAO;
import com.hospital.model.BedAllocation;
import com.hospital.model.HospitalBed;

import java.sql.SQLException;
import java.util.List;

public class BedService {

    private final BedDAO bedDAO;

    public BedService() {
        this.bedDAO = new BedDAO();
    }

    public BedService(BedDAO bedDAO) {
        this.bedDAO = bedDAO;
    }

    public List<HospitalBed> getAllBeds() throws SQLException {
        return bedDAO.findAllBeds();
    }

    public List<BedAllocation> getActiveAllocations() throws SQLException {
        return bedDAO.findActiveAllocations();
    }

    public boolean allocateBed(int bedId, int patientId, String notes) throws SQLException {
        if (bedId <= 0 || patientId <= 0) {
            throw new IllegalArgumentException("Valid bed and patient must be selected.");
        }
        return bedDAO.allocateBedTransactional(bedId, patientId, notes);
    }

    public boolean releaseBed(int allocationId, int bedId) throws SQLException {
        if (allocationId <= 0 || bedId <= 0) {
            throw new IllegalArgumentException("Invalid allocation or bed identifier.");
        }
        return bedDAO.releaseBedTransactional(allocationId, bedId);
    }
}
