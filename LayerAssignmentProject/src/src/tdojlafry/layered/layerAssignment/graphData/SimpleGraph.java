package src.tdojlafry.layered.layerAssignment.graphData;

import java.util.*;

public class SimpleGraph {
    private List<Node> nodes;
    private List<Edge> edges;
    
    public boolean isDummyNodeGraph = false;

    public SimpleGraph(List<Node> n, List<Edge> e) {
        setNodes(n);
        setEdges(e);
    }

    public SimpleGraph() {
        setNodes(new ArrayList<>());
        setEdges(new ArrayList<>());
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

}
