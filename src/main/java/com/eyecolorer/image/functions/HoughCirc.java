package com.eyecolorer.image.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import jip.JIPImage;
import jipfunc.FHoughCirc;
import jiputil.Circunferencia;

import com.eyecolorer.image.Circle;

/**
 * This is a convenient wrapper for JavaVis HoughCirc function
 * 
 * @author ecuevas
 * 
 */
public class HoughCirc extends FHoughCirc {

	private int threshold;
	private int rmin;
	private int rmax;
	List<Circle> listaCirculos;

	public HoughCirc() {
		// default values
		this.threshold = 30;
		this.rmin = 20;
		this.rmax = 70;
		super.setParamValue("thres", threshold);
		super.setParamValue("Rmin", rmin);
		super.setParamValue("Rmax", rmax);
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
		super.setParamValue("thres", threshold);
	}

	public int getRmin() {
		return rmin;
	}

	public void setRmin(int rmin) {
		this.rmin = rmin;
		super.setParamValue("Rmin", rmin);
	}

	public int getRmax() {
		return rmax;
	}

	public void setRmax(int rmax) {
		this.rmax = rmax;
		super.setParamValue("Rmax", rmax);
	}

	// get generated circle list
	@SuppressWarnings("unchecked")
	public List<Circle> getCircles(int cropping) {
		Vector<Circunferencia> vecAux = (Vector<Circunferencia>) super.getResultValueObj("circum");
		List<Circle> circleList = new ArrayList<Circle>();
		for (Circunferencia circunferencia : vecAux) {
			Circle c = new Circle(circunferencia.centroX + cropping, circunferencia.centroY + cropping, circunferencia.radio);
			circleList.add(c);
		}
		return circleList;
	}

	public List<Circle> getCircles() {
		return getCircles(0);
	}

	@Override
	public JIPImage processImg(JIPImage img) {
		return super.processImg(img);
	}
}
