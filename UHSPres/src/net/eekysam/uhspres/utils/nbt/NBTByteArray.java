package net.eekysam.uhspres.utils.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTByteArray extends NBTTag
{
	private byte[] payload = new byte[0];

	public NBTByteArray()
	{
		super();
	}

	public NBTByteArray(String name)
	{
		super(name);
	}

	@Override
	public byte getId()
	{
		return 7;
	}

	@Override
	void writePayload(DataOutput dataoutput) throws IOException
	{
		dataoutput.writeInt(this.payload.length);
		dataoutput.write(this.payload);
	}

	@Override
	void readPayload(DataInput datainput, int depth) throws IOException
	{
		int j = datainput.readInt();
		this.payload = new byte[j];
		datainput.readFully(this.payload);
	}

	public NBTByteArray setBytes(byte[] bytes)
	{
		this.payload = bytes;
		return this;
	}

	public byte[] getBytes()
	{
		return this.payload;
	}
}
