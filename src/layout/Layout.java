
package layout;

import java.util.ArrayList;
import graph.*;


public interface Layout<V, E> {
    
    public DirectedGraph<V, E> getDirectedGraph();
    
    public ArrayList<V> getVertices();
    
	public void setNextIter(int count, int iterations);
}
