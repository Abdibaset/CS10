import java.util.*;
public class TreeComparator implements Comparator<BinaryTree<CharInfo>>{

    /**
     *
     * @param key1 the first object to be compared.
     * @param key2 the second object to be compared.
     * @return result of the comparison
     */
    public int compare(BinaryTree<CharInfo> key1, BinaryTree<CharInfo> key2){
        if(key1.data.getFrequency() > key2.data.getFrequency()) return 1;       
        if(key1.data.getFrequency() < key2.data.getFrequency()) return -1;
        else return 0;
    }
}
