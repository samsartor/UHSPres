package net.eekysam.uhspres.font;

import gnu.trove.map.hash.TIntByteHashMap;

import org.lwjgl.opengl.GL11;

public class Glyph
{
	public final byte glyphx;
	public final byte glyphy;
	public final byte width;
	public final byte height;
	public final byte xoffset;
	public final byte yoffset;
	public final byte xadvance;
	public final byte page;
	final TIntByteHashMap kerning = new TIntByteHashMap(0);
	
	public Glyph(byte glyphx, byte glyphy, byte width, byte height, byte xoffset, byte yoffset, byte xadvance, byte page)
	{
		this.glyphx = glyphx;
		this.glyphy = glyphy;
		this.width = width;
		this.height = height;
		this.xoffset = xoffset;
		this.yoffset = yoffset;
		this.xadvance = xadvance;
		this.page = page;
	}
	
	public void render(float x, float y)
	{
		float tx = (this.glyphx & 0xFF) / 256.0F;
		float ty = (this.glyphy & 0xFF) / 256.0F;
		float w = (this.width & 0xFF) / 256.0F;
		float h = (this.height & 0xFF) / 256.0F;
		x += this.xoffset / 256.0F;
		y -= this.yoffset / 256.0F;
		GL11.glTexCoord2f(tx, ty + h);
		GL11.glVertex2f(x, y - h);
		GL11.glTexCoord2f(tx + w, ty + h);
		GL11.glVertex2f(x + w, y - h);
		GL11.glTexCoord2f(tx + w, ty);
		GL11.glVertex2f(x + w, y);
		GL11.glTexCoord2f(tx, ty);
		GL11.glVertex2f(x, y);
	}
}
