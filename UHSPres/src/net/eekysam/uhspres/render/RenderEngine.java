package net.eekysam.uhspres.render;

import net.eekysam.uhspres.render.verts.VertexBuffer;
import net.eekysam.uhspres.utils.graphics.GLUtils;

public class RenderEngine
{
	public static final int[] quadIndicies = new int[] { 0, 1, 3, 1, 2, 3 };

	public static void buffer2DQuad(VertexBuffer positionBuf, VertexBuffer indexBuf, float x, float y, float z, float w, float h)
	{
		float[] verts = new float[12];

		for (int i = 0; i < 12; i += 3)
		{
			verts[i] = x;
			verts[i + 1] = y;
			verts[i + 2] = z;
		}
		verts[1] += h;
		verts[4] += h;
		verts[3] += w;
		verts[6] += w;

		positionBuf.vertexData(GLUtils.bufferFloats(verts));
		indexBuf.vertexData(GLUtils.bufferInts(RenderEngine.quadIndicies));
	}
}
