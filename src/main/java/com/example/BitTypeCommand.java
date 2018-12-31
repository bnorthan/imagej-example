package com.example;

import net.imagej.ImageJ;
import net.imagej.ImgPlus;
import net.imagej.ops.OpService;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Command.class, headless = true, menuPath = "Plugins>BitTypeCommand")
public class BitTypeCommand<T extends RealType<T> & NativeType<T>> implements Command {

	@Parameter
	OpService ops;
	
	@Parameter(validater = "inputImage")
	private ImgPlus<T> imgPlus;
	
	@Parameter(type = ItemIO.OUTPUT)
	private Img<BitType> convertedImg;
	
	@Override
	public void run() {	
		convertedImg= ops.convert().bit(imgPlus.getImg());
	}
	
	public static <T extends RealType<T> & NativeType<T>> void main(final String[] args) throws InterruptedException {

		// create an instance of imagej
		final ImageJ ij = new ImageJ();

		// launch it
		ij.launch(args);
	}
}
