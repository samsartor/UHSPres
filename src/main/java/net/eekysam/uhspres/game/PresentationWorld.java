package net.eekysam.uhspres.game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

import net.eekysam.uhspres.Presentation;
import net.eekysam.uhspres.asset.AssetLoader;
import net.eekysam.uhspres.asset.GameAsset;
import net.eekysam.uhspres.asset.obj.OBJLoader;
import net.eekysam.uhspres.asset.obj.Vertex;
import net.eekysam.uhspres.render.RenderEngine;
import net.eekysam.uhspres.render.lights.PointLight;
import net.eekysam.uhspres.render.shader.Program;
import net.eekysam.uhspres.render.shader.ShaderUniform;
import net.eekysam.uhspres.render.verts.VertexArray;
import net.eekysam.uhspres.render.verts.VertexBuffer;
import net.eekysam.uhspres.utils.graphics.GLUtils;
import net.eekysam.uhspres.utils.graphics.ImgUtils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class PresentationWorld
{
	public VertexArray worldVAO;
	public VertexBuffer worldPosBuf;
	public VertexBuffer worldNormBuf;
	public VertexBuffer worldUVBuf;
	public VertexBuffer worldIndBuf;
	
	public int worldVertexCount;
	
	public int worldTexture;
	
	public Vector4f ambient = new Vector4f(0.4F, 0.4F, 0.4F, 1.0F);
	
	public ArrayList<PointLight> lights = new ArrayList<PointLight>();
	
	public CameraPathOutput outPath = null;
	public CameraPath path = null;
	
	public PresentationWorld()
	{
		Random rand = new Random();
		for (int i = 0; i < 40; i++) {
			this.lights.add(new PointLight(new Vector3f(rand.nextFloat() * 120 - 60, rand.nextFloat() * 120 - 60, rand.nextFloat() * 40 - 20), new Vector4f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1.0F), 0.5F, 1.4F, 1.0F));
		}
		//this.lights.add(new PointLight(new Vector3f(20.0F, 20.0F, 10.0F), new Vector4f(0.2F, 0.2F, 0.2F, 1.0F), 0.5F, 1.4F, 1.0F));
		//this.lights.add(new PointLight(new Vector3f(0.0F, -50.0F, 0.0F), new Vector4f(0.08F, 0.08F, 0.3F, 1.0F), 0.5F, 0.7F, 1.0F));
		//this.lights.add(new PointLight(new Vector3f(-10.0F, 10.0F, 10.0F), new Vector4f(0.5F, 0.42F, 0.35F, 1.0F), 0.5F, 1.1F, 1.0F));
		this.worldVAO = new VertexArray();
		this.worldPosBuf = new VertexBuffer(false);
		this.worldNormBuf = new VertexBuffer(false);
		this.worldUVBuf = new VertexBuffer(false);
		this.worldIndBuf = new VertexBuffer(true);
		
		if (Presentation.play())
		{
			this.path = new CameraPath(new GameAsset("path.txt"));
		}
		else
		{
			this.outPath = new CameraPathOutput(AssetLoader.instance().getOutput(new GameAsset("last-path.txt"), true), 0.0F);
		}
	}
	
	public void create()
	{
		float scale = 1.0F;
		OBJLoader loader = new OBJLoader();
		loader.read(new GameAsset("world/model.obj"));
		loader.load();
		this.worldVAO.create();
		this.worldPosBuf.create();
		this.worldNormBuf.create();
		this.worldUVBuf.create();
		this.worldIndBuf.create();
		int[][] faces = loader.getFaces();
		Vertex[] verts = loader.getVerts();
		this.worldVertexCount = faces.length * 6;
		int[] inds = new int[this.worldVertexCount];
		for (int i = 0; i < faces.length; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				inds[i * 6 + j] = faces[i][RenderEngine.quadIndicies[j]];
			}
		}
		this.worldIndBuf.vertexData(GLUtils.bufferInts(inds));
		float[] norms = new float[verts.length * 3];
		float[] uvs = new float[verts.length * 2];
		float[] pos = new float[verts.length * 3];
		for (int i = 0; i < verts.length; i++)
		{
			//System.out.println(verts[i]);
			pos[i * 3 + 0] = verts[i].v.x * scale;
			pos[i * 3 + 1] = verts[i].v.y * scale;
			pos[i * 3 + 2] = verts[i].v.z * scale;
			norms[i * 3 + 0] = verts[i].vn.x;
			norms[i * 3 + 1] = verts[i].vn.y;
			norms[i * 3 + 2] = verts[i].vn.z;
			uvs[i * 2 + 0] = verts[i].vt.x;
			uvs[i * 2 + 1] = verts[i].vt.y;
		}
		this.worldPosBuf.vertexData(GLUtils.bufferFloats(pos));
		this.worldNormBuf.vertexData(GLUtils.bufferFloats(norms));
		this.worldUVBuf.vertexData(GLUtils.bufferFloats(uvs));
		this.worldVAO.bind();
		VertexArray.enableAttrib(0);
		this.worldPosBuf.attribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		VertexArray.enableAttrib(1);
		this.worldNormBuf.attribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);
		VertexArray.enableAttrib(2);
		this.worldUVBuf.attribPointer(2, 2, GL11.GL_FLOAT, false, 0, 0);
		this.worldVAO.unbind();
		
		this.worldTexture = GL11.glGenTextures();
		BufferedImage img = null;
		try
		{
			img = AssetLoader.instance().readImage(new GameAsset("world/model.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		/*
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				img.setRGB(i, j, 0xFFFFFF);
			}
		}
		*/

		ByteBuffer buff = ImgUtils.imageToBufferDefault(img);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.worldTexture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, img.getWidth(), img.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buff);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public void renderGameGeo(Matrix4f model, Runnable onMatrixChange, Program program)
	{
		Random r = new Random();
		for (PointLight l : lights) {
			l.position = l.position.translate(r.nextFloat() * 0.2F - 0.1F, r.nextFloat() * 0.2F - 0.1F, r.nextFloat() * 0.2F - 0.1F);
		}

		ShaderUniform un = new ShaderUniform();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.worldTexture);
		this.worldVAO.bind();
		this.worldIndBuf.bind();
		un.setInt(0);
		un.upload(program, "samp_texture");
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, this.worldVertexCount, GL11.GL_UNSIGNED_INT, 0);
		
		this.worldIndBuf.unbind();
		this.worldVAO.unbind();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
}
