/*
 * IrisFrame.java
 * Created on November 4, 2004, 1:23 PM
 */

package com.eyecolorer.gui;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import jip.JIPImage;
import jip.JIPToolkit;
import jipfunc.FBinarize;
import jipfunc.FCanny;
import jipfunc.FColorToGray;
import jipfunc.FHoughCirc;
import jipfunc.FSmoothMedian;
import jiputil.Circunferencia;

/**
 * @author cs02rm0
 */
public class IrisFrame extends javax.swing.JFrame {

	public IrisFrame() {
		initComponents();
	}

	private void initComponents() {// GEN-BEGIN:initComponents
		jPanel1 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		jButton1 = new javax.swing.JButton();
		jPanel4 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
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
		jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.X_AXIS));

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
		// jPanel4.add(jLabel1, java.awt.BorderLayout.CENTER);
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
	}// GEN-END:initComponents

	private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem1ActionPerformed
		exit();
	}// GEN-LAST:event_jMenuItem1ActionPerformed

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
		// File select
		// JFileChooser ch = new JFileChooser();
		FileDialog ch = new FileDialog(this, "Choose a file", FileDialog.LOAD);
		BufferedImage img;
		File f;
		// ch.setCurrentDirectory(new File("."));
		// ch.setDialogTitle("Pick an appropriate iris image");
		// ch.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// /new Thread() {
		// public void run() {
		ch.setVisible(true);

		String file = ch.getDirectory() + ch.getFile();
		System.out.println(file);
		f = new File(file);
		try {
			img = ImageIO.read(f);
			JIPImage jipImage = JIPToolkit.getColorImage(img);

			FSmoothMedian fsmoothMedian = new FSmoothMedian();
			fsmoothMedian.setParamValue("radius", 3);
			JIPImage imgFiltroMediania = fsmoothMedian.processImg(jipImage);

			FColorToGray fColorToGray = new FColorToGray();
			JIPImage imageGray = fColorToGray.processImg(imgFiltroMediania);

			FCanny fCanny = new FCanny();
			JIPImage imgCanny = fCanny.processImg(imageGray);

			ImageIcon iris = new ImageIcon(JIPToolkit.getAWTImage(imgCanny));
			// jLabel1.setIcon(iris);

			FBinarize fBinarize = new FBinarize();
			fBinarize.setParamValue("u1", 30);
			fBinarize.setParamValue("u2", 255);
			JIPImage imgBinarize = fBinarize.processImg(imgCanny);

			FHoughCirc fHougCir = new FHoughCirc();
			fHougCir.setParamValue("thres", 30);
			fHougCir.setParamValue("Rmin", 10);
			fHougCir.setParamValue("Rmax", 80);
			fHougCir.processImg(imgBinarize);

			Image iris2 = JIPToolkit.getAWTImage(imgBinarize);
			// dp.setBi(iris2);
			dp.setBi(img);

			System.out.println("Numero de circulos: " + fHougCir.getResultValueInt("ncirc"));
			Vector vecAux = (Vector) fHougCir.getResultValueObj("circum");
			// Graphics2D g2d = (Graphics2D) jLabel1.getGraphics();
			// g2d.drawImage(iris.getImage(), 0, 0, null);
			// repaint();
			dp.setIris((Circunferencia) vecAux.get(0));
			dp.setPupila((Circunferencia) vecAux.get(1));
			for (int i = 0; i < vecAux.size(); i++) {
				Circunferencia c = (Circunferencia) vecAux.get(i);
				System.out.println("circunferencia " + i + ": " + c.centroX + " : " + c.centroY + " : " + c.radio);
				// Assume x, y, and diameter are instance variables.
				Ellipse2D.Double circle = new Ellipse2D.Double(c.centroX, c.centroY, c.radio * 2, c.radio * 2);
				// g2d.setColor(Color.red);
				// g2d.drawOval(c.centroX, c.centroY, c.radio * 2, c.radio * 2);
				// repaint();
			}

			// Segment segment = new Segment();
			// segment.segment(img);

			/*
			 * System.out.println("Numero de circulos: " +
			 * fHougCir.getResultValueInt("ncirc")); Vector vecAux = (Vector)
			 * fHougCir.getResultValueObj("circum"); Graphics2D g2d =
			 * (Graphics2D) jLabel1.getGraphics();
			 * g2d.drawImage(iris.getImage(), 0, 0, null); repaint(); for (int i
			 * = 0; i < vecAux.size(); i++) { Circunferencia c =
			 * (Circunferencia) vecAux.get(i); // Assume x, y, and diameter are
			 * instance variables. Ellipse2D.Double circle = new
			 * Ellipse2D.Double(c.centroX, c.centroY, c.radio * 2, c.radio * 2);
			 * g2d.setColor(Color.red); g2d.drawOval(c.centroX, c.centroY,
			 * c.radio * 2, c.radio * 2); repaint(); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		// }
		// }.start();
	}// GEN-LAST:event_jButton1ActionPerformed

	private void exitForm(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_exitForm
		exit();
	}// GEN-LAST:event_exitForm

	private void exit() {
		System.exit(0);
	}

	public static void main(String args[]) {
		System.setProperty("java.util.prefs.syncInterval", "2000000");
		new IrisFrame().show();
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenuItem jMenuItem1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private DrawPanel dp;
	// End of variables declaration//GEN-END:variables

}
