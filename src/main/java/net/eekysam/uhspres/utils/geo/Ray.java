package net.eekysam.uhspres.utils.geo;

/**
 * Ray has a starting point and ending point. More like a line.
 */
public class Ray
{
	public Point start;
	public Point end;
	
	/**
	 * Creates a new line/ray between two points.
	 */
	public static Ray getRay(Point start, Point end)
	{
		return new Ray(start, end);
	}
	
	/**
	 * Creates a new ray using a point and a vector.
	 * 
	 * @param start The starting point
	 * @param vector The vector from the starting point to the end point.
	 */
	public static Ray getRay(Point start, Vector vector)
	{
		Point end = Point.addVector(start, vector);
		return new Ray(start, end);
	}
	
	private Ray(Point p1, Point p2)
	{
		this.start = p1;
		this.end = p2;
	}
	
	/**
	 * Moves the end point so that the ray has a given length but leaves the
	 * starting point stationary and maintains the same vector.
	 * 
	 * @param length The new length
	 * @return This ray scaled
	 */
	public Ray setLength(double length)
	{
		Point e = Point.getSlideWithLength(this.start, this.end, length);
		this.end = e;
		return this;
	}
	
	/**
	 * Increases the length of the ray by a given amount, leaving the starting
	 * point stationary and maintaining the same vector.
	 * 
	 * @param length The amount to increase the length of this ray
	 * @return This ray scaled
	 */
	public Ray addLengthEnd(double length)
	{
		Point e = Point.getSlideAddLength(this.start, this.end, length);
		this.end = e;
		return this;
	}
	
	/**
	 * Increases the length of the ray by a given amount, leaving the end point
	 * stationary and maintaining the same vector.
	 * 
	 * @param length The amount to increase the length of this ray
	 * @return This ray scaled
	 */
	public Ray addLengthStart(double length)
	{
		Point s = Point.getSlideAddLength(this.end, this.start, length);
		this.start = s;
		return this;
	}
	
	/**
	 * Gets the length squared of this ray/line. Use whenever possible.
	 * 
	 * @return length^2
	 */
	public double getLengthSqr()
	{
		return Point.squareDistance(this.start, this.end);
	}
	
	/**
	 * Gets the length of this ray/line.
	 * 
	 * @return length
	 */
	public double getLength()
	{
		return Point.distance(this.start, this.end);
	}
	
	/**
	 * Creates a new box with this ray/line as the diagonal.
	 * 
	 * @return A new box
	 */
	public Box getBox()
	{
		return Box.getBox(this.start.xCoord, this.start.yCoord, this.start.zCoord, this.start.xCoord, this.start.yCoord, this.start.zCoord).eat(this.end);
	}
	
	/**
	 * Duplicates this ray
	 * 
	 * @return A new ray
	 */
	public Ray duplicate()
	{
		return new Ray(this.start.duplicate(), this.end.duplicate());
	}
	
	/**
	 * Scales this ray by the given ratio, leaving the starting point
	 * stationary.
	 * 
	 * @param scale The ratio to scale this ray by
	 * @return This ray scaled.
	 */
	public Ray extendScale(float scale)
	{
		this.end = Point.getSlide(this.start, this.end, scale);
		return this;
	}
	
	/**
	 * Gets a point along this ray/line.
	 * 
	 * @param loc The length from the starting point of this ray to the new
	 *            point along this ray divided by the length of this ray. A
	 *            value of 0.0 would give the starting point, 1.0 would give the
	 *            ending point, 0.5 would give the average of the two, etc.
	 * @return A point along (or extended out from) this ray/line
	 */
	public Point getPart(float loc)
	{
		return Point.getSlide(this.start, this.end, loc);
	}
	
	public Vector getVector()
	{
		return Vector.getVector(this);
	}
}
