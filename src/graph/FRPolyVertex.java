package graph;

public class FRPolyVertex<T> extends DefaultVertex {

    private T id;
    private double x, y;
    
    public FRPolyVertex(T id){
    	super(id);
    }
    
    public FRPolyVertex(T id, double x, double y) {
        super(id);
        this.x = x;
        this.y = y;        
    }
}
