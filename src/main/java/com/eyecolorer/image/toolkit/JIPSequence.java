package com.eyecolorer.image.toolkit;

import java.util.Vector;

/**<P><FONT COLOR="red">
 *<B>Description:</B><BR>
 *<FONT COLOR="blue">
 * This class define the sequence object. A sequence is an ordinate list of frames
 * which is an image object. The type of the images in the frame do not have 
 * to coincide and the same happen with size of the image.
 * The number of frames in a sequence can change during its life even it can be 0
 * (empty sequence). The access to the image of a sequence is done with an integer 
 * index which is between [0..numframes-1].
 * We can add, insert and delete frame of a sequence and we can add another sequences
 * at the end of it.   
 * Also, we can obtain or change the image of a frame in the sequence.
 * The sequence object is always load in a JIP format file 
 * (If it is an empty sequence it does not load).
 *  Therefore, to load an unique image in the file, we have to make a sequence with
 * only a frame which contains it.
 * The save and load methods for sequence are in the JIPToolkit class and they are not
 * in the sequence class.  
 * The reason to separate is because the file format is not inherent in the sequence struct
 * so we can change without we have to change this class. Finally, we can assign a 
 * name which its deault name will be "[Unnamed]". 
 */
public class JIPSequence {

	
	/** Number of Frames which is a part of Sequence */
	int nframes;

	/**
	 * Name of the sequence
	 * 
	 * @uml.property name="name"
	 */
	String name;


	/**
	 * Vector which contains the frames of the sequence
	 * 
	 * @uml.property name="frames"
	 * @uml.associationEnd 
	 * @uml.property name="frames" multiplicity="(0 -1)" elementType="jip.JIPImage"
	 */
	Vector frames;


	/**<P><FONT COLOR="red">
	 *<B>Description:</B><BR>
		 *<FONT COLOR="blue">
		 * Representation Function		 
		 * @return String text with the description of the sequence. 
	*/

	public String toString() {
		String seq = "Sequence [" + name + "]/nNFrames: " + nframes;
		for (int i = 0; i < nframes; i++) {
			JIPImage imga = (JIPImage) frames.elementAt(i);
			seq = seq + "\nFrame [" + i + "]\n\tNBands: " + imga.getNumBands()
					+ "\n\tNPixels: " + imga.getNumPixels() + "\n\tWidth: "
					+ imga.getWidth() + "\n\tHeight: " + imga.getHeight()
					+ "\n\tType: " + imga.getType();
		}
		return (seq);
	}


	//******************//
	//  Constructors   //
	//******************//

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
			 *<FONT COLOR="blue">	 
	 * Empty sequence constructor (Without frame).
	 */

	public JIPSequence() {
		nframes = 0;
		name = "[Unnamed]";
		frames = new Vector();
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
			 *<FONT COLOR="blue">
	 * Sequence constructor with a frame.	 
	 * @param img Image corresponding to sequence frame.
	 */
	public JIPSequence(JIPImage img) {
		nframes = 0;
		name = "[Unnamed]";
		frames = new Vector();
		if (img != null)
			frames.addElement(img);
		nframes = frames.size();
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
			 *<FONT COLOR="blue">	 
	 * Sequence constructor which copies another sequence.
	 * @param seq Reference sequence to make a copy.
	 */
	public JIPSequence(JIPSequence seq) {
		if (seq != null) {
			nframes = seq.nframes;
			name = seq.name;
		} else {
			nframes = 0;
			name = "[Unnamed]";
		}
		frames = new Vector();
		if (nframes > 0) {
			for (int i = 0; i < nframes; i++)
				frames.addElement(new JIPImage(seq.getFrame(i)));
			nframes = frames.size();
		}
	}


	//*******************//
	//     METHODS       //
	//*******************//

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
			 *<FONT COLOR="blue">	 
	 * It gets the number of frames of sequence.
	 * @return Number of frames.
	 */
	public int getNumFrames() {
		return nframes;
	}

	/**
	 * <P><FONT COLOR="red">
	 * <B>Description:</B><BR>
	 * <FONT COLOR="blue">	 
	 * It gets the name of the sequence.
	 * @return Name of the sequence.
	 * 
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * <P><FONT COLOR="red">
	 * <B>Description:</B><BR>
	 * <FONT COLOR="blue">	 
	 * It assigns the name of the sequence.
	 * @param nom Name of the sequence.
	 * 
	 * @uml.property name="name"
	 */
	public void setName(String nom) {
		if (nom == null || nom.length() == 0)
			name = "[Unnamed]";
		else
			name = nom;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
			 *<FONT COLOR="blue">	 
	 * It gets the image correspondig to a frame of the sequence.
	 * @param n Number of frames (0 <= n <= numframes-1)
	 * @return Image correspondig to n frame.
	 * null if n is not corresponding to a frame of the sequence.
	 */
	public JIPImage getFrame(int n) {
		if (n >= 0 && n < nframes)
			return (JIPImage) frames.elementAt(n);
		else
			return null;
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
			 *<FONT COLOR="blue">	 
	 * It assigns an image to a frame of the sequence.
	 * @param img Image which will be assign to the frame.
	 * @param n Number of frame which we assign the image.
	 * n Tell us the index of the frame to assign, therefore,
	 * it should correspond to an index of a frame which is exist and it is worth:
	 * 0 <= n <= numframes-1.
	 */
	public void setFrame(JIPImage img, int n) {
		if (img != null && n >= 0 && n < nframes) {
			frames.setElementAt(img, n);
			nframes = frames.size();
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
			 *<FONT COLOR="blue">	 
	 * It add a frame in the end of the sequence.
	 * @param img Image corresponding to the added frame.
	 */
	public void addFrame(JIPImage img) {
		if (img != null) {
			frames.addElement(img);
			nframes = frames.size();
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
			 *<FONT COLOR="blue">	
	 * It add a frame in the sequence.
	 * @param img Image corresponding to the frame added.
	 * @param n Position corresponding to the insert frame.
	 * n Show the index of the frame that we insert, therefor, it should be belong to
	 * an index of a frame which exist and it is worth: 0 <= n <= numframes-1.
	 * That frames whose index was equal or bigger than n will increase them index for
	 * a unit.	 
	 */
	public void insertFrame(JIPImage img, int n) {
		if (img != null && n >= 0 && n < nframes) {
			frames.insertElementAt(img, n);
			nframes = frames.size();
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
		  *<FONT COLOR="blue">	 
	 * It deletes a frame of the sequence.
	 * @param n Number of the frame (0 <= n <= numframes-1)
	 */
	public void removeFrame(int n) {
		if (n >= 0 && n < nframes) {
			frames.removeElementAt(n);
			nframes = frames.size();
		}
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
			 *<FONT COLOR="blue">	 
	 * It deletes all frames of the sequence (The sequence will be empty)
	 */

	public void removeAllFrames() {
		frames.removeAllElements();
		nframes = frames.size();
	}

	/**<P><FONT COLOR="red">
		 *<B>Description:</B><BR>
			 *<FONT COLOR="blue">	 
	 * It adds a sequence in the end of the sequence.
	 * @param seq Sequence to add.
	 */
	public void appendSequence(JIPSequence seq) {
		if (seq != null) {
			int nf = seq.nframes;
			if (nf > 0) {
				for (int i = 0; i < nf; i++)
					frames.addElement(seq.getFrame(i));
				nframes = frames.size();
			}
		}
	}
}
