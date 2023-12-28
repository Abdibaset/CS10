import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A multi-segment Shape, with straight lines connecting "joint" points -- (x1,y1) to (x2,y2) to (x3,y3) ...
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016
 * @author CBK, updated Fall 2016
 * @authors Abdibaset Bare and Bilan Aden
 */
public class Polyline implements Shape {

    // TODO: YOUR CODE HERE
	private Color color;
	private List<Segment> segments = new ArrayList<>(); //list to store all segments


	/**
	 * one line segment
	 */
	public Polyline(int x, int y, Color color) {
		Segment newS = new Segment(x, y, color);
		segments.add(newS);
		this.color = color;
	}

	/**
	 * Complete line with two segments
	 */
	public Polyline(int x1, int y1, int x2, int y2, Color color) {
		Segment newS = new Segment(x1, y1, x2, y2, color);
		segments.add(newS);
		this.color = color;
	}

	/**
	 * Adds segment to the freehand polyline segments list
	 * @param segment segment object
	 */
	public void addSegment(Segment segment){
		segments.add(segment);
	}

	@Override
	public void moveBy(int dx, int dy) {
		for(Segment s: segments){
			s.moveBy(dx, dy);
		}
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		for(Segment s: segments){
			s.setColor(color);
		}
	}
	
	@Override
	public boolean contains(int x, int y) {
		for(Segment s: segments){
			if(s.contains(x, y)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void draw(Graphics g) {
		for(Segment s: segments){
			s.draw(g);
		}
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("polyline ");
		for (Segment seg : segments){
			result.append(seg.toString()).append(" ");
		}
		return result.toString() + color.getRGB();
	}
}
