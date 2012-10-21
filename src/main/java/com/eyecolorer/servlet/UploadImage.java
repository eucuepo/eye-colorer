package com.eyecolorer.servlet;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.eyecolorer.image.EyeDetector;
import com.eyecolorer.image.ImageUtil;

@MultipartConfig
public class UploadImage extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8414206228109706371L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("request: " + request);

		String color = request.getParameter("color"); // Retrieves <input
														// type="text"
														// name="description">
		Part filePart = request.getPart("image"); // Retrieves <input
													// type="file"
													// name="file">
		InputStream file = filePart.getInputStream();

		Color eyeColor = new Color(Integer.parseInt(color, 16));

		try {
			// OutputStream outputStream = response.getOutputStream();

			response.setContentType("text/html");

			BufferedImage bi = ImageIO.read(file);
			EyeDetector eyeDetector = new EyeDetector();
			bi = eyeDetector.getEyesChange(bi, eyeColor);

			// resize to width 800
			BufferedImage toSave = ImageUtil.scaleImage(bi,
					ImageUtil.getScaleFactor(800, bi));

			// convert to JPG
			toSave = ImageUtil.convertToJpg(toSave);
			OutputStream out = new FileOutputStream("/tmp/image.jpg");
			// return to webpage
			// ImageIO.write(toSave, "JPG", outputStream);
			ImageIO.write(toSave, "JPG", out);

			PrintWriter writer = response.getWriter();
			writer.println("getImage/image.jpg");

			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// }

}
