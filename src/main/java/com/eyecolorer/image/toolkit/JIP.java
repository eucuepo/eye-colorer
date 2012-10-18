package com.eyecolorer.image.toolkit;

import java.util.Vector;

/**<P><FONT COLOR="red">
*<B>Description:</B><BR>
*<FONT COLOR="blue">
* Class to define constants and basic conversion methods for JIP library applications.
*/
public final class JIP {

	/** Empty Constructor*/

	JIP() {
	}

	//**************************************************
	// Constants to identify image types
	//**************************************************

	/**
	 * Const that identify a binary image.
	 * Pixel values are integers between [0..1]
	 */
	public static final int tBIT = 0;

	/**
	 * Const that identify a 256 grayscale image.
	 * Pixel values are integers between [0..255]
	 */
	public static final int tBYTE = 1;

	/**
	 * Const that identify a 65536 grayscale image.
	 * Pixel values are integers between [0..65535]
	 */
	public static final int tWORD = 2;

	/**
	 * Const that identify a grayscale image.
	 * Pixel values are floats between [0..1]
	 */
	public static final int tREAL = 3;

	/**
	 * Const that identify a color image (3 bands RGB).
	 * Pixel values are integers between [0..255] for each band.
	 */
	public static final int tCOLOR = 4;
	/**
	 * Const that identify an image with Segment type.
	 */

	public static final int tSEGMENT = 5;
	/**
		 * Const that identify an image with Point type.
		 */

	public static final int tPOINT = 6;
	/**
		 * Const that identify an image with Polygon type.
		 */
	public static final int tPOLY = 7;
	/**
		 * Const that identify an image with Edges type
		 */

	public static final int tEDGES = 8;

	//*******************************************
	// Consts to identify RGB Bands
	//*******************************************

	/** Const that identify RED Band of a color image. */
	public static final int bRED = 0;

	/** Const that identify GREEN Band of a color image. */
	public static final int bGREEN = 1;

	/** Const that identify BLUE Band of a color image. */
	public static final int bBLUE = 2;

	//****************************************************
	// Consts to identify param types
	//****************************************************

	/** Const that identify a boolean param. */
	public static final int pBOOL = 0;

	/** Const that identify an integer param. */
	public static final int pINT = 1;

	/** Const that identify a real param. */
	public static final int pREAL = 2;

	/** Const that identify a string param. */
	public static final int pSTRING = 3;

	/** Const that identify an image param. */
	public static final int pIMAGE = 4;

	/** Const that identify a list param. */
	public static final int pLIST = 5;

	/** Const that identify an object param. */
	public static final int pOBJECT = 6;

	/** Const that identify a file param. */
	public static final int pFILE = 7;

	/** Const that identify a dir param. */
	public static final int pDIR = 8;
	
