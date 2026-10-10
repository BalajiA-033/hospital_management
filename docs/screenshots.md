# Genuine Application Screenshot Guide

This guide describes how to run the application locally and capture genuine screenshots for inclusion in your college DBMS practical report.

## Step-by-Step Instructions

1. **Launch the Application:**
   Run the application using Maven or Java executable jar:
   ```bash
   java -cp "target/classes;target/dependency/*" com.hospital.Main
   ```

2. **Capture Key Application Views:**

   - **Screenshot 1: Authentication Screen (`01_login_screen.png`)**
     - Description: Login modal showing username, password input, and BCrypt verification.

   - **Screenshot 2: Main Hospital Dashboard (`02_main_dashboard.png`)**
     - Description: Live KPI stat cards (Total Patients, Doctors, Today's Appointments, Available/Occupied Beds, Low-Stock alerts) and 5-second polling timestamp.

   - **Screenshot 3: Patient Directory & Registration (`03_patient_management.png`)**
     - Description: JTable listing registered patients with search filtering and modal dialog.

   - **Screenshot 4: Doctor Directory & Availability (`04_doctor_directory.png`)**
     - Description: Doctor directory with specialization tags and availability status updates.

   - **Screenshot 5: Appointment Booking (`05_appointment_booking.png`)**
     - Description: Booking dialog validating doctor availability and preventing double bookings.

   - **Screenshot 6: Bed Allocation Grid (`06_bed_allocation.png`)**
     - Description: Bed availability table showing occupied vs. available beds.

   - **Screenshot 7: Emergency Intake & Triage (`07_emergency_triage.png`)**
     - Description: Emergency intake monitor sorted by triage priority (CRITICAL, HIGH, MEDIUM, LOW).

   - **Screenshot 8: Pharmacy Inventory & Low-Stock Warning (`08_pharmacy_inventory.png`)**
     - Description: Medicine inventory table with soft red highlighting for low-stock items.

   - **Screenshot 9: Analytical DBMS Query Viewer (`09_analytical_reports.png`)**
     - Description: Analytical SQL query executor displaying aggregated results.

3. **Save Location:**
   Place captured PNG images inside a `docs/screenshots/` directory for embedding in your report.
