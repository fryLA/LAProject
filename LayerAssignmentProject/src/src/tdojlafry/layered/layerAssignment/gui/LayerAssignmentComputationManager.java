package src.tdojlafry.layered.layerAssignment.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.elk.core.util.Pair;
import org.eclipse.elk.graph.ElkNode;

import parser.Parser;
import src.tdojlafry.layered.layerAssignment.graphData.LayerAssignment;
import src.tdojlafry.layered.layerAssignment.graphData.SimpleGraph;

/**
 * 
 * Helper class which holds all computation tasks of the layer assignment visualization.
 *
 */
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

    protected static ElkNode parseGraphToElkGraph(String path) {
        try {
            ElkNode graph = Parser.parse(path); // does not ignore parsing

            return graph;

        } catch (Exception ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

    private static boolean topologicalSortCyclic(String currentNode, Set<String> visited, Set<String> path,
            Map<String, ArrayList<String>> edges) {

        if (visited.contains(currentNode) && path.contains(currentNode)) {
            return true;
        }

        visited.add(currentNode);
        path.add(currentNode);

        ArrayList<String> adjacentNodes = edges.get(currentNode);
        if (adjacentNodes != null) {
            for (String target : adjacentNodes) {
                if (topologicalSortCyclic(target, visited, path, edges)) {
                    return true;
                }
            }
            path.remove(currentNode);
        }
        path.remove(currentNode);
        return false;
    }

    public static boolean isCyclic(List<String> nodes, Map<String, ArrayList<String>> edges) {

        Set<String> visited = new HashSet<String>();

        for (String node : nodes) {
            if (!visited.contains(node)) {
                Set<String> path = new HashSet<String>();

                if (topologicalSortCyclic(node, visited, path, edges)) {
                    return true;
                }
            }
        }
        return false;
    }

}
