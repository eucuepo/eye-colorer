package com.eyecolorer.filters;

import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.log4j.Logger;

import com.eyecolorer.image.Circle;
import com.eyecolorer.image.CircleDetector;
import com.eyecolorer.image.ImageUtil;

public abstract class ColorFilter implements EyeFilter {

	protected BufferedImage image;
	protected BufferedImage scaledImage;
	protected double scaleFactor;
	protected Circle[] eye;

	public ColorFilter(BufferedImage image) {
		this.image = image;
	}

	private static Logger log = Logger.getLogger(Class.class.getName());

	public BufferedImage changeEyeColor() {
		// Scale the original image for processing
		scaleFactor = ImageUtil.getScaleFactor(300, image);
		scaledImage = ImageUtil.scaleImage(image, scaleFactor);

		// detect the circles
		List<Circle> circleList = CircleDetector.getEyeCandidates(image);

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
		eye = Circle.getClosestCenters(circleList);

		log.debug("Number of circles after filters: " + circleList.size());

		BufferedImage eyeImage;
		if (eye[0] == null || eye[1] == null) {
			return null;
		} else {
			// print the eyes to a mask
			log.debug("pupil: " + eye[0].toString());
			log.debug("iris: " + eye[1].toString());
			eyeImage = createEyeMask();
		}
		// mix the original image and the masks
		return ImageUtil.combineImages(image, eyeImage);

	}

	public abstract BufferedImage createEyeMask();
}
