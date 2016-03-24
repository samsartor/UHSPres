package net.eekysam.uhspres.render.shader;

public class ShaderCreateInfo
{
	public static enum Error
	{
		NONE,
		ALREADY_CREATED,
		SOURCE_MISSING,
		IO_EXCEPTION,
		COMPILE_ERROR;
	}
	
	public ShaderCreateInfo(Error state, String log, Exception ex)
	{
		this.state = state;
		this.log = log;
		this.exeption = ex;
	}
	
	public final Error state;
	public final String log;
	public final Exception exeption;
}
