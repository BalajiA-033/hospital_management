package com.hospital.model;

public class DashboardStats {
    private int totalPatients;
    private int totalDoctors;
    private int todaysAppointments;
    private int availableBeds;
    private int occupiedBeds;
    private int lowStockMedicines;

    public DashboardStats() {}

    public DashboardStats(int totalPatients, int totalDoctors, int todaysAppointments, 
                          int availableBeds, int occupiedBeds, int lowStockMedicines) {
        this.totalPatients = totalPatients;
        this.totalDoctors = totalDoctors;
        this.todaysAppointments = todaysAppointments;
        this.availableBeds = availableBeds;
        this.occupiedBeds = occupiedBeds;
        this.lowStockMedicines = lowStockMedicines;
    }

    public int getTotalPatients() { return totalPatients; }
    public void setTotalPatients(int totalPatients) { this.totalPatients = totalPatients; }

    public int getTotalDoctors() { return totalDoctors; }
    public void setTotalDoctors(int totalDoctors) { this.totalDoctors = totalDoctors; }

    public int getTodaysAppointments() { return todaysAppointments; }
    public void setTodaysAppointments(int todaysAppointments) { this.todaysAppointments = todaysAppointments; }

    public int getAvailableBeds() { return availableBeds; }
    public void setAvailableBeds(int availableBeds) { this.availableBeds = availableBeds; }

    public int getOccupiedBeds() { return occupiedBeds; }
    public void setOccupiedBeds(int occupiedBeds) { this.occupiedBeds = occupiedBeds; }

    public int getLowStockMedicines() { return lowStockMedicines; }
    public void setLowStockMedicines(int lowStockMedicines) { this.lowStockMedicines = lowStockMedicines; }
}
