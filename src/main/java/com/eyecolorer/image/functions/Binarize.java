package com.eyecolorer.image.functions;

import jip.JIPImage;
import jipfunc.FBinarize;

/**
 * This is a convenient wrapper for JavaVis Binarize function
 * 
 * @author ecuevas
 * 
 */
public class Binarize extends FBinarize {

	private int u1;
	private int u2;

	public Binarize() {
		// default values
		this.u1 = 25;
		this.u2 = 255;
		super.setParamValue("u1", u1);
		super.setParamValue("u2", u2);
	}

	public int getU1() {
		return u1;
	}

	public void setU1(int u1) {
		this.u1 = u1;
		super.setParamValue("u1", u1);
	}

	public int getU2() {
		return u2;
	}

	public void setU2(int u2) {
		this.u2 = u2;
		super.setParamValue("u2", u2);
	}

	@Override
	public JIPImage processImg(JIPImage img) {
		return super.processImg(img);
	}
}
