package config;

import java.io.*;
import java.util.ArrayList;

public class FindCertainExtension
{
	private FindCertainExtension(){}
	private static FindCertainExtension INSTANCE = new FindCertainExtension();
	public static FindCertainExtension getInstance()
	{	
		return INSTANCE;
	}
	
	public ArrayList<String> listFile(String folder, String ext) 
	{
		GenericExtFilter filter = new GenericExtFilter(ext);

		File dir = new File(folder);
		
		if(dir.isDirectory()==false)
		{
			System.out.println("Directory does not exists : " + folder);
			return null;
		}
		
		ArrayList<String> liste = new ArrayList<String>();
		// list out all the file name and filter by the extension
		String[] list = dir.list(filter);

		if (list.length == 0) 
		{
			System.out.println("no files end with : " + ext);
			return liste;
		}

		for (String file : list) 
		{
			String temp = new StringBuffer(folder).append(File.separator)
					.append(file).toString();
			System.out.println("file : " + temp);
			liste.add(temp);
		}
		return liste;
	}

	// inner class, generic extension filter
	public class GenericExtFilter implements FilenameFilter 
	{
		private String ext;

		public GenericExtFilter(String ext) 
		{
			this.ext = ext;
		}

		public boolean accept(File dir, String name) 
		{
			return (name.endsWith(ext));
		}
	}
}
