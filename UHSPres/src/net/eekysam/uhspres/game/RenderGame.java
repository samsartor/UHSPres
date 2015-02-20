package net.eekysam.uhspres.game;

import java.awt.Color;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import net.eekysam.uhspres.Presentation;
import net.eekysam.uhspres.asset.Asset;
import net.eekysam.uhspres.asset.GameAsset;
import net.eekysam.uhspres.asset.OBJLoader;
import net.eekysam.uhspres.font.Font;
import net.eekysam.uhspres.render.IScreenLayer;
import net.eekysam.uhspres.render.RenderEngine;
import net.eekysam.uhspres.render.Transform;
import net.eekysam.uhspres.render.Transform.MatrixMode;
import net.eekysam.uhspres.render.fbo.DiffuseFBO;
import net.eekysam.uhspres.render.fbo.EnumDrawBufferLocs;
import net.eekysam.uhspres.render.fbo.GeometryFBO;
import net.eekysam.uhspres.render.shader.Program;
import net.eekysam.uhspres.render.shader.Shader;
import net.eekysam.uhspres.render.shader.Shader.ShaderType;
import net.eekysam.uhspres.render.shader.ShaderUniform;
import net.eekysam.uhspres.render.verts.VertexArray;
import net.eekysam.uhspres.render.verts.VertexBuffer;
import net.eekysam.uhspres.utils.graphics.GLUtils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class RenderGame implements IScreenLayer
{
	public Timer timer = null;
	public Font testFont;

	public VertexArray testVAO;
	public VertexBuffer testPosBuf;
	public VertexBuffer testIndBuf;
	public int testVertexCount;

	private Program basic;
	private Shader basicVert;
	private Shader basicFrag;
	private ShaderUniform basicColor;
	private ShaderUniform clip;

	public final RenderEngine theEngine;

	public final Transform transform;
	public final ShaderUniform mvpMatrix;

	public CameraView veiw;

	public GeometryFBO geometry;

	public RenderGame(RenderEngine engine)
	{
		this.theEngine = engine;
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

		Asset basicAsset = new Asset("shaders/basic");
		this.basic = new Program(EnumDrawBufferLocs.DIFFUSE);
		this.basicFrag = new Shader(ShaderType.FRAGMENT, basicAsset);
		this.basicVert = new Shader(ShaderType.VERTEX, basicAsset);
		this.basic.create();
		this.theEngine.createShader(this.basicFrag);
		this.theEngine.createShader(this.basicVert);
		this.basicFrag.attach(this.basic);
		this.basicVert.attach(this.basic);
		this.theEngine.linkProgram(this.basic, basicAsset.file);

		this.basicColor = new ShaderUniform("un_color");
		this.basicColor.setColorRGBA(new Color(0xFFF2C1));
		this.clip = new ShaderUniform("un_clip");

		this.transform = new Transform();
		this.mvpMatrix = new ShaderUniform("un_mvp");
		this.veiw = new CameraView();

		this.geometry = new GeometryFBO(Presentation.width(), Presentation.height());
		this.geometry.create();
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

		this.veiw.update(this.transform.get(MatrixMode.VIEW), new Vector3f(0.0F, 0.0F, 1.0F));

		this.geometry.bind();
		GL11.glClearColor(0, 0, 0, 0);
		GL11.glClearDepth(1.0F);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		this.renderGame(this.timer.partialTicks, engine, target);

		target.bind();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.geometry.getDiffuse());
		engine.bindBlit(0);
		GL11.glEnable(GL11.GL_BLEND);
		this.geometry.drawQuad();
		GL11.glDisable(GL11.GL_BLEND);
		engine.unbindBlit();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		this.timer.update();
	}

	public void tickGame()
	{

	}

	public void renderGame(float partial, RenderEngine engine, DiffuseFBO target)
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDepthRange(0.0f, 1.0f);
		Matrix4f project = this.transform.get(MatrixMode.PROJECT);
		Transform.createPerspective(project, 60.0F, Presentation.aspect(), 1.0F, 100.0F);
		this.clip.setFloats(1.0F, 10.0F);
		Matrix4f model = this.transform.get(MatrixMode.MODEL);
		model.setIdentity();
		Matrix4f mvp = this.transform.getMVP();
		this.mvpMatrix.setMatrix(mvp);
		this.testVAO.bind();
		this.testIndBuf.bind();
		this.basic.bind();
		this.mvpMatrix.upload(this.basic);
		this.basicColor.upload(this.basic);
		this.clip.upload(this.basic);
		GL11.glDrawElements(GL11.GL_TRIANGLES, this.testVertexCount, GL11.GL_UNSIGNED_INT, 0);
		this.basic.unbind();
		this.testVAO.unbind();
		this.testIndBuf.unbind();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
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
