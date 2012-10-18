package com.eyecolorer.image.toolkit;
import java.util.Vector;

/**<P><FONT COLOR="red">
*<B>Description:</B><BR>
*<FONT COLOR="blue">
*      Class that defines image object. An image is a set bands, each one is a bitmap.
* The width, height and type of each band are the same to everybads, and they are the
* image width, height and type. To access in a band is used a integer number between 0 and numbandas-1.
* An image can be any one posible bitmap posible and the same value rank:
* tBIT -> integer [0..1], tBYTE -> integer [0.255], tWORD -> integer [0..65535]
* and tREAL -> float [0..1]. Moreover, an image can be tCOLOR type. This type is a
* 3 bands image which bitmaps are tBYTE type. tCOLOR bands are the RGB intensity and
* we can access to them with the define constants in JIP class: bRED, bGREEN and bBLUE.
* Access to tCOLOR image pixels can do with RGB intensity values that they are packed in a 
* integer or they are packed separate intesity Red, Green or Blue. Like this, we can
* work with color images without band concept. 
* If we want to work with grey images with a band only, we can use constructors and methods
* to access to the pixels, then, we have not specify the band. Finally, we can asign a name
* that is "[Unnamed]" default. 
*/
public class JIPImage {

	/** Number of bands in the image */
	int nbands;

	/**
	 * Width of the image
	 * 
	 * @uml.property name="width"
	 */
	int width;

	/**
	 * Height of the image
	 * 
	 * @uml.property name="height"
	 */
	int height;

	/**
	 * Type of the image
	 * 
	 * @uml.property name="type"
	 */
	int type;

	/**
	 * Name of the image
	 * 
	 * @uml.property name="name"
	 */
	String name = "[Unnamed]";


	/**
	 * Array of bitmaps which has the values of the image
	 * 
	 * @uml.property name="bmp"
	 * @uml.associationEnd 
	 * @uml.property name="bmp" multiplicity="(0 -1)"
	 */
	JIPBitmap[] bmp;


	/******************/
	/*  Constructores */
	/******************/

	/**<P><FONT COLOR="red">
		*<B>description:</B><BR>
		*<FONT COLOR="blue">
		* Empty image constructor.		 
		*/
	JIPImage() {
	}

	/**<P><FONT COLOR="red">
			*<B>description:</B><BR>
			*<FONT COLOR="blue">
	* Image constructor, image is a new image copy.	
	* @param img Reference image to create the new copy.
	*/
	public JIPImage(JIPImage img) {
		if (img != null) {
			nbands = img.nbands;
			width = img.width;
			height = img.height;
			type = img.type;
			name = img.name;
			bmp = new JIPBitmap[nbands];
			for (int i = 0; i < nbands; i++)
				bmp[i] = new JIPBitmap(img.bmp[i]);
		}
	}

