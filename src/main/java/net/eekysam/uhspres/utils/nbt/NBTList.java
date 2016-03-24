package net.eekysam.uhspres.utils.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NBTList extends NBTTag
{
	private List<NBTTag> tagList = new ArrayList<NBTTag>();
	private byte tagType;
	
	public NBTList()
	{
		super();
	}
	
	public NBTList(String name)
	{
		super(name);
	}
	
	@Override
	public byte getId()
	{
		return 9;
	}
	
	@Override
	void writePayload(DataOutput dataoutput) throws IOException
	{
		if (!this.tagList.isEmpty())
		{
			this.tagType = this.tagList.get(0).getId();
		}
		else
		{
			this.tagType = 1;
		}
		
		dataoutput.writeByte(this.tagType);
		dataoutput.writeInt(this.tagList.size());
		
		for (int i = 0; i < this.tagList.size(); i++)
		{
			this.tagList.get(i).writePayload(dataoutput);
		}
	}
	
	@Override
	void readPayload(DataInput datainput, int depth) throws IOException
	{
		this.tagType = datainput.readByte();
		int size = datainput.readInt();
		this.tagList = new ArrayList<NBTTag>();
		
		for (int i = 0; i < size; i++)
		{
			NBTTag tag = NBTTag.getNewTag(this.tagType, null);
			tag.readPayload(datainput, depth + 1);
			this.tagList.add(tag);
		}
	}
	
	public void appendTag(NBTTag tag)
	{
		if (this.tagList.isEmpty())
		{
			this.tagType = tag.getId();
		}
		
		if (tag.getId() == this.tagType)
		{
			this.tagList.add(tag);
		}
	}
	
	public NBTTag removeTag(int i)
	{
		return this.tagList.remove(i);
	}
	
	public NBTTag tagAt(int i)
	{
		return this.tagList.get(i);
	}
	
	public int tagCount()
	{
		return this.tagList.size();
	}
	
	public void clear()
	{
		this.tagList.clear();
	}
}
