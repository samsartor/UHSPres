package net.eekysam.uhspres.asset.obj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import net.eekysam.uhspres.asset.GameAsset;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class OBJLoader
{
	private ArrayList<Vector3f> vs;
	private ArrayList<Vector3f> vns;
	private ArrayList<Vector2f> vts;
	private ArrayList<int[][]> fs;
	
	private HashMap<Vertex, Integer> vertsmap;
	
	private int[][] faces;
	private Vertex[] verts;
	
	public void read(GameAsset asset)
	{
		this.vs = new ArrayList<>();
		this.vns = new ArrayList<>();
		this.vts = new ArrayList<>();
		this.fs = new ArrayList<>();
		Scanner read = new Scanner(asset.getInput());
		while (read.hasNextLine())
		{
			String linetext = read.nextLine().trim();
			linetext = linetext.replaceAll("[ ]{2,}", " ");
			String[] line = linetext.split(" ");
			if (line.length > 0)
			{
				if (line[0].equalsIgnoreCase("v"))
				{
					this.vs.add(new Vector3f(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3])));
				}
				else if (line[0].equalsIgnoreCase("vn"))
				{
					this.vns.add(new Vector3f(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3])));
				}
				else if (line[0].equalsIgnoreCase("vt"))
				{
					this.vts.add(new Vector2f(Float.parseFloat(line[1]), 1 - Float.parseFloat(line[2])));
				}
				else if (line[0].equalsIgnoreCase("f"))
				{
					int[][] f = new int[line.length - 1][3];
					for (int i = 0; i < line.length - 1; i++)
					{
						String d = line[i + 1];
						String[] inds = Arrays.copyOf(d.split("/"), 3);
						f[i][0] = this.convertInd(this.readInt(inds[0]), this.vs.size());
						f[i][1] = this.convertInd(this.readInt(inds[1]), this.vts.size());
						f[i][2] = this.convertInd(this.readInt(inds[2]), this.vns.size());
					}
					this.fs.add(f);
				}
			}
		}
		read.close();
	}
	
	public void load()
	{
		ArrayList<int[]> faces = new ArrayList<int[]>();
		this.vertsmap = new HashMap<>();
		for (int[][] faceinds : this.fs)
		{
			int[] face = new int[faceinds.length];
			for (int i = 0; i < faceinds.length; i++)
			{
				int[] inds = faceinds[i];
				Vertex vert = this.getVertex(inds);
				if (this.vertsmap.containsKey(vert))
				{
					face[i] = this.vertsmap.get(vert);
				}
				else
				{
					int j = this.vertsmap.size();
					face[i] = j;
					this.vertsmap.put(vert, j);
				}
			}
			faces.add(face);
		}
		this.faces = new int[faces.size()][];
		faces.toArray(this.faces);
		this.verts = new Vertex[this.vertsmap.size()];
		for (Entry<Vertex, Integer> ent : this.vertsmap.entrySet())
		{
			this.verts[ent.getValue()] = ent.getKey();
		}
	}
	
	public Vertex[] getVerts()
	{
		return this.verts;
	}
	
	public int[][] getFaces()
	{
		return this.faces;
	}
	
	private Vertex getVertex(int[] vert)
	{
		Vector3f v = null;
		Vector2f vt = null;
		Vector3f vn = null;
		if (vert[0] >= 0 && vert[0] < this.vs.size())
		{
			v = this.vs.get(vert[0]);
		}
		if (vert[1] >= 0 && vert[1] < this.vts.size())
		{
			vt = this.vts.get(vert[1]);
		}
		if (vert[2] >= 0 && vert[2] < this.vns.size())
		{
			vn = this.vns.get(vert[2]);
		}
		return new Vertex(v, vt, vn);
	}
	
	private int readInt(String s)
	{
		if (s == null || s.isEmpty())
		{
			return 0;
		}
		else
		{
			return Integer.parseInt(s);
		}
	}
	
	private int convertInd(int ind, int num)
	{
		if (ind < 0)
		{
			return num + ind;
		}
		else
		{
			return ind - 1;
		}
	}
}
