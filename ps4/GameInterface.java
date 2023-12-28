import java.util.*;

/**
 * Authors: Abdibaset Bare & Bilan Aden. Got help by TA Ethan and Ravin
 * @param <V> data type for vertex
 * @param <E> data type for edge label
 */
public class GameInterface<V, E> {

    public static boolean mode = true;              //game mode true -
    public static String currentCenter;             //saves the current center

    /**
     * constructor
     */
    public GameInterface(){ }

    /**
     * enables user to choose command and calls the appropriate method depending on user input
     * @param relations - main graph built from actor-movie files
     */
    public static <V, E> void query(Graph<V, E> relations){
        Graph<V, E> newGraph = null;
        System.out.println("Hello, select one of the following commands to , for the commands with 'or' select only one\n"+
                "c: list top (positive number) or bottom (negative) centers of the universe, sorted by average separation\n" +
                "dl or dh: list actors sorted by degree, with degree between low and high\n" +
                "i: list actors with infinite separation from the current center\n" +
                "p <name>: find path from <name> to current center of the universe\n" +
                "sl or sh: list actors sorted by non-infinite separation from the current center, with separation between low and high\n" +
                "u <name>: make <name> the center of the universe\n" +
                "q: quit game");

        Scanner scanQuery = new Scanner(System.in);                         //promoting and reading userinput

        while(mode) {
            if(newGraph != null){
                System.out.println();
                System.out.println(currentCenter+ " game >");}
            String input = scanQuery.nextLine();
                //listing top - command c
            if (Objects.equals(input, "c")) {
                System.out.println("Specify the lower-cutoff for the sorted by selecting a positive(low to high) or negative(high to low) to number");
                Scanner n1 = new Scanner(System.in);                        //scan user input for the lower boundary
                int cutoff = n1.nextInt();
                if(cutoff == 0) { cutoff = 1;}
                distanceFromCenter(relations, newGraph, cutoff);   //call for the function
            }
            //sorting from lowest degree to highest
            else if (Objects.equals(input, "dl")) {
                listSortedbyDegree(relations, newGraph,"dl");
            }
            //sorting from highest degree to lowest
            else if (Objects.equals(input, "dh")) {
                listSortedbyDegree(newGraph, newGraph,"dh");
            }
            //listing the missing vertices in a graph
            else if (Objects.equals(input, "i")) {
                infiniteSeparation(relations, newGraph);
            }
            //listing all the relations from actor to the center with all the labels
            else if (Objects.equals(input, "p")) {
                System.out.println("Enter actor's name <Vertex Name>: ");
                //promoting and reading the actor name
                Scanner scan = new Scanner(System.in);                      //scanning the user input
                V actorName = (V) scan.nextLine();
                pathToCenter(newGraph, actorName);
            }

            //sorting the actors based on non-infinite separation from lowest to highest based on average separation
            else if (Objects.equals(input, "sl")) {
                non_infinitteSep(relations,newGraph, "sl");
            }

            //sorting the actors based on non-infinite separation from highest to lowest based on average separation
            else if (Objects.equals(input, "sh")) {
                non_infinitteSep(relations, newGraph, "sh");
            }
            //change the center to the user's desired center
            else if (Objects.equals(input, "u")) {
                System.out.println("Enter actor's name: ");
                Scanner scan = new Scanner(System.in);
                V name = (V) scan.nextLine();
                newGraph = makeCenter(relations, name);
            }
            //quiting the game option
            else if(Objects.equals(input, "q")){
                quit();
            }
            //if user selects an input
            else{
                System.out.println("Invalid input, try again");
            }
        }
    }

    /**
     * method prints the actor of based on average separation from lowest separation to highest
     * @param relations - graph with all the relations information
     * @param newGraph - graph built by the user with the user's desired center
     * @param cutOff - if negative sorts from high to low and vice verse if greater than zero
     */

