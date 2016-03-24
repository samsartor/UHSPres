package net.eekysam.uhspres.render.shading;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Random;

import net.eekysam.uhspres.render.RenderEngine;
import net.eekysam.uhspres.render.fbo.EnumDrawBufferLocs;
import net.eekysam.uhspres.render.fbo.GeometryFBO;
import net.eekysam.uhspres.render.fbo.ValueFBO;
import net.eekysam.uhspres.render.shader.Program;
import net.eekysam.uhspres.render.shader.ShaderUniform;
import net.eekysam.uhspres.utils.graphics.ImgUtils;
import net.eekysam.uhspres.utils.graphics.ImgUtils.EnumPixelByte;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class ShadeSSAO extends ShadePass
{
	private Program ssao;
	private Program blur;
	private int noiseTexture;
	private int noiseSize;
	private Vector3f[] samples;
	private long texrand;
	
	public ShadeSSAO(RenderEngine engine, Program blur, int noiseSize, int sampleSize, Random rand)
	{
		super(engine);
		this.blur = blur;
		this.noiseSize = noiseSize;
		this.ssao = new Program(EnumDrawBufferLocs.VALUE);
		
		Random srand = new Random(rand.nextLong());
		this.texrand = rand.nextLong();
		
		this.samples = new Vector3f[sampleSize];
		float scale;
		for (int i = 0; i < this.samples.length; ++i)
		{
			this.samples[i] = new Vector3f(srand.nextFloat() * 2 - 1, srand.nextFloat() * 2 - 1, srand.nextFloat() * 2 - 1);
			this.samples[i].normalise();
			scale = i / (float) sampleSize;
			scale *= scale;
			scale *= 0.9F;
			scale += 0.1F;
			this.samples[i].scale(scale);
		}
	}
	
	public void renderOcclusion(int width, int height, GeometryFBO geo, ValueFBO swap, ValueFBO occlusion, float radius, Matrix4f pmat)
	{
		ShaderUniform un = new ShaderUniform();
		
		this.ssao.bind();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, geo.getNormal());
		un.setInt(0);
		//un.upload(this.ssao, "samp_norm");
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, geo.getDepth());
		un.setInt(1);
		un.upload(this.ssao, "samp_depth");
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.noiseTexture);
		un.setInt(2);
		un.upload(this.ssao, "samp_noise");
		
		un.setFloats(width / (float) this.noiseSize, height / (float) this.noiseSize);
		un.upload(this.ssao, "u_noise_scale");
		un.setVectors(this.samples);
		un.upload(this.ssao, "un_samples");
		un.setInt(this.samples.length);
		un.upload(this.ssao, "un_samp_size");
		un.setFloat(radius);
		un.upload(this.ssao, "un_radius");
		un.setMatrix(pmat);
		un.upload(this.ssao, "un_pmat");
		un.setMatrix(Matrix4f.invert(pmat, null));
		un.upload(this.ssao, "un_inv_pmat");
		
		occlusion.bind();
		geo.drawQuad();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		this.blur.bind();
		
		un.setInt(0);
		un.upload(this.blur, "samp_value");
		un.setFloat(this.noiseSize / 2);
		un.upload(this.blur, "un_radius");
		un.setFloats(1.0F / width, 1.0F / height);
		un.upload(this.blur, "un_texel");
		un.setFloat(0.0F);
		un.upload(this.blur, "un_radImpact");
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glDisable(GL11.GL_BLEND);
		
		swap.bind();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, occlusion.getValue());
		un.setFloats(1.0F, 0.0F);
		un.upload(this.blur, "un_dir");
		occlusion.drawQuad();
		
		occlusion.bind();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, swap.getValue());
		un.setFloats(0.0F, 1.0F);
		un.upload(this.blur, "un_dir");
		swap.drawQuad();
		
		this.blur.unbind();
	}
	
	@Override
	public void doCreate()
	{
		this.ssao.create();
		this.shaders.blitV.attach(this.ssao);
		this.shaders.ssaoF.attach(this.ssao);
		this.engine.linkProgram(this.ssao, this.shaders.ssaoA.file);
		
		Random trand = new Random(this.texrand);
		
		BufferedImage noise = new BufferedImage(this.noiseSize, this.noiseSize, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < this.noiseSize; i++)
		{
			for (int j = 0; j < this.noiseSize; j++)
			{
				Vector3f norm = new Vector3f(trand.nextFloat() * 2 - 1, trand.nextFloat() * 2 - 1, trand.nextFloat() * 2 - 1);
				norm.normalise();
				noise.setRGB(i, j, (new Color((norm.x + 1) / 2, (norm.y + 1) / 2, (norm.z + 1) / 2)).getRGB());
			}
		}
		
		this.noiseTexture = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.noiseTexture);
		
		ByteBuffer bytes = ImgUtils.imageToBuffer(noise, EnumPixelByte.RED, EnumPixelByte.GREEN, EnumPixelByte.BLUE);
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, noise.getWidth(), noise.getHeight(), 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, bytes);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	@Override
	public void doDelete()
	{
	}
}
