
package graph;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;


public interface Graph<V, E> {
    
    public boolean addEdge(E e, V source, V target);

    public boolean addVertex(V v);

    public boolean containsEdge(E e);

    public boolean containsVertex(V v);
    
    public LinkedHashMap<V, List<V>> getAdjacencyMap();
    
    public String getName();
    
    public LinkedHashSet<E> getVertexEdges();
    
    public int getNumVertices();
    
    public int getNumEdges();
    
    public ArrayList<V> getVertices();
    
    public void setName(String name);
    
}
