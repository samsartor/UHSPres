package net.eekysam.uhspres.utils.geo;

import org.lwjgl.util.vector.Vector3f;

/**
 * A point in 3 dimensional space.
 */
public class Point
{
	public double xCoord;
	public double yCoord;
	public double zCoord;
	
	/**
	 * Creates a new point.
	 */
	public static Point getPoint(double x, double y, double z)
	{
		return new Point(x, y, z);
	}
	
	public static Point getPoint(Vector3f vector)
	{
		return new Point(vector.x, vector.y, vector.z);
	}
	
	private Point(double x, double y, double z)
	{
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
	}
	
	/**
	 * Sets the location of this point.
	 */
	public Point setPoint(double x, double y, double z)
	{
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
		return this;
	}
	
	/**
	 * Translates this point by the given amount.
	 */
	public void moveDelta(double dx, double dy, double dz)
	{
		this.xCoord += dx;
		this.yCoord += dy;
		this.zCoord += dz;
	}
	
	/**
	 * Gets the distance squared from the point to the origin.
	 */
	public double squareDistanceOrigin()
	{
		double xs = this.xCoord * this.xCoord;
		double ys = this.yCoord * this.yCoord;
		double zs = this.zCoord * this.zCoord;
		
		return xs + ys + zs;
	}
	
	/**
	 * Gets the distance from the point to the origin.
	 */
	public double distanceOrigin()
	{
		return Math.sqrt(this.squareDistanceOrigin());
	}
	
	/**
	 * Gets the distance from this point to the given point.
	 */
	public double distanceTo(Point p)
	{
		return distance(this, p);
	}
	
	/**
	 * Gets the distance between two points.
	 */
	public static double distance(Point p1, Point p2)
	{
		return Math.sqrt(squareDistance(p1, p2));
	}
	
	/**
	 * Gets the distance squared from this point to the given point.
	 */
	public double squareDistanceTo(double px, double py, double pz)
	{
		double x = this.xCoord - px;
		double y = this.yCoord - py;
		double z = this.zCoord - pz;
		
		double xs = x * x;
		double ys = y * y;
		double zs = z * z;
		
		return xs + ys + zs;
	}
	
	/**
	 * Gets the distance squared from this point to the given point.
	 */
	public double squareDistanceTo(Point p)
	{
		return squareDistance(this, p);
	}
	
	/**
	 * Gets the distance squared between two points.
	 */
	public static double squareDistance(Point p1, Point p2)
	{
		double x = p1.xCoord - p2.xCoord;
		double y = p1.yCoord - p2.yCoord;
		double z = p1.zCoord - p2.zCoord;
		
		double xs = x * x;
		double ys = y * y;
		double zs = z * z;
		
		return xs + ys + zs;
	}
	
	/**
	 * Gets a point along the line between the given points.
	 * 
	 * @param p1 The first point
	 * @param p2 The second point
	 * @param xval The x coordinate of the new point
	 * @return The only point on the line with the given coordinate. null if the
	 *         two points are two close or if the given coordinate is not
	 *         between the points.
	 */
	public static Point getIntermediateWithXValue(Point p1, Point p2, double xval)
	{
		double x = p2.xCoord - p1.xCoord;
		double y = p2.yCoord - p1.yCoord;
		double z = p2.zCoord - p1.zCoord;
		
		if (x * x < 1E-7D)
		{
			return null;
		}
		else
		{
			double weight = (xval - p1.xCoord) / x;
			return weight >= 0.0D && weight <= 1.0D ? new Point(p1.xCoord + x * weight, p1.yCoord + y * weight, p1.zCoord + z * weight) : null;
		}
	}
	
	/**
	 * Translates this point by the coordinates of the given point.
	 */
	public void addPoint(Point p)
	{
		this.xCoord += p.xCoord;
		this.yCoord += p.yCoord;
		this.zCoord += p.zCoord;
	}
	
	/**
	 * Creates a new point by translating the given point by the given vector.
	 * 
	 * @param point The point to translate (Remains unchanged)
	 * @param vector The translation vector
	 * @return A new point
	 */
	public static Point addVector(Point point, Vector vector)
	{
		return new Point(point.xCoord + vector.xpart, point.yCoord + vector.ypart, point.zCoord + vector.zpart);
	}
	
	/**
	 * Gets a point along the line between the given points.
	 * 
	 * @param p1 The first point
	 * @param p2 The second point
	 * @param yval The y coordinate of the new point
	 * @return The only point on the line with the given coordinate. null if the
	 *         two points are two close or if the given coordinate is not
	 *         between the points.
	 */
	public static Point getIntermediateWithYValue(Point p1, Point p2, double yval)
	{
		double x = p2.xCoord - p1.xCoord;
		double y = p2.yCoord - p1.yCoord;
		double z = p2.zCoord - p1.zCoord;
		
		if (y * y < 1E-7D)
		{
			return null;
		}
		else
		{
			double weight = (yval - p1.yCoord) / y;
			return weight >= 0.0D && weight <= 1.0D ? new Point(p1.xCoord + x * weight, p1.yCoord + y * weight, p1.zCoord + z * weight) : null;
		}
	}
	
