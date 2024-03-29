package com.eyecolorer.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;

/**
 * Implements functions for image processing like cropping or scaling
 * 
 * @author ecuevas
 * 
 */
public class ImageUtil {

	private static Logger log = Logger.getLogger(ImageUtil.class.getName());

	/**
	 * Creates an "Eye" mask for an image with the parameters passed. This is a
	 * convenience method for painting just one color
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
	 *            the RGB color of the eye
	 * 
	 * @return A buffered image containing the mask
	 */
	public static BufferedImage createEyeMask(int imageWidth, int imageHeight,
			int innerRadius, int outerRadius, int posX, int posY,
			int pupilPosX, int pupilPosY, Color eyeColor, double scaleFactor) {
		// get the eye shape
		Shape eye = generateEye(posX, posY, pupilPosX, pupilPosY, innerRadius,
				outerRadius);
		BufferedImage mask = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = mask.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		log.debug("Paint radius: " + (outerRadius - innerRadius));
		// Set the eye color
		// Create and set a RadialGradient centered on the eye to outside
		Color[] colors = {
				new Color(eyeColor.getRed(), eyeColor.getGreen(),
						eyeColor.getBlue(), 10),
				new Color(eyeColor.getRed(), eyeColor.getGreen(),
						eyeColor.getBlue(), 60),
				new Color(eyeColor.getRed(), eyeColor.getGreen(),
						eyeColor.getBlue(), 60),
				new Color(eyeColor.getRed(), eyeColor.getGreen(),
						eyeColor.getBlue(), 30) };
		Point2D center = new Point2D.Float(pupilPosX + (innerRadius / 2),
				pupilPosY + (innerRadius / 2));
		float[] dist = { .0f, .6f, .95f, 1f };
		RadialGradientPaint radialPaint = new RadialGradientPaint(center,
				(int) (outerRadius * scaleFactor), dist, colors,
				CycleMethod.NO_CYCLE);
		g.setPaint(radialPaint);

		// paint the eye!
		g.fill(eye);
		g.dispose();
		mask.flush();

		return mask;
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
	 *            the RGB color of the eye
	 * 
	 * @param secondEyeColor
	 *            a secondary RGB color of the eye
	 * @return A buffered image containing the mask
	 */
	public static BufferedImage createEyeMask(int imageWidth, int imageHeight,
			int innerRadius, int outerRadius, int posX, int posY,
			int pupilPosX, int pupilPosY, Color eyeColor, Color secondEyeColor,
			double scaleFactor) {
		// get the eye shape
		Shape eye = generateEye(posX, posY, pupilPosX, pupilPosY, innerRadius,
				outerRadius);
		BufferedImage mask = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = mask.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		log.debug("Paint radius: " + (outerRadius - innerRadius));
		// Set the eye color
		// Create and set a RadialGradient centered on the eye to outside
		Color[] colors = {
				new Color(eyeColor.getRed(), eyeColor.getGreen(),
						eyeColor.getBlue(), 10),
				new Color(eyeColor.getRed(), eyeColor.getGreen(),
						eyeColor.getBlue(), 60),
				new Color(secondEyeColor.getRed(), secondEyeColor.getGreen(),
						secondEyeColor.getBlue(), 60),
				new Color(secondEyeColor.getRed(), secondEyeColor.getGreen(),
						secondEyeColor.getBlue(), 30) };
		Point2D center = new Point2D.Float(pupilPosX + (innerRadius / 2),
				pupilPosY + (innerRadius / 2));
		float[] dist = { .0f, .4f, .6f, 1f };
		RadialGradientPaint radialPaint = new RadialGradientPaint(center,
				(int) (outerRadius * scaleFactor), dist, colors,
				CycleMethod.NO_CYCLE);
		g.setPaint(radialPaint);

		// paint the eye!
		g.fill(eye);
		g.dispose();
		mask.flush();

		return mask;
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
	 *            the RGB color of the eye
	 * 
	 * @param secondEyeColor
	 *            a secondary RGB color of the eye
	 * @return A buffered image containing the mask
	 */
	public static BufferedImage createEyeTextureMask(int imageWidth,
			int imageHeight, int innerRadius, int outerRadius, int posX,
			int posY, int pupilPosX, int pupilPosY, double scaleFactor,
			BufferedImage texture) {

		// get the eye shape
		Shape eye = generateEye(posX, posY, pupilPosX, pupilPosY, innerRadius,
				outerRadius);
		BufferedImage mask = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = mask.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		TexturePaint texturePaint = new TexturePaint(texture, new Rectangle(0,
				0, 300, 300));

		g.setPaint(texturePaint);

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
	private static Shape generateEye(double x, double y, double pupilPosX,
			double pupilPosY, double innerRadius, double outerRadius) {
		// create iris circle
		Area iris = new Area(new Ellipse2D.Double(x, y, outerRadius,
				outerRadius));
		// double innerOffset = (outerRadius - innerRadius) / 2;
		// create pupil circle
		Area pupil = new Area(new Ellipse2D.Double(pupilPosX, pupilPosY,
				innerRadius, innerRadius));
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
	public static BufferedImage combineImages(BufferedImage image,
			BufferedImage overlay) {

		// create the new image, canvas size is the max. of both image sizes
		int w = Math.max(image.getWidth(), overlay.getWidth());
		int h = Math.max(image.getHeight(), overlay.getHeight());
		BufferedImage combined = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);

		// paint both images, preserving the alpha channels
		Graphics g = combined.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.drawImage(overlay, 0, 0, null);

		// Save as new image
		return combined;
	}

	/**
	 * Combine two images into one. The first image will be overlapped by the
	 * second one, starting with the overlay coordinates
	 * 
	 * @param image
	 * @param overlay
	 * @param overlayX
	 * @param overlayY
	 * @return
	 */
	public static BufferedImage combineImages(BufferedImage image,
			BufferedImage overlay, int overlayX, int overlayY) {

		BufferedImage combined = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		// paint both images, preserving the alpha channels
		Graphics g = combined.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.drawImage(overlay, overlayX, overlayY, null);

		log.debug("Combining images: overlayX" + overlayX + " overlayY:"
				+ overlayY);
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
	public static BufferedImage cropImage(BufferedImage image, int topX,
			int topY, int width, int height) {
		// create cropping rectangle
		BufferedImage dest = image.getSubimage(topX, topY, width, height);
		// return cropped image
		return dest;
	}

	public static double getScaleFactor(int maxWidth, BufferedImage img) {

		if (img.getWidth() < maxWidth) { // image is small, do nothing
			return 1;
		} else {
			return maxWidth / (double) img.getWidth();
		}

	}

	public static double getRealScaleFactor(int maxWidth, BufferedImage img) {
		return maxWidth / (double) img.getWidth();
	}

	/**
	 * Scales an image aplying the scalefactor provided
	 * 
	 * @param img
	 *            The image to scale
	 * @param scaleFactor
	 *            the factor to scale. For example, 0.5 means half.
	 * @return The scaled image
	 */
	public static BufferedImage scaleImage(BufferedImage img, double scaleFactor) {
		double oldHeight = img.getHeight();
		double oldWidth = img.getWidth();
		log.debug("Scale factor: " + scaleFactor);
		log.debug("Scaling image: original size:" + img.getWidth() + "x"
				+ img.getHeight() + ". New size: "
				+ (int) (oldWidth * scaleFactor) + "x"
				+ (int) (oldHeight * scaleFactor));
		Image scaledImage = img.getScaledInstance(
				(int) (oldWidth * scaleFactor),
				(int) (oldHeight * scaleFactor), Image.SCALE_FAST);
		BufferedImage imageBuff = new BufferedImage(
				(int) (oldWidth * scaleFactor),
				(int) (oldHeight * scaleFactor), img.getType());
		Graphics g = imageBuff.createGraphics();
		g.drawImage(scaledImage, 0, 0, new Color(0, 0, 0, 0), null);
		g.dispose();
		return imageBuff;
	}

	/**
	 * Applies a grayscale mask to an image, returning just the pixels that
	 * intersect with the mask
	 * 
	 * @param image
	 * @param mask
	 * @return
	 */
	public static BufferedImage applyGrayscaleMaskToAlpha(BufferedImage image,
			BufferedImage mask) {
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
	 * Converts a buffered image with an alpha channel to a jpg-ready non-alpha
	 * image
	 * 
	 * @param image
	 *            the image to convert
	 * @return the image without the alpha channel
	 */
	public static BufferedImage convertToJpg(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage image2 = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image2.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);
		g.drawRenderedImage(image, null);
		g.dispose();
		return image2;
	}

}
