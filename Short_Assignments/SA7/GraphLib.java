import java.util.*;

/**
 * Library for graph analysis
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2016
 * 
 */
public class GraphLib {
	/**
	 * Takes a random walk from a vertex, up to a given number of steps
	 * So a 0-step path only includes start, while a 1-step path includes start and one of its out-neighbors,
	 * and a 2-step path includes start, an out-neighbor, and one of the out-neighbor's out-neighbors
	 * Stops earlier if no step can be taken (i.e., reach a vertex with no out-edge)
	 * @param g		graph to walk on
	 * @param start	initial vertex (assumed to be in graph)
	 * @param steps	max number of steps
	 * @return		a list of vertices starting with start, each with an edge to the sequentially next in the list;
	 * 			    null if start isn't in graph
	 */
	public static <V,E> List<V> randomWalk(Graph<V,E> g, V start, int steps) {
		// TODO: your code here
		List<V> path = new ArrayList<>();        //list to store nodes for the given num of steps
		path.add(start);						//adding the starting point of the random walk path
		for(int i = 0; i < steps -1; i++){		//looping over all the steps given
			if(g.outDegree(start) > 0){			//check if there are outNeighbors of the current node
				ArrayList<V> allNeighbors = new ArrayList<>();	//list to store all the neighbors
				//looping over the neighbors
				for(V node : g.outNeighbors(start)){
					allNeighbors.add(node);
				}
				V randomNode = allNeighbors.get((int) (allNeighbors.size() * Math.random())); //randomly selecting a vertex
				path.add(randomNode);
				start = randomNode;//changing the current position to traverse to the next in the graph
			}
		}
		return path;

	}
	
	/**
	 * Orders vertices in decreasing order by their in-degree
	 * @param g		graph
	 * @return		list of vertices sorted by in-degree, decreasing (i.e., largest at index 0)
	 */
	public static <V,E> List<V> verticesByInDegree(Graph<V,E> g) {
		// TODO: your code here
		List<V> paths = new ArrayList<>();

		if(g.numVertices() == 0){ return null;}	//if they are no vertices
		else {//adding all the node to the empty list
			for(V node : g.vertices()) paths.add(node);
		}

		//sorts the nodes from lowest to highest number of inDegree
		paths.sort((V vertexOne, V vertexTwo) -> g.inDegree(vertexTwo) - g.inDegree(vertexOne));
		return paths;
	}
}
