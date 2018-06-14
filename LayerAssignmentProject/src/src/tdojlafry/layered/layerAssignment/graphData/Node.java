package src.tdojlafry.layered.layerAssignment.graphData;
public class Node {
    
    private int layer = -1;
    
    private int posInlayer = -1;
    
    private Position position = new Position();
    
    private boolean isDummy = false;
    
    public Node() {
        
    }
    
    public Node(double x, double y) {
        this.getPosition().x = x;
        this.getPosition().y = y;
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
}
