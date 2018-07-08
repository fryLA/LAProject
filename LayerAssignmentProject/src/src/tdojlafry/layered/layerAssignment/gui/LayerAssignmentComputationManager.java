package src.tdojlafry.layered.layerAssignment.gui;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.elk.core.util.Pair;
import org.eclipse.elk.graph.ElkEdge;
import org.eclipse.elk.graph.ElkNode;

import parser.Parser;
import src.tdojlafry.layered.layerAssignment.graphData.LayerAssignment;
import src.tdojlafry.layered.layerAssignment.graphData.SimpleGraph;

public class LayerAssignmentComputationManager {

    protected static Pair<Boolean, List<SimpleGraph>> computeNewInputs(ElkNode graph) {

        try {
            LayerAssignment la = new LayerAssignment();
            List<SimpleGraph> data = la.assignLayers(graph);

            return new Pair<>(true, data);

        } catch (Exception ioe) {
            ioe.printStackTrace();
            return new Pair<>(false, null);
        }
    }

    protected static Pair<Boolean, ElkNode> parseGraphToElkGraph(String path) {
        try {
            ElkNode graph = Parser.parse(path); // does not ignore parsing

            return new Pair<>(true, graph);

        } catch (Exception ioe) {
            ioe.printStackTrace();
            return new Pair<>(false, null);
        }
    }

    private static boolean topologicalSortCyclic(ElkNode node, Set<ElkNode> visited, Set<ElkNode> path) {

        if (visited.contains(node) && path.contains(node)) {
            return true;
        }

        visited.add(node);
        path.add(node);

        for (ElkEdge edge : node.getOutgoingEdges()) {
            ElkNode targetNode = (ElkNode) edge.getTargets().get(0);
            if (topologicalSortCyclic(targetNode, visited, path)) {
                return true;
            }
        }

        path.remove(node);
        return false;
    }

    public static boolean isCyclic(ElkNode elkNode) {
        List<ElkNode> nodes = elkNode.getChildren();
        Set<ElkNode> visited = new HashSet<ElkNode>();

        for (ElkNode node : nodes) {
            if (!visited.contains(node)) {
                Set<ElkNode> path = new HashSet<ElkNode>();

                if (topologicalSortCyclic(node, visited, path)) {
                    return true;
                }
            }
        }

        return false;
    }

}
