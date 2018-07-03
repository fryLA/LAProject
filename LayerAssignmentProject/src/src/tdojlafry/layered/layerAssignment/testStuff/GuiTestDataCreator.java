package src.tdojlafry.layered.layerAssignment.testStuff;

import java.util.Iterator;
import java.util.List;

import org.eclipse.elk.graph.ElkEdge;
import org.eclipse.elk.graph.ElkLabel;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.util.ElkGraphUtil;


public class GuiTestDataCreator {
    
    // a -> b -> c -> d ...
    public static ElkNode createSimpleLayoutGraph(int nodeCount) {
        ElkNode parentNode = ElkGraphUtil.createNode(null);
        
        for (int i = 0; i < nodeCount; i++) {
            ElkNode newNode = ElkGraphUtil.createNode(parentNode);
            ElkLabel newLabel = ElkGraphUtil.createLabel(newNode);
            newLabel.setText("Node " + i);
        }
        
        Iterator<ElkNode> nodes = parentNode.getChildren().iterator();
        
        ElkNode currNode = nodes.next();
        
        while(nodes.hasNext()) {
            ElkEdge newEdge = ElkGraphUtil.createEdge(parentNode);
            newEdge.getSources().add(currNode);
            currNode = nodes.next();
            newEdge.getTargets().add(currNode);
        }
        
        return parentNode;
    }
    
    
    public static ElkNode createPyramid() {
        ElkNode parentNode = ElkGraphUtil.createNode(null);
        
        for (int i = 0; i < 10; i++) {
            ElkNode newNode = ElkGraphUtil.createNode(parentNode);
            ElkLabel newLabel = ElkGraphUtil.createLabel(newNode);
            newLabel.setText("Node " + i);
        }
        
        List<ElkNode> nodes = parentNode.getChildren();
        List<ElkNode> four = nodes.subList(0,4);
        List<ElkNode> three = nodes.subList(4,7);
        List<ElkNode> two = nodes.subList(7,9);
        ElkNode one = nodes.get(9);
        
        createNewEdge(four.get(0), three.get(0), parentNode);
        createNewEdge(four.get(1), three.get(0), parentNode);
        
        createNewEdge(four.get(1), three.get(1), parentNode);
        createNewEdge(four.get(2), three.get(1), parentNode);
        
        createNewEdge(four.get(2), three.get(2), parentNode);
        createNewEdge(four.get(3), three.get(2), parentNode);
        
        
        createNewEdge(three.get(0), two.get(0), parentNode);
        createNewEdge(three.get(1), two.get(0), parentNode);
        createNewEdge(three.get(1), two.get(1), parentNode);
        createNewEdge(three.get(2), two.get(1), parentNode);
        
        createNewEdge(two.get(0), one, parentNode);
        createNewEdge(two.get(1), one, parentNode);
        
        return parentNode;
    }


    private static void createNewEdge(ElkNode sNode, ElkNode tNode, ElkNode parentNode) {
        ElkEdge newEdge = ElkGraphUtil.createEdge(parentNode);
        newEdge.getSources().add(sNode);
        newEdge.getTargets().add(tNode);
        
    }
    
}
