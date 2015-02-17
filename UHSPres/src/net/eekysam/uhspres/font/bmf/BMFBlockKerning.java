package net.eekysam.uhspres.font.bmf;

import java.io.DataInput;
import java.io.IOException;

public class BMFBlockKerning extends BMFBlock
{
	public static class BMFKerningPair
	{
		public final int first;
		public final int second;
		public final short amount;
		
		public BMFKerningPair(DataInput in) throws IOException
		{
			this.first = in.readInt();
			this.second = in.readInt();
			this.amount = in.readShort();
		}
	}
	
	public BMFKerningPair[] pairs;
	
	public BMFBlockKerning(int size, BMFFile file)
	{
		super(size, file);
	}
	
	@Override
	public void read(DataInput in) throws IOException
	{
		this.pairs = new BMFKerningPair[this.size / 10];
		for (int i = 0; i < this.pairs.length; i++)
		{
			this.pairs[i] = new BMFKerningPair(in);
		}
	}
}