package net.eekysam.uhspres.game;

import java.util.Comparator;

public class PathPoint
{
	public static class PointOrder implements Comparator<PathPoint>
	{
		@Override
		public int compare(PathPoint p1, PathPoint p2)
		{
			return Float.compare(p1.time, p2.time);
		}
	}
	
	public static enum EnumPointType
	{
		KEY,
		STOP,
		INTERPOLATED;
	}
	
	public static final PointOrder comparator = new PointOrder();
	
	public final EnumPointType type;
	public final float x;
	public final float y;
	public final float z;
	public final float yaw;
	public final float pitch;
	public final float time;
	
	public PathPoint(EnumPointType type, float x, float y, float z, float yaw, float pitch, float time)
	{
		final float pi2 = (float) Math.PI * 2;
		
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw % pi2;
		this.pitch = pitch % pi2;
		this.time = time;
	}
	
	public PathPoint(PathPoint point, EnumPointType type, float time)
	{
		this(type, point.x, point.y, point.z, point.yaw, point.pitch, time);
	}
	
	public static PathPoint interpolate(PathPoint a, PathPoint b, float time)
	{
		final float pi = (float) Math.PI;
		final float pi2 = pi * 2;
		final float pi4 = pi2 * 2;
		
		if (a == null && b == null)
		{
			return null;
		}
		if (a == null)
		{
			return new PathPoint(b, EnumPointType.INTERPOLATED, time);
		}
		if (b == null)
		{
			return new PathPoint(a, EnumPointType.INTERPOLATED, time);
		}
		float dif = b.time - a.time;
		float mixb = (time - a.time) / dif;
		float mixa = 1 - mixb;
		float x = a.x * mixa + b.x * mixb;
		float y = a.y * mixa + b.y * mixb;
		float z = a.z * mixa + b.z * mixb;
		float yaw = a.yaw * mixa + b.yaw * mixb;
		float pitch = a.pitch * mixa + b.pitch * mixb;
		return new PathPoint(EnumPointType.INTERPOLATED, x, y, z, yaw, pitch, time);
	}
}
