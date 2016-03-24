package net.eekysam.uhspres.utils;

public abstract class ThreadedLoop
{
	private static class Loop extends Thread
	{
		private ThreadedLoop info;
		
		public Loop(ThreadedLoop info)
		{
			this.info = info;
		}
		
		@Override
		public void run()
		{
			this.info.first();
			while (this.info.running)
			{
				this.info.run();
			}
			this.info.last();
		}
	}
	
	private volatile boolean running = false;
	private Thread loop;
	
	public ThreadedLoop()
	{
		this.loop = new Loop(this);
	}
	
	public final void start()
	{
		this.running = true;
		if (!this.loop.isAlive())
		{
			this.loop.start();
		}
	}
	
	public final void end()
	{
		this.running = false;
		try
		{
			this.loop.join();
		}
		catch (InterruptedException e)
		{
			
		}
	}
	
	protected abstract void first();
	
	protected abstract void run();
	
	protected abstract void last();
}
