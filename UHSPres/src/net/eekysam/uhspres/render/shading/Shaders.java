package net.eekysam.uhspres.render.shading;

import net.eekysam.uhspres.asset.Asset;
import net.eekysam.uhspres.render.shader.Shader;
import net.eekysam.uhspres.render.shader.Shader.ShaderType;
import net.eekysam.uhspres.render.shader.ShaderCreateInfo;

public class Shaders
{
	public final Asset blitA;
	public final Shader blitV;
	public final Shader blitF;
	public final Asset mulblitA;
	public final Shader mulblitF;
	public final Asset geoA;
	public final Shader geoV;
	public final Shader geoF;
	public final Asset blurA;
	public final Shader blurF;
	public final Asset vblurA;
	public final Shader vblurF;
	public final Asset ssaoA;
	public final Shader ssaoF;
	public final Asset posA;
	public final Shader posV;
	public final Asset depthA;
	public final Shader depthF;
	public final Asset shadowA;
	public final Shader shadowF;
	
	public Shaders()
	{
		this.blitA = new Asset("shaders/blit");
		this.mulblitA = new Asset("shaders/mulblit");
		this.geoA = new Asset("shaders/geo");
		this.blurA = new Asset("shaders/blur");
		this.vblurA = new Asset("shaders/vblur");
		this.ssaoA = new Asset("shaders/ssao");
		this.posA = new Asset("shaders/pos");
		this.depthA = new Asset("shaders/depth");
		this.shadowA = new Asset("shaders/shadow");
		
		this.blitV = new Shader(ShaderType.VERTEX, this.blitA);
		this.blitF = new Shader(ShaderType.FRAGMENT, this.blitA);
		this.mulblitF = new Shader(ShaderType.FRAGMENT, this.mulblitA);
		this.geoV = new Shader(ShaderType.VERTEX, this.geoA);
		this.geoF = new Shader(ShaderType.FRAGMENT, this.geoA);
		this.blurF = new Shader(ShaderType.FRAGMENT, this.blurA);
		this.vblurF = new Shader(ShaderType.FRAGMENT, this.vblurA);
		this.ssaoF = new Shader(ShaderType.FRAGMENT, this.ssaoA);
		this.posV = new Shader(ShaderType.VERTEX, this.posA);
		this.depthF = new Shader(ShaderType.FRAGMENT, this.depthA);
		this.shadowF = new Shader(ShaderType.FRAGMENT, this.shadowA);
	}
	
	public void create()
	{
		this.createShader(this.blitF);
		this.createShader(this.blitV);
		this.createShader(this.mulblitF);
		this.createShader(this.blurF);
		this.createShader(this.vblurF);
		this.createShader(this.depthF);
		this.createShader(this.geoF);
		this.createShader(this.geoV);
		this.createShader(this.posV);
		this.createShader(this.shadowF);
		this.createShader(this.ssaoF);
	}
	
	public boolean createShader(Shader shader)
	{
		return this.displayShaderCreateInfo(shader.toString(), shader.create());
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
	
}
