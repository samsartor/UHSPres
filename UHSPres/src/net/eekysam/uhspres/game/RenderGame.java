package net.eekysam.uhspres.game;

import java.io.IOException;

import net.eekysam.uhspres.Presentation;
import net.eekysam.uhspres.asset.Asset;
import net.eekysam.uhspres.asset.GameAsset;
import net.eekysam.uhspres.font.Font;
import net.eekysam.uhspres.render.IScreenLayer;
import net.eekysam.uhspres.render.RenderEngine;
import net.eekysam.uhspres.render.fbo.DiffuseFBO;
import net.eekysam.uhspres.render.fbo.EnumDrawBufferLocs;
import net.eekysam.uhspres.render.fbo.GeometryFBO;
import net.eekysam.uhspres.render.shader.Program;
import net.eekysam.uhspres.render.shader.ProgramLinkInfo;
import net.eekysam.uhspres.render.shader.Shader;
import net.eekysam.uhspres.render.shader.Shader.ShaderType;
import net.eekysam.uhspres.render.shader.ShaderCreateInfo;
import net.eekysam.uhspres.render.shader.ShaderUniform;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.glu.GLU;

public class RenderGame implements IScreenLayer
{
	public Timer timer = null;
	public Font testFont;

	public Program makeGeo;
	public Shader makeGeoVert;
	public Shader makeGeoFrag;

	public Program testGeo;
	public Shader testGeoVert;
	public Shader testGeoFrag;

	public ShaderUniform diffuseSamp;
	public ShaderUniform normSamp;
	public ShaderUniform depthSamp;
	public ShaderUniform clipRange;

	public GeometryFBO geoFBO;

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

		this.diffuseSamp = new ShaderUniform("in_diffuse");
		this.normSamp = new ShaderUniform("in_normal");
		this.depthSamp = new ShaderUniform("in_depth");
		this.clipRange = new ShaderUniform("clipRange");

		Asset shad;
		ProgramLinkInfo linfo;

		this.makeGeo = new Program(EnumDrawBufferLocs.DIFFUSE, EnumDrawBufferLocs.NORMAL);
		this.makeGeo.create();
		shad = new Asset("shaders/make_geo");
		this.makeGeoVert = new Shader(ShaderType.VERTEX, shad);
		this.makeGeoFrag = new Shader(ShaderType.FRAGMENT, shad);
		this.createShader(this.makeGeoVert, this.makeGeo);
		this.createShader(this.makeGeoFrag, this.makeGeo);
		linfo = this.makeGeo.link();
		if (linfo.state != ProgramLinkInfo.Error.NONE)
		{
			System.err.printf("Failed to link program \"%s\" because of a %s.%n", "make_geo", linfo.state);
			if (linfo.log != null && !linfo.log.trim().isEmpty())
			{
				System.err.println(linfo.log);
			}
		}

		this.geoFBO = new GeometryFBO(Presentation.width(), Presentation.height());
		this.geoFBO.create();

		this.diffuseSamp.setInt(0);
		this.normSamp.setInt(1);
		this.depthSamp.setInt(2);

		this.testGeo = new Program(EnumDrawBufferLocs.DIFFUSE);
		this.testGeo.create();
		shad = new Asset("shaders/test_geo");
		this.testGeoVert = new Shader(ShaderType.VERTEX, shad);
		this.testGeoFrag = new Shader(ShaderType.FRAGMENT, shad);
		this.createShader(this.testGeoVert, this.testGeo);
		this.createShader(this.testGeoFrag, this.testGeo);
		linfo = this.testGeo.link();
		if (linfo.state != ProgramLinkInfo.Error.NONE)
		{
			System.err.printf("Failed to link program \"%s\" because of a %s.%n", "make_geo", linfo.state);
			if (linfo.log != null && !linfo.log.trim().isEmpty())
			{
				System.err.println(linfo.log);
			}
		}
	}

	private void createShader(Shader shader, Program program)
	{
		ShaderCreateInfo cinfo = shader.create();
		if (cinfo.state != ShaderCreateInfo.Error.NONE)
		{
			System.err.printf("Failed to create shader \"%s\" because of a %s.%n", shader.toString(), cinfo.state);
			if (cinfo.exeption != null)
			{
				System.err.printf("Exception:%s%n", cinfo.exeption);
			}
			if (cinfo.log != null && !cinfo.log.trim().isEmpty())
			{
				System.err.println(cinfo.log);
			}
			return;
		}
		shader.attach(program);
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
	}

	public void tickGame()
	{

	}

	public void renderGame(float partial, RenderEngine engine, DiffuseFBO target)
	{
		this.geoFBO.bind();
		this.makeGeo.bind();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(70, target.width / (float) target.height, 50, 500);
		GL11.glTranslated(-150, -100, -100);
		//GL11.glOrtho(0, target.width, 0, target.height, 50, 300);
		this.clipRange.setFloats(50, 500);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glColor3f(0.2F, 0.2F, 1.0F);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3f(100, 100, -150);
		GL11.glVertex3f(200, 100, -150);
		GL11.glVertex3f(200, 200, -150);
		GL11.glVertex3f(100, 200, -150);
		GL11.glVertex3f(200, 100, -100);
		GL11.glVertex3f(300, 100, -250);
		GL11.glVertex3f(300, 200, -250);
		GL11.glVertex3f(200, 200, -100);
		GL11.glEnd();
		GL11.glColor3f(0.0F, 0.0F, 0.0F);
		/*
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluOrtho2D(0, target.width, 0, target.height);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		this.testFont.renderSegment("This is a test!", 50.0F, 50, 50, true);
		this.testFont.renderSegment(this.testString, 32.0F, 50, 300, true);
		*/
		this.makeGeo.unbind();
		target.bind();
		this.diffuseSamp.upload(this.testGeo);
		this.depthSamp.upload(this.testGeo);
		this.normSamp.upload(this.testGeo);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.geoFBO.getDiffuse());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.geoFBO.getNormal());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.geoFBO.getDepth());
		this.clipRange.upload(this.testGeo);
		this.testGeo.bind();
		this.geoFBO.drawFull(false);
		this.testGeo.unbind();
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
