
package com.example.viewingimages;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.FinalDimensions;
import net.imglib2.FinalInterval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

public class Ex4_3DProjections {

	public static <T extends RealType<T> & NativeType<T>>
		RandomAccessibleInterval<T> XZY(RandomAccessibleInterval<T> rai)
	{

		IntervalView<T> xyzView = Views.permute(rai, 1, 2);

		FinalInterval permutedInterval = new FinalInterval(rai.dimension(0) - 1, rai
			.dimension(2) - 1, rai.dimension(1) - 1);

		RandomAccessibleInterval<T> permutedRAI = Views.interval(xyzView,
			permutedInterval);

		return permutedRAI;

	}

	public static <T extends RealType<T> & NativeType<T>> IterableInterval<T>
		projectBad(ImgPlus<T> imgPlus, AxisType ax, OpService ops)
	{
		long[] projectedDimensions = new long[imgPlus.numDimensions() - 1];

		int projectedDimensionIndex = imgPlus.dimensionIndex(ax);

		int i = 0;

		for (int d = 0; d < imgPlus.numDimensions(); d++) {
			if (d != projectedDimensionIndex) {
				projectedDimensions[i] = imgPlus.dimension(d);
				i++;
			}
		}

		Img<T> projection = ops.create().img(new FinalDimensions(
			projectedDimensions), imgPlus.firstElement());

		UnaryComputerOp projector = Computers.unary(ops, Ops.Stats.Sum.class,
			projection.firstElement(), imgPlus);

		return ops.transform().project(projection, imgPlus, projector,
			projectedDimensionIndex);

	}

	public static <T extends RealType<T> & NativeType<T>>
		IterableInterval<FloatType> projectBetter(ImgPlus<T> imgPlus, AxisType ax,
			OpService ops)
	{
		long[] projectedDimensions = new long[imgPlus.numDimensions() - 1];

		int projectedDimensionIndex = imgPlus.dimensionIndex(ax);

		int i = 0;

		for (int d = 0; d < imgPlus.numDimensions(); d++) {
			if (d != projectedDimensionIndex) {
				projectedDimensions[i] = imgPlus.dimension(d);
				i++;
			}
		}

		Img<FloatType> projection = ops.create().img(new FinalDimensions(
			projectedDimensions), new FloatType());

		UnaryComputerOp projector = Computers.unary(ops, Ops.Stats.Sum.class,
			projection.firstElement(), imgPlus);

		return ops.transform().project(projection, imgPlus, projector,
			projectedDimensionIndex);

	}

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

		ij.ui().show(cells);
		ij.ui().show(cells_sd);

		RandomAccessibleInterval<T> raiCellXYZ = XZY(
			(RandomAccessibleInterval<T>) cells);
		RandomAccessibleInterval<T> raiCell_sdXYZ = XZY(
			(RandomAccessibleInterval<T>) cells_sd);

		ij.ui().show("Widefield XYZ", raiCellXYZ);
		ij.ui().show("Spinning Disc XYZ", raiCell_sdXYZ);

		IterableInterval<T> projectionBad = projectBad((ImgPlus<T>) cells
			.getImgPlus(), Axes.Y, ij.op());
		IterableInterval<FloatType> projectionBetter = projectBetter(
			(ImgPlus<T>) cells.getImgPlus(), Axes.Y, ij.op());

		ij.ui().show("Cell Y Projection", projectionBad);
		ij.ui().show("Cell Y Projection", projectionBetter);

		// Img<T> sumProjection_wf = ij.op().create()
	}
}
