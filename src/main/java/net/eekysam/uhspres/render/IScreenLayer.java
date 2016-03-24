package net.eekysam.uhspres.render;

import net.eekysam.uhspres.render.fbo.DiffuseFBO;

public interface IScreenLayer
{
	public void render(RenderEngine engine, DiffuseFBO target);
	
	public boolean isOutdated();
	
	public boolean hasContent();
	
	public boolean pauseRequested();
}
