import java.io.*;
import java.util.*;

/**
 * class for compressing and decompressing the files
 * In compressed file - stores the information in Binary
 * Decompressed file - decode file.
 *
 * @recieved help from the following TAs: Avery Fogg, Anna Mikhilova and Kunal Jha
 * @authors - collaboration between Abdibaset Bare and Bilan Aden
 */

public class readFileAndCompress{
    public static Map<Character, Integer> charData;                 //frequency map to store character data
    static TreeComparator comparedVal;                              //stores returned value of comparator
    public static PriorityQueue<BinaryTree<CharInfo>> myQueue;      //priorityQueue to implement Heap method
    public static Map<Character, String> bitsMap;                   //Map for storing bit representation of characters


    /**
     * contructor
     * initializing
     * 1. the map for frequency data of characters
     * 2. the priority queue
     * 3. Map for storing bits representation and characters
     */
    public readFileAndCompress(){
        charData = new HashMap<>();
        comparedVal = new TreeComparator();
        myQueue = new PriorityQueue<>(comparedVal);
        bitsMap = new HashMap<>();
    }

    /**
     * method for reading file and inserting into the map
     * charData - map; keys are characters and values - frequency
     * @param pathname - the filename of file to be encoded
     * @throws Exception - if the filename doesn't exist
     */
    public static void loadFile(String pathname) throws Exception{
        try {
            BufferedReader in = new BufferedReader(new FileReader(pathname));
            char c;                                                 //stores character
            int inreadVal = in.read();                              //ASCII val of character
            //reading the file
            while (inreadVal != -1) {                                //while not empty
                c = (char) inreadVal;                               //ASCII to character
                //filling the charData map
                if (charData.containsKey(c)) {                      //populating the frequency map with char as key, and frequency as val
                    charData.put(c, charData.get(c) + 1);
                } else charData.put(c, 1);

                inreadVal = in.read();                              //advance to the next character
            }
            in.close();                                            //closing file
        }
        catch(Exception e){                                        //if file doesn't exist - can't load
            System.err.println("File couldn't load");
        }
    }

    /**
     * creating the binary tree and priority queue of characters and using the characters in map
     */

    public void insertLetters(){
        //looping through the map keys
        for(Character charInMap : charData.keySet()){
            CharInfo char_frequency = new CharInfo(charInMap, charData.get(charInMap));  //character info is created using char info class
            BinaryTree<CharInfo> charNode = new BinaryTree<>(char_frequency);            //new Node for each character
            myQueue.add(charNode);                                                       //adding node to the priority queue
        }
    }

    /**
     * restructuring the priority queue
     */
    public static void allPriorityQueue() throws Exception {
        //if there is one priority queues - one letter read
        if(myQueue.size() == 1){
            BinaryTree<CharInfo>  T1 = myQueue.remove();                    //obtain the node
            int totalFrequency = T1.getData().getFrequency();               //frequency of character
            CharInfo char_frequency = new CharInfo(null, totalFrequency); //object frequency to form root of the priority queue
            BinaryTree<CharInfo> treeNode = new BinaryTree<>(char_frequency, T1, null);
            myQueue.add(treeNode);                                          //add node to the priority queue
        }

        //if more than one priority queue exists
        while (myQueue.size() > 1) {
            //obtain first two nodes
            BinaryTree<CharInfo> T1 = myQueue.remove();
            BinaryTree<CharInfo> T2 = myQueue.remove();
            //get total frequency
            int totalFrequency = T1.getData().getFrequency() + T2.getData().getFrequency();
            CharInfo frequency = new CharInfo(null, totalFrequency);        //object frequency to form the root
            BinaryTree<CharInfo> treeNode = new BinaryTree<>(frequency, T1, T2);
            myQueue.add(treeNode);                                               //added to priority queue
        }
    }

