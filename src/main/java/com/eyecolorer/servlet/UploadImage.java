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

import com.eyecolorer.image.EyeColorer;

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
			OutputStream outputStream = response.getOutputStream();
			// TODO: add support for several image formats
			response.setContentType("image/jpg");

			BufferedImage bi = ImageIO.read(file);
			BufferedImage masked = EyeColorer.changeEyeColor(eyeColor, bi);
			ImageIO.write(masked, "jpg", outputStream);
			outputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// }

}
