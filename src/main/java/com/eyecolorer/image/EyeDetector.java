package com.eyecolorer.image;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import jviolajones.Detector;

public class EyeDetector {

	public static List<Rectangle> detectEyes(BufferedImage image) {

		String XMLFile = "C:/workspace_juno/JViola/haarcascade_eye.xml";
		List<Rectangle> res = null;
		Detector detector;
		try {
			detector = new Detector(XMLFile);

			res = detector.getFaces(image, 1.2f,1.1f,.05f, 2,
					true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(res.size() + " faces found!");
		return res;
	}
}
