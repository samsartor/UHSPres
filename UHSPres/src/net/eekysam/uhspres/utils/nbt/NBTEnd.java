package net.eekysam.uhspres.utils.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTEnd extends NBTTag
{
	public NBTEnd()
	{
		super(null);
	}
	
	@Override
	public byte getId()
	{
		return 0;
	}
	
	@Override
	void writePayload(DataOutput dataoutput) throws IOException
	{
	}
	
	@Override
	void readPayload(DataInput datainput, int depth) throws IOException
	{
	}
}
