/*
 * ViewImage.java
 * Created on November 1, 2004, 11:01 AM
 */

package org.cs02rm0.jirrm.segmentation;
import java.awt.image.*;
import java.awt.*;
import javax.imageio.*;
import javax.swing.*;
import java.io.*;
/**
 * @author  cs02rm0
 */
public class ViewImage {
    
    public ViewImage(BufferedImage img) {
	    JFrame f = new JFrame();
	    JLabel l = new JLabel();
	    l.setIcon(new ImageIcon(img));
	    f.getContentPane().add(l);
	    f.pack();
	    f.show();
    }
}
