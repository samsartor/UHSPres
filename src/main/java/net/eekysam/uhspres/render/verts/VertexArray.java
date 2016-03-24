package net.eekysam.uhspres.render.verts;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VertexArray
{
	private int array = -1;
	private boolean created = false;
	
	public void create()
	{
		if (this.created)
		{
			return;
		}
		this.array = GL30.glGenVertexArrays();
		this.created = true;
	}
	
	public void delete()
	{
		if (this.created)
		{
			GL30.glDeleteVertexArrays(this.array);
			this.created = false;
		}
	}
	
	public void bind()
	{
		if (this.created)
		{
			GL30.glBindVertexArray(this.array);
		}
	}
	
	public void unbind()
	{
		if (this.created)
		{
			GL30.glBindVertexArray(0);
		}
	}
	
	public static void enableAttrib(int index)
	{
		GL20.glEnableVertexAttribArray(index);
	}
	
	public static void disableAttrib(int index)
	{
		GL20.glDisableVertexAttribArray(index);
	}
}
