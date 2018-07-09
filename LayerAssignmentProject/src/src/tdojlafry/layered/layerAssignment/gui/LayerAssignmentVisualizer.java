package src.tdojlafry.layered.layerAssignment.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import src.tdojlafry.layered.layerAssignment.graphData.Edge;
import src.tdojlafry.layered.layerAssignment.graphData.Node;
import src.tdojlafry.layered.layerAssignment.graphData.SimpleGraph;

public class LayerAssignmentVisualizer extends JPanel implements ActionListener {

    // JFrame frame;

    JPanel layerPanel;

    Integer stepSize = 1;
    int currentStep = 0;
    JTextField stepSizeText;

    GraphDrawer gd;

    private JSlider stepSlider;

    private JSlider speedSlider;

    private JButton resetButton;

    private JButton playButton;

    private JButton jumpFwd;

    private JButton jumpBck;

    private JButton pauseButton;
    
    private JLabel speedLabel;
    
    private JLabel stepLabel;
    
    private JLabel nodeSizeLabel;
    
    private JSlider nodeSizeSlider;
    
    private JToolBar toolbar;

    private int layerCnt;

    ArrayList<JComponent> components;

    private static final int toolbarHeight = 100;
    
    private static final int tooblarPadding = 4;

    // Button IDs
    private static final String PLAY = "play";
    private static final String PAUSE = "pause";
    private static final String FWD = "fwd";
    private static final String BCK = "bck";
    private static final String RESET = "reset";

    // for button enablements and stuff
    private boolean initial = true; // enabled: play, fwd, bck. disabled: pause, reset.
    private boolean paused = false; // enabled: play, fwd, bck, reset. disabled: pause.
    private boolean active = false; // enabled: pause. disabled: play, fwd, bck, reset.

    private List<SimpleGraph> graphs;

    protected LayerAssignmentVisualizer(List<SimpleGraph> graphs) {

        layerCnt = computeLayerCount(graphs);

        if (layerCnt == 0) {
            JOptionPane.showMessageDialog(null, "No layers assigned " + layerCnt + ".", "Error Massage",
                    JOptionPane.ERROR_MESSAGE);
        }
        this.graphs = graphs;
        
        addComponentListener(new ComponentListener() {
            
            @Override
            public void componentShown(ComponentEvent e) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void componentResized(ComponentEvent e) {
                rescaleComponents();
                
            }
            
            @Override
            public void componentMoved(ComponentEvent e) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void componentHidden(ComponentEvent e) {
                // TODO Auto-generated method stub
                
            }
        });

    }

    private int computeLayerCount(List<SimpleGraph> graphs2) {
        int cnt = 0;
        for (SimpleGraph graph : graphs2) {
            if (!graph.isDummyNodeGraph) {
                cnt++;
            }
        }
        return cnt - 1;
    }

    public void createView() {

        // Create and set up the window
        // frame = new JFrame("Layer Assignment");
        // frame.setSize(width, height);
        // frame.getRootPane().setSize(width, height);

        // Toolkit tk = Toolkit.getDefaultToolkit();
        // Dimension dim = tk.getScreenSize();

        // int xPos = (dim.width / 2) - (frame.getWidth() / 2);
        // int yPos = (dim.height / 2) - (frame.getHeight() / 2);
        //
        // frame.setLocation(xPos, yPos);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //
        // addPanels(frame);
        addPanels();

        // frame.pack();
        // frame.setVisible(true);
    }

    public void update() {

    }

    void initialize() {

        SimpleGraph initialDrawing = graphs.get(0);

        // Add not layouted Graph
        List<Node> nodes = initialDrawing.getNodes();
        List<Edge> edges = initialDrawing.getEdges();

        for (Node node : graphs.get(graphs.size() - 1).getNodes()) {
            if (node.isDummy) {
                nodes.add(node);
            }
        }

        // Remember how may nodes are stored in specified layer - need this for drawing.
        HashMap<Integer, Integer> nodesInLayer = new HashMap<>();
        SimpleGraph finalGraph = graphs.get(graphs.size() - 1);

        for (Node node : finalGraph.getNodes()) {
            int layer = node.getLayer();
            if (nodesInLayer.containsKey(layer)) {
                int currNodesInLayer = nodesInLayer.get(layer);
                nodesInLayer.replace(layer, currNodesInLayer + 1);
            } else {
                nodesInLayer.put(layer, 1);
            }
        }

        gd = new GraphDrawer(nodes, edges, layerCnt, nodesInLayer);

        Border blackline = BorderFactory.createLineBorder(Color.black);
        gd.setBorder(blackline);
        layerPanel.add(gd);
        this.setVisible(true);
    }

