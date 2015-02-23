package net.eekysam.uhspres.game;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import net.eekysam.uhspres.Presentation;
import net.eekysam.uhspres.asset.GameAsset;
import net.eekysam.uhspres.asset.OBJLoader;
import net.eekysam.uhspres.font.Font;
import net.eekysam.uhspres.render.CalculateNormals;
import net.eekysam.uhspres.render.IScreenLayer;
import net.eekysam.uhspres.render.RenderEngine;
import net.eekysam.uhspres.render.Transform;
import net.eekysam.uhspres.render.Transform.MatrixMode;
import net.eekysam.uhspres.render.fbo.DiffuseFBO;
import net.eekysam.uhspres.render.fbo.GeometryFBO;
import net.eekysam.uhspres.render.fbo.ValueFBO;
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
	private class UpdateGeometryMat implements Runnable
	{
		public Transform transform;
		
		public UpdateGeometryMat(Transform transform)
		{
			this.transform = transform;
		}
		
		@Override
		public void run()
		{
			RenderGame.this.theEngine.geometryPass.uploadMV(this.transform.getResult(0b011));
			RenderGame.this.theEngine.geometryPass.uploadMVP(this.transform.getResult(0b111));
		}
	}
	
	public Timer timer = null;
	public float partial;
	public Font testFont;
	
	public VertexArray testVAO;
	public VertexBuffer testPosBuf;
	public VertexBuffer testNormBuf;
	public VertexBuffer testIndBuf;
	public int testVertexCount;
	
	public final RenderEngine theEngine;
	
	public final Transform cameraTransform;
	public final Transform lightTransform;
	
	public CameraView veiw;
	
	public GeometryFBO geometry;
	public ValueFBO valSwap;
	public ValueFBO occlusion;
	
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
		float[] pos = GLUtils.floatArray(testPos);
		int[] ind = GLUtils.intArray(testInd);
		CalculateNormals calcn = new CalculateNormals(pos, ind, true);
		calcn.calculate();
		float[] norm = calcn.getNormalData();
		FloatBuffer testPosBufVals = GLUtils.bufferFloats(pos);
		FloatBuffer testNormBufVals = GLUtils.bufferFloats(norm);
		IntBuffer testIndBufVals = GLUtils.bufferInts(ind);
		this.testVAO = new VertexArray();
		this.testVAO.create();
		this.testPosBuf = new VertexBuffer(false);
		this.testPosBuf.create();
		this.testNormBuf = new VertexBuffer(false);
		this.testNormBuf.create();
		this.testIndBuf = new VertexBuffer(true);
		this.testIndBuf.create();
		this.testPosBuf.vertexData(testPosBufVals);
		this.testNormBuf.vertexData(testNormBufVals);
		this.testIndBuf.vertexData(testIndBufVals);
		this.testVAO.bind();
		VertexArray.enableAttrib(0);
		this.testPosBuf.attribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		VertexArray.enableAttrib(1);
		this.testNormBuf.attribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);
		this.testVAO.unbind();
		this.testVertexCount = testInd.size();
		
		this.cameraTransform = new Transform();
		this.lightTransform = new Transform();
		this.veiw = new CameraView();
		
		this.geometry = new GeometryFBO(Presentation.width(), Presentation.height());
		this.geometry.create();
		this.valSwap = new ValueFBO(Presentation.width(), Presentation.height());
		this.valSwap.create();
		this.occlusion = new ValueFBO(Presentation.width(), Presentation.height());
		this.occlusion.create();
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
		
		this.renderGame();
		
		GL11.glDisable(GL11.GL_BLEND);
		
		target.bind();
		ShaderUniform un = new ShaderUniform();
		this.theEngine.mulblit.bind();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.geometry.getDiffuse());
		un.setInt(0);
		un.upload(this.theEngine.mulblit, "samp_diffuse");
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.occlusion.getValue());
		un.setInt(1);
		un.upload(this.theEngine.mulblit, "samp_value");
		
		this.geometry.drawQuad();
		
		this.theEngine.mulblit.unbind();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		this.timer.update();
	}
	
	public void tickGame()
	{
		
	}
	
	public void renderGame()
	{
		this.veiw.update(this.cameraTransform.get(MatrixMode.VIEW), new Vector3f(0.0F, 0.0F, 1.0F));
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDepthRange(0.0f, 1.0f);
		
		Matrix4f project = this.cameraTransform.get(MatrixMode.PROJECT);
		Transform.createPerspective(project, 60.0F, Presentation.aspect(), 1.0F, 100.0F);
		Matrix4f model = this.cameraTransform.get(MatrixMode.MODEL);
		model.setIdentity();
		
		this.geometry.bind();
		GL11.glClearColor(0, 0, 0, 0);
		GL11.glClearDepth(1.0F);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		
		this.theEngine.geometryPass.start(this.geometry);
		this.renderGameGeo(this.cameraTransform, new UpdateGeometryMat(this.cameraTransform));
		this.theEngine.geometryPass.end(this.geometry);
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		this.theEngine.ssaoPass.renderOcclusion(Presentation.width(), Presentation.height(), this.geometry, this.valSwap, this.occlusion, 3.0F, project);
	}
	
	public void renderGameGeo(Transform transform, Runnable onMatrixChange)
	{
		Matrix4f model = transform.get(MatrixMode.MODEL);
		Matrix4f modelStart = Matrix4f.load(model, null);
		
		this.testVAO.bind();
		this.testIndBuf.bind();
		
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				model.translate(new Vector3f(i * 5.0F, 0.0F, j * 5.0F));
				model.rotate((float) Math.PI / 4, new Vector3f(0.0F, 1.0F, 0.0F));
				
				onMatrixChange.run();
				
				GL11.glDrawElements(GL11.GL_TRIANGLES, this.testVertexCount, GL11.GL_UNSIGNED_INT, 0);
				
				model.load(modelStart);
			}
		}
		
		this.testVAO.unbind();
		this.testIndBuf.unbind();
	}
	
	/*
	public void renderGame(float partial, RenderEngine engine, DiffuseFBO target)
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDepthRange(0.0f, 1.0f);
		
		Matrix4f project = this.transform.get(MatrixMode.PROJECT);
		Transform.createPerspective(project, 60.0F, Presentation.aspect(), 1.0F, 100.0F);
		this.clip.setFloats(4.0F, 16.0F, Presentation.width(), Presentation.height());
		Matrix4f model = this.transform.get(MatrixMode.MODEL);
		model.setIdentity();
		
		this.testVAO.bind();
		this.testIndBuf.bind();
		this.basic.bind();
		
		this.basicColor.upload(this.basic);
		this.clip.upload(this.basic);
		
		Matrix4f modelStart = Matrix4f.load(model, null);
		
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				model.translate(new Vector3f(i * 5.0F, 0.0F, j * 5.0F));
				model.rotate((float) Math.PI / 4, new Vector3f(0.0F, 1.0F, 0.0F));
				
				Matrix4f mvp = this.transform.getMVP();
				this.mvpMatrix.setMatrix(mvp);
				Matrix4f mv = this.transform.getResult(0b011);
				this.mvMatrix.setMatrix(mv);
				
				this.mvpMatrix.upload(this.basic);
				this.mvMatrix.upload(this.basic);
				
				GL11.glDrawElements(GL11.GL_TRIANGLES, this.testVertexCount, GL11.GL_UNSIGNED_INT, 0);
				
				model.load(modelStart);
			}
		}
		
		this.basic.unbind();
		this.testVAO.unbind();
		this.testIndBuf.unbind();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	*/
	
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
