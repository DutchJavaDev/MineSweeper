/**
 * 28 feb. 2019
 */
package com.msp.util;

/**
 * @author Boris Mulder
 *
 */
public class MatrixHelper
{
	public static boolean isInRange(int r, int c, int MatrixHeight,int MatrixWidth)
	{
		return (r >= 0 && r <= MatrixHeight-1) && (c >= 0 && c <= MatrixWidth-1);
	}
}
