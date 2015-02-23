package net.eekysam.uhspres.game;

import java.io.IOException;

import net.eekysam.uhspres.Presentation;
import net.eekysam.uhspres.asset.GameAsset;
import net.eekysam.uhspres.font.Font;
import net.eekysam.uhspres.render.IScreenLayer;
import net.eekysam.uhspres.render.RenderEngine;
import net.eekysam.uhspres.render.Transform;
import net.eekysam.uhspres.render.Transform.MatrixMode;
import net.eekysam.uhspres.render.fbo.DiffuseFBO;
import net.eekysam.uhspres.render.fbo.GeometryFBO;
import net.eekysam.uhspres.render.fbo.ValueFBO;
import net.eekysam.uhspres.render.lights.PointLight;
import net.eekysam.uhspres.render.shader.Program;
import net.eekysam.uhspres.render.shader.ShaderUniform;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
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
	public Font font;
	
	public final RenderEngine theEngine;
	
	public final Transform cameraTransform;
	public final Transform lightTransform;
	
	public CameraView veiw;
	
	public GeometryFBO geometry;
	public ValueFBO valSwap;
	public ValueFBO occlusion;
	public DiffuseFBO light;
	
	public PresentationWorld world;
	
	public RenderGame(RenderEngine engine)
	{
		this.theEngine = engine;
		try
		{
			this.font = Font.loadFont(new GameAsset("fonts/Arial.fnt"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		this.font.load(Presentation.instance().theAssetLoader);
		
		this.cameraTransform = new Transform();
		this.lightTransform = new Transform();
		this.veiw = new CameraView();
		
		this.geometry = new GeometryFBO(Presentation.width(), Presentation.height());
		this.geometry.create();
		this.valSwap = new ValueFBO(Presentation.width(), Presentation.height());
		this.valSwap.create();
		this.occlusion = new ValueFBO(Presentation.width(), Presentation.height());
		this.occlusion.create();
		this.light = new DiffuseFBO(Presentation.width(), Presentation.height());
		this.light.create();
		
		this.world = new PresentationWorld();
		this.world.create();
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
		
		target.bind();
		ShaderUniform un = new ShaderUniform();
		this.theEngine.blit.bind();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.geometry.getDiffuse());
		un.setInt(0);
		un.upload(this.theEngine.blit, "samp_diffuse");
		
		GL11.glEnable(GL11.GL_BLEND);
		GL14.glBlendEquation(GL14.GL_FUNC_ADD);
		GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE, GL11.GL_ZERO);
		
		this.geometry.drawQuad();
		
		GL14.glBlendEquation(GL14.GL_FUNC_ADD);
		GL14.glBlendFuncSeparate(GL11.GL_DST_COLOR, GL11.GL_ZERO, GL11.GL_ZERO, GL11.GL_ONE);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.light.getDiffuse());
		
		this.light.drawQuad();
		
		this.theEngine.blit.unbind();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glDisable(GL11.GL_BLEND);
		
		this.timer.update();
	}
	
	public void tickGame()
	{
		
	}
	
	public void renderGame()
	{
		GL11.glDisable(GL11.GL_BLEND);
		ShaderUniform un = new ShaderUniform();
		
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
		this.renderGameGeo(this.cameraTransform, new UpdateGeometryMat(this.cameraTransform), this.theEngine.geometryPass.geo);
		this.theEngine.geometryPass.end(this.geometry);
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		this.occlusion.bind();
		GL11.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		
		this.theEngine.ssaoPass.renderOcclusion(Presentation.width(), Presentation.height(), this.geometry, this.valSwap, this.occlusion, 5.0F, project);
		
		this.light.bind();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		this.theEngine.lumblit.bind();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.occlusion.getValue());
		un.setInt(0);
		un.upload(this.theEngine.lumblit, "samp_value");
		un.setVector(this.world.ambient);
		un.upload(this.theEngine.lumblit, "un_base");
		
		this.occlusion.drawQuad();
		
		GL11.glEnable(GL11.GL_BLEND);
		GL14.glBlendEquation(GL14.GL_FUNC_ADD);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		
		this.theEngine.light.bind();
		PointLight.uploadCommonUniforms(this.cameraTransform, this.theEngine.light);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.geometry.getNormal());
		un.setInt(0);
		un.upload(this.theEngine.light, "samp_norm");
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.geometry.getDepth());
		un.setInt(1);
		un.upload(this.theEngine.light, "samp_depth");
		for (PointLight light : this.world.lights)
		{
			light.uploadUniforms(this.theEngine.light);
			this.light.drawQuad();
		}
		
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void renderGameGeo(Transform transform, Runnable onMatrixChange, Program program)
	{
		onMatrixChange.run();
		Matrix4f model = transform.get(MatrixMode.MODEL);
		this.world.renderGameGeo(model, onMatrixChange, program);
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
