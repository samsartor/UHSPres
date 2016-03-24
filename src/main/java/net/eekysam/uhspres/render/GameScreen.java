package net.eekysam.uhspres.render;

import java.util.EnumMap;

import net.eekysam.uhspres.Presentation;
import net.eekysam.uhspres.game.RenderGame;
import net.eekysam.uhspres.render.fbo.DiffuseFBO;
import net.eekysam.uhspres.render.shader.ShaderUniform;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;

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
		this.theGame = new RenderGame(this.engine);
		this.layers.put(EnumScreenLayers.GAME_LAYER, this.theGame);
	}
	
	public IScreenLayer getLayer(EnumScreenLayers layer)
	{
		return this.layers.get(layer);
	}
	
	public void render()
	{
		ShaderUniform un = new ShaderUniform();
		
		GL11.glClearColor(33 / 255.0F, 43 / 255.0F, 63 / 255.0F, 1.0F);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		for (IScreenLayer layer : this.layers.values())
		{
			if (layer != null)
			{
				this.target.bind();
				GL11.glClearColor(0, 0, 0, 0);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				layer.render(this.engine, this.target);
				this.target.unbind();
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.target.getDiffuse());
				this.engine.blit.bind();
				un.setInt(0);
				un.upload(this.engine.blit, "samp_diffuse");
				GL11.glEnable(GL11.GL_BLEND);
				GL20.glBlendEquationSeparate(GL14.GL_FUNC_ADD, GL14.GL_FUNC_ADD);
				GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
				this.target.drawQuad();
				GL11.glDisable(GL11.GL_BLEND);
				this.engine.blit.unbind();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			}
		}
	}
}
