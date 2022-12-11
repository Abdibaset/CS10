import java.io.IOException;
import java.lang.reflect.Array;
import java.net.Inet4Address;
import java.util.*;

/**
 * Authors: Abdibaset Bare & Bilan Aden. Got help by TA Ethan and Ravin
 * @param <V> data type for vertex
 * @param <E> data type for edge label
 */
public class GraphLibrary<V, E> {

    /**
     * constructor - nothing initialized
     */
    public GraphLibrary() {
    }
    /**
     * bfs - builds the graph given a actor's name
     *
     * @param gr     - parsed in graph to build vertices and edges from
     * @param source - the center of the graph to be built
     * @param <V>    - the value of the vertices
     * @param <E>    - the edge label
     * @return - the graph of relationships between actors with the user's desired center
     */
    public static <V, E> Graph<V, E> bfs(Graph<V, E> gr, V source) {
        if (!gr.hasVertex(source)) {
            System.out.println("There are no relationships between actors established");
            return new AdjacencyMapGraph<>();
        }
        //BFS technique to visit all the vertices in the graph
        Graph<V, E> createdPath = new AdjacencyMapGraph<>();         //new graph with a diff center - source
        Set<V> visited = new HashSet<>();                            //keeps track of visited vertices
        Queue<V> queue = new LinkedList<>();
        //adding the desired center to the graph, set and queue
        createdPath.insertVertex(source);
        queue.add(source);
        visited.add(source);
        //repeat until we find the goal vertex or the queue is empty:
        while (!queue.isEmpty()) {
            V u = queue.remove();                                    //dequeue the next vertex u from the queue
            //loop over all neighbors
            for (V v : gr.outNeighbors((u))) {
                if (!visited.contains(v)) {
                    queue.add(v);
                    visited.add(v);
                    createdPath.insertVertex(v);                            //add neighbor to the graph
                    createdPath.insertDirected(v, u, gr.getLabel(v, u));    //adding and setting the edge label to null
                }
            }
        }
        return createdPath;                                             //returning the graph
    }


    /**
     * method traverse the tree from the parsed vertices to the center adding all the vertices in the way to a list
     *
     * @param tree - the tree to traverse
     * @param v    - the vertex/actor to traverse from to the center of the graph
     * @param <V>  - data type of the vertex
     * @param <E>  - the data type of the edge label
     * @return - list with all the vertices from v to the center of the graph
     */
    public static <V, E> List<V> getPath(Graph<V, E> tree, V v) {
        //if tree is empty or doesn't contain the desired vertex v
        if (tree.numVertices() == 0) {
            System.out.println("Graph is empty, establish relations by selecting ");
        }
        if (!tree.hasVertex(v)) {
            return new ArrayList<>();
        }
        List<V> path = new ArrayList<>();                           //list to add all vertices in the way
        V curr = v;                                                 //where to traverse from
        path.add(v);
        //while there are outNeighbors of current vertex
        while (tree.outDegree(curr) > 0) {
            curr = tree.outNeighbors(curr).iterator().next();      //iterator through the neighbors of the current vertex
            path.add(curr);                                       //append current vertex to the list
        }
        return path;
    }

    /**
     * finds the number of vertices missing from a given subgraph based on the main graph(graph
     *
     * @param graph    - the main graph
     * @param subgraph - the desired subgraph that has been passed in
     * @param <V>      - data type of the vertex
     * @param <E>      - data type of the edge label
     * @return set of all vertices missing from the subgraph that are in the graph
     */
    public static <V, E> Set<V> missingVertices(Graph<V, E> graph, Graph<V, E> subgraph) {
        //if either of the main graph or sub-graphs are empty
        if (graph.numVertices() == 0 || subgraph.numVertices() == 0) return new HashSet<>();
        Set<V> nodes = new HashSet<>();
        //adding nodes in graph but not in subgraph to the set
        for (V v : graph.vertices()) {
            if (!subgraph.hasVertex(v)) {
                nodes.add(v);
            }
        }
        return nodes;                           //vertices missing in the sub-graph
    }

    /**
     * @param tree - graph to traverse to add up the number of steps
     * @param root - the center of the main graph to traverse
     * @param <V>  - the data type of the vertex
     * @param <E>  - the data type of the edge label
     * @return a float num for the average separation of all vertices from the center of the graph
     */

    public static <V, E> double averageSeparation(Graph<V, E> tree, V root) {
        //if the center doesn't exist or null
        if (!tree.hasVertex(root) || tree.numVertices() == 0) return -1; //if the graph doesn't cotain the vertex to traverse from
        double steps = helperAverage(tree, root, 0, new HashSet<V>()); //total steps from the start
        return steps / (tree.numEdges());    //average separation from current node
    }

    /**
     * accumulator that adds all the steps from the given node
     * @param tree - the backtrack tree created using bfs
     * @param root - the start of the graph
     * @param steps - accumulators for the number of steps from the center
     * @param visited - set for keeping track of all visited nodes
     * @return total steps
     * @param <V> - data type for the vertex
     * @param <E> - data type for the edge.
     */
    public static <V, E> double helperAverage(Graph<V, E> tree, V root, double steps, HashSet<V> visited) {
        double total = steps;                           //accumulator for the number of steps
        visited.add(root);                              //set if the tree has undirected edges
        //looping over all the neighbors of each vertex.
        for (V v : tree.inNeighbors(root)){
            if(!visited.contains(v)) {                  //check if the vertices are visited
                total += helperAverage(tree, v, steps + 1, visited);//accumulating the number of steps each run
            }
        }
        return total;
    }
}