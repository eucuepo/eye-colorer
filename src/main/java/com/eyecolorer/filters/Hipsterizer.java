package com.eyecolorer.filters;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import com.eyecolorer.image.ImageUtil;

public class Hipsterizer {

	public BufferedImage hipsterMe(BufferedImage image, int x, int y, double scaleFactor, double eyeScaleFactor, double eyeRadius, Rectangle eyeRectangle, List<Point2D> eyePoints) {
		BufferedImage hipsterized = null;
		// get the glasses
		try {
			BufferedImage glasses = ImageIO.read(getClass().getClassLoader().getResourceAsStream("hipster.png"));
			double distanceBetweenEyes = eyePoints.get(0).distance(eyePoints.get(1));
			System.out.println("distance between eyes: " + distanceBetweenEyes);
			int realEyeRadius = (int) ((eyeRadius / (scaleFactor)));
			double glassesScaleFactor = realEyeRadius / 55d;

			int xGlasses = (int) (((x / eyeScaleFactor) + (eyeRectangle.x / scaleFactor)) - (realEyeRadius * 2.8d));
			int yGlasses = (int) (((y / eyeScaleFactor) + (eyeRectangle.y / scaleFactor)) - (realEyeRadius * 2d));

			// 639 * 238
			// put them in the image
			glasses = ImageUtil.scaleImage(glasses, glassesScaleFactor);
			hipsterized = ImageUtil.combineImages(image, glasses, xGlasses, yGlasses);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return hipsterized;
	}
}
