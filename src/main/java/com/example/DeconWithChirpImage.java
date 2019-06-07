package com.example;

import java.io.IOException;

import ij.IJ;
import net.imagej.ImageJ;
import net.imagej.ops.Ops;
import net.imglib2.FinalDimensions;
import net.imglib2.FinalInterval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class DeconWithChirpImage {
	
final static ImageJ ij = new ImageJ();
	
	@SuppressWarnings("unchecked") 
	public static <T extends RealType<T> & NativeType<T>> void main(final String[] args) throws InterruptedException, IOException {
		
		ij.launch(args);
		
		Img<T> blank=(Img)ij.op().create().img(new long[] {512,512});
		
		String formula = "50 * (Math.sin(2*Math.PI*0.1*Math.pow(3,p[0]/149.8)*p[0]/149.8 )+1)+1";
		
		String formula2 = "((Math.pow(p[0]-240,2)+Math.pow(p[1]-240,2))<10000) ? 255:0";
		
		RandomAccessibleInterval<T> rai=Views.interval(blank, new long[]{32,32}, new long[]{480,480});
		
		IterableInterval<T> ii=Views.iterable(Views.zeroMin(rai));
		
		//Img<T> exponentialChirp = (Img<T>)ij.op().image().equation((IterableInterval)ii, formula);
		
		ij.op().image().equation((IterableInterval<T>)ii, formula);
		
		ij.ui().show(blank);
		
		Img<T> kernel=(Img)ij.op().create().kernelGauss(new double[]{12.0, 12.0});
		
		ij.ui().show(kernel);
		
		Img<FloatType> convolved=ij.op().create().img(blank, new FloatType());
		ij.op().filter().convolve(convolved, blank, kernel);
		
		ij.ui().show("convolved", convolved);
		
		RandomAccessibleInterval<T> cropped=(RandomAccessibleInterval<T>)ij.op().transform().crop(convolved, new FinalInterval(511,256));
		
		ij.ui().show("cropped", cropped);
		
		Img<FloatType> deconvolved=ij.op().create().img(convolved, new FloatType());
		Img<FloatType> deconvolvedcropped=ij.op().create().img(cropped, new FloatType());
				
		ij.op().deconvolve().richardsonLucy(deconvolved, convolved, kernel, null, null, null, null, null, 20, false, true);
		ij.op().deconvolve().richardsonLucy(deconvolvedcropped, deconvolved, kernel, null, null, null, null, null, 20, true, true);
		
		ij.ui().show("deconvolved", deconvolved);
		ij.ui().show("deconvolvedcropped", deconvolvedcropped);
		
	}

}
