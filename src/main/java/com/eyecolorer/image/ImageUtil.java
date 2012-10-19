package com.eyecolorer.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;

import jip.JIPImage;
import jip.JIPToolkit;
import jipfunc.FBinarize;
import jipfunc.FCanny;
import jipfunc.FColorToGray;
import jipfunc.FGrayToGray;
import jipfunc.FHoughCirc;
import jiputil.Circunferencia;

import com.eyecolorer.image.functions.SmoothMedian;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public class ImageUtil {

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
	public static BufferedImage createEyeMask(int imageWidth, int imageHeight,
			int innerRadius, int outerRadius, int posX, int posY, Color eyeColor) {
		// get the eye shape
		Shape eye = generateEye(posX, posY, innerRadius, outerRadius);
		BufferedImage mask = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = mask.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Set the eye color
		// TODO: Add alpha parameter
		// TODO: Fade borders with gradient paint
		// Create and set a RadialGradient centered on the object,
		// going from white at the center to black at the edges
		// RadialGradientPaint paint = new RadialGradientPaint(center, radius,
		// dist, colors);
		// g2d.setPaint(paint);
		Color paint = new Color(eyeColor.getRed(), eyeColor.getGreen(),
				eyeColor.getBlue(), 80);
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
	private static Shape generateEye(double x, double y, double innerRadius,
			double outerRadius) {
		// create iris circle
		Area iris = new Area(new Ellipse2D.Double(x, y, outerRadius,
				outerRadius));
		double innerOffset = (outerRadius - innerRadius) / 2;
		// create pupil circle
		Area pupil = new Area(new Ellipse2D.Double(x + innerOffset, y
				+ innerOffset, innerRadius, innerRadius));
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

	public static BufferedImage changeEyeColor(Color eyeColor,
			BufferedImage toChange) {
		double scaleFactor = getScaleFactor(400, toChange);
		BufferedImage bi = scaleImage(toChange, scaleFactor);

		JIPImage jipImage = JIPToolkit.getColorImage(bi);

		SmoothMedian smoothMedian = new SmoothMedian();
		smoothMedian.setRadius(3);
		JIPImage imgFiltroMediania = smoothMedian.processImg(jipImage);

		// TODO: Wrapper de canny
		FCanny fCanny = new FCanny();
		fCanny.setParamValue("sigma", 1.0f);
		fCanny.setParamValue("brightness", 100);
		JIPImage imgCanny = fCanny.processImg(imgFiltroMediania);

		ImageIcon iris = new ImageIcon(JIPToolkit.getAWTImage(imgCanny));

		FGrayToGray fGrayToGray = new FGrayToGray();
		JIPImage imageGray = fGrayToGray.processImg(imgCanny);

		// TODO: Wrapper de binarize
		FBinarize fBinarize = new FBinarize();
		fBinarize.setParamValue("u1", 40);
		fBinarize.setParamValue("u2", 255);
		JIPImage imgBinarize = fBinarize.processImg(imageGray);

		// TODO: Adjust the values with image dimensions
		FHoughCirc fHougCir = new FHoughCirc();
		fHougCir.setParamValue("thres", 25);
		fHougCir.setParamValue("Rmin", 10);
		fHougCir.setParamValue("Rmax", 80);
		fHougCir.processImg(imgBinarize);

		Image iris2 = JIPToolkit.getAWTImage(imgBinarize);

		System.out.println("Numero de circulos antes de filtro: "
				+ fHougCir.getResultValueInt("ncirc"));
		Vector<Circunferencia> vecAux = (Vector<Circunferencia>) fHougCir
				.getResultValueObj("circum");
		List<Circunferencia> listaCirculos = new ArrayList<Circunferencia>();
		listaCirculos.addAll(vecAux);
		listaCirculos = Circulo.getConcentricCircunferencia(listaCirculos);

		// Graphics2D g2d = (Graphics2D) jLabel1.getGraphics();
		// g2d.drawImage(iris.getImage(), 0, 0, null);
		// repaint();
		// dp.setIris(vecAux.get(0));
		// dp.setPupila(vecAux.get(1));

		// buscar los mas cercanos
		if (listaCirculos.size() < 2) {
			// no eyes found, return image
			Image binary = JIPToolkit.getAWTImage(imgBinarize);
			BufferedImage imageBuff = new BufferedImage(binary.getWidth(null),
					binary.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics g = imageBuff.createGraphics();
			g.drawImage(binary, 0, 0, new Color(0, 0, 0), null);
			g.dispose();
			return imageBuff;
		}
		Circunferencia[] eye = Circulo.getClosestCenters(listaCirculos);

		for (int i = 0; i < vecAux.size(); i++) {
			Circunferencia c = (Circunferencia) vecAux.get(i);
			System.out.println("circunferencia " + i + ": centro X: "
					+ c.centroX + " centroY: " + c.centroY + " radio: "
					+ c.radio);
		}

		System.out.println("Numero de circulos despues de filtro: "
				+ listaCirculos.size());
		int i = 0;
		for (Circunferencia circunferencia : listaCirculos) {
			i++;
			System.out.println("circunferencia " + i + ": centro X: "
					+ circunferencia.centroX + " centroY: "
					+ circunferencia.centroY + " radio: "
					+ circunferencia.radio);
		}

		/* end of crap */

		// TODO: change to detected eye position
		BufferedImage leftEye = ImageUtil
				.createEyeMask((int) (bi.getWidth() / scaleFactor),
						(int) (bi.getHeight() / scaleFactor),
						(int) (eye[0].radio * 2 / scaleFactor),
						(int) (eye[1].radio * 2 / scaleFactor),
						(int) ((eye[1].centroX - eye[1].radio) / scaleFactor),
						(int) ((eye[1].centroY - eye[1].radio) / scaleFactor),
						eyeColor);
		// BufferedImage leftEye =
		// ImageUtil.createEyeMask(bi.getWidth(), bi.getHeight(), 20,
		// 80, 50, 50, eyeColor);
		// BufferedImage rightEye =
		// ImageUtil.createEyeMask(bi.getWidth(), bi.getHeight(), 20,
		// 80, 200, 50, eyeColor);

		// mix the original image and the masks
		BufferedImage masked = ImageUtil.combineImages(toChange, leftEye);
		// masked = ImageUtil.combineImages(masked, rightEye);
		// masked = ImageUtil.cropImage(masked, 50, 100, 200, 300);
		// alter the image
		return masked;
	}

	public static double getScaleFactor(int maxWidth, BufferedImage img) {

		if (img.getWidth() < maxWidth) { // image is small, do nothing
			return 1;
		} else {
			return maxWidth / (double) img.getWidth();
		}

	}

	public static BufferedImage scaleImage(BufferedImage img, double scaleFactor) {
		double oldHeight = img.getHeight();
		if (scaleFactor >= 1) { // image is small, do nothing
			return img;
		}
		System.out.println("Factor de escala: " + scaleFactor);
		System.out.println("Escalando imagen: Tamaño original:"
				+ img.getWidth() + "x" + img.getHeight()
				+ ". Nuevo tamaño: 400x" + (int) (oldHeight * scaleFactor));
		Image scaledImage = img.getScaledInstance(400,
				(int) (oldHeight * scaleFactor), Image.SCALE_SMOOTH);
		BufferedImage imageBuff = new BufferedImage(400,
				(int) (oldHeight * scaleFactor), BufferedImage.TYPE_INT_RGB);
		Graphics g = imageBuff.createGraphics();
		g.drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);
		g.dispose();
		return imageBuff;
	}

}
