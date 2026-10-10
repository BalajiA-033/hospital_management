package com.hospital.ui;

import com.hospital.model.Doctor;
import com.hospital.service.DoctorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorPanel extends JPanel {

    private final DoctorService doctorService;
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnSearch, btnRefresh, btnAddDoctor, btnToggleStatus;
    private List<Doctor> currentDoctorsList;

    public DoctorPanel() {
        this.doctorService = new DoctorService();
        initUI();
        loadDoctors();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top Control Bar
        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("DOCTOR DIRECTORY & AVAILABILITY");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(24, 43, 73));
        topBar.add(lblTitle, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setOpaque(false);

        txtSearch = new JTextField(15);
        btnSearch = new JButton("Search");
        btnRefresh = new JButton("Refresh");
        btnAddDoctor = new JButton("+ Add Doctor");
        btnAddDoctor.setBackground(new Color(39, 174, 96));
        btnAddDoctor.setForeground(Color.WHITE);

        btnToggleStatus = new JButton("Update Availability");

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        searchPanel.add(btnAddDoctor);
        searchPanel.add(btnToggleStatus);

        topBar.add(searchPanel, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Center Table
        String[] columnNames = {"ID", "Name", "Specialization", "Qualification", "Phone", "Email", "Fee ($)", "Availability Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        doctorTable = new JTable(tableModel);
        doctorTable.setRowHeight(25);
        doctorTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        doctorTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(doctorTable);
        add(scrollPane, BorderLayout.CENTER);

        // Events
        btnSearch.addActionListener(e -> searchDoctors());
        txtSearch.addActionListener(e -> searchDoctors());
        btnRefresh.addActionListener(e -> loadDoctors());
        btnAddDoctor.addActionListener(e -> openAddDoctorDialog());
        btnToggleStatus.addActionListener(e -> toggleDoctorAvailability());
    }

    public void loadDoctors() {
        SwingWorker<List<Doctor>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Doctor> doInBackground() throws Exception {
                return doctorService.getAllDoctors();
            }

            @Override
            protected void done() {
                try {
                    currentDoctorsList = get();
                    populateTable(currentDoctorsList);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(DoctorPanel.this, "Failed to load doctors: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void searchDoctors() {
        String query = txtSearch.getText().trim();
        SwingWorker<List<Doctor>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Doctor> doInBackground() throws Exception {
                return doctorService.searchDoctors(query);
            }

            @Override
            protected void done() {
                try {
                    currentDoctorsList = get();
                    populateTable(currentDoctorsList);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(DoctorPanel.this, "Search failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void populateTable(List<Doctor> doctors) {
        tableModel.setRowCount(0);
        for (Doctor d : doctors) {
            tableModel.addRow(new Object[]{
                    d.getDoctorId(),
                    d.getFullName(),
                    d.getSpecialization(),
                    d.getQualification(),
                    d.getPhone(),
                    d.getEmail(),
                    String.format("%.2f", d.getConsultationFee()),
                    d.getAvailabilityStatus()
            });
        }
    }

    private void openAddDoctorDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Doctor Record", true);
        dialog.setSize(450, 420);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField txtFirstName = new JTextField();
        JTextField txtLastName = new JTextField();
        JTextField txtSpec = new JTextField("Cardiology");
        JTextField txtPhone = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtQual = new JTextField("MD");
        JTextField txtFee = new JTextField("500.00");
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"AVAILABLE", "ON_LEAVE", "BUSY", "OFF_DUTY"});

        panel.add(new JLabel("First Name:")); panel.add(txtFirstName);
        panel.add(new JLabel("Last Name:")); panel.add(txtLastName);
        panel.add(new JLabel("Specialization:")); panel.add(txtSpec);
        panel.add(new JLabel("Phone:")); panel.add(txtPhone);
        panel.add(new JLabel("Email:")); panel.add(txtEmail);
        panel.add(new JLabel("Qualification:")); panel.add(txtQual);
        panel.add(new JLabel("Consultation Fee ($):")); panel.add(txtFee);
        panel.add(new JLabel("Availability Status:")); panel.add(cbStatus);

        JButton btnSave = new JButton("Save Doctor");
        btnSave.setBackground(new Color(39, 174, 96));
        btnSave.setForeground(Color.WHITE);

        btnSave.addActionListener(e -> {
            try {
                Doctor d = new Doctor();
                d.setFirstName(txtFirstName.getText().trim());
                d.setLastName(txtLastName.getText().trim());
                d.setSpecialization(txtSpec.getText().trim());
                d.setPhone(txtPhone.getText().trim());
                d.setEmail(txtEmail.getText().trim());
                d.setQualification(txtQual.getText().trim());
                d.setConsultationFee(Double.parseDouble(txtFee.getText().trim()));
                d.setAvailabilityStatus((String) cbStatus.getSelectedItem());

                doctorService.addDoctor(d);
                JOptionPane.showMessageDialog(dialog, "Doctor added successfully!");
                dialog.dispose();
                loadDoctors();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Validation error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel());
        panel.add(btnSave);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void toggleDoctorAvailability() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to update availability.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Doctor doc = currentDoctorsList.get(selectedRow);

        String[] statuses = {"AVAILABLE", "ON_LEAVE", "BUSY", "OFF_DUTY"};
        String chosen = (String) JOptionPane.showInputDialog(this,
                "Select new availability status for " + doc.getFullName() + ":",
                "Update Doctor Availability", JOptionPane.PLAIN_MESSAGE, null, statuses, doc.getAvailabilityStatus());

        if (chosen != null && !chosen.equals(doc.getAvailabilityStatus())) {
            try {
                doctorService.setDoctorAvailability(doc.getDoctorId(), chosen);
                JOptionPane.showMessageDialog(this, "Availability status updated successfully!");
                loadDoctors();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to update availability: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
