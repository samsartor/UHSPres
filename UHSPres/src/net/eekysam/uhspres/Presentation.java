package net.eekysam.uhspres;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import net.eekysam.uhspres.asset.AssetLoader;
import net.eekysam.uhspres.asset.GameAsset;
import net.eekysam.uhspres.render.GameScreen;
import net.eekysam.uhspres.render.RenderEngine;
import net.eekysam.uhspres.utils.graphics.ImgUtils;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Presentation
{
	private static Presentation instance;
	
	private int width;
	private int height;
	private boolean full;
	
	public final File theGameDir;
	public final AssetLoader theAssetLoader;
	
	public RenderEngine engine;
	
	public static final boolean play = true;
	
	private boolean close = false;
	
	public Presentation(String dir, boolean full, int width, int height, int ssaoQuality)
	{
		this.theGameDir = new File(dir);
		
		this.theAssetLoader = new AssetLoader(new File(this.theGameDir, "assets/"));
		
		instance = this;
		
		this.width = width;
		this.height = height;
		this.full = full;
		
		Config.ssaoQuality = ssaoQuality;
	}
	
	public static Presentation instance()
	{
		return instance;
	}
	
	public static int width()
	{
		return instance.width;
	}
	
	public static int height()
	{
		return instance.height;
	}
	
	public static float aspect()
	{
		return width() / (float) height();
	}
	
	protected void run()
	{
		Keyboard.enableRepeatEvents(false);
		
		try
		{
			this.setIcons();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			DisplayMode mode;
			if (this.full)
			{
				mode = Display.getDesktopDisplayMode();
			}
			else
			{
				mode = new DisplayMode(Presentation.width(), Presentation.height());
			}
			Display.setDisplayMode(mode);
			Display.setFullscreen(Presentation.instance().full);
			Display.create();
			this.width = Display.getWidth();
			this.height = Display.getHeight();
			Mouse.create();
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
		
		System.out.printf("OpenGL version is (%s)%n", GL11.glGetString(GL11.GL_VERSION));
		System.out.printf("OpenGL renderer is (%s)%n", GL11.glGetString(GL11.GL_RENDERER));
		System.out.printf("GLSL version is (%s)%n", GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
		
		this.engine = new RenderEngine();
		this.engine.create();
		
		GameScreen screen = new GameScreen(this.engine);
		
		while (!this.closeRequested())
		{
			screen.render();
			Display.update();
			Display.sync(60);
		}
		
		if (screen.theGame.world.outPath != null)
		{
			try
			{
				screen.theGame.world.outPath.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		Display.destroy();
	}
	
	public boolean closeRequested()
	{
		return Display.isCloseRequested() || this.close;
	}
	
	public void close()
	{
		this.close = true;
	}
	
	public void onCrash(Exception e)
	{
		e.printStackTrace();
		this.shutdown();
	}
	
	public void shutdown()
	{
		Display.destroy();
		System.exit(0);
	}
	
	public void setIcons() throws IOException
	{
		ArrayList<ByteBuffer> icos = new ArrayList<ByteBuffer>();
		
		GameAsset appicons = new GameAsset("icons/");
		
		File[] filelist = appicons.getFile().listFiles();
		
		for (File icon : filelist)
		{
			BufferedImage img = ImageIO.read(icon);
			icos.add(ImgUtils.imageToBufferDefault(img));
		}
		
		Display.setIcon(icos.toArray(new ByteBuffer[0]));
	}
}
