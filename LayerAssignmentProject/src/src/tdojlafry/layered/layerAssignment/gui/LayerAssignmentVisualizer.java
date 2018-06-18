package src.tdojlafry.layered.layerAssignment.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import src.tdojlafry.layered.layerAssignment.graphData.Edge;
import src.tdojlafry.layered.layerAssignment.graphData.MyGraph;
import src.tdojlafry.layered.layerAssignment.graphData.Node;

public class LayerAssignmentVisualizer implements ActionListener {

    JFrame frame;

    JPanel rootPanel;

    JPanel layerPanel;

    Integer stepSize = 1;
    int currentStep = 0;
    JTextField stepSizeText;

    GraphDrawer gd;

    private JSlider stepSlider;

    private JButton resetButton;

    private JButton playButton;

    private JButton jumpFwd;

    private JButton jumpBck;

    private JButton pauseButton;

    private int layerCnt;

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
    
    private List<MyGraph> graphs;

    protected LayerAssignmentVisualizer( List<MyGraph> graphs) {
        
        layerCnt = graphs.size() - 1;
        
        
        if (layerCnt == 0) {
            JOptionPane.showMessageDialog(null, "No layers assigned " + layerCnt + ".",
                    "Error Massage", JOptionPane.ERROR_MESSAGE);
        }
        this.graphs = graphs;
        

    }

    public void createView(int width, int height) {

        // Create and set up the window
        frame = new JFrame("Layer Assignment");
        frame.setSize(width, height);
        frame.getRootPane().setSize(width, height);

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();

        int xPos = (dim.width / 2) - (frame.getWidth() / 2);
        int yPos = (dim.height / 2) - (frame.getHeight() / 2);

        frame.setLocation(xPos, yPos);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addPanels(frame);

        // frame.pack();
        frame.setVisible(true);
    }

    public void update() {

    }

    void initialize() {
        
        MyGraph initialDrawing = graphs.get(0);

        // Add not layouted Graph
        List<Node> nodes = initialDrawing.getNodes();
        List<Edge> edges = initialDrawing.getEdges();

        double width = 5.0;
        double height = 5.0;

        gd = new GraphDrawer(nodes, edges, width, height, layerCnt);
        Border blackline = BorderFactory.createLineBorder(Color.black);
        gd.setBorder(blackline);

        layerPanel.add(gd);

    }

    private void addPanels(JFrame dialog) {

        rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());
        rootPanel.setPreferredSize(new Dimension(450, 450));
        dialog.add(rootPanel);

        // Create the control toolbar
        JToolBar toolbar = new JToolBar("Control");
        addButtons(toolbar);
        rootPanel.add(toolbar, BorderLayout.PAGE_END);

        // Create graph panel
        layerPanel = new JPanel();
        GridLayout g = new GridLayout(1, 1);
        layerPanel.setLayout(g);
        layerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        rootPanel.add(layerPanel);

    }

    private void addButtons(JToolBar toolbar) {

        stepSlider = new JSlider(JSlider.HORIZONTAL, 0, layerCnt, 1);
        stepSlider.setToolTipText("Define step size");
        stepSlider.setMajorTickSpacing(5);
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
                String typed = stepSizeText.getText();
                if (typed.isEmpty()) {
                    return;
                } else if (!typed.matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "Error: Please enter number between 0 and " + layerCnt + ".",
                            "Error Massage", JOptionPane.ERROR_MESSAGE);

                    return;
                } else if (Integer.parseInt(typed) < 0 || Integer.parseInt(typed) > layerCnt) {
                    JOptionPane.showMessageDialog(null, "Error: Please enter number between 0 and " + layerCnt + ".",
                            "Error Massage", JOptionPane.ERROR_MESSAGE);
                    stepSizeText.setText(String.valueOf(stepSize));
                    return;
                }
                int value = Integer.parseInt(typed);
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
        
    }

    private JButton createButton(String location, String actionID, String tooltip, boolean enabled) {

        final int width = 20;
        final int height = 20;

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
            break;
        case PAUSE: 
            enableComponents(Arrays.asList(playButton, jumpBck, jumpFwd, resetButton, stepSizeText, stepSlider), true);
            enableComponents(Arrays.asList(pauseButton), false);
            break;
        case RESET: 
            enableComponents(Arrays.asList(playButton, jumpFwd, stepSizeText, stepSlider), true);
            enableComponents(Arrays.asList(pauseButton, resetButton, jumpBck), false);
            break;
        case FWD: 
            enableComponents(Arrays.asList(playButton, jumpBck, jumpFwd, resetButton, stepSizeText, stepSlider), true);
            enableComponents(Arrays.asList(pauseButton), false);
            if (currentStep + stepSize <= layerCnt) {
                currentStep += stepSize;
            } else {
                currentStep = layerCnt;
            }
            gd.update(graphs.get(currentStep));
            break;
        case BCK: 
            enableComponents(Arrays.asList(playButton, jumpBck, jumpFwd, resetButton, stepSizeText, stepSlider), true);
            enableComponents(Arrays.asList(pauseButton), false);
            if (currentStep - stepSize >= 0) {
                currentStep -= stepSize;
            } else {
                currentStep = 0;
            }
            gd.update(graphs.get(currentStep - stepSize));
            break;
        }

    }
    
    private void enableComponents(List<Component> components, boolean enablement) {
        for (Component comp : components) {
            comp.setEnabled(enablement);
        }
    }
    

}
