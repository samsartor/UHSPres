package net.eekysam.uhspres.render;

import java.util.EnumMap;

import net.eekysam.uhspres.Presentation;
import net.eekysam.uhspres.game.RenderGame;
import net.eekysam.uhspres.render.fbo.DiffuseFBO;

import org.lwjgl.opengl.GL11;

public class GameScreen
{
	public static enum EnumScreenLayers
	{
		GAME_LAYER,
		BASE_UI_LAYER,
		ALERT_UI_LAYER,
		FRONT_UI_LAYER,
		FULL_UI_LAYER;
	}
	
	private EnumMap<EnumScreenLayers, IScreenLayer> layers;
	public final RenderEngine engine;
	
	public RenderGame theGame;
	
	private DiffuseFBO target;
	
	public GameScreen(RenderEngine engine)
	{
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		this.engine = engine;
		this.layers = new EnumMap<>(EnumScreenLayers.class);
		this.target = new DiffuseFBO(Presentation.width(), Presentation.height());
		this.target.create();
		this.theGame = new RenderGame();
		this.layers.put(EnumScreenLayers.GAME_LAYER, this.theGame);
	}
	
	public IScreenLayer getLayer(EnumScreenLayers layer)
	{
		return this.layers.get(layer);
	}
	
	public void render()
	{
		GL11.glClearColor(0, 0, 0, 0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		for (IScreenLayer layer : this.layers.values())
		{
			if (layer != null)
			{
				this.target.bind();
				GL11.glClearColor(1, 1, 1, 1);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				layer.render(this.engine, this.target);
				this.target.unbind();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.target.getDiffuse());
				this.target.drawFull(false);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			}
		}
	}
}
