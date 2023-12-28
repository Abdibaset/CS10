/**
 * Test Class created by Abdibaset
 */
public class RelationshipsTest1 {
    public static void main(String[] args) {
        Graph<String, String> relations = new AdjacencyMapGraph<>();

        //adding vertices to the map and establishing relations between them.
        relations.insertVertex("A");
        relations.insertVertex("B");
        relations.insertVertex("C");
        relations.insertVertex("D");
        relations.insertVertex("E");

        //creating directed vertices
        relations.insertDirected("A", "B", "friend");
        relations.insertDirected("A", "C", "coworker");
        relations.insertDirected("A", "D", "follower");
        relations.insertDirected("A", "E", "follower");
        relations.insertDirected("B", "A", "follower");
        relations.insertDirected("B", "C", "follower");
        relations.insertDirected("C", "A", "follower");
        relations.insertDirected("C", "B", "Acquaintance");
        relations.insertDirected("C", "D", "friend");
        relations.insertDirected("E", "B", "follower");
        relations.insertDirected("E", "C", "follower");
        System.out.println("Random steps walk from vertex 'C': "+ GraphLib.randomWalk(relations, "C", 4));
        System.out.println("Vertices sorted by inDegree: " + GraphLib.verticesByInDegree(relations));
    }
}
