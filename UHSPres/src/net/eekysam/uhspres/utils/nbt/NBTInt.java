package net.eekysam.uhspres.utils.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTInt extends NBTTag
{
	private int payload;

	public NBTInt()
	{
		super();
	}

	public NBTInt(String name)
	{
		super(name);
	}

	@Override
	public byte getId()
	{
		return 3;
	}

	@Override
	void writePayload(DataOutput dataoutput) throws IOException
	{
		dataoutput.writeInt(this.payload);
	}

	@Override
	void readPayload(DataInput datainput, int depth) throws IOException
	{
		this.payload = datainput.readInt();
	}

	public NBTInt setInt(int i)
	{
		this.payload = i;
		return this;
	}

	public int getInt()
	{
		return this.payload;
	}
}
