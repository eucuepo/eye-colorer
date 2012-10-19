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
			BufferedImage masked = ImageUtil.changeEyeColor(eyeColor, bi);
			ImageIO.write(masked, "png", outputStream);
			outputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// }

	
}
