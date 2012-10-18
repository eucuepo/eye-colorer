package com.eyecolorer.image.toolkit;
import java.util.Vector;

/**<P><FONT COLOR="red">
*<B>Description:</B><BR>
*<FONT COLOR="blue">
* Class to define the Object Bitmap. A Bitmap represents a matrix of pixels with 
* width and height constant. Also can contain vectors of geometric types
* If exist a set of pixels, each component of the matrix will contain an intensity 
* value of the pixel represented. 
* These intensity values are represented depending on the Bitmap type. These types
* of bitmaps are:
* tBIT -> integers [0..1] , tBYTE -> integers [0..255] , tWORD -> integers [0..65535]
* and tREAL -> floats [0..1].
* Also we can create bitmap with geometric elements like Points, Segments, Polygons
* or Edges, whose associate types are : tPOINT, tSEGMENT, tPOLY and tEDGES.
* We could create 'empty' bitmaps (without values, only with width, height and type)
* or 'initialized'. Width, height and type are properties that we could not change.
* Nevertheless if the type is geometric we will be able to create it from an empty vector 
* and afterwards change values like size of the vector that contains the geometric elements
* We will be able to set and to get also the values of the pixels as a whole 
* (as an array) or individually . Individual access will be specifying x and y coords
* of the pixel. These coords are corresponding to: (0,0) ->
* upper left corner and (width-1,height-1) -> bottom right corner.
* In the same way we will be able to obtain the geometric values, getting a Vector that 
* contains all geometric coords.
* A Bitmap can't represent color, only contains intensities of pixels.
* A Bitmap will codify a band of an image. The images could be in color 
* (3 bands RGB represented with their bitmaps).
* @see JIP#tBIT
* @see JIP#tBYTE
* @see JIP#tWORD
* @see JIP#tREAL
* @see JIP#tPOINT
* @see JIP#tSEGMENT
* @see JIP#tPOLY
* @see JIP#tEDGES
*/
public class JIPBitmap {

	/**
	 * Width of the Bitmap
	 * 
	 * @uml.property name="width"
	 */
	int width = -1;

	/**
	 * Height of the Bitmap
	 * 
	 * @uml.property name="height"
	 */
	int height = -1;

	/**
	 * Type of the Bitmap
	 * 
	 * @uml.property name="type"
	 */
	int type = -1;


	/**
	 * Vector of Segments
	 * Each segment occupies four consecutive positions of the vector . 
	 * The first two corresponds to the beginning point, and the last 
	 * two to the final point of the vector.
	 * 
	 * @uml.property name="segmentos"
	 * @uml.associationEnd 
	 * @uml.property name="segmentos" multiplicity="(0 1)"
	 */
	Vector Segmentos;

	/**
	 * Vector of Points
	 * Each point occupies two consecutive positions of the vector. The first one
	 * is the X coord and the second one the Y coord.
	 * 
	 * @uml.property name="puntos"
	 * @uml.associationEnd 
	 * @uml.property name="puntos" multiplicity="(0 1)"
	 */
	Vector Puntos;

	/**
	 * Vector of Polygons
	 * Each position is another vector where each two positions a point of the 
	 * polygon is represented. The size of the main vector indicates the number of
	 * polygons.
	 * 
	 * @uml.property name="poligonos"
	 * @uml.associationEnd 
	 * @uml.property name="poligonos" multiplicity="(0 1)"
	 */
	Vector Poligonos;

	/**
	 * Vector of Edges
	 * Each position is another vector where each two positions a point of the 
	 * edge is represented. The size of the main vector indicates the number of
	 * edges.
	 * 
	 * @uml.property name="edges"
	 * @uml.associationEnd 
	 * @uml.property name="edges" multiplicity="(0 1)"
	 */
	Vector Edges;


	/** Number of segments */
	int lSegmentos = -1;

	/** Number of points */
	int lPuntos = -1;

	/** Number of polygons */
	int lPoligonos = -1;

	/** Number of edges */
	int lEdges = -1;

	/** Array of Points (type BIT)*/
	boolean[] bmpBit = null;

	/** Array of Points (type BYTE)*/
	byte[] bmpByte = null;

	/** Array of Points (type WORD)*/
	short[] bmpWord = null;

