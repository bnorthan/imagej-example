
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
		ij.launch(args);

		// get bridge as IJ1 ImagePlus (imp)
		ImagePlus impBridge = IJ.openImage("http://imagej.net/images/bridge.gif");

		// get bridge as IJ2 Dataset
		// Dataset
		// dataBridge=(Dataset)ij.io().open("http://imagej.net/images/bridge.gif");
		Dataset datasetBridge = (Dataset) ij.io().open("../images/bridge.tif");

		// take a look at the bit depth and data type of the image in IJ1
		ij.log().info("IJ1 bit depth " + impBridge.getBitDepth());
		ij.log().info("IJ1 type " + impBridge.getProcessor().getClass());

		// take a look at the bits per pixel and pixel type of the Dataset
		ij.log().info("IJ2 bit depth " + datasetBridge.firstElement()
			.getBitsPerPixel());
		ij.log().info("IJ2 type " + datasetBridge.firstElement().getClass());
		ij.log().info("");

		// show the
		impBridge.show();

		IJ2CourseImageUtility.displayAxisInfo(datasetBridge, ij.log());

		// show IJ2
		ij.ui().show(datasetBridge);

		// show using imagej functions
		ImageJFunctions.show((RandomAccessibleInterval<T>) datasetBridge
			.getImgPlus());

	}
}
