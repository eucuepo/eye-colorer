package com.eyecolorer.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.log4j.Logger;

public class EyeColorer {

	private static Logger log = Logger.getLogger(EyeColorer.class.getName());

	public BufferedImage changeEyeColor(Color eyeColor,
			BufferedImage toChange) {
		double scaleFactor = ImageUtil.getScaleFactor(300, toChange);
		BufferedImage bi = ImageUtil.scaleImage(toChange, scaleFactor);

		List<Circulo> listaCirculos = CircleDetector.getEyeCandidates(bi);

		if (listaCirculos.size() > 0 && listaCirculos.get(0) != null) {

			Circulo pupil = new Circulo(listaCirculos.get(0).getX(),
					listaCirculos.get(0).getY() + 1, listaCirculos.get(0)
							.getRadio() / 3);
			log.debug("Adding pupil: " + pupil);
			listaCirculos.add(pupil);
		}
		listaCirculos = Circulo.getConcentricCircunferencia(listaCirculos);

		if (listaCirculos.size() < 2) {
			return null;
		}

		Circulo[] eye = Circulo.getClosestCenters(listaCirculos);

		log.debug("Number of circles after filters: " + listaCirculos.size());

		BufferedImage eyeImage;
		if (eye[0] == null || eye[1] == null) {
			return null;
		} else {
			log.debug("iris" + ": centro X: " + eye[1].getX() + " centroY: "
					+ eye[1].getY() + " radio: " + eye[1].getRadio());
			log.debug("pupila" + ": centro X: " + eye[0].getX() + " centroY: "
					+ eye[0].getY() + " radio: " + eye[0].getRadio());
			eyeImage = ImageUtil.createEyeMask(
					(int) (bi.getWidth() / scaleFactor),
					(int) (bi.getHeight() / scaleFactor),
					(int) (eye[0].getRadio() * 2 / scaleFactor),
					(int) (eye[1].getRadio() * 2 / scaleFactor),
					(int) ((eye[1].getX() - eye[1].getRadio()) / scaleFactor),
					(int) ((eye[1].getY() - eye[1].getRadio()) / scaleFactor),
					(int) ((eye[0].getX() - eye[0].getRadio()) / scaleFactor),
					(int) ((eye[0].getY() - eye[0].getRadio()) / scaleFactor),
					eyeColor);
		}
		// mix the original image and the masks
		BufferedImage masked = ImageUtil.combineImages(toChange, eyeImage);
		return masked;

	}
}
