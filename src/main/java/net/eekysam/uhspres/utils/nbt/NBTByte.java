package net.eekysam.uhspres.utils.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTByte extends NBTTag
{
	private byte payload;
	
	public NBTByte()
	{
		super();
	}
	
	public NBTByte(String name)
	{
		super(name);
	}
	
	@Override
	public byte getId()
	{
		return 1;
	}
	
	@Override
	void writePayload(DataOutput dataoutput) throws IOException
	{
		dataoutput.writeByte(this.payload);
	}
	
	@Override
	void readPayload(DataInput datainput, int depth) throws IOException
	{
		this.payload = datainput.readByte();
	}
	
	public NBTByte setByte(byte b)
	{
		this.payload = b;
		return this;
	}
	
	public byte getByte()
	{
		return this.payload;
	}
}
