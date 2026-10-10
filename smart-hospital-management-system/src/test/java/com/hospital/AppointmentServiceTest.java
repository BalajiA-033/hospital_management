package com.hospital;

import com.hospital.dao.AppointmentDAO;
import com.hospital.dao.DoctorDAO;
import com.hospital.model.Appointment;
import com.hospital.model.Doctor;
import com.hospital.service.AppointmentService;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentServiceTest {

    @Test
    public void testDoctorOnLeaveBookingConflict() throws SQLException {
        // Stub DAO behavior
        DoctorDAO mockDoctorDAO = new DoctorDAO() {
            @Override
            public Doctor findById(int id) {
                Doctor d = new Doctor();
                d.setDoctorId(id);
                d.setFirstName("Carol");
                d.setLastName("Jenkins");
                d.setAvailabilityStatus("ON_LEAVE");
                return d;
            }
        };

        AppointmentDAO mockAppointmentDAO = new AppointmentDAO();
        AppointmentService service = new AppointmentService(mockAppointmentDAO, mockDoctorDAO);

        Appointment appt = new Appointment();
        appt.setPatientId(1);
        appt.setDoctorId(3);
        appt.setAppointmentDate(Date.valueOf("2026-10-15"));
        appt.setTimeSlot("10:00 AM");
        appt.setReason("Checkup");

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.bookAppointment(appt));
        assertTrue(ex.getMessage().contains("ON LEAVE"));
    }
}
