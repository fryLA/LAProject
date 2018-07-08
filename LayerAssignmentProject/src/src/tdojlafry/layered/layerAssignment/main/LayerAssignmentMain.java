package src.tdojlafry.layered.layerAssignment.main;

import java.util.List;

import org.eclipse.elk.graph.ElkNode;

import parser.Parser;
import src.tdojlafry.layered.layerAssignment.graphData.Edge;
import src.tdojlafry.layered.layerAssignment.graphData.LayerAssignment;
import src.tdojlafry.layered.layerAssignment.graphData.SimpleGraph;
import src.tdojlafry.layered.layerAssignment.graphData.Node;
import src.tdojlafry.layered.layerAssignment.gui.LayerAssignmentView;
import src.tdojlafry.layered.layerAssignment.testStuff.GuiTestDataCreator;

public class LayerAssignmentMain {

    public static void main(String[] args) {
            
            // USE THIS FOR TESTING (only one of the following two lines at a time)
//        ElkNode testGraph = GuiTestDataCreator.createPyramid();// // ignores parsing
//        ElkNode testGraph = GuiTestDataCreator.createSimpleLayoutGraph(200);
//            ElkNode testGraph = Parser.parse("testGraphs/testfile2.txt"); // does not ignore parsing
        
//        List<SimpleGraph> data =  la.assignLayers(testGraph);
        
//        for (int i = 0; i < data.size(); i ++) {
//            
//            // Some debug info as long as there is no animation for layer assignment
//            System.out.println("\n\nGraph " + i);
//            List<Node> nodes = data.get(i).getNodes();
//            for (Node node : nodes) {
//                System.out.println("\n" + node.getLayer());
//                System.out.println(node.getPosInlayer());
//                System.out.println(node.getPosition().x);
//                System.out.println(node.getPosition().y);
//                System.out.println(node.isDummy());
//            }
//            
//            List<Edge> edges = data.get(i).getEdges();
//            for (Edge edge : edges) {
//                System.out.println("\n" + edge.startNode);
//                System.out.println(edge.endNode);
//                System.out.println(edge.isDummy());
//            }
//        }
        new LayerAssignmentView();
    }

}
