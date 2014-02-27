/**
@author Mitch Talmadge (AKA Pew446)

Date Created:
	May 12, 2013
*/

package me.Pew446.SimpleSQL;

import java.io.File;

public class SQLiteUtils 
{
	private String directory;
	private String filename;
	private File file;
	private String extension = ".db";
	private Database db;

	public SQLiteUtils(Database db) 
	{
		this.db = db;		
	}

	public String getDirectory() 
	{
		return directory;
	}

	public void setDirectory(String directory) 
	{
		if (directory == null || directory.length() == 0)
			db.printError("Directory cannot be null or empty.");
		else
			this.directory = directory;
	}

	public String getFilename() 
	{
		return filename;
	}

	public void setFilename(String filename) 
	{
		if (filename == null || filename.length() == 0)
			db.printError("Filename cannot be null or empty.");
		else if (filename.contains("/") || filename.contains("\\") || filename.endsWith(".db"))
			db.printError("The database filename cannot contain: /, \\, or .db.");
		else
			this.filename = filename;
	}

	public String getExtension() 
	{
		return extension;
	}

	public void setExtension(String extension) 
	{
		if (extension == null || extension.length() == 0)
			db.printError("Extension cannot be null or empty.");
		if (extension.charAt(0) != '.')
			db.printError("Extension must begin with a period");
	}

	public File getFile() 
	{
		return this.file;
	}

	public void setFile(String directory, String filename) 
	{
		setDirectory(directory);
		setFilename(filename);

		File folder = new File(getDirectory());
		if (!folder.exists())
			folder.mkdir();

		file = new File(folder.getAbsolutePath() + File.separator + getFilename() + getExtension());
	}

	public void setFile(String directory, String filename, String extension) 
	{
		setExtension(extension);
		this.setFile(directory, filename);
	}
}
