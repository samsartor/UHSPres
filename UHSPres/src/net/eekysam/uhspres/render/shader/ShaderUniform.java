package net.eekysam.uhspres.render.shader;

import java.awt.Color;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import net.eekysam.uhspres.utils.graphics.GLUtils;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class ShaderUniform
{
	public static enum EnumUniformCalls
	{
		UNIFORM_1F("glUniform1f", float.class),
		UNIFORM_2F("glUniform2f", float.class, float.class),
		UNIFORM_3F("glUniform3f", float.class, float.class, float.class),
		UNIFORM_4F("glUniform4f", float.class, float.class, float.class, float.class),
		UNIFORM_1I("glUniform1i", int.class),
		UNIFORM_2I("glUniform2i", int.class, int.class),
		UNIFORM_3I("glUniform3i", int.class, int.class, int.class),
		UNIFORM_4I("glUniform4i", int.class, int.class, int.class, int.class),
		UNIFORM_1FV("glUniform1", FloatBuffer.class),
		UNIFORM_2FV("glUniform2", FloatBuffer.class),
		UNIFORM_3FV("glUniform3", FloatBuffer.class),
		UNIFORM_4FV("glUniform4", FloatBuffer.class),
		UNIFORM_1IV("glUniform1", IntBuffer.class),
		UNIFORM_2IV("glUniform2", IntBuffer.class),
		UNIFORM_3IV("glUniform3", IntBuffer.class),
		UNIFORM_4IV("glUniform4", IntBuffer.class),
		UNIFORM_MATRIX_2FV("glUniformMatrix2", boolean.class, FloatBuffer.class),
		UNIFORM_MATRIX_3FV("glUniformMatrix3", boolean.class, FloatBuffer.class),
		UNIFORM_MATRIX_4FV("glUniformMatrix4", boolean.class, FloatBuffer.class);
		
		public final Method call;
		
		EnumUniformCalls(String name, Class<?>... types)
		{
			this(GL20.class, name, types);
		}
		
		EnumUniformCalls(Class<?> gl, String name, Class<?>... types)
		{
			Method call;
			Class<?>[] params = new Class<?>[types.length + 1];
			params[0] = int.class;
			for (int i = 0; i < types.length; i++)
			{
				params[i + 1] = types[i];
			}
			try
			{
				call = gl.getMethod(name, params);
			}
			catch (NoSuchMethodException | SecurityException e)
			{
				System.err.println("Could not find glUniform call: " + name);
				call = null;
			}
			this.call = call;
		}
	}
	
	public String name;
	private Method call = null;
	private Object[] data = null;
	
	public ShaderUniform()
	{
		this("");
	}
	
	public ShaderUniform(String name)
	{
		this.name = name;
	}
	
	public boolean upload(Program program, String name)
	{
		this.name = name;
		return this.upload(program);
	}
	
	public boolean upload(Program program)
	{
		int pid = program.getProgram();
		if (pid <= 0)
		{
			return false;
		}
		if (this.call == null || this.data == null)
		{
			return false;
		}
		int loc = GL20.glGetUniformLocation(pid, this.name);
		if (loc == -1)
		{
			
			System.err.printf("Could not find uniform: %s%n", this.name);
			for (StackTraceElement elm : Thread.currentThread().getStackTrace())
			{
				//System.err.println(elm);
			}
			return false;
		}
		Object[] pars = new Object[this.data.length + 1];
		pars[0] = loc;
		for (int i = 0; i < this.data.length; i++)
		{
			pars[i + 1] = this.data[i];
		}
		try
		{
			this.call.invoke(null, pars);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			System.err.printf("Could not call %s with uniform (%s), it threw a:%n", this.call.getName(), this.name);
			e.printStackTrace(System.err);
			return false;
		}
		return true;
	}
	
	public void set(EnumUniformCalls call, Object... data)
	{
		this.call = call.call;
		this.data = data;
	}
	
	public void setInt(int value)
	{
		this.call = EnumUniformCalls.UNIFORM_1I.call;
		this.data = new Object[] { value };
	}
	
	public void setFloat(float value)
	{
		this.call = EnumUniformCalls.UNIFORM_1F.call;
		this.data = new Object[] { value };
	}
	
	public void setInts(int... value)
	{
		switch (value.length)
		{
			case 1:
				this.call = EnumUniformCalls.UNIFORM_1I.call;
				this.data = this.getArray(value);
				return;
			case 2:
				this.call = EnumUniformCalls.UNIFORM_2I.call;
				this.data = this.getArray(value);
				return;
			case 3:
				this.call = EnumUniformCalls.UNIFORM_3I.call;
				this.data = this.getArray(value);
				return;
			case 4:
				this.call = EnumUniformCalls.UNIFORM_4I.call;
				this.data = this.getArray(value);
				return;
			default:
				this.call = EnumUniformCalls.UNIFORM_1IV.call;
				this.data = new Object[] { GLUtils.bufferInts(value) };
				return;
		}
	}
	
	public void setInts(IntBuffer buf)
	{
		this.call = EnumUniformCalls.UNIFORM_1IV.call;
		this.data = new Object[] { buf };
	}
	
	public void setFloats(float... value)
	{
		switch (value.length)
		{
			case 1:
				this.call = EnumUniformCalls.UNIFORM_1F.call;
				this.data = this.getArray(value);
				return;
			case 2:
				this.call = EnumUniformCalls.UNIFORM_2F.call;
				this.data = this.getArray(value);
				return;
			case 3:
				this.call = EnumUniformCalls.UNIFORM_3F.call;
				this.data = this.getArray(value);
				return;
			case 4:
				this.call = EnumUniformCalls.UNIFORM_4F.call;
				this.data = this.getArray(value);
				return;
			default:
				this.call = EnumUniformCalls.UNIFORM_1FV.call;
				this.data = new Object[] { GLUtils.bufferFloats(value) };
				return;
		}
	}
	
	public void setFloats(FloatBuffer buf)
	{
		this.call = EnumUniformCalls.UNIFORM_1FV.call;
		this.data = new Object[] { buf };
	}
	
	public void setVector(Vector value)
	{
		if (value instanceof Vector2f)
		{
			this.setVectors((Vector2f) value);
		}
		else if (value instanceof Vector3f)
		{
			this.setVectors((Vector3f) value);
		}
		else if (value instanceof Vector4f)
		{
			this.setVectors((Vector4f) value);
		}
		else if (value instanceof Quaternion)
		{
			this.setVectors((Quaternion) value);
		}
	}
	
	private void setVectors(int size, Vector... value)
	{
		FloatBuffer buf = BufferUtils.createFloatBuffer(size * value.length);
		for (int i = 0; i < value.length; i++)
		{
			value[i].store(buf);
		}
		buf.flip();
		this.data = new Object[] { buf };
	}
	
	public void setVectors(Vector2f... value)
	{
		this.call = EnumUniformCalls.UNIFORM_2FV.call;
		this.setVectors(2, value);
	}
	
	public void setVectors(Vector3f... value)
	{
		this.call = EnumUniformCalls.UNIFORM_3FV.call;
		this.setVectors(3, value);
	}
	
	public void setVectors(Vector4f... value)
	{
		this.call = EnumUniformCalls.UNIFORM_4FV.call;
		this.setVectors(4, value);
	}
	
	public void setVectors(Quaternion... value)
	{
		this.call = EnumUniformCalls.UNIFORM_4FV.call;
		this.setVectors(4, value);
	}
	
	public void setFloats(int size, float[] data)
	{
		if (data.length % size != 0)
		{
			throw new IllegalArgumentException("The length of data is not a multiple of size.");
		}
		switch (size)
		{
			case 1:
				this.call = EnumUniformCalls.UNIFORM_1FV.call;
				break;
			case 2:
				this.call = EnumUniformCalls.UNIFORM_2FV.call;
				break;
			case 3:
				this.call = EnumUniformCalls.UNIFORM_3FV.call;
				break;
			case 4:
				this.call = EnumUniformCalls.UNIFORM_4FV.call;
				break;
			default:
				throw new IllegalArgumentException("Size must be less than or equal to 4 and at least 1.");
		}
		this.data = new Object[] { GLUtils.bufferFloats(data) };
	}
	
	public void setInts(int size, int[] data)
	{
		if (data.length % size != 0)
		{
			throw new IllegalArgumentException("The length of data is not a multiple of size.");
		}
		switch (size)
		{
			case 1:
				this.call = EnumUniformCalls.UNIFORM_1IV.call;
				break;
			case 2:
				this.call = EnumUniformCalls.UNIFORM_2IV.call;
				break;
			case 3:
				this.call = EnumUniformCalls.UNIFORM_3IV.call;
				break;
			case 4:
				this.call = EnumUniformCalls.UNIFORM_4IV.call;
				break;
			default:
				throw new IllegalArgumentException("Size must be less than or equal to 4 and at least 1.");
		}
		this.data = new Object[] { GLUtils.bufferInts(data) };
	}
	
	public void setMatrix(Matrix value)
	{
		this.setMatrix(value, false);
	}
	
	public void setMatrix(Matrix value, boolean transpose)
	{
		if (value instanceof Matrix2f)
		{
			this.setMatricies(transpose, (Matrix2f) value);
		}
		else if (value instanceof Matrix3f)
		{
			this.setMatricies(transpose, (Matrix3f) value);
		}
		else if (value instanceof Matrix4f)
		{
			this.setMatricies(transpose, (Matrix4f) value);
		}
	}
	
	private void setMatricies(int size, boolean transpose, Matrix... value)
	{
		FloatBuffer buf = BufferUtils.createFloatBuffer(size * size * value.length);
		for (int i = 0; i < value.length; i++)
		{
			value[i].store(buf);
		}
		buf.flip();
		this.data = new Object[] { transpose, buf };
	}
	
	public void setMatricies(Matrix2f... value)
	{
		this.setMatricies(false, value);
	}
	
	public void setMatricies(boolean transpose, Matrix2f... value)
	{
		this.call = EnumUniformCalls.UNIFORM_MATRIX_2FV.call;
		this.setMatricies(2, transpose, value);
	}
	
	public void setMatricies(Matrix3f... value)
	{
		this.setMatricies(false, value);
	}
	
	public void setMatricies(boolean transpose, Matrix3f... value)
	{
		this.call = EnumUniformCalls.UNIFORM_MATRIX_3FV.call;
		this.setMatricies(3, transpose, value);
	}
	
	public void setMatricies(Matrix4f... value)
	{
		this.setMatricies(false, value);
	}
	
	public void setMatricies(boolean transpose, Matrix4f... value)
	{
		this.call = EnumUniformCalls.UNIFORM_MATRIX_4FV.call;
		this.setMatricies(4, transpose, value);
	}
	
	public void setColorRGB(Color value)
	{
		this.setColorsRGB(value);
	}
	
	public void setColorsRGB(Color... value)
	{
		FloatBuffer buf = BufferUtils.createFloatBuffer(3 * value.length);
		float[] comps = new float[3];
		for (int i = 0; i < value.length; i++)
		{
			value[i].getRGBColorComponents(comps);
			buf.put(comps);
		}
		buf.flip();
		this.call = EnumUniformCalls.UNIFORM_3FV.call;
		this.data = new Object[] { buf };
	}
	
	public void setColorRGBA(Color value)
	{
		this.setColorsRGBA(value);
	}
	
	public void setColorsRGBA(Color... value)
	{
		FloatBuffer buf = BufferUtils.createFloatBuffer(4 * value.length);
		float[] comps = new float[4];
		for (int i = 0; i < value.length; i++)
		{
			value[i].getRGBComponents(comps);
			buf.put(comps);
		}
		buf.flip();
		this.call = EnumUniformCalls.UNIFORM_4FV.call;
		this.data = new Object[] { buf };
	}
	
	private Object[] getArray(Object val)
	{
		if (val instanceof Object[])
		{
			return (Object[]) val;
		}
		int arrlength = Array.getLength(val);
		Object[] outputArray = new Object[arrlength];
		for (int i = 0; i < arrlength; ++i)
		{
			outputArray[i] = Array.get(val, i);
		}
		return outputArray;
	}
}
