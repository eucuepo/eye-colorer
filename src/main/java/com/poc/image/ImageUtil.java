package com.poc.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class ImageUtil {

	public static BufferedImage applyGrayscaleMaskToAlpha(BufferedImage image, BufferedImage mask) {
		int width = image.getWidth();
		int height = image.getHeight();

		// get the image pixels
		int[] imagePixels = image.getRGB(0, 0, width, height, null, 0, width);
		int[] maskPixels = mask.getRGB(0, 0, width, height, null, 0, width);

		for (int i = 0; i < imagePixels.length; i++) {
			int color = imagePixels[i] & 0x00ffffff; // Mask preexisting alpha
			int alpha = maskPixels[i] << 24; // Shift green to alpha
			imagePixels[i] = color | alpha;
		}

		image.setRGB(0, 0, width, height, imagePixels, 0, width);
		return image;
	}

	/**
	 * Creates an "Eye" mask for an image with the parameters passed
	 * 
	 * @param imageWidth
	 *            Width of the original image in pixels
	 * @param imageHeight
	 *            Height of the original image in pixels
	 * @param innerRadius
	 *            Radius for the pupil in pixels
	 * @param outerRadius
	 *            Radius for the iris in pixels
	 * @param posX
	 *            X coordinate of the eye
	 * @param posY
	 *            Y coordinate of the eye
	 * @param eyeColor
	 * @return A buffered image containing the mask
	 */
	public static BufferedImage createEyeMask(int imageWidth, int imageHeight, int innerRadius, int outerRadius, int posX, int posY, Color eyeColor) {
		// get the eye shape
		Shape eye = generateEye(posX, posY, innerRadius, outerRadius);
		BufferedImage mask = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = mask.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Set the eye color
		// TODO: Add alpha parameter
		Color paint = new Color(eyeColor.getRed(), eyeColor.getGreen(), eyeColor.getBlue(), 80);
		g.setPaint(paint);

		// paint the eye!
		g.fill(eye);
		g.dispose();
		mask.flush();

		return mask;
	}

	/**
	 * Generates an "Eye" shape with colored iris and transparent retina
	 * 
	 * @param x
	 *            The x coordinate of the shape
	 * @param y
	 *            The y coordinate of the shape
	 * @param innerRadius
	 *            inner radius of the circle
	 * @param outerRadius
	 *            outer radius of the circle
	 * @return
	 */
	private static Shape generateEye(double x, double y, double innerRadius, double outerRadius) {
		// create iris circle
		Area iris = new Area(new Ellipse2D.Double(x, y, outerRadius, outerRadius));
		double innerOffset = (outerRadius - innerRadius) / 2;
		// create pupil circle
		Area pupil = new Area(new Ellipse2D.Double(x + innerOffset, y + innerOffset, innerRadius, innerRadius));
		// substract pupil
		iris.subtract(pupil);
		return iris;
	}

	/**
	 * Combine two images into one. The first image will be overlapped by the
	 * second one
	 * 
	 * @param image
	 *            bottom image
	 * @param overlay
	 *            top image
	 * @return
	 */
	public static BufferedImage combineImages(BufferedImage image, BufferedImage overlay) {

		// create the new image, canvas size is the max. of both image sizes
		int w = Math.max(image.getWidth(), overlay.getWidth());
		int h = Math.max(image.getHeight(), overlay.getHeight());
		BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		// paint both images, preserving the alpha channels
		Graphics g = combined.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.drawImage(overlay, 0, 0, null);

		// Save as new image
		return combined;
	}

	/**
	 * Crops an image returning the resulting one
	 * 
	 * @param image
	 *            The image to crop
	 * @param topX
	 *            The upper x bound of the rectangle
	 * @param topY
	 *            The upper y bound of the rectangle
	 * @param width
	 *            The width of the rectangle
	 * @param height
	 *            The height of the rectangle
	 * @return
	 */
	public static BufferedImage cropImage(BufferedImage image, int topX, int topY, int width, int height) {
		// create cropping rectangle
		BufferedImage dest = image.getSubimage(topX, topY, width, height);
		// return cropped image
		return dest;
	}

}
