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

protected static void drawArrow(Graphics g1, Edge e, boolean inLayer, GraphDrawer gd) {
        
        double halfWidth = GNode.NODE_WIDTH / 2;
        double halfHeight = GNode.NODE_HEIGHT / 2;
        
        GNode startingNode = gd.gNodes[gd.currNodes.indexOf(e.startNode)];
        GNode targetNode = gd.gNodes[gd.currNodes.indexOf(e.endNode)];
        
        Graphics2D g = (Graphics2D) g1.create();
        double x1 = startingNode.currentPosition.x + halfWidth + GraphDrawer.PADDING;
        double y1 = startingNode.currentPosition.y + halfHeight + GraphDrawer.PADDING;
        double x2 = targetNode.currentPosition.x + halfWidth + GraphDrawer.PADDING;
        double y2 = targetNode.currentPosition.y + halfHeight + GraphDrawer.PADDING;

        double dx = x2 - x1;
        double dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        int sx = 0;
        int sy = 0;     
        
        len -= halfWidth;
        
        g.drawLine(sx, sy, len, 0);
        g.fillPolygon(new int[] { len, len - ARROW_SIZE, len - ARROW_SIZE, len },
                new int[] { 0, -ARROW_SIZE, ARROW_SIZE, 0 }, 4);
    }

}
