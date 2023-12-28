import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 * 
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 */
public class RegionFinder {
	private static final int maxColorDiff = 20;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50; 				// how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	private ArrayList<ArrayList<Point>> regions;			// a region is a list of points
															// so the identified regions are in a list of lists of points

	public RegionFinder() { 
		this.image = null;
	}

	public RegionFinder(BufferedImage image) {
		this.image = image;		
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 */
	public void findRegions(Color targetColor) {
		// TODO: YOUR CODE HERE
		BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		regions = new ArrayList<ArrayList<Point>>();

		//looping over all pixels in image
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				Color oColor = new Color(image.getRGB(x, y));						//obtain pixel color
				if (visited.getRGB(x, y) == 0 && colorMatch(targetColor, oColor)) {	//check if the pixel is visited and of desired color match
					ArrayList<Point> region = new ArrayList<Point>();				//create a new region
					ArrayList<Point> toVisit = new ArrayList<Point>();				//create a toVisit to store points
					toVisit.add(new Point(x, y));									//adding the first point to the toVisit region

					while (!toVisit.isEmpty()) {
						Point coordinate = toVisit.remove(0);					//obtain the first point in toVisit array to check if visited and its neighbors=
						//if coordinate is unvisited, mark as visited and is added to the region
						if (visited.getRGB(coordinate.x, coordinate.y) == 0) {
							region.add(coordinate);
							visited.setRGB(coordinate.x, coordinate.y, 1);		//set this coordinate to visited

							//loop through all its neighbors
							for (int cy = Math.max(0, coordinate.y - 1); cy <= Math.min(image.getHeight()-1, coordinate.y+1); cy++) {
								for (int cx = Math.max(0, coordinate.x - 1); cx <=Math.min(image.getWidth()-1, coordinate.x+1); cx++) {
									Color NColor = new Color(image.getRGB(cx, cy));
									//check that the coordinates you have are not similar to current coordinate
									if (visited.getRGB(cx, cy) == 0 && colorMatch(targetColor, NColor)) {
										toVisit.add(new Point(cx, cy));
									}
								}
							}
							if (region.size() >= minRegion) {	//if regions is or above the max region threshold, it is added to region
								regions.add(region);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */
	private static boolean colorMatch(Color c1, Color c2) {
		// TODO: YOUR CODE HERE
		//absolute difference of RGB values
		int red = Math.abs(c2.getRed() - c1.getRed());
		int blue = Math.abs(c2.getBlue() - c1.getBlue());
		int green = Math.abs(c2.getGreen() - c1.getGreen());

		return red <= maxColorDiff && blue <= maxColorDiff && green <= maxColorDiff; //returns true if within the region.
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> largestRegion() {
		//getting the max region
		ArrayList<Point> max_region= new ArrayList<Point>();		//create new arrayList to store maximum regions
		if(regions !=null){
			for (ArrayList<Point> region : regions){				//for each-loop to run through all regions
				if(max_region.size() < region.size()) {				//if a region is more than the current max, max is current
					max_region = region;
				}
			}
		}
		return max_region;											//returning the largest region
	}

	/**
	 * Sets recoloredImage to be a copy of image, 
	 * but with each region a uniform random color, 
	 * so we can see where they are
	 */
	public void recolorImage() {
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		
		for(ArrayList<Point> region : regions){
			//randomly generating RGB values
			int red = (int)(Math.random() *255);
			int green = (int)(Math.random() *255);
			int blue = (int)(Math.random() *255);
			Color newColor = new Color(red, green, blue);		//generating the random color from RGB values calculated
			for (Point member : region){						//setting the new recolored Image to the color generated randomly
				recoloredImage.setRGB(member.x, member.y, newColor.getRGB()); //set the members of the desired to the random color
			}
		}
	}
}
