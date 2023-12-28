import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A class demonstrating manipulation of image pixels.
 * Version 0: just the core definition
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for BufferedImage
 * @author CBK, Spring 2015, refactored to separate GUI from operations
 */
public class ImageProcessor0 {
	private BufferedImage image;		// the current image being processed

	/**
	 * @param image		the original
	 */
	public ImageProcessor0(BufferedImage image) {
		this.image = image;
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
	/**
	 * This function creates a square with a mix of color, RGB, to produce a floral color.
	 * Steps:
	 * 	1. Obtained the RGB value of every pixel and manipulated it to display floral color.
	 * 	2. created local variable dx and dy that are generated randomly to increase the size of the square
	 * 	3. The pixel at x, and y every time the loop runs is set to new pixel color.
	 *
	 * 	To display this, one can drag across the open window.
	 * 	Select 'v' on the keyboard turns the brushUp hence it is not painted.
	 * 	Selecting 'y' changes back to brushDown to reverse the event triggered by 'v'
	 *
	 * @param wx - the selected x coordinate using the Mouse
	 * @param hy - selected x coordinate using the Mouse
	 * @param r - radius around wx and wy for which the floralSquare shape is drawn.
	 *
	 */
	public void floralSquare(int wx, int hy, int r){
		for (int h = Math.max(0, hy-r); h < Math.min(image.getHeight(), hy+r); h++){
			for (int w = Math.max(0, wx-r); w < Math.min(image.getWidth(), wx+r); w++){
				Color color = new Color(image.getRGB(w, h));
				int red = (int)(color.getRed()/3); //ensure red value is between 0 and 255
				int green = (int)(color.getGreen());
				int blue = (int)(color.getBlue()/2);
				Color newColor = new Color(red, green, blue);
				int dx = (int) (10*(Math.random()-0.5));
				int dy = (int) (10*(Math.random()-0.5));
				image.setRGB(w+dx, h+dy, (int)color.getRGB()/3);
			}
		}
	}
}
