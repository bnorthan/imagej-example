package com.example;

import net.imagej.ImageJ;
import net.imglib2.FinalDimensions;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;

public class LaunchImageJ {
	public static <T extends RealType<T> & NativeType<T>> void main(final String[] args) {

		// create an instance of imagej
		final ImageJ ij = new ImageJ();

		// launch it
		ij.launch(args);

		// define an x and y size
		final int xSize = 128;
		final int ySize = 128;

		// create a new image
		final Img<FloatType> img = (Img<FloatType>) ij.op().create()
				.img(new FinalDimensions(new long[] { xSize, ySize }), new FloatType());

		// get a 'RandomAccess' object, this object allows access to pixels at
		// specific locations
		final RandomAccess<FloatType> ra = img.randomAccess();

		// move to the middle of the image and set the pixel value there to 255
		ra.setPosition(new long[] { xSize / 2, ySize / 2 });
		ra.get().setReal(255.0);

		// move the ra to a new position, and set another pixel to 255
		ra.setPosition(new long[] { xSize / 4, ySize / 4 });
		ra.get().setReal(255.0);

		// show the image
		ij.ui().show(img);

	}
}
