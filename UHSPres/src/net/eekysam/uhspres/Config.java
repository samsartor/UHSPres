package net.eekysam.uhspres;

import org.lwjgl.opengl.Display;

public class Config
{
	public static final int entityRenderDistance = 96;
	public static final int chunkRenderRadius = 8;
	public static final int chunkRenderRadiusPrecise = 120;
	public static final float mouseSpeedX = 0.5F;
	public static final float mouseSpeedY = 0.5F;
	public static final float scrollSpeed = 0.012F;
	public static final float camDistMin = 8.0F;
	public static final float camDistMax = 120.0F;
	public static final boolean doAO = true;
	public static final long chunkRequestTimeLimit = 100;
	public static final int chunkRequestDistance = 8;
	public static final boolean shouldRequestChunks = false;
	
	public static int getHeight()
	{
		return Display.getDisplayMode().getHeight();
	}
	
	public static int getWidth()
	{
		return Display.getDisplayMode().getWidth();
	}
}
