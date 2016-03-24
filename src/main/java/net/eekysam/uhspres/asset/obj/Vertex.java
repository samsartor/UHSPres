package net.eekysam.uhspres.asset.obj;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Vertex
{
	public Vector3f v;
	public Vector3f vn;
	public Vector2f vt;
	
	public Vertex(Vector3f v, Vector2f vt, Vector3f vn)
	{
		this.v = v;
		this.vn = vn;
		this.vt = vt;
	}
	
	@Override
	public int hashCode()
	{
		int vh = 1;
		int vnh = 2;
		int vth = 3;
		if (this.v != null)
		{
			vh += smear(Float.hashCode(this.v.x)) ^ smear(Float.hashCode(this.v.y)) ^ smear(Float.hashCode(this.v.z));
		}
		if (this.vn != null)
		{
			vnh += smear(Float.hashCode(this.vn.x)) ^ smear(Float.hashCode(this.vn.y)) ^ smear(Float.hashCode(this.vn.z));
		}
		if (this.vt != null)
		{
			vth += smear(Float.hashCode(this.vt.x)) ^ smear(Float.hashCode(this.vt.y));
		}
		return smear(vh) ^ smear(vnh) ^ smear(vth);
	}
	
	private static int smear(int hashCode)
	{
		hashCode ^= (hashCode >>> 20) ^ (hashCode >>> 12);
		return hashCode ^ (hashCode >>> 7) ^ (hashCode >>> 4);
	}
	
	@Override
	public String toString()
	{
		return String.format("V = %s, VN = %s, VT = %s", this.v, this.vn, this.vt);
	}
	
	@Override
	public boolean equals(Object a)
	{
		if (a instanceof Vertex)
		{
			Vertex v = (Vertex) a;
			return (v.v == null || this.v == null || v.v.equals(this.v)) && (v.vn == null || this.vn == null || v.vn.equals(this.vn)) && (v.vt == null || this.vt == null || v.vt.equals(this.vt));
		}
		return false;
	}
}
