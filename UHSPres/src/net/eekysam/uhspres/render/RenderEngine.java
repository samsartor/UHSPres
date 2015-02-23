package net.eekysam.uhspres.render;

import java.util.Random;

import net.eekysam.uhspres.render.fbo.EnumDrawBufferLocs;
import net.eekysam.uhspres.render.lights.PointLight;
import net.eekysam.uhspres.render.shader.Program;
import net.eekysam.uhspres.render.shader.ProgramLinkInfo;
import net.eekysam.uhspres.render.shading.ShadeGeometry;
import net.eekysam.uhspres.render.shading.ShadeSSAO;
import net.eekysam.uhspres.render.shading.Shaders;
import net.eekysam.uhspres.render.verts.VertexBuffer;
import net.eekysam.uhspres.utils.graphics.GLUtils;

public class RenderEngine
{
	public static final int[] quadIndicies = new int[] { 0, 1, 3, 1, 2, 3 };
	
	public static void buffer2DQuad(VertexBuffer positionBuf, VertexBuffer indexBuf, float x, float y, float z, float w, float h)
	{
		float[] verts = new float[12];
		
		for (int i = 0; i < 12; i += 3)
		{
			verts[i] = x;
			verts[i + 1] = y;
			verts[i + 2] = z;
		}
		verts[1] += h;
		verts[4] += h;
		verts[3] += w;
		verts[6] += w;
		
		positionBuf.vertexData(GLUtils.bufferFloats(verts));
		indexBuf.vertexData(GLUtils.bufferInts(RenderEngine.quadIndicies));
	}
	
	public Program blit;
	public Program mulblit;
	public Program lumblit;
	public Program vblur;
	public Program light;
	
	public final Shaders shaders;
	
	public final ShadeGeometry geometryPass;
	public final ShadeSSAO ssaoPass;
	
	public RenderEngine()
	{
		this.shaders = new Shaders();
		
		this.blit = new Program(EnumDrawBufferLocs.DIFFUSE);
		this.mulblit = new Program(EnumDrawBufferLocs.DIFFUSE);
		this.lumblit = new Program(EnumDrawBufferLocs.DIFFUSE);
		this.vblur = new Program(EnumDrawBufferLocs.VALUE);
		this.light = new Program(EnumDrawBufferLocs.DIFFUSE);
		
		this.geometryPass = new ShadeGeometry(this);
		this.ssaoPass = new ShadeSSAO(this, this.vblur, 4, 128, new Random());
	}
	
	public void create()
	{
		this.shaders.create();
		
		this.blit.create();
		this.shaders.blitV.attach(this.blit);
		this.shaders.blitF.attach(this.blit);
		this.linkProgram(this.blit, this.shaders.blitA.file);
		
		this.mulblit.create();
		this.shaders.blitV.attach(this.mulblit);
		this.shaders.mulblitF.attach(this.mulblit);
		this.linkProgram(this.mulblit, this.shaders.mulblitA.file);
		
		this.lumblit.create();
		this.shaders.blitV.attach(this.lumblit);
		this.shaders.lumblitF.attach(this.lumblit);
		this.linkProgram(this.lumblit, this.shaders.lumblitA.file);
		
		this.vblur.create();
		this.shaders.blitV.attach(this.vblur);
		this.shaders.vblurF.attach(this.vblur);
		this.linkProgram(this.vblur, this.shaders.vblurA.file);
		
		this.light.create();
		this.shaders.lightV.attach(this.light);
		this.shaders.lightF.attach(this.light);
		this.linkProgram(this.light, this.shaders.lightA.file);
		
		this.geometryPass.create();
		this.ssaoPass.create();
		
		PointLight.createIcos();
	}
	
	public boolean linkProgram(Program program, String name)
	{
		return this.displayProgramLinkInfo(name, program.link());
	}
	
	private boolean displayProgramLinkInfo(String programName, ProgramLinkInfo info)
	{
		if (info.state == ProgramLinkInfo.Error.NONE)
		{
			System.out.printf("Successfully linked program (%s).%n", programName);
			return true;
		}
		else
		{
			System.err.printf("Failed to link program (%s). Reason: %s%n", programName, info.state.name());
			if (info.log != null && !info.log.isEmpty())
			{
				System.err.println(info.log);
			}
			return false;
		}
	}
}
