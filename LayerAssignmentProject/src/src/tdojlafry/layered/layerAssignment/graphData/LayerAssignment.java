package src.tdojlafry.layered.layerAssignment.graphData;

import org.eclipse.elk.graph.ElkEdge;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.properties.Property;
import org.eclipse.elk.graph.util.ElkGraphUtil;

import java.util.*;

public class LayerAssignment {
    private static final Property<Integer> LAYER = new Property<Integer>("Layer");
    private static final Property<Integer> POSITION_IN_LAYER = new Property<Integer>("PositionInLayer");
    private static final Property<Boolean> IS_DUMMY = new Property<Boolean>("IsDummy");
    private int seed = 333;

    
    public LayerAssignment() {
        
        // Compute a random seed for random initial node positions
        seed = (int)System.currentTimeMillis();
        int a = seed;
        a = a /100000;
        seed = seed - (int)( Math.signum(a)*(a * 100000));
        
    }


    /**
     * Fuehrt fuer den eingegebenen kreisfreien ElkGraphen das Layerassignment aus
     * 
     * @param layoutGraph
     * @return Eine Veraenderungshistorie des Graphen waehrend des Layerassignments
     */
    public List<SimpleGraph> assignLayers(ElkNode elkGraph) {

        // Defaultproperties werden verteilt
        for (ElkNode n : elkGraph.getChildren()) {
            n.setProperty(LAYER, -1);
            n.setProperty(IS_DUMMY, false);
            n.setProperty(POSITION_IN_LAYER, 1);
        }
        for (ElkEdge e : elkGraph.getContainedEdges()) {
            e.setProperty(IS_DUMMY, false);
        }

        SimpleGraph layoutGraph = elkGraphToMyGraph(elkGraph);
        List<SimpleGraph> Verlauf = new ArrayList<SimpleGraph>();
        Verlauf.add(layoutGraph);

        List<ElkNode> nodes = new ArrayList<>(elkGraph.getChildren());
        List<ElkEdge> edges = new ArrayList<>(elkGraph.getContainedEdges());
        List<ElkNode> toBeRemoved;
        int l = 0;
        while (!nodes.isEmpty()) {

            // Es werden alle Nodes n in g gesucht, die keine eingehenden Kanten haben
            toBeRemoved = new ArrayList<>();
            for (ElkNode n : nodes) {
                if (getIncommingEdges(edges, n).size() == 0) {
                    toBeRemoved.add(n);
                }
            }

            // alle oben gefundenen Knoten n werden in das l-te Layer getan und werden fuer die weitere
            // Berrechnung geloescht, so wie alle ausgehenden Kanten von n geloescht werden
            for (int i = 0; i < toBeRemoved.size(); i++) {
                ElkNode n = toBeRemoved.get(i);
                n.setProperty(POSITION_IN_LAYER, i);
                n.setProperty(LAYER, l);
                nodes.remove(n);
                List<ElkEdge> outgoingEdges = new ArrayList<>(getOutgoingEdges(edges, n));
                for (ElkEdge e : outgoingEdges) {
                    edges.remove(e);
                }
            }
            Verlauf.add(elkGraphToMyGraph(elkGraph));
            l++;
        }

        // einfuegen von dummynodes
        edges = new ArrayList<>(elkGraph.getContainedEdges());
        ElkNode lastDummy;
        
        List<ElkEdge> edgesToBeRemoved = new ArrayList<ElkEdge>();
        
        for (ElkEdge edge : edges) {
            ElkNode source = ElkGraphUtil.connectableShapeToNode(edge.getSources().get(0));
            ElkNode target = ElkGraphUtil.connectableShapeToNode(edge.getTargets().get(0));
            lastDummy = source;
            
            String sourceLabel = "";
            if (source.getLabels() != null && !source.getLabels().isEmpty()) {sourceLabel = source.getLabels().get(0).getText();}
            String targetLabel = "";
            if (target.getLabels() != null && !target.getLabels().isEmpty()) {targetLabel = target.getLabels().get(0).getText();}
            
            ElkEdge lastEdge = null;

            //iteriere durch alle Layer zwischen source und target
            for (int i = source.getProperty(LAYER) + 1; i < target.getProperty(LAYER); i++) {
                edgesToBeRemoved.clear();

                if (i == source.getProperty(LAYER) + 1) {
                    // Setze target der Kante auf einen neuen Dummyknoten
                    ElkNode dummy = ElkGraphUtil.createNode(elkGraph);
                    dummy.setProperty(IS_DUMMY, true);
                    dummy.setProperty(LAYER, i);
                    dummy.setProperty(POSITION_IN_LAYER, -1);
                    dummy.setProperty(POSITION_IN_LAYER, getMaxPositionInLayer(dummy.getProperty(LAYER), elkGraph) + 1);
                    ElkGraphUtil.createLabel(sourceLabel + ":" + targetLabel, dummy);
//                    dummy.setProperty(POSITION_IN_LAYER, getMaxPositionInLayer(dummy.getProperty(LAYER), elkGraph) + 1);
                    edge.getTargets().set(0, dummy);
                    
                    ElkEdge dummyEdge = ElkGraphUtil.createEdge(elkGraph);
                    dummyEdge.setProperty(IS_DUMMY, true);
                    dummyEdge.getSources().clear();
                    dummyEdge.getSources().add(dummy);
                    dummyEdge.getTargets().clear();
                    dummyEdge.getTargets().add(0, target);
                    lastEdge = dummyEdge;
                    lastDummy = dummy;
                } else if (i < target.getProperty(LAYER)) {
                    //setzte eine neue DummyKante vom letzten Dummyknoten zu einem neuen Dummyknoten
                    ElkNode dummy = ElkGraphUtil.createNode(elkGraph);
                    dummy.setProperty(IS_DUMMY, true);
                    dummy.setProperty(LAYER, i);
                    dummy.setProperty(POSITION_IN_LAYER, -1);
                    dummy.setProperty(POSITION_IN_LAYER, getMaxPositionInLayer(dummy.getProperty(LAYER), elkGraph) + 1);
                    ElkGraphUtil.createLabel(sourceLabel + ":"  + targetLabel, dummy);
                    
                    ElkEdge dummyEdge1 = ElkGraphUtil.createEdge(elkGraph);
                    dummyEdge1.setProperty(IS_DUMMY, true);
                    dummyEdge1.getSources().clear();
                    dummyEdge1.getSources().add(lastDummy);
                    dummyEdge1.getTargets().clear();
                    dummyEdge1.getTargets().add(0, dummy);
                    
                    ElkEdge dummyEdge = ElkGraphUtil.createEdge(elkGraph);
                    dummyEdge.setProperty(IS_DUMMY, true);
                    dummyEdge.getSources().clear();
                    dummyEdge.getSources().add(dummy);
                    dummyEdge.getTargets().clear();
                    dummyEdge.getTargets().add(0, target);
                    edgesToBeRemoved.add(lastEdge);
                    lastEdge = dummyEdge;
                    lastDummy = dummy;
                }
//                else {
//                    //setzte eine neue DummyKante vom letzten Dummyknoten zu target
//                    dummyEdge = ElkGraphUtil.createEdge(elkGraph);
//                    dummyEdge.setProperty(IS_DUMMY, true);
//                    dummyEdge.getSources().clear();
//                    dummyEdge.getSources().add(lastDummy);
//                    dummyEdge.getTargets().clear();
//                    dummyEdge.getTargets().add(0, target);
//                }
                elkGraph.getContainedEdges().removeAll(edgesToBeRemoved);
                Verlauf.add(elkGraphToMyGraph(elkGraph));
            }
        }
        return Verlauf;
    }

