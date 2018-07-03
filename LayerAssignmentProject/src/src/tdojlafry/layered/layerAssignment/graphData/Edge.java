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
    
    public Position getStartPostition() {
        Position pos = new Position();
        
//        if (!inLayer) {
//            
            if (startNode.currentPosition.x < endNode.currentPosition.x) {
                pos.x = startNode.currentPosition.x + Node.DEFAULT_NODE_WIDTH;
            } else {
                pos.x = startNode.currentPosition.x;
            }
            
            if (startNode.currentPosition.y < endNode.currentPosition.y) {
                pos.y = startNode.currentPosition.y + Node.DEFAULT_NODE_HEIGHT;
            } else {
                pos.y = startNode.currentPosition.y;
            }
//        } else {
//            pos.x = startNode.layeredPostition.x + Node.DEFAULT_NODE_WIDTH;
//            pos.y = startNode.layeredPostition.y + Node.DEFAULT_NODE_HEIGHT / 2;
//        }
        
        return pos;
    }
    
    public Position getEndPostition() {
        Position pos = new Position();
        
//        if (!inLayer) {
            if (startNode.currentPosition.x < endNode.currentPosition.x) {
                pos.x = endNode.currentPosition.x;
            } else {
                pos.x = endNode.currentPosition.x + Node.DEFAULT_NODE_WIDTH;
            }
            
            if (startNode.currentPosition.y < endNode.currentPosition.y) {
                pos.y = endNode.currentPosition.y;
            } else {
                pos.y = endNode.currentPosition.y + Node.DEFAULT_NODE_HEIGHT;
            }
//        } else {
//            pos.x = endNode.layeredPostition.x;
//            pos.y = endNode.layeredPostition.y + Node.DEFAULT_NODE_HEIGHT / 2;
//        }
        
        return pos;
    }
    
}
