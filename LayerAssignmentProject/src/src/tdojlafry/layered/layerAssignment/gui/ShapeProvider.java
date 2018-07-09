package src.tdojlafry.layered.layerAssignment.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import src.tdojlafry.layered.layerAssignment.graphData.Edge;

class ShapeProvider {

    private final static int ARROW_SIZE = 5;

    protected static void drawArrow(Graphics g1, Edge e, GraphDrawer gd) {

        double halfWidth = GNode.node_widht / 2;
        double halfHeight = GNode.node_height / 2;

        GNode startingNode = gd.gNodes[gd.currNodes.indexOf(e.startNode)];
        GNode targetNode = gd.gNodes[gd.currNodes.indexOf(e.endNode)];

        Graphics2D g = (Graphics2D) g1.create();
        g.setColor(Color.BLACK);
        double x1 = startingNode.currentPosition.x + halfWidth;
        double y1 = startingNode.currentPosition.y + halfHeight;
        double x2 = targetNode.currentPosition.x + halfWidth;
        double y2 = targetNode.currentPosition.y + halfHeight;

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

    // private static GradientPaint redGradiant = new GradientPaint(
    // 0,
    // 0,
    // new Color(255, 102, 102),
    // 1,
    // 5,
    // Color.BLACK, true);
    final static float dash1[] = { 10.0f };
    final static BasicStroke dashed =
            new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, dash1, 0.0f);

    public static void drawNode(Graphics g1, GNode node) {

        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // redGradiant.getPoint1().setLocation(node.currentPosition.x + GraphDrawer.PADDING,node.currentPosition.y +
        // GraphDrawer.PADDING);
        // redGradiant.getPoint2().setLocation(node.currentPosition.x + GraphDrawer.PADDING + GNode.NODE_WIDTH/2,
        // node.currentPosition.y + GraphDrawer.PADDING + GNode.NODE_HEIGHT/2);
        //
        
        Shape shape = null;

        if (node.moving()) {
            GradientPaint redGradiant = new GradientPaint((int) node.currentPosition.x,
                    (int) node.currentPosition.y, new Color(255, 238, 230),
                    (int) (node.currentPosition.x + GNode.node_widht / 2),
                    (int) (node.currentPosition.y + GNode.node_height / 2), new Color(255, 150, 102),
                    false);
            g.setPaint(redGradiant);
            
            shape = new RoundRectangle2D.Double(node.currentPosition.x,
                    node.currentPosition.y, GNode.node_widht, GNode.node_height, 5, 5);
        } else {
            if (node.dummy) {
                GradientPaint grayGradient = new GradientPaint((int) node.currentPosition.x,
                        (int) node.currentPosition.y , new Color(230, 230, 230),
                        (int) (node.currentPosition.x + GNode.node_widht / 2),
                        (int) (node.currentPosition.y + GNode.node_height / 2),  new Color(242, 242, 242),
                        false);
                
                g.setPaint(grayGradient);
                
                shape = new Ellipse2D.Double(node.currentPosition.x,
                    node.currentPosition.y, GNode.node_widht, GNode.node_height);
                
            } else {
                
                GradientPaint kakhiGradient = new GradientPaint((int) node.currentPosition.x,
                        (int) node.currentPosition.y , new Color(183, 183, 149),
                        (int) (node.currentPosition.x + GNode.node_widht / 2),
                        (int) (node.currentPosition.y + GNode.node_height / 2), new Color(224, 224, 209),
                        false);
                
                g.setPaint(kakhiGradient);
                
                shape = new RoundRectangle2D.Double(node.currentPosition.x,
                        node.currentPosition.y, GNode.node_widht, GNode.node_height, 5, 5);
                
            }

        }
        g.fill(shape);
        g.setPaint(new Color(183, 183, 149));
        g.setStroke(new BasicStroke(1.2f));
        g.draw(shape);
        
        // add labels
        String text = node.getLabel();
        FontRenderContext frc = new FontRenderContext(null,  false,  true);//g.getFontRenderContext();
        Font font = g.getFont().deriveFont((float)(GNode.node_widht / text.length()));
        g.setFont(font);
        g.setPaint(Color.BLACK);
        
        float sw = (float)font.getStringBounds(text, frc).getWidth();
        LineMetrics lm = font.getLineMetrics(text, frc);
        float sh = lm.getAscent() + lm.getDescent();
        g.setPaint(Color.BLACK);
        g.drawString(node.getLabel(),
                (float)(shape.getBounds2D().getX() + (shape.getBounds2D().getWidth()-sw) / 2) , 
                (float)(shape.getBounds2D().getY() + (shape.getBounds2D().getHeight() +sh) / 2 - lm.getDescent()));
        
        
    }

    public static Rectangle2D drawLayer(Graphics g1, double startPosX, double startPosY, double layerWidth,
            double layerHeight, boolean visible) {
        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // redGradiant.getPoint1().setLocation(node.currentPosition.x + GraphDrawer.PADDING,node.currentPosition.y +
        // GraphDrawer.PADDING);
        // redGradiant.getPoint2().setLocation(node.currentPosition.x + GraphDrawer.PADDING + GNode.NODE_WIDTH/2,
        // node.currentPosition.y + GraphDrawer.PADDING + GNode.NODE_HEIGHT/2);
        //

        if (visible) {
            GradientPaint blueGradient = new GradientPaint((int) startPosX, (int) startPosY, new Color(230, 250, 255),
                    (int) (startPosX + layerWidth), (int) (startPosY + layerHeight), new Color(128, 229, 255), false);
            g.setPaint(blueGradient);
        } else {
            GradientPaint grayGradient = new GradientPaint((int) startPosX, (int) startPosY, new Color(230, 230, 230),
                    (int) (startPosX + layerWidth), (int) (startPosY + layerHeight), new Color(242, 242, 242), false);
            g.setPaint(grayGradient);

        }
        Rectangle2D rect = new Rectangle2D.Double(startPosX, startPosY, layerWidth, layerHeight);
        g.fill(rect);
        g.setPaint(new Color(230, 230, 230));
        g.setStroke(new BasicStroke(1.2f));
        g.draw(rect);
        return rect;
    }
}
