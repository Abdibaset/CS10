import java.awt.Color;
import java.awt.Graphics;

/**
 * A rectangle-shaped Shape
 * Defined by an upper-left corner (x1,y1) and a lower-right corner (x2,y2)
 * with x1<=x2 and y1<=y2
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, updated Fall 2016
 * @authors Abdibaset Bare and Bilan Aden
 */
public class Rectangle implements Shape {

	// TODO: YOUR CODE HERE
	private int x1, x2, y1, y2;			//the rectangle coordinates
	private Color color;

	/**
	 * rectangle defined by two points and the desired color
	 * @param x1 - the x coordinate
	 * @param y1 - the y coordinate
	 * @param color - the desired color
	 */
	public Rectangle(int x1, int y1, Color color){
		this.x1 = x1;
		this.x2 = x1;
		this.y1 = y1;
		this.y2 = y1;
		this.color = color;

	}

	/**
	 * A rectangle defined by two corners
	 */
	public Rectangle(int x1, int y1, int x2, int y2, Color color) {
		setCorners(x1, y1, x2, y2);
		this.color = color;
	}

	/**
	 * setting corners to the corners of the rectangle
	 * @param x1 - the x coordinate of top most corner of the rectangle
	 * @param y1 - the y coordinate of top most corner of the rectangle
	 * @param x2 - the x coordinate of lower most corner of the rectangle
	 * @param y2 - the y coordinate of lower most corner of the rectangle
	 */
	public void setCorners(int x1, int y1, int x2, int y2){
		// Ensure correct upper left and lower right
		this.x1 = Math.min(x1, x2);
		this.y1 = Math.min(y1, y2);
		this.x2 = Math.max(x1, x2);
		this.y2 = Math.max(y1, y2);
	}

	/**
	 * moving the shape
	 * @param dx - the change in x direction
	 * @param dy - the change in y direction
	 */
	@Override
	public void moveBy(int dx, int dy) {
		this.x1 += dx;
		this.x2 += dx;
		this.y1 += dy;
		this.y2 += dy;
	}

	/**
	 * getting the color of the rectangle
	 * @return - the color
	 */
	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * checks if the point is within bounds
	 * @param x
	 * @param y
	 * @return bool
	 */
	@Override
	public boolean contains(int x, int y) {
		return (x1 <= x && x <= x2) && (y1 <= y && y <= y2);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.drawLine(x1,y1,x2,y1);
		g.drawLine(x1,y1,x1,y2);
		g.drawLine(x1,y2,x2,y2);
		g.drawLine(x2,y1,x2,y2);
		g.fillRect(x1, y1, x2-x1, y2-y1);
	}

	public String toString() {
		return "Rectangle with coordinates "+x1+" "+y1+" "+x2+" "+y2+" "+ " and value of " + color.getRGB();
	}
}
