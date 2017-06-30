package com.example;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.axis.Axes;
import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

public class ProjectionTest {
	
	final static String inputName = "./C2-confocal-stack.tif";
	
	final static ImageJ ij = new ImageJ();
	
	public static <T extends RealType<T> & NativeType<T>> void main(final String[] args) throws InterruptedException, IOException {
		
		ij.launch(args);
	
		//final Img<T> img = (Img<T>) ij.dataset().open(inputName).getImgPlus().getImg();
		final Dataset currentData=ij.dataset().open(inputName);
		
		ij.ui().show(currentData);
		
		int d;
		int[ ] projected_dimensions = new int[currentData.numDimensions()-1];
		int dim = currentData.dimensionIndex(Axes.Z);
		for (d=0; d < currentData.numDimensions();++d){
		    if(d != dim)
		         projected_dimensions[d]= (int) currentData.dimension(d); 
		}
				
		Img<T> proj = (Img<T>) ij.op().create().img(projected_dimensions);
		
		// 1.  Use Computers.unary to get op
		//UnaryComputerOp mean_op =Computers.unary(ij.op(), Ops.Stats.Mean.class, RealType.class, Iterable.class);
		
		// or 2. Cast it
		UnaryComputerOp mean_op =(UnaryComputerOp) ij.op().op(Ops.Stats.Mean.NAME, currentData.getImgPlus());
		
		Img<T> projection=(Img<T>)ij.op().transform().project(proj, currentData.getImgPlus(), mean_op, dim);
		
		ij.ui().show(projection);
		
	}

}
