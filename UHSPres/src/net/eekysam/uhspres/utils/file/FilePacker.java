package net.eekysam.uhspres.utils.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FilePacker
{
	public static void cloneData(InputStream in, OutputStream out) throws IOException
	{
		byte[] bytes = new byte[1024];
		int length;
		while ((length = in.read(bytes)) >= 0)
		{
			out.write(bytes, 0, length);
		}
	}
	
	public static boolean extractSingleZip(ZipInputStream in, File outdir) throws IOException
	{
		ZipEntry ent = in.getNextEntry();
		if (ent == null)
		{
			return false;
		}
		
		File outf = new File(outdir.getAbsolutePath() + "/" + ent.getName());
		outf.getParentFile().mkdirs();
		outf.createNewFile();
		FileOutputStream out = new FileOutputStream(outf);
		
		cloneData(in, out);
		out.close();
		in.closeEntry();
		
		return true;
	}
	
	public static int extractZip(File zip, File outdir) throws IOException
	{
		ZipInputStream in = new ZipInputStream(new FileInputStream(zip));
		int i = extractZip(in, outdir);
		in.close();
		return i;
	}
	
	public static int extractZip(ZipInputStream in, File outdir) throws IOException
	{
		int i = 0;
		while (extractSingleZip(in, outdir))
		{
			i++;
		}
		return i;
	}
	
	public static void addToZip(InputStream in, String name, ZipOutputStream out) throws IOException
	{
		ZipEntry entry = new ZipEntry(name);
		out.putNextEntry(entry);
		cloneData(in, out);
		out.closeEntry();
	}
	
	public static void addFileToZip(File file, String name, ZipOutputStream out) throws IOException
	{
		FileInputStream in = new FileInputStream(file);
		addToZip(in, name, out);
		in.close();
	}
	
	public static void addFileToZip(File file, ZipOutputStream out) throws IOException
	{
		addFileToZip(file, file.getName(), out);
	}
	
	public static void addDirectoryToZip(File dir, ZipOutputStream out) throws IOException
	{
		addFilesToZip(dir, dir.getName(), out, null);
	}
	
	public static void addDirectoryToZip(File dir, ZipOutputStream out, FileFilter filter) throws IOException
	{
		addFilesToZip(dir, dir.getName(), out, filter);
	}
	
	public static void addFilesToZip(File dir, ZipOutputStream out) throws IOException
	{
		addFilesToZip(dir, "", out, null);
	}
	
	public static void addFilesToZip(File dir, ZipOutputStream out, FileFilter filter) throws IOException
	{
		addFilesToZip(dir, "", out, filter);
	}
	
	protected static void addFilesToZip(File dir, String name, ZipOutputStream out, FileFilter filter) throws IOException
	{
		if (dir.isDirectory())
		{
			if (!name.isEmpty())
			{
				name += "/";
			}
			File[] subs = dir.listFiles(filter);
			for (File sub : subs)
			{
				addFilesToZip(sub, name + sub.getName(), out, filter);
			}
		}
		else
		{
			addFileToZip(dir, name, out);
		}
	}
}
