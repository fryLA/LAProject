package src.tdojlafry.layered.layerAssignment.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Timer;

import org.eclipse.elk.core.util.Pair;

import src.tdojlafry.layered.layerAssignment.graphData.Edge;
import src.tdojlafry.layered.layerAssignment.graphData.Node;
import src.tdojlafry.layered.layerAssignment.graphData.SimpleGraph;

public class GraphDrawer extends JPanel implements ActionListener {

    public static double MINIGRAPH_AREA_DIVISOR = 3;

    /**
     * 
     */
    private static final long serialVersionUID = 8786962247310354215L;
    List<Node> currNodes;
    List<Node> lastNodes = new ArrayList<>();
    private List<Edge> currEdges;
    private List<Edge> edgesToBeRemovedfromMiniGraph= new ArrayList<Edge>();;

    static final int PADDING = (int) Math.max(GNode.node_height, GNode.node_widht);

    double layerWidth;
    double layerHeight;

    private int layerCnt;
    private HashMap<Integer, Rectangle2D> layers = new HashMap<>();
    private HashMap<Integer, Integer> nodesInLayer;

    public GNode gNodes[];
    Timer animationTimer;
    Timer showChangesTimer;
    private int actualWidth = 150;
    private int actualHeight = 150;
    
    private int visibleLayers = -1;

    protected int timerDelay = 10;

    protected GraphDrawer(List<Node> nodes, List<Edge> edges, int layerCnt, HashMap<Integer, Integer> nodesInLayer) {
        this.layerCnt = layerCnt;
        this.currEdges = edges;
        this.currNodes = nodes;

        this.nodesInLayer = nodesInLayer;


        this.addComponentListener(new ComponentListener() {

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentResized(ComponentEvent e) {
                update(currNodes, currEdges);

            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });


        gNodes = new GNode[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            gNodes[i] = new GNode(node.position.x, node.position.y / MINIGRAPH_AREA_DIVISOR, this, PADDING, node.label,
                    node.isDummy());
        }

        animationTimer = new Timer(timerDelay, this);

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
        for (int i = layerCnt - 1; i >= 0; i--) {
            Rectangle2D rect = ShapeProvider.drawLayer(g, PADDING + i * layerWidth, startPosY, layerWidth, layerHeight,
                    i <= visibleLayers);
            layers.put(i, rect);
            g2.draw(rect);
        }

        // EDGES
        for (Edge edge : currEdges) {
            ShapeProvider.drawArrow(g, edge, this, false);
        }
        
        for (Edge edge : edgesToBeRemovedfromMiniGraph) {
            ShapeProvider.drawArrow(g, edge, this, true);
        }

        // NODES
        for (int i = 0; i < gNodes.length; i++) {
            GNode gNode = gNodes[i];
            if (isUsed(i) || !gNode.isDummy()) {
                ShapeProvider.drawNode(g, gNode);
            }
        }
    }

    private void update(List<Node> nodes, List<Edge> edges) {
        computeDiffs(nodes, edges);
        
        showNextChanges();
//        currEdges = edges;
//        currNodes = nodes;
//        int maxLayer = -1;
//
//        for (int i = 0; i < gNodes.length; i++) {
//            GNode gNode = gNodes[i];
//            if (isUsed(i) || !gNode.isDummy()) {
//                Node node = nodes.get(i);
//                if (node.layer == -1) {
//                    gNode.setTargetPosition(node.position.x, node.position.y / MINIGRAPH_AREA_DIVISOR);
//                } else {
//                    maxLayer = Math.max(maxLayer, node.getLayer());
//                    Rectangle2D layerRect = layers.get(node.layer);
//                    gNode.targetPosition.x = layerRect.getCenterX() - (GNode.node_widht / 2);
//
//                    double d = (layerRect.getHeight()) / (nodesInLayer.get(node.layer) + 1);
//                    gNode.targetPosition.y = layerRect.getMinY() + ((node.posInlayer + 1) * d - GNode.node_height / 2);
//                    if (gNode.isDummy()) {
//                        gNode.currentPosition.x = gNode.targetPosition.x;
//                        gNode.currentPosition.y = gNode.targetPosition.y;
//                    }
//                }
//            }
//        }
//        this.visibleLayers = maxLayer;
        repaint();
        animationTimer.start();

    }
    
    private void showNextChanges() {
        showChangesTimer = new Timer(200, new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
                edgesToBeRemovedfromMiniGraph.clear();
            }
        });
        showChangesTimer.setRepeats(false);
        showChangesTimer.start();
    }



    private void computeDiffs(List<Node> nodes, List<Edge> edges) {
        // Remember the edges which are replaced 
        edgesToBeRemovedfromMiniGraph.clear();
        for (Edge oldEdge : currEdges) {
            if (!existsInNewGraph(oldEdge, edges, nodes))
            
                edgesToBeRemovedfromMiniGraph.add(oldEdge);
            
        }
        
        if (currNodes != null) {
            lastNodes.clear();
            lastNodes.addAll(currNodes);
        }
        
        currEdges = edges;
        currNodes = nodes;
        int maxLayer = -1;

        
        for (int i = 0; i < gNodes.length; i++) {
            GNode gNode = gNodes[i];
            if (isUsed(i) || !gNode.isDummy()) {
                Node node = nodes.get(i);
                if (node.layer == -1) {
                    gNode.setTargetPosition(node.position.x, node.position.y / MINIGRAPH_AREA_DIVISOR);
                } else {
                    maxLayer = Math.max(maxLayer, node.getLayer());
                    Rectangle2D layerRect = layers.get(node.layer);
                    gNode.targetPosition.x = layerRect.getCenterX() - (GNode.node_widht / 2);

                    double d = (layerRect.getHeight()) / (nodesInLayer.get(node.layer) + 1);
                    gNode.targetPosition.y = layerRect.getMinY() + ((node.posInlayer + 1) * d - GNode.node_height / 2);
                    if (gNode.isDummy()) {
                        gNode.currentPosition.x = gNode.targetPosition.x;
                        gNode.currentPosition.y = gNode.targetPosition.y;
                    }
                }
            }
        }
        this.visibleLayers = maxLayer;
    }
    
    private boolean existsInNewGraph(Edge oldEdge, List<Edge> edges, List<Node> nodes) {
        for (Edge edge : edges) {
            if (currNodes.indexOf(oldEdge.startNode) == nodes.indexOf(edge.startNode) && currNodes.indexOf(oldEdge.endNode) == nodes.indexOf(edge.endNode)) {
                return true;
            }
        }
        return false;
    }



    protected void update() {
        update(this.currNodes, this.currEdges);
    }

    protected void update(SimpleGraph newGraph) {
        update(newGraph.getNodes(), newGraph.getEdges());
    }

    private boolean isUsed(int i) {
        boolean used = false;
        if (i >= currNodes.size()) {
            return false;
        }

        Node node = currNodes.get(i);
        for (Edge edge : currEdges) {
            used = used | edge.startNode == node | edge.endNode == node;
        }

        return used;
    }

    public void reset(List<Node> nodes, List<Edge> edges, int layerCnt) {

        currNodes = nodes;
        currEdges = edges;

        visibleLayers = -1;
        for (int i = 0; i < gNodes.length; i++) {
            GNode gNode = gNodes[i];

            if (isUsed(i) || !gNode.isDummy()) {
                Node node = nodes.get(i);
                gNode.setTargetPosition(node.position.x, node.position.y / MINIGRAPH_AREA_DIVISOR);
            }

        }
        animationTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!showChangesTimer.isRunning()) {
            
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
    }

    //

}
