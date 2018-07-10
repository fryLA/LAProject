package src.tdojlafry.layered.layerAssignment.gui;

import src.tdojlafry.layered.layerAssignment.graphData.Position;

/**
 * 
 * This class represents a node on the drawing area. A GNode is associated to a
 * {@link src.tdojlafry.layered.layerAssignment.graphData.Node} and computes its movement for each timer step in
 * {@link GraphDrawer}
 *
 */
public class GNode {
    /**
     *  The abstract dimension borders of the random positions given to nodes in the {@link src.tdojlafry.layered.layerAssignment.graphData.LayerAssignment}
     */
    public final static double ABSTRACT_WIDTH = 100;
    public final static double ABSTRACT_HEIGHT = 100;

    public static double node_widht = 20;
    public static double node_height = 20;

    static final int MAX_SPEED = 60;

    static double moveSteps = 10;
    private final static double MOVE_SNAPIN = 1;

    private String label = "";

    boolean dummy = false;

    GraphDrawer graphDrawer;
    Position currentPosition = new Position();
    Position targetPosition = new Position();
    double actualScaleX;
    double actualScaleY;
    double padding;

    public GNode(double absX, double absY, GraphDrawer gd, double padding, String l, boolean d) {
        dummy = d;
        label = l;
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

    protected String getLabel() {
        return label;
    }

    double scaleX(double x, double w) {
        actualScaleX = w;
        return x * w / ABSTRACT_WIDTH;
    }

    double scaleY(double y, double h) {
        actualScaleY = h;
        return y * h / ABSTRACT_HEIGHT;
    }

    // Rescale if window size changed
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

    // Called by graph drawer in each timer step
    boolean update() {
        if ((Math.abs(currentPosition.x - targetPosition.x) < MOVE_SNAPIN)
                && (Math.abs(currentPosition.y - targetPosition.y) < MOVE_SNAPIN)) {
            currentPosition.x = targetPosition.x;
            currentPosition.y = targetPosition.y;
            return false;
        }
        if (Math.abs(currentPosition.x - targetPosition.x) < MOVE_SNAPIN) {
            currentPosition.x = targetPosition.x;
        }
        if (Math.abs(currentPosition.y - targetPosition.y) < MOVE_SNAPIN) {
            currentPosition.y = targetPosition.y;
        }

        double dx = targetPosition.x - currentPosition.x;
        double dy = targetPosition.y - currentPosition.y;

        double angle = Math.atan2(dy, dx);

        double d = Math.sqrt(dx * dx + dy * dy);
        double acc = d / moveSteps;

        currentPosition.x += acc * Math.cos(angle);
        currentPosition.y += acc * Math.sin(angle);

        return true;
    }

    // Determines if this node is still moving -> if true, the ShapProvider will use different colors
    boolean moving() {

        boolean a = ((Math.abs(currentPosition.x - targetPosition.x) >= MOVE_SNAPIN)
                || (Math.abs(currentPosition.y - targetPosition.y) >= MOVE_SNAPIN));
        return a;
    }

    // if true, the ShapeProvider will draw a different node shape
    protected boolean isDummy() {
        return dummy;
    }
}
