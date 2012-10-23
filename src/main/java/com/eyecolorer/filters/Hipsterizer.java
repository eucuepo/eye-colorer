package com.eyecolorer.filters;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.eyecolorer.image.ImageUtil;

public class Hipsterizer {

	public BufferedImage hipsterMe(BufferedImage image, int x, int y,
			int height, int width, double scaleFactor, double eyeScaleFactor, double eyeRadius,Rectangle eyeRectangle) {
		BufferedImage hipsterized = null;
		// get the glasses
		try {
			BufferedImage glasses = ImageIO.read(getClass().getClassLoader()
					.getResourceAsStream("hipster.png"));
			int realEyeRadius = (int)((eyeRadius/(scaleFactor)));
			double glassesScaleFactor = realEyeRadius/55d;
			
			int xGlasses = (int)(((x/eyeScaleFactor)+(eyeRectangle.x/scaleFactor)) - (realEyeRadius * 2.8d)) ;
			int yGlasses = (int)(((y/eyeScaleFactor)+(eyeRectangle.y/scaleFactor))  - (realEyeRadius * 2d));
			System.out.println("x glasses: " + xGlasses);
			System.out.println("y glasses: " + yGlasses);
			System.out.println("height glasses: " + 238);
			System.out.println("width glasses: " + 639);
			System.out.println("scaleFactor: " + glassesScaleFactor);
			System.out.println("eye radius: " + realEyeRadius);
			// 639 * 238
			// put them in the image
			glasses = ImageUtil.scaleImage(glasses, glassesScaleFactor);
			hipsterized = ImageUtil.combineImages(image, glasses, xGlasses,
					yGlasses);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return hipsterized;
	}
}
