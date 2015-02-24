package net.eekysam.uhspres.utils.geo;

public class BoxPin
{
	public EnumDir side;
	public Point location;
	public Box theBox;
	
	public BoxPin(Box box, EnumDir side, Point location)
	{
		this.theBox = box;
		this.side = side;
		this.location = location;
	}
}
