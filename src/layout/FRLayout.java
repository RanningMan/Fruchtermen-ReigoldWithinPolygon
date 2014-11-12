package layout;

import java.util.ArrayList;
import java.util.Random;

import graph.DefaultEdge;
import graph.DefaultVertex;
import graph.DirectedGraph;
import graph.FRPolyVertex;

/*
 * this class contains the implementation and layout of F-R algorithm
 */
public class FRLayout implements Layout {
	
	private DirectedGraph<DefaultVertex, DefaultEdge> directed;
    private int width, height;
    private Random random;
    private double t;
    private double C = 0.75;
    
    public FRLayout(DirectedGraph<DefaultVertex, DefaultEdge> directed, int width, int height, Random random, double temp) {
        this.directed = directed;
        this.width = width - 30;
        this.height = height - 40;
        this.random = random;
        this.t = temp;
        setRandomVertices();
    }
    
    public void setTemp(double t){
    	this.t = t;
    }
    
    public double getTemp(){
    	return t;
    }
    
    private void setRandomVertices() {
        for (DefaultVertex v : directed.getVertices()) {
            v.setX(getRandomInt(width / 2 - 10, width / 2 + 10));
            v.setY(getRandomInt(height / 2 - 8, height / 2 + 8));
        }	
    }
    
    private int getRandomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
    
    private double getArea(int width, int height){
    	return width * height;
    }
    
    private double getK(double area, int numV){
    	return C * Math.sqrt(area / numV);
    }
    
    private double attractiveForce(double distance, double k){
    	return distance * distance / k;
    }
    
    private double repulsiveForce(double distance, double k){
    	return k * k / distance;
    }
    
	@Override
	public DirectedGraph getDirectedGraph() {
		return this.directed;
	}
	
	@Override
	public ArrayList getVertices() {
		return directed.getVertices();
	}
	
	@Override
	//this method implements the F-R algorithm 
	public void setNextIter(int count, int iterations) {

		double k = getK(getArea(width, height), directed.getNumVertices());

//		double k = getK(1000, directed.getNumVertices());

		
		//calculate repulsive force 
		for(DefaultVertex v : directed.getVertices()){
			v.setDispx(0);
			v.setDispy(0);
			for(DefaultVertex u : directed.getVertices()){
				if(!u.equals(v)){
					double deltax = v.getX() - u.getX();
					double deltay = v.getY() - u.getY();
					double distance = Math.sqrt(deltax * deltax + deltay * deltay);
					double dx = v.getDispx() + deltax * repulsiveForce(distance, k) / distance;
					double dy = v.getDispy() + deltay * repulsiveForce(distance, k) / distance;
					v.setDispx(dx);
					v.setDispy(dy);
				}
			}
		}
		
		//calculate attractive force
		for(DefaultEdge e : directed.getVertexEdges()){
			double deltax = e.getTarget().getX() - e.getSource().getX();
			double deltay = e.getTarget().getY() - e.getSource().getY();
			double distance = Math.sqrt(deltax * deltax + deltay * deltay);
			double dx1 = e.getTarget().getDispx() - deltax * attractiveForce(distance, k) / distance;
			double dy1 = e.getTarget().getDispy() - deltay * attractiveForce(distance, k) / distance;
			double dx2 = e.getSource().getDispx() + deltax * attractiveForce(distance, k) / distance;
			double dy2 = e.getSource().getDispy() + deltay * attractiveForce(distance, k) / distance;
			e.getTarget().setDispx(dx1);
			e.getTarget().setDispy(dy1);
			e.getSource().setDispx(dx2);
			e.getSource().setDispy(dy2);
		}
		
		// calculate central force
//		for(DefaultVertex v : directed.getVertices()){
//			double distance = (v.getX() - 410) * (v.getX() - 410) 
//					+ (v.getY() - 105) * (v.getY() - 105);
//			distance = Math.sqrt(distance);
//			double cf =  0.01 * k * 0.01 * distance * distance;
//			double dx = v.getDispx() - cf * v.getDispx() / distance;
//			double dy = v.getDispy() - cf * v.getDispy() / distance;
//			v.setDispx(dx / 10);
//			v.setDispy(dy / 10);
//			
//			System.out.println(v.getDispx() + "," + v.getDispy());
//			
//		}	

		//limit the maximum displacement to the temperature t
		//and then prevent from being displaced outside frame
		for(DefaultVertex v : directed.getVertices()){
			double delta = Math.sqrt(v.getDispx() * v.getDispx() + v.getDispy() * v.getDispy());
			double minx = Math.abs(v.getDispx()) < t ? Math.abs(v.getDispx()) : t;
			double miny = Math.abs(v.getDispy()) < t ? Math.abs(v.getDispy()) : t;
			double maxx = (v.getX() + v.getDispx() / delta * minx) > 0 ? (v.getX() + v.getDispx() / delta * minx) : 0.000000001;
			double maxy = (v.getY() + v.getDispy() / delta * miny) > 0 ? (v.getY() + v.getDispy() / delta * miny) : 0.000000001;
			v.setX(width > maxx ? maxx : width);
			v.setY(height > maxy ? maxy : height);	
		}
		
//		t = cool(t, count, iterations);
		
	}
	
    private double cool(double temp, double count, double iterations){
    	temp *= (1 - count / iterations);
    	return temp;
    }
    
}
