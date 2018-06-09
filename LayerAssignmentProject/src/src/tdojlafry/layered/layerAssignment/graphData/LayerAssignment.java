package src.tdojlafry.layered.layerAssignment.graphData;

import org.eclipse.elk.graph.ElkEdge;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.util.ElkGraphUtil;

import java.util.*;

public class LayerAssignment {

    /**
     * Fuehrt fuer den eingegebenen kreisfreien ElkGraphen das Layerassignment aus 
     * 
     * @param layoutGraph
     * @return Eine Veraenderungshistorie des Graphen waehrend des Layerassignments
     */
    public static List<MyGraph> assignLayers(ElkNode elkGraph) {
        MyGraph layoutGraph = elkGraphToMyGraph(elkGraph);
        List<MyGraph> Verlauf = new ArrayList<MyGraph>();
        Verlauf.add(layoutGraph);

        return Verlauf;
    }

    public static MyGraph elkGraphToMyGraph(ElkNode elkGraph) {
        
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        Random rand = new Random(218);
        
        //Knoten werden aus dem Elkgraphen in die MyGraph datenstrukture uebertragen und mit zufaelligen koordinaten 
        //versehen
        for (ElkNode elkNode : elkGraph.getChildren()) {
            Node node = new Node(rand.nextInt(100) + 1, rand.nextInt(100) + 1);
            nodes.add(node);
        }
        
        //Kanten werden aus dem Elkgraphen in die MyGraph datenstruktur uebertragen
        for (ElkEdge elkEdge : elkGraph.getContainedEdges()) {
            Node sourceNode = null;
            Node targetNode = null;
            ElkNode source = ElkGraphUtil.connectableShapeToNode(elkEdge.getSources().get(0));
            ElkNode target = ElkGraphUtil.connectableShapeToNode(elkEdge.getTargets().get(0));
            for (int i = 0; i < elkGraph.getChildren().size(); i++) {
                if (elkGraph.getChildren().get(i) == source) {
                    sourceNode = nodes.get(i);
                } else if (elkGraph.getChildren().get(i) == target) {
                    targetNode = nodes.get(i);
                }
            }
            edges.add(new Edge(sourceNode, targetNode));
        }

        return new MyGraph(nodes, edges);
    }

}
