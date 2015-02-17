package net.eekysam.uhspres.utils.graphics;

public class RgbConvert
{
	public static int getInt(int r, int g, int b)
	{
		int rgb = r;
		rgb = (rgb << 8) + g;
		rgb = (rgb << 8) + b;
		return rgb;
	}
	
	public static int getRed(int rgb)
	{
		return (rgb >> 16) & 0xFF;
	}
	
	public static int getGreen(int rgb)
	{
		return (rgb >> 8) & 0xFF;
	}
	
	public static int getBlue(int rgb)
	{
		return rgb & 0xFF;
	}
}
