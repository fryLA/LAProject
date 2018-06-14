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
import src.tdojlafry.layered.layerAssignment.graphData.Node;

public class GraphDrawer extends JPanel {
    
    private List<Node> nodes;
    private List<Edge> edges;
    
    private static final int PADDING = 20;
    
    private double width;
    private double height;
    
    double layerWidth;
    double layerHeight;
    
    private int layerCnt;
    
    private HashMap<Integer,List<Node>> layers = new HashMap<>();
    
    protected GraphDrawer(  List<Node> nodes, List<Edge> edges, double width, double height, int layerCnt) {
        this.nodes = nodes;
        this.edges = edges;
        this.width = width;
        this.height = height;
        this.layerCnt = layerCnt;
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
        
        for (Edge edge : edges) {
            Node n1 = edge.startNode;
            Node n2 = edge.endNode;
            Line2D line = new Line2D.Double(n1.getPosition().x, n1.getPosition().y, n2.getPosition().x, n2.getPosition().y);
            g2.setColor(Color.BLACK);
            g2.draw(line);
        }
        
        for (Node node : nodes) {
            Rectangle2D rect = new Rectangle2D.Double(node.getPosition().x, node.getPosition().y, width, height);
            g2.setColor(Color.RED);
            g2.fill(rect);
            g2.draw(rect);
        }
        
        layerWidth = this.getParent().getWidth() / nodes.size();
        layerHeight = this.getParent().getHeight() - 4 * PADDING;
        
        for (int i = 0; i < layerCnt; i++) {
            layers.put(i, new ArrayList<Node>());
            
            Rectangle2D rect = new Rectangle2D.Double(PADDING + i * layerWidth, PADDING, layerWidth,layerHeight);
            g2.setColor(Color.GREEN);
//            g2.fill(rect);
            g2.draw(rect);
        }
        
    }

}
