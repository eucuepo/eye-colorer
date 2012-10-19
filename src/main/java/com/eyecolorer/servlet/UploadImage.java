package com.eyecolorer.servlet;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.swing.ImageIcon;

import jip.JIPImage;
import jip.JIPToolkit;
import jipfunc.FBinarize;
import jipfunc.FCanny;
import jipfunc.FColorToGray;
import jipfunc.FHoughCirc;
import jiputil.Circunferencia;

import com.eyecolorer.image.Circulo;
import com.eyecolorer.image.ImageUtil;
import com.eyecolorer.image.functions.SmoothMedian;

@MultipartConfig
public class UploadImage extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8414206228109706371L;

	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("request: " + request);

		String color = request.getParameter("color"); // Retrieves <input
														// type="text"
														// name="description">
		Part filePart = request.getPart("image"); // Retrieves <input type="file"
													// name="file">
		InputStream file = filePart.getInputStream();


		Color eyeColor = new Color(Integer.parseInt(color, 16));

		try {
			OutputStream outputStream = response.getOutputStream();
			// TODO: add support for several image formats
			response.setContentType("image/png");

			BufferedImage bi = ImageIO.read(file);
			/* Sorry for this */
			//DrawPanel dp = new DrawPanel();
			JIPImage jipImage = JIPToolkit.getColorImage(bi);

			SmoothMedian smoothMedian = new SmoothMedian();
			smoothMedian.setRadius(3);
			JIPImage imgFiltroMediania = smoothMedian.processImg(jipImage);

			FColorToGray fColorToGray = new FColorToGray();
			JIPImage imageGray = fColorToGray.processImg(imgFiltroMediania);

			//TODO: Wrapper de canny
			FCanny fCanny = new FCanny();
			fCanny.setParamValue("sigma",1.0f);
			fCanny.setParamValue("brightness",100);
			JIPImage imgCanny = fCanny.processImg(imageGray);

			ImageIcon iris = new ImageIcon(JIPToolkit.getAWTImage(imgCanny));
			// jLabel1.setIcon(iris);
			
			//TODO: Wrapper de binarize
			FBinarize fBinarize = new FBinarize();
			fBinarize.setParamValue("u1", 25);
			fBinarize.setParamValue("u2", 255);
			JIPImage imgBinarize = fBinarize.processImg(imgCanny);

			// TODO: Adjust the values with image dimensions
			FHoughCirc fHougCir = new FHoughCirc();
			fHougCir.setParamValue("thres", 30);
			fHougCir.setParamValue("Rmin", 10);
			fHougCir.setParamValue("Rmax", 80);
			fHougCir.processImg(imgBinarize);

			Image iris2 = JIPToolkit.getAWTImage(imgBinarize);

			System.out.println("Numero de circulos antes de filtro: " + fHougCir.getResultValueInt("ncirc"));
			Vector<Circunferencia> vecAux = (Vector<Circunferencia>) fHougCir.getResultValueObj("circum");
			List<Circunferencia> listaCirculos = new ArrayList<Circunferencia>();
			listaCirculos.addAll(vecAux);
			listaCirculos = Circulo.getConcentricCircunferencia(listaCirculos);

			// Graphics2D g2d = (Graphics2D) jLabel1.getGraphics();
			// g2d.drawImage(iris.getImage(), 0, 0, null);
			// repaint();
			//dp.setIris(vecAux.get(0));
			//dp.setPupila(vecAux.get(1));
			
			
			Circunferencia circunferenciaIris = (Circunferencia) listaCirculos.get(0);
			Circunferencia circunferenciaPupila = (Circunferencia) listaCirculos.get(1);
			//buscar los mas cercanos
			Circulo.getClosestCenters(listaCirculos,circunferenciaIris,circunferenciaPupila);			
			
		
			for (int i = 0; i < vecAux.size(); i++) {
				Circunferencia c = (Circunferencia) vecAux.get(i);
				System.out.println("circunferencia " + i + ": centro X: " + c.centroX + " centroY: " + c.centroY + " radio: " + c.radio);
			}

			System.out.println("Numero de circulos despues de filtro: " + listaCirculos.size());
			int i = 0;
			for (Circunferencia circunferencia : listaCirculos) {
				i++;
				System.out.println("circunferencia " + i + ": centro X: " + circunferencia.centroX + " centroY: " + circunferencia.centroY + " radio: " + circunferencia.radio);
			}

			/* end of crap */

			// TODO: change to detected eye position
			BufferedImage leftEye = ImageUtil.createEyeMask(bi.getWidth(), bi.getHeight(), circunferenciaIris.radio * 2, circunferenciaPupila.radio * 2, 138, 75, eyeColor);
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
	// }
}
