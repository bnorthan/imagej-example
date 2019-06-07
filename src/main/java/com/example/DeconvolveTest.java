/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package com.example;

import net.imagej.ImageJ;
import net.imagej.ops.deconvolve.PadAndRichardsonLucy;
import net.imagej.ops.filter.convolve.PadAndConvolveFFT;
import net.imglib2.Point;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.region.hypersphere.HyperSphere;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

/**
 * Tests involving convolvers.
 */
public class DeconvolveTest {

	public static <T extends RealType<T> & NativeType<T>> void main(
		final String[] args)
	{

		// create an instance of imagej
		final ImageJ ij = new ImageJ();

		// launch it
		ij.launch(args);

		int[] size = new int[] { 225, 167 };

		// create an input with a small sphere at the center
		Img<FloatType> in = new ArrayImgFactory<FloatType>().create(size,
			new FloatType());

		placeSphereInCenter(in);
		
		// crop the image so the sphere is truncated at the corner
		// (this is useful for testing non-circulant mode)
		IntervalView<FloatType> incropped = Views.interval(in, new long[] {
			size[0] / 2, size[1] / 2 }, new long[] { size[0] - 1, size[1] - 1 });

		incropped = Views.zeroMin(incropped);

		ij.ui().show(incropped);

		RandomAccessibleInterval<FloatType> kernel = ij.op().create().kernelGauss(
			new double[] { 7.0, 7.0 }, new FloatType());

		// convolve 
		@SuppressWarnings("unchecked")
		final Img<FloatType> convolved = (Img<FloatType>) ij.op().run(
			PadAndConvolveFFT.class, incropped, kernel);

		@SuppressWarnings("unchecked")
		final Img<FloatType> convolved2 = (Img<FloatType>) ij.op().run(
			PadAndConvolveFFT.class, in, kernel);

		ij.ui().show("convolved", convolved);

		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<FloatType> deconvolved =
			(RandomAccessibleInterval<FloatType>) ij.op().run(PadAndRichardsonLucy.class,
				convolved, kernel, null, new OutOfBoundsConstantValueFactory<>(Util
					.getTypeFromInterval(in).createVariable()), 100);

		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<FloatType> deconvolved2 =
			(RandomAccessibleInterval<FloatType>) ij.op().run(PadAndRichardsonLucy.class,
				convolved, kernel, null, new OutOfBoundsConstantValueFactory<>(Util
					.getTypeFromInterval(in).createVariable()), null, null, null, 100,
				true, false);

		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<FloatType> deconvolved3 =
			(RandomAccessibleInterval<FloatType>) ij.op().run(PadAndRichardsonLucy.class,
				convolved2, kernel, null, new OutOfBoundsConstantValueFactory<>(Util
					.getTypeFromInterval(in).createVariable()), 100);

		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<FloatType> deconvolved4 =
			(RandomAccessibleInterval<FloatType>) ij.op().run(PadAndRichardsonLucy.class,
				convolved2, kernel, null, new OutOfBoundsConstantValueFactory<>(Util
					.getTypeFromInterval(in).createVariable()), null, null, null, 100,
				true, false);

		ij.ui().show("deconvolved", deconvolved);
		ij.ui().show("deconvolved-nc", deconvolved2);
		ij.ui().show("deconvolved", deconvolved3);
		ij.ui().show("deconvolved-nc", deconvolved4);

	}

	// utility to place a small sphere at the center of the image
	static private void placeSphereInCenter(Img<FloatType> img) {

		final Point center = new Point(img.numDimensions());

		for (int d = 0; d < img.numDimensions(); d++)
			center.setPosition(img.dimension(d) / 2, d);

		HyperSphere<FloatType> hyperSphere = new HyperSphere<>(img, center, 30);

		for (final FloatType value : hyperSphere) {
			value.setReal(1000);
		}
	}
}
