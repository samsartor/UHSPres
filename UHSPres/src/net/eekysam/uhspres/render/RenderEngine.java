package net.eekysam.uhspres.render;

import org.lwjgl.util.vector.Matrix4f;

import net.eekysam.uhspres.asset.Asset;
import net.eekysam.uhspres.render.fbo.EnumDrawBufferLocs;
import net.eekysam.uhspres.render.shader.Program;
import net.eekysam.uhspres.render.shader.ProgramLinkInfo;
import net.eekysam.uhspres.render.shader.Shader;
import net.eekysam.uhspres.render.shader.Shader.ShaderType;
import net.eekysam.uhspres.render.shader.ShaderCreateInfo;
import net.eekysam.uhspres.render.shader.ShaderUniform;
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
	
	private Program blit;
	private Shader blitVert;
	private Shader blitFrag;
	private ShaderUniform blitDiffuse;
	
	private ShaderUniform mvpMatrix;
	
	public RenderEngine()
	{
		this.blit = new Program(EnumDrawBufferLocs.DIFFUSE);
		Asset blitAsset = new Asset("shaders/blit");
		this.blitVert = new Shader(ShaderType.VERTEX, blitAsset);
		this.blitFrag = new Shader(ShaderType.FRAGMENT, blitAsset);
		this.blitDiffuse = new ShaderUniform("samp_diffuse");
		
		this.mvpMatrix = new ShaderUniform("un_mvp");
	}
	
	public void create()
	{
		this.blit.create();
		this.createShader(this.blitVert);
		this.createShader(this.blitFrag);
		this.blitVert.attach(this.blit);
		this.blitFrag.attach(this.blit);
		this.linkProgram(this.blit, this.blitFrag.asset.file);
		this.mvpMatrix.setMatricies(Matrix4f.setIdentity(new Matrix4f()));
	}
	
	public boolean createShader(Shader shader)
	{
		return this.displayShaderCreateInfo(shader.toString(), shader.create());
	}
	
	public boolean linkProgram(Program program, String name)
	{
		return this.displayProgramLinkInfo(name, program.link());
	}
	
	private boolean displayShaderCreateInfo(String shaderName, ShaderCreateInfo info)
	{
		if (info.state == ShaderCreateInfo.Error.NONE)
		{
			System.out.printf("Successfully created shader (%s).%n", shaderName);
			return true;
		}
		else
		{
			System.err.printf("Failed to create shader (%s). Reason: %s%n", shaderName, info.state.name());
			if (info.log != null && !info.log.isEmpty())
			{
				System.err.println(info.log);
			}
			else if (info.exeption != null)
			{
				info.exeption.printStackTrace(System.err);
			}
			return false;
		}
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
	
	public void bindBlit(int texture)
	{
		this.blit.bind();
		this.blitDiffuse.setInt(texture);
		this.blitDiffuse.upload(this.blit);
	}
	
	public void unbindBlit()
	{
		this.blit.unbind();
	}
	
	public void uploadMVPMatrix(Program program)
	{
		this.mvpMatrix.upload(program);
	}
}
