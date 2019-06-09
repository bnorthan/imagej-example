
package com.example.viewingimages;

import net.imagej.ImageJ;
import net.imagej.ops.OpService;
import net.imglib2.Point;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.region.hypersphere.HyperSphere;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
*/

@Plugin(type = Command.class,
	menuPath = "Plugins>Learnathon>Draw A Sphere (IJ2)")
public class DrawSphereInCenter<T extends RealType<T> & NativeType<T>>
	implements Command
{

	@Parameter
	OpService ops;

	@Parameter
	RandomAccessibleInterval<T> rai;

	@Parameter
	ImageJ ij;

	@Override
	public void run() {
		final Point center = new Point(rai.numDimensions());

		for (int d = 0; d < rai.numDimensions(); d++)
			center.setPosition(rai.dimension(d) / 2, d);

		long radius = Math.min(rai.dimension(0), Math.min(rai.dimension(1), rai
			.dimension(2)));

		T intensity = ops.stats().max(Views.iterable(rai));

		HyperSphere<T> hyperSphere = new HyperSphere<>(rai, center, radius);

		for (final T value : hyperSphere) {
			value.setReal(intensity.getRealFloat());
		}
	}
}
