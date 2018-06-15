package src.tdojlafry.layered.layerAssignment.gui;

import java.util.List;

import src.tdojlafry.layered.layerAssignment.graphData.MyGraph;
import src.tdojlafry.layered.layerAssignment.graphData.Node;

public class LayerAssignmentView{
    
    List<MyGraph> graphs;
    
    

    public LayerAssignmentView(List<MyGraph> graphs) {
        this.graphs = graphs;
    }
    

    public void visualize() {
        
        int layerCnt = 0;
        
        for (Node node : graphs.get(graphs.size() - 1).getNodes()) {
            if (node.getLayer() > layerCnt) {
                layerCnt = node.getLayer();
            }
        }
        
        LayerAssignmentVisualizer visualizer = new LayerAssignmentVisualizer(layerCnt);
        visualizer.createView(500, 500);
        
        
        visualizer.initialize(graphs.get(0));
        
        
        visualizer.update();
    }
    
    
    
    
    
}
