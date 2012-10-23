package com.eyecolorer.servlet;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

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
 * Servlet to change eye color of images and return the path to the generated
 * image
 * 
 * @author ecuevas
 * 
 */
@MultipartConfig
public class UploadImage extends HttpServlet {

	private static final long serialVersionUID = -8414206228109706371L;
	private static Logger log = Logger.getLogger(UploadImage.class.getName());

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// get the parameters from the request
		String firstColorValue = request.getParameter("firstColor"); // Retrieves
		// <input
		// type="text"
		// name="color">

		String secondColorValue = request.getParameter("secondColor"); // Retrieves
																		// <input
		// type="text"
		// name="color">
		Part filePart = request.getPart("image"); // Retrieves <input
													// type="file"
													// name="file">
		String multiface = request.getParameter("multiface");
		String enableSecondColor = request.getParameter("secondColorEnabled");
		String hipsterize = request.getParameter("hipsterize");

		InputStream file = filePart.getInputStream();

		Color firstColor = new Color(Integer.parseInt(firstColorValue, 16));
		Color secondColor = null;

		secondColor = new Color(Integer.parseInt(secondColorValue, 16));

		try {
			response.setContentType("text/html");

			BufferedImage bi = ImageIO.read(file);
			EyeDetector eyeDetector = new EyeDetector();
			eyeDetector.setFirstColor(firstColor);
			if (enableSecondColor.equals("true")) {
				eyeDetector.setSecondColor(secondColor);
			}
			if (hipsterize.equals("true")){
				eyeDetector.setHipsterize(true);
			}
			if (multiface != null && multiface.equals("true")) {
				bi = eyeDetector.getEyesChangeMultiFace(bi);
			} else {
				bi = eyeDetector.getEyesChange(bi);
			}
			// resize to width 800
			BufferedImage toSave = ImageUtil.scaleImage(bi,
					ImageUtil.getScaleFactor(800, bi));

			// Append the date to the image file to avoid conflicts
			Date date = new Date();
			// convert to JPG
			toSave = ImageUtil.convertToJpg(toSave);
			OutputStream out = new FileOutputStream("/tmp/image"
					+ date.getTime() + ".jpg");
			// return to webpage
			ImageIO.write(toSave, "JPG", out);

			PrintWriter writer = response.getWriter();
			writer.println("getImage/image" + date.getTime() + ".jpg");

			out.close();

		} catch (Exception e) {
			log.debug("Error during eye coloring" + e.getMessage());
			e.printStackTrace();
		}
	}
	// }

}
