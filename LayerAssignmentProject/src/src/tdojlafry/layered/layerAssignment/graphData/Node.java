package src.tdojlafry.layered.layerAssignment.graphData;
public class Node {
    
    int layer = -1;
    
    int posInlayer = -1;
    
    Position position = new Position();
    
    boolean isDummy = false;
    
    public Node() {
        
    }
    
    public Node(double x, double y) {
        this.position.x = x;
        this.position.y = y;
    }
}
