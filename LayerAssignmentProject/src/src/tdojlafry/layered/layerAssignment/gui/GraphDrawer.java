package src.tdojlafry.layered.layerAssignment.gui;

import java.awt.Color;
import java.awt.Dimension;
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
import src.tdojlafry.layered.layerAssignment.graphData.MyGraph;
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

    protected GraphDrawer(List<Node> nodes, List<Edge> edges, int layerCnt, HashMap<Integer, Integer> nodesInLayer) {
        this.currNodes = nodes;
        this.currEdges = edges;
        this.layerCnt = layerCnt;
        
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

        // LAYER
        int startPos = ((this.getHeight() - Node.DEFAULT_NODE_HEIGHT) / 3) + PADDING;

        layers.clear();
        for (int i = 0; i < layerCnt; i++) {

            Rectangle2D rect = new Rectangle2D.Double(PADDING + i * layerWidth, startPos, layerWidth, layerHeight);

            layers.put(i, rect);
            g2.setColor(Color.BLUE);
            g2.draw(rect);
        }
        
        
        // EDGES
        for (Edge edge : currEdges) {
            g.setColor(Color.RED);
            if (!edgesToBeRemovedfromMiniGraph.contains(edge)) {
                if (edge.startNode.layer >= 0) {
                    ShapeProvider.drawArrow(g, edge, true, this);
                } else {
                    ShapeProvider.drawArrow(g, edge, false, this);
                }
            }
        }
        
        g2.setColor(Color.BLUE);
        for (int i = 0; i < gNodes.length; i++) {
            GNode node = gNodes[i];
            Rectangle2D rect = new Rectangle2D.Double(node.currentPosition.x + PADDING, node.currentPosition.y + PADDING, 
                    GNode.NODE_WIDTH, GNode.NODE_HEIGHT);
            g2.fill(rect);
            g2.draw(rect);
        }
    }

//    private void refreshNodeInLayerPosition(Node node) {
//
//        Rectangle2D layerRect = layers.get(node.layer);
//
//        node.layeredPostition.x = layerRect.getCenterX() - (Node.DEFAULT_NODE_WIDTH / 2);
//
//        double d = (layerRect.getHeight() - (2 * PADDING)) / (nodesInLayer.get(node.layer));
//
//        node.layeredPostition.y = layerRect.getMinY() + PADDING + (node.posInlayer * (d + 1));
//        System.out.println(node.posInlayer);
//        System.out.println("LAYERED POS: " + node.layeredPostition.x + " : " + node.layeredPostition.y);
//    }

    protected void update(MyGraph newGraph) {
        List<Node> nodes = newGraph.getNodes();
        List<Edge> edges = newGraph.getEdges();
        
        for (int i = 0; i < gNodes.length; i++) {
            GNode gNode = gNodes[i];
            Node node = newGraph.getNodes().get(i);
            if (node.layer == -1) {
                gNode.setTargetPosition(node.position.x, node.position.y / MINIGRAPH_AREA_DIVISOR);
            } else {
                Rectangle2D layerRect = layers.get(node.layer);
                gNode.targetPosition.x = layerRect.getCenterX() - (Node.DEFAULT_NODE_WIDTH / 2) - PADDING;
                double d = (layerRect.getHeight() - (2 * PADDING)) / (nodesInLayer.get(node.layer) + 1);
                gNode.targetPosition.y = layerRect.getMinY() + ((node.posInlayer + 1) * d );                
            }
        }
        animationTimer.start();

//        newlyAssignedNodes = new ArrayList<Node>();
//        edgesToBeRemovedfromMiniGraph = new ArrayList<Edge>();
//
//        for (int i = 0; i < nodes.size(); i++) {
//            Node oldNode = currNodes.get(i);
//            Node newNode = nodes.get(i);
//            if (oldNode.getLayer() != newNode.getLayer()) {
//                newlyAssignedNodes.add(newNode);
//            } else if (newNode.layer >= 0) {
//                newNode.currentPosition.x = oldNode.layeredPostition.x;
//                newNode.currentPosition.y = oldNode.layeredPostition.y;
//            }
//        }
//
//        for (Edge edge : edges) {
//            if (newlyAssignedNodes.contains(edge.startNode)) {
//                edgesToBeRemovedfromMiniGraph.add(edge);
//            }
//        }
//
//        this.currNodes = nodes;
//        this.currEdges = edges;
//
//        // This is defined in the LayerAssignemnt class, where random coordinates are assigned.
//        double widthOfMiniGraph = 100;
//        double heightOfMiniGraph = 100;
//
//        double minigraphXMultiplier = (this.getWidth() - 2 * Node.DEFAULT_NODE_WIDTH) / widthOfMiniGraph;
//        double miniGraphYMultiplier = ((this.getHeight() - 2 * Node.DEFAULT_NODE_HEIGHT) / 3) / heightOfMiniGraph;
//
//        for (Node node : newlyAssignedNodes) {
//            node.refreshScaledPosition(minigraphXMultiplier, miniGraphYMultiplier);
//            node.currentPosition.x = node.scaledPosition.x;
//            node.currentPosition.y = node.scaledPosition.y;
//
//            refreshNodeInLayerPosition(node);
//            double dx = node.layeredPostition.x - node.scaledPosition.x;
//            double dy = node.layeredPostition.y - node.scaledPosition.y;
//
//            double angle = Math.atan2(dy, dx);
//
//            double d = Math.sqrt(dx * dx + dy * dy);
//            double acc = d / 50;
//            GraphDrawer gd = this;
//
//            Timer t = new Timer(10, new ActionListener() {
//
//                @Override
//                public void actionPerformed(ActionEvent e) {
//
//                    node.currentPosition.x += acc * Math.cos(angle);
//                    node.currentPosition.y += acc * Math.sin(angle);
//                    gd.repaint();
//
//                    double nextDX = node.layeredPostition.x - (node.currentPosition.x + acc * Math.cos(angle));
//                    double nextDY = node.layeredPostition.y - (node.currentPosition.y + acc * Math.sin(angle));
//                    double nextDist = Math.sqrt(nextDX * nextDX + nextDY * nextDY);
//
//                    if (nextDist <= acc) {
//                        node.currentPosition.x = node.layeredPostition.x;
//                        node.currentPosition.y = node.layeredPostition.y;
//                        System.out.println(d);
//                        System.out.println(nextDist);
//                        gd.repaint();
//                        try {
//                            ((Timer) e.getSource()).stop();
//                        } catch (Throwable e1) {
//                            // TODO Auto-generated catch block
//                            e1.printStackTrace();
//                        }
//                    }
//
//                }
//            });
//
//            t.start();
//            // t.stop();
//            // t.stop();
//
//        }
//
//        // this.repaint();
    }

    public void reset(List<Node> nodes, List<Edge> edges, int layerCnt) {

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
