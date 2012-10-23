package com.eyecolorer.image;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import jviolajones.Detector;

import org.apache.log4j.Logger;

import com.eyecolorer.filters.OneColorFilter;

/**
 * Changes the eye color of an image of the face
 * 
 * @author ecuevas
 * 
 */
public class EyeDetector {

	private static Logger log = Logger.getLogger(EyeDetector.class.getName());

	private List<Rectangle> eyesList;
	private double scaleFactor;
	private double faceScaleFactor;
	private List<Rectangle> facesList;

	public List<Rectangle> getFacesList() {
		return facesList;
	}

	public void setFacesList(List<Rectangle> facesList) {
		this.facesList = facesList;
	}

	public List<Rectangle> getEyesList() {
		return eyesList;
	}

	public void setEyesList(List<Rectangle> eyesList) {
		this.eyesList = eyesList;
	}

	public double getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(double scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public double getFaceScaleFactor() {
		return faceScaleFactor;
	}

	public void setFaceScaleFactor(double faceScaleFactor) {
		this.faceScaleFactor = faceScaleFactor;
	}

	public BufferedImage getEyesChangeMultiFace(BufferedImage originalImage, Color firstColor, Color secondColor) {
		BufferedImage thumbImage;
		// prepare image
		this.faceScaleFactor = ImageUtil.getScaleFactor(900, originalImage);
		// Scale to optimize for haar cascades
		thumbImage = ImageUtil.scaleImage(originalImage, faceScaleFactor);
		this.facesList = detectFaces(thumbImage);

		// detect overlapping rectangles
		facesList = removeOverlappingRectangles(facesList);
		log.debug("Faces after filter: " + facesList.size());

		for (Rectangle rectangle : facesList) {
			// extract all the eyes from the original image using the
			// generated faces
			BufferedImage extracted = ImageUtil.cropImage(originalImage, (int) (rectangle.x / faceScaleFactor), (int) (rectangle.y / faceScaleFactor), (int) (rectangle.width / faceScaleFactor),
					(int) (rectangle.height / faceScaleFactor));

			EyeDetector auxEyeDetector = new EyeDetector();

			extracted = auxEyeDetector.getEyesChange(extracted, firstColor, firstColor);
			if (extracted != null) {
				// tengo los ojos pintados, combinar con la original
				log.debug("Extracted face, paint it");
				originalImage = ImageUtil.combineImages(originalImage, extracted, (int) (rectangle.x / faceScaleFactor), (int) (rectangle.y / faceScaleFactor));
			}
		}
		return originalImage;
	}

	/**
	 * Changes the eyes color of the image passed
	 * 
	 * @param originalImage
	 * @param firstColor
	 * @param secondColor
	 * @return
	 */
	public BufferedImage getEyesChange(BufferedImage originalImage, Color firstColor, Color secondColor) {
		BufferedImage thumbImage;
		// prepare image
		// Speed optimization. If the image is large enough, is safe to crop
		// the bottom third
		if (originalImage.getHeight() > 1024) {
			thumbImage = ImageUtil.cropImage(originalImage, 0, 0, originalImage.getWidth(), (int) (originalImage.getHeight() / 1.5));
		}
		this.scaleFactor = ImageUtil.getScaleFactor(800, originalImage);
		// Scale to optimize for haar cascades
		thumbImage = ImageUtil.scaleImage(originalImage, scaleFactor);
		EyeDetector eyeDetector = new EyeDetector();
		eyesList = eyeDetector.detectEyes(thumbImage);

		// detect overlapping rectangles
		eyesList = removeOverlappingRectangles(eyesList);
		log.debug("Eyes after filter: " + eyesList.size());
		for (Rectangle rectangle : eyesList) {
			log.debug("Eye detected: height:" + rectangle.height + "width:" + rectangle.width + " x:" + rectangle.x + " y:" + rectangle.y);
		}

		for (Rectangle rectangle : eyesList) {
			// extract all the eyes from the original image using the
			// generated faces
			BufferedImage extracted = ImageUtil.cropImage(originalImage, (int) (rectangle.x / scaleFactor), (int) (rectangle.y / scaleFactor), (int) (rectangle.width / scaleFactor),
					(int) (rectangle.height / scaleFactor));
			OneColorFilter eyeColorer = new OneColorFilter();
			extracted = eyeColorer.changeEyeColor(firstColor, firstColor, extracted);
			if (extracted != null) {
				// tengo el ojo pintado, combinar con la original
				log.debug("Extracted eye, paint it");
				originalImage = ImageUtil.combineImages(originalImage, extracted, (int) (rectangle.x / scaleFactor), (int) (rectangle.y / scaleFactor));
			}
		}
		return originalImage;
	}

	/**
	 * Removes the overlapping rectangles from a list
	 * 
	 * @param rectList
	 *            The list of rectangles to filter
	 * @return The filtered list of rectangles
	 */
	private List<Rectangle> removeOverlappingRectangles(List<Rectangle> rectList) {
		HashSet<Rectangle> rectangleClearSet = new HashSet<Rectangle>();
		List<Rectangle> clearList = new ArrayList<Rectangle>();
		for (Rectangle innerRectangle : rectList) {
			boolean alone = true;
			for (Rectangle rectangle : rectList) {
				if (!innerRectangle.equals(rectangle) && innerRectangle.intersects(rectangle)) {
					alone = false;
					if (innerRectangle.width > rectangle.width) {
						rectangleClearSet.add(innerRectangle);
					} else {
						rectangleClearSet.add(rectangle);
					}
				}
			}
			if (alone) {
				rectangleClearSet.add(innerRectangle);
			}
		}
		clearList.addAll(rectangleClearSet);
		return clearList;
	}

	/**
	 * Detect the eye regions of an image
	 * 
	 * @param image
	 * @return The list of rectangles with the detected eyes
	 */
	private List<Rectangle> detectEyes(BufferedImage image) {

		Detector detector;
		detector = new Detector(getClass().getClassLoader().getResourceAsStream("haarcascade_eye.xml"));

		List<Rectangle> res = detector.getFaces(image, 1.2f, 1.1f, .05f, 2, true);

		log.debug(res.size() + " faces found!");
		return res;
	}

	/**
	 * Detect the eye regions of an image
	 * 
	 * @param image
	 * @return The list of rectangles with the detected eyes
	 */
	private List<Rectangle> detectFaces(BufferedImage image) {

		Detector detector;
		detector = new Detector(getClass().getClassLoader().getResourceAsStream("haarcascade_frontalface_default.xml"));

		List<Rectangle> res = detector.getFaces(image, 1.2f, 1.1f, .05f, 2, true);

		log.debug(res.size() + " faces found!");
		return res;
	}
}
