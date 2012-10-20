/*
 * IrisFrame.java
 * Created on November 4, 2004, 1:23 PM
 */

package com.eyecolorer.gui;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.eyecolorer.image.EyeDetector;
import com.eyecolorer.image.ImageUtil;

/**
 * @author cs02rm0
 */
public class EyeFrame extends javax.swing.JFrame {

	public EyeFrame() {
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
			List<Rectangle> eyesList = EyeDetector.detectEyes(f);
			img = ImageIO.read(f);
			for (Rectangle rectangle : eyesList) {
				System.out.println("Eye detected: height:" + rectangle.height
						+ "width:" + rectangle.width + " x:" + rectangle.x
						+ " y:" + rectangle.y);
			}

			List<BufferedImage> extractEyes = new ArrayList<BufferedImage>();
			for (Rectangle rectangle : eyesList) {
				//extract all the eyes
				BufferedImage extracted = ImageUtil.cropImage(img, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
				extractEyes.add(extracted);
			}

			// for each image, paint eyes if possible
			for (BufferedImage bufferedImage : extractEyes) {
				bufferedImage = ImageUtil.changeEyeColor(new Color(255, 0, 0), bufferedImage);
				//tengo el ojo pintado, combinar con la original
				//ImageUtil.combineImages(image, overlay)
				new ImageFrame(bufferedImage).setVisible(true);
				File outputFile = new File("C:/result"+bufferedImage.getHeight()+".jpg");
				ImageIO.write(bufferedImage, "JPG", outputFile);
			}
			dp.setBi(img);

			// fill image
			// Save
			//File outputFile = new File("C:/result.png");
			// ImageIO.write(img, "PNG", outputFile);

			// System.out.println("Numero de circulos: " +
			// fHougCir.getResultValueInt("ncirc"));
			// Vector vecAux = (Vector) fHougCir.getResultValueObj("circum");
			// Graphics2D g2d = (Graphics2D) jLabel1.getGraphics();
			// g2d.drawImage(iris.getImage(), 0, 0, null);
			// repaint();
			// dp.setIris((Circunferencia) vecAux.get(0));
			// dp.setPupila((Circunferencia) vecAux.get(1));
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
		new EyeFrame().show();
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
