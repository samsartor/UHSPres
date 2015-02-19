package net.eekysam.uhspres.render.verts;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

public class VertexBuffer
{
	private int buffer = -1;
	private final int bufferType;
	private boolean created = false;

	public VertexBuffer(boolean isIndexBuffer)
	{
		if (isIndexBuffer)
		{
			this.bufferType = GL15.GL_ELEMENT_ARRAY_BUFFER;
		}
		else
		{
			this.bufferType = GL15.GL_ARRAY_BUFFER;
		}
	}

	public int getBuffer()
	{
		if (this.created)
		{
			return this.buffer;
		}
		return -1;
	}

	public void create()
	{
		if (this.created)
		{
			return;
		}
		this.buffer = GL15.glGenBuffers();
		this.created = true;
	}

	public void delete()
	{
		if (!this.created)
		{
			return;
		}
		GL15.glDeleteBuffers(this.buffer);
		this.created = false;
	}

	public void bind()
	{
		if (this.created)
		{
			GL15.glBindBuffer(this.bufferType, this.buffer);
		}
	}

	public void unbind()
	{
		if (this.created)
		{
			GL15.glBindBuffer(this.bufferType, 0);
		}
	}

	public void attribPointer(int index, int size, int type, boolean normalized, int stride, int offset)
	{
		if (!this.created || this.bufferType != GL15.GL_ARRAY_BUFFER)
		{
			return;
		}
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.buffer);
		GL20.glVertexAttribPointer(index, size, type, normalized, stride, offset);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	public void vertexData(Buffer data)
	{
		if (!this.created)
		{
			return;
		}
		GL15.glBindBuffer(this.bufferType, this.buffer);
		if (data instanceof ByteBuffer)
		{
			GL15.glBufferData(this.bufferType, (ByteBuffer) data, GL15.GL_STREAM_DRAW);
		}
		else if (data instanceof ShortBuffer)
		{
			GL15.glBufferData(this.bufferType, (ShortBuffer) data, GL15.GL_STREAM_DRAW);
		}
		else if (data instanceof IntBuffer)
		{
			GL15.glBufferData(this.bufferType, (IntBuffer) data, GL15.GL_STREAM_DRAW);
		}
		else if (data instanceof FloatBuffer)
		{
			GL15.glBufferData(this.bufferType, (FloatBuffer) data, GL15.GL_STREAM_DRAW);
		}
		else if (data instanceof DoubleBuffer)
		{
			GL15.glBufferData(this.bufferType, (DoubleBuffer) data, GL15.GL_STREAM_DRAW);
		}
		GL15.glBindBuffer(this.bufferType, 0);
	}
}
