package net.eekysam.uhspres.font.bmf;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.LittleEndianDataInputStream;

public class BMFFile
{
	public static final byte[] magic = new byte[] { 66, 77, 70 };
	public static final byte version = 3;
	
	public BMFBlockInfo info;
	public BMFBlockCommon common;
	public BMFBlockPages pages;
	public BMFBlockChars chars;
	public BMFBlockKerning kerning;
	
	public BMFFile(InputStream in) throws IOException
	{
		this.read(new LittleEndianDataInputStream(in));
	}
	
	private void setBlock(BMFBlock block)
	{
		if (block instanceof BMFBlockInfo)
		{
			this.info = (BMFBlockInfo) block;
		}
		else if (block instanceof BMFBlockCommon)
		{
			this.common = (BMFBlockCommon) block;
		}
		else if (block instanceof BMFBlockPages)
		{
			this.pages = (BMFBlockPages) block;
		}
		else if (block instanceof BMFBlockChars)
		{
			this.chars = (BMFBlockChars) block;
		}
		else if (block instanceof BMFBlockKerning)
		{
			this.kerning = (BMFBlockKerning) block;
		}
	}
	
	private void read(DataInput in) throws IOException
	{
		for (int i = 0; i < magic.length; i++)
		{
			if (in.readByte() != magic[i])
			{
				throw new IOException("Wrong magic number");
			}
		}
		if (in.readByte() != version)
		{
			throw new IOException("Wrong version");
		}
		BMFBlock b;
		while (true)
		{
			byte type;
			try
			{
				type = in.readByte();
			}
			catch (EOFException e)
			{
				break;
			}
			int size = in.readInt();
			b = BMFBlock.createBlock(type, size, this);
			b.read(in);
			this.setBlock(b);
		}
	}
}
