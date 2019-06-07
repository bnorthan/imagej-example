
package com.example;

import java.io.IOException;

import net.imagej.ImageJ;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;

public class LaunchImageJAndDeconvolve {

	static String workingDir = "/home/bnorthan/images/DeconQuickTest/";
	static String imgName = workingDir + "CElegans-CY3.tif";
	static String psfName = workingDir + "PSF-CElegans-CY3-1-128-104.tif";

	public static <T extends RealType<T> & NativeType<T>> void main(
		final String[] args) throws InterruptedException, IOException
	{

		// create an instance of imagej
		final ImageJ ij = new ImageJ();

		// launch it
		ij.launch(args);

		final Img<T> img = (Img<T>) (ij.dataset().open(imgName).getImgPlus()
			.getImg());
		final Img<T> psf = (Img<T>) (ij.dataset().open(psfName).getImgPlus()
			.getImg());

		ij.ui().show(img);
		ij.ui().show(psf);

		final Img<FloatType> img32 = ij.op().convert().float32(img);
		Img<FloatType> psf32 = ij.op().convert().float32(psf);

		psf32 = (Img<FloatType>) ij.op().math().divide(psf32, new FloatType(ij.op()
			.stats().sum(psf32).getRealFloat()));

		final long startTime = System.currentTimeMillis();

		final Img<FloatType> deconvolved = ij.op().create().img(img32,
			new FloatType());

		ij.op().deconvolve().richardsonLucy(deconvolved, img32, psf32, new long[] {
			0, 0, 0 }, null, null, null, null, 5, true, true);

		final long endTime = System.currentTimeMillis();

		System.out.println("Time is: " + (endTime - startTime) / 1000.);

		ij.ui().show(deconvolved);

	}
}
