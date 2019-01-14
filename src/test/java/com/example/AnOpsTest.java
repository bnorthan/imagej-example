package com.example;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Test;

public class AnOpsTest extends AbstractOpTest {

	@Test
	public void testAnOp() {
		Img<FloatType> img=this.generateFloatArrayTestImg(true, new long[] {128,128});
		
		float sum=ops.stats().sum(img).getRealFloat();
		
		System.out.println(sum);
		
		assertEquals(sum, 57.530945, 0.0001);
		
	}
}
