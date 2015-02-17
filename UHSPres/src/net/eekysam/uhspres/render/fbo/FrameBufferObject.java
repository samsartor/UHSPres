package net.eekysam.uhspres.render.fbo;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

class FrameBufferObject
{
	private static final int[] colorAttachments;
	
	private int buffer = -1;
	private int[] textures;
	private boolean created = false;
	
	public FrameBufferObject(int textures)
	{
		this.textures = new int[textures];
		Arrays.fill(this.textures, -1);
	}
	
	public void bind()
	{
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.buffer);
	}
	
	public void unbind()
	{
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}
	
	public void delete()
	{
		if (!this.created)
		{
			return;
		}
		for (int i = 0; i < this.textures.length; i++)
		{
			GL11.glDeleteTextures(this.textures[i]);
		}
		GL30.glDeleteFramebuffers(this.buffer);
		this.created = false;
	}
	
	public int create()
	{
		if (this.created)
		{
			return this.buffer;
		}
		for (int i = 0; i < this.textures.length; i++)
		{
			this.textures[i] = GL11.glGenTextures();
		}
		this.buffer = GL30.glGenFramebuffers();
		this.created = true;
		return this.buffer;
	}
	
	public int getBuffer()
	{
		if (this.created)
		{
			return this.buffer;
		}
		return -1;
	}
	
	public int getTexture(int attachment)
	{
		if (this.created && attachment < this.textures.length && attachment >= 0)
		{
			return this.textures[attachment];
		}
		return -1;
	}
	
	public int textureCount()
	{
		return this.textures.length;
	}
	
	public static int glColorAttachment(int attachment)
	{
		if (attachment >= 16)
		{
			return -1;
		}
		return colorAttachments[attachment];
	}
	
	static
	{
		colorAttachments = new int[16];
		colorAttachments[0] = GL30.GL_COLOR_ATTACHMENT0;
		colorAttachments[1] = GL30.GL_COLOR_ATTACHMENT1;
		colorAttachments[2] = GL30.GL_COLOR_ATTACHMENT2;
		colorAttachments[3] = GL30.GL_COLOR_ATTACHMENT3;
		colorAttachments[4] = GL30.GL_COLOR_ATTACHMENT4;
		colorAttachments[5] = GL30.GL_COLOR_ATTACHMENT5;
		colorAttachments[6] = GL30.GL_COLOR_ATTACHMENT6;
		colorAttachments[7] = GL30.GL_COLOR_ATTACHMENT7;
		colorAttachments[8] = GL30.GL_COLOR_ATTACHMENT8;
		colorAttachments[9] = GL30.GL_COLOR_ATTACHMENT9;
		colorAttachments[10] = GL30.GL_COLOR_ATTACHMENT10;
		colorAttachments[11] = GL30.GL_COLOR_ATTACHMENT11;
		colorAttachments[12] = GL30.GL_COLOR_ATTACHMENT12;
		colorAttachments[13] = GL30.GL_COLOR_ATTACHMENT13;
		colorAttachments[14] = GL30.GL_COLOR_ATTACHMENT14;
		colorAttachments[15] = GL30.GL_COLOR_ATTACHMENT15;
	}
}
