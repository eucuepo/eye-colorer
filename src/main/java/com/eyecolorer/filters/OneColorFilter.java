package com.eyecolorer.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.log4j.Logger;

import com.eyecolorer.image.Circle;
import com.eyecolorer.image.CircleDetector;
import com.eyecolorer.image.ImageUtil;

public class OneColorFilter implements EyeFilter {

	private static Logger log = Logger.getLogger(OneColorFilter.class.getName());

	/**
	 * Changes the color of the eye of the image provided
	 * 
	 * @param eyeColor
	 *            The RGB Color to change to
	 * @param toChange
	 *            The image of the eye
	 * @return
	 */
	public BufferedImage changeEyeColor(Color eyeColor, Color secondEyeColor, BufferedImage toChange) {
		// Scale the original image for processing
		double scaleFactor = ImageUtil.getScaleFactor(300, toChange);
		BufferedImage bi = ImageUtil.scaleImage(toChange, scaleFactor);

		// detect the circles
		List<Circle> circleList = CircleDetector.getEyeCandidates(bi);

		// no circles detected, end processing
		if (circleList.size() == 0) {
			return null;
		}

		if (circleList.size() > 0 && circleList.get(0) != null) {
			// If the pupil has not been detected, calculate it to 1/3 of the
			// iris radius
			Circle pupil = new Circle(circleList.get(0).getX(), circleList.get(0).getY() + 1, circleList.get(0).getRadius() / 3);
			log.debug("Adding pupil: " + pupil);
			circleList.add(pupil);
		}
		// filter circles that doesn't intersect with another
		circleList = Circle.getConcentricCircunferencia(circleList);

		// No circles detected return null
		if (circleList.size() < 2) {
			return null;
		}

		// look for the closest centers, that would be the pupil and iris
		Circle[] eye = Circle.getClosestCenters(circleList);

		log.debug("Number of circles after filters: " + circleList.size());

		BufferedImage eyeImage;
		if (eye[0] == null || eye[1] == null) {
			return null;
		} else {
			// print the eyes to a mask
			log.debug("pupil: " + eye[0].toString());
			log.debug("iris: " + eye[1].toString());
			eyeImage = ImageUtil.createEyeMask((int) (bi.getWidth() / scaleFactor), (int) (bi.getHeight() / scaleFactor), (int) (eye[0].getRadius() * 2 / scaleFactor),
					(int) (eye[1].getRadius() * 2 / scaleFactor), (int) ((eye[1].getX() - eye[1].getRadius()) / scaleFactor), (int) ((eye[1].getY() - eye[1].getRadius()) / scaleFactor),
					(int) ((eye[0].getX() - eye[0].getRadius()) / scaleFactor), (int) ((eye[0].getY() - eye[0].getRadius()) / scaleFactor), eyeColor, secondEyeColor);
		}
		// mix the original image and the masks
		return ImageUtil.combineImages(toChange, eyeImage);

	}

	public BufferedImage changeEyeColor() {
		// TODO Auto-generated method stub
		return null;
	}
}
