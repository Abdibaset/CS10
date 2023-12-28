import java.util.ArrayList;
import java.util.List;

/**
 * A point quadtree: stores an element at a 2D position, 
 * with children at the subdivided quadrants
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016, explicit rectangle
 * @author CBK, Fall 2016, generic with Point2D interface
 *
 */
public class PointQuadtree<E extends Point2D> {
	private E point;							// the point anchoring this node/root
	private int x1, y1;							// upper-left corner of the region
	private int x2, y2;							// bottom-right corner of the region
	private PointQuadtree<E> c1, c2, c3, c4;	// children


	/**
	 * Initializes a leaf quadtree, holding the point in the rectangle
	 */
	public PointQuadtree(E point, int x1, int y1, int x2, int y2) {
		this.point = point;
		this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}

	// Getters
	
	public E getPoint() {
		return point;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	/**
	 * Returns the child (if any) at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public PointQuadtree<E> getChild(int quadrant) {
		if (quadrant==1) return c1;
		if (quadrant==2) return c2;
		if (quadrant==3) return c3;
		if (quadrant==4) return c4;
		return null;
	}

	/**
	 * Returns whether or not there is a child at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public boolean hasChild(int quadrant) {
		return (quadrant==1 && c1!=null) || (quadrant==2 && c2!=null) || (quadrant==3 && c3!=null) || (quadrant==4 && c4!=null);
	}

	/**
	 * Inserts the point into the tree
	 * quad 1 & 3 are closed and 2 & 4 are open
	 * CONJUGATE(DIRECTION) -> (N) = ((N+1) mod 4) + 1
	 * 	  ------------------------------
	 *    |           |               |
	 *    |		2	  |       1       |
	 *    |           |               |
	 *    ______________________________
	 *    |           |               |
	 *    |      3    |        4      |
	 *    |           |               |
	 *    _____________________________
	 */
	public void insert(E p2) {
		// TODO: YOUR CODE HERE
		//quad 1 - p2's x greater than root's and p2's less than root's y
		if (p2.getX() >= point.getX() && p2.getY() < point.getY()) {
			if (hasChild(1)){ c1.insert(p2);} //add c1's child if c1 exists
			else{	c1 = new PointQuadtree<E>(p2, (int) point.getX(), getY1(), getX2(), (int)point.getY());}//make p2 c1 in quad 1
		}

		//quad 2 - p2's x less than root's and p2's y less than root's
		else if (p2.getX() < point.getX() && p2.getY() <= point.getY()) {
			if (hasChild(2)){ c2.insert(p2);} //add as c2's child if c2 doesn't exist
			else{ c2 = new PointQuadtree<E>(p2, getX1(), getY1(), (int)point.getX(), (int)point.getY());} //make p2 c2 in quad 2
		}

		//quad 3 - p2's x less than root's and y greater than root's
		else if (p2.getX() <= point.getX() && p2.getY() > point.getY()) {
			if (hasChild(3)){ c3.insert(p2);} //add as c3's child
			else{ c3 = new PointQuadtree<E>(p2, getX1(), (int) point.getY(), (int)point.getX(), getY2());}//make p2 c3 in quad 3
		}

		//quad 4 - p2's x and y greater than root's
		else if (p2.getX() > point.getX() && p2.getY() >= point.getY()) {
			if (hasChild(4)){ c4.insert(p2);} //add as c4's child
			else{ c4 = new PointQuadtree<E>(p2, (int) point.getX(), (int) point.getY(), getX2(), getY2());}//make p2 c4 in quad 4
		}
	}
	/**
	 * Finds the number of points in the quadtree (including its descendants)
	 * recursed through the root and each child that contains something and returned the number of elements
	 * method return the number of elements in Tree
	 *
	 * @variable num - the number of elements in a given tree.
	 */
	public int size() {
		// TODO: YOUR CODE HERE
		if(getPoint() == null) return 0;
		int num = 1;									// 1- is for the root
		if(hasChild(1))  num += getChild(1).size();
		if(hasChild(2))  num += getChild(2).size();
		if(hasChild(3))  num += getChild(3).size();
		if(hasChild(4))  num += getChild(4).size();

		return num;				//all children + root
	}
	
	/**
	 * Builds a list of all the points in the quadtree (including its descendants)
	 * Created a list and filled in with the elements in the tree.
	 *
	 * @method recursivePoints - helper that adds points to an empty list
	 * @variable addedPoints - arrayList to each all points in the list are recursively added
	 * @return type - list containing all the elements in the Tree.
	 *
	 */
	public List<E> allPoints() {
		// TODO: YOUR CODE HERE
		List<E> addedPoints = new ArrayList<E>();			//new List to add all points in Tree
		recursePoints(addedPoints);							//call for helper method
		return addedPoints;									//returns list
	}	

	/**
	 * Uses the quadtree to find all points within the circle around mouse click
	 *
	 * @variable pointInCirle - list to which all the circles that intersect rectangle and in circle are added.
	 * @param cx	circle center x
	 * @param cy  	circle center y
	 * @param cr  	circle radius
	 * @return    	the points in the circle (and the qt's rectangle)
	 */
	public List<E> findInCircle(double cx, double cy, double cr) {
		// TODO: YOUR CODE HERE
		List<E> pointInCircle = new ArrayList<>();									//new list to add points in circle
		if (Geometry.circleIntersectsRectangle(cx, cy, cr, x1, y1, x2, y2)) {	//checks if the point intersects the circle, prevent out of bounds
			if (Geometry.pointInCircle(point.getX(), point.getY(), cx, cy, cr)) {//checks if point is in circle around the root node
				pointInCircle.add(getPoint());
			}
			//for each quadrant with a child, recurse with that child
			if(hasChild(1)) pointInCircle.addAll(getChild(1).findInCircle(cx, cy, cr)); //quad 1
			if(hasChild(2)) pointInCircle.addAll(getChild(2).findInCircle(cx, cy, cr)); //quad 2
			if(hasChild(3)) pointInCircle.addAll(getChild(3).findInCircle(cx, cy, cr)); //quad 3
			if(hasChild(4)) pointInCircle.addAll(getChild(4).findInCircle(cx, cy, cr)); //quad 4
		}
		return pointInCircle;									//returns all the points in circle considered a hit around mouse click
	}

	// TODO: YOUR CODE HERE for any helper methods

	/**
	 * Function recursively fills the list with points from different nodes in the quadtree
	 * @param lst - the list populated with points from all nodes
	 */
	public void recursePoints(List<E> lst){
		lst.add(point);
		if(hasChild(1)) lst.addAll(getChild(1).allPoints());		//points from quad 1
		if(hasChild(2)) lst.addAll(getChild(2).allPoints());		//points from quad 1 & 2
		if(hasChild(3)) lst.addAll(getChild(3).allPoints());		//points from 1, 2 & 3
		if(hasChild(4)) lst.addAll(getChild(4).allPoints());		//points from all nodes.
	}

}