    public static <V, E> void distanceFromCenter(Graph<V, E> relations, Graph<V, E> newGraph, int cutOff){
        //if the relations graph is not established
        if(relations == null || newGraph == null) System.out.println("No relations established, define center by selecting u");

        //if the graph has relations
        else {
            Map<String, Integer> verticesInPath = new HashMap<>();              //map for all vertices - keys- vertices, value - distance from current center
           //add all vertices and distance from center
            for(V actor_name: relations.vertices()){
                List<V> path = GraphLibrary.getPath(newGraph, actor_name);
                verticesInPath.put((String)actor_name, path.size()-1);
            }
            //comparator class to compares the distance between the vertices in the graph
            class DistanceComparator implements Comparator<String> {
                @Override
                public int compare(String s1, String s2) {
                    int compareVal = 0;
                    if(cutOff < 0) compareVal =verticesInPath.get(s1)-verticesInPath.get(s2); //sorts from lowest to highest
                    else if(cutOff > 0) compareVal = verticesInPath.get(s2)-verticesInPath.get(s1); //sorts from highest to lowest
                    return compareVal;
                }
            }
            // add all actors by comparing their average separation to the pq
            PriorityQueue<String> pq = new PriorityQueue<String>(new DistanceComparator());
            for (V actor: relations.vertices()) {
                if (newGraph.hasVertex(actor)){         //adding to priority queue if all the vertices are in main graph and desired graph
                        pq.add((String) actor);
                }
            }
            //adding the vertices to the array based on the distance from the center
            ArrayList<String> sortedList = new ArrayList<String>();
            while (!pq.isEmpty()){ sortedList.add(pq.remove());}
            System.out.println("Sorted list based on distance from center of the current universe: ");
            System.out.println();
            System.out.println(sortedList);
        }
    }

    /**
     * accommodates for two commands, sorted by highest degree to lowest degree as chosen by the user
     * sorts based on outDegrees from the
     * @param relations - the main graph with the relationships
     * @param newGraph - the graph built by user with the desired center
     * @param direction - either low degree or high degree(user input)
     */
    public static <V, E> void listSortedbyDegree(Graph<V, E> relations, Graph<V, E> newGraph, String direction){
        //if the main graph or the desired graph is empty
        if(relations == null || newGraph == null) System.out.println("No relations established");
        else {
            Map<String, Integer> degreesSep = new HashMap<>();      //map: keys - vertices, values - the number of outDegrees
            for(V actor : relations.vertices()){
                degreesSep.put((String)actor, relations.outDegree(actor));
            }
            //comparator that sorts from highest outDegree to lowest (dh) or verse (dl)
            class DegreeComparator implements Comparator<String> {
                @Override
                public int compare(String s1, String s2) {
                    // top (positive value) means low average separation
                    int compareVal = 0;
                    if(direction.equals("dl")) compareVal = degreesSep.get(s1)-degreesSep.get(s2); //sorts from lowest to highest num of outDegrees
                    else if(direction.equals("dh")) compareVal = degreesSep.get(s2)-degreesSep.get(s1); //sorts from highest to lowest num of outDegrees
                    return compareVal;
                }
            }
            // add all actors by comparing their average separation to the pq
            PriorityQueue<String> pq = new PriorityQueue<String>(new DegreeComparator());
            for (V actor: relations.vertices()) {
                if (newGraph.hasVertex(actor)){ //added to priority queue if both in main graph and desired graph built by user
                        pq.add((String) actor);
                }
            }
            //removing from the priority queue and adding to the list
            ArrayList<String> sortedList = new ArrayList<String>();
            while (!pq.isEmpty()){sortedList.add(pq.remove());}
            System.out.println("The list of actors after sorting based on the number of outDegrees from center of universe: ");
            System.out.println();
            System.out.println(sortedList);
        }
    }


    /**
     * this method finds the vertices that are at infinite separation by calling the missingVertices
     * @param relations - main graph
     * @param newGraph - subgraph that we have built with command u about
     */
    public static <V, E> void infiniteSeparation(Graph<V, E> relations, Graph<V, E> newGraph){
        //either the main graph or the desired graph with desired center
       if(newGraph==null || relations == null) {
			System.out.println("To see relations, define the center by selecting 'u'");
		}
       //prints the list of missing vertices from the graph
		else {
			System.out.println("Infinite separation from current center");
			Set<V> infinite = GraphLibrary.missingVertices(relations,newGraph);
            System.out.println("The following vertices have infinite separation");
            System.out.println();
			System.out.println(infinite);
		}
    }

