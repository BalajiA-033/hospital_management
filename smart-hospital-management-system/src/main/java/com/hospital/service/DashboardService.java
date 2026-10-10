package com.hospital.service;

import com.hospital.dao.DashboardDAO;
import com.hospital.model.DashboardStats;

import java.sql.SQLException;

public class DashboardService {

    private final DashboardDAO dashboardDAO;

    public DashboardService() {
        this.dashboardDAO = new DashboardDAO();
    }

    public DashboardService(DashboardDAO dashboardDAO) {
        this.dashboardDAO = dashboardDAO;
    }

    public DashboardStats getStats() throws SQLException {
        return dashboardDAO.getDashboardStats();
    }
}
