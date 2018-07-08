package src.tdojlafry.layered.layerAssignment.gui;

import java.io.IOException;
import java.util.List;

import org.eclipse.elk.graph.ElkNode;

import parser.Parser;
import src.tdojlafry.layered.layerAssignment.graphData.LayerAssignment;
import src.tdojlafry.layered.layerAssignment.graphData.SimpleGraph;

public class LayerAssignmentComputationManager {

    protected static List<SimpleGraph> computeNewInputs(String path) {

        try {
            ElkNode testGraph = Parser.parse(path); // does not ignore parsing

            LayerAssignment la = new LayerAssignment();
            List<SimpleGraph> data = la.assignLayers(testGraph);

            return data;

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

}
