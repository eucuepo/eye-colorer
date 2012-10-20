/*
 * IrisFrame.java
 * Created on November 4, 2004, 1:23 PM
 */

package com.eyecolorer.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.eyecolorer.image.EyeColorer;
import com.eyecolorer.image.EyeDetector;
import com.eyecolorer.image.ImageUtil;

public class EyeFrame extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2553391688838747458L;
	private static Logger log = Logger.getLogger(EyeFrame.class.getName());

	private javax.swing.JButton jButton1;
	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenuItem jMenuItem1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private DrawPanel dp;

	public EyeFrame() {
		initComponents();
	}

	private void initComponents() {// GEN-BEGIN:initComponents
		jPanel1 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		jButton1 = new javax.swing.JButton();
		jPanel4 = new javax.swing.JPanel();
		jMenuBar1 = new javax.swing.JMenuBar();
		jMenu1 = new javax.swing.JMenu();
		jMenuItem1 = new javax.swing.JMenuItem();
		dp = new DrawPanel();

		setTitle("Iris Concurso");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});

		getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
		jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2,
				javax.swing.BoxLayout.X_AXIS));

		jPanel2.setPreferredSize(new java.awt.Dimension(460, 300));
		jButton1.setText("Select image");
		jButton1.setActionCommand("jButton1");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jPanel3.add(jButton1);
		jPanel2.add(jPanel3);
		jPanel4.setLayout(new java.awt.BorderLayout());
		jPanel4.add(dp, java.awt.BorderLayout.CENTER);
		jPanel2.add(jPanel4);
		getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

		jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem1ActionPerformed(evt);
			}
		});

		jMenu1.add(jMenuItem1);
		jMenuBar1.add(jMenu1);
		setJMenuBar(jMenuBar1);

		pack();
	}

	private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
		exit();
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		FileDialog ch = new FileDialog(this, "Choose a file", FileDialog.LOAD);
		BufferedImage originalImage;
		File f;
		ch.setVisible(true);

		String file = ch.getDirectory() + ch.getFile();
		System.out.println(file);
		f = new File(file);
		try {
			originalImage = ImageIO.read(f);

			EyeDetector eyeDetector = new EyeDetector();
			originalImage = eyeDetector.getEyesChange(originalImage, new Color(
					255, 0, 0, 80));

			ImageViewer d = new ImageViewer(originalImage);
			d.setRects(eyeDetector.getEyesList());
			d.setScaleFactor(eyeDetector.getScaleFactor());
			setContentPane(d);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("serial")
	class ImageViewer extends JPanel {
		protected Image img;
		int img_width, img_height;
		List<Rectangle> res;
		private double scaleFactor;

		public ImageViewer(Image img) {
			super();
			this.img = img;
			img_width = img.getWidth(null);
			img_height = img.getHeight(null);
			res = null;
		}

		public void paint(Graphics g) {
			Graphics2D g1 = (Graphics2D) g;
			g1.setColor(Color.red);
			g1.setStroke(new BasicStroke(2f));
			if (img == null)
				return;
			Dimension dim = getSize();
			// System.out.println("véridique");
			g1.clearRect(0, 0, dim.width, dim.height);
			double scale_x = dim.width * 1.f / img_width;
			double scale_y = dim.height * 1.f / img_height;
			double scale = Math.min(scale_x, scale_y);
			int x_img = (dim.width - (int) (img_width * scale)) / 2;
			int y_img = (dim.height - (int) (img_height * scale)) / 2;
			g1.drawImage(img, x_img, y_img, (int) (img_width * scale),
					(int) (img_height * scale), null);
			if (res == null)
				return;

			for (Rectangle rect : res) {
				int w = (int) (rect.width * scale / scaleFactor);
				int h = (int) (rect.height * scale / scaleFactor);
				int x = (int) (rect.x * scale / scaleFactor) + x_img;
				int y = (int) (rect.y * scale / scaleFactor) + y_img;
				g1.drawRect(x, y, w, h);
			}

		}

		public void setRects(List<Rectangle> list) {
			this.res = list;
			repaint();
		}

		public void setScaleFactor(double scaleFactor) {
			this.scaleFactor = scaleFactor;
		}

	}

	private void exitForm(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_exitForm
		exit();
	}// GEN-LAST:event_exitForm

	private void exit() {
		System.exit(0);
	}

	public static void main(String args[]) {
		System.setProperty("java.util.prefs.syncInterval", "2000000");
		new EyeFrame().setVisible(true);
	}

}