    private void addPanels() {

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 400));

        // Create the control toolbar
        toolbar = new JToolBar("Control");
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT, tooblarPadding, tooblarPadding));
        addButtons(toolbar);
        add(toolbar, BorderLayout.PAGE_END);

        // Create graph panel
        layerPanel = new JPanel();
        GridLayout g = new GridLayout(1, 1);
        layerPanel.setLayout(g);
        layerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(layerPanel);
    }

    private void addButtons(JToolBar toolbar) {

        stepLabel = new JLabel("Step size:");
        toolbar.add(stepLabel);

        stepSlider = new JSlider(JSlider.HORIZONTAL, 0, graphs.size() - 1, 1);
        stepSlider.setToolTipText("Define step size");
        stepSlider.setMajorTickSpacing(Math.min(25, Math.max(1, graphs.size() / 4)));
        stepSlider.setMinorTickSpacing(1);
        stepSlider.createStandardLabels(1);
        stepSlider.setPaintTicks(true);
        stepSlider.setPaintLabels(true);
        stepSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {

                Integer step = (int) stepSlider.getValue();
                stepSize = step;
                stepSizeText.setText(String.valueOf(step));
            }
        });

        stepSizeText = new JTextField(new Integer(1).toString(), 1);
        stepSizeText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                String input = stepSizeText.getText();
                if (input.isEmpty()) {
                    return;
                } else if (!input.matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "Error: Please enter number between 0 and " + (graphs.size()-1) + ".",
                            "Error Massage", JOptionPane.ERROR_MESSAGE);

                    return;
                } else if (Integer.parseInt(input) < 0 || Integer.parseInt(input) > layerCnt) {
                    JOptionPane.showMessageDialog(null, "Error: Please enter number between 0 and " + (graphs.size()-1) + ".",
                            "Error Massage", JOptionPane.ERROR_MESSAGE);
                    stepSizeText.setText(String.valueOf(stepSize));
                    return;
                }
                int value = Integer.parseInt(input);
                stepSlider.setValue(value);
            }
        });

        toolbar.add(stepSlider);
        toolbar.add(stepSizeText);

        playButton = createButton("play_en.png", PLAY, "Start/Resume execution", true);
        toolbar.add(playButton);

        pauseButton = createButton("pause_en.png", PAUSE, "Pause execution", false);
        toolbar.add(pauseButton);

        resetButton = createButton("reset_en.png", RESET, "Reset execution", false);
        toolbar.add(resetButton);

        jumpFwd = createButton("fwd_en.png", FWD, "Jump forward", true);
        toolbar.add(jumpFwd);

        jumpBck = createButton("bck_en.png", BCK, "Jump backward", false);
        toolbar.add(jumpBck);

        speedLabel = new JLabel("Speed:");
        toolbar.add(speedLabel);
        speedSlider = new JSlider(JSlider.HORIZONTAL, 10, 60, 60);
        speedSlider.setToolTipText("Set acceleration of animation");
        speedSlider.setMajorTickSpacing(20);
        speedSlider.setMinorTickSpacing(10);
        speedSlider.createStandardLabels(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {

                double speed =  speedSlider.getValue();
                GNode.moveSteps = 70 - speed;
            }
        });
        
        toolbar.add(speedSlider);
        
        nodeSizeLabel = new JLabel("Size:");
        toolbar.add(nodeSizeLabel);
        nodeSizeSlider = new JSlider(JSlider.HORIZONTAL, 20, 60, 20);
        nodeSizeSlider.setToolTipText("Set the node size");
        nodeSizeSlider.setMajorTickSpacing(20);
        nodeSizeSlider.setMinorTickSpacing(10);
        nodeSizeSlider.createStandardLabels(1);
        nodeSizeSlider.setPaintTicks(true);
        nodeSizeSlider.setPaintLabels(true);
        nodeSizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {

                int size =  nodeSizeSlider.getValue();
                GNode.node_height = size;
                GNode.node_widht = size;
                gd.update();
            }
        });
        toolbar.add(nodeSizeSlider);
        
        components = new ArrayList<>(Arrays.asList(stepSlider, speedSlider,
                resetButton, playButton, jumpBck, jumpFwd, pauseButton, speedLabel, stepLabel, nodeSizeLabel, nodeSizeSlider, toolbar));
        
        rescaleComponents();

    }

    private JButton createButton(String location, String actionID, String tooltip, boolean enabled) {

        final int width = 20;
        final int height = 20;

        // Relative path to application root!
        ImageIcon icon = getScaledIcon("icons/" + location, width, height);
        JButton button = new JButton(icon);
        button.setActionCommand(actionID);
        button.setToolTipText(tooltip);
        button.setEnabled(enabled);
        button.addActionListener(this);

        return button;

    }

    private ImageIcon getScaledIcon(String location, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(location);
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {
        case PLAY:
            enableComponents(Arrays.asList(playButton, jumpBck, jumpFwd, resetButton, stepSizeText, stepSlider), false);
            enableComponents(Arrays.asList(pauseButton), true);
            paused = false;

            new Thread() {

                @Override
                public void run() {
                    while (!paused && currentStep < graphs.size()) {

                        if (currentStep + stepSize < graphs.size()) {
                            currentStep += stepSize;
                        } else {
                            enableComponents(Arrays.asList(jumpBck, resetButton, stepSizeText, stepSlider), true);
                            enableComponents(Arrays.asList(playButton, jumpFwd, pauseButton), false);
                            currentStep = graphs.size() - 1;
                            paused = true;
                        }
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                gd.update(graphs.get(currentStep));

                            }
                        });
                        try {
                            // The next layer(s) shall only be filled after the animation of the last layer(s) is finished.
                            Thread.sleep(100);
                            while(gd.animationTimer.isRunning()) {
                                
                                Thread.sleep(10);
                            }
                            Thread.sleep((long)(300));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            break;
        case PAUSE:
            enableComponents(Arrays.asList(playButton, jumpBck, jumpFwd, resetButton, stepSizeText, stepSlider), true);
            enableComponents(Arrays.asList(pauseButton), false);

            paused = true;
            break;
        case RESET:
            enableComponents(Arrays.asList(playButton, jumpFwd, stepSizeText, stepSlider), true);
            enableComponents(Arrays.asList(pauseButton, resetButton, jumpBck), false);
            currentStep = 0;
            SimpleGraph initialDrawing = graphs.get(0);

            List<Node> nodes = initialDrawing.getNodes();
            List<Edge> edges = initialDrawing.getEdges();

            for (Node node : graphs.get(graphs.size() - 1).getNodes()) {
                if (node.isDummy) {
                    nodes.add(node);
                }
            }

            gd.reset(nodes, edges, graphs.size() - 1);
            break;
        case FWD:
            enableComponents(Arrays.asList(playButton, jumpBck, jumpFwd, resetButton, stepSizeText, stepSlider), true);
            enableComponents(Arrays.asList(pauseButton), false);

            if (currentStep + stepSize < graphs.size()) {
                currentStep += stepSize;
            } else {
                enableComponents(Arrays.asList(jumpBck, resetButton, stepSizeText, stepSlider), true);
                enableComponents(Arrays.asList(playButton, jumpFwd, pauseButton), false);
                currentStep = graphs.size() - 1;
            }
            gd.update(graphs.get(currentStep));
            break;
        case BCK:
            enableComponents(Arrays.asList(playButton, jumpBck, jumpFwd, resetButton, stepSizeText, stepSlider), true);
            enableComponents(Arrays.asList(pauseButton), false);
            gd.update(graphs.get(Math.max(currentStep - stepSize, 0)));
            currentStep = Math.max(currentStep - stepSize, 0);
            break;
        }

    }

    private void enableComponents(List<Component> components, boolean enablement) {
        for (Component comp : components) {
            comp.setEnabled(enablement);
        }
    }

    private static final int toolbarDivisor = 20;
    private static final int toolbarRows = 2;

    private Dimension setDimensionOfToolbarComponent(int multX, int multY) {
        return new Dimension((this.getPreferredSize().width / toolbarDivisor) * multX,
                ((toolbarHeight + 2 * tooblarPadding) / toolbarRows) * multY);
    }
    
    private void rescaleComponents() {
        for (JComponent comp : components) {
            if (comp != null) {
                if (comp instanceof JSlider) {
                    comp.setPreferredSize(setDimensionOfToolbarComponent(5, 1));
                } else if (comp instanceof JLabel) {
                    comp.setPreferredSize(setDimensionOfToolbarComponent(3, 1));
                } else if (comp instanceof JToolBar){
                    comp.setPreferredSize(setDimensionOfToolbarComponent(toolbarDivisor, toolbarRows));
                }
                else {
                    comp.setPreferredSize(setDimensionOfToolbarComponent(2, 1));
                }
                comp.repaint();
            }
        }
        
    }

}
