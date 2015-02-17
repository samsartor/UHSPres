package net.eekysam.uhspres.render.fbo;

public enum EnumDrawBufferLocs
{
	DIFFUSE("diffuse", 0),
	NORMAL("normal", 1),
	POSITION("position", 2),
	LIGHT_ACCUMULATION("light", 1);
	
	public final String outputName;
	public final int location;
	
	EnumDrawBufferLocs(String output, int loc)
	{
		this.outputName = "out_" + output;
		this.location = loc;
	}
	
	public static final int numberOfLocations = 3;
}
