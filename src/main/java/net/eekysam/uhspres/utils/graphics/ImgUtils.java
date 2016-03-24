package net.eekysam.uhspres.utils.graphics;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class ImgUtils
{
	public static enum EnumPixelByte
	{
		RED(16),
		GREEN(8),
		BLUE(0),
		ALPHA(24);
		
		public final int bitshift;
		
		EnumPixelByte(int bitshift)
		{
			this.bitshift = bitshift;
		}
	}
	
	public static ByteBuffer imageToBuffer(BufferedImage image, EnumPixelByte... bytes)
	{
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * bytes.length);
		for (int y = 0; y < image.getHeight(); y++)
		{
			for (int x = 0; x < image.getWidth(); x++)
			{
				int pixel = pixels[y * image.getWidth() + x];
				for (EnumPixelByte part : bytes)
				{
					buffer.put((byte) ((pixel >> part.bitshift) & 0xFF));
				}
			}
		}
		buffer.flip();
		return buffer;
	}
	
	public static ByteBuffer imageToBufferDefault(BufferedImage image)
	{
		return imageToBuffer(image, EnumPixelByte.RED, EnumPixelByte.GREEN, EnumPixelByte.BLUE, EnumPixelByte.ALPHA);
	}
}