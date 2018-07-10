package src.tdojlafry.layered.layerAssignment.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * This panel only exists to display which phase of the algorithm is about to be visualized.
 *
 */
public class PhaseDisplayer extends JPanel{
    
    JLabel phase;
    int layerCnt;
    private final static String initialText = "Layer Assignment visualized. Please press play or step forward.";

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    protected PhaseDisplayer(int layerCnt) {
        this.layerCnt = layerCnt;
        phase = new JLabel(initialText);
        this.add(phase);
        
    }
    
    protected void changeText(int step) {
        
        if (step == 0) {
            setInitalText();
        } else if (step <= layerCnt) {
            this.phase.setText("Applying all nodes which are sources up to layer " + step + ".. ");
        } else {
            this.phase.setText("Applying dummy nodes for long edges.");
        }
        
        this.repaint();
    }
    
    private void setInitalText() {
        this.phase.setText(initialText);
        this.repaint();
    }

}
