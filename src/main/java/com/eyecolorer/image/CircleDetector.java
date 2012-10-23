package com.eyecolorer.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;

import jip.JIPImage;
import jip.JIPToolkit;

import org.apache.log4j.Logger;

import com.eyecolorer.image.functions.Binarize;
import com.eyecolorer.image.functions.Canny;
import com.eyecolorer.image.functions.Crop;
import com.eyecolorer.image.functions.GrayToGray;
import com.eyecolorer.image.functions.HoughCirc;
import com.eyecolorer.image.functions.SmoothMedian;

public class CircleDetector {

	private static Logger log = Logger.getLogger(CircleDetector.class.getName());
	// constants for the detection
	private static final int SMOOTH_RADIUS = 2;
	private static final float CANNY_SIGMA = 1.0f;
	private static final int CANNY_BRIGHTNESS = 100;
	private static final int BINARIZE_LOW_THRESHOLD = 25;
	private static final int BINARIZE_HIGH_THRESHOLD = 255;

	/**
	 * Get a list of circles detected in an image. The parameters are tweaked
	 * for iris and pupil detection
	 * 
	 * @param bi
	 * @return
	 */
	public static List<Circle> getEyeCandidates(BufferedImage bi) {
		JIPImage jipImage = JIPToolkit.getColorImage(bi);

		SmoothMedian smoothMedian = new SmoothMedian();
		smoothMedian.setRadius(SMOOTH_RADIUS);
		JIPImage imgFilterMedian = smoothMedian.processImg(jipImage);

		// Canny edge detection
		Canny canny = new Canny();
		canny.setSigma(CANNY_SIGMA);
		canny.setBrightness(CANNY_BRIGHTNESS);
		JIPImage imgCanny = canny.processImg(imgFilterMedian);

		GrayToGray grayToGray = new GrayToGray();
		JIPImage imageGray = grayToGray.processImg(imgCanny);

		// Binarize the image for circle detection
		Binarize binarize = new Binarize();
		binarize.setU1(BINARIZE_LOW_THRESHOLD);
		binarize.setU2(BINARIZE_HIGH_THRESHOLD);
		JIPImage imgBinarize = binarize.processImg(imageGray);
		List<Circle> circleClist = performPass(imgBinarize, 0, 30, 20, 70);

		if (circleClist.size() == 0) {
			// no circles found, let's do another pass with more relaxed args
			log.debug("Performing second pass");
			circleClist = performPass(imgBinarize, 50, 28, 20, 60);
		}
		log.debug(printCircles(circleClist));
		return circleClist;
	}

	/**
	 * Performs a pass looking for circles in a binary image with the selected
	 * parameters
	 * 
	 * @param imgBinarize
	 * @param cropping
	 * @param houghThreshold
	 * @param houghMinRadius
	 * @param houghMaxRadius
	 * @return
	 */
	private static List<Circle> performPass(JIPImage imgBinarize, int cropping, int houghThreshold, int houghMinRadius, int houghMaxRadius) {
		// crop the image to the center
		Crop crop = new Crop();
		crop.setX(cropping);
		crop.setY(cropping);
		crop.setW(imgBinarize.getWidth() - cropping * 2);
		crop.setH(imgBinarize.getHeight() - cropping * 2);
		imgBinarize = crop.processImg(imgBinarize);

		// detect circles
		HoughCirc houghCirc = new HoughCirc();
		houghCirc.setThreshold(houghThreshold);
		houghCirc.setRmin(houghMinRadius);
		houghCirc.setRmax(houghMaxRadius);
		houghCirc.processImg(imgBinarize);
		return houghCirc.getCircles(cropping);
	}

	/**
	 * Used for debugging purposes. Return a string with the list of circles
	 * 
	 * @return
	 */
	private static String printCircles(List<Circle> circleList) {
		String result;
		result = "Circles before filter: " + circleList.size();
		for (int i = 0; i < circleList.size(); i++) {
			Circle c = circleList.get(i);
			result += "circle" + i + ": center X: " + c.getCenterX() + " center Y: " + c.getCenterY() + " radius: " + c.getRadius() + "\n";
		}
		return result;
	}

	@SuppressWarnings("unused")
	/**
	 * This is used for debug purposes. It generates an image from a binarized image
	 * @param imgBinarize
	 * @param scaleFactor
	 * @return
	 */
	private static BufferedImage calculateImageBuffer(JIPImage imgBinarize, double scaleFactor) {
		// no eyes found, return image
		Image binary = JIPToolkit.getAWTImage(imgBinarize);
		BufferedImage imageBuff = new BufferedImage((int) (binary.getWidth(null)), (int) (binary.getHeight(null)), BufferedImage.TYPE_INT_RGB);

		Graphics g = imageBuff.createGraphics();
		g.drawImage(binary, 0, 0, new Color(0, 0, 0), null);
		g.dispose();
		imageBuff = ImageUtil.scaleImage(imageBuff, 1 / scaleFactor);
		return imageBuff;
	}

}
