package net.eekysam.uhspres.utils.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTString extends NBTTag
{
	private String payload;
	
	public NBTString()
	{
		super();
	}
	
	public NBTString(String name)
	{
		super(name);
	}
	
	@Override
	public byte getId()
	{
		return 8;
	}
	
	@Override
	void writePayload(DataOutput dataoutput) throws IOException
	{
		dataoutput.writeUTF(this.payload);
	}
	
	@Override
	void readPayload(DataInput datainput, int depth) throws IOException
	{
		this.payload = datainput.readUTF();
	}
	
	public NBTString setString(String string)
	{
		this.payload = string;
		return this;
	}
	
	public String getString()
	{
		return this.payload;
	}
}
