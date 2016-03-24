package net.eekysam.uhspres.font.bmf;

import java.io.DataInput;
import java.io.IOException;

public class BMFBlockInfo extends BMFBlock
{
	public short fontSize;
	public byte bitField;
	public byte charSet;
	public short stretchH;
	public byte aa;
	public byte paddingUp;
	public byte paddingRight;
	public byte paddingDown;
	public byte paddingLeft;
	public byte spacingHoriz;
	public byte spacingVert;
	public byte outline;
	public String fontName;
	
	public BMFBlockInfo(int size, BMFFile file)
	{
		super(size, file);
	}
	
	@Override
	public void read(DataInput in) throws IOException
	{
		this.fontSize = in.readShort();
		this.bitField = in.readByte();
		this.charSet = in.readByte();
		this.stretchH = in.readShort();
		this.aa = in.readByte();
		this.paddingUp = in.readByte();
		this.paddingRight = in.readByte();
		this.paddingDown = in.readByte();
		this.paddingLeft = in.readByte();
		this.spacingHoriz = in.readByte();
		this.spacingVert = in.readByte();
		this.outline = in.readByte();
		this.fontName = this.readNullTerminated(in);
	}
}