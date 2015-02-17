package net.eekysam.uhspres.asset;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class AssetLoader
{
	private static AssetLoader instance;

	public final File assetDir;

	public AssetLoader(File gameDir)
	{
		if (instance != null)
		{
			throw new IllegalStateException("An AssetLoader has already been created for this context!");
		}
		this.assetDir = gameDir;
		GameAsset.loader = this;
		instance = this;
	}

	public static AssetLoader instance()
	{
		return instance;
	}

	public BufferedImage readImage(GameAsset asset) throws IOException
	{
		return ImageIO.read(this.getInput(asset));
	}

	public OutputStream getOutput(GameAsset asset)
	{
		return this.getOutput(asset, true);
	}

	public OutputStream getOutput(GameAsset asset, boolean createIfMissing)
	{
		return this.getOutput(asset, createIfMissing, false);
	}

	public OutputStream getOutput(GameAsset asset, boolean createIfMissing, boolean append)
	{
		try
		{
			File f = asset.getFile();
			if (!f.exists() && createIfMissing)
			{
				f.createNewFile();
			}
			return new FileOutputStream(f, append);
		}
		catch (IOException e)
		{
			return null;
		}
	}

	public InputStream getInput(GameAsset asset)
	{
		try
		{
			return new FileInputStream(asset.getFile());
		}
		catch (FileNotFoundException e)
		{
			return null;
		}
	}
}