	/**<P><FONT COLOR="red">
			*<B>description:</B><BR>
			*<FONT COLOR="blue">
	* Constructor of an empty image (without pixels values).	
	* @param w Width of the image, columns (w>0)
	* @param h Height of the image, rows   (h>0)
	* @param t Type of the image. If type is tCOLOR, it will have 3 RGB internal bands.
	* Else it will have only one.
	*/
	public JIPImage(int w, int h, int t) {
		if (w > 0 && h > 0) {
			nbands = (t == JIP.tCOLOR) ? 3 : 1;
			bmp = new JIPBitmap[nbands];
			width = w;
			height = h;
			type = t;
			if (t == JIP.tCOLOR) {
				bmp[JIP.bRED] = new JIPBitmap(w, h, JIP.tBYTE);
				bmp[JIP.bGREEN] = new JIPBitmap(w, h, JIP.tBYTE);
				bmp[JIP.bBLUE] = new JIPBitmap(w, h, JIP.tBYTE);
			} else
				bmp[0] = new JIPBitmap(w, h, t);
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
	   *<FONT COLOR="blue">
	* Constructor of an empty image with b bands (without pixels values).	
	* @param b Number of bands on the image (b>0)
	* @param w Width of the image, columns (w>0)
	* @param h Height of the image, rows (h>0)
	* @param t Type of the image. If type is tCOLOR, it will have 3 RGB internal bands.
	* Else it will have only one. indepentdently specified b.
	* In other case it will have b bands.
	*/
	public JIPImage(int b, int w, int h, int t) {
		if (b > 0 && w > 0 && h > 0) {
			nbands = (t == JIP.tCOLOR) ? 3 : b;
			bmp = new JIPBitmap[nbands];
			width = w;
			height = h;
			type = t;
			if (t == JIP.tCOLOR) {
				bmp[JIP.bRED] = new JIPBitmap(w, h, JIP.tBYTE);
				bmp[JIP.bGREEN] = new JIPBitmap(w, h, JIP.tBYTE);
				bmp[JIP.bBLUE] = new JIPBitmap(w, h, JIP.tBYTE);
			} else
				for (int i = 0; i < b; i++)
					bmp[i] = new JIPBitmap(w, h, t);
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * Constructor of an image with initial integer values of the pixels.	 
	 * @param w Width of the image, columns (w>0)
	 * @param h Height of the image, rows (h>0)
	 * @param t Type of the image (tREAL is not permit because initials values are integer.
	 * If type of the image is tCOLOR, then the image has 3 RBG bands and the integer values
	 * of the vector bmpInt are considered packed RGB values. Else it has a band only.	 
	 * @param bmpInt Vector which has initial values of the pixels.
	 * bmpInt.length should fit in with the pixels number (w*h).
	 * The rank to adjust this values depend of image type:	 
	 * tBIT -> [0..1] , tBYTE -> [0..255] , tWORD -> [0..65535] , tCOLOR -> RGB
	 * packed.
	 * Order of the pixels in the vector,it is doing in rows:	 
	 * ( row values 0, row values 1, ... , row values h-1 ) where
	 * The 0 row is the top.
	 */
	public JIPImage(int w, int h, int t, int[] bmpInt) {
		if (w > 0 && h > 0 && w * h == bmpInt.length) {
			nbands = (t == JIP.tCOLOR) ? 3 : 1;
			bmp = new JIPBitmap[nbands];
			width = w;
			height = h;
			type = t;
			if (t == JIP.tCOLOR) {
				bmp[JIP.bRED] = new JIPBitmap(w, h, JIP.tBYTE,
						JIP.getAllRedFromRGB(bmpInt));
				bmp[JIP.bGREEN] = new JIPBitmap(w, h, JIP.tBYTE,
						JIP.getAllGreenFromRGB(bmpInt));
				bmp[JIP.bBLUE] = new JIPBitmap(w, h, JIP.tBYTE,
						JIP.getAllBlueFromRGB(bmpInt));
			} else
				bmp[0] = new JIPBitmap(w, h, t, bmpInt);
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * Constructor of a color image constructor with separate initial values of RGB pixels 
	 * @param w Width of the image, columns (w>0)
	 * @param h Height of the image, rows (h>0)
	 * @param red Vector which has RED initial values of the pixels.
	 * @param green Vector which has GREEN initial values of the pixels.
	 * @param blue Vector which has BLUE initial values of the pixels.
	 * The 3 vectors length should fit in with the (w*h) number of pixels.
	 * The rank to adjust this values is [0..255].	 
	 * The order of the pixels in the vector is in rows:	 
	 * ( row values 0, row values 1, ... , row values h-1 ) where
	 * 0 row is the top.	 
	 */
	public JIPImage(int w, int h, int[] red, int[] green, int[] blue) {
		if (w > 0
			&& h > 0
			&& w * h == red.length
			&& w * h == green.length
			&& w * h == blue.length) {
			nbands = 3;
			bmp = new JIPBitmap[3];
			width = w;
			height = h;
			type = JIP.tCOLOR;
			bmp[JIP.bRED] = new JIPBitmap(w, h, JIP.tBYTE, red);
			bmp[JIP.bGREEN] = new JIPBitmap(w, h, JIP.tBYTE, green);
			bmp[JIP.bBLUE] = new JIPBitmap(w, h, JIP.tBYTE, blue);
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * Constructor of a color image with a tREAL type and a band only and initial real values of the pixels. 
	 * @param w Width of the image, columns (w>0)
	 * @param h Height of the image, rows (h>0)
	 * @param bmpFloat Vector which has initial values of the pixels.
	 * The 3 vectors length should fit in with the (w*h) number of pixels.
	 * The rank to adjust this values is [0..255].	 
	 * The order of pixels in the vector is in rows:	 
	 * ( row values 0, row values 1, ... , row values h-1 ) where
	 * 0 row is the top.	 
	 * bmpFloat.length should fit in with the pixels number (w*h).
	 * The rank to adjust this values is [0..1].	 
	 * Pixels order in the vector is in rows:	 
	 * ( row values 0, row values 1, ... , row values h-1 ) where
	 * 0 row is the top.	 
	 */
	public JIPImage(int w, int h, float[] bmpFloat) {
		if (w > 0 && h > 0 && w * h == bmpFloat.length) {
			nbands = 1;
			bmp = new JIPBitmap[1];
			width = w;
			height = h;
			type = JIP.tREAL;
			bmp[0] = new JIPBitmap(w, h, bmpFloat);
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * Constructor of image with a band only and Geometry type, it is especified by means of
	 * argument with input vector. 
	 * @param w Width of the image, columns (w>0)	 
	 * @param h Height of the image, rows (h>0)
	 * @param t Image type (tPOINT, tSEGMENT, tPOLY, tEDGES) 
	 * @param v Vector which has geometry data.
	 */
	public JIPImage(int w, int h, int t, Vector v) {
		nbands = 1;
		bmp = new JIPBitmap[nbands];
		width = w;
		height = h;
		type = t;
		bmp[0] = new JIPBitmap(w, h, t, v);
	}

	/*******************/
	/*     METHODS     */
	/*******************/

	/*******************************************************/
	/* Getting and Setting of general characteristics      */
	/*******************************************************/

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It obtains number bands of the image.
	 * @return Number of bands.
	 */
	public int getNumBands() {
		return nbands;
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It obtains number of pixels of the image.	 
	 * @return Number of pixels (width*height)
	 */
	public int getNumPixels() {
		return width * height;
	}

	/**
	 * <P><FONT COLOR="red">
	 * <B>description:</B><BR>
	 * <FONT COLOR="blue">
	 * It obtains width of the image.	 
	 * @return Number of columns.
	 * 
	 * @uml.property name="width"
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * <P><FONT COLOR="red">
	 * <B>description:</B><BR>
	 * <FONT COLOR="blue">
	 * It obtains height of the image.
	 * @return Nubmer of rows.
	 * 
	 * @uml.property name="height"
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * <P><FONT COLOR="red">
	 * <B>description:</B><BR>
	 * <FONT COLOR="blue">
	 * It obtains type of the image.
	 * @return Constant who identify the type.
	 * @see JIP#tBIT
	 * @see JIP#tBYTE
	 * @see JIP#tWORD
	 * @see JIP#tREAL
	 * @see JIP#tCOLOR
	 * @see JIP#tPOINT
	 * @see JIP#tSEGMENT
	 * @see JIP#tPOLY
	 * @see JIP#tEDGES
	 * 
	 * @uml.property name="type"
	 */
	public int getType() {
		return type;
	}

	/**
	 * <P><FONT COLOR="red">
	 * <B>description:</B><BR>
	 * <FONT COLOR="blue">
	 * It obtains name of the image.
	 * @return Name of the image.
	 * 
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * <P><FONT COLOR="red">
	 * <B>description:</B><BR>
	 * <FONT COLOR="blue">
	 * It assigns name of the image.	 
	 * @param nom Name of the image.
	 * 
	 * @uml.property name="name"
	 */
	public void setName(String nom) {
		if (nom == null || nom.length() == 0)
			name = "[Unnamed]";
		else
			name = nom;
	}

	/**************************************/
	/* Assign values to pixels            */
	/**************************************/

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
	   *<FONT COLOR="blue">
	* It assigns an integer band to a pixel, (b,x,y) value of the image which type is
	* tBIT, tBYTE, tWORD o tCOLOR.
	* @param b Band which the pixels belong (0 <= b <= numbands-1)
	* If image type is tCOLOR, b can refer to constants bRED, bGREEN o bBLUE. 
	* @param x Coordinate of x pixel (0 <= x <= width-1)
	* @param y Coordinate of Y pixel (0 <= y <= height-1)
	* @param pix Value asigned to pixel.
	* The rank depends of image type:
	* tBIT -> [0..1] , tBYTE -> [0..255] , tWORD -> [0..65535] , tCOLOR -> [0..255]
	*/
	public void setPixel(int b, int x, int y, int pix) {
		if (b >= 0 && b < nbands)
			bmp[b].setPixel(x, y, pix);
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It asigns RED value of intensity in a pixel of a color image (bRED,x,y). 
	 * It is equivalent to: setPixel(JIP.bRED,x,y,red) in a color image.
	 * @param x Coordinate of x pixel (0 <= x <= width-1)
	 * @param y Coordinate of Y pixel (0 <= y <= height-1)
	 * @param red Value asigned to RED pixel intensity.
	 * The intensity rank is [0..255]
	 */
	public void setPixelRed(int x, int y, int red) {
		if (type == JIP.tCOLOR)
			setPixel(JIP.bRED, x, y, red);
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It asigns GREEN value of intensity in a pixel of a color image (bGREEN,x,y). 
	 * It is equivalent to: setPixel(JIP.bGREEN,x,y,green) in a color image.
	 * @param x Coordinate of x pixel (0 <= x <= width-1)
	 * @param y Coordinate of Y pixel (0 <= y <= height-1)
	 * @param green Value asigned to GREEN pixel intensity.
	 * The intensity rank is [0..255]	 
	 */
	public void setPixelGreen(int x, int y, int green) {
		if (type == JIP.tCOLOR)
			setPixel(JIP.bGREEN, x, y, green);
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It asigns BLUE value of intensity in a pixel of a color image (bGREEN,x,y). 
	 * It is equivalent to: setPixel(JIP.bBLUE,x,y,blue) in a color image.
	 * @param x Coordinate of x pixel (0 <= x <= width-1)
	 * @param y Coordinate of Y pixel (0 <= y <= height-1)
	 * @param green Value asigned to BLUE pixel intensity.
	 * The intensity rank is [0..255]
	 */
	public void setPixelBlue(int x, int y, int blue) {
		if (type == JIP.tCOLOR)
			setPixel(JIP.bBLUE, x, y, blue);
	}


	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It assigns the integer value in a 0 band of the pixel, (0,x,y) of a tBIT,
	 * tBYTE or tWORD image, or a RGB packed value in a pixel (x,y) of a
	 * tCOLOR image.
	 * @param x Coordinate of x pixel (0 <= x <= width-1)
	 * @param y Coordinate of Y pixel (0 <= y <= height-1)
	 * @param pix Valuee asigned in a pixel.
	 * The rank depend of the image type:
	 * tBIT -> [0..1] , tBYTE -> [0..255] , tWORD -> [0..65535] , tCOLOR ->
	 * packed RGB.
	 */
	public void setPixel(int x, int y, int pix) {
		if (type != JIP.tCOLOR)
			bmp[0].setPixel(x, y, pix);
		else {
			bmp[JIP.bRED].setPixel(x, y, JIP.getRedFromRGB(pix));
			bmp[JIP.bGREEN].setPixel(x, y, JIP.getGreenFromRGB(pix));
			bmp[JIP.bBLUE].setPixel(x, y, JIP.getBlueFromRGB(pix));
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It assigns integer values on some band of the pixels (b,x,y) of a tBIT, tBYTE, 
	 * tWORD or tCOLOR image.
	 * @param b Band which pixels belong (0 <= b <= numbands-1)
	 * If is a tCOLOR image, The bRED, bGREEN or bBLUE constants can refer b.	 
	 * @param bmpInt Vector with assigned values on pixels.
	 * The rank of vector values depend of image type:
	 * tBIT -> [0..1] , tBYTE -> [0..255] , tWORD -> [0..65535] , tCOLOR -> [0..255]
	 */
	public void setAllPixel(int b, int[] bmpInt) {
		if (b >= 0 && b < nbands)
			bmp[b].setAllPixel(bmpInt);
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It assigns RED intensity values in some pixels of a color image.	 
	 * It is equivalent to: setAllPixel(JIP.bRED, red) in a color image.	 
	 * @param red Vector of values which are assigned to RED intensity on pixels.
	 * The rank of intensity is [0..255]
	 */
	public void setAllPixelRed(int[] red) {
		if (type == JIP.tCOLOR)
			setAllPixel(JIP.bRED, red);
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It assigns GREEN intensity values in some pixels of a color image.	 
	 * It is equivalent to: setAllPixel(JIP.bGREEN, red) in a color image.	 
	 * @param green Values vector which are assigned to GREEN intensity on pixels.
	 * The rank of intensity is [0..255]	 
	 */
	public void setAllPixelGreen(int[] green) {
		if (type == JIP.tCOLOR)
			setAllPixel(JIP.bGREEN, green);
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It assigns BLUE intensity values in some pixles of a color image.	 
	 * It is equivalent to: setAllPixel(JIP.bBLUE, red) in a color image.	 
	 * @param blue Values vector which are assigned to BLUE intensity on pixels.
	 * The rank of intensity is [0..255]	 
	 */
	public void setAllPixelBlue(int[] blue) {
		if (type == JIP.tCOLOR)
			setAllPixel(JIP.bBLUE, blue);
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
     * It assigns integer values in a pixles of 0 band, (0,x,y) of a tBIT, tBYTE or
     * tWORD image or packed RGB values of a tCOLOR image.
	 * @param bmpInt Assigned values in pixels.
	 * The rank of values in vector depend of image type:
	 * tBIT -> [0..1] , tBYTE -> [0..255] , tWORD -> [0..65535] , tCOLOR ->
	 * packed RGB.
	 */
	public void setAllPixel(int[] bmpInt) {
		if (type != JIP.tCOLOR)
			bmp[0].setAllPixel(bmpInt);
		else {
			bmp[JIP.bRED].setAllPixel(JIP.getAllRedFromRGB(bmpInt));
			bmp[JIP.bGREEN].setAllPixel(JIP.getAllGreenFromRGB(bmpInt));
			bmp[JIP.bBLUE].setAllPixel(JIP.getAllBlueFromRGB(bmpInt));
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It assigns real values in a pixle of 0 band (b,x,y) of a tREAL image.
	 * @param b Number of bands.
	 * @param x Coordinate of x pixel (0 <= x <= width-1)
	 * @param y Coordinate of Y pixel (0 <= y <= height-1)
	 * @param pix Value to asign in a pixel.	 	 
	 * The rank of values are [0..1]	 
	 */
	public void setPixel(int b, int x, int y, float pix) {
		if (b >= 0 && b < nbands)
			bmp[b].setPixel(x, y, pix);
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It assigns real values in a pixle of 0 band (0,x,y) of a tREAL image.
	 * @param x Coordinate of x pixel (0 <= x <= width-1)
	 * @param y Coordinate of Y pixel (0 <= y <= height-1)
	 * @param pix Value to asign in a pixel.	 	 
	 * The rank of values are [0..1]	 	 
	 */
	public void setPixel(int x, int y, float pix) {
		if (type != JIP.tCOLOR)
			bmp[0].setPixel(x, y, pix);
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
	   *<FONT COLOR="blue">
	* It adds a segment, which coordinates are inserted as parameter in the image.
	* The image should be tSEGMENT type.	
	* @param x0 initial x point of coordinate
	* @param y0 initial y point of coordinate
	* @param x1 final x point of coordinate
	* @param y1 final y point of coordinate
	*/
	public void addSegment(int x0, int y0, int x1, int y1) {
		if (type != JIP.tSEGMENT)
			return;
		if (x0 < 0 || y0 < 0 || x1 < 0 || y1 < 0 || x0 > width - 1
			|| y0 > height - 1 || x0 > width - 1 || y1 > height - 1)
			return;
		bmp[0].addSegment(x0, y0, x1, y1);
	}

	/**<P><FONT COLOR="red">
	*<B>description:</B><BR>
	*<FONT COLOR="blue">
	** It adds a point, which coordinates are inserted as parameter in the image.
	* The image should be tPOINT type.	
	* @param x0 initial x point of coordinate
	* @param y0 initial y point of coordinate
	*/
	public void addPoint(int x0, int y0) {
		if (type != JIP.tPOINT)
			return;
		if (x0 < 0 || y0 < 0 || x0 > width - 1 || y0 > height - 1)
			return;
		bmp[0].addPoint(x0, y0);

	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
	   *<FONT COLOR="blue">
	* It adds real values in a pixels of the band (b,x,y) of tREAL image.
	* @param b Band which pixels belong (0 <= b <= numbands-1)
	* @param bmpFloat Values vector assigned to pixels.
	* The values vector rank is [0..1]	
	*/
	public void setAllPixel(int b, float[] bmpFloat) {
		if (b >= 0 && b < nbands)
			bmp[b].setAllPixel(bmpFloat);
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">	 
	 * It adds real values in a pixels of 0 band (0,x,y) of tREAL image.
	 * @param b Band which pixels belong (0 <= b <= numbands-1)
	 * @param bmpFloat Values of vector assigned to pixels.
	 * The rank of values vector are [0..1]	
	 */
	public void setAllPixel(float[] bmpFloat) {
		if (type != JIP.tCOLOR)
			bmp[0].setAllPixel(bmpFloat);
	}

	/****************************************/
	/* Assign values to vectors.		    */
	/****************************************/

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It assigns values to geometric vectors. It take the input vector, and depend of 
	 * image type, it changes the actual values for new values. 
	 * @param v Vector which has geometric elements.
	 */
	public void setVector(Vector v) {

		if (type == JIP.tPOINT
			|| type == JIP.tSEGMENT
			|| type == JIP.tPOLY
			|| type == JIP.tEDGES) {
			bmp[0].setVector(type, v);
		}

	}

	/**************************************/
	/* Geeting pixels values			  */	
	/**************************************/

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It gets the integer pixel value in an image band which type is	 
	 * tBIT, tBYTE, tWORD o tCOLOR.
	 * @param b Band which the pixel belong (0 <= b <= numbands-1)
	 * If the image is tCOLOR type, b can refer with bRED, bGREEN or bBLUE constants.
	 * @param x Coordinate of x pixel (0 <= x <= width-1)
	 * @param y Coordinate of Y pixel (0 <= y <= height-1)
	 * @return Value of pixel (b,x,y)
	 * The rank depends of image type:	 
	 * tBIT -> [0..1] , tBYTE -> [0..255] , tWORD -> [0..65535] , tCOLOR -> [0..255]
	 */
	public int getPixel(int b, int x, int y) {
		if (b >= 0 && b < nbands)
			return bmp[b].getPixel(x, y);
		else
			return -1;
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
     * It gets the value of intensity RED of pixel in a color image.	 
	 * It is equivalent to: getPixel(JIP.bRED,x,y) in a color image.
	 * @param x Coordinate of x pixel (0 <= x <= width-1)
	 * @param y Coordinate of Y pixel (0 <= y <= height-1)
	 * @return Value of RED intensity on pixel (x,y).	 
	 * The intensity rank is [0..255].
	 * If image is not color image, it returns -1 or returns a bad coordinates.	 
	 */
	public int getPixelRed(int x, int y) {
		if (type == JIP.tCOLOR)
			return getPixel(JIP.bRED, x, y);
		else
			return -1;
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
     * It gets the value of intensity GREEN of pixel in a color image.	 
	 * It is equivalent to: getPixel(JIP.bGREEN,x,y) in a color image.
	 * @param x Coordinate of x pixel (0 <= x <= width-1)
	 * @param y Coordinate of Y pixel (0 <= y <= height-1)
	 * @return Value of GREEN intensity on pixel (x,y).	 
	 * The intensity rank is [0..255].
	 * If image is not color image, it returns -1 or returns a bad coordinates.
	 */
	public int getPixelGreen(int x, int y) {
		if (type == JIP.tCOLOR)
			return getPixel(JIP.bGREEN, x, y);
		else
			return -1;
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It gets the value of intensity BLUE of pixel in a color image.	 
	 * It is equivalent to: getPixel(JIP.bBLUE,x,y) in a color image.
	 * @param x Coordinate of x pixel (0 <= x <= width-1)
	 * @param y Coordinate of Y pixel (0 <= y <= height-1)
	 * @return Value of BLUE intensity on pixel (x,y).	 
	 * The intensity rank is [0..255].
	 * If image is not color image, it returns -1 or returns a bad coordinates.
	 */
	public int getPixelBlue(int x, int y) {
		if (type == JIP.tCOLOR)
			return getPixel(JIP.bBLUE, x, y);
		else
			return -1;
	}



	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	* It gets the integer value of a 0 band pixel, (0,x,y) on a tBIT, tBYTE or tWORD
	* image or packed RGB of a pixel (x,y) on tCOLOR image.
	* @param x Coordinate of x pixel (0 <= x <= width-1)
	* @param y Coordinate of Y pixel (0 <= y <= height-1)
	* @return Value of Assigned pixel.
	* The rank depends of image type:	 
	* tBIT -> [0..1] , tBYTE -> [0..255] , tWORD -> [0..65535] , tCOLOR ->
	* packed RGB.
	*/
	public int getPixel(int x, int y) {
		if (type != JIP.tCOLOR)
			return bmp[0].getPixel(x, y);
		else {
			int r = bmp[JIP.bRED].getPixel(x, y);
			int g = bmp[JIP.bGREEN].getPixel(x, y);
			int b = bmp[JIP.bBLUE].getPixel(x, y);
			return JIP.getRGB(r, g, b);
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue"> 
	* It gets the integer value of a band pixel in a tBIT, tBYTE, tWORD or tCOLOR image.	 
	* @param b Band which the pixels belong (0 <= b <= numbands-1)
	* If the image is a tCOLOR type, bRED, bGREEN or bBLUE constants can refer to b.	 
	* @return Vector with assigned value to the pixels.
	* The rank of vector value depend of image type:	 
	* tBIT -> [0..1] , tBYTE -> [0..255] , tWORD -> [0..65535] , tCOLOR ->[0..255]	 
	*/	
	public int[] getAllPixel(int b) {
		if (b >= 0 && b < nbands)
			return bmp[b].getAllPixel();
		else
			return null;
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It gets the intensity RED values of the pixel in a color image.
	 * It is equivalent to: getAllPixel(JIP.bRED) in a color image.
	 * @return Vector with RED itensity values of the pixels.
	 * The rank of intensity is [0..255]	 
	 */
	public int[] getAllPixelRed() {
		if (type == JIP.tCOLOR)
			return getAllPixel(JIP.bRED);
		else
			return null;
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It gets the intensity GREEN values of the pixel in a color image.
	 * It is equivalent to: getAllPixel(JIP.bGREEN) in a color image.
	 * @return Vector with GREEN itensity values of the pixels.
	 * The rank of intensity is [0..255] 	 
	 */
	public int[] getAllPixelGreen() {
		if (type == JIP.tCOLOR)
			return getAllPixel(JIP.bGREEN);
		else
			return null;
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It gets the intensity BLUE values of the pixel in a color image.
	 * It is equivalent to: getAllPixel(JIP.bBLUE) in a color image.
	 * @return Vector with BLUE itensity values of the pixels.
	 * The rank of intensity is [0..255]	 
	 */
	public int[] getAllPixelBlue() {
		if (type == JIP.tCOLOR)
			return getAllPixel(JIP.bBLUE);
		else
			return null;
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It gets the integer values of 0 band pixels in a tBIT, tBYTE o tWORD image
	 * or RGB packed values of the pixels on a tCOLOR image.
	 * @return Pixels values.
	 * The rank of values depend of image type:	 
	 * tBIT -> [0..1] , tBYTE -> [0..255] , tWORD -> [0..65535] , tCOLOR ->
	 * packed RGB.
	 */
	public int[] getAllPixel() {
		if (type != JIP.tCOLOR)
			return bmp[0].getAllPixel();
		else {
			int[] r = bmp[JIP.bRED].getAllPixel();
			int[] g = bmp[JIP.bGREEN].getAllPixel();
			int[] b = bmp[JIP.bBLUE].getAllPixel();
			return JIP.getAllRGB(r, g, b);
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It gets the real value of a band pixel (b,x,y) in a tREAL image.
	 * @param b Band which the pixel belong (0 <= b <= numbands-1)
	 * @param x Coordinate of x pixel (0 <= x <= width-1)
	 * @param y Coordinate of Y pixel (0 <= y <= height-1)
	 * @return The real value of pixel.
	 * The rank of values are [0..1]
	 */
	public float getPixelFlo(int b, int x, int y) {
		if (b >= 0 && b < nbands)
			return bmp[b].getPixelFlo(x, y);
		else
			return -1;
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It gets the real value of a 0 band pixel (0,x,y) in a tREAL image.
	 * @param x Coordinate of x pixel (0 <= x <= width-1)
	 * @param y Coordinate of Y pixel (0 <= y <= height-1) 
	 * @return The Real value of the pixel.
	 * The rank of values are [0..1]
	 */
	public float getPixelFlo(int x, int y) {
		if (type != JIP.tCOLOR)
			return bmp[0].getPixelFlo(x, y);
		else
			return -1;
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
     * It gets the real values of a band pixels in a tREAL image.
	 * @param b Band which the pixels belong (0 <= b <= numbands-1)
	 * @return  Vector with real values of the band pixels.
	 * The rank of values for the vector are [0..1]
	 */
	public float[] getAllPixelFlo(int b) {
		if (b >= 0 && b < nbands)
			return bmp[b].getAllPixelFlo();
		else
			return null;
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It gets the real values of a 0 band pixels in a tREAL image.	 
	 * @return  Vector with real values of the band pixels.
	 * The rank of values for the vector are [0..1]
	 */
	public float[] getAllPixelFlo() {
		if (type != JIP.tCOLOR)
			return bmp[0].getAllPixelFlo();
		else
			return null;
	}

	/************************************************/	
	/* Getting geometry vectors values				*/
	/************************************************/

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It gets the geometry values of vector, if the image is geometric type.	 
	 * @return Vector with the geometric image values.
	 */
	public Vector getVector() {
		Vector aux = new Vector();
		switch (type) {
			case JIP.tPOINT :
				aux = bmp[0].getVectorPoint();
				break;
			case JIP.tSEGMENT :
				aux = bmp[0].getVectorSegment();
				break;
			case JIP.tPOLY :
				aux = bmp[0].getVectorPoly();
				break;
			case JIP.tEDGES :
				aux = bmp[0].getVectorEdges();
				break;
		}

		return aux;
	}

	/************************************************/
	/* Gettin values of actual vectors length       */	
	/************************************************/

	/**<P><FONT COLOR="red">
	*<B>description:</B><BR>
	*<FONT COLOR="blue">
	* It gets the geometric elemnts size in the  image, if the image is a geometry type.
	* @return Number of geometric elements of the image.
	*/

	public int getLVector() {
		int aux = -1;
		switch (type) {
			case JIP.tPOINT :
				aux = bmp[0].getLVectorPoint();
				break;
			case JIP.tSEGMENT :
				aux = bmp[0].getLVectorSegment();
				break;
			case JIP.tPOLY :
				aux = bmp[0].getLVectorPoly();
				break;
			case JIP.tEDGES :
				aux = bmp[0].getLVectorEdges();
				break;
		}

		return aux;
	}

	/***************************************/
	/* Manipulating image bands			   */	
	/***************************************/

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
	   *<FONT COLOR="blue">
	* It gets the bitmap which represents an image band.	
	* @param b Band which it should set (0 <= b <= numbands-1)
	* If is a tCOLOR image, b can refer with bRED, bGREEN or bBLUE constants.
	* The bitmap is a 3 bands in a tCOLOR image is everytime tBYTE type.
	* @return Bitmap which belong to b band.
	*/
	public JIPBitmap getBand(int b) {
		if (b >= 0 && b < nbands)
			return bmp[b];
		else
			return null;
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It assigns a new bitmap for an image band.	 
	 * @param b Band to assign (0 <= b <= numbands-1)
	 * If the image is tCOLOR type, b can refer with bRED, bGREEN or bBLUE constants.
	 * @param newbmp Bitmap that we assign to b band of the image. it should
	 * have the same size as the image and if the image is not a tCOLOR,
	 * it should be from the same type.
	 * In case of it is a tCOLOR, the bitmap should be tBYTE type.	 
	 */
	public void setBand(int b, JIPBitmap newbmp) {
		if (newbmp != null
			&& width == newbmp.width
			&& height == newbmp.height
			&& b >= 0
			&& b < nbands) {
			if ((type == JIP.tCOLOR && newbmp.type == JIP.tBYTE)
				|| (type != JIP.tCOLOR && type == newbmp.type)) {
				bmp[b] = new JIPBitmap(newbmp);
			}
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It adds a band in a image. The image can not be tCOLOR type.	 
	 * @param newbmp is a Bitmap thate we add to be the image band.
	 * It should have the same size as the image and should be the same type.
	 */
	public void appendBand(JIPBitmap newbmp) {
		if (newbmp != null
			&& type != JIP.tCOLOR
			&& type == newbmp.type
			&& width == newbmp.width
			&& height == newbmp.height) {
			nbands++;
			JIPBitmap[] auxbmp = new JIPBitmap[nbands];
			for (int i = 0; i < nbands - 1; i++)
				auxbmp[i] = bmp[i];
			auxbmp[nbands - 1] = new JIPBitmap(newbmp);
			bmp = auxbmp;
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>description:</B><BR>
		   *<FONT COLOR="blue">
	 * It deletes an image band. The image can not be tCOLOR type and should
	 * have 2 bands or more because it can not be empty.
	 * @param b Band to delete (0 <= b <= numbands-1) where (numbands > 1)
	 */
	public void removeBand(int b) {
		if (nbands > 1 && type != JIP.tCOLOR && b >= 0 && b < nbands) {
			int i, j;
			JIPBitmap[] auxbmp = new JIPBitmap[nbands - 1];
			for (i = 0, j = 0; i < nbands; i++) {
				if (i != b) {
					auxbmp[j] = bmp[i];
					j++;
				}
			}
			nbands--;
			bmp = auxbmp;
		}
	}

}
