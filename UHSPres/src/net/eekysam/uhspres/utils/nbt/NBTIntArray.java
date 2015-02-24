package net.eekysam.uhspres.utils.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTIntArray extends NBTTag
{
	private int[] payload = new int[0];
	
	public NBTIntArray()
	{
		super();
	}
	
	public NBTIntArray(String name)
	{
		super(name);
	}
	
	@Override
	public byte getId()
	{
		return 11;
	}
	
	@Override
	void writePayload(DataOutput dataoutput) throws IOException
	{
		dataoutput.writeInt(this.payload.length);
		
		for (int i = 0; i < this.payload.length; i++)
		{
			dataoutput.writeInt(this.payload[i]);
		}
	}
	
	@Override
	void readPayload(DataInput datainput, int depth) throws IOException
	{
		int j = datainput.readInt();
		this.payload = new int[j];
		
		for (int i = 0; i < j; i++)
		{
			this.payload[i] = datainput.readInt();
		}
	}
	
	public NBTIntArray setInts(int[] ints)
	{
		this.payload = ints;
		return this;
	}
	
	public int[] getInts()
	{
		return this.payload;
	}
}
