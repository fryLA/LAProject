package src.tdojlafry.layered.layerAssignment.gui;

import src.tdojlafry.layered.layerAssignment.graphData.Position;

public class GNode {
    public final static double ABSTRACT_WIDTH = 100;
    public final static double ABSTRACT_HEIGHT = 100;
    
    public final static double NODE_WIDTH = 20;
    public final static double NODE_HEIGHT = 20;
    
    private final static double MOVE_STEPS = 10;
    private final static double MOVE_SNAPIN = 1;

    GraphDrawer graphDrawer;
    Position currentPosition = new Position();
    Position targetPosition = new Position();
    double actualScaleX;
    double actualScaleY;
    double padding;
    
    public GNode(double absX, double absY, GraphDrawer gd, double padding) {
        graphDrawer = gd;
        this.padding = padding;
        currentPosition.x = scaleX(absX, gd.getPreferredSize().getWidth());
        currentPosition.y = scaleY(absY, gd.getPreferredSize().getHeight());
        targetPosition.x = scaleX(absX, gd.getPreferredSize().getWidth());
        targetPosition.y = scaleY(absY, gd.getPreferredSize().getHeight());
    }
    
    void setTargetPosition(double absX, double absY) {
        targetPosition.x = scaleX(absX, graphDrawer.getWidth() - 2 * padding);
        targetPosition.y = scaleY(absY, graphDrawer.getHeight() - 2 * padding);
    }
    
    double scaleX(double x, double w) {
        actualScaleX = w;
        return x * w / ABSTRACT_WIDTH;
    }

    double scaleY(double y, double h) {
        actualScaleY = h;
        return y * h / ABSTRACT_HEIGHT;
    }
    
    void rescaleX(double newW) {
        currentPosition.x = currentPosition.x * newW / actualScaleX;
        targetPosition.x = targetPosition.x * newW / actualScaleX;
        actualScaleX = newW;
    }
    
    void rescaleY(double newH) {
        currentPosition.y = currentPosition.y * newH / actualScaleY;
        targetPosition.y = targetPosition.y * newH / actualScaleY;
        actualScaleY = newH;
    }
    
    boolean update() {
        if ((Math.abs(currentPosition.x - targetPosition.x) < MOVE_SNAPIN) &&
           (Math.abs(currentPosition.y - targetPosition.y) < MOVE_SNAPIN)) {
            targetPosition.x = currentPosition.x;
            targetPosition.y = currentPosition.y;
            return false;
        } 
        if (Math.abs(currentPosition.x - targetPosition.x) < MOVE_SNAPIN) {
            targetPosition.x = currentPosition.x;
        } 
        if (Math.abs(currentPosition.y - targetPosition.y) < MOVE_SNAPIN) {
            targetPosition.y = currentPosition.y;
        } 
                
        double dx = targetPosition.x - currentPosition.x;
        double dy = targetPosition.y - currentPosition.y;

        double angle = Math.atan2(dy, dx);

        double d = Math.sqrt(dx * dx + dy * dy);
        double acc = d / MOVE_STEPS;

        currentPosition.x += acc * Math.cos(angle);
        currentPosition.y += acc * Math.sin(angle);     
        
        return true;
    }
    
    boolean moving() {
        boolean a = (currentPosition.x != targetPosition.x) && (currentPosition.y != targetPosition.y);
        return a;
    }
}
