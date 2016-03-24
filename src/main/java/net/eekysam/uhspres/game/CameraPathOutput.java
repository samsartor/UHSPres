package net.eekysam.uhspres.game;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class CameraPathOutput implements Closeable
{
	private OutputStreamWriter writer;
	private float time;
	
	public CameraPathOutput(OutputStream out, float startTime)
	{
		this.writer = new OutputStreamWriter(out);
		this.time = startTime;
	}
	
	public void addPoint(boolean stop, float x, float y, float z, float yaw, float pitch, float dtime)
	{
		int mins = (int) (this.time / 60);
		float secs = this.time - mins * 60;
		try
		{
			this.writer.write(String.format("%s %.2f %.2f %.2f %.4f %.4f %d:%.1f%n", stop ? "@" : "*", x, y, z, yaw, pitch, mins, secs));
		}
		catch (IOException e)
		{
		}
		this.time += dtime;
	}
	
	@Override
	public void close() throws IOException
	{
		this.writer.close();
	}
}
