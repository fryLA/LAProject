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
            
            if (startNode.scaledPosition.x < endNode.scaledPosition.x) {
                pos.x = startNode.scaledPosition.x + Node.DEFAULT_NODE_WIDTH;
            } else {
                pos.x = startNode.scaledPosition.x;
            }
            
            if (startNode.scaledPosition.y < endNode.scaledPosition.y) {
                pos.y = startNode.scaledPosition.y + Node.DEFAULT_NODE_HEIGHT;
            } else {
                pos.y = startNode.scaledPosition.y;
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
                pos.x = endNode.scaledPosition.x;
            } else {
                pos.x = endNode.scaledPosition.x + Node.DEFAULT_NODE_WIDTH;
            }
            
            if (startNode.getPosition().y < endNode.getPosition().y) {
                pos.y = endNode.scaledPosition.y;
            } else {
                pos.y = endNode.scaledPosition.y + Node.DEFAULT_NODE_HEIGHT;
            }
        } else {
            pos.x = endNode.layeredPostition.x;
            pos.y = endNode.layeredPostition.y + Node.DEFAULT_NODE_HEIGHT / 2;
        }
        
        return pos;
    }
    
}
