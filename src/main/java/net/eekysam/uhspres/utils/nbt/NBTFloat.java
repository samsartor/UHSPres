package net.eekysam.uhspres.utils.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTFloat extends NBTTag
{
	private float payload;
	
	public NBTFloat()
	{
		super();
	}
	
	public NBTFloat(String name)
	{
		super(name);
	}
	
	@Override
	public byte getId()
	{
		return 5;
	}
	
	@Override
	void writePayload(DataOutput dataoutput) throws IOException
	{
		dataoutput.writeFloat(this.payload);
	}
	
	@Override
	void readPayload(DataInput datainput, int depth) throws IOException
	{
		this.payload = datainput.readFloat();
	}
	
	public NBTFloat setFloat(float i)
	{
		this.payload = i;
		return this;
	}
	
	public float getFloat()
	{
		return this.payload;
	}
}
