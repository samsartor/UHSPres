package net.eekysam.uhspres.utils.geo;

public enum EnumDir
{
	XUp((byte) 0, (byte) 1, (byte) 0, (byte) 0),
	XDown((byte) 1, (byte) -1, (byte) 0, (byte) 0),
	YUp((byte) 2, (byte) 0, (byte) 1, (byte) 0),
	YDown((byte) 3, (byte) 0, (byte) -1, (byte) 0),
	ZUp((byte) 4, (byte) 0, (byte) 0, (byte) 1),
	ZDown((byte) 5, (byte) 0, (byte) 0, (byte) -1);
	
	public byte ind;
	
	public byte xoff;
	public byte yoff;
	public byte zoff;
	
	EnumDir(byte ind, byte x, byte y, byte z)
	{
		this.ind = ind;
		this.xoff = x;
		this.yoff = y;
		this.zoff = z;
	}
}