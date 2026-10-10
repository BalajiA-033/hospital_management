package com.hospital.ui;

import com.hospital.model.User;
import com.hospital.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainDashboardFrame extends JFrame {

    private JTabbedPane tabbedPane;
    private DashboardPanel dashboardPanel;
    private PatientPanel patientPanel;
    private DoctorPanel doctorPanel;
    private AppointmentPanel appointmentPanel;
    private BedPanel bedPanel;
    private EmergencyPanel emergencyPanel;
    private PharmacyPanel pharmacyPanel;
    private ReportsPanel reportsPanel;

    private JLabel lblUserInfo;

    public MainDashboardFrame() {
        super("Smart Hospital Management System - DBMS Practical Case Study");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setMinimumSize(new Dimension(1024, 700));
        setLocationRelativeTo(null);

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(245, 247, 250));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setPreferredSize(new Dimension(1280, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblLogo = new JLabel("SMART HOSPITAL MANAGEMENT SYSTEM");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLogo.setForeground(Color.WHITE);
        headerPanel.add(lblLogo, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setOpaque(false);

        User curUser = AuthService.getCurrentUser();
        String userDisplay = curUser != null ? curUser.getFullName() + " (" + curUser.getRole() + ")" : "Administrator";
        lblUserInfo = new JLabel("User: " + userDisplay);
        lblUserInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUserInfo.setForeground(new Color(220, 230, 242));

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);

        btnLogout.addActionListener(e -> logout());

        userPanel.add(lblUserInfo);
        userPanel.add(btnLogout);
        headerPanel.add(userPanel, BorderLayout.EAST);

        mainContent.add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        dashboardPanel = new DashboardPanel();
        patientPanel = new PatientPanel();
        doctorPanel = new DoctorPanel();
        appointmentPanel = new AppointmentPanel();
        bedPanel = new BedPanel();
        emergencyPanel = new EmergencyPanel();
        pharmacyPanel = new PharmacyPanel();
        reportsPanel = new ReportsPanel();

        tabbedPane.addTab("Dashboard", dashboardPanel);
        tabbedPane.addTab("Patients", patientPanel);
        tabbedPane.addTab("Doctors", doctorPanel);
        tabbedPane.addTab("Appointments", appointmentPanel);
        tabbedPane.addTab("Beds Management", bedPanel);
        tabbedPane.addTab("Emergency Intake", emergencyPanel);
        tabbedPane.addTab("Pharmacy", pharmacyPanel);
        tabbedPane.addTab("Analytical Reports", reportsPanel);

        // Role-based restrictions if needed
        applyRoleRestrictions(curUser);

        mainContent.add(tabbedPane, BorderLayout.CENTER);
        add(mainContent);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dashboardPanel.stopAutoPolling();
            }
        });
    }

    private void applyRoleRestrictions(User user) {
        if (user != null && "STAFF".equals(user.getRole().name())) {
            // Staff restrictions example
            // tabbedPane.setEnabledAt(2, false); // Doctors edit
        }
    }

    private void logout() {
        dashboardPanel.stopAutoPolling();
        AuthService.logout();
        dispose();

        LoginDialog loginDialog = new LoginDialog(null);
        loginDialog.setVisible(true);
        if (loginDialog.isAuthenticated()) {
            new MainDashboardFrame().setVisible(true);
        } else {
            System.exit(0);
        }
    }
}
