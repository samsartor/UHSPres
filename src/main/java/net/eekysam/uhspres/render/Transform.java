package net.eekysam.uhspres.render;

import java.util.EnumMap;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Transform
{
	public enum MatrixMode
	{
		MODEL(0b001),
		VIEW(0b010),
		PROJECT(0b100);
		
		public final int bit;
		
		MatrixMode(int bit)
		{
			this.bit = bit;
		}
		
		public boolean isInCode(int code)
		{
			return (this.bit & code) != 0;
		}
	}
	
	private final EnumMap<MatrixMode, Matrix4f> transform = new EnumMap<MatrixMode, Matrix4f>(MatrixMode.class);
	
	public Transform()
	{
		this.transform.put(MatrixMode.MODEL, new Matrix4f());
		this.transform.put(MatrixMode.VIEW, new Matrix4f());
		this.transform.put(MatrixMode.PROJECT, new Matrix4f());
		this.setIdentity(0b111);
	}
	
	public int getCode(MatrixMode... modes)
	{
		int code = 0;
		for (MatrixMode mode : modes)
		{
			code |= mode.bit;
		}
		return code;
	}
	
	public void setIdentity(int code)
	{
		for (MatrixMode mode : MatrixMode.values())
		{
			if (mode.isInCode(code))
			{
				this.transform.get(mode).setIdentity();
			}
		}
	}
	
	public Matrix4f get(MatrixMode mode)
	{
		return this.transform.get(mode);
	}
	
	public Matrix4f getCopy(MatrixMode mode)
	{
		Matrix4f copy = new Matrix4f();
		copy.load(this.get(mode));
		return copy;
	}
	
	public Matrix4f getResult(int code)
	{
		Matrix4f result = new Matrix4f();
		result.setIdentity();
		for (int i = MatrixMode.values().length - 1; i >= 0; i--)
		{
			MatrixMode mode = MatrixMode.values()[i];
			if (mode.isInCode(code))
			{
				Matrix4f.mul(result, this.get(mode), result);
			}
		}
		return result;
	}
	
	public Matrix4f getMVP()
	{
		return this.getResult(0b111);
	}
	
	public void set(MatrixMode mode, Matrix4f matrix)
	{
		this.get(mode).load(matrix);
	}
	
	public void translate(MatrixMode mode, Vector3f vec)
	{
		this.get(mode).translate(vec);
	}
	
	public void translate(MatrixMode mode, Vector2f vec)
	{
		this.get(mode).translate(vec);
	}
	
	public void rotate(MatrixMode mode, Vector3f axis, float angle)
	{
		this.get(mode).rotate(angle, axis);
	}
	
	public void scale(MatrixMode mode, Vector3f vec)
	{
		this.get(mode).scale(vec);
	}
	
	public void setIdentity(MatrixMode mode)
	{
		this.get(mode).setIdentity();
	}
	
	public static void createPerspectiveFrustrum(Matrix4f mat, float n, float f, float r, float l, float t, float b)
	{
		mat.setZero();
		mat.m00 = 2 * n / (r - l);
		mat.m11 = 2 * n / (t - b);
		mat.m22 = -(f + n) / (f - n);
		mat.m32 = -(2 * f * n) / (f - n);
		mat.m23 = -1;
		mat.m21 = (t + b) / (t - b);
		mat.m20 = (r + l) / (r - l);
	}
	
	public static void createPerspectiveFrustrum(Matrix4f mat, float near, float far, float width, float height)
	{
		createPerspectiveFrustrum(mat, near, far, width / 2, -width / 2, height / 2, -height / 2);
	}
	
	public static void createPerspective(Matrix4f mat, float fov, float aspect, float near, float far)
	{
		float height = near * (float) Math.tan((Math.PI / 180) * (fov / 2)) * 2;
		float width = height * aspect;
		createPerspectiveFrustrum(mat, near, far, width, height);
	}
}
