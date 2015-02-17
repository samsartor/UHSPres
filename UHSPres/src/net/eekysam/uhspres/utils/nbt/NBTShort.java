package net.eekysam.uhspres.utils.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTShort extends NBTTag
{
	private short payload;

	public NBTShort()
	{
		super();
	}

	public NBTShort(String name)
	{
		super(name);
	}

	@Override
	public byte getId()
	{
		return 2;
	}

	@Override
	void writePayload(DataOutput dataoutput) throws IOException
	{
		dataoutput.writeShort(this.payload);
	}

	@Override
	void readPayload(DataInput datainput, int depth) throws IOException
	{
		this.payload = datainput.readShort();
	}

	public NBTShort setShort(short i)
	{
		this.payload = i;
		return this;
	}

	public short getShort()
	{
		return this.payload;
	}
}
