package src.tdojlafry.layered.layerAssignment.graphData;
public class Node {
    
    public int layer = -1;
    
    public int posInlayer = -1;
    
    // Minimap position based on 100x100 window
    public Position position = new Position();
    
    public boolean isDummy = false;
    
    // Minimap position based on windows size
    public Position scaledPosition = new Position();
    
    // Position after assigning to layer in drawing
    public Position layeredPostition = new Position(-1,-1);
    
    public double xOffset = 0;
    public double yOffset = 0;
    
    public final static int DEFAULT_NODE_WIDTH = 10;
    public final static int DEFAULT_NODE_HEIGHT = 10;
    
    public Node() {
        
    }
    
    public Node(int x, int y) {
        this.getPosition().x = x;
        this.getPosition().y = y;
        scaledPosition.x = x;
        scaledPosition.y = y;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getPosInlayer() {
        return posInlayer;
    }

    public void setPosInlayer(int posInlayer) {
        this.posInlayer = posInlayer;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isDummy() {
        return isDummy;
    }

    public void setDummy(boolean isDummy) {
        this.isDummy = isDummy;
    }

    public Position getGetScaledPosition() {
        return scaledPosition;
    }
    
    public void refreshScaledPosition(double xMod, double yMod) {
        scaledPosition.x = position.x * xMod + xOffset;
        scaledPosition.y = position.y * yMod + yOffset;
    }
}
