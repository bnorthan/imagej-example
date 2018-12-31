
package com.example;

import java.io.IOException;

import net.imagej.ImageJ;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

public class ComputeMax {

	public static <T extends RealType<T> & NativeType<T>> void main(
		final String[] args) throws IOException
	{

		// create an instance of imagej
		final ImageJ ij = new ImageJ();

		// launch it
		ij.launch(args);

		@SuppressWarnings("unchecked")
		final Img<T> image = (Img<T>) ij.io().open(
			"http://imagej.net/images/bridge.gif"); // convenient example stack

		ij.ui().show("bridge", image);
		
		T max = ij.op().stats().max(image);

		System.out.println(max.getClass());

	}

}
