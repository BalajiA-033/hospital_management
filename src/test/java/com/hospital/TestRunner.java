package com.hospital;

import com.hospital.model.Appointment;
import com.hospital.model.Doctor;
import com.hospital.model.PharmacyTransaction;
import com.hospital.service.AppointmentService;
import com.hospital.service.PharmacyService;
import com.hospital.dao.AppointmentDAO;
import com.hospital.dao.DoctorDAO;
import com.hospital.dao.PharmacyDAO;
import com.hospital.util.PasswordUtil;
import com.hospital.util.ValidationUtil;

import java.sql.Date;

public class TestRunner {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   SMART HOSPITAL SYSTEM - UNIT TEST SUITE       ");
        System.out.println("==================================================");

        testPasswordHashing();
        testValidationUtils();
        testAppointmentConflictDetection();
        testPharmacyStockValidation();

        System.out.println("--------------------------------------------------");
        System.out.printf("RESULTS: %d PASSED, %d FAILED\n", passed, failed);
        System.out.println("==================================================");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void testPasswordHashing() {
        try {
            String hash = PasswordUtil.hashPassword("secret123");
            boolean valid = PasswordUtil.checkPassword("secret123", hash);
            boolean invalid = PasswordUtil.checkPassword("wrongpass", hash);

            if (hash != null && valid && !invalid) {
                System.out.println("[PASS] PasswordUtil Test");
                passed++;
            } else {
                System.err.println("[FAIL] PasswordUtil Test");
                failed++;
            }
        } catch (Exception e) {
            System.err.println("[FAIL] PasswordUtil Test: " + e.getMessage());
            failed++;
        }
    }

    private static void testValidationUtils() {
        try {
            boolean e1 = ValidationUtil.isValidEmail("user@hospital.org");
            boolean e2 = ValidationUtil.isValidEmail("invalid-email");
            boolean p1 = ValidationUtil.isValidPhone("9876543210");
            boolean d1 = ValidationUtil.isValidDate("2026-10-09");

            if (e1 && !e2 && p1 && d1) {
                System.out.println("[PASS] ValidationUtil Test");
                passed++;
            } else {
                System.err.println("[FAIL] ValidationUtil Test");
                failed++;
            }
        } catch (Exception e) {
            System.err.println("[FAIL] ValidationUtil Test: " + e.getMessage());
            failed++;
        }
    }

    private static void testAppointmentConflictDetection() {
        try {
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

            AppointmentService service = new AppointmentService(new AppointmentDAO(), mockDoctorDAO);
            Appointment appt = new Appointment();
            appt.setPatientId(1);
            appt.setDoctorId(3);
            appt.setAppointmentDate(Date.valueOf("2026-10-15"));
            appt.setTimeSlot("10:00 AM");
            appt.setReason("Checkup");

            try {
                service.bookAppointment(appt);
                System.err.println("[FAIL] Appointment Conflict Test - Exception expected but none thrown");
                failed++;
            } catch (IllegalStateException ex) {
                System.out.println("[PASS] Appointment Conflict Test (Caught: Doctor ON LEAVE)");
                passed++;
            }
        } catch (Exception e) {
            System.err.println("[FAIL] Appointment Conflict Test: " + e.getMessage());
            failed++;
        }
    }

    private static void testPharmacyStockValidation() {
        try {
            PharmacyService service = new PharmacyService(new PharmacyDAO());
            PharmacyTransaction tx = new PharmacyTransaction();
            tx.setMedicineId(1);
            tx.setTransactionType("DISPENSE");
            tx.setQuantity(-10);

            try {
                service.processStockTransaction(tx);
                System.err.println("[FAIL] Pharmacy Stock Validation Test - Exception expected");
                failed++;
            } catch (IllegalArgumentException ex) {
                System.out.println("[PASS] Pharmacy Stock Validation Test (Caught: Invalid quantity)");
                passed++;
            }
        } catch (Exception e) {
            System.err.println("[FAIL] Pharmacy Stock Validation Test: " + e.getMessage());
            failed++;
        }
    }
}
