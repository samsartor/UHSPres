package net.eekysam.uhspres.asset;

public class Asset
{
	public final String file;

	public Asset(String file)
	{
		this.file = file;
	}

	public GameAsset getAsset(String extension)
	{
		return new GameAsset(this.file + "." + extension);
	}
}
