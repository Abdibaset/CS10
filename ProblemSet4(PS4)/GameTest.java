import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class for testing the Bacon Game
 * Creates the relationships map based on the information stored in the map from the read files class
 * Authors: Abdibaset Bare & Bilan Aden. Got help by TA Ethan and Ravin
 *
 */
public class GameTest {
    public static void main(String[] args) throws IOException {

        Graph<String, Set<String>> allRelations = new AdjacencyMapGraph<>();            //instantiate a graph
        //map with all the information read from the files

        //stores the map for storing movie ids as key, and actor names as set of values
        Map<String, Set<String>> movieId_actorName = ActorMoviesInfo.movieActorIds("PS4/movie-actors.txt", "PS4/actors.txt");
        Map<String, String> actors = ActorMoviesInfo.readActor("PS4/actors.txt"); //actor id-key, actor name- values
        Map<String, String> movies = ActorMoviesInfo.readMoviesInfo("PS4/movies.txt"); //movie id - key, actor name - values

        //insert vertices into the graph
        for(String id: actors.keySet()){
            allRelations.insertVertex(actors.get(id));
        }

        //establish relations - adding edges
        for(Map.Entry<String, Set<String>> movie_id: movieId_actorName.entrySet()){
            //looping over the list of actors twice to establish edge between them if not the same actor
            for(String actor_name: movie_id.getValue()){
                for(String costar_name: movie_id.getValue()){
                    if(!actor_name.equals(costar_name)){
                        //adding edge lebal - movie name
                        if (allRelations.hasEdge(actor_name, costar_name)) {
                            allRelations.getLabel(actor_name, costar_name).add(movies.get(movie_id.getKey()));
                        }
                        else{
                            allRelations.insertUndirected(actor_name, costar_name, new HashSet<>());
                            allRelations.getLabel(actor_name, costar_name).add(movies.get(movie_id.getKey()));
                        }
                    }
                }
            }
        }

        //testing the bfs
        Graph<String, Set<String>> bestPath = GraphLibrary.bfs(allRelations, "Kevin Bacon");
        double  num = GraphLibrary.averageSeparation(bestPath, "Kevin Bacon");
        //playing the game
        System.out.println("Testing all my methods: ");
        GameInterface.query(allRelations);

    }
}
