
package com.example;

import java.io.IOException;

import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.view.Views;

public class MeanFilterTests {

	// create an instance of imagej
	public static <T extends RealType<T> & NativeType<T>> void main(
		final String[] args) throws InterruptedException, IOException
	{

		final ImageJ ij = new ImageJ();

		// launch it
		ij.launch(args);

		double[][] k = new double[][] { new double[] { 1.0, 0, 1.0 }, new double[] {
			1.0, 1.0, 1.0 }, new double[] { 0.0, 1.0, 1.0 } };

		RandomAccessibleInterval<ByteType> in = ij.op().create().kernel(k,
			new ByteType());
		
		byte test=(2+2+1)/3;
		
		System.out.println(test);
		
		System.out.println();
		
		
		RandomAccessibleInterval<ByteType> out = ij.op().create().img(in);

		ij.op().filter().mean(Views.iterable(out), in, new RectangleShape(3,false));
		
		for (Type t:Views.iterable(in)) {
			System.out.println(t+" ");
		}
		System.out.println();
		
		for (Type t:Views.iterable(out)) {
			System.out.println(t+" ");
		}
		System.out.println();
	
		//System.out.println(ij.op().stats().mean(Views.iterable(in)));
	}

}
