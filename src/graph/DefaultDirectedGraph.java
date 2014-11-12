
package graph;

import java.util.*;


public class DefaultDirectedGraph<V, E> implements DirectedGraph<V, E> {
    
    private LinkedHashMap<V, List<V>> adjacencyMap;
    private LinkedHashSet<E> vertexEdges;
    private ArrayList<V> vertexList;
    private String name;
    
    
    public DefaultDirectedGraph() {
        adjacencyMap = new LinkedHashMap<>();
        vertexEdges = new LinkedHashSet<>();
        vertexList = new ArrayList();
    }
    
    @Override
    public boolean addEdge(E e, V source, V target) {
        if (containsEdge(e))
            return false;
        else {
            adjacencyMap.get(source).add(target);
            vertexEdges.add(e);
        }
        return true;
    }
     
    @Override
    public boolean addVertex(V v) {
        if (containsVertex(v))
            return false;
        else {
            adjacencyMap.put(v, new ArrayList<V>());
            vertexList.add(v);
        }
        return true;
    }
    
    @Override
    public boolean containsEdge(E e) {
        return vertexEdges.contains(e);
    }
    
    @Override
    public boolean containsVertex(V v) {
        return adjacencyMap.containsKey(v);
    }
    
    @Override
    public int degreeOfIncoming(V v) {  // might be a more efficient way to compute this
        int count = 0;
        for (V x : adjacencyMap.keySet()) {
            if (!x.equals(v)) {
                List<V> list = adjacencyMap.get(x);
                for (V y : list) {
                    if (y.equals(v))
                        count++;
                }
            }
        }
        return count;
    }
    
    @Override
    public int degreeOfOutgoing(V v) {
        List<V> list = adjacencyMap.get(v);
        if (list == null)
            return 0;
        else
            return list.size();
    }
    
    @Override
    public LinkedHashMap<V, List<V>> getAdjacencyMap() {
        return this.adjacencyMap;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public LinkedHashSet<E> getVertexEdges() {
        return this.vertexEdges;
    }
    
    @Override
    public int getNumEdges() {
        return this.vertexEdges.size();  
    }
    
    @Override
    public int getNumVertices() {
        return this.adjacencyMap.size();
    }
    
    @Override
    public ArrayList<V> getVertices() {
        return this.vertexList;
    }
    
    public V getVertex(V v) {
        for (V x : vertexList) {
            if (x.equals(v)) {
                return x;
            }
        }
        return null;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
}
