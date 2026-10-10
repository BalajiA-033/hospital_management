package com.hospital.ui;

import com.hospital.model.Appointment;
import com.hospital.model.Doctor;
import com.hospital.model.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class AppointmentPanel extends JPanel {

    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    private JTable apptTable;
    private DefaultTableModel tableModel;
    private JButton btnRefresh, btnBook, btnUpdateStatus;
    private List<Appointment> currentAppointments;

    public AppointmentPanel() {
        this.appointmentService = new AppointmentService();
        this.patientService = new PatientService();
        this.doctorService = new DoctorService();
        initUI();
        loadAppointments();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("APPOINTMENT SCHEDULING & TRACKING");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(24, 43, 73));
        topBar.add(lblTitle, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        btnRefresh = new JButton("Refresh");
        btnBook = new JButton("+ Book Appointment");
        btnBook.setBackground(new Color(142, 68, 173));
        btnBook.setForeground(Color.WHITE);

        btnUpdateStatus = new JButton("Change Status");

        btnPanel.add(btnRefresh);
        btnPanel.add(btnBook);
        btnPanel.add(btnUpdateStatus);
        topBar.add(btnPanel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Date", "Slot", "Patient", "Doctor", "Reason", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        apptTable = new JTable(tableModel);
        apptTable.setRowHeight(25);
        apptTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        apptTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        add(new JScrollPane(apptTable), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadAppointments());
        btnBook.addActionListener(e -> openBookAppointmentDialog());
        btnUpdateStatus.addActionListener(e -> updateStatusSelected());
    }

    public void loadAppointments() {
        SwingWorker<List<Appointment>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Appointment> doInBackground() throws Exception {
                return appointmentService.getAllAppointments();
            }

            @Override
            protected void done() {
                try {
                    currentAppointments = get();
                    tableModel.setRowCount(0);
                    for (Appointment a : currentAppointments) {
                        tableModel.addRow(new Object[]{
                                a.getAppointmentId(),
                                a.getAppointmentDate(),
                                a.getTimeSlot(),
                                a.getPatientName(),
                                a.getDoctorName(),
                                a.getReason(),
                                a.getStatus()
                        });
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AppointmentPanel.this, "Error loading appointments: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void openBookAppointmentDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Book New Appointment", true);
        dialog.setSize(480, 420);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JComboBox<String> cbPatient = new JComboBox<>();
        JComboBox<String> cbDoctor = new JComboBox<>();

        List<Patient> patients;
        List<Doctor> doctors;
        try {
            patients = patientService.getAllPatients();
            doctors = doctorService.getAllDoctors();
            for (Patient p : patients) cbPatient.addItem(p.getPatientId() + ": " + p.getFullName());
            for (Doctor d : doctors) cbDoctor.addItem(d.getDoctorId() + ": " + d.getFullName() + " (" + d.getSpecialization() + " - " + d.getAvailabilityStatus() + ")");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load drop-down lists: " + ex.getMessage());
            return;
        }

        JTextField txtDate = new JTextField(LocalDate.now().toString());
        JComboBox<String> cbSlot = new JComboBox<>(new String[]{"09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "02:00 PM", "02:30 PM", "03:00 PM", "03:30 PM", "04:00 PM"});
        JTextField txtReason = new JTextField("General Consultation");
        JTextField txtNotes = new JTextField();

        panel.add(new JLabel("Patient:")); panel.add(cbPatient);
        panel.add(new JLabel("Doctor:")); panel.add(cbDoctor);
        panel.add(new JLabel("Date (YYYY-MM-DD):")); panel.add(txtDate);
        panel.add(new JLabel("Time Slot:")); panel.add(cbSlot);
        panel.add(new JLabel("Reason for Visit:")); panel.add(txtReason);
        panel.add(new JLabel("Notes:")); panel.add(txtNotes);

        JButton btnSubmit = new JButton("Confirm Booking");
        btnSubmit.setBackground(new Color(142, 68, 173));
        btnSubmit.setForeground(Color.WHITE);

        btnSubmit.addActionListener(e -> {
            if (cbPatient.getItemCount() == 0 || cbDoctor.getItemCount() == 0) {
                JOptionPane.showMessageDialog(dialog, "Please register patients and doctors before booking.");
                return;
            }
            try {
                String patientStr = (String) cbPatient.getSelectedItem();
                int patientId = Integer.parseInt(patientStr.split(":")[0]);

                String doctorStr = (String) cbDoctor.getSelectedItem();
                int doctorId = Integer.parseInt(doctorStr.split(":")[0]);

                Appointment appt = new Appointment();
                appt.setPatientId(patientId);
                appt.setDoctorId(doctorId);
                appt.setAppointmentDate(Date.valueOf(txtDate.getText().trim()));
                appt.setTimeSlot((String) cbSlot.getSelectedItem());
                appt.setReason(txtReason.getText().trim());
                appt.setNotes(txtNotes.getText().trim());
                appt.setStatus("SCHEDULED");

                appointmentService.bookAppointment(appt);
                JOptionPane.showMessageDialog(dialog, "Appointment booked successfully!");
                dialog.dispose();
                loadAppointments();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Booking Failed: " + ex.getMessage(), "Validation/Conflict Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel());
        panel.add(btnSubmit);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void updateStatusSelected() {
        int row = apptTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an appointment to change status.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Appointment appt = currentAppointments.get(row);

        String[] statuses = {"SCHEDULED", "COMPLETED", "CANCELLED", "NO_SHOW"};
        String chosen = (String) JOptionPane.showInputDialog(this, "Select new status for Appointment ID " + appt.getAppointmentId() + ":",
                "Update Status", JOptionPane.PLAIN_MESSAGE, null, statuses, appt.getStatus());

        if (chosen != null && !chosen.equals(appt.getStatus())) {
            try {
                appointmentService.updateStatus(appt.getAppointmentId(), chosen);
                JOptionPane.showMessageDialog(this, "Status updated!");
                loadAppointments();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Update failed: " + ex.getMessage());
            }
        }
    }
}
