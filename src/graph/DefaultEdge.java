
package graph;


public class DefaultEdge<V> implements Edge<DefaultVertex> {
    private DefaultVertex source, target;
    private boolean directed;
    
    public DefaultEdge(DefaultVertex source, DefaultVertex target, boolean directed) {
        this.source = source;
        this.target = target;
        this.directed = directed;
    }
    
    public DefaultVertex getSource() {
        return this.source;
    }

    public DefaultVertex getTarget() {
        return this.target;
    }
    
    @Override
    public String toString() {
        return "Source: " + source.toString() + ", Target: " + target.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DefaultEdge))
            return false;
        if (obj == this)
            return true;
        if (directed)  // perhaps this should be made into separate implementations...
            return (this.source.equals(((DefaultEdge)obj).source) && this.target.equals(((DefaultEdge)obj).target));
        else
            return (this.source.equals(((DefaultEdge)obj).source) && this.target.equals(((DefaultEdge)obj).target)) || 
                (this.source.equals(((DefaultEdge)obj).source) && this.target.equals(((DefaultEdge)obj).target));
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
}
