package com.eyecolorer.image;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import jviolajones.Detector;

import org.apache.log4j.Logger;

public class EyeDetector {

	private static Logger log = Logger.getLogger(EyeDetector.class.getName());

	private List<Rectangle> eyesList;
	private double scaleFactor;

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

	public BufferedImage getEyesChange(BufferedImage originalImage,
			Color newColor) {
		BufferedImage thumbImage;
		// prepare image
		// Speed optimization. If the image is large enough, is safe to crop
		// the bottom third
		if (originalImage.getHeight() > 1024) {
			thumbImage = ImageUtil.cropImage(originalImage, 0, 0,
					originalImage.getWidth(),
					(int) (originalImage.getHeight() / 1.5));
		}
		this.scaleFactor = ImageUtil.getScaleFactor(600, originalImage);
		// Scale to optimize for haar cascades
		thumbImage = ImageUtil.scaleImage(originalImage, scaleFactor);
		EyeDetector eyeDetector = new EyeDetector();
		eyesList = eyeDetector.detectEyes(thumbImage);

		// detect overlapping rectangles
		eyesList = removeOverlappingRectangles(eyesList);
		log.debug("Rectangles after filter: " + eyesList.size());
		for (Rectangle rectangle : eyesList) {
			log.debug("Eye detected: height:" + rectangle.height + "width:"
					+ rectangle.width + " x:" + rectangle.x + " y:"
					+ rectangle.y);
		}

		for (Rectangle rectangle : eyesList) {
			// extract all the eyes from the original image using the
			// generated faces
			BufferedImage extracted = ImageUtil.cropImage(originalImage,
					(int) (rectangle.x / scaleFactor),
					(int) (rectangle.y / scaleFactor),
					(int) (rectangle.width / scaleFactor),
					(int) (rectangle.height / scaleFactor));
			EyeColorer eyeColorer = new EyeColorer();
			extracted = eyeColorer.changeEyeColor(newColor, extracted);
			if (extracted != null) {
				// tengo el ojo pintado, combinar con la original
				log.debug("Extracted eye, paint it");
				originalImage = ImageUtil.combineImages(originalImage,
						extracted, (int) (rectangle.x / scaleFactor),
						(int) (rectangle.y / scaleFactor));
//				ImageFrame imageFrame = new ImageFrame(extracted);
//				imageFrame.setSize(new Dimension(400, 400));
//				imageFrame.setVisible(true);
			}
		}
		return originalImage;
	}

	/**
	 * Removes the overlapping rectangles from a list
	 * @param rectList
	 * @return
	 */
	private List<Rectangle> removeOverlappingRectangles(List<Rectangle> rectList) {
		HashSet<Rectangle> rectangleClearSet = new HashSet<Rectangle>();
		List<Rectangle> clearList = new ArrayList<Rectangle>();
		for (Rectangle innerRectangle : rectList) {
			boolean alone = true;
			for (Rectangle rectangle : rectList) {
				if (!innerRectangle.equals(rectangle)
						&& innerRectangle.intersects(rectangle)) {
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

	public List<Rectangle> detectEyes(BufferedImage image) {

		Detector detector;
		detector = new Detector(getClass().getClassLoader()
				.getResourceAsStream("haarcascade_eye.xml"));

		List<Rectangle> res = detector.getFaces(image, 1.2f, 1.1f, .05f, 2,
				true);

		log.debug(res.size() + " faces found!");
		return res;
	}
}
