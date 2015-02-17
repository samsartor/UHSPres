package net.eekysam.uhspres.utils.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class NBTTag
{
	public static final String[] NBTTypes = new String[] { "END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]" };

	private String name;

	public NBTTag(String name)
	{
		this.name = name;
	}

	public NBTTag()
	{
		this.name = null;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public abstract byte getId();

	abstract void writePayload(DataOutput dataoutput) throws IOException;

	abstract void readPayload(DataInput datainput, int depth) throws IOException;

	public void write(DataOutput dataoutput) throws IOException
	{
		dataoutput.write(this.getId());
		if (this.getId() != 0)
		{
			dataoutput.writeUTF(this.getName());
			this.writePayload(dataoutput);
		}
	}

	public static NBTTag read(DataInput datainput, int depth) throws IOException
	{
		byte id = datainput.readByte();
		if (id == 0)
		{
			return new NBTEnd();
		}
		else
		{
			String name = datainput.readUTF();
			NBTTag tag = getNewTag(id, name);
			if (tag != null)
			{
				tag.readPayload(datainput, depth);
			}
			return tag;
		}
	}

	public String getName()
	{
		if (this.name == null)
		{
			return "";
		}
		else
		{
			return this.name;
		}
	}

	public static NBTTag getNewTag(byte id, String name)
	{
		switch (id)
		{
			case 0:
				return new NBTEnd();
			case 1:
				return new NBTByte(name);
			case 2:
				return new NBTShort(name);
			case 3:
				return new NBTInt(name);
			case 4:
				return new NBTLong(name);
			case 5:
				return new NBTFloat(name);
			case 6:
				return new NBTDouble(name);
			case 7:
				return new NBTByteArray(name);
			case 8:
				return new NBTString(name);
			case 9:
				return new NBTList(name);
			case 10:
				return new NBTCompound(name);
			case 11:
				return new NBTIntArray(name);
			default:
				return null;
		}
	}
}