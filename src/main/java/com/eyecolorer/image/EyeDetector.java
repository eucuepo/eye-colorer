package com.eyecolorer.image;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.List;

import jviolajones.Detector;

public class EyeDetector {

	public static List<Rectangle> detectEyes(File image) {

		String XMLFile = "C:/workspace_juno/JViola/haarcascade_eye.xml";
		List<Rectangle> res = null;
		Detector detector;
		try {
			detector = new Detector(XMLFile);

			res = detector.getFaces(image.getAbsolutePath(), 1, 1.25f, 0.1f, 1,
					true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(res.size() + " faces found!");
		return res;
	}
}
