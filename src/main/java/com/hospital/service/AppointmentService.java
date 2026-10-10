package com.hospital.service;

import com.hospital.dao.AppointmentDAO;
import com.hospital.dao.DoctorDAO;
import com.hospital.model.Appointment;
import com.hospital.model.Doctor;

import java.sql.SQLException;
import java.util.List;

public class AppointmentService {

    private final AppointmentDAO appointmentDAO;
    private final DoctorDAO doctorDAO;

    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAO();
        this.doctorDAO = new DoctorDAO();
    }

    public AppointmentService(AppointmentDAO appointmentDAO, DoctorDAO doctorDAO) {
        this.appointmentDAO = appointmentDAO;
        this.doctorDAO = doctorDAO;
    }

    public List<Appointment> getAllAppointments() throws SQLException {
        return appointmentDAO.findAll();
    }

    public boolean bookAppointment(Appointment appt) throws SQLException {
        if (appt.getPatientId() <= 0 || appt.getDoctorId() <= 0) {
            throw new IllegalArgumentException("Valid Patient and Doctor must be selected.");
        }
        if (appt.getAppointmentDate() == null) {
            throw new IllegalArgumentException("Appointment date is required.");
        }
        if (appt.getTimeSlot() == null || appt.getTimeSlot().trim().isEmpty()) {
            throw new IllegalArgumentException("Time slot is required.");
        }

        // Check if Doctor is on leave or unavailable
        Doctor doc = doctorDAO.findById(appt.getDoctorId());
        if (doc != null && "ON_LEAVE".equalsIgnoreCase(doc.getAvailabilityStatus())) {
            throw new IllegalStateException("Cannot book appointment: Dr. " + doc.getFirstName() + " " + doc.getLastName() + " is currently ON LEAVE.");
        }

        // Check doctor time slot availability
        if (appointmentDAO.isDoctorSlotBooked(appt.getDoctorId(), appt.getAppointmentDate(), appt.getTimeSlot())) {
            throw new IllegalStateException("Appointment Conflict: Doctor already has an active appointment booked for " + appt.getTimeSlot() + " on " + appt.getAppointmentDate());
        }

        return appointmentDAO.save(appt);
    }

    public boolean updateStatus(int appointmentId, String status) throws SQLException {
        return appointmentDAO.updateStatus(appointmentId, status);
    }
}
