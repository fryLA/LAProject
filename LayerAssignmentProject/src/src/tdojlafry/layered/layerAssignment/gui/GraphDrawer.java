package src.tdojlafry.layered.layerAssignment.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import src.tdojlafry.layered.layerAssignment.graphData.Edge;
import src.tdojlafry.layered.layerAssignment.graphData.MyGraph;
import src.tdojlafry.layered.layerAssignment.graphData.Node;

public class GraphDrawer extends JPanel {
    
    /**
     * 
     */
    private static final long serialVersionUID = 8786962247310354215L;
    private List<Node> currNodes;
    private List<Edge> currEdges;
    
    private List<Node> newlyAssignedNodes;
    private List<Edge> edgesToBeRemovedfromMiniGraph;
    
    private static final int PADDING = 20;
    
    private double nodeWidth;
    private double nodeHeight;
    
    double layerWidth;
    double layerHeight;
    
    private int layerCnt;
    
    private HashMap<Integer,List<Node>> layers = new HashMap<>();
    
    protected GraphDrawer(List<Node> nodes, List<Edge> edges, double width, double height, int layerCnt) {
        this.currNodes = nodes;
        this.currEdges = edges;
        this.nodeWidth = width;
        this.nodeHeight = height;
        this.layerCnt = layerCnt;
        
        newlyAssignedNodes = new ArrayList<Node>();
        edgesToBeRemovedfromMiniGraph = new ArrayList<Edge>();
    }
    
    @Override
    public Dimension getPreferredSize() {
       return new Dimension(150, 150);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // This is defined in the LayerAssignemnt class, where random coordinates are assigned.
        int widthOfMiniGraph = 100;
        int heightOfMiniGraph = 100;
        
        layerWidth = (this.getWidth() - 2 * PADDING) / currNodes.size();
        layerHeight = (this.getHeight() - heightOfMiniGraph) - 2 * PADDING;
        
        // MINI GRAPH
        for (Edge edge : currEdges) {
            Node n1 = edge.startNode;
            Node n2 = edge.endNode;
            Line2D line = new Line2D.Double(n1.getPosition().x, n1.getPosition().y, n2.getPosition().x, n2.getPosition().y);
            g2.setColor(Color.BLACK);
            g2.draw(line);
        }
        
        for (Node node : currNodes) {
            Rectangle2D rect = new Rectangle2D.Double(node.getPosition().x, node.getPosition().y, nodeWidth, nodeHeight);
            if (newlyAssignedNodes.contains(node)) {
                g2.setColor(Color.RED);
            } else {
                g2.setColor(Color.BLUE);
            }
            g2.fill(rect);
            g2.draw(rect);
        }
        
        
//        double cussPosX = node.getPosition().x;POSITIONEN ÄNDERN
//        double currPosY = node.getPosition().y;
//        
//        double destX = PADDING + (layerWidth / 2)+ node.getLayer() * layerWidth;
//        double destY = heightOfMiniGraph + node.getPosInlayer() * (PADDING + nodeHeight);

        // LAYER GRAPH
        for (int i = 0; i < layerCnt; i++) {
            layers.put(i, new ArrayList<Node>());
            
            Rectangle2D rect = new Rectangle2D.Double(PADDING + i * layerWidth, heightOfMiniGraph + PADDING, layerWidth, layerHeight);
            g2.setColor(Color.BLUE);
            g2.draw(rect);
        }
        
    }
    
    protected void update(MyGraph newGraph) {
        List<Node> nodes = newGraph.getNodes();
        List<Edge> edges = newGraph.getEdges();
        
        newlyAssignedNodes = new ArrayList<Node>();
        edgesToBeRemovedfromMiniGraph = new ArrayList<Edge>();
        
        for (int i = 0; i < nodes.size(); i++) {
            Node oldNode = currNodes.get(i);
            Node newNode = nodes.get(i);
            if (oldNode.getLayer() != newNode.getLayer()) {
                newlyAssignedNodes.add(newNode);
            }
        }
        
        this.currNodes = nodes;
        this.currEdges = edges;
        
        this.repaint();
    }

}
