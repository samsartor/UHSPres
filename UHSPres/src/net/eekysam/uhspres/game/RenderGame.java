package net.eekysam.uhspres.game;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import net.eekysam.uhspres.Presentation;
import net.eekysam.uhspres.asset.GameAsset;
import net.eekysam.uhspres.asset.OBJLoader;
import net.eekysam.uhspres.font.Font;
import net.eekysam.uhspres.render.IScreenLayer;
import net.eekysam.uhspres.render.RenderEngine;
import net.eekysam.uhspres.render.fbo.DiffuseFBO;
import net.eekysam.uhspres.render.verts.VertexArray;
import net.eekysam.uhspres.render.verts.VertexBuffer;
import net.eekysam.uhspres.utils.graphics.GLUtils;

import org.lwjgl.opengl.GL11;

public class RenderGame implements IScreenLayer
{
	public Timer timer = null;
	public Font testFont;

	public VertexArray testVAO;
	public VertexBuffer testPosBuf;
	public VertexBuffer testIndBuf;
	public int testVertexCount;

	public RenderGame()
	{
		try
		{
			this.testFont = Font.loadFont(new GameAsset("fonts/Arial.fnt"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		this.testFont.load(Presentation.instance().theAssetLoader);

		GameAsset testAsset = new GameAsset("test.obj");
		ArrayList<Float> testPos = new ArrayList<Float>();
		ArrayList<Integer> testInd = new ArrayList<Integer>();
		OBJLoader loader = new OBJLoader(testAsset, testInd, testPos, 3);
		loader.load();
		FloatBuffer testPosBufVals = GLUtils.bufferFloats(testPos);
		IntBuffer testIndBufVals = GLUtils.bufferInts(testInd);
		this.testVAO = new VertexArray();
		this.testVAO.create();
		this.testPosBuf = new VertexBuffer(false);
		this.testPosBuf.create();
		this.testIndBuf = new VertexBuffer(true);
		this.testIndBuf.create();
		this.testPosBuf.vertexData(testPosBufVals);
		this.testIndBuf.vertexData(testIndBufVals);
		this.testVAO.bind();
		VertexArray.enableAttrib(0);
		this.testPosBuf.attribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		this.testVAO.unbind();
		this.testVertexCount = testInd.size();
	}

	@Override
	public void render(RenderEngine engine, DiffuseFBO target)
	{
		if (this.timer == null)
		{
			this.timer = new Timer();
			this.timer.resetTimer();
			this.timer.partialTicks += 1;
		}

		for (int i = 0; i < this.timer.elapsedTicks; i++)
		{
			this.tickGame();
		}

		this.renderGame(this.timer.partialTicks, engine, target);
		this.timer.update();
	}

	public void tickGame()
	{

	}

	public void renderGame(float partial, RenderEngine engine, DiffuseFBO target)
	{
		this.testVAO.bind();
		this.testIndBuf.bind();
		GL11.glDrawElements(GL11.GL_TRIANGLES, this.testVertexCount, GL11.GL_UNSIGNED_INT, 0);
		this.testVAO.unbind();
		this.testIndBuf.unbind();
	}

	@Override
	public boolean isOutdated()
	{
		return true;
	}

	@Override
	public boolean hasContent()
	{
		return true;
	}

	@Override
	public boolean pauseRequested()
	{
		return false;
	}

}
