
package com.example;

import io.scif.img.ImgOpener;

import java.io.IOException;
import java.util.List;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.ImgPlus;
import net.imagej.ops.slice.SlicesII;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.morphology.StructuringElements;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

public class NDFillHoles {

	// create an instance of imagej
	@SuppressWarnings("unchecked")
	public static <T extends RealType<T> & NativeType<T>> void main(
		final String[] args) throws InterruptedException, IOException
	{
		ImageJ ij = new ImageJ();

		ij.launch(args);

		// final Dataset data= (Dataset)
		// ij.io().open("./binary_image_XYZT-cropped.tif");
		ImgPlus data = new ImgOpener().openImg("./binary_image_XYZT-cropped.tif");

		// final Dataset data= (Dataset) ij.io().open("./small_boats.tif");

		ij.ui().show(data);

		for (int d = 0; d < data.numDimensions(); d++) {
			ij.log().info(data.axis(d));
			ij.log().info(data.dimension(d));
			ij.log().info("");
		}

		Img<BitType> b = ij.op().convert().bit((Img<T>)data);
		Img<BitType> out = ij.op().create().img(b);

		ij.ui().show(b);

		SlicesII<BitType> timeSlicer = new SlicesII<>(b, new int[] { 0, 1, 2 });
		Cursor<RandomAccessibleInterval<BitType>> timeCursor = timeSlicer.cursor();

		SlicesII<BitType> timeSlicerOut = new SlicesII<>(out, new int[] { 0, 1,
			2 });
		Cursor<RandomAccessibleInterval<BitType>> timeCursorOut = timeSlicerOut
			.cursor();

		List<Shape> shape = StructuringElements.square(2, 2, false);

		while (timeCursor.hasNext()) {
			timeCursor.fwd();
			timeCursorOut.fwd();

			SlicesII<BitType> zSlicer = new SlicesII<>(timeCursor.get(), new int[] {
				0, 1 });
			Cursor<RandomAccessibleInterval<BitType>> zCursor = zSlicer.cursor();

			SlicesII<BitType> zSlicerOut = new SlicesII<>(timeCursorOut.get(),
				new int[] { 0, 1 });
			Cursor<RandomAccessibleInterval<BitType>> zCursorOut = zSlicerOut
				.cursor();

			while (zCursor.hasNext()) {
				zCursor.fwd();
				zCursorOut.fwd();

				// ij.ui().show(zCursor.get());
				ij.op().morphology().fillHoles(zCursorOut.get(), zCursor.get());
				// ij.ui().show(zCursor.get());
			}

		}

		ij.ui().show(data);
		ij.ui().show(b);
		ij.ui().show(out);

	}

}