    /**
     * bits representation based of the priority queue
     * @throws Exception - if priority queue is empty
     */
    public static void bitRepresentation() throws Exception{
        BinaryTree<CharInfo> root = myQueue.peek();     //the root
        String bits = " ";                              //stores the binary representation
        if(root == null) throw new Exception("Priority Queue wasn't created");
        //if the priority queues has something, recursively filling the bitsMap
        fillbitsMapRecursively(bitsMap, root, bits);
    }

    /**
     * helper method to fill map characters to binary representation
     * @param bitsMap - map for mapping characters to binary representation
     * @param root  - the root of the priority queue
     * @param bits - the bits representation i.e 0s and 1s
     */
    public static void  fillbitsMapRecursively(Map<Character, String> bitsMap, BinaryTree<CharInfo> root, String bits){
        if(root.isLeaf()){ bitsMap.put(root.getData().getName(), bits);}                      //add character and bit to map
        if(root.hasLeft()) { fillbitsMapRecursively(bitsMap, root.getLeft(), bits+"0");}  //concatenate zero for each left node traversed
        if(root.hasRight()){ fillbitsMapRecursively(bitsMap, root.getRight(), bits+"1");} //concatenate one for each right node traversed
    }

    /**
     *
     * @param filetoRead - text file to read from
     * @param filetoWrite - empty file to write the bit representation into
     * @throws IOException - exception if the file doesn't exist
     */
    public static void writeCompressed(String filetoRead, String filetoWrite)throws IOException{
        try {
            BufferedReader fileContent = new BufferedReader(new FileReader(filetoRead)); //original file
            BufferedBitWriter bitFile = new BufferedBitWriter(filetoWrite);              //bit file to write into

            int i = fileContent.read();                                                  //reading first character
            //reading the file character by character
            while (i != -1) {                                                            //file has something
                Character ch = (char) i;
                String binaryBits = bitsMap.get(ch);                                    //binary representation of character in map
                if (binaryBits != null) {
                    //looping through all bits in the string
                    for (int j = 0; j < binaryBits.length(); j++) {
                        if (binaryBits.charAt(j) == '1') {                              //if bit is '1'
                            bitFile.writeBit(true);
                        } else if (binaryBits.charAt(j) == '0') {
                            bitFile.writeBit(false);
                        }
                    }
                }
                i = fileContent.read();                                             //advancing to the next character in file.
            }
            fileContent.close();                                                    //closing the original file.
            bitFile.close();                                                        //closing the bit file.
        }
        catch(IOException e){
            System.err.println("File is not there");
        }
    }

    /**
     *decodes the encoded file in the compressed method
     * @param compressedFile - file with the binary representation of characters.
     * @param toDecompressFile - file to write into.
     * @throws IOException - exception if the file doesn't exist.
     */

    public static void decompressFile(String compressedFile, String toDecompressFile) throws IOException{
        try {
            BufferedWriter decompressedFile = new BufferedWriter(new FileWriter(toDecompressFile)); //new file for decompressed content
            BufferedBitReader decompress = new BufferedBitReader(compressedFile);               //compressed file with content
            BinaryTree<CharInfo> checkRoot = myQueue.peek();                                    //root of priority queue

            //reading the bit file to decode
            while (decompress.hasNext()) {
                boolean myBit = decompress.readBit();                                           //reading the bit
                if (myBit) {
                    assert checkRoot != null;
                    checkRoot = checkRoot.getRight();                                           //node to the right
                }
                else {
                    assert checkRoot != null;
                    checkRoot = checkRoot.getLeft();                                           //node to the left
                }
                if (checkRoot.isLeaf()) {
                    char letter = checkRoot.getData().getName();                               //getting the character name
                    decompressedFile.write(letter);                                            //writing into the file
                    checkRoot = myQueue.peek();                                                //checking the new root
                }
            }
            //closing both the compressed and decompressed files
            decompress.close();
            decompressedFile.close();
        }
        //if the files don't exist
        catch (IOException e){
            System.err.println("File not found");
        }
    }

}
