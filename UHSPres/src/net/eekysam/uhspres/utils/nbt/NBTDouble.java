package net.eekysam.uhspres.utils.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTDouble extends NBTTag
{
	private double payload;
	
	public NBTDouble()
	{
		super();
	}
	
	public NBTDouble(String name)
	{
		super(name);
	}
	
	@Override
	public byte getId()
	{
		return 6;
	}
	
	@Override
	void writePayload(DataOutput dataoutput) throws IOException
	{
		dataoutput.writeDouble(this.payload);
	}
	
	@Override
	void readPayload(DataInput datainput, int depth) throws IOException
	{
		this.payload = datainput.readDouble();
	}
	
	public NBTDouble setDouble(double i)
	{
		this.payload = i;
		return this;
	}
	
	public double getDouble()
	{
		return this.payload;
	}
}
