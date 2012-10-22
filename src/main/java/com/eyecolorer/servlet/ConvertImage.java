package com.eyecolorer.servlet;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import com.eyecolorer.image.EyeDetector;
import com.eyecolorer.image.ImageUtil;

/**
 * Webservice to change eye color of images and return the generated image to the caller
 * 
 * @author ecuevas
 * 
 */
@MultipartConfig
public class ConvertImage extends HttpServlet {

	private static final long serialVersionUID = -8414206228109706371L;
	private static Logger log = Logger.getLogger(ConvertImage.class.getName());

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// get the parameters from the request
		String color = request.getParameter("color"); // Retrieves <input
														// type="text"
														// name="color">
		Part filePart = request.getPart("image"); // Retrieves <input
													// type="file"
													// name="file">
		Integer width = Integer.valueOf(request.getParameter("width")); // Retrieves
																		// <input

		InputStream file = filePart.getInputStream();

		Color eyeColor = new Color(Integer.parseInt(color, 16));

		try {
			OutputStream outputStream = response.getOutputStream();

			response.setContentType("image/jpg");

			BufferedImage bi = ImageIO.read(file);
			EyeDetector eyeDetector = new EyeDetector();
			bi = eyeDetector.getEyesChange(bi, eyeColor);

			// resize to received width
			BufferedImage toSave = ImageUtil.scaleImage(bi, ImageUtil.getScaleFactor(width, bi));

			// convert to JPG
			toSave = ImageUtil.convertToJpg(toSave);
			// return to caller
			ImageIO.write(toSave, "JPG", outputStream);

			outputStream.close();

		} catch (Exception e) {
			log.debug("Error during eye coloring");
		}
	}

}
