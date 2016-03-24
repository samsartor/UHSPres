package net.eekysam.uhspres.utils.geo;

import org.lwjgl.util.vector.Vector3f;

/**
 * A vector
 */
public class Vector
{
	public float xpart;
	public float ypart;
	public float zpart;
	
	/**
	 * Creates a new vector.
	 */
	public static Vector getVector(float xpart, float ypart, float zpart)
	{
		return new Vector(xpart, ypart, zpart);
	}
	
	/**
	 * Gets the vector from origin to the given point.
	 */
	public static Vector getVector(Point point)
	{
		return new Vector((float) point.xCoord, (float) point.yCoord, (float) point.zCoord);
	}
	
	/**
	 * Gets the vector from the first point to the second point.
	 */
	public static Vector getVector(Point p1, Point p2)
	{
		return new Vector((float) (p2.xCoord - p1.xCoord), (float) (p2.yCoord - p1.yCoord), (float) (p2.zCoord - p1.zCoord));
	}
	
	/**
	 * Gets the vector of the given ray.
	 */
	public static Vector getVector(Ray ray)
	{
		return getVector(ray.start, ray.end);
	}
	
	public static Vector getVector(Vector3f vector)
	{
		return new Vector(vector.x, vector.y, vector.z);
	}
	
	private Vector(float x, float y, float z)
	{
		this.xpart = x;
		this.ypart = y;
		this.zpart = z;
	}
	
	/**
	 * Gets the length of this vector squared.
	 */
	public float lengthSqr()
	{
		return this.xpart * this.xpart + this.ypart * this.ypart + this.zpart * this.zpart;
	}
	
	/**
	 * Gets the length of this vector.
	 */
	public float length()
	{
		return (float) Math.sqrt(this.lengthSqr());
	}
	
	/**
	 * Normalize this vector.
	 */
	public void normalize()
	{
		this.changeLength(1.0F);
	}
	
	/**
	 * Changes the length of this vector.
	 * 
	 * @param length The new length
	 */
	public void changeLength(float length)
	{
		this.scale(length / this.length());
	}
	
	/**
	 * Scales this vector.
	 * 
	 * @param scale The ratio to scale this vector by
	 */
	public void scale(float scale)
	{
		this.xpart *= scale;
		this.ypart *= scale;
		this.zpart *= scale;
	}
	
	/**
	 * Cross Product of A and B
	 * <p>
	 * <li>Finds a vector that is 90° to two other vectors</li>
	 * <li>Finds a the normal vector of a plane defined by two other vectors</li>
	 * </p>
	 * 
	 * @param a Vector A
	 * @param b Vector B
	 * @return A x B
	 * @code [ya * zb - za * yb, za * xb - xz * zb, xa * yb - yz * xb]
	 */
	public static Vector cross(Vector a, Vector b)
	{
		float x = a.ypart * b.zpart - a.zpart * b.ypart;
		float y = a.zpart * b.xpart - a.xpart * b.zpart;
		float z = a.xpart * b.ypart - a.ypart * b.xpart;
		
		return new Vector(x, y, z);
	}
	
	/**
	 * Dot Product of A and B
	 * <p>
	 * <li>God knows what is does, but it is useful</li>
	 * </p>
	 * 
	 * @param a Vector A
	 * @param b Vector B
	 * @return dot(A, B)
	 * @code xa * xb + ya * yb + za * zb
	 */
	public static float dot(Vector a, Vector b)
	{
		return a.xpart * b.xpart + a.ypart * b.ypart + a.zpart * b.zpart;
	}
	
	/**
	 * Cos of the angle between A and B
	 * 
	 * @param a Vector A
	 * @param b Vector B
	 * @return cos(theta)
	 * @code dot(A, B) / (|A| * |B|)
	 */
	public static float cosTheta(Vector a, Vector b)
	{
		float num = dot(a, b);
		float den = (float) Math.sqrt(a.magnitudeSqr() * b.magnitudeSqr());
		return num / den;
	}
	
	/**
	 * The angle between two vectors
	 * 
	 * @param a Vector A
	 * @param b Vector B
	 * @return theta
	 * @code acos(dot(A, B) / (|A| * |B|))
	 */
	public static float theta(Vector a, Vector b)
	{
		return (float) Math.acos(cosTheta(a, b));
	}
	
	/**
	 * The length of vector A when projected onto this vector
	 * <p>
	 * <li>The length of the bottom of a triangle with vector A as the
	 * hypotonuse and with the bottom along this vector
	 * </p>
	 * <li>The length of the shadow cast by vector A onto this vector from a
	 * light source 90° to this vector</p> </P>
	 * 
	 * @param a Vector A
	 * @return |A| * cos(theta)
	 * @code dot(A, this) / |this|
	 */
	public float scalarProjection(Vector a)
	{
		return dot(a, this) / this.magnitude();
	}
	
	/**
	 * The length if you want to sound pretentious
	 * 
	 * @return |this|
	 * @code sqrt(x * x + y * y + z * z)
	 */
	public float magnitude()
	{
		return (float) Math.sqrt(this.magnitudeSqr());
	}
	
	/**
	 * The length squared if you want to sound pretentious and not use square
	 * roots
	 * 
	 * @return |this|^2
	 * @code x * x + y * y + z * z
	 */
	public float magnitudeSqr()
	{
		return this.xpart * this.xpart + this.ypart * this.ypart + this.zpart * this.zpart;
	}
	
	public Vector returnNormalized()
	{
		this.normalize();
		return this;
	}
	
	public Vector3f getGLVec()
	{
		return new Vector3f(this.xpart, this.ypart, this.zpart);
	}
}
