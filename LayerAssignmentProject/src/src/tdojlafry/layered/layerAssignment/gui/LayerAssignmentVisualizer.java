package src.tdojlafry.layered.layerAssignment.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import src.tdojlafry.layered.layerAssignment.graphData.Edge;
import src.tdojlafry.layered.layerAssignment.graphData.MyGraph;
import src.tdojlafry.layered.layerAssignment.graphData.Node;

public class LayerAssignmentVisualizer {

    JFrame frame;

    JSplitPane rootPanel;

    JPanel buttonPanel;

    JPanel layerPanel;

    int stepSize = 1;
    
    GraphDrawer gd;

    public void createView(int width, int height) {
        frame = new JFrame();
        frame.setSize(width, height);
        frame.getRootPane().setSize(width, height);

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();

        int xPos = (dim.width / 2) - (frame.getWidth() / 2);
        int yPos = (dim.height / 2) - (frame.getHeight() / 2);

        frame.setLocation(xPos, yPos);

        frame.setTitle("Layer Assignment");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addPanels(frame);

        addButtons();

        frame.setVisible(true);
    }

    public void update() {

    }

    void initialize(MyGraph initialDrawing, int layerCnt) {
        
        // Add not layouted Graph
        List<Node> nodes = initialDrawing.getNodes();
        List<Edge> edges = initialDrawing.getEdges();
        
        double width = 5.0;
        double height = 5.0;
        
        gd = new GraphDrawer(nodes, edges, width, height, layerCnt);
//        gd.setSize(gd.getPreferredSize().width, gd.getPreferredSize().height);
        // int x = (int) (layerPanel.getWidth() - gd.getPreferredSize().getWidth());
        // int y = (int) (layerPanel.getHeight() - gd.getPreferredSize().getHeight());
        // System.out.println(x + " : "+ y);
        // gd.setLocation(x, y);
        Border blackline = BorderFactory.createLineBorder(Color.black);
        gd.setBorder(blackline);
        
        layerPanel.add(gd);
        frame.setVisible(true);
        
    }

    private void addPanels(JFrame dialog) {

        rootPanel = new JSplitPane();
        rootPanel.setDividerLocation(dialog.getWidth() - 100);
        rootPanel.setResizeWeight(0.9);
        dialog.add(rootPanel);

        layerPanel = new JPanel();
        GridLayout g = new GridLayout(1, 1);
        layerPanel.setLayout(g);
        layerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        rootPanel.setLeftComponent(layerPanel);

        buttonPanel = new JPanel();
        JLabel buttonLabel = new JLabel("Control");
        buttonPanel.add(buttonLabel);

        GridLayout gl = new GridLayout(6, 1);
        gl.setHgap(20);
        gl.setVgap(20);
        buttonPanel.setLayout(gl);
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        rootPanel.setRightComponent(buttonPanel);

    }

    private void addButtons() {

        JButton playButton = new JButton("play");
        playButton.setContentAreaFilled(true);
        playButton.setToolTipText("Start execution");

        buttonPanel.add(playButton);

        JButton stopButton = new JButton("stop");
        stopButton.setContentAreaFilled(true);
        stopButton.setToolTipText("Stop execution");

        buttonPanel.add(stopButton);

        JButton pauseButton = new JButton("pause");
        pauseButton.setContentAreaFilled(true);
        pauseButton.setToolTipText("Pause execution");

        buttonPanel.add(pauseButton);

        JButton jumpFwd = new JButton("fwd");
        jumpFwd.setContentAreaFilled(true);
        jumpFwd.setToolTipText("Jump " + stepSize + " steps forward");

        buttonPanel.add(jumpFwd);

        JButton jumpBck = new JButton("bck");
        jumpBck.setContentAreaFilled(true);
        jumpBck.setToolTipText("Jump " + stepSize + " steps backward");

        buttonPanel.add(jumpBck);

    }

}
