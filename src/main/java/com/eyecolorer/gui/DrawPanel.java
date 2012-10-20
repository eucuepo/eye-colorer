package com.eyecolorer.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

import jiputil.Circunferencia;

public class DrawPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image bi;
	private Circunferencia iris;
	private Circunferencia pupila;

	public Image getBi() {
		return bi;
	}

	public void setBi(Image bi) {
		this.bi = bi;
	}

	public Circunferencia getIris() {
		return iris;
	}

	public void setIris(Circunferencia iris) {
		this.iris = iris;
	}

	public Circunferencia getPupila() {
		return pupila;
	}

	public void setPupila(Circunferencia pupila) {
		this.pupila = pupila;
	}

	@SuppressWarnings("unused")
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();

		// Clear background to white
		g2.setColor(Color.WHITE);
		g2.clearRect(0, 0, 1024, 800);

		// Draw square
		// g2.setColor(Color.BLACK);
		// g2.drawRect(50, 50, 100, 100);

		// Draw image inside square
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		if (bi != null) {
			g2.drawImage(bi, 0, 0, bi.getWidth(null), bi.getHeight(null), null);
		}
		// dibujamos iris
		if (iris != null) {
			Ellipse2D.Double circle = new Ellipse2D.Double(iris.centroX, iris.centroY, iris.radio * 2, iris.radio * 2);
			g2.setColor(Color.red);
			g2.drawOval(iris.centroX - iris.radio, iris.centroY - iris.radio, iris.radio * 2, iris.radio * 2);
		}

		// dibujamos pupila
		if (pupila != null) {
			Ellipse2D.Double circle = new Ellipse2D.Double(pupila.centroX, pupila.centroY, pupila.radio * 2, pupila.radio * 2);
			g2.setColor(Color.green);
			g2.drawOval(pupila.centroX - pupila.radio, pupila.centroY - pupila.radio, pupila.radio * 2, pupila.radio * 2);
		}

		g2.dispose();
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400, 400);
	}

}
