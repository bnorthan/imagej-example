
package com.example.viewingimages;

import io.scif.img.ImgIOException;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.DatasetService;
import net.imagej.ImageJ;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;

public class Ex3_CropAndDisplayImages {

	public static <T extends RealType<T> & NativeType<T>> void main(
		final String[] args) throws IOException, ImgIOException,
		IncompatibleTypeException
	{

		// where are we in the file system??
		System.out.println("Working Directory = " + System.getProperty("user.dir"));

		// create an instance of imagej
		final ImageJ ij = new ImageJ();

		// launch it
		ij.launch(args);

		final Dataset image = (Dataset) ij.io().open("../images/mitosis.tif"); // convenient
																																						// example
																																						// stack

		for (int d = 0; d < image.numDimensions(); d++) {
			ij.log().info(image.axis(d).type());
			ij.log().info(image.dimension(d));
			ij.log().info("");
		}

		ij.ui().show(image);

		long xLen = image.dimension(image.dimensionIndex(Axes.X));
		long yLen = image.dimension(image.dimensionIndex(Axes.Y));
		long zLen = image.dimension(image.dimensionIndex(Axes.Z));
		long cLen = image.dimension(image.dimensionIndex(Axes.CHANNEL));
		long tLen = image.dimension(image.dimensionIndex(Axes.TIME));

		// crop out the first timepoint (all x, y, z and channel at timepoint 1)
		RandomAccessibleInterval<T> raiVolume = (RandomAccessibleInterval<T>) ij
			.op().transform().crop(image, Intervals.createMinMax(0, 0, 0, 0, 0, xLen -
				1, yLen - 1, cLen - 1, zLen - 1, 0));

		// display the image... note that something isn't quite right
		ij.ui().show("RAI volume", raiVolume);

		// the cropped section is an RAI. To get it to display correctly we need to
		// use the dtaasetservice to convert it to an ImgPlus with ocrrect axis
		DatasetService datasetService = ij.dataset();
		AxisType[] axisTypes = new AxisType[] { Axes.X, Axes.Y, Axes.CHANNEL,
			Axes.Z };
		ImgPlus imgPlusVolume = new ImgPlus(datasetService.create(raiVolume),
			"image", axisTypes);

		// now the viewer should display the image with correct axis
		ij.ui().show("ImgPlus volume", imgPlusVolume);

		// ij.io().save(imgPlusVolume, "test_ij.io.save.tif");
		// new ImgSaver().saveImg("test.ImgSaver.tif", imgPlusVolume);
	}
}
