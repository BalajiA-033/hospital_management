package com.hospital.ui;

import com.hospital.model.Patient;
import com.hospital.service.PatientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class PatientPanel extends JPanel {

    private final PatientService patientService;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnSearch, btnRefresh, btnRegister, btnEdit, btnDelete;
    private List<Patient> currentPatientsList;

    public PatientPanel() {
        this.patientService = new PatientService();
        initUI();
        loadPatients();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top Control Bar
        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("PATIENT MANAGEMENT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(24, 43, 73));
        topBar.add(lblTitle, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setOpaque(false);

        txtSearch = new JTextField(15);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        btnSearch = new JButton("Search");
        btnRefresh = new JButton("Refresh");
        btnRegister = new JButton("+ Register Patient");
        btnRegister.setBackground(new Color(41, 128, 185));
        btnRegister.setForeground(Color.WHITE);

        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        searchPanel.add(btnRegister);
        searchPanel.add(btnEdit);
        searchPanel.add(btnDelete);

        topBar.add(searchPanel, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Center Table
        String[] columnNames = {"ID", "Name", "Gender", "DOB", "Phone", "Email", "Blood Group", "Emergency Contact"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        patientTable = new JTable(tableModel);
        patientTable.setRowHeight(25);
        patientTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        patientTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(patientTable);
        add(scrollPane, BorderLayout.CENTER);

        // Actions
        btnSearch.addActionListener(e -> searchPatients());
        txtSearch.addActionListener(e -> searchPatients());
        btnRefresh.addActionListener(e -> loadPatients());
        btnRegister.addActionListener(e -> openRegisterDialog(null));
        btnEdit.addActionListener(e -> openEditSelected());
        btnDelete.addActionListener(e -> deleteSelectedPatient());
    }

    public void loadPatients() {
        SwingWorker<List<Patient>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Patient> doInBackground() throws Exception {
                return patientService.getAllPatients();
            }

            @Override
            protected void done() {
                try {
                    currentPatientsList = get();
                    populateTable(currentPatientsList);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PatientPanel.this, "Failed to load patients: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void searchPatients() {
        String query = txtSearch.getText().trim();
        SwingWorker<List<Patient>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Patient> doInBackground() throws Exception {
                return patientService.searchPatients(query);
            }

            @Override
            protected void done() {
                try {
                    currentPatientsList = get();
                    populateTable(currentPatientsList);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PatientPanel.this, "Search failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void populateTable(List<Patient> patients) {
        tableModel.setRowCount(0);
        for (Patient p : patients) {
            tableModel.addRow(new Object[]{
                    p.getPatientId(),
                    p.getFullName(),
                    p.getGender(),
                    p.getDob(),
                    p.getPhone(),
                    p.getEmail() != null ? p.getEmail() : "-",
                    p.getBloodGroup(),
                    p.getEmergencyContact()
            });
        }
    }

    private void openEditSelected() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a patient to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Patient p = currentPatientsList.get(selectedRow);
        openRegisterDialog(p);
    }

    private void openRegisterDialog(Patient existingPatient) {
        boolean isEdit = (existingPatient != null);
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isEdit ? "Edit Patient" : "Register Patient", true);
        dialog.setSize(450, 480);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(10, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField txtFirstName = new JTextField(isEdit ? existingPatient.getFirstName() : "");
        JTextField txtLastName = new JTextField(isEdit ? existingPatient.getLastName() : "");
        JComboBox<String> cbGender = new JComboBox<>(new String[]{"MALE", "FEMALE", "OTHER"});
        if (isEdit) cbGender.setSelectedItem(existingPatient.getGender());
        JTextField txtDob = new JTextField(isEdit ? existingPatient.getDob().toString() : "1995-01-01");
        JTextField txtPhone = new JTextField(isEdit ? existingPatient.getPhone() : "");
        JTextField txtEmail = new JTextField(isEdit ? (existingPatient.getEmail() != null ? existingPatient.getEmail() : "") : "");
        JTextField txtAddress = new JTextField(isEdit ? (existingPatient.getAddress() != null ? existingPatient.getAddress() : "") : "");
        JComboBox<String> cbBlood = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"});
        if (isEdit) cbBlood.setSelectedItem(existingPatient.getBloodGroup());
        JTextField txtEmergency = new JTextField(isEdit ? existingPatient.getEmergencyContact() : "");

        panel.add(new JLabel("First Name:")); panel.add(txtFirstName);
        panel.add(new JLabel("Last Name:")); panel.add(txtLastName);
        panel.add(new JLabel("Gender:")); panel.add(cbGender);
        panel.add(new JLabel("DOB (YYYY-MM-DD):")); panel.add(txtDob);
        panel.add(new JLabel("Phone:")); panel.add(txtPhone);
        panel.add(new JLabel("Email:")); panel.add(txtEmail);
        panel.add(new JLabel("Address:")); panel.add(txtAddress);
        panel.add(new JLabel("Blood Group:")); panel.add(cbBlood);
        panel.add(new JLabel("Emergency Contact:")); panel.add(txtEmergency);

        JButton btnSave = new JButton(isEdit ? "Update" : "Register");
        btnSave.setBackground(new Color(41, 128, 185));
        btnSave.setForeground(Color.WHITE);

        btnSave.addActionListener(e -> {
            try {
                Patient p = isEdit ? existingPatient : new Patient();
                p.setFirstName(txtFirstName.getText().trim());
                p.setLastName(txtLastName.getText().trim());
                p.setGender((String) cbGender.getSelectedItem());
                p.setDob(Date.valueOf(txtDob.getText().trim()));
                p.setPhone(txtPhone.getText().trim());
                p.setEmail(txtEmail.getText().trim());
                p.setAddress(txtAddress.getText().trim());
                p.setBloodGroup((String) cbBlood.getSelectedItem());
                p.setEmergencyContact(txtEmergency.getText().trim());

                if (isEdit) {
                    patientService.updatePatient(p);
                    JOptionPane.showMessageDialog(dialog, "Patient updated successfully!");
                } else {
                    patientService.registerPatient(p);
                    JOptionPane.showMessageDialog(dialog, "Patient registered successfully!");
                }
                dialog.dispose();
                loadPatients();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Validation error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel());
        panel.add(btnSave);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void deleteSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Patient p = currentPatientsList.get(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete patient record: " + p.getFullName() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = patientService.deletePatient(p.getPatientId());
                if (success) {
                    JOptionPane.showMessageDialog(this, "Patient deleted successfully!");
                    loadPatients();
                } else {
                    JOptionPane.showMessageDialog(this, "Could not delete patient record.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cannot delete patient: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
