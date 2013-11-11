package ru.yaal.project.articlesaver.ui;

import org.apache.log4j.Level;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Properties;


/**
 * Главное окно UI.
 * User: Aleks
 * Date: 10.11.13
 */
public class GuiStart extends Thread {
    private final Properties props = new Properties();

    public static void main(String[] args) {
        //todo удалить метод
        new GuiStart().start();
    }

    @Override
    public void run() {
        final JFrame frame = new JFrame("Article Saver");
        Container contentPane = frame.getContentPane();
        BoxLayout layout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
        frame.setJMenuBar(prepareMenu());
        contentPane.setLayout(layout);
        contentPane.add(prepareTargetFolder());
        contentPane.add(prepareStatistics());
        contentPane.add(prepareLog());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    private JMenuBar prepareMenu() {
        JRadioButtonMenuItem traceLevelMenuItem = new JRadioButtonMenuItem("TRACE");
        JRadioButtonMenuItem infoLevelMenuItem = new JRadioButtonMenuItem("INFO", true);
        JRadioButtonMenuItem errorLevelMenuItem = new JRadioButtonMenuItem("ERROR");

        traceLevelMenuItem.setActionCommand(Level.TRACE.toString());
        infoLevelMenuItem.setActionCommand(Level.TRACE.toString());
        errorLevelMenuItem.setActionCommand(Level.TRACE.toString());

        ActionListener logLevelActionListener = new LogLevelActionListener(props);
        traceLevelMenuItem.addActionListener(logLevelActionListener);
        infoLevelMenuItem.addActionListener(logLevelActionListener);
        errorLevelMenuItem.addActionListener(logLevelActionListener);

        JMenu logLevelMenu = new JMenu("Log Level");
        logLevelMenu.add(traceLevelMenuItem);
        logLevelMenu.add(infoLevelMenuItem);
        logLevelMenu.add(errorLevelMenuItem);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ExitActionListener());

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(logLevelMenu);
        menuBar.add(exitMenuItem);
        return menuBar;
    }

    private JPanel prepareLog() {
        JTextArea logTextArea = new JTextArea();
        TitledBorder border = new TitledBorder("Log");

        JPanel panel = new JPanel();
        panel.add(logTextArea);
        panel.setEnabled(false);
        panel.setBorder(border);
        return panel;
    }

    private JPanel prepareStatistics() {
        TitledBorder border = new TitledBorder("Statistics");
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        panel.add(new StatisticsLabel("Wait for loading: "));
        panel.add(new StatisticsLabel("Is loading: "));
        panel.add(new StatisticsLabel("Has loaded: "));
        panel.setEnabled(false);
        panel.setBorder(border);
        return panel;
    }

    private JPanel prepareTargetFolder() {
        TitledBorder border = new TitledBorder("Target Folder");
        JTextField targetFolderField = new JTextField(20);
        JButton chooseFolderButton = new JButton("...");
        JPanel panel = new JPanel();
        panel.add(targetFolderField);
        panel.add(chooseFolderButton);
        panel.setBorder(border);
        return panel;
    }
}
