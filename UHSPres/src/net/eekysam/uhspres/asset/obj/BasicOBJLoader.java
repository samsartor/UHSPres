package net.eekysam.uhspres.asset.obj;

import java.util.List;
import java.util.Scanner;

import net.eekysam.uhspres.asset.GameAsset;

public class BasicOBJLoader
{
	private GameAsset asset;
	private List<Integer> inds;
	private List<Float> verts;
	private int polySize;
	private int vertNum = -1;
	private int faceNum = -1;
	
	public BasicOBJLoader(GameAsset asset, List<Integer> inds, List<Float> verts, int polySize)
	{
		this.asset = asset;
		this.inds = inds;
		this.verts = verts;
		this.polySize = polySize;
	}
	
	public void load()
	{
		this.vertNum = 0;
		this.faceNum = 0;
		Scanner read = new Scanner(this.asset.getInput());
		while (read.hasNextLine())
		{
			String[] line = read.nextLine().trim().split(" ");
			if (line.length > 0)
			{
				if (line[0].equalsIgnoreCase("v"))
				{
					this.vertNum++;
					for (int i = 1; i < 4; i++)
					{
						this.verts.add(Float.parseFloat(line[i]));
					}
				}
				else if (line[0].equalsIgnoreCase("f"))
				{
					if (line.length - 1 == this.polySize)
					{
						this.faceNum++;
						for (int i = 1; i < this.polySize + 1; i++)
						{
							int ind = Integer.parseInt(line[i]);
							if (ind < 0)
							{
								ind = this.vertNum + ind + 1;
							}
							else
							{
								ind = ind - 1;
							}
							this.inds.add(ind);
						}
					}
				}
			}
		}
		read.close();
	}
	
	public int getFaceNum()
	{
		return this.faceNum;
	}
	
	public int getVertNum()
	{
		return this.vertNum;
	}
}
