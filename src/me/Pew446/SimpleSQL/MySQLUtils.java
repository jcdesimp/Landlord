package me.Pew446.SimpleSQL;

public class MySQLUtils 
{
	private String hostname = "localhost";
	private int port = 0;
	private String username = "minecraft";
	private String password = "";
	private String database = "minecraft";
	private Database db;

	public MySQLUtils(Database db) 
	{
		this.db = db;
	}

	public String getHostname() 
	{
		return hostname;
	}

	public void setHostname(String hostname) 
	{
		if (hostname == null || hostname.length() == 0)
			db.printError("Hostname cannot be null or empty.");
		this.hostname = hostname;
	}

	public int getPort() 
	{
		return port;
	}

	public void setPort(int port) 
	{
		if (port < 0 || 65535 < port)
			db.printError("Port number cannot be below 0 or greater than 65535.");
		this.port = port;
	}

	public String getUsername() 
	{
		return this.username;
	}

	public void setUsername(String username) 
	{
		if (username == null || username.length() == 0)
			db.printError("Username cannot be null or empty.");
		this.username = username;
	}

	public String getPassword() 
	{
		return this.password;
	}

	public void setPassword(String password) 
	{
		if (password == null || password.length() == 0)
			db.printError("Password cannot be null or empty.");
		this.password = password;
	}

	public String getDatabase() 
	{
		return this.database;
	}

	public void setDatabase(String database) 
	{
		if (database == null || database.length() == 0)
			db.printError("Database cannot be null or empty.");
		this.database = database;
	}
}
