package com.hospital.ui;

import com.hospital.model.Medicine;
import com.hospital.model.Patient;
import com.hospital.model.PharmacyTransaction;
import com.hospital.service.AuthService;
import com.hospital.service.PatientService;
import com.hospital.service.PharmacyService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class PharmacyPanel extends JPanel {

    private final PharmacyService pharmacyService;
    private final PatientService patientService;

    private JTable medicineTable;
    private DefaultTableModel tableModel;
    private JButton btnRefresh, btnAddMedicine, btnDispenseRestock, btnTxHistory;
    private List<Medicine> medicineList;

    public PharmacyPanel() {
        this.pharmacyService = new PharmacyService();
        this.patientService = new PatientService();
        initUI();
        loadMedicines();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("PHARMACY INVENTORY & TRANSACTIONS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(24, 43, 73));
        topBar.add(lblTitle, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        btnRefresh = new JButton("Refresh");
        btnAddMedicine = new JButton("+ Add Medicine");
        btnAddMedicine.setBackground(new Color(41, 128, 185));
        btnAddMedicine.setForeground(Color.WHITE);

        btnDispenseRestock = new JButton("Dispense / Restock");
        btnDispenseRestock.setBackground(new Color(39, 174, 96));
        btnDispenseRestock.setForeground(Color.WHITE);

        btnTxHistory = new JButton("Transaction History");

        btnPanel.add(btnRefresh);
        btnPanel.add(btnAddMedicine);
        btnPanel.add(btnDispenseRestock);
        btnPanel.add(btnTxHistory);
        topBar.add(btnPanel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        String[] columns = {"ID", "Medicine Name", "Category", "Unit Price ($)", "Stock Quantity", "Reorder Level", "Manufacturer", "Expiry Date", "Stock Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        medicineTable = new JTable(tableModel);
        medicineTable.setRowHeight(25);
        medicineTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        medicineTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Highlight low stock rows in soft red
        medicineTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int stock = (int) table.getValueAt(row, 4);
                int reorder = (int) table.getValueAt(row, 5);

                if (!isSelected) {
                    if (stock <= reorder) {
                        c.setBackground(new Color(255, 230, 230));
                        c.setForeground(new Color(180, 0, 0));
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                }
                return c;
            }
        });

        add(new JScrollPane(medicineTable), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadMedicines());
        btnAddMedicine.addActionListener(e -> openAddMedicineDialog());
        btnDispenseRestock.addActionListener(e -> openTransactionDialog());
        btnTxHistory.addActionListener(e -> openTransactionHistoryDialog());
    }

    public void loadMedicines() {
        SwingWorker<List<Medicine>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Medicine> doInBackground() throws Exception {
                return pharmacyService.getAllMedicines();
            }

            @Override
            protected void done() {
                try {
                    medicineList = get();
                    tableModel.setRowCount(0);
                    for (Medicine m : medicineList) {
                        tableModel.addRow(new Object[]{
                                m.getMedicineId(),
                                m.getName(),
                                m.getCategory(),
                                String.format("%.2f", m.getUnitPrice()),
                                m.getStockQuantity(),
                                m.getReorderLevel(),
                                m.getManufacturer(),
                                m.getExpiryDate(),
                                m.isLowStock() ? "LOW STOCK ALERT" : "OK"
                        });
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PharmacyPanel.this, "Error loading pharmacy stock: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void openAddMedicineDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Medicine Item", true);
        dialog.setSize(450, 420);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField txtName = new JTextField();
        JTextField txtCategory = new JTextField("Analgesic");
        JTextField txtPrice = new JTextField("10.00");
        JTextField txtStock = new JTextField("100");
        JTextField txtReorder = new JTextField("20");
        JTextField txtManufacturer = new JTextField("PharmaCorp");
        JTextField txtExpiry = new JTextField(LocalDate.now().plusYears(1).toString());

        panel.add(new JLabel("Medicine Name:")); panel.add(txtName);
        panel.add(new JLabel("Category:")); panel.add(txtCategory);
        panel.add(new JLabel("Unit Price ($):")); panel.add(txtPrice);
        panel.add(new JLabel("Stock Quantity:")); panel.add(txtStock);
        panel.add(new JLabel("Reorder Level:")); panel.add(txtReorder);
        panel.add(new JLabel("Manufacturer:")); panel.add(txtManufacturer);
        panel.add(new JLabel("Expiry Date (YYYY-MM-DD):")); panel.add(txtExpiry);

        JButton btnSave = new JButton("Save Medicine");
        btnSave.setBackground(new Color(41, 128, 185));
        btnSave.setForeground(Color.WHITE);

        btnSave.addActionListener(e -> {
            try {
                Medicine m = new Medicine();
                m.setName(txtName.getText().trim());
                m.setCategory(txtCategory.getText().trim());
                m.setUnitPrice(Double.parseDouble(txtPrice.getText().trim()));
                m.setStockQuantity(Integer.parseInt(txtStock.getText().trim()));
                m.setReorderLevel(Integer.parseInt(txtReorder.getText().trim()));
                m.setManufacturer(txtManufacturer.getText().trim());
                m.setExpiryDate(Date.valueOf(txtExpiry.getText().trim()));

                pharmacyService.addMedicine(m);
                JOptionPane.showMessageDialog(dialog, "Medicine added successfully!");
                dialog.dispose();
                loadMedicines();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Validation error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel());
        panel.add(btnSave);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openTransactionDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Stock Transaction (Dispense / Restock)", true);
        dialog.setSize(480, 380);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JComboBox<String> cbMedicine = new JComboBox<>();
        try {
            for (Medicine m : pharmacyService.getAllMedicines()) {
                cbMedicine.addItem(m.getMedicineId() + ": " + m.getName() + " (Stock: " + m.getStockQuantity() + ")");
            }
        } catch (Exception ex) {}

        JComboBox<String> cbType = new JComboBox<>(new String[]{"DISPENSE", "RESTOCK"});
        JTextField txtQty = new JTextField("10");

        JComboBox<String> cbPatient = new JComboBox<>();
        cbPatient.addItem("0: -- General / Restock (No Patient) --");
        try {
            for (Patient p : patientService.getAllPatients()) {
                cbPatient.addItem(p.getPatientId() + ": " + p.getFullName());
            }
        } catch (Exception ex) {}

        JTextField txtNotes = new JTextField();

        panel.add(new JLabel("Select Medicine:")); panel.add(cbMedicine);
        panel.add(new JLabel("Transaction Type:")); panel.add(cbType);
        panel.add(new JLabel("Quantity:")); panel.add(txtQty);
        panel.add(new JLabel("Patient (Optional for Dispense):")); panel.add(cbPatient);
        panel.add(new JLabel("Transaction Notes:")); panel.add(txtNotes);

        JButton btnProcess = new JButton("Process Transaction");
        btnProcess.setBackground(new Color(39, 174, 96));
        btnProcess.setForeground(Color.WHITE);

        btnProcess.addActionListener(e -> {
            try {
                int medId = Integer.parseInt(((String) cbMedicine.getSelectedItem()).split(":")[0]);
                String type = (String) cbType.getSelectedItem();
                int qty = Integer.parseInt(txtQty.getText().trim());

                int pId = Integer.parseInt(((String) cbPatient.getSelectedItem()).split(":")[0]);

                PharmacyTransaction tx = new PharmacyTransaction();
                tx.setMedicineId(medId);
                tx.setTransactionType(type);
                tx.setQuantity(qty);
                if (pId > 0) tx.setPatientId(pId);
                tx.setPerformedByUserId(AuthService.getCurrentUser() != null ? AuthService.getCurrentUser().getUserId() : 1);
                tx.setNotes(txtNotes.getText().trim());

                pharmacyService.processStockTransaction(tx);
                JOptionPane.showMessageDialog(dialog, "Stock transaction processed successfully!");
                dialog.dispose();
                loadMedicines();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Transaction Failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel());
        panel.add(btnProcess);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openTransactionHistoryDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Pharmacy Transaction History", true);
        dialog.setSize(750, 450);
        dialog.setLocationRelativeTo(this);

        String[] cols = {"ID", "Date", "Medicine", "Type", "Qty", "Total ($)", "Patient", "Performed By"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);

        try {
            List<PharmacyTransaction> txList = pharmacyService.getTransactionHistory();
            for (PharmacyTransaction tx : txList) {
                model.addRow(new Object[]{
                        tx.getTransactionId(),
                        tx.getTransactionDate(),
                        tx.getMedicineName(),
                        tx.getTransactionType(),
                        tx.getQuantity(),
                        String.format("%.2f", tx.getTotalPrice()),
                        tx.getPatientName() != null ? tx.getPatientName() : "-",
                        tx.getPerformedByUserName()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load transaction history: " + ex.getMessage());
        }

        dialog.add(new JScrollPane(table));
        dialog.setVisible(true);
    }
}
