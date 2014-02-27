/**
@author Mitch Talmadge (AKA Pew446)

Date Created:
	May 12, 2013
*/

package me.Pew446.SimpleSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public abstract class Database {
	
	protected Logger logger;
	protected Connection connection;
	protected DBList driver;
	protected String prefix;
	protected String dbprefix;
	private int lastUpdate;
	
	/**
	 * Used for child class super
	 * @param prefix
	 * @param dbprefix
	 * @param logger
	 */
	public Database(String prefix, String dbprefix, Logger logger)
	{
		if(logger == null)
		{
			Logger.getLogger("SimpleSQL").severe("logger cannot be null!");
			return;
		}
		if(prefix == null)
		{
			Logger.getLogger("SimpleSQL").severe("prefix cannot be null!");
			return;
		}
		this.prefix = prefix;
		this.dbprefix = dbprefix;
		this.logger = logger;
	}


	public abstract boolean open();
	
	public final boolean close() {
		if (connection != null) {
			try {
				connection.close();
				return true;
			} catch (SQLException e) {
				this.printError("Could not close connection, SQLException: " + e.getMessage());
				return false;
			}
		} else {
			this.printError("Could not close connection, it is null.");
			return false;
		}
	}
	
	public final Connection getConnection() {
		return this.connection;
	}
	
	public final boolean isOpen() {
		if (connection != null)
			try {
				if (connection.isValid(1))
					return true;
			} catch (SQLException e) {}
		return false;
	}

	public final boolean isOpen(int seconds) {
		if (connection != null)
			try {
				if (connection.isValid(seconds))
					return true;
			} catch (SQLException e) {}
		return false;
	}
	
	protected void printError(String error)
	{
		logger.severe(this.prefix+" "+this.dbprefix+" "+error);
	}

	public abstract StatementsList getStatement(String query) throws SQLException;

	protected abstract void queryValidation(StatementsList statement) throws SQLException;
	
	public final ResultSet query(String query) throws SQLException {
		queryValidation(this.getStatement(query));
		Statement statement = this.getConnection().createStatement();
	    if (statement.execute(query)) {
	    	return statement.getResultSet();
	    } else {
	    	int uc = statement.getUpdateCount();
	    	this.lastUpdate = uc;
	    	return this.getConnection().createStatement().executeQuery("SELECT " + uc);
	    }
	}
}