	/** Array of Points (type REAL)*/
	float[] bmpReal = null;

	//******************//
	//  Constructors   //
	//******************//

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
		 * Constructor of a bitmap like a copy of another bitmap.
	* @param bmp Bitmap of reference.
	*/
	public JIPBitmap(JIPBitmap bmp) {
		if (bmp != null) {
			width = bmp.width;
			height = bmp.height;
			type = bmp.type;
			switch (type) {
				case 0 :
					bmpBit = new boolean[width * height];
					break;
				case JIP.tBYTE :
					bmpByte = new byte[width * height];
					break;
				case JIP.tWORD :
					bmpWord = new short[width * height];
					break;
				case JIP.tREAL :
					bmpReal = new float[width * height];
					break;
				case JIP.tPOINT :
					Puntos = new Vector();
					break;
				case JIP.tPOLY :
					Poligonos = new Vector();
					break;
				case JIP.tEDGES :
					Edges = new Vector();
					break;

			}
			if (type == JIP.tBIT || type == JIP.tBYTE
				|| type == JIP.tWORD || type == JIP.tREAL) {
				for (int i = 0; i < width * height; i++) {
					switch (type) {
						case JIP.tBIT :
							bmpBit[i] = bmp.bmpBit[i];
							break;
						case JIP.tBYTE :
							bmpByte[i] = bmp.bmpByte[i];
							break;
						case JIP.tWORD :
							bmpWord[i] = bmp.bmpWord[i];
							break;
						case JIP.tREAL :
							bmpReal[i] = bmp.bmpReal[i];
							break;
					}
				}
			} else
				switch (type) {
					case JIP.tPOINT :
						Puntos = (Vector) bmp.Puntos.clone();
						lPuntos = bmp.lPuntos;
						break;
					case JIP.tSEGMENT :
						Segmentos = (Vector) bmp.Segmentos.clone();
						lSegmentos = bmp.lSegmentos;
						break;
					case JIP.tPOLY :
						Poligonos = (Vector) bmp.Poligonos.clone();
						lPoligonos = bmp.lPoligonos;
						break;
					case JIP.tEDGES :
						Edges = (Vector) bmp.Edges.clone();
						lEdges = bmp.lEdges;
						break;
				}
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
	* Constructor of an empty bitmap (without pixel values).
	* @param w witdh (columns) of the bitmap (w>0)
	* @param h Height (rows) of the bitmap (h>0)
	* @param t Type of the bitmap.
	*/
	public JIPBitmap(int w, int h, int t) {
		if (w > 0 && h > 0) {
			width = w;
			height = h;
			type = t;
			switch (t) {
				case JIP.tBIT :
					bmpBit = new boolean[w * h];
					break;
				case JIP.tBYTE :
					bmpByte = new byte[w * h];
					break;
				case JIP.tWORD :
					bmpWord = new short[w * h];
					break;
				case JIP.tREAL :
					bmpReal = new float[w * h];
					break;
				case JIP.tSEGMENT :
					Segmentos = new Vector();
					break;
				case JIP.tPOINT :
					Puntos = new Vector();
					break;
				case JIP.tPOLY :
					Poligonos = new Vector();
					break;
				case JIP.tEDGES :
					Edges = new Vector();
					break;

			}
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
	* Constructor of the bitmap with initial integer values of the pixels.
	* @param w Width (columns) of the bitmap (w>0)
	* @param h Height (rows) of the bitmap (h>0)
	* @param t Type of the bitmap (tREAL is not allowed because the initial values
	* are integers)
	* @param bmpInt Vector that contains the initial values of the pixels.
	* bmpInt.length must to be the same that the number of pixels (w*h).
	* The value ranges depend on the type of the bitmap:
	* tBIT -> [0..1] , tBYTE -> [0..255] , tWORD -> [0..65535]
	* Pixels in the vector are ordered by rows:
	* ( values row 0, values row 1, ... , values row h-1 ) where row 0 correspond to 
	* the upper row in the image.
	*/
	public JIPBitmap(int w, int h, int t, int[] bmpInt) {
		if (w > 0 && h > 0 && w * h == bmpInt.length) {
			width = w;
			height = h;
			type = t;
			switch (t) {
				case JIP.tBIT :
					bmpBit = JIP.getAllBitFromInt(bmpInt);
					break;
				case JIP.tBYTE :
					bmpByte = JIP.getAllByteFromInt(bmpInt);
					break;
				case JIP.tWORD :
					bmpWord = JIP.getAllWordFromInt(bmpInt);
					break;
			}
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
	* Constructor of the bitmap with initial floats values of the pixels.
	* Type will be tREAL.
	* @param w Width (columns) of the bitmap (w>0)
	* @param Height (rows) of the bitmap (h>0)
	* @param bmpFloat Vector that contains the initial values of the pixels.
	* bmpFloat.length debe coincidir con el numero de pixels (w*h).
	* The value ranges: [0..1].
	* Pixels in the vector are ordered by rows:
	* ( values row 0, values row 1, ... , values row h-1 ) where row 0 correspond to 
	* the upper row in the image.
	*/
	public JIPBitmap(int w, int h, float[] bmpFloat) {
		if (w > 0 && h > 0 && w * h == bmpFloat.length) {
			width = w;
			height = h;
			type = JIP.tREAL;
			bmpReal = JIP.getAllRealFromFloat(bmpFloat);
		}
	}
	/**<P><FONT COLOR="red">
	 *<B>Description:</B><BR>
	 *<FONT COLOR="blue">
	* Constructor of the bitmap with initial boolean values of the pixels.
	* Type will be tBIT.
	* @param w Width (columns) of the bitmap (w>0)
	* @param Height (rows) of the bitmap (h>0)
	* @param bmpFloat Vector that contains the initial values of the pixels.
	* bmpFloat.length debe coincidir con el numero de pixels (w*h).
	* The value ranges: [0,1].
	* Pixels in the vector are ordered by rows:
	* ( values row 0, values row 1, ... , values row h-1 ) where row 0 correspond to 
	* the upper row in the image.
	*/

	public JIPBitmap(int w, int h, boolean[] bmp) {
		if (w > 0 && h > 0 && w * h == bmp.length) {
			width = w;
			height = h;
			type = JIP.tBIT;
			bmpBit = bmp;
		}
	}

	/**<P><FONT COLOR="red">
	 *<B>Description:</B><BR>
	 *<FONT COLOR="blue">
	* Constructor of the bitmap with initial byte values of the pixels.
	* Type will be tBYTE.
	* @param w Width (columns) of the bitmap (w>0)
	* @param Height (rows) of the bitmap (h>0)
	* @param bmpFloat Vector that contains the initial values of the pixels.
	* bmpFloat.length debe coincidir con el numero de pixels (w*h).
	* The value ranges: [0..255].
	* Pixels in the vector are ordered by rows:
	* ( values row 0, values row 1, ... , values row h-1 ) where row 0 correspond to 
	* the upper row in the image.
	*/

	public JIPBitmap(int w, int h, byte[] bmp) {
		if (w > 0 && h > 0 && w * h == bmp.length) {
			width = w;
			height = h;
			type = JIP.tBYTE;
			bmpByte = bmp;
		}
	}
	/**<P><FONT COLOR="red">
	 *<B>Description:</B><BR>
	 *<FONT COLOR="blue">
	* Constructor of the bitmap with initial word values of the pixels.
	* Type will be tWORD.
	* @param w Width (columns) of the bitmap (w>0)
	* @param Height (rows) of the bitmap (h>0)
	* @param bmpFloat Vector that contains the initial values of the pixels.
	* bmpFloat.length debe coincidir con el numero de pixels (w*h).
	* The value ranges: [0..65535].
	* Pixels in the vector are ordered by rows:
	* ( values row 0, values row 1, ... , values row h-1 ) where row 0 correspond to 
	* the upper row in the image.
	*/

	public JIPBitmap(int w, int h, short[] bmp) {
		if (w > 0 && h > 0 && w * h == bmp.length) {
			width = w;
			height = h;
			type = JIP.tWORD;
			bmpWord = bmp;
		}
	}

	/**<P><FONT COLOR="red">
	*<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Constructor of a geometric bitmap from a vector of values.
	* Type will be: POINT, SEGMENT, POLY or EDGES
	* @param w Width (columns) of the bitmap (w>0)
	* @param Height (rows) of the bitmap (h>0)
	* @param v Vector with geometric values
	* The order of the elements depend on type:<BR>
	* - POINT: Each point occupies two consecutive positions iThe first one is the X 
	* coord and the second one is the Y coord.<BR>
	* - SEGMENT: Each segment occupies four consecutive positions in the vector. 
	* The first two positions correspond to the init point and the last two positions
	* to the end point.<BR>
	* - POLY: Each position is another vector where each two positions a point of the 
	* polygon is represented. The size of the main vector indicates the number of
	* polygons.<BR>
	* - EDGES: Each position is another vector where each two positions a point of the 
	* edge is represented. The size of the main vector indicates the number of
	* edges. <BR>
	*/

	public JIPBitmap(int w, int h, int t, Vector v) {
		type = t;
		switch (type) {
			case JIP.tPOINT :
				Puntos = new Vector();
				Puntos = (Vector) v.clone();
				lPuntos = Puntos.size();
				break;
			case JIP.tSEGMENT :
				Segmentos = new Vector();
				Segmentos = (Vector) v.clone();
				lSegmentos = Segmentos.size();
				break;
			case JIP.tPOLY :
				Poligonos = new Vector();
				Poligonos = (Vector) v.clone();
				lPoligonos = Poligonos.size();
				break;
			case JIP.tEDGES :
				Edges = new Vector();
				Edges = (Vector) v.clone();
				lEdges = Edges.size();
				break;
		}
	}

	//*******************//
	//     METHODS       //
	//*******************//

	/******************************************/
	/* Getting general features */
	/******************************************/

	/**<P><FONT COLOR="red">
			 *<B>Description:</B><BR>
			 *<FONT COLOR="blue">
	 * Get the number of pixel of a bitmap.
	 * @return Number of pixels (rows*columns)
	 */
	public int getNumPixels() {
		return width * height;
	}

	/**
	 * <P><FONT COLOR="red">
	 * <B>Description:</B><BR>
	 * <FONT COLOR="blue">
	 * Get the width of the bitmap.
	 * @return Number of columns.
	 * 
	 * @uml.property name="width"
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * <P><FONT COLOR="red">
	 * <B>Description:</B><BR>
	 * <FONT COLOR="blue">
	 * Get the height of the bitmap.
	 * @return Number of rows.
	 * 
	 * @uml.property name="height"
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * <P><FONT COLOR="red">
	 * <B>Description:</B><BR>
	 * <FONT COLOR="blue">
	 * Getthe type of the bitmap.
	 * @return Const to identify type.
	 * @see JIP#tBIT
	 * @see JIP#tBYTE
	 * @see JIP#tWORD
	 * @see JIP#tREAL
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

	/**<P><FONT COLOR="red">
			 *<B>Description:</B><BR>
			 *<FONT COLOR="blue">
	 * Get the vector of points of the bitmap
	 * @return Vector of points
	 */
	public Vector getVectorPoint() {
		return Puntos;
	}
	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
	* Get the vector of segments of the bitmap
	* @return Vector of segments
	*/
	public Vector getVectorSegment() {
		return Segmentos;
	}
	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
	* Get the vector of polygons of the bitmap
	* @return Vector of polygons
	*/
	public Vector getVectorPoly() {
		return Poligonos;
	}
	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
	* Get the vector of edges of the bitmap
	* @return Vector of edges
	*/
	public Vector getVectorEdges() {
		return Edges;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
	* Get the number of points from the Vector of points
	* @return Number of points
	*/

	public int getLVectorPoint() {
		return lPuntos;
	}
	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
	* Get the number of segments from the Vector of segments
	* @return Number of segments
	*/

	public int getLVectorSegment() {
		return lSegmentos;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
	* Get the number of polygons from the Vector of polygons
	* @return Number of polygons
	*/

	public int getLVectorPoly() {
		return lPoligonos;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
	* Get the number of edges from the Vector of edges
	* @return Number of edges
	*/

	public int getLVectorEdges() {
		return lEdges;
	}

	/**************************************/
	/* Set values to pixels */
	/**************************************/

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
	* Set a integer value to a pixel of a bitmap of type tBIT, tBYTE or tWORD.
	* @param x X Coord of the pixel (0 <= x <= width-1)
	* @param y Y Coord of the pixel (0 <= y <= height-1)
	* @param pix Value to assingn.
	* The range depend on the type of the bitmap:
	* tBIT -> [0,1] , tBYTE -> [0..255] , tWORD -> [0..65535]
	*/

	public void setPixel(int x, int y, int pix) {
		if (x < width && y < height && x >= 0 && y >= 0) {
			switch (type) {
				case JIP.tBIT :
					bmpBit[x + y * width] = JIP.getBitFromInt(pix);
					break;
				case JIP.tBYTE :
					bmpByte[x + y * width] = JIP.getByteFromInt(pix);
					break;
				case JIP.tWORD :
					bmpWord[x + y * width] = JIP.getWordFromInt(pix);
					break;
			}
		}
	}

	/**<P><FONT COLOR="red">
			 *<B>Description:</B><BR>
			 *<FONT COLOR="blue">
	 * Set a float value to a pixel of a bitmap of type tREAL.
	 * @param x X Coord of the pixel (0 <= x <= width-1)
	 * @param y Y Coord of the pixel (0 <= y <= height-1)
	 * @param pix Value to assingn.
	 * The range will be [0..1]
	 */
	public void setPixel(int x, int y, float pix) {
		if (x < width && y < height && x >= 0 && y >= 0 && type == JIP.tREAL)
			bmpReal[x + y * width] = JIP.getRealFromFloat(pix);
	}

	/**<P><FONT COLOR="red">
			 *<B>Description:</B><BR>
			 *<FONT COLOR="blue">
	 * Set the value to all pixels of a bitmap of type tBIT, tBYTE or tWORD.
	 * @param bmpInt Vector of values.
	 * bmpInt.length must be the same that the number of pixels (width*height)
	 * The range depend on the type of the bitmap:
	 * tBIT -> [0,1] , tBYTE -> [0..255] , tWORD -> [0..65535]
	 * Pixels in the vector are ordered by rows:
	 * ( values row 0, values row 1, ... , values row h-1 ) where row 0 correspond to 
	 * the upper row in the image.
	 */
	public void setAllPixel(int[] bmpInt) {
		if (width * height == bmpInt.length) {
			switch (type) {
				case JIP.tBIT :
					bmpBit = JIP.getAllBitFromInt(bmpInt);
					break;
				case JIP.tBYTE :
					bmpByte = JIP.getAllByteFromInt(bmpInt);
					break;
				case JIP.tWORD :
					bmpWord = JIP.getAllWordFromInt(bmpInt);
					break;
			}
		}
	}

	/**<P><FONT COLOR="red">
			 *<B>Description:</B><BR>
			 *<FONT COLOR="blue">
	 * Set the value to all pixels of a bitmap of type tBIT, tBYTE or tWORD.
	 * @param bmpFloat Vector of values.
	 * bmpFloat.length must be the same that the number of pixels (width*height)
	 * The range will be [0..1]
	 * Pixels in the vector are ordered by rows:
	 * ( values row 0, values row 1, ... , values row h-1 ) where row 0 correspond to 
	 * the upper row in the image.
	 */
	public void setAllPixel(float[] bmpFloat) {
		if (width * height == bmpFloat.length && type == JIP.tREAL) {
			bmpReal = JIP.getAllRealFromFloat(bmpFloat);
		}
	}

	/**<P><FONT COLOR="red">
	*<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Set value from a geometric value to a bitmap of type
	* tPOINT, tSEGMENT, tPOLY or tEDGES
	* @param t: Type of the bitmap
	* @param v: Vector that contains geometric info.
	*/

	public void setVector(int t, Vector v) {
		switch (t) {
			case JIP.tPOINT :

				Puntos = (Vector) v.clone();
				break;
			case JIP.tSEGMENT :
				Segmentos = (Vector) v.clone();
				break;
			case JIP.tPOLY :
				Poligonos = (Vector) v.clone();
				break;
			case JIP.tEDGES :
				Edges = (Vector) v.clone();
				break;
		}

	}

	/**************************************/
	/* Add single values to a vector  */
	/**************************************/

	/**<P><FONT COLOR="red">
	 *<B>Description:</B><BR>
	 *<FONT COLOR="blue">
	* Add a segment with coords values assigned from the params to a bitmap of type tSEGMENT,
	* @param x0: X Coord of init point.
	* @param y0: Y Coord of init point.
	* @param x1: X Coord of end point.
	* @param y1: Y Coord of end point.
	*/

	public void addSegment(int x0, int y0, int x1, int y1) {
		if (type != JIP.tSEGMENT)
			return;
		Integer aux = new Integer(x0);
		Segmentos.addElement(aux);
		aux = new Integer(y0);
		Segmentos.addElement(aux);
		aux = new Integer(x1);
		Segmentos.addElement(aux);
		aux = new Integer(y1);
		Segmentos.addElement(aux);
		lSegmentos = Segmentos.size();
	}

	/**<P><FONT COLOR="red">
	 *<B>Description:</B><BR>
	 *<FONT COLOR="blue">
	* Add a point with coords values assigned 
	* from the params to a bitmap of type tPOINT,
	* @param x0: X Coord of the point.
	* @param y0: Y Coord of the point.
	*/

	public void addPoint(int x0, int y0) {
		if (type != JIP.tPOINT)
			return;
		Integer aux = new Integer(x0);
		Puntos.addElement(aux);
		aux = new Integer(y0);
		Puntos.addElement(aux);
	}

	/**************************************/
	/* Get value of the pixels */
	/**************************************/

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
		 * Get the value of a pixel of a bitmap of type tBIT, tBYTE or tWORD.
	* @param x X Coord of the pixel (0 <= x <= width-1)
	* @param y Y Coord of the pixel (0 <= y <= height-1)
	* @return Integer value of the pixel.
	* The range of the value depend on the type of the bitmap:
	* tBIT -> [0,1] , tBYTE -> [0..255] , tWORD -> [0..65535]
	*/

	public int getPixel(int x, int y) {
		int res = -1;
		if (x < width && y < height && x >= 0 && y >= 0) {
			switch (type) {
				case JIP.tBIT :
					res = JIP.getIntFromBit(bmpBit[x + y * width]);
					break;
				case JIP.tBYTE :
					res = JIP.getIntFromByte(bmpByte[x + y * width]);
					break;
				case JIP.tWORD :
					res = JIP.getIntFromWord(bmpWord[x + y * width]);
					break;
			}
		}
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
		 * Get the value of a pixel of a bitmap of type tREAL.
	* @param x X Coord of the pixel (0 <= x <= width-1)
	* @param y Y Coord of the pixel (0 <= y <= height-1)
	* @return Float value of the pixel.
	* The range of the value is [0..1]
	*/
	public float getPixelFlo(int x, int y) {
		float res = -1;
		if (x < width && y < height && x >= 0 && y >= 0 && type == JIP.tREAL)
			res = bmpReal[x + y * width];
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
		 * Get the values of the pixels of a bitmap of type tBIT, tBYTE or tWORD.
	* @return Vector that contains integers values of the pixels.
	* The lenght of the vector is width*height.
	* The range of the value depend on the type of the bitmap:
	* tBIT -> [0,1] , tBYTE -> [0..255] , tWORD -> [0..65535]
	* Pixels in the vector are ordered by rows:
	* ( values row 0, values row 1, ... , values row h-1 ) where row 0 correspond to 
	* the upper row in the image.
	*/
	public int[] getAllPixel() {
		int[] res = null;
		switch (type) {
			case JIP.tBIT :
				res = JIP.getAllIntFromBit(bmpBit);
				break;
			case JIP.tBYTE :
				res = JIP.getAllIntFromByte(bmpByte);
				break;
			case JIP.tWORD :
				res = JIP.getAllIntFromWord(bmpWord);
				break;
		}
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
		 * Get the values of the pixels of a bitmap of type tREAL.
	* @return Vector that contains float values of the pixels.
	* The lenght of the vector is width*height.
	* The range of the value depend will be [0..1]
	* Pixels in the vector are ordered by rows:
	* ( values row 0, values row 1, ... , values row h-1 ) where row 0 correspond to 
	* the upper row in the image.
	*/
	public float[] getAllPixelFlo() {
		float[] res = null;
		if (type == JIP.tREAL)
			res = JIP.getAllRealFromFloat(bmpReal);
		return res;
	}
}