    /**
     * method that prints the all the vertices from a node(user input) to the current center of the graph
     * @param relations - main graph to traverse
     * @param name - the node to traverse back to the center of the main graph
     */
    public static <V, E> void pathToCenter(Graph<V, E> relations, V name){
        //if the graph is empty
        if(relations == null) {
            System.out.println("There is no graph, create graph of relations by selecting command 'u'");
        }
        else if(!relations.hasVertex(name)) {
            System.out.println("Unfortunately, There are no relations provided, select 'u' to define relations");
        }
        else{
            //backtracking the path to the center of the universe
            System.out.println(name + " number is "+ (GraphLibrary.getPath(relations, name).size()-1));
            if(GraphLibrary.getPath(relations, name).size()-1 != 0){
                int len = GraphLibrary.getPath(relations, name).size()-1;
                //looping through all the connections
                List<V> path =  GraphLibrary.getPath(relations, name);
                for(int i = 0; i < len; i++){
                    System.out.println(path.get(i) + " appeared in " + relations.getLabel(path.get(i), path.get(i+1))+ " with " + path.get(i+1));
                }
            }
        }
    }

    /**
     * sorts and prints all the based on the average separation
     * @param relations - main graph to traverse
     * @param choice - sorting criterion as desired by the user
     */
    public static <V, E> void non_infinitteSep(Graph<V, E> relations, Graph<V, E> newGraph, String choice) {
        if (relations == null || newGraph == null) System.out.println("No relationships between actors established, select 'u'");
        else {
            Map<String, Double> sortedByAve = new HashMap<>();
            for(V actor: relations.vertices()){
                sortedByAve.put((String)actor, GraphLibrary.averageSeparation(relations, actor));
            }
            //comparator that sorts from highest outDegree to lowest (dh) or verse (dl)
            class SepComparator implements Comparator<String> {
                @Override
                public int compare(String s1, String s2) {
                    // top (positive value) means low average separation
                    int compareVal = 0;
                    if(choice.equals("sl")) compareVal = Double.compare(sortedByAve.get(s1), sortedByAve.get(s2)); //sorts from lowest to highest sep
                    else if(choice.equals("sh")) compareVal = Double.compare(sortedByAve.get(s2), sortedByAve.get(s1)); //sorts from highest to lowest sep
                    return compareVal;
                }
            }
            // add all actors by comparing their average separation to the pq
            PriorityQueue<String> pq = new PriorityQueue<String>(new SepComparator());
            for (V actor: relations.vertices()) {
                if (newGraph.hasVertex(actor)){ //added to priority queue if both in main graph and desired graph built by user
                    pq.add((String)actor);
                }
            }
            //removing from the priority queue and adding to the list
            ArrayList<String> sortedList = new ArrayList<String>();
            while (!pq.isEmpty()){sortedList.add(pq.remove());}
            System.out.println("Sorted list based on non_infinite separation fron current center: ");
            System.out.println();
            System.out.println(sortedList);
        }
    }

    /**
     * changes the center to the user's desired center
     * @param relations - main graph to traverse
     * @param name - the new center as specified by the user
     */
    public static <V, E> Graph<V, E> makeCenter(Graph<V, E> relations, V name){
        //if the vertex given is not the relationship graph
       if(!relations.hasVertex(name)) {
            System.out.println("There are no relations established between actors");
            return null;
        }
        //check if the center is the graph
        else{
            Graph<V, E> center = GraphLibrary.bfs(relations, name);  //new graph with the user input name as the center
            //creating the new graph with the new center - name
           currentCenter = (String)name;
            System.out.println(name + " is now the center of the acting universe, connected to " + (center.numVertices()-1)+ "/9235 "+
                    "actors with average separation " + GraphLibrary.averageSeparation(center, name));
            return center;
        }
    }

    /**
     * this is the quit method that reverses the game mood when the user selects q
     */
    public static void quit(){
        System.out.println("That's the end, thank you!");
        mode = false;
    }
}
