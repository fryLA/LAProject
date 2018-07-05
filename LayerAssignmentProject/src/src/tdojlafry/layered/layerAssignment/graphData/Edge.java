package src.tdojlafry.layered.layerAssignment.graphData;

public class Edge {

    public Node startNode;
    public Node endNode;
    
    public boolean isDummy = false;
    
    public Edge(Node sn, Node en) {
        this.startNode = sn;
        this.endNode = en;
    }

    public boolean isDummy() {
        return isDummy;
    }

    public void setDummy(boolean isDummy) {
        this.isDummy = isDummy;
    }
    
}
