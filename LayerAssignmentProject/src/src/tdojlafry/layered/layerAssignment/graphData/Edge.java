package src.tdojlafry.layered.layerAssignment.graphData;

public class Edge {

    public Node startNode;
    public Node endNode;
    
    boolean isDummy = false;
    
    public Edge(Node sn, Node en) {
        this.startNode = sn;
        this.endNode = en;
    }
    
}
