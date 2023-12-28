import java.util.*;
import java.awt.*;

/**
 * handles all sketches in map and other operations related to the sketch
 * @author Abdibaset Bare and Bilan Aden
 */
public class Sketch {
    private int shapeCount = 0;                                          //id of the shape
    private  TreeMap<Integer, Shape> keepTrackSketch;        //stores the shapes drawing

    /**
     * constructor to initiatilize the Treemap
     */
    public Sketch(){
        keepTrackSketch = new TreeMap<>() ;
    }

    /**
     * adding a shape to the map using the current id and incrementing the id
     * @param curr - the shape to added to the map with the current id
     */
    public synchronized void add(int id, Shape curr, int count){
        keepTrackSketch.put(id, curr);
        shapeCount = count;
    }

    /**
     * removing a shape from map
     * @param id - the id of the shape to be removed from the map
     */
    public synchronized void removeShape(Integer id){
        keepTrackSketch.remove(id);
    }

    /**
     * recoloring the shape to desired color
     * @param id - id of the shape to be recolored
     * @param color - the RGB value of the desired color
     */
    public synchronized void recolorShape(Integer id, int color){
        Color newColor = new Color(color);
        keepTrackSketch.get(id).setColor(newColor);
    }

    /**
     * Draws all the shapes in the sketch
     * @param g graphics
     */
    public synchronized void draw(Graphics g) {
        // iterate through the map from the lowest to the highest key
        for (Integer id : keepTrackSketch.navigableKeySet()) keepTrackSketch.get(id).draw(g);
    }

    /**
     * check if the map is empty or if there are shapes to be drawn
     * @return
     */
    public synchronized boolean isEmpty(){
        return keepTrackSketch.isEmpty();
    }

    /**
     * getting the shape using id
     * @param id - id of the shape to get
     * @return the shape
     */
    public synchronized Shape getShape(int id){
        return keepTrackSketch.get(id);
    }

    /**
     * getting the map that stores information about the shapes
     * @return - map
     */
    public synchronized Map<Integer, Shape> getKeepTrackSketch(){
        return keepTrackSketch;
    }

    /**
     * the id of a shape
     * @return int(id)
     */
    public synchronized Integer getShapeCount(){
        return shapeCount;
    }

    /**
     *
     * @param x - the x coordinate of the point
     * @param y - y coordinate
     * @return - the id of shape that contains the x and y coordinate
     */
    public synchronized Integer topMostShapId(int x, int y){
        //iterate from highest to lowest ids
        for(Integer id: keepTrackSketch.descendingKeySet()){
            if(keepTrackSketch.get(id).contains(x, y)){
                return id;
            }
        }
        return -1;                  //if there is no shape at the selected point
    }
}
