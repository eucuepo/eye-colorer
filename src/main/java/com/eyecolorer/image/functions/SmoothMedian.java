package com.eyecolorer.image.functions;

import jip.JIPImage;
import jipfunc.FSmoothMedian;

/**
 * This is a wrapper for FSmoothMedian methods of JavaVIS library
 * 
 */
public class SmoothMedian extends FSmoothMedian {

	private int radius;

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
		super.setParamValue("radius", 2);
	}

	public SmoothMedian() {
		// default values
		this.radius = 2;
		super.setParamValue("radius", 2);
	}

	public SmoothMedian(int radius) {
		this.radius = radius;
	}

	@Override
	public JIPImage processImg(JIPImage img) {
		return super.processImg(img);
	}

}
