package layout;

import graph.DefaultEdge;
import graph.DefaultVertex;
import graph.FRPolyVertex;
import graph.DirectedGraph;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

//this class handles F-R layout algorithm in a polygon 
public class FRPolygonLayout implements Layout {
	private DirectedGraph<FRPolyVertex, DefaultEdge> directed;
    private double[] x, y; //input coordinates
    private Random random; 
    private double t; // temperature, which get from Windows.java
    private double C = 0.75; //parameter
    private Polygon p;
    int itime = 1; //count times the nodes meet the edge of polygon
    
	public FRPolygonLayout(DirectedGraph<FRPolyVertex, DefaultEdge> directed, double[] x, double[] y, Random random, int temp) {
        this.directed = directed;
        this.x = x;
        this.y = y;
        this.random = random;
        t = temp; 
        p = new Polygon(this.x,this.y,x.length);
        setRandomVertices(p);
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
	//this method implements the F-R algorithm to visualize the nodes in a polygon
	public void setNextIter(int count, int iterations) {		
		
		//calculate K value
		double k = getK(getArea(x,y), directed.getNumVertices());
		
		//calculate repulsive force 
		for(FRPolyVertex v : directed.getVertices()){
			v.setDispx(0);
			v.setDispy(0);
			for(FRPolyVertex u : directed.getVertices()){
				if(!u.equals(v)){
					double deltax = v.getX() - u.getX();
					double deltay = v.getY() - u.getY();
					double distance = Math.sqrt(deltax * deltax + deltay * deltay);
					double dx, dy;
					if(distance == 0){
						JOptionPane.showMessageDialog(null, "There are at least two nodes that have same position! Please stop the program and reposition nodes", "Error Input nodes", JOptionPane.ERROR_MESSAGE);
						dx = v.getDispx() + repulsiveForce(distance, k / itime);
						dy = v.getDispy() + repulsiveForce(distance, k / itime);
					}
					else {
						dx = v.getDispx() + deltax * repulsiveForce(distance, k / itime) / distance;
						dy = v.getDispy() + deltay * repulsiveForce(distance, k / itime) / distance;
					}
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
			double dx1, dy1, dx2, dy2;
			if(distance != 0){
				dx1 = e.getTarget().getDispx() - deltax * attractiveForce(distance, k) / distance;
				dy1 = e.getTarget().getDispy() - deltay * attractiveForce(distance, k) / distance;
				dx2 = e.getSource().getDispx() + deltax * attractiveForce(distance, k) / distance;
				dy2 = e.getSource().getDispy() + deltay * attractiveForce(distance, k) / distance;
				e.getTarget().setDispx(dx1);
				e.getTarget().setDispy(dy1);
				e.getSource().setDispx(dx2);
				e.getSource().setDispy(dy2);
			}	
		}

//		 //calculate central force
//		for(FRPolyVertex v : directed.getVertices()){
//			double distance = (v.getX() - p.getCenter()[0]) * (v.getX() - p.getCenter()[0]) 
//					+ (v.getY() - p.getCenter()[1]) * (v.getY() - p.getCenter()[1]);
//			distance = Math.sqrt(distance);
//			double cf =  0.01 * k * 0.01 * distance * distance;
//			double dx = v.getDispx() - cf * v.getDispx() / distance;
//			double dy = v.getDispy() - cf * v.getDispy() / distance;
//			v.setDispx(dx / 10);
//			v.setDispy(dy / 10);
//		}	
		
		//limit the maximum displacement to the temperature t
		//and then prevent from being displaced outside polygon
		for(FRPolyVertex v : directed.getVertices()){
			double delta = Math.sqrt(v.getDispx() * v.getDispx() + v.getDispy() * v.getDispy());
			double minx = Math.abs(v.getDispx()) < t ? Math.abs(v.getDispx()) : t;
			double miny = Math.abs(v.getDispy()) < t ? Math.abs(v.getDispy()) : t;
			double dipx = v.getX() + v.getDispx() / delta * minx;
			double dipy = v.getY() + v.getDispy() / delta * miny;
			v.setDispx(v.getDispx() / delta * minx);
			v.setDispy(v.getDispy() / delta * miny);							
			double[] before = {v.getX(),v.getY()};		//save previous positions 			
			v.setX(dipx);								//get current positions
			v.setY(dipy);				
			double[] inters = p.getIntersect(v);		//get intersect positions if exist, return null otherwise
			while(inters != null){						//check whether the node meet the edge of polygon and get rebound
				double[] rebound = p.getRebound(inters,v);
				v.setX(rebound[0]);
				v.setY(rebound[1]);
				v.setDispx(v.getX() - before[0]);
				v.setDispy(v.getY() - before[1]);
				inters = p.getIntersect(v);
				if(inters != null)
					itime++;
			}	
		}
		t = cool(t, count, iterations);	
	}
	
	//set initial positions of nodes
    private void setRandomVertices(Polygon p) {
    	double[] c = p.getInitial(p.nPoints);
        for (FRPolyVertex v : directed.getVertices()) {          
          v.setX(getRandomInt((int)(c[0] - 10), (int)(c[0] + 10)));
          v.setY(getRandomInt((int)(c[1] - 8), (int)(c[1] + 8)));           
        }	
    }
    
    private double getRandomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
    
    //get area of input polygon
    private double getArea(double[] x, double[] y){
    	double sum = 0;
    	for(int i = 0;i < x.length;i++){
    		sum += ((x[i % x.length] - x[(i + 1) % x.length]) * (Math.abs(y[(i + 1) % x.length]) + Math.abs(y[i % x.length])) / 2);
    	}
    	return Math.abs(sum);
    }
    
    //get K value
    private double getK(double area, int numV){
    	return C * Math.sqrt(area / (1 + numV));
    }
    
    //get attractive force represented by a displacement value
    private double attractiveForce(double distance, double k){
    	if(distance == 0)
    		return 0;
    	return distance * distance / k;
    }
    
    //get repulsive force represented by a displacement value
    private double repulsiveForce(double distance, double k){
    	if(distance == 0)
    		return 1000;
    	return k * k / distance;
    }
	
	//get cooling function
    private double cool(double temp, double count, double iterations){
      	temp *= (1 - count / iterations);
    	return temp;
    }	    
}

class Polygon {
	int nPoints;
	double[][] nodes;
	
	public Polygon(double[] x, double[] y, int nPoints){
		this.nPoints = nPoints;
		nodes = new double[nPoints][2];
		for(int i = 0;i < nPoints;i++){			
			nodes[i][0] = x[i];
			nodes[i][1] = y[i];			
		}
	}

	//get the new position when a nodes meet the edge of polygon and get rebound
	public double[] getRebound(double[] inters, FRPolyVertex v){
		int i = (int)inters[2];
		double x1 = nodes[i][0];
		double y1 = nodes[i][1];
		double x2 = nodes[(i + 1) % nPoints][0];
		double y2 = nodes[(i + 1) % nPoints][1];
		double x0 = v.getX();
		double y0 = v.getY();
		double x = (x0 * (x2 - x1) * (x2 - x1) + 2 * (y0 - y1) * (x2 - x1) * (y2 - y1) - (y2 - y1) * (y2 - y1) * (x0 - 2 * x1))
				/ ((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
		double y = (y2 == y1) ? (2 * y1 - y0) :((x2 - x1) * (x0 - x) / (y2 - y1) + y0);
		return new double[] {x, y};
	}
		
	//get the intersect, return intersection points
	public double[] getIntersect(FRPolyVertex v){
		for(int i = 0;i < nPoints;i++){	
			if(((nodes[(i + 1) % nPoints][1] - nodes[i % nPoints][1]) / (nodes[(i + 1) % nPoints][0] - nodes[i % nPoints][0]) - v.getDispy() / v.getDispx()) == 0)
				continue;			
			double x = ((nodes[(i + 1) % nPoints][1] - nodes[i % nPoints][1]) / (nodes[(i + 1) % nPoints][0] - nodes[i % nPoints][0]) * nodes[i % nPoints][0] - nodes[i % nPoints][1] - v.getDispy() / v.getDispx() * v.getX() + v.getY()) 
					/ ((nodes[(i + 1) % nPoints][1] - nodes[i % nPoints][1]) / (nodes[(i + 1) % nPoints][0] - nodes[i % nPoints][0]) - v.getDispy() / v.getDispx());
			double y = nodes[i % nPoints][1] + (nodes[i % nPoints][1] - nodes[(i + 1) % nPoints][1]) / (nodes[i % nPoints][0] - nodes[(i + 1) % nPoints][0]) * (x - nodes[i % nPoints][0]);
			double[] quax = {nodes[i % nPoints][0], v.getX() - v.getDispx(), nodes[(i + 1) % nPoints][0], v.getX()};
			double[] quay = {nodes[i % nPoints][1], v.getY() - v.getDispy(), nodes[(i + 1) % nPoints][1], v.getY()};						
			boolean b = inQua(x,y,quax,quay);
			if(b){
				return new double[] {x,y,i};
			}
		}
		return null;
	}
			
	//get the center of polygon
	private double[] getCenter(){		
		double cx = 0,cy = 0;		
		for(int i = 0; i < nPoints;i++){			
			cx += nodes[i][0];
			cy += nodes[i][1];
		}
		cx /= nPoints;
		cy /= nPoints;
		return new double[] {cx,cy};
	}
	
	//get the initial positions of points
	public double[] getInitial(int num){		
		double cx = 0,cy = 0;		
		for(int i = 0; i < num;i++){			
			cx += nodes[i][0];
			cy += nodes[i][1];
		}
		cx /= num;
		cy /= num;
		double[] p = {cx,cy};
		if(!inPoly(p)){
			int j = getNear(p);
			 for(int i = 0; i < num;i++){
				 if(i == j) continue;
				 if(Math.abs(i - (j % num)) == 1)continue;
				 cx = (nodes[i][0] + nodes[j][0]) / 2;
				 cy = (nodes[i][1] + nodes[j][1]) / 2;
				 p = new double[] {cx,cy};
				 if(inPoly(p)) break;
			 }
		}
		return p;
	}
	
	//get the vertex which is nearest to the given point (only used in initializing points)
	private int getNear(double[] p){
		int near = 0;
		double min = 1000000;
		for(int i = 0;i < nPoints;i++){
			double dis = (nodes[i][0] - p[0]) * (nodes[i][0] - p[0]) + (nodes[i][1] - p[1]) * (nodes[i][1] - p[1]);
			if(dis < min){
				near = i;
				min = dis;
			}
		}
		return near;
	}
	
	//check whether a given point is in polygon or not (only used in initializing points)
	private boolean inPoly(double[] p){
		int nCross = 0;
	    for(int i = 0;i < nPoints;i++){
	    	if(nodes[i][1] == nodes[(i + 1) % nPoints][1])
    			continue;
    		double min = nodes[i][1] < nodes[(i + 1) % nPoints][1] ? nodes[i][1] : nodes[(i + 1) % nPoints][1];
    		double max = nodes[i][1] > nodes[(i + 1) % nPoints][1] ? nodes[i][1] : nodes[(i + 1) % nPoints][1];
    		if (p[1] < min)
                continue;
    		if(p[1] >= max)
    			continue;
    		double x = (p[1] - nodes[i][1]) * (nodes[(i + 1) % nPoints][0] - nodes[i][0]) / (nodes[(i + 1) % nPoints][1] - nodes[i][1]) + nodes[i][0];
            if(x > p[0])  
            	nCross++; 
	    }
	    return (nCross % 2 == 1);
	}
	
	//check whether a given point in a given quadrilateral
    private boolean inQua(double x, double y, double[] nodex, double[] nodey){
    	boolean bv = ((x - nodex[1]) * (x - nodex[3]) <= 0) ? true : false;
		boolean bp = ((x - nodex[0]) * (x - nodex[2]) <= 0) ? true : false;
		boolean byv = ((y - nodey[1]) * (y - nodey[3]) <= 0) ? true : false;
		boolean byp = ((y - nodey[0]) * (y - nodey[2]) <= 0) ? true : false;			
		
		if(bv && bp && byv && byp){
			return true;
		}	
		else{
			return false;
		}
    }
}

