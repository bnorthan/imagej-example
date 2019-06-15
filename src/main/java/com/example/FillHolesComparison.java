
package com.example;

import io.scif.img.ImgOpener;

import java.io.IOException;
import java.util.List;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.ImgPlus;
import net.imagej.ops.Ops;
import net.imagej.ops.morphology.fillHoles.DefaultFillHoles;
import net.imagej.ops.slice.SlicesII;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.morphology.StructuringElements;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

import ij.IJ;
import ij.ImagePlus;

public class FillHolesComparison {

	// create an instance of imagej
	@SuppressWarnings("unchecked")
	public static <T extends RealType<T> & NativeType<T>> void main(
		final String[] args) throws InterruptedException, IOException
	{
		ImageJ ij = new ImageJ();

		ij.launch(args);

		final Dataset data = (Dataset) ij.io().open(
			"./binary_image_XY-cropped.tif");

		// final Dataset data= (Dataset) ij.io().open("./small_boats.tif");

		ij.ui().show(data);

		for (int d = 0; d < data.numDimensions(); d++) {
			ij.log().info(data.axis(d));
			ij.log().info(data.dimension(d));
			ij.log().info("");
		}

		Img<BitType> b = ij.op().convert().bit((Img<T>) data.getImgPlus());

		ij.ui().show(b);

		ImagePlus imp = ImageJFunctions.wrap(b, "imageplus");

		ImagePlus imp2 = imp.duplicate();

		UnaryFunctionOp fillHolesOp = Functions.unary(ij.op(),DefaultFillHoles.class, RandomAccessibleInterval.class, b);

		long start = System.nanoTime();
		Img<BitType> bfilled = (Img<BitType>) ij.op().morphology().fillHoles(b);
		long end = System.nanoTime();

		System.out.println("FillHoles Op took: " + (end - start) / 1e6);
		
		start = System.nanoTime();
		IterableInterval<BitType> bfilled2=(IterableInterval)fillHolesOp.calculate(b);
		end = System.nanoTime();
		System.out.println("FillHoles Prematched op took " + (end - start )/ 1e6);

		imp2.show();

		start = System.nanoTime();
		IJ.run(imp2, "Fill Holes", "stack");
		end = System.nanoTime();
		System.out.println("FillHoles IJ1 took " + (end - start) / 1e6);
	}

}
