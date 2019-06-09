
package com.example;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.ImgPlus;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.create.img.CreateImgFromDimsAndType;
import net.imagej.ops.filter.convolve.PadAndConvolveFFT;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCFI;
import net.imagej.ops.stats.AbstractStatsOp;
import net.imagej.ops.stats.DefaultMean;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class CreateSomeOps<T extends RealType<T> & NativeType<T>> {

	BinaryFunctionOp<Dimensions, NativeType<T>, Img<T>> creator;

	AbstractBinaryHybridCFI<IterableInterval<T>, IterableInterval<T>> subtracter;

	AbstractBinaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> convolver;

	AbstractStatsOp<Iterable<T>, T> meanOp;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	void initializeSomeOps(Img<T> img, OpService ops) {

		creator = (BinaryFunctionOp) Functions.binary(ops,
			CreateImgFromDimsAndType.class, Img.class, Dimensions.class,
			NativeType.class);

		subtracter = (AbstractBinaryHybridCFI) Functions.binary(ops,
			Ops.Math.Subtract.class, IterableInterval.class, img, img);

		convolver = (AbstractBinaryComputerOp) Computers.binary(ops,
			PadAndConvolveFFT.class, RandomAccessibleInterval.class, img, img);

		meanOp = (AbstractStatsOp) Functions.unary(ops, DefaultMean.class,
			RealType.class, Views.iterable(img));

	}

	void doSomething(Img<T> img, ImageJ ij) {

		long start, finish;

		start = System.nanoTime();
		Img<FloatType> imgF1 = ij.op().create().img(new FinalDimensions(100, 100),
			new FloatType());
		finish = System.nanoTime();
		System.out.println("time for ij.op().create(): " + (finish - start));

		start = System.nanoTime();

		Img<T> imgF2 = creator.calculate(new FinalDimensions(100, 100), img
			.firstElement());
		finish = System.nanoTime();
		System.out.println("time for creator.calculate(): " + (finish - start));

		RandomAccessibleInterval<T> gauss = ij.op().create().kernelGauss(
			new double[] { 3.0, 3.0 }, img.firstElement());

		ij.ui().show(gauss);

		RandomAccessibleInterval<T> filtered = creator.calculate(img, img
			.firstElement());
		convolver.compute(img, gauss, filtered);

		ij.ui().show(filtered);

		IterableInterval<T> subtracted = subtracter.calculate(img, Views.iterable(
			filtered));

		ij.ui().show(subtracted);

		float mean = meanOp.calculate(img).getRealFloat();

		System.out.println("mean is: " + mean);

	}

	// create an instance of imagej
	@SuppressWarnings("unchecked")
	public static <T extends RealType<T> & NativeType<T>> void main(
		final String[] args) throws InterruptedException, IOException
	{
		ImageJ ij = new ImageJ();

		ij.launch(args);

		Dataset dataBoats = (Dataset) ij.io().open("./small_boatsf.tif");

		ImgPlus<T> impBoats = (ImgPlus<T>) dataBoats.getImgPlus();

		ij.ui().show(impBoats);

		CreateSomeOps createSomeOps = new CreateSomeOps();
		createSomeOps.initializeSomeOps(impBoats, ij.op());
		createSomeOps.doSomething(impBoats, ij);

	}

}
