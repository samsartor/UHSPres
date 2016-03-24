package net.eekysam.uhspres.utils.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTLong extends NBTTag
{
	private long payload;
	
	public NBTLong()
	{
		super();
	}
	
	public NBTLong(String name)
	{
		super(name);
	}
	
	@Override
	public byte getId()
	{
		return 4;
	}
	
	@Override
	void writePayload(DataOutput dataoutput) throws IOException
	{
		dataoutput.writeLong(this.payload);
	}
	
	@Override
	void readPayload(DataInput datainput, int depth) throws IOException
	{
		this.payload = datainput.readLong();
	}
	
	public NBTLong setLong(long i)
	{
		this.payload = i;
		return this;
	}
	
	public long getLong()
	{
		return this.payload;
	}
}
