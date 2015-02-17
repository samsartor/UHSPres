package net.eekysam.uhspres.game;

public class Timer
{
	public float ticksPerSecond = 20.0F;
	
	public long lastUpdateTime;
	
	public float partialTicks;
	public int elapsedTicks;
	
	public Timer()
	{
		
	}
	
	public void resetTimer()
	{
		this.lastUpdateTime = System.currentTimeMillis();
		this.partialTicks = 0.0F;
		this.elapsedTicks = 0;
	}
	
	public void update()
	{
		long time = System.currentTimeMillis();
		long diff = time - this.lastUpdateTime;
		if (diff < 0 || diff > 2000)
		{
			this.elapsedTicks = 0;
			return;
		}
		this.lastUpdateTime = time;
		
		this.partialTicks += (diff / 1000.0F) * this.ticksPerSecond;
		this.elapsedTicks = (int) this.partialTicks;
		this.partialTicks -= this.elapsedTicks;
	}
}
