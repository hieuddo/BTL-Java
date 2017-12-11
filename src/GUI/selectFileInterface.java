package GUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import AnalyzeData.*;
import DataStore.ObjectManagement;

public class selectFileInterface extends JFrame {
    private JPanel panel;
    private JTextField path;
    private JButton draw;
    private JButton addFile;
    private JButton addFolder;
    private JButton tips;
    private ObjectManagement manager;

    private void initData() {
        panel = new JPanel();
        path = new JTextField("");
        draw = new JButton("Draw");
        addFile = new JButton("Add Java File");
        addFolder = new JButton("Add Java Project");
        tips = new JButton("Tips");
        manager = new ObjectManagement();
    }
    public selectFileInterface() {
        super("Choose java file or folder to analyze");
        initData();

        path.setBounds(50, 70, 300, 50);

        draw.setBounds(380, 70, 80, 50);
        draw.setMnemonic(KeyEvent.VK_D);

        addFile.setBounds(60, 150, 120, 50);
        addFile.setMnemonic(KeyEvent.VK_F);

        addFolder.setBounds(210, 150, 130, 50);
        addFolder.setMnemonic(KeyEvent.VK_L);

        tips.setBounds(370, 150, 50, 50);
        tips.setMnemonic(KeyEvent.VK_T);

        // event
        draw.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!isInvalidPath()) {
                            manager.initRela();
                            new Draw(manager);
                        }
                    }
                }
        );
        addFile.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser chooser = new JFileChooser();
                        chooser.setFileFilter(new FileNameExtensionFilter("Java", "java"));

                        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                            try {
                                readNewFile(chooser.getSelectedFile());
                            } catch (Exception ie) {
                                ie.printStackTrace();
                            }
                            path.setText(chooser.getSelectedFile().getAbsolutePath());
                        } else {
                            path.setText("No selection!");
                        }
                    }
                }
        );
        addFolder.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser chooser = new JFileChooser();
                        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                            path.setText(chooser.getSelectedFile().getAbsolutePath());
                            ScanFiles(chooser.getSelectedFile());
                        } else {
                            path.setText("No selection!");
                        }
                    }
                }
        );
        tips.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String tip = "Press +/I to zoom in\nPress -/O to zoom out\nPrintScreen to save image.";
                        JOptionPane.showMessageDialog(null, tip, "Tips", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
        );

        // Build panel
        panel.setLayout(null);
        panel.add(path);
        panel.add(draw);
        panel.add(addFile);
        panel.add(addFolder);
        panel.add(tips);

        this.add(panel);

        this.setSize(500, 300);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void readNewFile(File file) {
        try {
            StringParsing newStringParsing = new StringParsing();
            newStringParsing.readFile(file);
            newStringParsing.parsing();
            while (newStringParsing.isEmpty() == false) {
                manager.addObject(new ObjectParsing(newStringParsing.getParsedCode()).getClassFile());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ScanFiles(File file) {
        if ( file.isDirectory() ) {
            for (File subfile : file.listFiles() ) {
                ScanFiles(subfile);
            }
        } else if ( file.getName().substring(file.getName().lastIndexOf(".") + 1).equals("java") ) {
            readNewFile(file);
        }
    }
    boolean isInvalidPath() {
        return (path.getText().equals("No selection!")
                || path.getText().equals(""));
    }
}