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
	public final Asset lumblitA;
	public final Shader lumblitF;
	public final Asset geoA;
	public final Shader geoV;
	public final Shader geoF;
	public final Asset vblurA;
	public final Shader vblurF;
	public final Asset ssaoA;
	public final Shader ssaoF;
	public final Asset lightA;
	public final Shader lightF;
	public final Asset vignetteA;
	public final Shader vignetteF;
	
	public Shaders()
	{
		this.blitA = new Asset("shaders/blit");
		this.lumblitA = new Asset("shaders/lumblit");
		this.geoA = new Asset("shaders/geo");
		this.vblurA = new Asset("shaders/vblur");
		this.ssaoA = new Asset("shaders/ssao");
		this.lightA = new Asset("shaders/light");
		this.vignetteA = new Asset("shaders/vignette");
		
		this.blitV = new Shader(ShaderType.VERTEX, this.blitA);
		this.blitF = new Shader(ShaderType.FRAGMENT, this.blitA);
		this.lumblitF = new Shader(ShaderType.FRAGMENT, this.lumblitA);
		this.geoV = new Shader(ShaderType.VERTEX, this.geoA);
		this.geoF = new Shader(ShaderType.FRAGMENT, this.geoA);
		this.vblurF = new Shader(ShaderType.FRAGMENT, this.vblurA);
		this.ssaoF = new Shader(ShaderType.FRAGMENT, this.ssaoA);
		this.lightF = new Shader(ShaderType.FRAGMENT, this.lightA);
		this.vignetteF = new Shader(ShaderType.FRAGMENT, this.vignetteA);
	}
	
	public void create()
	{
		this.createShader(this.blitF);
		this.createShader(this.blitV);
		this.createShader(this.lumblitF);
		this.createShader(this.vblurF);
		this.createShader(this.geoF);
		this.createShader(this.geoV);
		this.createShader(this.ssaoF);
		this.createShader(this.lightF);
		this.createShader(this.vignetteF);
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
