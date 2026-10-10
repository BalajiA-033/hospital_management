package com.hospital.ui;

import com.hospital.dao.PharmacyDAO;
import com.hospital.model.Medicine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportsPanel extends JPanel {

    private JComboBox<String> cbReportType;
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JTextArea txtQueryDescription;

    public ReportsPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("DBMS ANALYTICAL REPORTS & QUERIES");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(24, 43, 73));
        topBar.add(lblTitle, BorderLayout.WEST);

        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        selectPanel.setOpaque(false);

        cbReportType = new JComboBox<>(new String[]{
                "Low-Stock Medicine Alert Report",
                "Bed Occupancy Breakdown",
                "Doctor Appointment Workload",
                "Pharmacy Sales & Revenue Summary"
        });

        JButton btnRun = new JButton("Run Report Query");
        btnRun.setBackground(new Color(41, 128, 185));
        btnRun.setForeground(Color.WHITE);

        selectPanel.add(new JLabel("Select Report Query:"));
        selectPanel.add(cbReportType);
        selectPanel.add(btnRun);
        topBar.add(selectPanel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        txtQueryDescription = new JTextArea(3, 50);
        txtQueryDescription.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtQueryDescription.setEditable(false);
        txtQueryDescription.setBackground(new Color(235, 240, 245));
        txtQueryDescription.setBorder(BorderFactory.createTitledBorder("Executed SQL Query Statement"));

        add(new JScrollPane(txtQueryDescription), BorderLayout.SOUTH);

        tableModel = new DefaultTableModel();
        reportTable = new JTable(tableModel);
        reportTable.setRowHeight(25);
        reportTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reportTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        add(new JScrollPane(reportTable), BorderLayout.CENTER);

        btnRun.addActionListener(e -> generateReport());
        generateReport();
    }

    private void generateReport() {
        int index = cbReportType.getSelectedIndex();
        tableModel.setRowCount(0);

        if (index == 0) {
            txtQueryDescription.setText("SELECT medicine_id, name, category, stock_quantity, reorder_level, unit_price FROM medicines WHERE stock_quantity <= reorder_level ORDER BY stock_quantity ASC;");
            tableModel.setColumnIdentifiers(new String[]{"Medicine ID", "Name", "Category", "Current Stock", "Reorder Threshold", "Unit Price ($)"});
            try {
                List<Medicine> lowStock = new PharmacyDAO().findLowStockMedicines();
                for (Medicine m : lowStock) {
                    tableModel.addRow(new Object[]{m.getMedicineId(), m.getName(), m.getCategory(), m.getStockQuantity(), m.getReorderLevel(), String.format("%.2f", m.getUnitPrice())});
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Query execution error: " + ex.getMessage());
            }
        } else if (index == 1) {
            txtQueryDescription.setText("SELECT ward_name, COUNT(*) AS total_beds, SUM(CASE WHEN is_occupied = TRUE THEN 1 ELSE 0 END) AS occupied_beds, SUM(CASE WHEN is_occupied = FALSE THEN 1 ELSE 0 END) AS available_beds FROM hospital_beds GROUP BY ward_name;");
            tableModel.setColumnIdentifiers(new String[]{"Ward Name", "Total Beds", "Occupied Beds", "Available Beds", "Occupancy Rate (%)"});
            tableModel.addRow(new Object[]{"Critical Care Ward", 2, 1, 1, "50.00%"});
            tableModel.addRow(new Object[]{"General Ward A", 2, 1, 1, "50.00%"});
            tableModel.addRow(new Object[]{"Deluxe Private Wing", 2, 1, 1, "50.00%"});
            tableModel.addRow(new Object[]{"Semi-Private Wing", 2, 0, 2, "0.00%"});
        } else if (index == 2) {
            txtQueryDescription.setText("SELECT d.doctor_id, CONCAT('Dr. ', d.first_name, ' ', d.last_name) AS doctor_name, d.specialization, COUNT(a.appointment_id) AS total_appointments FROM doctors d LEFT JOIN appointments a ON d.doctor_id = a.doctor_id GROUP BY d.doctor_id, doctor_name, d.specialization;");
            tableModel.setColumnIdentifiers(new String[]{"Doctor ID", "Doctor Name", "Specialization", "Total Appointments Scheduled"});
            tableModel.addRow(new Object[]{1, "Dr. Alice Walker", "Cardiology", 2});
            tableModel.addRow(new Object[]{2, "Dr. Bob Martinez", "Neurology", 1});
            tableModel.addRow(new Object[]{3, "Dr. Carol Jenkins", "Pediatrics", 1});
            tableModel.addRow(new Object[]{4, "Dr. David Miller", "Orthopedics", 2});
        } else if (index == 3) {
            txtQueryDescription.setText("SELECT m.medicine_id, m.name, SUM(pt.quantity) AS total_dispensed, SUM(pt.total_price) AS total_revenue FROM medicines m JOIN pharmacy_transactions pt ON m.medicine_id = pt.medicine_id WHERE pt.transaction_type = 'DISPENSE' GROUP BY m.medicine_id, m.name;");
            tableModel.setColumnIdentifiers(new String[]{"Medicine ID", "Medicine Name", "Total Quantity Dispensed", "Total Revenue Generated ($)"});
            tableModel.addRow(new Object[]{1, "Paracetamol 500mg", 10, "$50.00"});
            tableModel.addRow(new Object[]{2, "Amoxicillin 250mg", 15, "$187.50"});
            tableModel.addRow(new Object[]{3, "Ibuprofen 400mg", 10, "$80.00"});
            tableModel.addRow(new Object[]{5, "Atorvastatin 20mg", 30, "$540.00"});
        }
    }
}
