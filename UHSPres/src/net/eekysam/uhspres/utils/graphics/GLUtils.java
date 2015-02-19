package net.eekysam.uhspres.utils.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class GLUtils
{
	public static IntBuffer bufferInts(int... values)
	{
		IntBuffer buff = BufferUtils.createIntBuffer(values.length);
		buff.put(values);
		buff.flip();
		return buff;
	}

	public static IntBuffer bufferInts(List<Integer> values)
	{
		int[] array = new int[values.size()];
		for (int i = 0; i < array.length; i++)
		{
			array[i] = values.get(i);
		}
		return bufferInts(array);
	}

	public static FloatBuffer bufferFloats(float... values)
	{
		FloatBuffer buff = BufferUtils.createFloatBuffer(values.length);
		buff.put(values);
		buff.flip();
		return buff;
	}

	public static FloatBuffer bufferFloats(List<Float> values)
	{
		float[] array = new float[values.size()];
		for (int i = 0; i < array.length; i++)
		{
			array[i] = values.get(i);
		}
		return bufferFloats(array);
	}

	public static void glTexParameter(int target, int pname, int... param)
	{
		GL11.glTexParameter(target, pname, bufferInts(param));
	}

	public static void glTexParameter(int target, int pname, float... param)
	{
		GL11.glTexParameter(target, pname, bufferFloats(param));
	}
}