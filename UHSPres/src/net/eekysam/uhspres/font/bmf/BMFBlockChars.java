package net.eekysam.uhspres.font.bmf;

import java.io.DataInput;
import java.io.IOException;

public class BMFBlockChars extends BMFBlock
{
	public BMFChar[] chars;
	
	public static class BMFChar
	{
		public final int id;
		public final short x;
		public final short y;
		public final short width;
		public final short height;
		public final short xoffset;
		public final short yoffset;
		public final short xadvance;
		public final byte page;
		public final byte chnl;
		
		public BMFChar(DataInput in) throws IOException
		{
			this.id = in.readInt();
			this.x = in.readShort();
			this.y = in.readShort();
			this.width = in.readShort();
			this.height = in.readShort();
			this.xoffset = in.readShort();
			this.yoffset = in.readShort();
			this.xadvance = in.readShort();
			this.page = in.readByte();
			this.chnl = in.readByte();
		}
	}
	
	public BMFBlockChars(int size, BMFFile file)
	{
		super(size, file);
	}
	
	@Override
	public void read(DataInput in) throws IOException
	{
		this.chars = new BMFChar[this.size / 20];
		for (int i = 0; i < this.chars.length; i++)
		{
			this.chars[i] = new BMFChar(in);
		}
	}
}