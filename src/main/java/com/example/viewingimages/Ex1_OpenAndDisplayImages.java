
package com.example.viewingimages;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import ij.IJ;
import ij.ImagePlus;

public class Ex1_OpenAndDisplayImages {

	public static <T extends RealType<T> & NativeType<T>> void main(
		final String[] args) throws IOException
	{

		// create an instance of imagej
		final ImageJ ij = new ImageJ();

		// launch it
		ij.ui().showUI();

		// get bridge as IJ1 ImagePlus (imp)
		ImagePlus impBridge = IJ.openImage("../images/bridge.tif");

		// get bridge as IJ2 Dataset
		// Dataset
		Dataset datasetBridge = (Dataset) ij.io().open("../images/bridge.tif");

		// show the IJ1 ImagePlus
		impBridge.show();
		impBridge.setTitle("ImageJ1 ImagePlus");

		// show the IJ2 Dataset
		ij.ui().show("Bridge IJ3 ij.ui().show", datasetBridge);

		// show using imagej functions
		ImageJFunctions.show((RandomAccessibleInterval<T>) datasetBridge
			.getImgPlus()).setTitle("Bridge IJ2 ImageJFunctions.show");
		
	}

	// take a look at the bit depth and data type of the image in IJ1
	public static void extras(ImageJ ij, ImagePlus imp, Dataset data) {
		ij.log().info("IJ1 bit depth " + imp.getBitDepth());
		ij.log().info("IJ1 type " + imp.getProcessor().getClass());

		// take a look at the bits per pixel and pixel type of the Dataset
		ij.log().info("IJ2 bit depth " + data.firstElement().getBitsPerPixel());
		ij.log().info("IJ2 type " + data.firstElement().getClass());
		ij.log().info("");

		IJ2CourseImageUtility.displayAxisInfo(data, ij.log());
	}
}
