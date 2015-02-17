package net.eekysam.uhspres;

public class Main
{
	public static void main(String[] args)
	{
		String dir = System.getProperty("user.dir");
		dir = dir.replaceAll("\\\\", "\\/");
		dir += "/";
		Presentation turrem = new Presentation(dir);
		Thread.currentThread().setName("UHS Presentation");
		turrem.run();
	}
}
