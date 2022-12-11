import java.awt.*;

import javax.swing.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Using a quadtree for collision detection
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016, updated for blobs
 * @author CBK, Fall 2016, using generic PointQuadtree
 *
 */
public class CollisionGUI extends DrawingGUI {
	private static final int width=800, height=600;		// size of the universe

	private List<Blob> blobs;							// all the blobs
	private List<Blob> colliders;						// the blobs who collided at this step
	private char blobType = 'b';						// what type of blob to create
	private char collisionHandler = 'c';				// when there's a collision, 'c'olor them, or 'd'estroy them
	private int delay = 100;							// timer control

	public CollisionGUI() {
		super("super-collider", width, height);

		blobs = new ArrayList<Blob>();

		// Timer drives the animation.
		startTimer();
	}

	/**
	 * Adds a blob of the current blobType at the location
	 */
	private void add(int x, int y) {
		if (blobType=='b') {
			blobs.add(new Bouncer(x,y,width,height));
		}
		else if (blobType=='w') {
			blobs.add(new Wanderer(x,y));
		}
		else {
			System.err.println("Unknown blob type "+blobType);
		}
	}

	/**
	 * DrawingGUI method, here creating a new blob
	 */
	public void handleMousePress(int x, int y) {
		add(x,y);
		repaint();
	}

	/**
	 * DrawingGUI method
	 * added a test case for key press == m
	 */
	public void handleKeyPress(char k) {
		if (k == 'f') { // faster
			if (delay>1) delay /= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else if (k == 's') { // slower
			delay *= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else if (k == 'r') { // add some new blobs at random positions
			for (int i=0; i<10; i++) {
				add((int)(width*Math.random()), (int)(height*Math.random()));
				repaint();
			}
		}
		else if (k == 'c' || k == 'd') { // control how collisions are handled
			collisionHandler = k;
			System.out.println("collision:" + k);
		}

		else if(k == 'm'){				//test cases for show collisions and their color
			blobs.add(new Blob(150, 500, 6));//first three are close to each other
			blobs.add(new Blob(154, 502, 6));
			blobs.add(new Blob(156, 504, 6));
			blobs.add(new Blob(500, 100, 6));
			blobs.add(new Blob(600, 50, 6));
		}
		else { // set the type for new blobs
			blobType = k;
		}
	}

	/**
	 * DrawingGUI method, here drawing all the blobs and then re-drawing the colliders in red
	 */
	public void draw(Graphics g) {
		// TODO: YOUR CODE HERE
		// Ask all the blobs to draw themselves.
		if(blobs == null) return;
		else {
			for(Blob blob : blobs) {
				g.setColor(Color.BLUE);
				g.fillOval((int) blob.getX(), (int) blob.getY(), 6, 6);
			}
		}
		// Ask the colliders to draw themselves in red.
		if(colliders != null){
			for(Blob collider : colliders){					//drawing all colliders the list isn't empty
				g.setColor(Color.RED);
				g.fillOval((int)collider.getX(), (int)collider.getY(), 6, 6);
			}
		}
	}

	/**
	 * Sets colliders to include all blobs in contact with another blob
	 * method fills in all the colliding elements into colliders list
	 *
	 * @variable colliderTree - variable to create a new tree for all collider points
	 *
	 */
	private void findColliders() {
		// TODO: YOUR CODE HERE
		// Create the tree
		PointQuadtree<Blob> colliderTree = null;
		colliders = new ArrayList<>();
		//looping through all the blobs in the blobs and inserting all colliding blobs to colliders
		for(Blob blob : blobs){
			if(colliderTree == null){ //new collider created if colliders in empty
				colliderTree = new PointQuadtree<>(blobs.get(0), 0, 0, width, height); //initializing the quadtree
			}
			else{ colliderTree.insert(blob);}											//a blob is inserted to the collider quadtree
			List<Blob> collides = colliderTree.findInCircle(blob.x, blob.y, blob.r);	//list for all colliding blobs that come into contact
			if(collides.size() > 1){ colliders.addAll(collides);}						//if more one that two blobs collide, they are added to the list
		}
	}

	/**
	 * DrawingGUI method, here moving all the blobs and checking for collisions
	 */
	public void handleTimer() {
		// Ask all the blobs to move themselves.
		for (Blob blob : blobs) {
			blob.step();
		}
		// Check for collisions
		if (blobs.size() > 0) {
			findColliders();
			if (collisionHandler=='d') {
				blobs.removeAll(colliders);
				colliders = null;
			}
		}
		// Now update the drawing
		repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CollisionGUI();
			}
		});
	}
}
