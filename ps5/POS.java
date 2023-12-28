import java.io.*;
import java.util.*;

/**
 * Hidden Markov Models to determine the most appropriate transitions from a given of speech
 * @authors Abdibaset Bare and Bilan Aden
 * @got help for the following TAs - Natasha Kapadia and Avery Fogg
 */
public class POS {
    public static String start = "#";                                   //designed start state
    public static Map<String, Map<String, Double>> transitionsMap;      //stores all the transitions and their frequencies
    public static Map<String, Map<String, Double>> observationsMap;     //stores all words observations and the frequency


    public POS(){
    }


    /**
     * reading the train files with tags and sentences to map transitions from current to nextStates, and the tags to words
     * @param tagsFilePath - the training file path with tags
     * @param sentencesFilePath -training file with sentences
     * @throws IOException - i/o exception if there is nothing to read in the files given
     */
    public static void NFAtrain(String tagsFilePath, String sentencesFilePath) throws IOException {
        try {
            BufferedReader in = new BufferedReader(new FileReader(tagsFilePath));//tags file
            BufferedReader inSentences = new BufferedReader(new FileReader(sentencesFilePath));//sentence file
            transitionsMap = new HashMap<>();
            observationsMap = new HashMap<>();
            String line;
            ArrayList<String[]> tagsList = new ArrayList<>();   //stores all the lines with POS tags

            //reading the tags file
            while ((line = in.readLine()) != null) {
                String[] allTranisitions = line.split(" ");   //splitting by space
                tagsList.add(allTranisitions);                      //adds line POS tags line by line

                if (!transitionsMap.containsKey(start)) {// add the key start '#' to my map
                    transitionsMap.put(start, new HashMap<>());
                    transitionsMap.get(start).put(allTranisitions[0], 1.0);//adding the first starting POS of the file
                } else {
                    if (!transitionsMap.get(start).containsKey(allTranisitions[0])) { //the second POS transition after the start with the hash sign
                        transitionsMap.get(start).put(allTranisitions[0], 1.0);       //setting the new transition to zero
                    } else {
                        transitionsMap.get(start).put(allTranisitions[0], transitionsMap.get(start).get(allTranisitions[0]) + 1);//Increment the value of frequency if this POS exists
                    }
                }
                //looping over the POS in the array
                for (int i = 0; i < allTranisitions.length-1; i++) {
                    if (!transitionsMap.containsKey(allTranisitions[i])) { //if current POS isn't in map as key, and next POS isn't a period, add to map
                        transitionsMap.put(allTranisitions[i], new HashMap<>());              //add POS state as a key
                        if (!Objects.equals(allTranisitions[i + 1], ".")) {
                            transitionsMap.get(allTranisitions[i]).put(allTranisitions[i + 1], 1.0);//insert the following P0S as key and setting frequency to 1
                        }
                    } else {
                        if (!transitionsMap.get(allTranisitions[i]).containsKey(allTranisitions[i + 1])) {//if the next POS doesn't exist, insert into the nested map and set one
                            transitionsMap.get(allTranisitions[i]).put(allTranisitions[i + 1], 1.0);
                        } else { //if it exists update frequency
                            transitionsMap.get(allTranisitions[i]).put(allTranisitions[i + 1], transitionsMap.get(allTranisitions[i]).get(allTranisitions[i + 1]) + 1);
                        }
                    }
                }
            }
            in.close();//close the tags file
            String read;//stores lines read from the sentence file below
            int index = 0;

            while ((read = inSentences.readLine()) != null) {
                String[] allObservations = read.toLowerCase().split(" ");
                String[] tags = tagsList.get(index);    //getting the tag lines stored in the array

                for (int i = 0; i < tags.length; i++) {
                    if (!observationsMap.containsKey(tags[i])) {//if POS doesn't exist, add as key, and its value is a map
                        observationsMap.put(tags[i], new HashMap<>());
                        observationsMap.get(tags[i]).put(allObservations[i], 1.0); //add the next word in the 2nd map and set frequency to 1.0
                    } else {
                        if (!observationsMap.get(tags[i]).containsKey(allObservations[i])) {
                            observationsMap.get(tags[i]).put(allObservations[i], 1.0);//add next word in the 2nd map and set frequency to 1.0
                        } else {//increment the frequency of word in the second word is it exists in the map
                            observationsMap.get(tags[i]).put(allObservations[i], observationsMap.get(tags[i]).get(allObservations[i]) + 1);
                        }
                    }
                }
                index++;//increment the index to move to the next tags line in the ArrayList
            }
            inSentences.close();//close the sentence file
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * computes the logs of the probabilities of the normalized data for either the transition or observations maps
     * @param scores - either the observation map or the transition map based on the what is parsed from the main method
     *
     */
    public static void probabilitiesInLogs(Map<String, Map<String, Double>> scores){
        //loop over the outer keys
        for(String state : scores.keySet()){
            double normalizeBy = 0.0;                   //total frequency of the inner frequency
            for(String POS : scores.get(state).keySet()){
                normalizeBy += scores.get(state).get(POS);//adding up all frequencies in the inner map
            }
            for(String POS : scores.get(state).keySet()){   //logs of the normalized data that log(frequency/totalFrequency)
                double log = Math.log((scores.get(state).get(POS))/normalizeBy); //computing the logs
                scores.get(state).put(POS, log); //over the writing the value of the second map
            }
        }
    }


    /**
     * Used the pseudocode from the course, COSC 10, fall 2022, website
     * Viterbi algorithm - produces list of POS based on the brackTracing of the POS as we traverse the list words in the sentence
     * @param sentence - the sentence to be tagged
     * @return arrayList with tags for a given sentence that has been parsed into this method.
     *
     */
    public static ArrayList<String> viterbiAlgorithm(String sentence) {
        Set<String> currStates = new HashSet<>(); //current states map
        Map<String, Double> currScores = new HashMap<>(); //scores of the current state

        ArrayList<Map<String, String>> backTrack = new ArrayList<>();
        ArrayList<String> possiblePOSPath = new ArrayList<>(); // will pass in the states with the best score so we can we retrieve the best final state at the end

        currStates.add(start);                       //the starting pointing of the transitions '#'
        currScores.put(start, 0.0);
        double totalScore;                           //the score to determine the most appropriate transition
        double unseen = -100;                              //if the transition is unknown because it was encountered in training

        String [] words = sentence.toLowerCase().split(" ");
        //looping over all the observations - words in the sentence
        for(int i = 0; i < words.length;  i++) {
            Map<String, String> nextStates = new HashMap<>();   //map to store the most probable next state based on sum of probabilities
            Map<String, Double> nextScores = new HashMap<>();   //stores the scores of the next states
            Map<String, String> backTrackMap = new HashMap<>(); //backtrack map

            Double highestNextScore = null; // tracks the highest score for a particular state
            String bestPOSTransition = null; // tracks the best POS

            //loop over the current states
            for (String currState : currStates) {
                //loop over the nextStates based on the transitionsMap- global variable
                for (String nextState : transitionsMap.get(currState).keySet()) {
                    if (!nextState.equals(".")) {
                        nextStates.put(nextState, currState);
                    }
                    if (!observationsMap.get(nextState).containsKey(words[i])) {//if the word is unseen in training phase
                        totalScore = currScores.get(currState) + transitionsMap.get(currState).get(nextState) + unseen;
                    }
                    else { totalScore = currScores.get(currState) + transitionsMap.get(currState).get(nextState) + observationsMap.get(nextState).get(words[i]);}

                    if (!nextScores.containsKey(nextState) || totalScore > nextScores.get(nextState)) {
                        nextScores.put(nextState, totalScore);//add to next scores in the total is lower than the ones in the map
                        backTrackMap.put(nextState, currState);//the nextState -> currState to the backtrace map

                        //update the highest score
                        if(highestNextScore == null || totalScore > highestNextScore){
                            highestNextScore = totalScore;
                            bestPOSTransition = nextState;
                        }
                    }
                }

            }
            currStates = nextStates.keySet(); //the sentence the currentStates to the nextStates
            currScores = nextScores;
            backTrack.add(backTrackMap); // add backtrack map for this section to the Array of maps
            possiblePOSPath.add(bestPOSTransition); // add the best possible POS to the array list
        }

        ArrayList<String> POSPath = new ArrayList<String>(); //stores the most appropriate tags as a list
        String POStags = possiblePOSPath.get(possiblePOSPath.size() - 1); //get the state with the highest score which is found in the previous map at the last index
        POSPath.add(POStags); //add the bestPOS to the front of the list that will track the path

        for (int i = 0; i < possiblePOSPath.size() - 1; i++) { // will use this for loop to find where the best final POS came from
            POStags = backTrack.get(backTrack.size() - 1 - i).get(POStags); // in the backTrack map always go one index of maps back to get the value (prior state) of where the best POS came from
            POSPath.add(0, POStags); // add to the front of the list the best POS that is found through backtracking through the map
        }

        return POSPath; // return the best possible POS map
    }

    /**
     * reading the test file [simple test and Brown test] and print the most appropriate tags for the given sentence in the file
     * @param tagsFilePath - the file path that has the tags
     * @param sentenceFilePath - the file path that has the contents that have been tagged
     * @throws IOException
     */
    public static void readFiles(String tagsFilePath, String sentenceFilePath) throws IOException {
        try {
            BufferedReader inTags = new BufferedReader(new FileReader(tagsFilePath));
            BufferedReader inSentence = new BufferedReader(new FileReader(sentenceFilePath));
            String lineTags;

            ArrayList<String[]> allTags = new ArrayList<>(); //stores all the tags from the tags file
            while ((lineTags = inTags.readLine()) != null) {
                String[] tagLine = lineTags.split(" ");
                allTags.add(tagLine);//adding the tag lines to the array list
            }
            inTags.close();             //closing the tags file
            int match = 0;              //count of tags that match
            int notMatch = 0;           //number of incorrect tags

            int index = 0;              //used to extract the tags array elements
            String read;
            //reading the sentences file
            while ((read = inSentence.readLine()) != null) {
                ArrayList<String> possiblePOStags = viterbiAlgorithm(read);
                String[] tagsFromFile = allTags.get(index);//getting the tags for each line

                //comparing the viterbi list and the tags in the file
                for (int i = 0; i < tagsFromFile.length; i++) {
                    if (possiblePOStags.get(i).equals(tagsFromFile[i])) {
                        match++; //increment count if the viterbi tag and
                    } else {
                        notMatch++;
                    }
                }
                index++; //increment the index to get the next element in the tags array list
            }
            inSentence.close();//close file
            System.out.println(match + " words are correctly tagged,but " + notMatch + " are incorrectly match using the viterbi algorithm");
            double total = match + notMatch;
            double percentage = (match/total) * 100;
            System.out.println("Accuracy: " + percentage);
            System.out.println();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * this is the harded portion to make sure my file works well
     * It is the training data from the PD7 assignments
     */
    public static void hardCoded() {
        transitionsMap = new HashMap<>(); //new transition map to add training data
        observationsMap = new HashMap<>();//new observation map to add the observation data - that's word frequencies

        //populating the transitions map
        transitionsMap.put("#", new HashMap<>());
        transitionsMap.get("#").put("NP", Math.log(2.0/7.0));
        transitionsMap.get("#").put("N", Math.log(5.0/7.0));

        transitionsMap.put("NP", new HashMap<>());
        transitionsMap.get("NP").put("V", Math.log(1.0));
        transitionsMap.put("N", new HashMap<>());
        transitionsMap.get("N").put("CNJ", Math.log(2.0/8.0));
        transitionsMap.get("N").put("V", Math.log(6.0/8.0));

        transitionsMap.put("CNJ", new HashMap<>());
        transitionsMap.get("CNJ").put("NP", Math.log(1.0/3.0));
        transitionsMap.get("CNJ").put("N", Math.log(1.0/3.0));
        transitionsMap.get("CNJ").put("V", Math.log(1.0/3.0));

        transitionsMap.put("V", new HashMap<>());
        transitionsMap.get("V").put("NP", Math.log(2.0/9.0));
        transitionsMap.get("V").put("N", Math.log(6.0/9.0));
        transitionsMap.get("V").put("CNJ", Math.log(1.0/9.0));

        //populating the observations map
        observationsMap.put("NP", new HashMap<>());
        observationsMap.get("NP").put("chase", Math.log(1.0));
        observationsMap.put("N", new HashMap<>());
        observationsMap.get("N").put("cat", Math.log(5.0/12));
        observationsMap.get("N").put("dog", Math.log(5.0/12));
        observationsMap.get("N").put("watch", Math.log(2.0/12));
        observationsMap.put("CNJ", new HashMap<>());
        observationsMap.get("CNJ").put("and", Math.log(1.0));
        observationsMap.put("V", new HashMap<>());
        observationsMap.get("V").put("watch", Math.log(6.0/9.0));
        observationsMap.get("V").put("get", Math.log(1.0/9.0));
        observationsMap.get("V").put("chase", Math.log(2.0/9.0));

        //testing the training data with the expected tags from PD7
        System.out.println("Tags for 'dog chase cat': " + viterbiAlgorithm("dog chase cat."));
        System.out.println("Tags for 'cat watch chase': " + viterbiAlgorithm("cat watch chase"));
        System.out.println("Tags for 'chase get watch': " + viterbiAlgorithm("chase get watch"));
        System.out.println("Tags for 'chase watch dog and cat': " + viterbiAlgorithm("chase watch dog and cat"));
        System.out.println("Tags for 'dog watch cat watch dog': " + viterbiAlgorithm("dog watch cat watch dog"));
        System.out.println("Tags for 'cat watch watch and chase': " + viterbiAlgorithm("cat watch watch and chase"));
        System.out.println("Tags for 'dog watch and chase chase': " + viterbiAlgorithm("dog watch and chase chase"));



    }

    public static void main(String[] args) throws IOException{

        //Simple file training data and outputting the correctness of the data
        NFAtrain("PS5/simple-train-tags.txt","PS5/simple-train-sentences.txt" );
        probabilitiesInLogs(transitionsMap);//finding the probabilities
        probabilitiesInLogs(observationsMap);
        System.out.println("Transition map: " +transitionsMap);
        System.out.println("Observation map: " +observationsMap);
        System.out.println("TEST FILE: Simple");//testing the file after training
        readFiles("PS5/simple-test-tags.txt", "PS5/simple-test-sentences.txt");

        //testing and training data using the Brown file
        NFAtrain("PS5/brown-train-tags.txt", "PS5/brown-train-sentences.txt");
        probabilitiesInLogs(transitionsMap);//finding the logs of the probabilities
        probabilitiesInLogs(observationsMap);
        System.out.println("TEST FILE: Brown");
        readFiles("PS5/brown-test-tags.txt", "PS5/brown-test-sentences.txt");

        //testing the hard coded samples
        System.out.println("Hard coded samples using the PD7 training data: ");
        hardCoded();

        //console testing - trained with the simple-training data files
        System.out.println();
        System.out.println("Console Test");
        NFAtrain("PS5/simple-train-tags.txt","PS5/simple-train-sentences.txt" );
        probabilitiesInLogs(transitionsMap);//finding the probabilities of the observational and transitional data
        probabilitiesInLogs(observationsMap);
        System.out.println("Write a sentence or enter 'q' to quit: ");
        Scanner in = new Scanner(System.in);//scanning the user input
        boolean mode = true;
        while (mode) {
            String sentence = in.nextLine();
            ArrayList<String> arr = viterbiAlgorithm(sentence);//parsing the sentence to the viterbi algorithm to get the tags
            System.out.println(arr);
            if (sentence.equals("q")) mode = false; //if the user wants to quit testing.
        }

        System.out.println();
        System.out.println("Console testing with Brown tags training");
        NFAtrain("PS5/brown-train-tags.txt", "PS5/brown-train-sentences.txt");
        probabilitiesInLogs(transitionsMap);//finding the logs of the probabilities
        probabilitiesInLogs(observationsMap);
        Scanner brown = new Scanner(System.in);
        mode = true;
        while(mode){
            String sent = brown.nextLine();
            ArrayList<String> arr = viterbiAlgorithm(sent);//parsing the sentence to the viterbi algorithm to get the tags
            System.out.println(arr);
            if (sent.equals("q")) mode = false; //if the user wants to quit testing.
        }

    }


}
