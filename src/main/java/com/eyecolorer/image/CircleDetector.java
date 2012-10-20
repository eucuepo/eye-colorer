package com.eyecolorer.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import jip.JIPImage;
import jip.JIPToolkit;
import jipfunc.FBinarize;
import jipfunc.FCanny;
import jipfunc.FCrop;
import jipfunc.FGrayToGray;
import jipfunc.FHoughCirc;
import jiputil.Circunferencia;

import com.eyecolorer.image.functions.SmoothMedian;

public class CircleDetector {

	// constants for the detection
	private static final int SMOOTH_RADIUS = 2;
	private static final float CANNY_SIGMA = 1.0f;
	private static final int CANNY_BRIGHTNESS = 100;
	private static final int BINARIZE_LOW_THRESHOLD = 25;
	private static final int BINARIZE_HIGH_THRESHOLD = 255;
	
	public static List<Circulo> getEyeCandidates(BufferedImage bi) {
		JIPImage jipImage = JIPToolkit.getColorImage(bi);

		SmoothMedian smoothMedian = new SmoothMedian();
		smoothMedian.setRadius(SMOOTH_RADIUS);
		JIPImage imgFiltroMediania = smoothMedian.processImg(jipImage);

		// TODO: Wrapper de canny
		FCanny fCanny = new FCanny();
		fCanny.setParamValue("sigma", CANNY_SIGMA);
		fCanny.setParamValue("brightness", CANNY_BRIGHTNESS);
		JIPImage imgCanny = fCanny.processImg(imgFiltroMediania);

		FGrayToGray fGrayToGray = new FGrayToGray();
		JIPImage imageGray = fGrayToGray.processImg(imgCanny);

		// TODO: Wrapper the binarize
		FBinarize fBinarize = new FBinarize();
		fBinarize.setParamValue("u1", BINARIZE_LOW_THRESHOLD);
		fBinarize.setParamValue("u2", BINARIZE_HIGH_THRESHOLD);
		JIPImage imgBinarize = fBinarize.processImg(imageGray);
		List<Circulo> listaCirculos = performPass(imgBinarize, 0, 30, 20, 70);
		System.out.println("Numero de circulos antes de filtros: "
				+ listaCirculos.size());
		for (int i = 0; i < listaCirculos.size(); i++) {
			Circulo c =  listaCirculos.get(i);
			System.out.println("circunferencia " + i + ": centro X: "
					+ c.getCentroX() + " centroY: " + c.getCentroY() + " radio: "
					+ c.getRadio());
		}
		if (listaCirculos.size() == 0) {
			// no circles found, let's do another pass with more relaxed args
			listaCirculos = performPass(imgBinarize, 50, 28, 20, 60);

		}
		
		return listaCirculos;
	}

	@SuppressWarnings("unchecked")
	private static List<Circulo> performPass(JIPImage imgBinarize,
			int cropping, int houghThreshold, int houghMinRadius,
			int houghMaxRadius) {
		Vector<Circunferencia> vecAux;
		// crop the image to the center
		FCrop fCrop = new FCrop();
		fCrop.setParamValue("x", cropping);
		fCrop.setParamValue("y", cropping);
		fCrop.setParamValue("w", imgBinarize.getWidth() - cropping * 2);
		fCrop.setParamValue("h", imgBinarize.getHeight() - cropping * 2);
		imgBinarize = fCrop.processImg(imgBinarize);
		// try a second pass with lower threshold
		
		FHoughCirc fHoughCirc = new FHoughCirc();
		fHoughCirc.setParamValue("thres", houghThreshold);
		fHoughCirc.setParamValue("Rmin", houghMinRadius);
		fHoughCirc.setParamValue("Rmax", houghMaxRadius);
		fHoughCirc.processImg(imgBinarize);
		vecAux = (Vector<Circunferencia>) fHoughCirc
				.getResultValueObj("circum");
		List<Circulo> listaCirculos = new ArrayList<Circulo>();
		for (Circunferencia circunferencia : vecAux) {
			Circulo c = new Circulo(circunferencia.centroX+cropping,
					circunferencia.centroY+cropping, circunferencia.radio);
			listaCirculos.add(c);
		}
		return listaCirculos;
	}

	@SuppressWarnings("unused")
	private static BufferedImage calculateImageBuffer(JIPImage imgBinarize,
			double scaleFactor) {
		// no eyes found, return image
		Image binary = JIPToolkit.getAWTImage(imgBinarize);
		BufferedImage imageBuff = new BufferedImage(
				(int) (binary.getWidth(null)), (int) (binary.getHeight(null)),
				BufferedImage.TYPE_INT_RGB);

		Graphics g = imageBuff.createGraphics();
		g.drawImage(binary, 0, 0, new Color(0, 0, 0), null);
		g.dispose();
		imageBuff = ImageUtil.scaleImage(imageBuff, 1 / scaleFactor);
		return imageBuff;
	}

	
}