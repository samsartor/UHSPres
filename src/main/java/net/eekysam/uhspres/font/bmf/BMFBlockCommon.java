package net.eekysam.uhspres.font.bmf;

import java.io.DataInput;
import java.io.IOException;

public class BMFBlockCommon extends BMFBlock
{
	public short lineHeight;
	public short base;
	public short scaleW;
	public short scaleH;
	public short pages;
	public byte bitField;
	public byte alphaChnl;
	public byte redChnl;
	public byte greenChnl;
	public byte blueChnl;
	
	public BMFBlockCommon(int size, BMFFile file)
	{
		super(size, file);
	}
	
	@Override
	public void read(DataInput in) throws IOException
	{
		this.lineHeight = in.readShort();
		this.base = in.readShort();
		this.scaleW = in.readShort();
		this.scaleH = in.readShort();
		this.pages = in.readShort();
		this.bitField = in.readByte();
		this.alphaChnl = in.readByte();
		this.redChnl = in.readByte();
		this.greenChnl = in.readByte();
		this.blueChnl = in.readByte();
	}
}