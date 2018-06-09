package src.tdojlafry.layered.layerAssignment.graphData;

import java.util.*;

public class MyGraph {
    List<Node> nodes;
    List<Edge> edges;
    
     public MyGraph(List<Node> n, List<Edge> e) {
         nodes = n;
         edges = e;
     }
     public MyGraph() {
         nodes = new ArrayList<>();
         edges = new ArrayList<>();
     }
    
}
