package net.eekysam.uhspres.utils.geo;

/**
 * An axis-aligned box
 */
public class Box
{
	public double minX;
	public double minY;
	public double minZ;
	public double maxX;
	public double maxY;
	public double maxZ;
	
	/**
	 * Creates a new box
	 */
	public static Box getBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
	{
		return new Box(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	private Box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
	{
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		this.fixVerts();
	}
	
	/**
	 * Changes the bounds of a box
	 * 
	 * @return This box resized
	 */
	public Box setBoundsThis(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
	{
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		this.fixVerts();
		return this;
	}
	
	/**
	 * Creates a new box with different bounds
	 * 
	 * @return A new box
	 */
	public Box setBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
	{
		return this.duplicate().setBoundsThis(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	/**
	 * Duplicates this box
	 * 
	 * @return A new box
	 */
	public Box duplicate()
	{
		return new Box(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
	}
	
	/**
	 * Increases the size of the box by a certain amount in each direction
	 * 
	 * @return This box resized
	 */
	public Box expand(float size)
	{
		this.maxX += size;
		this.maxY += size;
		this.maxZ += size;
		this.minX -= size;
		this.minY -= size;
		this.minZ -= size;
		this.fixVerts();
		return this;
	}
	
	/**
	 * Increases the size of the box by the absolute value of the amounts in the
	 * respective directions. Positive values move the upper faces. Negative
	 * values move the lower faces.
	 * 
	 * @return This box resized
	 */
	public Box grow(float x, float y, float z)
	{
		if (x > 0)
		{
			this.maxX += x;
		}
		else
		{
			this.minX += x;
		}
		if (y > 0)
		{
			this.maxY += y;
		}
		else
		{
			this.minY += y;
		}
		if (z > 0)
		{
			this.maxZ += z;
		}
		else
		{
			this.minZ += z;
		}
		return this;
	}
	
	/**
	 * Increases the size of the box so that the point is inside the box
	 * 
	 * @param p The point to include
	 * @return This box resized
	 */
	public Box eat(Point p)
	{
		double x = p.xCoord;
		if (x < this.minX)
		{
			this.minX = x;
		}
		if (x > this.maxX)
		{
			this.maxX = x;
		}
		double y = p.yCoord;
		if (y < this.minY)
		{
			this.minY = y;
		}
		if (y > this.maxY)
		{
			this.maxY = y;
		}
		double z = p.zCoord;
		if (z < this.minZ)
		{
			this.minZ = z;
		}
		if (z > this.maxZ)
		{
			this.maxZ = z;
		}
		return this;
	}
	
	/**
	 * Checks to see if a point is within this box.
	 * 
	 * @param point The point to check
	 */
	public boolean isPointInside(Point point)
	{
		return point.xCoord > this.minX && point.xCoord < this.maxX ? (point.yCoord > this.minY && point.yCoord < this.maxY ? point.zCoord > this.minZ && point.zCoord < this.maxZ : false) : false;
	}
	
	/**
	 * Checks to see if a point is within this box, but ignores the X
	 * coordinate.
	 * 
	 * @param point The point to check
	 */
	public boolean isPointInYZ(Point point)
	{
		return point == null ? false : point.yCoord >= this.minY && point.yCoord <= this.maxY && point.zCoord >= this.minZ && point.zCoord <= this.maxZ;
	}
	
	/**
	 * Checks to see if a point is within this box, but ignores the Y
	 * coordinate.
	 * 
	 * @param point The point to check
	 */
	public boolean isPointInXZ(Point point)
	{
		return point == null ? false : point.xCoord >= this.minX && point.xCoord <= this.maxX && point.zCoord >= this.minZ && point.zCoord <= this.maxZ;
	}
	
	/**
	 * Checks to see if a point is within this box, but ignores the Z
	 * coordinate.
	 * 
	 * @param point The point to check
	 */
	public boolean isPointInXY(Point point)
	{
		return point == null ? false : point.xCoord >= this.minX && point.xCoord <= this.maxX && point.yCoord >= this.minY && point.yCoord <= this.maxY;
	}
	
	/**
	 * Tests if a given box intersect this box.
	 * 
	 * @param box The box to compare
	 */
	public boolean intersectsWith(Box box)
	{
		return box.maxX > this.minX && box.minX < this.maxX ? (box.maxY > this.minY && box.minY < this.maxY ? box.maxZ > this.minZ && box.minZ < this.maxZ : false) : false;
	}
	
	/**
	 * Tests if two boxes intersect each other.
	 */
	public static boolean intersects(Box box1, Box box2)
	{
		return box1.intersectsWith(box2);
	}
	
	/**
	 * Translate this box by a given offset.
	 * 
	 * @return This box translated
	 */
	public Box moveThis(double x, double y, double z)
	{
		this.maxX += x;
		this.maxY += y;
		this.maxZ += z;
		this.minX += x;
		this.minY += y;
		this.minZ += z;
		return this;
	}
	
	/**
	 * Creates a new box translated by a given offset.
	 * 
	 * @return A new box
	 */
	public Box moveNew(double x, double y, double z)
	{
		return this.duplicate().moveThis(x, y, z);
	}
	
	/**
	 * Rotates this box on a vertical line. (Yaw)
	 * 
	 * @param drot The amount to rotate the box in increments of 90°.
	 * @param x The x coordinate of the center of rotation.
	 * @param y The y coordinate of the center of rotation. Has no effect.
	 * @param z The z coordinate of the center of rotation.
	 * @return This box rotated
	 */
	public Box yawThis(int drot, double x, double y, double z)
	{
		short[] trg = new short[] { 0, 1, 0, -1 };
		int sin = (drot % 4) + 4;
		int cos = sin + 1;
		sin %= 4;
		cos %= 4;
		this.moveThis(-x, -y, -z);
		double mx;
		double my;
		double mz;
		mx = this.minX * trg[cos] - this.minZ * trg[sin];
		my = this.minY;
		mz = this.minX * trg[sin] + this.minZ * trg[cos];
		this.minX = mx;
		this.minY = my;
		this.minZ = mz;
		mx = this.maxX * trg[cos] - this.maxZ * trg[sin];
		my = this.maxY;
		mz = this.maxX * trg[sin] + this.maxZ * trg[cos];
		this.maxX = mx;
		this.maxY = my;
		this.maxZ = mz;
		this.moveThis(x, y, z);
		this.fixVerts();
		return this;
	}
	
	/**
	 * Ensures that the lower bounds are less than the upper bounds. If not, the
	 * two variables are swapped.
	 * 
	 * @return This box corrected
	 */
	public Box fixVerts()
	{
		if (this.maxX < this.minX)
		{
			this.flipX();
		}
		if (this.maxY < this.minY)
		{
			this.flipY();
		}
		if (this.maxZ < this.minZ)
		{
			this.flipZ();
		}
		return this;
	}
	
	/**
	 * Swaps the lower x bound and upper x bound. Should only be performed if
	 * maxX < minX.
	 */
	protected void flipX()
	{
		double max = this.maxX;
		double min = this.minX;
		this.maxX = min;
		this.minX = max;
	}
	
	/**
	 * Swaps the lower y bound and upper y bound. Should only be performed if
	 * maxY < minY.
	 */
	protected void flipY()
	{
		double max = this.maxY;
		double min = this.minY;
		this.maxY = min;
		this.minY = max;
	}
	
	/**
	 * Swaps the lower z bound and upper z bound. Should only be performed if
	 * maxZ < minZ.
	 */
	protected void flipZ()
	{
		double max = this.maxZ;
		double min = this.minZ;
		this.maxZ = min;
		this.minZ = max;
	}
	
	/**
	 * Calculates the first point along a line that intersects the surface of
	 * this box.
	 * 
	 * @param ray The ray/line. Has a starting point, end point, and direction.
	 * @return The first intersection of the line and the surface of this box.
	 *         Returns null if there is no intersection.
	 */
	public BoxPin calculateIntercept(Ray ray)
	{
		return this.calculateIntercept(ray.start, ray.end);
	}
	
	/**
	 * Calculates the first point on a line that intersects the surface of this
	 * box.
	 * 
	 * @param point1 The starting point of the line
	 * @param point2 The end point of the line
	 * @return The first intersection of the line and the surface of this box.
	 *         Returns null if there is no intersection.
	 */
	public BoxPin calculateIntercept(Point point1, Point point2)
	{
		Point xdown = Point.getIntermediateWithXValue(point1, point2, this.minX);
		Point xup = Point.getIntermediateWithXValue(point1, point2, this.maxX);
		Point ydown = Point.getIntermediateWithYValue(point1, point2, this.minY);
		Point yup = Point.getIntermediateWithYValue(point1, point2, this.maxY);
		Point zdown = Point.getIntermediateWithZValue(point1, point2, this.minZ);
		Point zup = Point.getIntermediateWithZValue(point1, point2, this.maxZ);
		
		if (!this.isPointInYZ(xdown))
		{
			xdown = null;
		}
		
		if (!this.isPointInYZ(xup))
		{
			xup = null;
		}
		
		if (!this.isPointInXZ(ydown))
		{
			ydown = null;
		}
		
		if (!this.isPointInXZ(yup))
		{
			yup = null;
		}
		
		if (!this.isPointInXY(zdown))
		{
			zdown = null;
		}
		
		if (!this.isPointInXY(zup))
		{
			zup = null;
		}
		
		Point pin = null;
		
		if (xdown != null && (pin == null || point1.squareDistanceTo(xdown) < point1.squareDistanceTo(pin)))
		{
			pin = xdown;
		}
		
		if (xup != null && (pin == null || point1.squareDistanceTo(xup) < point1.squareDistanceTo(pin)))
		{
			pin = xup;
		}
		
		if (ydown != null && (pin == null || point1.squareDistanceTo(ydown) < point1.squareDistanceTo(pin)))
		{
			pin = ydown;
		}
		
		if (yup != null && (pin == null || point1.squareDistanceTo(yup) < point1.squareDistanceTo(pin)))
		{
			pin = yup;
		}
		
		if (zdown != null && (pin == null || point1.squareDistanceTo(zdown) < point1.squareDistanceTo(pin)))
		{
			pin = zdown;
		}
		
		if (zup != null && (pin == null || point1.squareDistanceTo(zup) < point1.squareDistanceTo(pin)))
		{
			pin = zup;
		}
		
		if (pin == null)
		{
			return null;
		}
		else
		{
			EnumDir dir = null;
			
			if (pin == xdown)
			{
				dir = EnumDir.XDown;
			}
			
			if (pin == xup)
			{
				dir = EnumDir.XUp;
			}
			
			if (pin == ydown)
			{
				dir = EnumDir.YDown;
			}
			
			if (pin == yup)
			{
				dir = EnumDir.YUp;
			}
			
			if (pin == zdown)
			{
				dir = EnumDir.ZDown;
			}
			
			if (pin == zup)
			{
				dir = EnumDir.ZUp;
			}
			
			return new BoxPin(this, dir, pin);
		}
	}
	
	/**
	 * @return The side length of the box along the X axis.
	 */
	public double getXLength()
	{
		return this.maxX - this.minX;
	}
	
	/**
	 * @return The side length of the box along the Y axis.
	 */
	public double getYLength()
	{
		return this.maxY - this.minY;
	}
	
	/**
	 * @return The side length of the box along the Z axis.
	 */
	public double getZLength()
	{
		return this.maxZ - this.minZ;
	}
}
