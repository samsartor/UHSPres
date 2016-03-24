package net.eekysam.uhspres;

public class Main
{
	public static void main(String[] args)
	{
		String dir = System.getProperty("user.dir");
		dir = dir.replaceAll("\\\\", "\\/");
		dir += "/";
		boolean full = false;
		int sizew = 1000;
		int sizeh = 600;
		boolean play = false;
		for (int i = 0; i < args.length; i++)
		{
			String arg = args[i];
			if (arg.startsWith("-"))
			{
				arg = arg.substring(1);
				switch (arg)
				{
					case "f":
						full = true;
						break;
					case "p":
						play = true;
						break;
					case "ssao":
						i++;
						Config.ssaoQuality = Integer.parseInt(args[i]);
						break;
					case "speed":
						i++;
						Config.presSpeed = Float.parseFloat(args[i]);
						break;
				}
			}
		}
		Presentation turrem = new Presentation(dir, full, sizew, sizeh, play);
		Thread.currentThread().setName("UHS Presentation");
		turrem.run();
	}
}
