package src.tdojlafry.layered.layerAssignment.gui;

import java.util.List;

import src.tdojlafry.layered.layerAssignment.graphData.MyGraph;

public class LayerAssignmentView {

    List<MyGraph> graphs;

    public LayerAssignmentView(List<MyGraph> graphs) {
        this.graphs = graphs;
    }

    public void visualize() {

        LayerAssignmentVisualizer visualizer = new LayerAssignmentVisualizer(graphs);

        visualizer.createView(500, 500);
        visualizer.initialize();

    }

}
