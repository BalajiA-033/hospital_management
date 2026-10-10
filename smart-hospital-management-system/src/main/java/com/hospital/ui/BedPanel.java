package com.hospital.ui;

import com.hospital.model.BedAllocation;
import com.hospital.model.HospitalBed;
import com.hospital.model.Patient;
import com.hospital.service.BedService;
import com.hospital.service.PatientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BedPanel extends JPanel {

    private final BedService bedService;
    private final PatientService patientService;

    private JTable bedTable;
    private DefaultTableModel tableModel;
    private JButton btnRefresh, btnAllocate, btnRelease;
    private List<HospitalBed> bedsList;
    private List<BedAllocation> activeAllocations;
    private Timer autoRefreshTimer;

    public BedPanel() {
        this.bedService = new BedService();
        this.patientService = new PatientService();
        initUI();
        loadBeds();
        startAutoRefresh();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("HOSPITAL BED AVAILABILITY & ALLOCATION");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(24, 43, 73));
        topBar.add(lblTitle, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        btnRefresh = new JButton("Refresh");
        btnAllocate = new JButton("Allocate Bed");
        btnAllocate.setBackground(new Color(22, 160, 133));
        btnAllocate.setForeground(Color.WHITE);

        btnRelease = new JButton("Release Bed");

        btnPanel.add(btnRefresh);
        btnPanel.add(btnAllocate);
        btnPanel.add(btnRelease);
        topBar.add(btnPanel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // Table
        String[] columns = {"Bed ID", "Bed Number", "Ward Name", "Bed Type", "Daily Charge ($)", "Status", "Occupied By Patient"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        bedTable = new JTable(tableModel);
        bedTable.setRowHeight(25);
        bedTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bedTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        add(new JScrollPane(bedTable), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadBeds());
        btnAllocate.addActionListener(e -> openAllocateDialog());
        btnRelease.addActionListener(e -> releaseSelectedBed());
    }

    public void loadBeds() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                bedsList = bedService.getAllBeds();
                activeAllocations = bedService.getActiveAllocations();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    tableModel.setRowCount(0);
                    for (HospitalBed b : bedsList) {
                        String patientInfo = "-";
                        if (b.isOccupied()) {
                            for (BedAllocation ba : activeAllocations) {
                                if (ba.getBedId() == b.getBedId()) {
                                    patientInfo = ba.getPatientName();
                                    break;
                                }
                            }
                        }
                        tableModel.addRow(new Object[]{
                                b.getBedId(),
                                b.getBedNumber(),
                                b.getWardName(),
                                b.getBedType(),
                                String.format("%.2f", b.getDailyCharge()),
                                b.isOccupied() ? "OCCUPIED" : "AVAILABLE",
                                patientInfo
                        });
                    }
                } catch (Exception ex) {
                    // Suppress alerts on background periodic poll errors
                }
            }
        };
        worker.execute();
    }

    private void openAllocateDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Allocate Bed to Patient", true);
        dialog.setSize(450, 320);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JComboBox<String> cbBed = new JComboBox<>();
        JComboBox<String> cbPatient = new JComboBox<>();
        JTextField txtNotes = new JTextField();

        try {
            for (HospitalBed b : bedService.getAllBeds()) {
                if (!b.isOccupied()) {
                    cbBed.addItem(b.getBedId() + ": " + b.getBedNumber() + " (" + b.getWardName() + " - " + b.getBedType() + ")");
                }
            }
            for (Patient p : patientService.getAllPatients()) {
                cbPatient.addItem(p.getPatientId() + ": " + p.getFullName());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load beds/patients: " + ex.getMessage());
            return;
        }

        if (cbBed.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No available beds present in inventory!");
            return;
        }

        panel.add(new JLabel("Select Available Bed:")); panel.add(cbBed);
        panel.add(new JLabel("Select Patient:")); panel.add(cbPatient);
        panel.add(new JLabel("Allocation Notes:")); panel.add(txtNotes);

        JButton btnSubmit = new JButton("Allocate");
        btnSubmit.setBackground(new Color(22, 160, 133));
        btnSubmit.setForeground(Color.WHITE);

        btnSubmit.addActionListener(e -> {
            try {
                int bedId = Integer.parseInt(((String) cbBed.getSelectedItem()).split(":")[0]);
                int patientId = Integer.parseInt(((String) cbPatient.getSelectedItem()).split(":")[0]);

                bedService.allocateBed(bedId, patientId, txtNotes.getText().trim());
                JOptionPane.showMessageDialog(dialog, "Bed allocated successfully!");
                dialog.dispose();
                loadBeds();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Allocation Failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel());
        panel.add(btnSubmit);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void releaseSelectedBed() {
        int row = bedTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an occupied bed row to release.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        HospitalBed bed = bedsList.get(row);
        if (!bed.isOccupied()) {
            JOptionPane.showMessageDialog(this, "Bed " + bed.getBedNumber() + " is already available!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        BedAllocation activeAlloc = null;
        for (BedAllocation ba : activeAllocations) {
            if (ba.getBedId() == bed.getBedId()) {
                activeAlloc = ba;
                break;
            }
        }

        if (activeAlloc == null) {
            JOptionPane.showMessageDialog(this, "No active allocation record found for bed.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Release bed " + bed.getBedNumber() + " (Patient: " + activeAlloc.getPatientName() + ")?", "Release Bed", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                bedService.releaseBed(activeAlloc.getAllocationId(), bed.getBedId());
                JOptionPane.showMessageDialog(this, "Bed released successfully!");
                loadBeds();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Release failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void startAutoRefresh() {
        autoRefreshTimer = new Timer(5000, e -> loadBeds());
        autoRefreshTimer.start();
    }
}