    public SimpleGraph elkGraphToMyGraph(ElkNode elkGraph) {

        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        Random rand = new Random(seed);
        
        boolean dummyNodesPresent = false;

        // Knoten werden aus dem Elkgraphen in die MyGraph datenstrukture uebertragen und mit zufaelligen koordinaten
        // versehen
        for (ElkNode elkNode : elkGraph.getChildren()) {
            Node node = new Node(rand.nextInt(100 - Node.DEFAULT_NODE_WIDTH) + 1, rand.nextInt(100 - Node.DEFAULT_NODE_HEIGHT) + 1);
            if (!elkNode.getLabels().isEmpty()) {
                node.label = elkNode.getLabels().get(0).getText();
            }
            node.layer = elkNode.getProperty(LAYER);
            node.isDummy = elkNode.getProperty(IS_DUMMY);
            if (node.isDummy) {
                dummyNodesPresent = true;
            }
            node.posInlayer = elkNode.getProperty(POSITION_IN_LAYER);

            nodes.add(node);
        }

        // Kanten werden aus dem Elkgraphen in die MyGraph datenstruktur uebertragen
        for (ElkEdge elkEdge : elkGraph.getContainedEdges()) {
            Node sourceNode = null;
            Node targetNode = null;
            Edge edge;
            ElkNode source = ElkGraphUtil.connectableShapeToNode(elkEdge.getSources().get(0));
            ElkNode target = ElkGraphUtil.connectableShapeToNode(elkEdge.getTargets().get(0));
            for (int i = 0; i < elkGraph.getChildren().size(); i++) {
                if (elkGraph.getChildren().get(i) == source) {
                    sourceNode = nodes.get(i);
                } else if (elkGraph.getChildren().get(i) == target) {
                    targetNode = nodes.get(i);
                }
            }
            edge = new Edge(sourceNode, targetNode);
            edge.isDummy = elkEdge.getProperty(IS_DUMMY);

            edges.add(edge);
        }
        
        SimpleGraph graph = new SimpleGraph(nodes, edges);
        graph.isDummyNodeGraph = dummyNodesPresent;

        return graph;
    }

    public static List<ElkEdge> getIncommingEdges(List<ElkEdge> edges, ElkNode n) {
        List<ElkEdge> incommingEdges = new ArrayList<>();

        for (ElkEdge e : edges) {
            ElkNode target = ElkGraphUtil.connectableShapeToNode(e.getTargets().get(0));
            if (target.equals(n)) {
                incommingEdges.add(e);
            }
        }
        return incommingEdges;
    }

    public static List<ElkEdge> getOutgoingEdges(List<ElkEdge> edges, ElkNode n) {
        List<ElkEdge> outgoingEdges = new ArrayList<>();
        for (ElkEdge e : edges) {
            ElkNode source = ElkGraphUtil.connectableShapeToNode(e.getSources().get(0));
            if (source.equals(n)) {
                outgoingEdges.add(e);
            }
        }
        return outgoingEdges;
    }

    public int getMaxPositionInLayer(int layer, ElkNode graph) {
        int max = 0;
        for (ElkNode node : graph.getChildren()) {
            if (node.getProperty(LAYER) == layer) {
                max = Math.max(max, node.getProperty(POSITION_IN_LAYER));
            }
        }

        return max;
    }
}
