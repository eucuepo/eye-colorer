package com.poc.servlet;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

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

				// TODO: change to detected positions
				BufferedImage leftEye = ImageUtil.createEyeMask(bi.getWidth(), bi.getHeight(), 20, 80, 50, 50, eyeColor);
				BufferedImage rightEye = ImageUtil.createEyeMask(bi.getWidth(), bi.getHeight(), 20, 80, 200, 50, eyeColor);

				// mix the original image and the masks
				BufferedImage masked = ImageUtil.combineImages(bi, leftEye);
				masked = ImageUtil.combineImages(masked, rightEye);
				masked = ImageUtil.cropImage(masked, 50, 100, 200, 300);
				// alter the image
				ImageIO.write(masked, "png", outputStream);
				outputStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
