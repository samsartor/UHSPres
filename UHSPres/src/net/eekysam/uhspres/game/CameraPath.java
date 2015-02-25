package net.eekysam.uhspres.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import net.eekysam.uhspres.asset.GameAsset;
import net.eekysam.uhspres.game.PathPoint.EnumPointType;

public class CameraPath
{
	private ArrayList<PathPoint> path;
	private int point = 0;
	private float time = 0.0F;
	private boolean stopped = false;
	private PathPoint current;
	
	public CameraPath(GameAsset pathFile)
	{
		this.path = new ArrayList<PathPoint>();
		Scanner read = new Scanner(pathFile.getInput());
		while (read.hasNextLine())
		{
			String text = read.nextLine().trim();
			text = text.replaceAll("[ ]{2,}", " ");
			String[] line = text.split(" ");
			if (line.length == 7)
			{
				EnumPointType type = line[0].equals("@") ? EnumPointType.STOP : EnumPointType.KEY;
				float x = Float.parseFloat(line[1]);
				float y = Float.parseFloat(line[2]);
				float z = Float.parseFloat(line[3]);
				float yaw = Float.parseFloat(line[4]);
				float pitch = Float.parseFloat(line[5]);
				String[] timeText = line[6].split(":");
				float time = Integer.parseInt(timeText[0]) * 60.0F;
				time += Float.parseFloat(timeText[1]);
				this.path.add(new PathPoint(type, x, y, z, yaw, pitch, time));
			}
		}
		read.close();
		Collections.sort(this.path, PathPoint.comparator);
		this.update(0.0F);
		this.stopped = true;
	}
	
	private PathPoint getPoint(int point)
	{
		if (point < 0 || point >= this.path.size())
		{
			return null;
		}
		return this.path.get(point);
	}
	
	public float getTime()
	{
		PathPoint current = this.getPoint(this.point);
		if (current == null)
		{
			return this.time;
		}
		return current.time + this.time;
	}
	
	public void next()
	{
		if (!this.stopped)
		{
			if (this.point < this.path.size() - 1)
			{
				this.point++;
			}
		}
		this.time = 0.0F;
		this.stopped = false;
	}
	
	public void prev()
	{
		if (this.point > 0)
		{
			this.point--;
			this.stopped = false;
		}
		else
		{
			this.stopped = true;
		}
		this.time = 0.0F;
	}
	
	public PathPoint getCurrentPoint()
	{
		return this.current;
	}
	
	public void update(float dtime)
	{
		if (this.stopped)
		{
			return;
		}
		PathPoint a = this.getPoint(this.point);
		PathPoint b = this.getPoint(this.point + 1);
		if (b == null)
		{
			this.current = a;
			return;
		}
		this.time += dtime;
		if (this.getTime() > b.time)
		{
			this.time = 0.0F;
			this.point++;
			if (b.type == EnumPointType.STOP)
			{
				this.stopped = true;
			}
		}
		a = this.getPoint(this.point);
		b = this.getPoint(this.point + 1);
		this.current = PathPoint.interpolate(a, b, this.getTime());
	}
}
