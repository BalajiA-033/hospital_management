package com.hospital.ui;

import com.hospital.model.Doctor;
import com.hospital.model.EmergencyCase;

import com.hospital.model.HospitalBed;
import com.hospital.model.Patient;
import com.hospital.service.BedService;
import com.hospital.service.DoctorService;
import com.hospital.service.EmergencyService;
import com.hospital.service.PatientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmergencyPanel extends JPanel {

    private final EmergencyService emergencyService;
    private final DoctorService doctorService;
    private final BedService bedService;
    private final PatientService patientService;

    private JTable emergencyTable;
    private DefaultTableModel tableModel;
    private JButton btnRefresh, btnNewEmergency, btnUpdateStatus;
    private List<EmergencyCase> emergencyCases;

    public EmergencyPanel() {
        this.emergencyService = new EmergencyService();
        this.doctorService = new DoctorService();
        this.bedService = new BedService();
        this.patientService = new PatientService();
        initUI();
        loadEmergencyCases();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("EMERGENCY & TRIAGE MANAGEMENT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(192, 57, 43));
        topBar.add(lblTitle, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        btnRefresh = new JButton("Refresh");
        btnNewEmergency = new JButton("+ Register Emergency Case");
        btnNewEmergency.setBackground(new Color(192, 57, 43));
        btnNewEmergency.setForeground(Color.WHITE);

        btnUpdateStatus = new JButton("Update Status");

        btnPanel.add(btnRefresh);
        btnPanel.add(btnNewEmergency);
        btnPanel.add(btnUpdateStatus);
        topBar.add(btnPanel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        String[] columns = {"ID", "Admission Time", "Patient Name", "Priority", "Condition Description", "Assigned Doctor", "Bed", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        emergencyTable = new JTable(tableModel);
        emergencyTable.setRowHeight(25);
        emergencyTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        emergencyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        add(new JScrollPane(emergencyTable), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadEmergencyCases());
        btnNewEmergency.addActionListener(e -> openRegisterEmergencyDialog());
        btnUpdateStatus.addActionListener(e -> updateStatusSelected());
    }

    public void loadEmergencyCases() {
        SwingWorker<List<EmergencyCase>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<EmergencyCase> doInBackground() throws Exception {
                return emergencyService.getAllEmergencyCases();
            }

            @Override
            protected void done() {
                try {
                    emergencyCases = get();
                    tableModel.setRowCount(0);
                    for (EmergencyCase ec : emergencyCases) {
                        tableModel.addRow(new Object[]{
                                ec.getEmergencyId(),
                                ec.getAdmissionTime(),
                                ec.getPatientNameTemp(),
                                ec.getTriagePriority(),
                                ec.getConditionDescription(),
                                ec.getAssignedDoctorName() != null ? ec.getAssignedDoctorName() : "Unassigned",
                                ec.getAllocatedBedNumber() != null ? ec.getAllocatedBedNumber() : "None",
                                ec.getStatus()
                        });
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(EmergencyPanel.this, "Error loading emergency cases: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void openRegisterEmergencyDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Register Emergency Intake", true);
        dialog.setSize(480, 420);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField txtPatientName = new JTextField();
        JComboBox<String> cbPriority = new JComboBox<>(new String[]{"CRITICAL", "HIGH", "MEDIUM", "LOW"});
        JTextField txtCondition = new JTextField();

        JComboBox<String> cbDoctor = new JComboBox<>();
        cbDoctor.addItem("0: -- Unassigned --");
        try {
            for (Doctor d : doctorService.getAllDoctors()) {
                cbDoctor.addItem(d.getDoctorId() + ": " + d.getFullName());
            }
        } catch (Exception ex) {}

        JComboBox<String> cbBed = new JComboBox<>();
        cbBed.addItem("0: -- No Bed Allocated --");
        try {
            for (HospitalBed b : bedService.getAllBeds()) {
                if (!b.isOccupied()) cbBed.addItem(b.getBedId() + ": " + b.getBedNumber() + " (" + b.getBedType() + ")");
            }
        } catch (Exception ex) {}

        panel.add(new JLabel("Patient Name/Alias:")); panel.add(txtPatientName);
        panel.add(new JLabel("Triage Priority:")); panel.add(cbPriority);
        panel.add(new JLabel("Condition Description:")); panel.add(txtCondition);
        panel.add(new JLabel("Assign Doctor:")); panel.add(cbDoctor);
        panel.add(new JLabel("Assign Bed:")); panel.add(cbBed);

        JButton btnSubmit = new JButton("Register Intake");
        btnSubmit.setBackground(new Color(192, 57, 43));
        btnSubmit.setForeground(Color.WHITE);

        btnSubmit.addActionListener(e -> {
            try {
                EmergencyCase ec = new EmergencyCase();
                ec.setPatientNameTemp(txtPatientName.getText().trim());
                ec.setTriagePriority((String) cbPriority.getSelectedItem());
                ec.setConditionDescription(txtCondition.getText().trim());

                int docId = Integer.parseInt(((String) cbDoctor.getSelectedItem()).split(":")[0]);
                if (docId > 0) ec.setAssignedDoctorId(docId);

                int bedId = Integer.parseInt(((String) cbBed.getSelectedItem()).split(":")[0]);
                if (bedId > 0) ec.setBedId(bedId);

                ec.setStatus("ADMITTED");

                emergencyService.registerEmergency(ec);
                JOptionPane.showMessageDialog(dialog, "Emergency case registered successfully!");
                dialog.dispose();
                loadEmergencyCases();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel());
        panel.add(btnSubmit);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void updateStatusSelected() {
        int row = emergencyTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an emergency case to update status.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        EmergencyCase ec = emergencyCases.get(row);

        String[] statuses = {"ADMITTED", "TREATED", "TRANSFERRED", "CLOSED"};
        String chosen = (String) JOptionPane.showInputDialog(this, "Select status for Case ID " + ec.getEmergencyId() + ":",
                "Update Status", JOptionPane.PLAIN_MESSAGE, null, statuses, ec.getStatus());

        if (chosen != null && !chosen.equals(ec.getStatus())) {
            try {
                emergencyService.updateCaseStatus(ec.getEmergencyId(), chosen);
                JOptionPane.showMessageDialog(this, "Status updated!");
                loadEmergencyCases();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed: " + ex.getMessage());
            }
        }
    }
}
