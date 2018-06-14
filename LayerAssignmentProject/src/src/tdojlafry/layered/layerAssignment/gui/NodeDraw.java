package src.tdojlafry.layered.layerAssignment.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

class NodeDraw extends JPanel {

    private int x = 200;
    private int y = 50;
    private int width = 20;
    private int height = 20;

    public NodeDraw(double x, double y, double width, double height) {
        
        this.x = (int) x;
        this.y= (int) y;
        this.width = (int) width;
        this.height = (int) height;
        
        System.out.println(x+" : "+y);
//
//        setBorder(BorderFactory.createLineBorder(Color.black));
//
//        addMouseListener(new MouseAdapter() {
//            public void mousePressed(MouseEvent e) {
//                moveSquare(e.getX(),e.getY());
//            }
//        });
//
//        addMouseMotionListener(new MouseAdapter() {
//            public void mouseDragged(MouseEvent e) {
//                moveSquare(e.getX(),e.getY());
//            }
//        });
        
    }
    
//    private void moveSquare(int x, int y) {
//        int OFFSET = 1;
//        if ((squareX!=x) || (squareY!=y)) {
//            repaint(squareX,squareY,squareW+OFFSET,squareH+OFFSET);
//            squareX=x;
//            squareY=y;
//            repaint(squareX,squareY,squareW+OFFSET,squareH+OFFSET);
//        } 
//    }
    

    public Dimension getPreferredSize() {
        return new Dimension(150,150);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);       
        g.drawString("This is my custom Panel!",10,20);
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }  
}
