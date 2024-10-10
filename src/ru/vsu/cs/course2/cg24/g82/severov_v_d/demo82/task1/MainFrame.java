package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1;

import javax.swing.*;

public class MainFrame extends JDialog {
    private JPanel contentPane;
    private JPanel paintAreaPanel;
    private DrawPanel dp;

    public MainFrame() {
        setContentPane(contentPane);
        setModal(true);
        setResizable(false);
        dp = new DrawPanel();
        paintAreaPanel.add(dp);
    }

    public static void main(String[] args) {
        MainFrame dialog = new MainFrame();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
