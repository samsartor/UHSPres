package net.eekysam.uhspres.font.bmf;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;

public class BMFBlockPages extends BMFBlock
{
	public String[] pageNames;
	
	public BMFBlockPages(int size, BMFFile file)
	{
		super(size, file);
	}
	
	@Override
	public void read(DataInput in) throws IOException
	{
		ArrayList<String> pages = new ArrayList<String>();
		int size = this.size;
		while (size > 0)
		{
			String s = this.readNullTerminated(in);
			size -= s.length() + 1;
			pages.add(s);
		}
		this.pageNames = new String[pages.size()];
		pages.toArray(this.pageNames);
	}
}