package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.practice1;

import javax.swing.*;

public class MainFrame extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel paintAreaPanel;
    private DrawPanelPractice dp;

    public MainFrame() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        dp = new DrawPanelPractice();
        paintAreaPanel.add(dp);
    }

    public static void main(String[] args) {
        MainFrame dialog = new MainFrame();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
