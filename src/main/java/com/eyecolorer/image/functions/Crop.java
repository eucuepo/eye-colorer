package com.eyecolorer.image.functions;

import jip.JIPImage;
import jipfunc.FCrop;

/**
 * This is a convenient wrapper forJavaVis Crop function
 * 
 * @author ecuevas
 * 
 */
public class Crop extends FCrop {

	private int x;
	private int y;
	private int w;
	private int h;

	public Crop() {

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		super.setParamValue("x", x);
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		super.setParamValue("y", y);
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
		super.setParamValue("w", w);
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
		super.setParamValue("h", h);
	}

	public JIPImage processImg(JIPImage img) {
		return super.processImg(img);
	}

}
