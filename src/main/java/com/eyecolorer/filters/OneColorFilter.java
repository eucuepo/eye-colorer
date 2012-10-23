package com.eyecolorer.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.eyecolorer.image.ImageUtil;

public class OneColorFilter extends ColorFilter {

	private Color eyeColor;

	public OneColorFilter(Color eyeColor, BufferedImage image) {
		super(image);
		this.eyeColor = eyeColor;
	}

	@Override
	public BufferedImage createEyeMask() {
		return ImageUtil.createEyeMask((int) (scaledImage.getWidth() / scaleFactor), (int) (scaledImage.getHeight() / scaleFactor), (int) (eye[0].getRadius() * 2 / scaleFactor),
				(int) (eye[1].getRadius() * 2 / scaleFactor), (int) ((eye[1].getX() - eye[1].getRadius()) / scaleFactor), (int) ((eye[1].getY() - eye[1].getRadius()) / scaleFactor),
				(int) ((eye[0].getX() - eye[0].getRadius()) / scaleFactor), (int) ((eye[0].getY() - eye[0].getRadius()) / scaleFactor), eyeColor,scaleFactor);

	}

}
