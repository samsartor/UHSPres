package net.eekysam.uhspres.render.shader;

import net.eekysam.uhspres.render.fbo.EnumDrawBufferLocs;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Program
{
	private int program = -1;
	private boolean created = false;
	private EnumDrawBufferLocs[] outputs;

	public Program(EnumDrawBufferLocs... outputs)
	{
		this.outputs = outputs;
	}

	public void create()
	{
		if (!this.created)
		{
			this.program = GL20.glCreateProgram();
			for (EnumDrawBufferLocs loc : this.outputs)
			{
				GL30.glBindFragDataLocation(this.program, loc.location, loc.outputName);
			}
			this.created = true;
		}
	}

	public ProgramLinkInfo link()
	{
		if (!this.created)
		{
			return new ProgramLinkInfo(ProgramLinkInfo.Error.PROGRAM_NOT_CREATED, null);
		}
		GL20.glLinkProgram(this.program);
		if (GL20.glGetProgrami(this.program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE)
		{
			int loglength = GL20.glGetProgrami(this.program, GL20.GL_INFO_LOG_LENGTH);
			String log = GL20.glGetProgramInfoLog(this.program, loglength);
			return new ProgramLinkInfo(ProgramLinkInfo.Error.LINK_ERROR, log);
		}
		return new ProgramLinkInfo(ProgramLinkInfo.Error.NONE, null);
	}

	public void delete()
	{
		if (this.created)
		{
			GL20.glDeleteProgram(this.program);
			this.created = false;
		}
	}

	public void bind()
	{
		if (this.created)
		{
			GL20.glUseProgram(this.program);
		}
	}

	public void unbind()
	{
		GL20.glUseProgram(0);
	}

	public int getProgram()
	{
		if (this.created)
		{
			return this.program;
		}
		return -1;
	}
}
