package net.eekysam.uhspres.font.bmf;

import java.io.DataInput;
import java.io.IOException;

abstract class BMFBlock
{
	protected final int size;
	protected final BMFFile file;
	
	public BMFBlock(int size, BMFFile file)
	{
		this.size = size;
		this.file = file;
	}
	
	public abstract void read(DataInput in) throws IOException;
	
	static BMFBlock createBlock(byte type, int size, BMFFile file)
	{
		switch (type)
		{
			case 1:
				return new BMFBlockInfo(size, file);
			case 2:
				return new BMFBlockCommon(size, file);
			case 3:
				return new BMFBlockPages(size, file);
			case 4:
				return new BMFBlockChars(size, file);
			case 5:
				return new BMFBlockKerning(size, file);
			default:
				return null;
		}
	}
	
	protected String readNullTerminated(DataInput in) throws IOException
	{
		String text = "";
		byte c;
		while (true)
		{
			c = in.readByte();
			if (c == 0)
			{
				break;
			}
			text += (char) c;
		}
		return text;
	}
}