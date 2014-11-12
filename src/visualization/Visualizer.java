
package visualization;

import layout.Layout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

import graph.*;


// this class handles drawing the graph, mouse events, and timer
public class Visualizer extends JPanel implements Visualization, MouseListener, MouseMotionListener {
    
    private JTextArea outputText;
    private int radius = 2, count, iterations;
    private boolean drag = false;
    private double[] x,y;
    private DefaultVertex movableVertex = null;
    private Layout<DefaultVertex, DefaultEdge> layout;
    private Timer timer;
    
    public Visualizer(Layout layout, JTextArea outputText, int iterations) {
        setBackground(Color.WHITE);
        this.layout = layout;
        this.outputText = outputText;
        this.iterations = iterations;
        this.count = 0;
        addMouseListener(this);
        addMouseMotionListener(this);
        
        timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintNextIter();
            }
        });
        
        timer.start();
    }
    
    public Visualizer(Layout layout, JTextArea outputText, int iterations, double[] x, double[] y){
    	setBackground(Color.WHITE);
        this.layout = layout;
        this.outputText = outputText;
        this.iterations = iterations;
        this.count = 0;
        this.x = x;
        this.y = y;
        addMouseListener(this);
        addMouseMotionListener(this);
        
        timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintNextIter();
            }
        });
        
        timer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Dimension d = getSize();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        //draw polygon 
        if(x != null && y != null){
        	int[] nx = new int[x.length];
        	int[] ny = new int[x.length];
        	for(int i = 0;i < x.length;i++){
        		nx[i] = (int)x[i];
        		ny[i] = (int)y[i];
        	}        	
        	g2d.drawPolygon(nx, ny, x.length);
        }
        
        // draw edges
        g2d.setPaint(Color.BLUE);
        for (DefaultVertex v : layout.getDirectedGraph().getAdjacencyMap().keySet()) {
            DefaultVertex source = layout.getVertices().get(layout.getVertices().indexOf(v));
            for (DefaultVertex t : layout.getDirectedGraph().getAdjacencyMap().get(v)) {
                DefaultVertex target = layout.getVertices().get(layout.getVertices().indexOf(t));
                g2d.drawLine((int)source.getX()+radius, (int)source.getY()+radius, (int)target.getX()+radius, (int)target.getY()+radius);
            }
        }
        
        // draw vertices
        for (DefaultVertex v : layout.getVertices()) {
        	g2d.setPaint(Color.BLACK);
            Ellipse2D.Double circle = new Ellipse2D.Double(v.getX(), v.getY(), 2*radius, 2*radius); 
            g2d.fill(circle);
            g2d.setPaint(Color.BLACK);
        	circle = new Ellipse2D.Double(v.getX(), v.getY(), 2*radius, 2*radius);
            g2d.draw(circle);
        }
    }
    
    private void paintNextIter() { 
        count++;
        if (count > iterations)
            timer.stop();
        layout.setNextIter(count, iterations);
        repaint();
    }
    
    private DefaultVertex clickedVertex(MouseEvent e) {
        for (DefaultVertex v : layout.getVertices()) {
            if ((e.getX() < (v.getX()+radius) + radius && e.getX() > (v.getX()+radius) - radius) && (e.getY() > (v.getY()+radius) - radius && e.getY() < (v.getY()+radius) + radius)) {
                return v;
            }
        }
        return null;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        DefaultVertex v = clickedVertex(e);
        if (v != null) {
            drag = true;
            movableVertex = v;
        }
    }
     
    @Override
    public void mouseReleased(MouseEvent e) {
        if (drag)
            drag = false;
    }
     
    public void mouseEntered(MouseEvent e) {
    }
     
    @Override
    public void mouseExited(MouseEvent e) {
    }
     
    @Override
    public void mouseClicked(MouseEvent e) {
        DefaultVertex v = clickedVertex(e);
        if (v != null)
            outputText.append("Vertex Id: " + v.getId() + ", Degree of incoming: " + layout.getDirectedGraph().degreeOfIncoming(v) + ", Degree of outgoing: " + layout.getDirectedGraph().degreeOfOutgoing(v) + "\n");
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (drag) {
            movableVertex.setX(e.getX() - radius);
            movableVertex.setY(e.getY() - radius);
            repaint();
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        
    }
}
