package src.tdojlafry.layered.layerAssignment.graphData;

public class Edge {

    public Node startNode;
    public Node endNode;
    
    public boolean isDummy = false;
    
    public Edge(Node sn, Node en) {
        this.startNode = sn;
        this.endNode = en;
    }

    public boolean isDummy() {
        return isDummy;
    }

    public void setDummy(boolean isDummy) {
        this.isDummy = isDummy;
    }
    
    public Position getStartPostition(boolean inLayer) {
        Position pos = new Position();
        
        if (!inLayer) {
            
            if (startNode.getGetScaledPosition().x < endNode.getGetScaledPosition().x) {
                pos.x = startNode.getGetScaledPosition().x + Node.DEFAULT_NODE_WIDTH;
            } else {
                pos.x = startNode.getGetScaledPosition().x;
            }
            
            if (startNode.getGetScaledPosition().y < endNode.getGetScaledPosition().y) {
                pos.y = startNode.getGetScaledPosition().y + Node.DEFAULT_NODE_HEIGHT;
            } else {
                pos.y = startNode.getGetScaledPosition().y;
            }
        } else {
            pos.x = startNode.layeredPostition.x + Node.DEFAULT_NODE_WIDTH;
            pos.y = startNode.layeredPostition.y + Node.DEFAULT_NODE_HEIGHT / 2;
        }
        
        return pos;
    }
    
    public Position getEndPostition(boolean inLayer) {
        Position pos = new Position();
        
        if (!inLayer) {
            if (startNode.getPosition().x < endNode.getPosition().x) {
                pos.x = endNode.getGetScaledPosition().x;
            } else {
                pos.x = endNode.getGetScaledPosition().x + Node.DEFAULT_NODE_WIDTH;
            }
            
            if (startNode.getPosition().y < endNode.getPosition().y) {
                pos.y = endNode.getGetScaledPosition().y;
            } else {
                pos.y = endNode.getGetScaledPosition().y + Node.DEFAULT_NODE_HEIGHT;
            }
        } else {
            pos.x = endNode.layeredPostition.x;
            pos.y = endNode.layeredPostition.y + Node.DEFAULT_NODE_HEIGHT / 2;
        }
        
        return pos;
    }
    
}
