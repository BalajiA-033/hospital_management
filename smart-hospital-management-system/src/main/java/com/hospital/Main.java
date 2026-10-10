package com.hospital;

import com.hospital.config.DatabaseConfig;
import com.hospital.ui.LoginDialog;
import com.hospital.ui.MainDashboardFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Set Look and Feel (FlatLaf if available, otherwise System LAF)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default Java Look & Feel fallback
        }

        // Test database connectivity asynchronously / initialize
        try {
            DatabaseConfig.getConnection();
        } catch (Exception ex) {
            System.err.println("Database initialization notice: " + ex.getMessage());
        }

        // Launch Swing Application GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginDialog loginDialog = new LoginDialog(null);
            loginDialog.setVisible(true);

            if (loginDialog.isAuthenticated()) {
                MainDashboardFrame mainFrame = new MainDashboardFrame();
                mainFrame.setVisible(true);
            } else {
                System.out.println("Authentication cancelled or exited by user.");
                System.exit(0);
            }
        });
    }
}
