package net.eekysam.uhspres.render.shader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.eekysam.uhspres.asset.Asset;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader
{
	public static enum ShaderType
	{
		FRAGMENT("fsh", GL20.GL_FRAGMENT_SHADER),
		VERTEX("vsh", GL20.GL_VERTEX_SHADER);

		public final String extension;
		public final int glMode;

		ShaderType(String extension, int glMode)
		{
			this.extension = extension;
			this.glMode = glMode;
		}
	}

	public final ShaderType type;
	public final Asset asset;
	private boolean created = false;
	private boolean isMarkedForDelete = false;
	private int shader = -1;

	public Shader(ShaderType type, Asset asset)
	{
		this.type = type;
		this.asset = asset;
	}

	public String loadSource() throws IOException
	{
		InputStream in = this.asset.getAsset(this.type.extension).getInput();
		if (in == null)
		{
			throw new FileNotFoundException("Could not find shader source: " + this.asset);
		}
		return IOUtils.toString(in);
	}

	public ShaderCreateInfo create()
	{
		this.updateDeleteStatus();
		if (this.created)
		{
			return new ShaderCreateInfo(ShaderCreateInfo.Error.ALREADY_CREATED, null, null);
		}
		String source;
		try
		{
			source = this.loadSource();
		}
		catch (FileNotFoundException e)
		{
			return new ShaderCreateInfo(ShaderCreateInfo.Error.SOURCE_MISSING, null, e);
		}
		catch (IOException e)
		{
			return new ShaderCreateInfo(ShaderCreateInfo.Error.IO_EXCEPTION, null, e);
		}
		this.shader = GL20.glCreateShader(this.type.glMode);
		this.created = true;
		GL20.glShaderSource(this.shader, source);
		if (GL20.glGetShaderi(this.shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
		{
			int loglength = GL20.glGetShaderi(this.shader, GL20.GL_INFO_LOG_LENGTH);
			String log = GL20.glGetShaderInfoLog(this.shader, loglength);
			this.delete();
			return new ShaderCreateInfo(ShaderCreateInfo.Error.COMPILE_ERROR, log, null);
		}
		return new ShaderCreateInfo(ShaderCreateInfo.Error.NONE, null, null);
	}

	public ShaderAttachInfo attach(Program program)
	{
		this.updateDeleteStatus();
		if (!this.created)
		{
			return new ShaderAttachInfo(ShaderAttachInfo.Error.SHADER_NOT_CREATED, null);
		}
		int pid = program.getProgram();
		if (pid <= 0)
		{
			return new ShaderAttachInfo(ShaderAttachInfo.Error.PROGRAM_NOT_CREATED, null);
		}
		GL20.glAttachShader(pid, this.shader);
		if (GL20.glGetProgrami(pid, GL20.GL_LINK_STATUS) == GL11.GL_FALSE)
		{
			int loglength = GL20.glGetProgrami(pid, GL20.GL_INFO_LOG_LENGTH);
			String log = GL20.glGetProgramInfoLog(pid, loglength);
			return new ShaderAttachInfo(ShaderAttachInfo.Error.LINK_ERROR, log);
		}
		return new ShaderAttachInfo(ShaderAttachInfo.Error.NONE, null);
	}

	public void detach(Program program)
	{
		this.updateDeleteStatus();
		if (!this.created)
		{
			return;
		}
		int pid = program.getProgram();
		if (pid <= 0)
		{
			return;
		}
		GL20.glDetachShader(pid, this.shader);
	}

	public void delete()
	{
		if (this.created)
		{
			this.isMarkedForDelete = true;
			GL20.glDeleteShader(this.shader);
			this.updateDeleteStatus();
		}
	}

	private void updateDeleteStatus()
	{
		if (this.isMarkedForDelete)
		{
			if (!GL20.glIsShader(this.shader))
			{
				this.isMarkedForDelete = false;
				this.shader = -1;
				this.created = false;
			}
		}
	}

	public int getShader()
	{
		this.updateDeleteStatus();
		if (this.created)
		{
			return this.shader;
		}
		return -1;
	}
}
