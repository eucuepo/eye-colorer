package com.eyecolorer.image.toolkit;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.util.Vector;
import java.util.zip.*;

import com.sun.image.codec.jpeg.*;

/**<P><FONT COLOR="red">
 *<B>Description:</B><BR>
 *<FONT COLOR="blue">
 * Class to define basic methods to get and load AWT and JIP images.
 * AWT images can be got with JIP images or with GIF o JPEG file directly.
 * JIP images can get with AWT images or it can be a directly sequence of JIP files.
 * We can load images and JIP sequences in JIP files or in TGA (Targa) format files. 
 */ 
public final class JIPToolkit {

	static final Toolkit toolkit = Toolkit.getDefaultToolkit();

	/**<P><FONT COLOR="red">
	 *<B>Description:</B><BR>
	 *<FONT COLOR="blue">	 
	 * Empty constructor of the class 
	 */
	public JIPToolkit() {
	}

	/***********************************************/
	/* Methods to get an AWT image				   */
	/***********************************************/

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
			 *<FONT COLOR="blue">
	 * It gets an AWT image object from a band of JIP image.
	 * @param img Image JIP source.
	 * @param b img band which bitmap will be change to AWT image.
	 * (0 <= b <= numbandas-1)
	 * @return AWT image result. AWT images are usually representing like color image
	 * , however, a band does not have color so the colors of a AWT image will be grey.
	 */
	public static Image getAWTImage(JIPImage img, int b) {
		if (img == null)
			return null;
		Image res = null;
		if (b >= 0 && b < img.nbands) {
			int npixels = img.width * img.height;
			int[] pix = new int[npixels];
			switch (img.type) {
				case JIP.tBIT :
					pix =
						JIP.getAllByteFromBit(
							JIP.getAllIntFromBit(img.bmp[b].bmpBit));
					break;
				case JIP.tBYTE :
				case JIP.tCOLOR :
					pix = JIP.getAllIntFromByte(img.bmp[b].bmpByte);
					break;
				case JIP.tWORD :
					pix =
						JIP.getAllByteFromWord(
							JIP.getAllIntFromWord(img.bmp[b].bmpWord));
					break;
				case JIP.tREAL :
					pix = JIP.getAllByteFromReal(img.bmp[b].bmpReal);
					break;
				case JIP.tPOLY :
					for (int i = 0; i < npixels; i++)
						pix[i] = 0;
					break;
				case JIP.tPOINT :
					for (int i = 0; i < npixels; i++)
						pix[i] = 0;
					break;
				case JIP.tEDGES :
					for (int i = 0; i < npixels; i++)
						pix[i] = 0;
					break;
				case JIP.tSEGMENT :
					for (int i = 0; i < npixels; i++)
						pix[i] = 0;
					break;

			}
			for (int i = 0; i < npixels; i++) {
				int val = pix[i] & 0xFF;
				pix[i] = (0xFF << 24) | (val << 16) | (val << 8) | val;
			}

			res = toolkit.createImage(
					new MemoryImageSource(img.width, img.height,
						pix, 0, img.width));
		}

		return res;
	}

	/**<P><FONT COLOR="red">
	*<B>Description:</B><BR>
	*<FONT COLOR="blue">	
	* It gets an AWT image object from a JIP image.	
	* If the JIP image is not tCOLOR type then it will be get the image of the 0 band.	
	* If the JIP image is a color image then it will be a calor image.
	* @param img JIP image source.
	* @return AWT imagen result.
	* If JIP image is not in color we will be get the 0 band image. So, 
	* although the AWT image is in color, it have a grey scale.	
	* If JIP image is in color new image is belong with it.
	* @see JIPToolkit#getAWTImage(jip.JIPImage, int)
	*/
	public static Image getAWTImage(JIPImage img) {
		if (img.type != JIP.tCOLOR)
			return getAWTImage(img, 0);
		else {
			int[] pix = img.getAllPixel();
			Image res = toolkit.createImage(
					new MemoryImageSource(img.width, img.height,
						pix, 0, img.width));
			return res;
		}
	}


	/**<P><FONT COLOR="red">
	*<B>Description:</B><BR>
	*<FONT COLOR="blue">	
	* It gets an AWT image object from a GIF or JPEG file. 
	* This method is blocked and it do not return the control until the new 
	* image is formed completely.
	* It is better that you use it before the load methods that they are not
	* blocked AWT, for example, getImage() to avoid the asynchronous protocol
	* based on ImageObserver interface of AWT which assure that the values that
	* we are accessed are correct.
	* Beside, we do not use the MediaTracker object to wait for the load of the 
	* image so this method uses MediaTracker internally.
	* @param file Source file (It should be GIF or JPEG)
	* @return AWT image result. (if can not load the image return null)
	*/
	public static Image getAWTImage(String file) {
		Image res = toolkit.getImage(file);
		if (res == null)
			return null;
		MediaTracker mt = new MediaTracker(new Canvas());
		mt.addImage(res, 0);
		try {
			mt.waitForID(0);
		} catch (InterruptedException e) {
			return null;
		}
		if (mt.isErrorID(0))
			return null;
		else
			return res;
	}

	/***********************************************/
	/* Metodos para la obtencion de una Imagen JIP */
	/***********************************************/

	/**<P><FONT COLOR="red">
	 *<B>Description:</B><BR>
	 *<FONT COLOR="blue">	 
	 * It gets a JIP image object that its type is tCOLOR from a AWT image.
	 * @param img AWT image source (It should be loaded completelly,
	 * this is assured with getAWTImage(String fich).
	 * @see JIPToolkit#getAWTImage(java.lang.String)
	 * @return JIP image result (tCOLOR type).
	 */
	public static JIPImage getColorImage(Image img) {
		Canvas canv = new Canvas();
		int w = img.getWidth(canv);
		int h = img.getHeight(canv);
		if (w <= 0 || h <= 0)
			return null;
		int[] pix = new int[w * h];
		PixelGrabber pg = new PixelGrabber(img, 0, 0, w, h, pix, 0, w);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			return null;
		}

