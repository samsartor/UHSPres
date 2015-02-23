package net.eekysam.uhspres.render.shading;

import net.eekysam.uhspres.render.RenderEngine;

public abstract class ShadePass
{
	public final RenderEngine engine;
	public final Shaders shaders;
	
	public ShadePass(RenderEngine engine)
	{
		this.engine = engine;
		this.shaders = this.engine.shaders;
	}
	
	public void create()
	{
		this.doCreate();
	}
	
	public void delete()
	{
		this.doDelete();
	}
	
	public abstract void doCreate();
	
	public abstract void doDelete();
}
