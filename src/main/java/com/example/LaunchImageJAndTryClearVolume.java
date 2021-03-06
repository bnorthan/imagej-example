package com.example;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.scijava.command.Command;
import org.scijava.plugin.Plugin;

//import clearvolume.renderer.ClearVolumeRendererInterface;
//import clearvolume.renderer.factory.ClearVolumeRendererFactory;
//import clearvolume.transferf.TransferFunctions;
//import coremem.enums.NativeTypeEnum;
//import de.mpicbg.jug.clearvolume.ImgLib2ClearVolume;
import net.imagej.ImageJ;
import net.imglib2.FinalDimensions;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.stats.Normalize;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;


public class LaunchImageJAndTryClearVolume {
	public static <T extends RealType<T> & NativeType<T>> void main(final String[] args) throws InterruptedException {

		// create an instance of imagej
		final ImageJ ij = new ImageJ();

		// launch it
		ij.launch(args);
		
	
		// define an x and y size
		final int xSize = 128;
		final int ySize = 128;

		// create a new image
		final Img<FloatType> img = (Img<FloatType>) ij.op().create()
				.img(new FinalDimensions(new long[] { xSize, ySize }), new FloatType());

		// get a 'RandomAccess' object, this object allows access to pixels at
		// specific locations
		final RandomAccess<FloatType> ra = img.randomAccess();

		// move to the middle of the image and set the pixel value there to 255
		ra.setPosition(new long[] { xSize / 2, ySize / 2 });
		ra.get().setReal(255.0);

		// move the ra to a new position, and set another pixel to 255
		ra.setPosition(new long[] { xSize / 4, ySize / 4 });
		ra.get().setReal(255.0);

		// show the image
		ij.ui().show(img);
		
		//clearVolumeExample();
		
		//clearVolumeShowImg(img);

	}
/*	
	private static void clearVolumeShowImg(Img<FloatType> img) {
		
		final List< RandomAccessibleInterval< FloatType >> imgs =
				new ArrayList< RandomAccessibleInterval< FloatType >>();
		imgs.add( img );

		final long toc = System.currentTimeMillis();
		//System.out.println( String.format( " ...done in %d ms.", toc - tic ) );

		Normalize.normalize( img, new FloatType( 0f ), new FloatType( 1f ) );
		final ClearVolumeRendererInterface cv =
				ImgLib2ClearVolume.initRealImgs(
						imgs,
						null,
						"Img -> ClearVolume",
						512, 512,
						512, 512,
						false,
						new double[] { 0. },
						new double[] { 1.0 },
						true );
		cv.setVoxelSize( 1., 1., 3.5 );
		cv.requestDisplay();

		while ( cv.isShowing() ) {
			try {
				Thread.sleep( 500 );
			} catch ( final InterruptedException e ) {
				e.printStackTrace();
			}
		}
		cv.close();
	}
	
	static void clearVolumeExample() throws InterruptedException {
		// obtain the best renderer, this usually means picking the best supported GPU programming framwork such as CUDA or OpenCL or GLSL on the most performant GPU installed.
		final ClearVolumeRendererInterface lClearVolumeRenderer = ClearVolumeRendererFactory.newBestRenderer("ClearVolumeTest",768,768,NativeTypeEnum.UnsignedShort,768,768,true);

		// Different transfer functions can be used:
		lClearVolumeRenderer.setTransferFunction(TransferFunctions.getGrayLevel());
		lClearVolumeRenderer.setVisible(true);

		// volume dimensions:
		final int lResolutionX = 256;
		final int lResolutionY = 256;
		final int lResolutionZ = 256;

		// size of the buffer in bytes (16bit data)
		final int lLengthInBytes = lResolutionX * lResolutionY* lResolutionZ* 2;

		// standard java byte array for holding the data:
		final byte[] lVolumeDataArray = new byte[lLengthInBytes];

		// Filling the buffer with some dummy yet interesting looking data:
		for (int z = 0; z < lResolutionZ; z++)
			for (int y = 0; y < lResolutionY; y++)
				for (int x = 0; x < lResolutionX; x++)
				{
					final int lIndex = 2 * (x + lResolutionX * y + lResolutionX * lResolutionY * z);
					lVolumeDataArray[lIndex + 1] = (byte) (((byte) x ^ (byte) y ^ (byte) z));
				}

		lClearVolumeRenderer.setVisible(true);
				
		// sets the current buffer to be displayed:
		lClearVolumeRenderer.setVolumeDataBuffer(0, ByteBuffer.wrap(lVolumeDataArray), lResolutionX, lResolutionY, lResolutionZ);

		// Waits for the renderer's window to be closed:
		while (lClearVolumeRenderer.isShowing())
		{
			Thread.sleep(100);
		}

		// closes the renderer and release all ressources:
		lClearVolumeRenderer.close();

		
	}
	*/
}
