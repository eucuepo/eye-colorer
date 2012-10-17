package com.poc.servlet;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import jip.JIPImage;
import jip.JIPToolkit;
import jipfunc.FBinarize;
import jipfunc.FCanny;
import jipfunc.FColorToGray;
import jipfunc.FHoughCirc;
import jipfunc.FSmoothMedian;
import jiputil.Circunferencia;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.cs02rm0.jirrm.DrawPanel;

import com.poc.image.ImageUtil;

public class UploadImage extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8414206228109706371L;

	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		System.out.println("request: " + request);
		if (!isMultipart) {
			System.out.println("File Not Uploaded");
		} else {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = null;
			// try to get the form parameters, image and iris color
			try {
				items = (List<FileItem>) upload.parseRequest(request);
				System.out.println("items: " + items);
				if (items.size() < 1)
					throw new FileUploadException("File not found");
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
			// get the file
			FileItem file = (FileItem) items.get(0);
			// get the color
			FileItem color = (FileItem) items.get(1);
			// parse the color
			Color eyeColor = new Color(Integer.parseInt(color.getString(), 16));

			try {
				OutputStream outputStream = response.getOutputStream();
				// TODO: add support for several image formats
				response.setContentType("image/png");

				BufferedImage bi = ImageIO.read(file.getInputStream());
				/* Sorry for this */
				DrawPanel dp = new DrawPanel();
				JIPImage jipImage = JIPToolkit.getColorImage(bi);

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

				System.out.println("Numero de circulos: " + fHougCir.getResultValueInt("ncirc"));
				Vector vecAux = (Vector) fHougCir.getResultValueObj("circum");
				// Graphics2D g2d = (Graphics2D) jLabel1.getGraphics();
				// g2d.drawImage(iris.getImage(), 0, 0, null);
				// repaint();
				dp.setIris((Circunferencia) vecAux.get(0));
				dp.setPupila((Circunferencia) vecAux.get(1));
				Circunferencia circunferenciaIris = (Circunferencia) vecAux.get(0);
				Circunferencia circunferenciaPupila = (Circunferencia) vecAux.get(1);
				for (int i = 0; i < vecAux.size(); i++) {
					Circunferencia c = (Circunferencia) vecAux.get(i);
					System.out.println("circunferencia " + i + ": " + c.centroX + " : " + c.centroY + " : " + c.radio);
					// Assume x, y, and diameter are instance variables.
					Ellipse2D.Double circle = new Ellipse2D.Double(c.centroX, c.centroY, c.radio * 2, c.radio * 2);
				}

				/* end of crap */

				// TODO: change to detected eye position
				BufferedImage leftEye = ImageUtil.createEyeMask(bi.getWidth(), bi.getHeight(), circunferenciaIris.radio*2, circunferenciaPupila.radio*2, 138, 75, eyeColor);
				// BufferedImage leftEye =
				// ImageUtil.createEyeMask(bi.getWidth(), bi.getHeight(), 20,
				// 80, 50, 50, eyeColor);
				// BufferedImage rightEye =
				// ImageUtil.createEyeMask(bi.getWidth(), bi.getHeight(), 20,
				// 80, 200, 50, eyeColor);

				// mix the original image and the masks
				BufferedImage masked = ImageUtil.combineImages(bi, leftEye);
				// masked = ImageUtil.combineImages(masked, rightEye);
				// masked = ImageUtil.cropImage(masked, 50, 100, 200, 300);
				// alter the image
				ImageIO.write(masked, "png", outputStream);
				outputStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
