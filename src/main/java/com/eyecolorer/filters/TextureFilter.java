package com.eyecolorer.filters;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.eyecolorer.image.ImageUtil;

/*
 * paints a texture in the eye
 */
public class TextureFilter extends ColorFilter {

	public String textureString;

	public TextureFilter(BufferedImage image, String texture) {
		super(image);
		this.textureString = texture;
	}

	@Override
	public BufferedImage createEyeMask() {
		// load texture
		BufferedImage texture = null;
		try {
			texture = ImageIO.read(getClass().getClassLoader().getResourceAsStream(textureString+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ImageUtil.createEyeTextureMask(
				(int) (scaledImage.getWidth() / scaleFactor),
				(int) (scaledImage.getHeight() / scaleFactor),
				(int) (eye[0].getRadius() * 2 / scaleFactor),
				(int) (eye[1].getRadius() * 2 / scaleFactor),
				(int) ((eye[1].getX() - eye[1].getRadius()) / scaleFactor),
				(int) ((eye[1].getY() - eye[1].getRadius()) / scaleFactor),
				(int) ((eye[0].getX() - eye[0].getRadius()) / scaleFactor),
				(int) ((eye[0].getY() - eye[0].getRadius()) / scaleFactor),
				scaleFactor, texture);

	}

}