	//******************************************************************
	// Conversion Methods USER --> VALID
	//******************************************************************

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
	* Converts the pixel value into binary value.
	* @param pix Pixel value.
	* @return Valid binary value: integer [0..1]
	*/
	public static int getValidBit(int pix) {
		return (pix >= 1) ? 1 : 0;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
	* Converts the pixels values into binary values.
	* @param pix Pixels values.
	* @return Valid binary values: integers [0..1]
	*/
	public static int[] getAllValidBit(int[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		int[] res = new int[pix.length];
		for (int i = 0; i < pix.length; i++)
			res[i] = (pix[i] >= 1) ? 1 : 0;
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts the pixel value into BYTE value.
	* @param pix Pixel value.
	* @return Valid BYTE value: integer [0..255]
	*/
	public static int getValidByte(int pix) {
		if (pix >= 255) return 255;
		else if (pix <= 0) return 0;
			 else return pix;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts the pixels values into BYTE values.
	* @param pix Pixels values.
	* @return Valid BYTE values: integers [0..255]
	*/
	public static int[] getAllValidByte(int[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		int[] res = new int[pix.length];
		for (int i = 0; i < pix.length; i++) {
			if (pix[i] >= 255) res[i] = 255;
			else if (pix[i] <= 0) res[i] = 0;
				 else res[i] = pix[i];
		}
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts the pixel value into WORD value.
	* @param pix Pixel value.
	* @return Valid WORD value: integer [0..65535]
	*/
	public static int getValidWord(int pix) { 
		if (pix >= 65535) return 65535;
		else if (pix <= 0) return 0;
			 else return pix;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts the pixels values into WORD values.
	* @param pix Pixels values.
	* @return Valid WORD values: integers [0..65535]
	*/
	public static int[] getAllValidWord(int[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		int[] res = new int[pix.length];
		for (int i = 0; i < pix.length; i++) {
			if (pix[i] >= 65535) res[i] = 65535;
			else if (pix[i] <= 0) res[i] = 0;
				 else res[i] = pix[i];
		}
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts the pixel value into REAL value.
	* @param pix Pixel value.
	* @return Valid REAL value: float [0..1]
	*/
	public static float getValidReal(float pix) {
		if (pix >= 1) return 1;
		else if (pix <= 0) return 0;
			 else return pix;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts the pixels values into REAL values.
	* @param pix Pixels values.
	* @return Valid REAL values: float [0..1]
	*/
	public static float[] getAllValidReal(float[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		float[] res = new float[pix.length];
		for (int i = 0; i < pix.length; i++)
			res[i] = pix[i];
		return res;
	}

	//********************************************
	// Color Conversion Methods
	//********************************************

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Extracts Red value of the RGB Pixel.
	* @param rgb RGB value of the pixel.
	* @return RED value of the pixel.
	*/
	public static int getRedFromRGB(int rgb) {
		return (rgb >>> 16) & 0xFF;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Extracts Green value of the RGB Pixel.
	* @param rgb RGB value of the pixel.
	* @return GREEN value of the pixel.
	*/
	public static int getGreenFromRGB(int rgb) {
		return (rgb >>> 8) & 0xFF;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Extracts Blue value of the RGB Pixel.
	* @param rgb RGB value of the pixel.
	* @return BLUE value of the pixel.
	*/
	public static int getBlueFromRGB(int rgb) {
		return rgb & 0xFF;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Extracts Red values from a Vector of RGB pixels.
	* @param rgb Vector of RGB pixels.
	* @return RED values of the pixels.
	*/
	public static int[] getAllRedFromRGB(int[] rgb) {
		if (rgb == null || rgb.length == 0)
			return null;
		int[] res = new int[rgb.length];
		for (int i = 0; i < rgb.length; i++)
			res[i] = (rgb[i] >>> 16) & 0xFF;
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Extracts Green values from a Vector of RGB pixels.
	* @param rgb Vector of RGB pixels.
	* @return GREEN values of the pixels.
	*/
	public static int[] getAllGreenFromRGB(int[] rgb) {
		if (rgb == null || rgb.length == 0)
			return null;
		int[] res = new int[rgb.length];
		for (int i = 0; i < rgb.length; i++)
			res[i] = (rgb[i] >>> 8) & 0xFF;
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Extracts Blue values from a Vector of RGB pixels.
	* @param rgb Vector of RGB pixels.
	* @return BLUE values of the pixels.
	*/
	public static int[] getAllBlueFromRGB(int[] rgb) {
		if (rgb == null || rgb.length == 0)
			return null;
		int[] res = new int[rgb.length];
		for (int i = 0; i < rgb.length; i++)
			res[i] = (rgb[i]) & 0xFF;
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Calculates the RGB value from RED, GREEN and BLUE value.
	* @param red Red value of the pixel.
	* @param green value of the pixel.
	* @param blue value of the pixel.
	* @return RGB value of the pixel.
	*/
	public static int getRGB(int red, int green, int blue) {
		red = red & 0xFF;
		green = green & 0xFF;
		blue = blue & 0xFF;
		return (255 << 24) | (red << 16) | (green << 8) | blue;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Calculates the RGB values from RED, GREEN and BLUE values.
	* @param red Red values of the pixels.
	* @param green values of the pixels.
	* @param blue values of the pixels.
	* @return RGB values of the pixels.
	*/
	public static int[] getAllRGB(int[] red, int[] green, int[] blue) {
		if (red == null || green == null || blue ==null || 
			red.length == 0 || red.length != green.length ||
			red.length != blue.length)
			return null;
		int[] res = new int[red.length];
		for (int i = 0; i < red.length; i++)
			res[i] = (255 << 24)
			| ((red[i] & 0xFF) << 16)
			| ((green[i] & 0xFF) << 8)
			| (blue[i] & 0xFF);
		return res;
	}

	//****************************************************
	//USER -> BIT Conversion Methods
	//****************************************************

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts a BYTE pixel into a BIT pixel.
	* @param pix Value of the BYTE pixel.
	* @return BIT value: integer [0..1]
	*/
	public static int getBitFromByte(int pix) {
		int val = getValidByte(pix);
		return (val >>> 7) & 0x01;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts BYTE pixels into BIT pixels.
	* @param pix Values of the BYTE pixels.
	* @return BIT values: integers [0..1]
	*/
	public static int[] getAllBitFromByte(int[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		int[] res = new int[pix.length];
		int[] val = getAllValidByte(pix);
		for (int i = 0; i < pix.length; i++)
			res[i] = (val[i] >>> 7) & 0x01;
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts a WORD pixel into a BIT pixel.
	* @param pix Value of the WORD pixel.
	* @return BIT value: integer [0..1]
	*/
	public static int getBitFromWord(int pix) {
		int val = getValidWord(pix);
		return (val >>> 15) & 0x01;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts WORD pixels into BIT pixels.
	* @param pix Values of the WORD pixels.
	* @return BIT values: integers [0..1]
	*/
	public static int[] getAllBitFromWord(int[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		int[] res = new int[pix.length];
		int[] val = getAllValidWord(pix);
		for (int i = 0; i < pix.length; i++)
			res[i] = (val[i] >>> 15) & 0x01;
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts a REAL pixel into a BIT pixel.
	* @param pix Value of the REAL pixel.
	* @return BIT value: float [0..1]
	*/
	public static int getBitFromReal(float pix) {
		if (getValidReal(pix) >= 0.5) return 1;
		else return 0;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts REAL pixels into BIT pixels.
	* @param pix Values of the REAL pixels.
	* @return BIT values: floats [0..1]
	*/

	public static int[] getAllBitFromReal(float[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		int[] res = new int[pix.length];
		float[] val = getAllValidReal(pix);
		for (int i = 0; i < pix.length; i++) {
			if (val[i] >= 0.5) res[i] = 1;
			else res[i] = 0;
		}
		return res;
	}

	//*****************************************************
	//USER -> BYTE Conversion Methods
	//*****************************************************

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts a BIT pixel into a BYTE pixel.
	* @param pix Value of the BIT pixel.
	* @return BYTE value: integer [0..255]
	*/
	public static int getByteFromBit(int pix) {
		return (getValidBit(pix) == 1) ? 255 : 0;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts BIT pixels into BYTE pixels.
	* @param pix Values of the BIT pixels.
	* @return BYTE values: integers [0..255]
	*/
	public static int[] getAllByteFromBit(int[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		int[] res = new int[pix.length];
		int[] val = getAllValidBit(pix);
		for (int i = 0; i < pix.length; i++)
			res[i] = (val[i] == 1) ? 255 : 0;
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts a WORD pixel into a BYTE pixel.
	* @param pix Value of the WORD pixel.
	* @return BYTE value: integer [0..255]
	*/
	public static int getByteFromWord(int pix) {
		return getValidWord(pix) >>> 8;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts WORD pixels into BYTE pixels.
	* @param pix Values of the WORD pixels.
	* @return BYTE values: integers [0..255]
	*/
	public static int[] getAllByteFromWord(int[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		int[] res = new int[pix.length];
		int[] val = getAllValidWord(pix);
		for (int i = 0; i < pix.length; i++)
			res[i] = val[i] >>> 8;
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts a REAL pixel into a BYTE pixel.
	* @param pix Value of the REAL pixel.
	* @return BYTE value: integer [0..255]
	*/
	public static int getByteFromReal(float pix) {
		return (int) Math.round(getValidReal(pix) * 255.0);
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts REAL pixels into BYTE pixels.
	* @param pix Values of the REAL pixels.
	* @return BYTE values: integers [0..255]
	*/
	public static int[] getAllByteFromReal(float[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		int[] res = new int[pix.length];
		float[] val = getAllValidReal(pix);
		for (int i = 0; i < pix.length; i++)
			res[i] = (int) Math.round(Math.abs(val[i]) * 255.0);
		return res;
	}

	//*****************************************************
	//USER -> WORD Conversion Methods
	//*****************************************************

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts a BIT pixel into a WORD pixel.
	* @param pix Value of the BIT pixel.
	* @return WORD value: integer [0..65535]
	*/
	public static int getWordFromBit(int pix) {
		return (getValidBit(pix) == 1) ? 65535 : 0;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts BIT pixels into WORD pixels.
	* @param pix Values of the BIT pixels.
	* @return WORD values: integers [0..65535]
	*/
	public static int[] getAllWordFromBit(int[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		int[] res = new int[pix.length];
		int[] val = getAllValidBit(pix);
		for (int i = 0; i < pix.length; i++)
			res[i] = (val[i] == 1) ? 65535 : 0;
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts a BYTE pixel into a WORD pixel.
	* @param pix Value of the BYTE pixel.
	* @return WORD value: integer [0..65535]
	*/
	public static int getWordFromByte(int pix) {
		return getValidByte(pix) & 0xFF;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts BYTE pixels into WORD pixels.
	* @param pix Values of the BYTE pixels.
	* @return WORD values: integers [0..65535]
	*/
	public static int[] getAllWordFromByte(int[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		int[] res = new int[pix.length];
		int[] val = getAllValidByte(pix);
		for (int i = 0; i < pix.length; i++)
			res[i] = val[i] & 0xFF;
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts a REAL pixel into a WORD pixel.
	* @param pix Value of the REAL pixel.
	* @return WORD value: integer [0..65535]
	*/
	public static int getWordFromReal(float pix) {
		return (int) Math.round((double) getValidReal(pix) * 65535.0);
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts REAL pixels into WORD pixels.
	* @param pix Values of the REAL pixels.
	* @return WORD values: integers [0..65535]
	*/
	public static int[] getAllWordFromReal(float[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		int[] res = new int[pix.length];
		float[] val = getAllValidReal(pix);
		for (int i = 0; i < pix.length; i++)
			res[i] = (int) Math.round((double) val[i] * 65535.0);
		return res;
	}

	//*****************************************************
	//USER -> REAL Conversion Methods
	//*****************************************************

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts a BIT pixel into a REAL pixel.
	* @param pix Value of the BIT pixel.
	* @return REAL value: float [0..1]
	*/
	public static float getRealFromBit(int pix) {
		return (getValidBit(pix) == 1) ? 1.0f : 0.0f;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts BIT pixels into REAL pixels.
	* @param pix Values of the BIT pixels.
	* @return REAL values: floats [0..1]
	*/
	public static float[] getAllRealFromBit(int[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		float[] res = new float[pix.length];
		int[] val = getAllValidBit(pix);
		for (int i = 0; i < pix.length; i++)
			res[i] = (val[i] == 1) ? 1.0f : 0.0f;
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts a BYTE pixel into a REAL pixel.
	* @param pix Value of the BYTE pixel.
	* @return REAL value: float [0..1]
	*/
	public static float getRealFromByte(int pix) {
		return getValidByte(pix) / 255.0f;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts BYTE pixels into REAL pixels.
	* @param pix Values of the BYTE pixels.
	* @return REAL values: floats [0..1]
	*/
	public static float[] getAllRealFromByte(int[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		float[] res = new float[pix.length];
		int[] val = getAllValidByte(pix);
		for (int i = 0; i < pix.length; i++)
			res[i] = val[i] / 255.0f;
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts a WORD pixel into a REAL pixel.
	* @param pix Value of the WORD pixel.
	* @return REAL value: float [0..1]
	*/
	public static float getRealFromWord(int pix) {
		return getValidWord(pix) / 65535.0f;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts WORD pixels into REAL pixels.
	* @param pix Values of the WORD pixels.
	* @return REAL values: floats [0..1]
	*/
	public static float[] getAllRealFromWord(int[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		float[] res = new float[pix.length];
		int[] val = getAllValidWord(pix);
		for (int i = 0; i < pix.length; i++)
			res[i] = val[i] / 65535.0f;
		return res;
	}

	//*******************************************************************
	//USER -> INTERNAL Conversion Methods
	//*******************************************************************

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts an Integer pixel into a BIT pixel.
	* @param pix Integer value of the pixel.
	* @return BIT value of the pixel: integer [0..1].
	*/

	static boolean getBitFromInt(int pix) {
		return (getValidBit(pix) == 1);
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts Integers pixels into BIT pixels.
	* @param pix Integers values of the pixels.
	* @return BIT values of the pixels: integers [0..1].
	*/

	static boolean[] getAllBitFromInt(int[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		boolean[] res = new boolean[pix.length];
		int[] val = getAllValidBit(pix);
		for (int i = 0; i < pix.length; i++)
			res[i] = (val[i] == 1);
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts an Integer pixel into a BYTE pixel.
	* @param pix Integer value of the pixel.
	* @return BYTE value of the pixel: integer [0..255].
	*/

	static byte getByteFromInt(int pix) {
		return (byte) getValidByte(pix);
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts Integers pixels into BYTE pixels.
	* @param pix Integers values of the pixels.
	* @return BYTE values of the pixels: integers [0..255].
	*/

	static byte[] getAllByteFromInt(int[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		byte[] res = new byte[pix.length];
		int[] val = getAllValidByte(pix);
		for (int i = 0; i < pix.length; i++)
			res[i] = (byte) val[i];
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts an Integer pixel into a WORD pixel.
	* @param pix Integer value of the pixel.
	* @return WORD value of the pixel: integer [0..65535].
	*/

	static short getWordFromInt(int pix) {
		return (short) getValidWord(pix);
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts Integers pixels into WORD pixels.
	* @param pix Integers values of the pixels.
	* @return WORD values of the pixels: integers [0..65535].
	*/

	static short[] getAllWordFromInt(int[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		short[] res = new short[pix.length];
		int[] val = getAllValidWord(pix);
		for (int i = 0; i < pix.length; i++)
			res[i] = (short) val[i];
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts a Float pixel into a REAL pixel.
	* @param pix Float value of the pixel.
	* @return REAL value of the pixel: float [0..1].
	*/

	static float getRealFromFloat(float pix) {
		return getValidReal(pix);
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts Floats pixels into REAL pixelss.
	* @param pix Floats values of the pixels.
	* @return REAL values of the pixels: floats [0..1].
	*/

	static float[] getAllRealFromFloat(float[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		return getAllValidReal(pix);
	}

	//*******************************************************************
	//INTERNAL -> USER Conversion Methods
	//*******************************************************************

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts a BIT pixel into a Integer pixel.
	* @param pix BIT value of the pixel.
	* @return Integer value of the pixel.
	*/

	static int getIntFromBit(boolean pix) {
		return (pix) ? 1 : 0;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts BIT pixels into Integers pixels.
	* @param pix BIT values of the pixels.
	* @return Integers values of the pixels.
	*/

	static int[] getAllIntFromBit(boolean[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		int[] res = new int[pix.length];
		for (int i = 0; i < pix.length; i++)
			res[i] = (pix[i]) ? 1 : 0;
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts a BYTE pixel into a Integer pixel.
	* @param pix BYTE value of the pixel.
	* @return Integer value of the pixel.
	*/
	static int getIntFromByte(byte pix) {
		//return (pix < 0) ? (int) pix + 256 : (int) pix;
		int a= pix & 0xFF;
		return a; 
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts BYTE pixels into Integers pixels.
	* @param pix BYTE values of the pixels.
	* @return Integers values of the pixels.
	*/
	static int[] getAllIntFromByte (byte[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		int[] res = new int[pix.length];
		for (int i = 0; i < pix.length; i++)
			//res[i] = (pix[i] < 0) ? (int) pix[i] + 256 : (int) pix[i];
			res[i] = pix[i] & 0xFF;
		return res;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts a WORD pixel into a Integer pixel.
	* @param pix WORD value of the pixel.
	* @return Integer value of the pixel.
	*/

	static int getIntFromWord(short pix) {
		//return (pix < 0) ? (int) pix + 65536 : (int) pix;
		int a=pix & 0xFFFF;
		return a;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts WORD pixels into Integers pixels.
	* @param pix WORD values of the pixels.
	* @return Integers values of the pixels.
	*/

	static int[] getAllIntFromWord(short[] pix) {
		if (pix == null || pix.length == 0)
			return null;
		int[] res = new int[pix.length];
		for (int i = 0; i < pix.length; i++)
			res[i] = pix[i] & 0xFFFF;
		return res;
	}

	//************************************
	//INTERNAL --> RAW Conversion Methods
	//************************************

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts BIT pixels into RAW pixels (Array of bytes)
	* @param bmp BIT values of the pixels.
	* @return RAW values of the pixels
	*/

	static byte[] getRawFromBit(boolean[] bmp) {
		int bmpRAWlg = (int) Math.ceil((double) bmp.length / 8.0);
		byte[] bmpRAW = new byte[bmpRAWlg];

		for (int j = 0, i = 0; j < bmpRAWlg; j++) {
			byte mask = 0;
			for (int bit = 7; bit >= 0; bit--) {
				if (i < bmp.length && bmp[i])
					mask |= 1 << bit;
				i++;
			}
			bmpRAW[j] = mask;
		}
		return bmpRAW;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts BYTE pixels into RAW pixels (Array of bytes)
	* @param bmp BYTE values of the pixels.
	* @return RAW values of the pixels
	*/
	static byte[] getRawFromByte(byte[] bmp) {
		int bmpRAWlg = bmp.length;
		byte[] bmpRAW = new byte[bmpRAWlg];
		for (int i = 0; i < bmp.length; i++)
			bmpRAW[i] = bmp[i];
		return bmpRAW;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts SEGMENTS vectors into RAW vector (Array of bytes)
	* @param bmp Vector of SEGMENTS.
	* @return RAW values of the SEGMENTS
	*/
	static byte[] getRawFromSegment(Vector bmp) {
		int bmpRAWlg = bmp.size() * 2;
		byte[] bmpRAW = new byte[bmpRAWlg];
		for (int i = 0, j = 0; i < bmp.size(); i++, j += 2) {
			Integer dd = (Integer) bmp.elementAt(i);
			bmpRAW[j] = (byte) (dd.intValue() >>> 8);
			bmpRAW[j + 1] = (byte) (dd.intValue() & 0x00ff);
		}
		return bmpRAW;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts POINTS vectors into RAW vector (Array of bytes)
	* @param bmp Vector of POINTS.
	* @return RAW values of the POINTS
	*/
	static byte[] getRawFromPoint(Vector bmp) {
		int bmpRAWlg = bmp.size() * 2;
		byte[] bmpRAW = new byte[bmpRAWlg];
		for (int i = 0, j = 0; i < bmp.size(); i++, j += 2) {
			Integer dd = (Integer) bmp.elementAt(i);
			bmpRAW[j] = (byte) (dd.intValue() >>> 8);
			bmpRAW[j + 1] = (byte) (dd.intValue() & 0x00ff);
		}
		return bmpRAW;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts POLYGONS vectors into RAW vector (Array of bytes)
	* @param bmp Vector of POLYGONS.
	* @return RAW values of the POLYGONS
	*/

	static byte[] getRawFromPoly(Vector bmp) {
		int sizet = 0;
		for (int npol = 0; npol < bmp.size(); npol++)
			sizet += ((Vector) bmp.elementAt(npol)).size();
		sizet *= 2;
		int bmpRAWlg = sizet;
		byte[] bmpRAW = new byte[bmpRAWlg];
		for (int kj = 0, j = 0; kj < bmp.size(); kj++) {
			Vector vaux = new Vector((Vector) bmp.elementAt(kj));
			for (int i = 0; i < vaux.size(); i++, j += 2) {
				Integer dd = (Integer) vaux.elementAt(i);
				bmpRAW[j] = (byte) (dd.intValue() >>> 8);
				bmpRAW[j + 1] = (byte) (dd.intValue() & 0x00ff);
			}
		}
		return bmpRAW;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts EDGES vectors into RAW vector (Array of bytes)
	* @param bmp Vector of EDGES.
	* @return RAW values of the EDGES
	*/

	static byte[] getRawFromEdges(Vector bmp) {
		int sizet = 0;
		for (int npol = 0; npol < bmp.size(); npol++) {
			sizet += ((Vector) bmp.elementAt(npol)).size();
		}
		sizet *= 2;
		int bmpRAWlg = sizet;
		byte[] bmpRAW = new byte[bmpRAWlg];
		Vector vaux;
		for (int kj = 0, j = 0; kj < bmp.size(); kj++) {
			vaux = new Vector((Vector) bmp.elementAt(kj));
			for (int i = 0; i < vaux.size(); i++, j += 2) {
				Integer dd = (Integer) vaux.elementAt(i);
				bmpRAW[j] = (byte) (dd.intValue() >>> 8);
				bmpRAW[j + 1] = (byte) (dd.intValue() & 0x00ff);
			}
		}
		return bmpRAW;

	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts WORD pixels into RAW pixels (Array of bytes)
	* @param bmp WORD values of the pixels.
	* @return RAW values of the pixels
	*/
	static byte[] getRawFromWord(short[] bmp) {
		int bmpRAWlg = bmp.length * 2;
		byte[] bmpRAW = new byte[bmpRAWlg];
		for (int i = 0, j = 0; i < bmp.length; i++, j += 2) {
			bmpRAW[j] = (byte) (bmp[i] >>> 8);
			bmpRAW[j + 1] = (byte) (bmp[i] & 0x00ff);
		}
		return bmpRAW;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts REAL pixels into RAW pixels (Array of bytes)
	* @param bmp REAL values of the pixels.
	* @return RAW values of the pixels
	*/
	static byte[] getRawFromReal(float[] bmp) {
		int bmpRAWlg = bmp.length * 4;
		byte[] bmpRAW = new byte[bmpRAWlg];
		for (int i = 0, j = 0; i < bmp.length; i++, j += 4) {
			int val = Float.floatToIntBits(bmp[i]);
			bmpRAW[j] = (byte) (val >>> 24);
			bmpRAW[j + 1] = (byte) ((val >>> 16) & 0xff);
			bmpRAW[j + 2] = (byte) ((val >>> 8) & 0xff);
			bmpRAW[j + 3] = (byte) (val & 0xff);
		}
		return bmpRAW;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts WORD Vector into RAW Vector (Array of bytes)
	* @param bmp WORD Vector.
	* @return RAW Vector
	*/

	static byte[] getRawFromVectorWord(Vector bmp) {
		short[] aux = new short[bmp.size()];
		for (int k = 0; k < bmp.size(); k++) {
			aux[k] = (short) ((Integer) (bmp.elementAt(k))).intValue();
		}
		return getRawFromWord(aux);
	}

	//************************************
	//RAW --> INTERNAL Conversion Methods
	//************************************

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts RAW pixels into BIT Pixels
	* @param bmpRAW RAW Values of the pixels
	* @param npixels Number of pixels in bmpRAW
	* @return BIT Values of the pixels
	*/

	static boolean[] getBitFromRaw(byte[] bmpRAW, int npixels) {
		boolean[] bmp = new boolean[npixels];
		for (int i = 0, j = 0; i < bmpRAW.length; i++) {
			for (int bit = 7; bit >= 0; bit--) {
				if (j < npixels) {
					if ((bmpRAW[i] & (1 << bit)) != 0)
						bmp[j] = true;
					else
						bmp[j] = false;
				}
				j++;
			}
		}
		return bmp;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts RAW pixels into BYTE Pixels
	* @param bmpRAW RAW Values of the pixels
	* @return BYTE Values of the pixels
	*/

	static byte[] getByteFromRaw(byte[] bmpRAW) {
		int npixels = bmpRAW.length;
		byte[] bmp = new byte[npixels];
		for (int i = 0; i < npixels; i++)
			bmp[i] = bmpRAW[i];
		return bmp;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts RAW pixels into WORD Pixels
	* @param bmpRAW RAW Values of the pixels
	* @return WORD Values of the pixels
	*/

	static short[] getWordFromRaw(byte[] bmpRAW) {
		int npixels = bmpRAW.length / 2;
		short[] bmp = new short[npixels];
		for (int i = 0, j = 0; i < npixels; i++) {
			bmp[i] = (short) ((bmpRAW[j] << 8) | (bmpRAW[j + 1]) & 0x00ff);
			j += 2;
		}
		return bmp;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
	*<FONT COLOR="blue">
	* Converts RAW pixels into REAL Pixels
	* @param bmpRAW RAW Values of the pixels
	* @return REAL Values of the pixels
	*/

	static float[] getRealFromRaw(byte[] bmpRAW) {
		int npixels = bmpRAW.length / 4;
		float[] bmp = new float[npixels];
		for (int i = 0, j = 0; i < npixels; i++) {
			int b0 = (bmpRAW[j] < 0) ? (int) bmpRAW[j] + 256 : (int) bmpRAW[j];
			int b1 =
				(bmpRAW[j + 1] < 0)
					? (int) bmpRAW[j + 1] + 256
					: (int) bmpRAW[j + 1];
			int b2 =
				(bmpRAW[j + 2] < 0)
					? (int) bmpRAW[j + 2] + 256
					: (int) bmpRAW[j + 2];
			int b3 =
				(bmpRAW[j + 3] < 0)
					? (int) bmpRAW[j + 3] + 256
					: (int) bmpRAW[j + 3];
			int val = (b0 << 24) | (b1 << 16) | (b2 << 8) | (b3);
			bmp[i] = Float.intBitsToFloat(val);
			j += 4;
		}
		return bmp;
	}
}
