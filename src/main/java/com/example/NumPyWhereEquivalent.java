
package com.example;

import java.io.IOException;

import net.imagej.ImageJ;
import net.imagej.ops.OpEnvironment;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imagej.ops.special.computer.AbstractNullaryComputerOp;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.NullaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

public class NumPyWhereEquivalent {

	public static <T extends RealType<T> & NativeType<T>> void main(
		final String[] args) throws IOException
	{

		// create an instance of imagej
		final ImageJ ij = new ImageJ();

		// launch it
		ij.launch(args);

		@SuppressWarnings("unchecked")
		final Img<T> image = (Img<T>) ij.io().open(
			"http://imagej.net/images/bridge.gif"); // convenient example stack

		ij.ui().show("bridge", image);
		
		T max = ij.op().stats().max(image);
		
		UnaryComputerOp<T, T> op=new AbstractUnaryComputerOp<T, T>() {

			@Override
			public void compute(T input, T output) {
				if (input.getRealFloat()>0) {
					output.set(input);
				}
				else {
					output.setReal(0);
				}
			}
		};
		
		ij.op().map(Views.iterable(image), Views.iterable(image), op);

		System.out.println(max.getClass());

	}

}
