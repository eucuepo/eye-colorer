package com.poc.image;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TestMask {

	private BufferedImage[] m_images;

	TestMask() throws IOException {
		m_images = new BufferedImage[3];
		m_images[0] = ImageIO.read(new File("E:/Documents/images/map.png"));
		m_images[1] = ImageIO.read(new File("E:/Documents/images/mapMask3.png"));
		Image transpImg = TransformGrayToTransparency(m_images[1]);
		m_images[2] = applyTranspacency(m_images[0], transpImg);
	}

	private Image TransformGrayToTransparency(BufferedImage image) {
		ImageFilter filter = new RGBImageFilter() {
			public final int filterRGB(int x, int y, int rgb) {
				return (rgb << 8) & 0xFF000000;
			}
		};

		ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	public static BufferedImage applyTranspacency(BufferedImage image, Image mask) {
		BufferedImage dest = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = dest.createGraphics();
		g2.drawImage(image, 0, 0, null);
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.DST_IN, 1.0F);
		g2.setComposite(ac);
		g2.drawImage(mask, 0, 0, null);
		g2.dispose();
		return dest;
	}

}
