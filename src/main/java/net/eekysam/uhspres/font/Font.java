package net.eekysam.uhspres.font;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.PrimitiveIterator.OfInt;

import net.eekysam.uhspres.asset.AssetLoader;
import net.eekysam.uhspres.asset.GameAsset;
import net.eekysam.uhspres.font.bmf.BMFFile;
import net.eekysam.uhspres.utils.graphics.ImgUtils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Font
{
	public final TIntObjectHashMap<Glyph> glyphs = new TIntObjectHashMap<>();
	public final int size;
	public final int lineHeight;
	private int[] textures;
	private GameAsset[] texturePaths;
	private boolean loaded = false;
	public float switchPerCharStat = Float.NaN;
	
	Font(int size, int lineHeight, GameAsset[] texturePaths)
	{
		this.size = size;
		this.lineHeight = lineHeight;
		this.texturePaths = texturePaths;
	}
	
	public void load(AssetLoader render)
	{
		if (this.loaded)
		{
			return;
		}
		try
		{
			this.textures = new int[this.texturePaths.length];
			for (int i = 0; i < this.textures.length; i++)
			{
				this.textures[i] = this.loadTexture(this.texturePaths[i], render);
			}
		}
		catch (IllegalArgumentException | IOException e)
		{
			return;
		}
	}
	
	float getScale(float size)
	{
		return size / this.size;
	}
	
	public void unload()
	{
		if (!this.loaded)
		{
			return;
		}
		for (int id : this.textures)
		{
			GL11.glDeleteTextures(id);
		}
		this.loaded = false;
	}
	
	private int loadTexture(GameAsset path, AssetLoader render) throws IOException
	{
		BufferedImage img = render.readImage(path);
		if (img != null)
		{
			int texId = GL11.glGenTextures();
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
			
			ByteBuffer bytes = ImgUtils.imageToBufferDefault(img);
			
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, img.getWidth(), img.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bytes);
			//GLUtils.glTexParameter(GL11.GL_TEXTURE_2D, GL33.GL_TEXTURE_SWIZZLE_RGBA, GL11.GL_ONE, GL11.GL_ONE, GL11.GL_ONE, GL11.GL_RED);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			
			return texId;
		}
		else
		{
			throw new IllegalArgumentException("Texture file does not exist: " + path);
		}
	}
	
	public void addGlyph(Glyph glyph, int codepoint)
	{
		this.glyphs.put(codepoint, glyph);
	}
	
	public Glyph getGlyph(int codepoint)
	{
		return this.glyphs.get(codepoint);
	}
	
	float segmentLength(final String text, final float scale, final boolean kerning)
	{
		float length = 0.0F;
		OfInt codes = text.codePoints().iterator();
		Glyph prev = null;
		while (codes.hasNext())
		{
			int code = codes.next();
			if (kerning && prev != null)
			{
				length += prev.kerning.get(code) * scale;
			}
			Glyph g = this.getGlyph(code);
			length += g.xadvance * scale;
			prev = g;
		}
		return length;
	}
	
	public int segmentClip(final String text, final float scale, float length, final boolean kerning)
	{
		float unleng = length / scale;
		OfInt codes = text.codePoints().iterator();
		Glyph prev = null;
		int i = 0;
		while (codes.hasNext())
		{
			int code = codes.next();
			if (kerning && prev != null)
			{
				unleng -= prev.kerning.get(code);
			}
			Glyph g = this.getGlyph(code);
			unleng -= g.xadvance;
			if (unleng <= 0)
			{
				return text.offsetByCodePoints(0, i);
			}
			i++;
			prev = g;
		}
		return text.length();
	}
	
	public void renderSegment(final String text, final float size, float xpos, float ypos, final boolean kerning)
	{
		float scale = size / (this.size / 256.0F);
		int texture = -1;
		OfInt codes = text.chars().iterator();
		Glyph prev = null;
		boolean started = false;
		float x = 0;
		float y = 0;
		GL11.glPushMatrix();
		GL11.glTranslatef(xpos, ypos, 0);
		GL11.glScalef(scale, scale, 1);
		int switchCount = 0;
		while (codes.hasNext())
		{
			int code = codes.next();
			if (kerning && prev != null)
			{
				x += prev.kerning.get(code) / 256.0F;
			}
			Glyph g = this.getGlyph(code);
			if (g != null)
			{
				if (texture != (g.page & 0xFF))
				{
					texture = g.page & 0xFF;
					if (started)
					{
						GL11.glEnd();
					}
					this.bindTexture(this.textures[g.page & 0xFF]);
					switchCount++;
					GL11.glBegin(GL11.GL_QUADS);
					started = true;
				}
				g.render(x, y);
				x += g.xadvance / 256.0F;
				prev = g;
			}
		}
		if (started)
		{
			GL11.glEnd();
		}
		float per = switchCount / (float) text.length();
		if (this.switchPerCharStat == Float.NaN)
		{
			this.switchPerCharStat = per;
		}
		else
		{
			this.switchPerCharStat *= 0.95F;
			this.switchPerCharStat += per * 0.05F;
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	private void bindTexture(int id)
	{
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}
	
	public static Font loadFont(GameAsset font) throws IOException
	{
		BMFFile bmf = new BMFFile(font.getInput());
		BMFConvertToFont convert = new BMFConvertToFont(bmf, font.getParent());
		return convert.getFont();
	}
}