	/**
	 * Gets a point along or extended beyond the line created by the given
	 * points.
	 * 
	 * @param p1 The first point
	 * @param p2 The second point
	 * @param yval The y coordinate of the new point
	 * @return The only point on the line with the given coordinate. null if the
	 *         two points are two close.
	 */
	public static Point getSlideWithYValue(Point p1, Point p2, double yval)
	{
		double x = p2.xCoord - p1.xCoord;
		double y = p2.yCoord - p1.yCoord;
		double z = p2.zCoord - p1.zCoord;
		
		if (y * y < 1E-7D)
		{
			return null;
		}
		else
		{
			double weight = (yval - p1.yCoord) / y;
			return new Point(p1.xCoord + x * weight, p1.yCoord + y * weight, p1.zCoord + z * weight);
		}
	}
	
	/**
	 * Gets a point along the line between the given points.
	 * 
	 * @param p1 The first point
	 * @param p2 The second point
	 * @param zval The z coordinate of the new point
	 * @return The only point on the line with the given coordinate. null if the
	 *         two points are two close or if the given coordinate is not
	 *         between the points.
	 */
	public static Point getIntermediateWithZValue(Point p1, Point p2, double zval)
	{
		double x = p2.xCoord - p1.xCoord;
		double y = p2.yCoord - p1.yCoord;
		double z = p2.zCoord - p1.zCoord;
		
		if (z * z < 1E-7D)
		{
			return null;
		}
		else
		{
			double weight = (zval - p1.zCoord) / z;
			return weight >= 0.0D && weight <= 1.0D ? new Point(p1.xCoord + x * weight, p1.yCoord + y * weight, p1.zCoord + z * weight) : null;
		}
	}
	
	/**
	 * Gets a point along the line between the given points.
	 * 
	 * @param p1 The first point
	 * @param p2 The second point
	 * @param slide The length from the first point to the new point divided by
	 *            the length between the given points. A value of 0.0 would give
	 *            the first point, 1.0 would give the second point, 0.5 would
	 *            give the average of the two, etc.
	 * @return A point along (or extended out from) the line
	 * @see {@link net.eekysam.uhspres.utils.geo.Ray#getPart(float)}
	 */
	public static Point getSlide(Point p1, Point p2, double slide)
	{
		double x = p2.xCoord - p1.xCoord;
		double y = p2.yCoord - p1.yCoord;
		double z = p2.zCoord - p1.zCoord;
		
		return new Point(p1.xCoord + x * slide, p1.yCoord + y * slide, p1.zCoord + z * slide);
	}
	
	/**
	 * Gets a point along the line between the given points.
	 * 
	 * @param p1 The first point
	 * @param p2 The second point
	 * @param length The length from the first point to the new point
	 * @return A point along (or extended out from) the line. null if the points
	 *         are too close.
	 */
	public static Point getSlideWithLength(Point p1, Point p2, double length)
	{
		double x = p2.xCoord - p1.xCoord;
		double y = p2.yCoord - p1.yCoord;
		double z = p2.zCoord - p1.zCoord;
		
		double l = distance(p1, p2);
		
		if (l < 1E-7D)
		{
			return null;
		}
		else
		{
			double weight = length / l;
			return new Point(p1.xCoord + x * weight, p1.yCoord + y * weight, p1.zCoord + z * weight);
		}
	}
	
	/**
	 * Gets a point along the line between the given points.
	 * 
	 * @param p1 The first point
	 * @param p2 The second point
	 * @param length The length from the second point to the new point
	 * @return A point along (or extended out from) the line. null if the points
	 *         are too close.
	 */
	public static Point getSlideAddLength(Point p1, Point p2, double length)
	{
		double x = p2.xCoord - p1.xCoord;
		double y = p2.yCoord - p1.yCoord;
		double z = p2.zCoord - p1.zCoord;
		
		double l = distance(p1, p2);
		
		if (l < 1E-7D)
		{
			return null;
		}
		else
		{
			double weight = (length + l) / l;
			return new Point(p1.xCoord + x * weight, p1.yCoord + y * weight, p1.zCoord + z * weight);
		}
	}
	
	/**
	 * Duplicates this point.
	 * 
	 * @return A new point.
	 */
	public Point duplicate()
	{
		return new Point(this.xCoord, this.yCoord, this.zCoord);
	}
	
	@Override
	public String toString()
	{
		return String.format("(%.2f, %.2f, %.2f)", this.xCoord, this.yCoord, this.zCoord);
	}
	
	public Vector3f getGLVec()
	{
		return new Vector3f((float) this.xCoord, (float) this.yCoord, (float) this.zCoord);
	}
}
