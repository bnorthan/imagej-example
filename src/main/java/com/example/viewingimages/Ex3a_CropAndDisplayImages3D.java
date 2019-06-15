
package com.example.viewingimages;

import io.scif.img.ImgIOException;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;

public class Ex3a_CropAndDisplayImages3D {

	@SuppressWarnings("unchecked")
	public static <T extends RealType<T> & NativeType<T>> void main(
		final String[] args) throws IOException, ImgIOException,
		IncompatibleTypeException
	{

		// create an instance of imagej
		final ImageJ ij = new ImageJ();

		// launch it
		ij.launch(args);

		Dataset image = (Dataset) ij.io().open(
			"../images/Eugene Katrukha/20180310_cells_SD2_60oil_100ms_Z Series_250nm_col1wel24_GFP_fillter_registered.tif");

		ij.ui().show(image);

		Interval interval = Intervals.createMinMax(100, 100, 20, 400, 400, 40);

		// crop interval
		RandomAccessibleInterval<T> raiVolume = (RandomAccessibleInterval<T>) ij
			.op().transform().crop(image, interval);

		// display the image
		ij.ui().show("RAI volume", raiVolume);

	}
}
