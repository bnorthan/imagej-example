package com.example;

import java.io.IOException;

import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.outofbounds.OutOfBoundsBorderFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;

public class ConvolveBorderExample_List_3_18_19 {
	public static <T extends RealType<T> & NativeType<T>> void main(
		final String[] args) throws IOException
	{

		// create an instance of imagej
		final ImageJ ij = new ImageJ();
		double sigma=3.0f;

		// launch it
		ij.launch(args);

		@SuppressWarnings("unchecked")
		final Img<T> image = (Img<T>) ij.io().open(
			"http://imagej.net/images/bridge.gif"); // convenient example stack

		ij.ui().show("bridge", image);
		
		RandomAccessibleInterval<FloatType> logKernel=ij.op().create().kernelLog(sigma, image.numDimensions(), new FloatType());
		RandomAccessibleInterval<FloatType> logFiltered=ij.op().filter().convolve(image, logKernel, null, new OutOfBoundsBorderFactory());
		
		ij.ui().show("log filtered", logFiltered);
	}
}
