package net.eekysam.uhspres.utils.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;

public class NBTCompound extends NBTTag
{
	private HashMap<String, NBTTag> payload = new HashMap<String, NBTTag>();

	public NBTCompound()
	{
		super();
	}

	public NBTCompound(String name)
	{
		super(name);
	}

	@Override
	public byte getId()
	{
		return 10;
	}

	public boolean isEmpty()
	{
		return this.payload.isEmpty();
	}

	public boolean hasTag(String name)
	{
		return this.payload.containsKey(name);
	}

	public void writeAsRoot(DataOutput dataoutput) throws IOException
	{
		for (NBTTag tag : this.payload.values())
		{
			tag.write(dataoutput);
		}
	}

	public static NBTCompound readAsRoot(DataInput datainput) throws IOException
	{
		NBTCompound compound = new NBTCompound();
		while (true)
		{
			NBTTag tag;
			try
			{
				tag = NBTTag.read(datainput, 0);
			}
			catch (EOFException e)
			{
				return compound;
			}
			compound.payload.put(tag.getName(), tag);
			if (tag instanceof NBTEnd)
			{
				return compound;
			}
		}
	}

	@Override
	void writePayload(DataOutput dataoutput) throws IOException
	{
		for (NBTTag tag : this.payload.values())
		{
			tag.write(dataoutput);
		}
		dataoutput.write((byte) 0);
	}

	@Override
	void readPayload(DataInput datainput, int depth) throws IOException
	{
		NBTTag tag;
		while ((tag = NBTTag.read(datainput, depth + 1)).getId() != 0)
		{
			this.payload.put(tag.getName(), tag);
		}
	}

	public void setTag(String name, NBTTag tag)
	{
		tag.setName(name);
		this.payload.put(name, tag);
	}

	public void setTag(NBTTag tag)
	{
		this.payload.put(tag.getName(), tag);
	}

	public NBTTag getTag(String name)
	{
		return this.payload.get(name);
	}

	public void setString(String name, String tag)
	{
		NBTString string = new NBTString(name);
		string.setString(tag);
		this.setTag(string);
	}

	public String getString(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTString)
		{
			return ((NBTString) tag).getString();
		}
		return "";
	}

	public void setByte(String name, byte tag)
	{
		NBTByte b = new NBTByte(name);
		b.setByte(tag);
		this.setTag(b);
	}

	public byte getByte(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTByte)
		{
			return ((NBTByte) tag).getByte();
		}
		return 0;
	}

	public void setBool(String name, boolean tag)
	{
		NBTByte b = new NBTByte(name);
		b.setByte((byte) (tag ? 1 : 0));
		this.setTag(b);
	}

	public boolean getBool(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTByte)
		{
			return ((NBTByte) tag).getByte() == 1;
		}
		return false;
	}

	public void setShort(String name, short tag)
	{
		NBTShort nbt = new NBTShort(name);
		nbt.setShort(tag);
		this.setTag(nbt);
	}

	public short getShort(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTShort)
		{
			return ((NBTShort) tag).getShort();
		}
		return 0;
	}

	public void setInt(String name, int tag)
	{
		NBTInt nbt = new NBTInt(name);
		nbt.setInt(tag);
		this.setTag(nbt);
	}

	public int getInt(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTInt)
		{
			return ((NBTInt) tag).getInt();
		}
		return 0;
	}

	public void setLong(String name, long tag)
	{
		NBTLong nbt = new NBTLong(name);
		nbt.setLong(tag);
		this.setTag(nbt);
	}

	public long getLong(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTLong)
		{
			return ((NBTLong) tag).getLong();
		}
		return 0;
	}

	public void setFloat(String name, float tag)
	{
		NBTFloat nbt = new NBTFloat(name);
		nbt.setFloat(tag);
		this.setTag(nbt);
	}

	public float getFloat(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTFloat)
		{
			return ((NBTFloat) tag).getFloat();
		}
		return Float.NaN;
	}

	public void setDouble(String name, double tag)
	{
		NBTDouble nbt = new NBTDouble(name);
		nbt.setDouble(tag);
		this.setTag(nbt);
	}

	public double getDouble(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTDouble)
		{
			return ((NBTDouble) tag).getDouble();
		}
		return Double.NaN;
	}

	public void setByteArray(String name, byte[] tag)
	{
		NBTByteArray nbt = new NBTByteArray(name);
		nbt.setBytes(tag);
		this.setTag(nbt);
	}

	public byte[] getByteArray(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTByteArray)
		{
			return ((NBTByteArray) tag).getBytes();
		}
		return new byte[0];
	}

	public void setIntArray(String name, int[] tag)
	{
		NBTIntArray nbt = new NBTIntArray(name);
		nbt.setInts(tag);
		this.setTag(nbt);
	}

	public int[] getIntArray(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTIntArray)
		{
			return ((NBTIntArray) tag).getInts();
		}
		return new int[0];
	}

	public void setCompound(String name, NBTCompound tag)
	{
		tag.setName(name);
		this.setTag(tag);
	}

	public NBTCompound getCompound(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTCompound)
		{
			return (NBTCompound) tag;
		}
		return null;
	}

	public void setList(String name, NBTList tag)
	{
		tag.setName(name);
		this.setTag(tag);
	}

	public NBTList getList(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTList)
		{
			return (NBTList) tag;
		}
		return null;
	}
}
