
package com.example.viewingimages;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

public class Ex4a_3DPermute {

	@SuppressWarnings("unchecked")
	public static <T extends RealType<T> & NativeType<T>> void main(
		final String[] args) throws IOException
	{
		// create an instance of imagej
		final ImageJ ij = new ImageJ();

		ij.launch(args);

		// get cells as IJ2 Dataset
		Dataset cells = (Dataset) ij.io().open(
			"../images/Eugene Katrukha/20180310_cells_N2_60water_100ms_Z Series_250nm_col1wel24_GFP_fillter_registered.tif");

		Dataset cells_sd = (Dataset) ij.io().open(
			"../images/Eugene Katrukha/20180310_cells_SD2_60oil_100ms_Z Series_250nm_col1wel24_GFP_fillter_registered.tif");

		ij.ui().show("Widefield", cells);
		ij.ui().show("Spinning Disc", cells_sd);

		IntervalView<T> raiCellXYZ = Views.permute(
			(RandomAccessibleInterval<T>) cells, 1, 2);
		IntervalView<T> raiCell_sdXYZ = Views.permute(
			(RandomAccessibleInterval<T>) cells, 1, 2);

		ij.ui().show("Widefield XZY", raiCellXYZ);
		ij.ui().show("Spinning Disc XZY", raiCell_sdXYZ);
	}
}
