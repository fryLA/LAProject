package src.tdojlafry.layered.layerAssignment.gui;

import java.util.List;

import src.tdojlafry.layered.layerAssignment.graphData.SimpleGraph;

public class LayerAssignmentView {

    List<SimpleGraph> graphs;

    public LayerAssignmentView(List<SimpleGraph> graphs) {
        this.graphs = graphs;
    }

    public void visualize() {

        LayerAssignmentVisualizer visualizer = new LayerAssignmentVisualizer(graphs);

        visualizer.createView(500, 500);
        visualizer.initialize();

    }

}
