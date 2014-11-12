
package graph;


public class DefaultVertex<T> implements Vertex<T> {
    private T id;
    private double x, y, dispx, dispy;
    
    public DefaultVertex(T id) {
        this.id = id;
    }
    
    public DefaultVertex(T id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
    
    public T getId() {
        return this.id;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getDispx(){
    	return this.dispx;
    }
    
    public double getDispy(){
    	return this.dispy;
    }
    
    public void setId(T id) {
        this.id = id;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public void setDispx(double disp){
    	this.dispx = disp;
    }
    
    public void setDispy(double disp){
    	this.dispy = disp;
    }
    
    @Override
    public String toString() {
        return "Id: " + this.id.toString() + ", X: " + this.x + ", Y: " + this.y;
    }
    
    @Override 
    public boolean equals(Object obj) {
        if (!(obj instanceof DefaultVertex))
            return false;
        if (obj == this)
            return true;
        return this.id.equals(((DefaultVertex)obj).id);
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
}
