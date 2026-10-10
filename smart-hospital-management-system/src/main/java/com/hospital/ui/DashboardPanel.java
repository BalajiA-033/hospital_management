package com.hospital.ui;

import com.hospital.config.DatabaseConfig;
import com.hospital.model.DashboardStats;
import com.hospital.service.DashboardService;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardPanel extends JPanel {

    private final DashboardService dashboardService;
    private Timer refreshTimer;

    private JLabel lblTotalPatients;
    private JLabel lblTotalDoctors;
    private JLabel lblTodaysAppointments;
    private JLabel lblAvailableBeds;
    private JLabel lblOccupiedBeds;
    private JLabel lblLowStockMedicines;
    private JLabel lblLastUpdated;
    private JLabel lblDbStatus;

    public DashboardPanel() {
        this.dashboardService = new DashboardService();
        initUI();
        startAutoPolling();
        refreshStats();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Status Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("HOSPITAL DASHBOARD OVERVIEW");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(24, 43, 73));

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightTop.setOpaque(false);

        lblDbStatus = new JLabel("DB Mode: " + (DatabaseConfig.isH2Fallback() ? "H2 In-Memory" : "MySQL 8.x"));
        lblDbStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDbStatus.setForeground(DatabaseConfig.isH2Fallback() ? new Color(180, 100, 0) : new Color(0, 120, 60));

        lblLastUpdated = new JLabel("Last Refreshed: Never");
        lblLastUpdated.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        JButton btnManualRefresh = new JButton("Refresh Now");
        btnManualRefresh.addActionListener(e -> refreshStats());

        rightTop.add(lblDbStatus);
        rightTop.add(lblLastUpdated);
        rightTop.add(btnManualRefresh);

        topBar.add(lblTitle, BorderLayout.WEST);
        topBar.add(rightTop, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Center Grid of KPI Cards
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        gridPanel.setOpaque(false);

        lblTotalPatients = createCardValueLabel();
        lblTotalDoctors = createCardValueLabel();
        lblTodaysAppointments = createCardValueLabel();
        lblAvailableBeds = createCardValueLabel();
        lblOccupiedBeds = createCardValueLabel();
        lblLowStockMedicines = createCardValueLabel();

        gridPanel.add(createKpiCard("Total Patients Registered", lblTotalPatients, new Color(41, 128, 185)));
        gridPanel.add(createKpiCard("Active Doctor Directory", lblTotalDoctors, new Color(39, 174, 96)));
        gridPanel.add(createKpiCard("Today's Appointments", lblTodaysAppointments, new Color(142, 68, 173)));
        gridPanel.add(createKpiCard("Available Beds", lblAvailableBeds, new Color(22, 160, 133)));
        gridPanel.add(createKpiCard("Occupied Beds", lblOccupiedBeds, new Color(211, 84, 0)));
        gridPanel.add(createKpiCard("Low Stock Medicine Alerts", lblLowStockMedicines, new Color(192, 57, 43)));

        add(gridPanel, BorderLayout.CENTER);

        // Footer Information
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setOpaque(false);
        JLabel lblInfo = new JLabel("Real-time Periodic DB Polling: Auto-refreshes every 5 seconds without GUI freezing.");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(100, 110, 120));
        footerPanel.add(lblInfo);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JLabel createCardValueLabel() {
        JLabel lbl = new JLabel("--", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lbl.setForeground(new Color(30, 30, 30));
        return lbl;
    }

    private JPanel createKpiCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(4, 0, 0, 0, accentColor),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(new Color(80, 90, 100));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    public void refreshStats() {
        lblDbStatus.setText("DB Mode: " + (DatabaseConfig.isH2Fallback() ? "H2 In-Memory" : "MySQL 8.x"));
        lblDbStatus.setForeground(DatabaseConfig.isH2Fallback() ? new Color(180, 100, 0) : new Color(0, 120, 60));

        SwingWorker<DashboardStats, Void> worker = new SwingWorker<>() {
            @Override
            protected DashboardStats doInBackground() throws Exception {
                return dashboardService.getStats();
            }

            @Override
            protected void done() {
                try {
                    DashboardStats stats = get();
                    lblTotalPatients.setText(String.valueOf(stats.getTotalPatients()));
                    lblTotalDoctors.setText(String.valueOf(stats.getTotalDoctors()));
                    lblTodaysAppointments.setText(String.valueOf(stats.getTodaysAppointments()));
                    lblAvailableBeds.setText(String.valueOf(stats.getAvailableBeds()));
                    lblOccupiedBeds.setText(String.valueOf(stats.getOccupiedBeds()));
                    lblLowStockMedicines.setText(String.valueOf(stats.getLowStockMedicines()));

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    lblLastUpdated.setText("Last Refreshed: " + sdf.format(new Date()));
                } catch (Exception ex) {
                    lblLastUpdated.setText("Refresh Error: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void startAutoPolling() {
        // Poll database every 5 seconds asynchronously
        refreshTimer = new Timer(5000, e -> refreshStats());
        refreshTimer.start();
    }

    public void stopAutoPolling() {
        if (refreshTimer != null && refreshTimer.isRunning()) {
            refreshTimer.stop();
        }
    }
}
