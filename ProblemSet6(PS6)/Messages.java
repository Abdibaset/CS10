import java.awt.*;

/**
 * handles messages for the EditorCommunicator and SketchServerCommunicators
 * @author Abdibaset Bare and Bilan Aden
 */
public class Messages {
    Sketch sketch;
    Shape curr = null;

    /**
     * single client communication
     * @param sketch - sketching to be drawn
     */
    public Messages(Sketch sketch){
        this.sketch = sketch;
    }

    /**
     * decoding the message from the editorCommunicator and Sketch Communiators
     * @param line - the input read from the console
     */
    public void decodeMessage(String line){
        String [] message = line.split(" "); //splitting into an array of strings
        switch (message[0]){ //calling functions depending on the request
            case "add" -> addShape(message);
            case "recolor" -> recolorShape(message);
            case "delete" -> deleteShape(message);
            case "move" -> moveShape(message);
            default -> {
                System.out.println(line + " is invalid command");//if the command is an invalid one
            }

        }
    }

    /**
     * adding a shape to the sketch canvas
     * @param msg - the message
     */
    public void addShape(String [] msg){
        int shape_Id = Integer.parseInt(msg[1]);    //shape id
        int shapeCount = Integer.parseInt(msg[2]);  //the count
        if(msg[3].equals("ellipse") || msg[3].equals("rectangle") || msg[3].equals("segment")){
            //the x and y coordinate and the RGB value
            int x1 = Integer.parseInt(msg[4]);
            int y1 = Integer.parseInt(msg[5]);
            int x2 = Integer.parseInt(msg[6]);
            int y2 = Integer.parseInt(msg[7]);
            int colorRGB = Integer.parseInt(msg[8]);

            //if request is an ellipse
            if(msg[3].equals("ellipse")){
                curr = new Ellipse(x1, y1, x2, y2, new Color(colorRGB));
                sketch.add(shape_Id, curr, shapeCount); // add to the map
            }
            //if request is a rectangle
            else if(msg[3].equals("rectangle")){
                curr = new Rectangle(x1, y1, x2, y2, new Color(colorRGB));
                sketch.add(shape_Id, curr, shapeCount);
            }

            //if request is a line segment
            else if(msg[3].equals("segment")){
                curr = new Segment(x1, y1, x2, y2, new Color(colorRGB));
                sketch.add(shape_Id, curr, shapeCount);
            }
        }
        else if(msg[3].equals("polyline")){
            Polyline newP = new Polyline(Integer.parseInt(msg[5]), Integer.parseInt(msg[6]),
                    Integer.parseInt(msg[7]), Integer.parseInt(msg[8]), new Color(Integer.parseInt(msg[9])));
            //looping over all the instance of polyline is the message - 6 parameters per run
            for (int i = 10; i < msg.length; i+=6) {
                if (msg[i].equals("segment")) {
                    newP.addSegment(new Segment(Integer.parseInt(msg[i+1]), Integer.parseInt(msg[i+2]), Integer.parseInt(msg[i+3]), Integer.parseInt(msg[i+4]), new Color(Integer.parseInt(msg[i+5]))));
                }
            }
            curr = newP;
            sketch.add(shape_Id, curr, shapeCount); //adding the sketch to the map
        }
    }

    /**
     * recoloring the shape
     * @param msg - message content in an array
     */

    public void recolorShape(String [] msg){
        int shape_Id = Integer.parseInt(msg[1]);//the unique id of shape
        int colorRGBval = Integer.parseInt(msg[2]);//color

        curr = sketch.getShape(shape_Id); //setting the current shape to shape with id shape_id
        if(curr != null){
            curr.setColor(new Color(colorRGBval));  //recolor shape
        }
    }

    /**
     * deletes shape
     * @param msg - message content in an array
     */
    public void deleteShape(String [] msg){
        sketch.removeShape(Integer.parseInt(msg[1]));
    }

    /**
     * moving the shape as specified in message
     * @param msg - message content in an array
     */
    public void moveShape(String [] msg) {
        int shape_Id = Integer.parseInt(msg[1]); //the shape id
        int dx = Integer.parseInt(msg[2]); //the change in x direction
        int dy = Integer.parseInt(msg[3]); //the change in y direction

        // get the current shape
        curr = sketch.getShape(shape_Id);

        // move the shape
        if (curr != null) {
            curr.moveBy(dx, dy);
        }
    }
}
