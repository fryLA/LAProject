package src.tdojlafry.layered.layerAssignment.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import src.tdojlafry.layered.layerAssignment.graphData.Edge;
import src.tdojlafry.layered.layerAssignment.graphData.Node;

class ShapeProvider {

    private final static int ARROW_SIZE = 5;

    protected static void drawArrow(Graphics g1, Edge e, boolean inLayer) {

        Graphics2D g = (Graphics2D) g1.create();
        double x1 = e.getStartPostition(inLayer).x;
        double y1 = e.getStartPostition(inLayer).y;
        double x2 = e.getEndPostition(inLayer).x;
        double y2 = e.getEndPostition(inLayer).y;

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] { len, len - ARROW_SIZE, len - ARROW_SIZE, len },
                new int[] { 0, -ARROW_SIZE, ARROW_SIZE, 0 }, 4);
    }

    protected static void drawLayer(Graphics g1, Edge e) {

        Graphics2D g = (Graphics2D) g1.create();
    }

    protected static void drawNode(Graphics g1, Node node) {
        
        Graphics2D g = (Graphics2D) g1.create();
        
        Rectangle2D rect =
                new Rectangle2D.Double(node.getGetScaledPosition().x, 
                        node.getGetScaledPosition().y, Node.DEFAULT_NODE_WIDTH, Node.DEFAULT_NODE_HEIGHT);
//        if (newlyAssignedNodes.contains(node)) {
//            g2.setColor(Color.RED);
//        } else {
            g.setColor(Color.BLUE);
//        }
        g.fill(rect);
        g.draw(rect);
    }
}
