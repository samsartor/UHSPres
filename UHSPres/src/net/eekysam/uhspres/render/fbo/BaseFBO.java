package net.eekysam.uhspres.render.fbo;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map.Entry;

import net.eekysam.uhspres.render.RenderEngine;
import net.eekysam.uhspres.render.verts.VertexArray;
import net.eekysam.uhspres.render.verts.VertexBuffer;
import net.eekysam.uhspres.utils.graphics.GLUtils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class BaseFBO
{
	private static VertexBuffer quadPos = null;
	private static VertexBuffer quadInd = null;
	private static VertexArray quadVAO = null;

	private static int count = 0;

	public final int width;
	public final int height;
	private FrameBufferObject fbo;
	private boolean created = false;
	private int[] buffers;

	public BaseFBO(int width, int height, int size)
	{
		this.width = width;
		this.height = height;
		this.fbo = new FrameBufferObject(size);
		this.buffers = new int[EnumDrawBufferLocs.numberOfLocations];
	}

	public int getFBO()
	{
		return this.fbo.getBuffer();
	}

	public int getTexture(int attachment)
	{
		return this.fbo.getTexture(attachment);
	}

	public void create()
	{
		if (this.created)
		{
			return;
		}

		BaseFBO.count++;

		if (BaseFBO.quadVAO == null)
		{
			BaseFBO.quadVAO = new VertexArray();
			BaseFBO.quadPos = new VertexBuffer(false);
			BaseFBO.quadInd = new VertexBuffer(true);
			BaseFBO.quadVAO.create();
			BaseFBO.quadPos.create();
			BaseFBO.quadInd.create();
			RenderEngine.buffer2DQuad(BaseFBO.quadPos, BaseFBO.quadInd, -1, -1, 0, 2, 2);
			BaseFBO.quadVAO.bind();
			VertexArray.enableAttrib(0);
			BaseFBO.quadPos.attribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
			BaseFBO.quadVAO.unbind();
		}

		this.fbo.create();
		this.fbo.bind();

		EnumMap<EnumDrawBufferLocs, Integer> bufsmap = new EnumMap<>(EnumDrawBufferLocs.class);

		int texture;
		for (int i = 0; i < this.fbo.textureCount(); i++)
		{
			texture = this.fbo.getTexture(i);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
			this.createAttachment(i, texture, bufsmap);
		}

		Arrays.fill(this.buffers, GL11.GL_NONE);

		for (Entry<EnumDrawBufferLocs, Integer> buf : bufsmap.entrySet())
		{
			this.buffers[buf.getKey().location] = buf.getValue();
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		this.fbo.unbind();
		this.created = true;
	}

	public void setDrawBuffers()
	{
		if (this.created)
		{
			GL20.glDrawBuffers(GLUtils.bufferInts(this.buffers));
		}
	}

	protected abstract void createAttachment(int attachment, int texture, EnumMap<EnumDrawBufferLocs, Integer> locations);

	public void delete()
	{
		if (this.created)
		{
			this.fbo.delete();
			BaseFBO.count--;
			if (BaseFBO.count == 0)
			{
				BaseFBO.quadVAO.delete();
				BaseFBO.quadPos.delete();
				BaseFBO.quadInd.delete();
			}
			this.created = false;
		}
	}

	public void bind()
	{
		GL11.glViewport(0, 0, this.width, this.height);
		this.fbo.bind();
	}

	public void unbind()
	{
		this.fbo.unbind();
	}

	public void drawQuad()
	{
		BaseFBO.quadVAO.bind();
		BaseFBO.quadInd.bind();
		GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0);
		BaseFBO.quadInd.unbind();
		BaseFBO.quadVAO.unbind();
	}

	public void drawFull(boolean saveMatrix)
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		if (saveMatrix)
		{
			GL11.glPushMatrix();
		}
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		if (saveMatrix)
		{
			GL11.glPushMatrix();
		}
		GL11.glLoadIdentity();
		this.drawQuad();
		if (saveMatrix)
		{
			GL11.glPopMatrix();
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glPopMatrix();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
		}
	}
}
