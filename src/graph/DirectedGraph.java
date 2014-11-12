
package graph;


public interface DirectedGraph<V, E> extends Graph<V, E> {
    
     public int degreeOfIncoming(V vertex);
     
     public int degreeOfOutgoing(V vertex);
     
}
