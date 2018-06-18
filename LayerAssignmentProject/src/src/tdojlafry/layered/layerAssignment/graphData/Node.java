package src.tdojlafry.layered.layerAssignment.graphData;
public class Node {
    
    public int layer = -1;
    
    public int posInlayer = -1;
    
    public Position position = new Position();
    
    public boolean isDummy = false;
    
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
