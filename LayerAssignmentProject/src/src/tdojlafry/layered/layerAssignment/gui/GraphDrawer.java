package src.tdojlafry.layered.layerAssignment.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import src.tdojlafry.layered.layerAssignment.graphData.Edge;
import src.tdojlafry.layered.layerAssignment.graphData.SimpleGraph;
import src.tdojlafry.layered.layerAssignment.graphData.Node;
import src.tdojlafry.layered.layerAssignment.graphData.Position;

public class GraphDrawer extends JPanel implements ActionListener{
    
    public static double MINIGRAPH_AREA_DIVISOR = 3;

    /**
     * 
     */
    private static final long serialVersionUID = 8786962247310354215L;
    List<Node> currNodes;
    private List<Edge> currEdges;

    private List<Node> newlyAssignedNodes;
    private List<Edge> edgesToBeRemovedfromMiniGraph;

    static final int PADDING = 20;

    double layerWidth;
    double layerHeight;

    private int layerCnt;
    private HashMap<Integer, Rectangle2D> layers = new HashMap<>();
    private HashMap<Integer, Integer> nodesInLayer;
    
    public GNode gNodes[];
    private Timer animationTimer;
    private int actualWidth = 150;
    private int actualHeight = 150;
    
    private int visibleLayers = -1;

    protected GraphDrawer(List<Node> nodes, List<Edge> edges, int layerCnt, HashMap<Integer, Integer> nodesInLayer) {
        this.layerCnt = layerCnt;
        this.currEdges = edges;
        this.currNodes = nodes;
        
        this.nodesInLayer = nodesInLayer;
        
        newlyAssignedNodes = new ArrayList<Node>();
        edgesToBeRemovedfromMiniGraph = new ArrayList<Edge>();
        
        gNodes = new GNode[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            gNodes[i] = new GNode(node.position.x, node.position.y / MINIGRAPH_AREA_DIVISOR, this, PADDING);
        }
        
        animationTimer = new Timer(10, this);
//        animationTimer.start();

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(150, 150);
    }

    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.setBackground(Color.WHITE);
        

        int w = getWidth() - 2 * PADDING;
        int h = getHeight() - 2 * PADDING;
        
        if (w != actualWidth) {
            for (int i = 0; i < gNodes.length; i++) {
                gNodes[i].rescaleX(w);
            }
            actualWidth = w;
        }
        if (h != actualHeight) {
            for (int i = 0; i < gNodes.length; i++) {
                gNodes[i].rescaleY(h);
            }
            actualHeight = h;
        }

        layerWidth = (this.getWidth() - 2 * PADDING) / layerCnt;
        layerHeight = (this.getHeight() / 3) * 2 - 2 * PADDING;

        double startPosY = ((this.getHeight() - Node.DEFAULT_NODE_HEIGHT) / 3) + PADDING;
        layers.clear();
        
        // LAYERS
        for (int i = layerCnt -1; i >= 0 ; i--) {
            Rectangle2D rect = ShapeProvider.drawLayer(g, PADDING + i * layerWidth, startPosY, layerWidth, layerHeight, i <= visibleLayers);
            layers.put(i, rect);
            g2.draw(rect);
        }
        
        // EDGES
        for (Edge edge : currEdges) {
            ShapeProvider.drawArrow(g, edge, this);
        }
        
        // NODES
        for (int i = 0; i < gNodes.length; i++) {
            GNode node = gNodes[i];
            ShapeProvider.drawNode(g, node);
        }
    }


    protected void update(SimpleGraph newGraph) {
        int maxLayer = -1;
        
        for (int i = 0; i < gNodes.length; i++) {
            GNode gNode = gNodes[i];
            Node node = newGraph.getNodes().get(i);
            if (node.layer == -1) {
                gNode.setTargetPosition(node.position.x, node.position.y / MINIGRAPH_AREA_DIVISOR);
            } else {
                maxLayer = Math.max(maxLayer, node.getLayer());
                Rectangle2D layerRect = layers.get(node.layer);
                gNode.targetPosition.x = layerRect.getCenterX() - (Node.DEFAULT_NODE_WIDTH / 2) - PADDING;
                double d = (layerRect.getHeight() - (2 * PADDING)) / (nodesInLayer.get(node.layer) + 1);
                gNode.targetPosition.y = layerRect.getMinY() + ((node.posInlayer + 1) * d );                
            }
        }
        this.visibleLayers = maxLayer;
        animationTimer.start();

    }

    public void reset(List<Node> nodes, List<Edge> edges, int layerCnt) {

        visibleLayers = -1;
        for (int i = 0; i < gNodes.length; i++) {
            GNode gNode = gNodes[i];
            Node node = nodes.get(i);
                gNode.setTargetPosition(node.position.x, node.position.y / MINIGRAPH_AREA_DIVISOR);
        }
        animationTimer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        boolean repaint = false;
        for (int i = 0; i < gNodes.length; i++) {
            repaint = gNodes[i].update() || repaint;
        }
        
        if (repaint) {
            this.repaint();
        } else {
            animationTimer.stop();
        }
    }

    //

}
