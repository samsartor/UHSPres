package net.eekysam.uhspres.render.fbo;

import java.nio.FloatBuffer;
import java.util.EnumMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class GeometryFBO extends DiffuseFBO
{
	public GeometryFBO(int width, int height)
	{
		super(width, height, 2);
	}
	
	protected GeometryFBO(int width, int height, int size)
	{
		super(width, height, size + 2);
	}
	
	@Override
	protected void createAttachment(int attachment, int texture, EnumMap<EnumDrawBufferLocs, Integer> locations)
	{
		switch (attachment)
		{
			case 1:
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGB16F, this.width, this.height, 0, GL11.GL_RGB, GL11.GL_FLOAT, (FloatBuffer) null);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
				GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT1, GL11.GL_TEXTURE_2D, texture, 0);
				locations.put(EnumDrawBufferLocs.NORMAL, GL30.GL_COLOR_ATTACHMENT1);
				return;
				
			case 2:
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_DEPTH_COMPONENT32F, this.width, this.height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (FloatBuffer) null);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
				GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, texture, 0);
				return;
				
			default:
				super.createAttachment(attachment, texture, locations);
				return;
		}
	}
	
	public int getNormal()
	{
		return this.getTexture(1);
	}
	
	public int getDepth()
	{
		return this.getTexture(2);
	}
}
