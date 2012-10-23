package com.eyecolorer.image.functions;

import jip.JIPImage;
import jipfunc.FCanny;

/**
 * This is a convenient wrapper for JavaVis Canny function
 * 
 * @author ecuevas
 * 
 */
public class Canny extends FCanny {

	private float sigma;
	private int brightness;

	public Canny() {
		// default values
		this.sigma = 1.0f;
		this.brightness = 100;
		super.setParamValue("sigma", sigma);
		super.setParamValue("brightness", brightness);
	}

	public float getSigma() {
		return sigma;
	}

	public void setSigma(float sigma) {
		this.sigma = sigma;
		super.setParamValue("sigma", sigma);
	}

	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
		super.setParamValue("brightness", brightness);
	}

	public JIPImage processImg(JIPImage img) {
		return super.processImg(img);
	}
}
