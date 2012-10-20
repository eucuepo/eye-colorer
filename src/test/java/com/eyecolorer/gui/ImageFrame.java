package com.eyecolorer.gui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImageFrame extends javax.swing.JFrame {

	JPanel jPanel1;
	JLabel jLabel1;
	BufferedImage img;

	public ImageFrame(BufferedImage img) {
		this.img = img;
		initComponents();
	}

	private void initComponents() {// GEN-BEGIN:initComponents
		getContentPane().setPreferredSize(new Dimension(400,400));
		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel1.setIcon(new ImageIcon(img));
		jPanel1.setPreferredSize(new java.awt.Dimension(img.getWidth(), img.getWidth()));
		jPanel1.add(jLabel1);
		getContentPane().add(jPanel1);
		
	}
}
