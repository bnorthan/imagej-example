
package com.example.viewingimages;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import net.imagej.ImageJ;
import net.imglib2.Cursor;
import net.imglib2.FinalDimensions;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class Ex5_ReadThunderStormCSVAndRender {

	static ArrayList<Float> x = new ArrayList<>();
	static ArrayList<Float> y = new ArrayList<>();

	public static <T extends RealType<T> & NativeType<T>> void main(
		final String[] args)
	{

		// where are we in the file system??
		System.out.println("Working Directory = " + System.getProperty("user.dir"));

		// create an instance of imagej
		final ImageJ ij = new ImageJ();

		// launch it
		ij.launch(args);

		Img<FloatType> img = ij.op().create().img(new FinalDimensions(1000, 1000),
			new FloatType());

		RandomAccessibleInterval<FloatType> guassian = ij.op().create().kernelGauss(
			new double[] { 1.0, 1.0 }, new FloatType());

		readCSV();

		float maxX = Collections.max(x);
		float maxY = Collections.max(y);

		for (int i = 0; i < x.size(); i++) {

			long x_ = (long) (img.dimension(0) * x.get(i) / maxX);
			long y_ = (long) (img.dimension(1) * y.get(i) / maxY);

			long[] start = new long[] { (x_ - guassian.dimension(0) / 2), (y_ -
				guassian.dimension(1) / 2) };
			long[] end = new long[] { start[0] + guassian.dimension(0) - 1, start[1] +
				guassian.dimension(1) - 1 };

			if ((start[0] >= 0) && (start[1] >= 0) && (end[0] < img.dimension(0)) &&
				(end[1] < img.dimension(1)))
			{

				RandomAccessibleInterval<FloatType> rai = Views.interval(img,
					new FinalInterval(start, end));

				Cursor<FloatType> c1 = Views.iterable(Views.zeroMin(rai)).cursor();
				Cursor<FloatType> c2 = Views.iterable(guassian).cursor();

				while (c1.hasNext()) {
					c1.fwd();
					c2.fwd();

					c1.get().add(c2.get());
				}

				// ij.op().math().add(Views.iterable(Views.zeroMin(rai)), guassian,
				// Views
				// .iterable(Views.zeroMin(rai)));
			}

		}

		ij.ui().show(img);

	}

	// from https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
	public static void readCSV() {

		String csvFile = "../images/thunderstorm.csv";
		String line = "";
		String cvsSplitBy = ",";

		BufferedReader br = null;

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] split = line.split(cvsSplitBy);

				try {
					x.add(Float.parseFloat(split[1]));
					y.add(Float.parseFloat(split[2]));

					// System.out.println(line);
				}
				catch (Exception ex) {

				}
			}

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (br != null) {
				try {
					br.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
