package net.eekysam.uhspres.render.fbo;

import java.nio.FloatBuffer;
import java.util.EnumMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class ValueFBO extends BaseFBO
{
	public ValueFBO(int width, int height)
	{
		super(width, height, 1);
	}
	
	protected ValueFBO(int width, int height, int size)
	{
		super(width, height, size + 1);
	}
	
	@Override
	protected void createAttachment(int attachment, int texture, EnumMap<EnumDrawBufferLocs, Integer> locations)
	{
		if (attachment == 0)
		{
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_R32F, this.width, this.height, 0, GL11.GL_RED, GL11.GL_FLOAT, (FloatBuffer) null);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture, 0);
			locations.put(EnumDrawBufferLocs.VALUE, GL30.GL_COLOR_ATTACHMENT0);
		}
	}
	
	public int getValue()
	{
		return this.getTexture(0);
	}
}
