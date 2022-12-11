import java.util.*;
import java.io.*;

/**
 * Authors: Abdibaset Bare & Bilan Aden. Got help by TA Ethan and Ravin
 * @param <K>key of the maps
 * @param <V> value
 */
public class ActorMoviesInfo<K, V> {

    public ActorMoviesInfo() {
    }

    /**
     * this method reads the actors text file and stores the information in a map
     * Map key - actor id(String), map value = actor name (String)
     *
     * @param pathtoFile - the pathname of the file to read
     * @throws IOException - io exception to catch if the file couldn't be found in the given path name
     */
    public static Map<String, String> readActor(String pathtoFile) throws IOException {
        Map<String, String> actorInfo = new HashMap<>();
        String[] info;                                                  //stores the info read from file
        String line;
        BufferedReader in = new BufferedReader(new FileReader(pathtoFile));
        try {
            //reading the file content
            while ((line = in.readLine()) != null) {
                info = line.split("\\|");                        //storing in an array
                actorInfo.put(info[0], info[1]);                       //adding to the map
            }
            in.close();                                                //closing file
        } catch (IOException e) {
            System.out.println("File doesn't exist");
        }
        return actorInfo;
    }

    /**
     * this method reads the movie-actor file and maps the movie ids to actor's Names
     * @param pathtoFile - the path of the file to read
     * @param pathname - the path to file with actors information
     * @throws FileNotFoundException - if file not there
     */
    public static Map<String, Set<String>> movieActorIds(String pathtoFile, String pathname) throws IOException {
        Map<String, Set<String>> movieActor = new HashMap<>();
        Map<String, String> actorInfo = readActor(pathname);            //map for actors keys --> id and values --> actor's name
        String[] idInfo;                                                //array of what is read from file
        String line;
        BufferedReader in = new BufferedReader(new FileReader(pathtoFile));
        try {
            //reading the file
            while ((line = in.readLine()) != null) {
                idInfo = line.split("\\|");
                //adding to map - key--> movie id and values --> actor name
                if (movieActor.containsKey(idInfo[0])) {
                    movieActor.get(idInfo[0]).add(actorInfo.get(idInfo[1]));
                } else {
                    movieActor.put(idInfo[0], new HashSet<>());
                    movieActor.get(idInfo[0]).add(actorInfo.get(idInfo[1]));
                }
            }
            in.close();                                                //closing file after reading
        } catch (IOException e) {
            System.out.println("File doesn't exist");
        }
        return movieActor;
    }

    /**
     * this method reads the movies.txt file and maps the movie ids to the movie names
     * @param pathtoFile - the path to the file to be read
     * @throws FileNotFoundException - the file in the path is not found
     */

    public static Map<String, String> readMoviesInfo(String pathtoFile) throws IOException {
        Map<String, String> movieInfo = new HashMap<>();
        String[] movies;                                            //Stores array of movie information
        String line;
        BufferedReader in = new BufferedReader(new FileReader(pathtoFile));
        try {
            //reading file content
            while ((line = in.readLine()) != null) {
                movies = line.split("\\|");
                movieInfo.put(movies[0], movies[1]);
            }
            in.close();                                           //closing file after reading
        } catch (IOException e) {
            System.out.println("File doesn't exist");
        }
        return movieInfo;
    }

}
