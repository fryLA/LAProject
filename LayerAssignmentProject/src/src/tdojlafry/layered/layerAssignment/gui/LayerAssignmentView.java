package src.tdojlafry.layered.layerAssignment.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import src.tdojlafry.layered.layerAssignment.graphData.SimpleGraph;

public class LayerAssignmentView extends JFrame {

    private JTabbedPane tabbedPane;

    private static int width = 500;
    private static int height = 500;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LayerAssignmentView() {

        // Create and set up the window
        this.setResizable(true);
        setSize(width, height);
        getRootPane().setSize(width, height);

        getContentPane().setLayout(new BorderLayout());

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();

        int xPos = (dim.width / 2) - (getWidth() / 2);
        int yPos = (dim.height / 2) - (getHeight() / 2);

        setLocation(xPos, yPos);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        addLoadFileComponents();

        setVisible(true);
    }

    public void visualize(List<SimpleGraph> graphs, String fileName) {

        LayerAssignmentVisualizer visualizer = new LayerAssignmentVisualizer(graphs);
        tabbedPane.addTab(fileName, visualizer);

        visualizer.createView();
        visualizer.initialize();

    }

    private void addLoadFileComponents() {

        JButton loadButton = new JButton("Load File");

        loadButton.setEnabled(true);
        loadButton.setPreferredSize(new Dimension(width, height / 10));
        loadButton.setSize(new Dimension(width, height / 10));
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // Relative path to application root!
                JFileChooser fileChooser = new JFileChooser("testGraphs");
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    List<SimpleGraph> graphs =
                            LayerAssignmentComputationManager.computeNewInputs(selectedFile.getPath());
                    visualize(graphs, selectedFile.getName());
                }
            }
        });

        getContentPane().add(loadButton, BorderLayout.AFTER_LAST_LINE);

    }

}
