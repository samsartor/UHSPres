package net.eekysam.uhspres.render;

import org.lwjgl.util.vector.Vector3f;

public class CalculateNormals
{
	private float[] verts;
	private int[] indices;
	private Vector3f[] normals;
	private boolean equal;

	public CalculateNormals(float[] verts, int[] indices, boolean weightEqual)
	{
		this.verts = verts;
		this.indices = indices;
		this.equal = weightEqual;

		this.normals = new Vector3f[this.verts.length / 3];
	}

	public void calculate()
	{
		int v1;
		int v2;
		int v3;
		for (int i = 0; i < this.indices.length; i += 3)
		{
			v1 = this.indices[i];
			v2 = this.indices[i + 1];
			v3 = this.indices[i + 2];
			this.calcVert(v1, v2, v3);
			this.calcVert(v3, v1, v2);
			this.calcVert(v2, v3, v1);
		}

		for (int i = 0; i < this.normals.length; i++)
		{
			if (this.normals[i] == null)
			{
				this.normals[i] = new Vector3f(0.0F, 0.0F, 0.0F);
			}
			else
			{
				this.normals[i].normalise();
			}
		}
	}

	public Vector3f[] getNormals()
	{
		return this.normals;
	}

	public float[] getNormalData()
	{
		float[] data = new float[this.normals.length * 3];
		for (int i = 0; i < this.normals.length; i++)
		{
			data[i * 3 + 0] = this.normals[i].x;
			data[i * 3 + 1] = this.normals[i].y;
			data[i * 3 + 2] = this.normals[i].z;
		}
		return data;
	}

	private void calcVert(int ind1, int ind2, int ind3)
	{
		Vector3f v1 = this.getVertex(ind1);
		Vector3f v2 = this.getVertex(ind2);
		Vector3f v3 = this.getVertex(ind3);
		Vector3f.sub(v2, v1, v2);
		Vector3f.sub(v3, v1, v3);
		Vector3f.cross(v2, v3, v1);
		if (this.equal)
		{
			v1.normalise();
		}
		if (this.normals[ind1] == null)
		{
			this.normals[ind1] = v1;
		}
		else
		{
			Vector3f.add(this.normals[ind1], v1, this.normals[ind1]);
		}
	}

	private Vector3f getVertex(int index)
	{
		index *= 3;
		return new Vector3f(this.verts[index], this.verts[index + 1], this.verts[index + 2]);
	}
}
