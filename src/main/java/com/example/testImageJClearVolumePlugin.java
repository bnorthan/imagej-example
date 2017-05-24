package com.example;

import java.io.File;
import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.display.DatasetView;
import net.imagej.display.ImageDisplay;

/**
 *
 */

/**
 * @author jug
 */
public class testImageJClearVolumePlugin {

	public static void main( final String[] args ) {
		final String fname = "convolved.tif";

		final File file = new File( fname );

		final ImageJ ij = new ImageJ();
		try {
			Dataset ds = null;
			DatasetView dsv = null;
			if ( file.exists() && file.canRead() ) {
				ds = ij.scifio().datasetIO().open( fname );
//				dsv = ( DatasetView ) ij.imageDisplay().createDataView( ds );
//				dsv.rebuild();
				final ImageDisplay display = ( ImageDisplay ) ij.display().createDisplay( ds );
				dsv = ij.imageDisplay().getActiveDatasetView( display );

			}

			ij.ui().showUI();

			if ( ds != null ) {
				ij.command().run(
						de.mpicbg.jug.plugins.ClearVolumePlugin.class,
						true,
						"datasetView",
						dsv );
			}
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}
}