		return new JIPImage(w, h, JIP.tCOLOR, pix);
	}

	/**************************************/
	/* Metodos para salvar una imagen JIP */
	/**************************************/

	/**<P><FONT COLOR="red">
	 *<B>description:</B><BR>
	 *<FONT COLOR="blue">
	 * It saves a JIP image object in a JIP format file.
	 * The image is saved as a sequence with a frame only which contains it.	 
	 * @param img Image to save.
	 * @param file File where the image is loaded (if it does not exist then it is creatted,
	 * else it is rewritten)
	 */
	public static void saveImageIntoFile(JIPImage img, String file) {
		saveSeqIntoFile(new JIPSequence(img), file);
	}

	/**<P><FONT COLOR="red">
	 *<B>description:</B><BR>
	 *<FONT COLOR="blue">	 
	 * It saves a JIP image object in a TGA (Targa) format file.
	 * If the image has some grey bands, then is generated a file to band, 
	 * it is added in the name (before the exptension), the suffix "_b0", "_b1", ...
	 * depend of the corresponding band. 
	 * @param img Imagen to save.
	 * @param file File where the image is loaded (if it do not exist then it is creatted,
	 * else it is rewritten)
	 */
	public static void saveImageIntoTga(JIPImage img, String file) {
		//Streams para escribir en el fichero de salida
		FileOutputStream fs = null;
		BufferedOutputStream bfs = null;
		DataOutputStream dat = null;

		int numfiles = 1;
		boolean color = (img.type == JIP.tCOLOR);
		if (!color && img.nbands > 1)
			numfiles = img.nbands;

		//Obtiene nombre del fichero y extension
		String name = null;
		String ext = null;
		if (file.lastIndexOf(".") == -1) {
			name = file;
			ext = ".tga";
		} else {
			name = file.substring(0, file.lastIndexOf("."));
			ext = file.substring(file.lastIndexOf("."));
		}

		int w = img.width;
		int h = img.height;
		int pix = 0;

		// Generamos cada uno de los ficheros necesarios
		for (int nf = 0; nf < numfiles; nf++) {
			try {
				if (numfiles > 1)
					fs = new FileOutputStream(name + "_b" + nf + ext);
				else
					fs = new FileOutputStream(name + ext);
				bfs = new BufferedOutputStream(fs, 4096);
				dat = new DataOutputStream(bfs);

				//Escribe la cabecera
				for (int i = 0; i < 12; i++)
					dat.writeByte((i == 2) ? 2 : 0);
				dat.writeByte(w & 0xff);
				dat.writeByte((w >> 8) & 0xff);
				dat.writeByte(h & 0xff);
				dat.writeByte((h >> 8) & 0xff);
				dat.writeByte(24);
				dat.writeByte(0x20);

				//Escribe los datos
				for (int i = 0; i < h; i++)
					for (int j = 0; j < w; j++) {
						if (color) {
							dat.writeByte(img.getPixel(JIP.bBLUE, j, i) & 0xff);
							dat.writeByte(
								img.getPixel(JIP.bGREEN, j, i) & 0xff);
							dat.writeByte(img.getPixel(JIP.bRED, j, i) & 0xff);
						} else {
							switch (img.type) {
								case JIP.tBIT :
									pix =
										JIP.getByteFromBit(
											img.getPixel(nf, j, i));
									break;
								case JIP.tBYTE :
									pix = img.getPixel(nf, j, i);
									break;
								case JIP.tWORD :
									pix =
										JIP.getByteFromWord(
											img.getPixel(nf, j, i));
									break;
								case JIP.tREAL :
									pix =
										JIP.getByteFromReal(
											img.getPixelFlo(nf, j, i));
									break;
							}

							pix &= 0xff;
							dat.writeByte(pix);
							dat.writeByte(pix);
							dat.writeByte(pix);
						}
					}
				dat.flush();
			} catch (Exception e) {
				System.err.println(e);
			}
			finally {
				try {
					dat.close();
					bfs.close();
					fs.close();
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		}
	}

	/********************************************/
	/* Metodos para interacci�n con MatLab */
	/********************************************/

	/**<P><FONT COLOR="red">
	 *<B>description:</B><BR>
	 *<FONT COLOR="blue">	 
	 * It returns a (float) image to can be used by MatLab.
	 * @param seq JIP sequence that has the image to extract.
	 * @param f Frame to extract.
	 * @param b Band to extract.
	 * @return Image in array form.
	 */
	public static float [][] getImgMatlab (JIPSequence seq, int f, int b) {		
		int rows=seq.getFrame(f).getHeight();
		int cols=seq.getFrame(f).getWidth();
		float [][]out=new float [rows][];
		
		if (seq.getFrame(f).getType()==JIP.tREAL) {
			float []bmp=seq.getFrame(f).getAllPixelFlo(b);
			for (int r=0; r<rows; r++) {
				out[r]=new float[cols];
				for (int c=0; c<cols; c++) {
					out[r][c]=bmp[r*cols+c];
				}
			}
		}
		else { 
			int []bmp=seq.getFrame(f).getAllPixel(b);
			for (int r=0; r<rows; r++) {
				out[r]=new float[cols];
				for (int c=0; c<cols; c++) {
					out[r][c]=bmp[r*cols+c];
				}
			}
		}
		
		return out;
	}

	/**<P><FONT COLOR="red">
	 *<B>Description:</B><BR>
	 *<FONT COLOR="blue">
	 * It returns a (float) image to can be used by MatLab.
	 * @param img JIP imagen that has the image to extract.
	 * @param b Band to extract.
	 * @return Image in array form.
	 */
	public static float [][] getImgMatlab (JIPImage img, int b) {		
		int rows=img.getHeight();
		int cols=img.getWidth();
		float [][]out=new float [rows][];
		
		if (img.getType()==JIP.tREAL) {
			float []bmp=img.getAllPixelFlo(b);
			for (int r=0; r<rows; r++) {
				out[r]=new float[cols];
				for (int c=0; c<cols; c++) {
					out[r][c]=bmp[r*cols+c];
				}
			}
		}
		else { 
			int []bmp=img.getAllPixel(b);
			for (int r=0; r<rows; r++) {
				out[r]=new float[cols];
				for (int c=0; c<cols; c++) {
					out[r][c]=bmp[r*cols+c];
				}
			}
		}
		
		return out;
	}

	/**<P><FONT COLOR="red">
	 *<B>Description:</B><BR>
	 *<FONT COLOR="blue">
	 * It returns a JIP image from a matrix.	 
	 * @param img Matrix to assign.
	 * @param type Image type to create.
	 * @return JIP format image
	 */
	public static JIPImage setMatrix (float [][]img, int type) {
		int rows=img.length, cols=img[0].length;
		
		if (type==JIP.tREAL) {
			float []aux=new float[rows*cols];
			JIPImage res=new JIPImage(cols, rows, JIP.tREAL);
			for (int r=0; r<rows; r++) {
				for (int c=0; c<cols; c++) {
					aux[r*cols+c]=img[r][c];
				}
			}
			res.setAllPixel(aux);
			return res;
		}
		else {
			int []aux=new int[rows*cols];
			JIPImage res=new JIPImage(cols, rows, type);
			for (int r=0; r<rows; r++) {
				for (int c=0; c<cols; c++) {
					aux[r*cols+c]=(int)img[r][c];
				}
			}
			res.setAllPixel(aux);
			return res;
		}
	}

	/**<P><FONT COLOR="red">
	 *<B>Description:</B><BR>
	 *<FONT COLOR="blue">	 
	 * It returns a color JIP image from 3 bands.
	 * @param red Matrix which represents red.
	 * @param green Matrix which represents green.
	 * @param blue Matrix which represents blue.
	 * @return JIP format image
	 */
	public static JIPImage setMatrix (float [][]red, float [][]green, float [][]blue) {
		int rows=red.length, cols=red[0].length;
		
		int []auxr=new int[rows*cols];
		int []auxg=new int[rows*cols];
		int []auxb=new int[rows*cols];
		JIPImage res=new JIPImage(cols, rows, JIP.tCOLOR);
		for (int r=0; r<rows; r++) {
			for (int c=0; c<cols; c++) {
				auxr[r*cols+c]=(int)red[r][c];
				auxg[r*cols+c]=(int)green[r][c];
				auxb[r*cols+c]=(int)blue[r][c];
			}
		}
		res.setAllPixel(JIP.bRED, auxr);
		res.setAllPixel(JIP.bGREEN, auxg);
		res.setAllPixel(JIP.bBLUE, auxb);
		return res;
	}


	/******************************************/
	/* Method to load a sequence			  */	
	/******************************************/

	/**<P><FONT COLOR="red">
	 *<B>Description:</B><BR>
	 *<FONT COLOR="blue">	 
	 * It gets a sequence JIP object from a JIP file.
	 * @param file Name of the file.
	 * @return JIP sequence. (if can not load the secuence return null)
	 */
	public static JIPSequence getSeqFromFile(String file) {
		JIPSequence res = new JIPSequence();
		JIPImage[] imgs = null;
		int[] imgsizes = null;
		byte[] raw;

		//Streams para leer del fichero
		FileInputStream fs = null;
		BufferedInputStream bfs = null;
		DataInputStream dat = null;
		BufferedReader cab = null;

		String line;
		int index, index1, i, j, nframes, sz;
		int w, h, b, t;
		int auxmio;
		Vector arbPoli = new Vector();
		Vector arbEdge = new Vector();
		String name, tip;

		try {

			/* LECTURA DE LA CABECERA */

			//Abre el stream para leer la cabecera
			cab = new BufferedReader(new FileReader(file));

			cab.readLine(); //Linea 1 "JIP\n"
			cab.readLine(); //Linea 2 "\n"

			//Linea 3 "Sequence:<name>\n"
			line = cab.readLine();
			res.name = line.substring(9).trim();

			//Linea 4 "Frames:<nframes>\n"
			line = cab.readLine();
			nframes = Integer.parseInt(line.substring(7).trim());

			line = cab.readLine(); //Linea 5 "\n"

			//Crea el vector de imagenes
			imgs = new JIPImage[nframes];
			imgsizes = new int[nframes];

			//Para cada frame de la cabecera
			for (i = 0; i < nframes; i++) {

				//Nombre de la imagen
				line = cab.readLine();
				index = line.indexOf(":") + 1;
				name = line.substring(index).trim();

				//Anchura y altura de la imagen
				line = cab.readLine();
				index = line.indexOf(":") + 1;
				index1 = line.lastIndexOf("x");
				w = Integer.parseInt(line.substring(index, index1).trim());
				h = Integer.parseInt(line.substring(index1 + 1).trim());

				//Tipo de la imagen
				line = cab.readLine();
				index = line.indexOf(":") + 1;
				tip = line.substring(index).trim();
				t = JIP.tBIT;
				if (tip.compareTo("BIT") == 0)
					t = JIP.tBIT;
				else if (tip.compareTo("BYTE") == 0)
					t = JIP.tBYTE;
				else if (tip.compareTo("WORD") == 0)
					t = JIP.tWORD;
				else if (tip.compareTo("REAL") == 0)
					t = JIP.tREAL;
				else if (tip.compareTo("COLOR") == 0)
					t = JIP.tCOLOR;
				else if (tip.compareTo("POINT") == 0)
					t = JIP.tPOINT;
				else if (tip.compareTo("SEGMENT") == 0)
					t = JIP.tSEGMENT;
				else if (tip.compareTo("POLY") == 0)
					t = JIP.tPOLY;
				else if (tip.compareTo("EDGES") == 0)
					t = JIP.tEDGES;

				//Bandas de la imagen
				line = cab.readLine();
				index = line.indexOf(":") + 1;
				b = Integer.parseInt(line.substring(index).trim());

				//Bytes de la imagen
				if (t != JIP.tPOLY && t != JIP.tEDGES) {
					line = cab.readLine();
					index = line.indexOf(":") + 1;
					index1 = line.lastIndexOf(" ");
					imgsizes[i] =
						Integer.parseInt(line.substring(index, index1).trim());
					arbPoli.addElement(null);
					arbEdge.addElement(null);

				} else {
					Vector tamPoly = new Vector();

					while (true) {
						line = cab.readLine();
						index = line.indexOf(":") + 1;
						if (line.compareTo("\tend") == 0)
							break;
						index1 = line.lastIndexOf(" ");
						auxmio =
							Integer.parseInt(
								line.substring(index, index1).trim());
						Integer auxmio2 = new Integer(auxmio);
						tamPoly.addElement(auxmio2);
					}

					arbPoli.addElement((Vector) (tamPoly));
					arbEdge.addElement((Vector) (tamPoly));

					imgsizes[i] = 0;
					for (int kl = 0; kl < tamPoly.size(); kl++) {
						imgsizes[i]
							+= ((Integer) tamPoly.elementAt(kl)).intValue();
					}
				}

				//Linea "\n"
				cab.readLine();

				//Crea la imagen totalmente vacia
				imgs[i] = new JIPImage();

				//Asigna los datos que se conocen 
				imgs[i].width = w;
				imgs[i].height = h;
				imgs[i].type = t;
				imgs[i].nbands = b;
				imgs[i].name = name;

			}

			//Ultima linea de cabecera "RAW\n"
			cab.readLine();

			cab.close();

			/* LECTURA DE DATOS */

			//Abre el fichero de entrada con buffering de 4K
			fs = new FileInputStream(file);
			bfs = new BufferedInputStream(fs, 4096);
			dat = new DataInputStream(bfs);

			//Busca la marca de comienzo de datos
			while (dat.readUnsignedByte() != 255);

			//Para cada frame de datos
			for (i = 0; i < nframes; i++) {
				w = imgs[i].width;
				h = imgs[i].height;
				t = imgs[i].type;
				b = imgs[i].nbands;
				sz = imgsizes[i] / b;

				imgs[i].bmp = new JIPBitmap[b];

				//Para cada banda lee y asigna el bitmap
				for (j = 0; j < b; j++) {
					raw = new byte[sz];
					dat.readFully(raw);
					switch (t) {
						case JIP.tBIT :
							imgs[i].bmp[j] =
								new JIPBitmap(
									w,
									h,
									JIP.getBitFromRaw(raw, w * h));
							break;
						case JIP.tBYTE :
						case JIP.tCOLOR :
							imgs[i].bmp[j] =
								new JIPBitmap(w, h, JIP.getByteFromRaw(raw));
							break;
						case JIP.tWORD :
							imgs[i].bmp[j] =
								new JIPBitmap(w, h, JIP.getWordFromRaw(raw));
							break;
						case JIP.tREAL :
							imgs[i].bmp[j] =
								new JIPBitmap(w, h, JIP.getRealFromRaw(raw));
							break;
						case JIP.tPOINT :
							Vector puntos = new Vector();
							{
								int npixels = raw.length / 2;
								short[] bmp = new short[npixels];
								for (int i1 = 0, j1 = 0; i1 < npixels; i1++) {
									bmp[i1] =
										(short) ((raw[j1] << 8)
											| (raw[j1 + 1])
											& 0x00ff);
									j1 += 2;
								}
								for (int i2 = 0; i2 < npixels; i2++) {
									Integer datap = new Integer((int) bmp[i2]);
									puntos.addElement(datap);
								}
							}
							imgs[i] = new JIPImage(w, h, t, puntos);
							break;
						case JIP.tSEGMENT :
							Vector segmentos = new Vector();
							{
								int npixels = raw.length / 2;
								short[] bmp = new short[npixels];
								for (int i1 = 0, j1 = 0; i1 < npixels; i1++) {
									bmp[i1] =
										(short) ((raw[j1] << 8)
											| (raw[j1 + 1])
											& 0x00ff);
									j1 += 2;
								}
								for (int i2 = 0; i2 < npixels; i2++) {
									Integer datas = new Integer((int) bmp[i2]);
									segmentos.addElement(datas);
								}
							}
							imgs[i] = new JIPImage(w, h, t, segmentos);
							break;
						case JIP.tPOLY :
							Vector tPoligonos = new Vector();
							Vector poligonos = new Vector();
							{
								int npixels = raw.length / 2;

								short[] bmp = new short[npixels];
								for (int i1 = 0, j1 = 0; i1 < npixels; i1++) {
									bmp[i1] =
										(short) ((raw[j1] << 8)
											| (raw[j1 + 1])
											& 0x00ff);
									j1 += 2;
								}
								for (int i2 = 0; i2 < npixels; i2++) {
									Integer datas = new Integer((int) bmp[i2]);
									tPoligonos.addElement(datas);
								}

								Vector numPoli =
									new Vector((Vector) (arbPoli.elementAt(i)));
								int poligonosPorFrame = numPoli.size();

								for (int i2 = 0, elem = 0;
									i2 < poligonosPorFrame;
									i2++) {
									Vector polaux = new Vector();

									for (int i3 = 0;
										i3
											< (((Integer) (numPoli
												.elementAt(i2)))
												.intValue())
												/ 2;
										i3++, elem++) {
										polaux.addElement(
											(Integer) (tPoligonos
												.elementAt(elem)));
									}
									poligonos.addElement(
										(Vector) (polaux).clone());
								}
							}

							imgs[i] = new JIPImage(w, h, t, poligonos);

							break;
						case JIP.tEDGES :
							Vector tEdges = new Vector();
							Vector edges = new Vector();
							{
								int npixels = raw.length / 2;

								short[] bmp = new short[npixels];
								for (int i1 = 0, j1 = 0; i1 < npixels; i1++) {
									bmp[i1] =
										(short) ((raw[j1] << 8)
											| (raw[j1 + 1])
											& 0x00ff);
									j1 += 2;
								}
								for (int i2 = 0; i2 < npixels; i2++) {
									Integer datas = new Integer((int) bmp[i2]);
									tEdges.addElement(datas);
								}

								Vector numEdge =
									new Vector((Vector) (arbEdge.elementAt(i)));
								int edgesPorFrame = numEdge.size();

								for (int i2 = 0, elem = 0;
									i2 < edgesPorFrame;
									i2++) {
									Vector polaux = new Vector();

									for (int i3 = 0;
										i3
											< (((Integer) (numEdge
												.elementAt(i2)))
												.intValue())
												/ 2;
										i3++, elem++) {
										polaux.addElement(
											(Integer) (tEdges.elementAt(elem)));
									}
									edges.addElement((Vector) (polaux));
								}
							}
							imgs[i] = new JIPImage(w, h, t, edges);
							break;
					}
				}
				res.addFrame(imgs[i]);
			}

		} catch (Exception e) {
			return null;
		}
		finally {
			try {
				dat.close();
				bfs.close();
				fs.close();
			} catch (Exception e) {
				return null;
			}
		}

		return res;
	}

	/**************************************/	
	/* Methods to save a sequence		  */
	/**************************************/

	/**<P><FONT COLOR="red">
	 *<B>Description:</B><BR>
	 *<FONT COLOR="blue">	 
	 * It saves a JIP sequence object in a JIP format file.
	 * @param seq Secuence to save.
	 * @param dir Directory where save it.
	 * @param file File where the sequence is loaded (if it does not exist then it is created,
	 * else it is rewritten)
	 */
	public static void saveSeqIntoFile(
		JIPSequence seq,
		String dir,
		String file) {
		saveSeqIntoFile(seq, dir + file);
	}

	/**<P><FONT COLOR="red">
	 *<B>Description:</B><BR>
	 *<FONT COLOR="blue">	 
	 * It saves a JIP sequence object in a JIP format file.
	 * @param seq Sequence to save.
	 * @param file File where the sequence is loaded (if it does not exist then it is created,
	 * else it is rewritten)
	 */
	public static void saveSeqIntoFile(JIPSequence seq, String file) {
		JIPImage img;
		byte[] raw;

		//Streams para escribir en el fichero
		FileOutputStream fs = null;
		BufferedOutputStream bfs = null;
		DataOutputStream dat = null;
		BufferedWriter cab = null;

		String line;
		int i, j, nframes, sz;
		int w, h, b, t;
		String name, tip;

		try {

			//Abre el fichero de salida con buffering de 4K
			fs = new FileOutputStream(file);
			bfs = new BufferedOutputStream(fs, 4096);
			dat = new DataOutputStream(bfs);
			cab = new BufferedWriter(new OutputStreamWriter(dat));

			/* ESCRITURA DE LA CABECERA */

			//Linea 1 "JIP\n"
			line = "JIP";
			cab.write(line, 0, line.length());
			cab.newLine();

			//Linea 2 "\n"
			cab.newLine();

			//Linea 3 "Sequence:<name>\n"
			line = "Sequence:" + seq.name;
			cab.write(line, 0, line.length());
			cab.newLine();

			//Linea 4 "Frames:<nframes>\n"
			nframes = seq.nframes;
			line = "Frames:" + nframes;
			cab.write(line, 0, line.length());
			cab.newLine();

			//Linea 5 "\n"
			cab.newLine();

			//Para cada frame de la secuencia
			for (i = 0; i < nframes; i++) {

				//Obtiene datos de la imagen del frame
				img = seq.getFrame(i);
				t = img.type;
				Vector geoData;
				tip = "";
				name = "";
				w = 0;
				h = 0;
				b = 0;
				sz = 0;

				switch (t) {
					case JIP.tBIT :
						w = img.width;
						h = img.height;
						b = img.nbands;
						name = img.name;
						tip = "BIT";
						sz = ((int) Math.ceil((double) (w * h) / 8.0)) * b;
						break;
					case JIP.tBYTE :
						w = img.width;
						h = img.height;
						b = img.nbands;
						name = img.name;
						tip = "BYTE";
						sz = w * h * b;
						break;
					case JIP.tCOLOR :
						w = img.width;
						h = img.height;
						b = img.nbands;
						name = img.name;
						tip = "COLOR";
						sz = w * h * b;
						break;
					case JIP.tWORD :
						w = img.width;
						h = img.height;
						b = img.nbands;
						name = img.name;
						tip = "WORD";
						sz = w * h * 2 * b;
						break;
					case JIP.tREAL :
						w = img.width;
						h = img.height;
						b = img.nbands;
						name = img.name;
						tip = "REAL";
						sz = w * h * 4 * b;
						break;
					case JIP.tPOINT :
						w = img.width;
						h = img.height;
						b = img.nbands;
						name = img.name;
						tip = "POINT";
						sz = img.getVector().size();
						sz *= 2;
						break;
					case JIP.tSEGMENT :
						w = img.width;
						h = img.height;
						b = img.nbands;
						name = img.name;
						tip = "SEGMENT";
						sz = img.getVector().size();
						sz *= 2;
						break;
					case JIP.tPOLY :
						w = img.width;
						h = img.height;
						b = img.nbands;
						name = img.name;
						tip = "POLY";
						sz = 0;
						Vector vaux1;
						geoData = new Vector(img.getVector());
						for (int a = 0; a < geoData.size(); a++) {
							vaux1 = new Vector((Vector) geoData.elementAt(a));
							sz += vaux1.size();
						}
						sz *= 2;
						break;
					case JIP.tEDGES :
						w = img.width;
						h = img.height;
						b = img.nbands;
						name = img.name;
						tip = "EDGES";
						sz = 0;
						Vector vaux2;
						geoData = new Vector(img.getVector());
						for (int a = 0; a < geoData.size(); a++) {
							vaux2 = new Vector((Vector) geoData.elementAt(a));
							sz += vaux2.size();
						}
						sz *= 2;
						break;
				}

				//Nombre de la imagen
				line = "Frame " + i + ":" + name;
				cab.write(line, 0, line.length());
				cab.newLine();

				//Anchura y altura de la imagen
				line =
					"\tPixels:" + String.valueOf(w) + "x" + String.valueOf(h);
				cab.write(line, 0, line.length());
				cab.newLine();

				//Tipo de la imagen
				line = "\tType:" + tip;
				cab.write(line, 0, line.length());
				cab.newLine();

				//Bandas de la imagen
				line = "\tBands:" + String.valueOf(b);
				cab.write(line, 0, line.length());
				cab.newLine();

				//Bytes de la imagen
				if (t != JIP.tPOLY && t != JIP.tEDGES) {
					line = "\tSize:" + String.valueOf(sz) + " bytes";
					cab.write(line, 0, line.length());
					cab.newLine();
				} else {
					sz = 0;
					Vector vaux1;
					geoData = new Vector(img.getVector());
					for (int a = 0; a < geoData.size(); a++) {
						vaux1 = new Vector((Vector) geoData.elementAt(a));
						line =
							"\tSize:"
								+ String.valueOf(vaux1.size() * 2)
								+ " bytes";
						cab.write(line, 0, line.length());
						cab.newLine();
					}
					line = "\t" + "end";
					cab.write(line, 0, line.length());
					cab.newLine();

				}
				//Linea "\n"
				cab.newLine();

			}

			//Ultima linea de cabecera "RAW\n"
			line = "RAW";
			cab.write(line, 0, line.length());
			cab.newLine();

			//Vuelca la cabecera
			cab.flush();

			/* ESCRITURA DE DATOS */

			//Marca de comienzo de datos RAW
			dat.writeByte(255);

			//Para cada frame de la secuencia
			for (i = 0; i < nframes; i++) {

				//Obtiene datos de la imagen del frame
				img = seq.getFrame(i);
				b = img.nbands;
				t = img.type;

				//Para cada banda del frame
				for (j = 0; j < b; j++) {
					raw = null;
					switch (t) {
						case JIP.tBIT :
							raw = JIP.getRawFromBit(img.bmp[j].bmpBit);
							break;
						case JIP.tBYTE :
						case JIP.tCOLOR :
							raw = JIP.getRawFromByte(img.bmp[j].bmpByte);
							break;
						case JIP.tWORD :
							raw = JIP.getRawFromWord(img.bmp[j].bmpWord);
							break;
						case JIP.tREAL :
							raw = JIP.getRawFromReal(img.bmp[j].bmpReal);
							break;

						case JIP.tSEGMENT :
							raw = JIP.getRawFromSegment(img.bmp[j].Segmentos);
							break;
						case JIP.tPOLY :
							raw = JIP.getRawFromPoly(img.bmp[j].Poligonos);
							break;

						case JIP.tPOINT :
							raw = JIP.getRawFromPoint(img.bmp[j].Puntos);
							break;

						case JIP.tEDGES :
							raw = JIP.getRawFromEdges(img.bmp[j].Edges);
							break;

					}
					dat.write(raw, 0, raw.length);
				}
			}

			dat.flush();
		} catch (Exception e) {
			System.err.println(e);
		}
		finally {
			try {
				dat.close();
				bfs.close();
				fs.close();
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

	/**<P><FONT COLOR="red">
	 *<B>Description:</B><BR>
	 *<FONT COLOR="blue">	 
	 * It save a JIP sequence object in a ZIP format file.
	 * @param seq Sequence to save.
	 * @param dir Directory where ZIP file is saved. 
	 * @param namefile Name of the file.	 
	 */

	public static void saveSeqIntoFileZip(
		JIPSequence seq,
		String dir,
		String namefile) {
		JIPImage img;
		byte[] raw;

		//Streams para escribir en el fichero
		FileOutputStream fs = null;
		BufferedOutputStream bfs = null;
		DataOutputStream dat = null;
		BufferedWriter cab = null;

		String line;
		int i, j, nframes, sz;
		int w, h, b, t;
		String name, tip;

		try {

			//Abre el fichero de salida con buffering de 4K
			fs = new FileOutputStream(dir + "temp.~~~");
			bfs = new BufferedOutputStream(fs, 4096);
			dat = new DataOutputStream(bfs);
			cab = new BufferedWriter(new OutputStreamWriter(dat));

			/* ESCRITURA DE LA CABECERA */

			//Linea 1 "JIP\n"
			line = "JIP";
			cab.write(line, 0, line.length());
			cab.newLine();

			//Linea 2 "\n"
			cab.newLine();

			//Linea 3 "Sequence:<name>\n"
			line = "Sequence:" + seq.name;
			cab.write(line, 0, line.length());
			cab.newLine();

			//Linea 4 "Frames:<nframes>\n"
			nframes = seq.nframes;
			line = "Frames:" + nframes;
			cab.write(line, 0, line.length());
			cab.newLine();

			//Linea 5 "\n"
			cab.newLine();

			//Para cada frame de la secuencia
			for (i = 0; i < nframes; i++) {

				//Obtiene datos de la imagen del frame
				img = seq.getFrame(i);
				t = img.type;
				Vector geoData;
				tip = "";
				name = "";
				w = 0;
				h = 0;
				b = 0;
				sz = 0;

				switch (t) {
					case JIP.tBIT :
						w = img.width;
						h = img.height;
						b = img.nbands;
						name = img.name;
						tip = "BIT";
						sz = ((int) Math.ceil((double) (w * h) / 8.0)) * b;
						break;
					case JIP.tBYTE :
						w = img.width;
						h = img.height;
						b = img.nbands;
						name = img.name;
						tip = "BYTE";
						sz = w * h * b;
						break;
					case JIP.tCOLOR :
						w = img.width;
						h = img.height;
						b = img.nbands;
						name = img.name;
						tip = "COLOR";
						sz = w * h * b;
						break;
					case JIP.tWORD :
						w = img.width;
						h = img.height;
						b = img.nbands;
						name = img.name;
						tip = "WORD";
						sz = w * h * 2 * b;
						break;
					case JIP.tREAL :
						w = img.width;
						h = img.height;
						b = img.nbands;
						name = img.name;
						tip = "REAL";
						sz = w * h * 4 * b;
						break;
					case JIP.tPOINT :
						b = img.nbands;
						name = img.name;
						tip = "POINT";
						sz = img.getVector().size();
						sz *= 2;
						break;
					case JIP.tSEGMENT :
						b = img.nbands;
						name = img.name;
						tip = "SEGMENT";
						sz = img.getVector().size();
						sz *= 2;
						break;
					case JIP.tPOLY :
						b = img.nbands;
						name = img.name;
						tip = "POLY";
						sz = 0;
						Vector vaux1;
						geoData = new Vector(img.getVector());
						for (int a = 0; a < geoData.size(); a++) {
							vaux1 = new Vector((Vector) geoData.elementAt(a));
							sz += vaux1.size();
						}
						sz *= 2;
						break;
					case JIP.tEDGES :
						b = img.nbands;
						name = img.name;
						tip = "EDGES";
						sz = 0;
						Vector vaux2;
						geoData = new Vector(img.getVector());
						for (int a = 0; a < geoData.size(); a++) {
							vaux2 = new Vector((Vector) geoData.elementAt(a));
							sz += vaux2.size();
						}
						sz *= 2;
						break;
				}

				//Nombre de la imagen
				line = "Frame " + i + ":" + name;
				cab.write(line, 0, line.length());
				cab.newLine();

				//Anchura y altura de la imagen
				line =
					"\tPixels:" + String.valueOf(w) + "x" + String.valueOf(h);
				cab.write(line, 0, line.length());
				cab.newLine();

				//Tipo de la imagen
				line = "\tType:" + tip;
				cab.write(line, 0, line.length());
				cab.newLine();

				//Bandas de la imagen
				line = "\tBands:" + String.valueOf(b);
				cab.write(line, 0, line.length());
				cab.newLine();

				//Bytes de la imagen
				if (t != JIP.tPOLY && t != JIP.tEDGES) {
					line = "\tSize:" + String.valueOf(sz) + " bytes";
					cab.write(line, 0, line.length());
					cab.newLine();
				} else {
					sz = 0;
					Vector vaux1;
					geoData = new Vector(img.getVector());
					for (int a = 0; a < geoData.size(); a++) {
						vaux1 = new Vector((Vector) geoData.elementAt(a));
						line =
							"\tSize:"
								+ String.valueOf(vaux1.size() * 2)
								+ " bytes";
						cab.write(line, 0, line.length());
						cab.newLine();
					}
					line = "\t" + "end";
					cab.write(line, 0, line.length());
					cab.newLine();

				}
				//Linea "\n"
				cab.newLine();

			}

			//Ultima linea de cabecera "RAW\n"
			line = "RAW";
			cab.write(line, 0, line.length());
			cab.newLine();

			//Vuelca la cabecera
			cab.flush();

			/* ESCRITURA DE DATOS */

			//Marca de comienzo de datos RAW
			dat.writeByte(255);

			//Para cada frame de la secuencia
			for (i = 0; i < nframes; i++) {

				//Obtiene datos de la imagen del frame
				img = seq.getFrame(i);
				b = img.nbands;
				t = img.type;

				//Para cada banda del frame
				for (j = 0; j < b; j++) {
					raw = null;
					switch (t) {
						case JIP.tBIT :
							raw = JIP.getRawFromBit(img.bmp[j].bmpBit);
							break;
						case JIP.tBYTE :
						case JIP.tCOLOR :
							raw = JIP.getRawFromByte(img.bmp[j].bmpByte);
							break;
						case JIP.tWORD :
							raw = JIP.getRawFromWord(img.bmp[j].bmpWord);
							break;
						case JIP.tREAL :
							raw = JIP.getRawFromReal(img.bmp[j].bmpReal);
							break;

						case JIP.tSEGMENT :
							raw = JIP.getRawFromSegment(img.bmp[j].Segmentos);
							break;
						case JIP.tPOLY :
							raw = JIP.getRawFromPoly(img.bmp[j].Poligonos);
							break;

						case JIP.tPOINT :
							raw = JIP.getRawFromPoint(img.bmp[j].Puntos);
							break;

						case JIP.tEDGES :
							raw = JIP.getRawFromEdges(img.bmp[j].Edges);
							break;

					}
					dat.write(raw, 0, raw.length);
				}
			}

			//Vuelca los datos
			dat.flush();

			// fichero .zip
			FileOutputStream f = new FileOutputStream(dir + namefile);
			CheckedOutputStream csum = new CheckedOutputStream(f, new CRC32());
			ZipOutputStream out =
				new ZipOutputStream(new BufferedOutputStream(csum));

			ZipEntry ze = new ZipEntry(namefile + ".jip");

			BufferedInputStream in =
				new BufferedInputStream(new FileInputStream(dir + "temp.~~~"));

			File aux = new File(dir + "temp.~~~");
			// longitud en bytes del fichero
			long longi = aux.length();

			out.putNextEntry(ze);

			out.setLevel(9);

			byte b1[] = new byte[(int) longi];
			in.read(b1, 0, (int) longi); // leo el fichero
			out.write(b1); // grabo los datos comprimidos

			out.closeEntry();

			in.close();
			out.close();
			aux.delete();
		} catch (Exception e) {
			System.err.println(e);
		}
		finally {
			try {
				dat.close();
				bfs.close();
				fs.close();
			} catch (Exception e) {
				System.err.println(e);
			}
		}

	}

	/**<P><FONT COLOR="red">
	 *<B>Description:</B><BR>
	 *<FONT COLOR="blue">	 
	 * It saves a JPG image format.
	 * @param seq: Opend sequence in the screen which wants to save a copy in JPG format.
	 * @param active: Frame which is shown in the ViewerG window and it will be saved in a JPG format
	 * @param dir: Directory where the JPG image is saved.
	 * @param namefile: Name that the JPG format file will be saved.
	 */
	public static void saveImgIntoFileJpg(
		JIPSequence seq,
		int active,
		String dir,
		String namefile) {
		Image origen;
		BufferedImage buffImage;
		float quality = 1.0f; // Quality: 0.0 worst, 1.0 best
		// Para escribir en el fichero
		OutputStream os;
		// Para codificar en formato JPG
		JPEGImageEncoder encoder;
		try {
			origen = JIPToolkit.getAWTImage(seq.getFrame(active));
			buffImage =
				new BufferedImage(
					origen.getWidth(null),
					origen.getHeight(null),
					BufferedImage.TYPE_INT_RGB);
			AffineTransform tx = new AffineTransform();
			//pintar la imagen en el buffer de imagen
			Graphics2D g2d = buffImage.createGraphics();
			g2d.drawImage(origen, tx, null);
			g2d.dispose();
			//codificar la imagen en formato JPG y escribirla en un fichero
			os = new FileOutputStream(dir + namefile);
			encoder = JPEGCodec.createJPEGEncoder(os);
			JPEGEncodeParam jep = encoder.getDefaultJPEGEncodeParam(buffImage);
			jep.setQuality(quality, true);
			encoder.setJPEGEncodeParam(jep);
			encoder.encode(buffImage);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**<P><FONT COLOR="red">
	 *<B>Description:</B><BR>
	 *<FONT COLOR="blue">
	 * It saves a JIP sequence object in TGA (Targa) format file.	 
	 * If the sequence has some frames, a new file will be generated for each of them,
	 * ( or more if the frames have some grey bands), it adds to name the suffix "_f0", "_fi", ...
	 * depend of the frame and the suffix "_b0", "_b1", ... in the case of same gresy bands
	 * to the same frame.
	 * @param seq Sequence to save.
	 * @param file File where the sequence is loaded. (if it does not exist then it creates it,
	 * else it is rewritten)
	 */
	public static void saveSeqIntoTga(JIPSequence seq, String file) {

		//Obtiene nombre del fichero y extension
		String name = null;
		String ext = null;
		if (file.lastIndexOf(".") == -1) {
			name = file;
			ext = ".tga";
		} else {
			name = file.substring(0, file.lastIndexOf("."));
			ext = file.substring(file.lastIndexOf("."));
		}

		int nf = seq.getNumFrames();
		for (int i = 0; i < nf; i++) {
			if (nf > 1)
				saveImageIntoTga(seq.getFrame(i), name + "_f" + i + ext);
			else
				saveImageIntoTga(seq.getFrame(i), name + ext);
		}

	}

	/**<P><FONT COLOR="red">
	*<B>Description:</B><BR>
	*<FONT COLOR="blue">	
	* It draws the line which coordinate is transfered how parameter,
	* by means of Bressenham algorithm.	
	*@param x0 X coordinate of initial point.
	*@param y0 Y coordinate of inicial point.
	 *@param x1 X coordinate of the last point.
	 *@param y1 Y coordinate of the last point.
	 *@param icol Column	
	*@param irow Row
	*@param pix Array of pixels.
	*/

	public static void drawline(int x0, int y0, int x1, int y1, int icol,
		int irow, float pix[]) {
		int xmin, xmax; /* Coordenadas de la Linea */
		int ymin, ymax;
		int dir; /* Direccion de busqueda */
		int dx;
		int dy;

		/* Incrementos Este, Nort-Este, Sur, Sur-Este, Norte */
		int incrE, incrNE, incrSE;
		int d;
		int x, y;
		int mpCase;
		int done;
		xmin = x0;
		xmax = x1;
		ymin = y0;
		ymax = y1;

		dx = xmax - xmin;
		dy = ymax - ymin;

		if (dx * dx > dy * dy) /* busqueda horizontal */ {
			dir = 0;
			if (xmax < xmin) {
				xmin ^= xmax;
				xmax ^= xmin;
				xmin ^= xmax;
				ymin ^= ymax;
				ymax ^= ymin;
				ymin ^= ymax;
			}
			dx = xmax - xmin;
			dy = ymax - ymin;

			if (dy >= 0) {
				mpCase = 1;
				d = 2 * dy - dx;
			} else {
				mpCase = 2;
				d = 2 * dy + dx;
			}

			incrNE = 2 * (dy - dx);
			incrE = 2 * dy;
			incrSE = 2 * (dy + dx);
		} else { /* busqueda vertical */
			dir = 1;
			if (ymax < ymin) {
				xmin ^= xmax;
				xmax ^= xmin;
				xmin ^= xmax;
				ymin ^= ymax;
				ymax ^= ymin;
				ymin ^= ymax;
			}
			dx = xmax - xmin;
			dy = ymax - ymin;

			if (dx >= 0) {
				mpCase = 1;
				d = 2 * dx - dy;
			} else {
				mpCase = 2;
				d = 2 * dx + dy;
			}

			incrNE = 2 * (dx - dy);
			incrE = 2 * dx;
			incrSE = 2 * (dx + dy);
		}

		/* Comenzamos la busqueda */
		x = xmin;
		y = ymin;
		done = 0;

		while (done == 0) {
			if (x > 0 && x < icol && y > 0 && y < irow)
				pix[y * icol + x] = 1.0f;

			/* Movemos al siguiente punto */
			switch (dir) {
				case 0 : /* horizontal */ {
						if (x < xmax) {
							switch (mpCase) {
								case 1 :
									if (d <= 0) {
										d += incrE;
										x++;
									} else {
										d += incrNE;
										x++;
										y++;
									}
									break;

								case 2 :
									if (d <= 0) {
										d += incrSE;
										x++;
										y--;
									} else {
										d += incrE;
										x++;
									}
									break;
							}
						} else
							done = 1;
					}
					break;

				case 1 : /* vertical */ {
						if (y < ymax) {
							switch (mpCase) {
								case 1 :
									if (d <= 0) {
										d += incrE;
										y++;
									} else {
										d += incrNE;
										y++;
										x++;
									}
									break;

								case 2 :
									if (d <= 0) {
										d += incrSE;
										y++;
										x--;
									} else {
										d += incrE;
										y++;
									}
									break;
							}
						} else
							done = 1;
					}
					break;
			}
		}
	}

}
