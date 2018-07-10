package src.tdojlafry.layered.layerAssignment.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.eclipse.elk.core.util.Pair;
import org.eclipse.elk.graph.ElkNode;

import src.tdojlafry.layered.layerAssignment.graphData.SimpleGraph;

public class LayerAssignmentView extends JFrame {

    private JTabbedPane tabbedPane;

    private static int width = 500;
    private static int height = 500;
    
    static String reloadEvent = "reload file";
    
    JButton loadButton;
    ActionListener loadButtonActionListener;

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

    public void visualize(List<SimpleGraph> graphs, File file) {

        LayerAssignmentVisualizer visualizer = new LayerAssignmentVisualizer(graphs);
        
        TextPanel textPanel = new TextPanel(file.getPath());
        
        visualizer.createView();
        visualizer.initialize();
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textPanel, visualizer);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(Math.max(getWidth(), getPreferredSize().width) -  400);
        splitPane.setResizeWeight(0.8);
        tabbedPane.addTab(file.getName(), splitPane);

        tabbedPane.setSelectedComponent(splitPane);

    }

    private void addLoadFileComponents() {

        loadButton = new JButton("Load File");

        loadButton.setEnabled(true);
        loadButton.setPreferredSize(new Dimension(width, height / 10));
        loadButton.setSize(new Dimension(width, height / 10));
        
        loadButtonActionListener = new ActionListener() {
            
            public void actionPerformed(ActionEvent ae) {
                
                // Don't show the file chooser dialog in this case
                if (ae.getActionCommand() == reloadEvent) {
                    
                }
                // Relative path to application root!
                JFileChooser fileChooser = new JFileChooser("testGraphs");
                
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {

                    File selectedFile = fileChooser.getSelectedFile();
                    doVisualize(selectedFile);
                    
                }

            }
        };
        loadButton.addActionListener(loadButtonActionListener);

        getContentPane().add(loadButton, BorderLayout.AFTER_LAST_LINE);

    }
    
    private void doVisualize(File selectedFile) {
        ElkNode elkGraph = LayerAssignmentComputationManager.parseGraphToElkGraph(selectedFile.getPath()); //GuiTestDataCreator.createSimpleLayoutGraph(200);

        if (elkGraph != null) {
            Pair<Boolean, List<SimpleGraph>> computation =
                    LayerAssignmentComputationManager.computeNewInputs(elkGraph);
            if (computation.getFirst()) {
                visualize(computation.getSecond(), selectedFile);
            } else {
                JOptionPane.showMessageDialog(tabbedPane, null, "No valid input file.",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(tabbedPane, null, "Graph is not acyclic.",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private class TextPanel extends JPanel {
        
        String path = "";
        private String storeAllString="";
        private JButton saveButton = new JButton("Save");
        
        protected TextPanel(String p) {
            setLayout(new BorderLayout());
            path = p;
            fileRead();
            panels();
        }
        
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private JTextArea textArea = new JTextArea();
        
        private void fileRead(){
            try{    
              FileReader read = new FileReader(path);
              Scanner scan = new Scanner(read);
                 while(scan.hasNextLine()){
                  String temp=scan.nextLine()+"\n";
                  storeAllString=storeAllString+temp;
                 }
                 textArea.setText(storeAllString);
          }
            catch (Exception exception)
             {
             exception.printStackTrace();
             }
        } 
        
        private void panels(){        
            JPanel panel = new JPanel(new GridLayout(1,1));
            panel.setBorder(new EmptyBorder(5, 5, 5, 5));
           // JTextArea textArea = new JTextArea(storeAllString,0,70);
             JScrollPane scrollBarForTextArea=new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
             panel.add(scrollBarForTextArea); 
             add(panel, BorderLayout.PAGE_START);
            add(saveButton, BorderLayout.PAGE_END);
            
            JPanel p = this;
            
            saveButton.addActionListener(new ActionListener() {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveBtn();
                    tabbedPane.remove(p.getParent());
                    doVisualize(new File(path));
                    
                }
            });
        }
        
        private void saveBtn(){
            File file = null;
            FileWriter out=null;


            try {
                file = new File(path);
                out = new FileWriter(file);     
                out.write(textArea.getText());
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
      }
            

        
    }

}
