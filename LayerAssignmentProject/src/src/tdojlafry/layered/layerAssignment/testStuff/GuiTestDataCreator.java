package src.tdojlafry.layered.layerAssignment.testStuff;

import java.util.Iterator;

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
    
    
}
