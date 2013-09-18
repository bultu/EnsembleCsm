package com.centurylink.xprsr.db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;


import com.centurylink.xprsr.util.MyResourceReader;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBManager
{
	private static DBManager man = null;
	private static ComboPooledDataSource cpds = new ComboPooledDataSource();
	private DBManager(String requiredDatabase)
	{
	    MyResourceReader  reader = MyResourceReader.getInstance(requiredDatabase);
        cpds.setUser(reader.getProperties().getProperty("database.username"));
        cpds.setPassword(reader.getProperties().getProperty("database.password"));
        cpds.setJdbcUrl(reader.getProperties().getProperty("database.url"));
	}
	
	public static DBManager getInstance(String requiredDatabase)
	{
		if(man == null)
		{
			man = new DBManager(requiredDatabase);
			return man;
		}
		
		else
			return man;
	}
	
	public Connection getConnection(String requiredDatabase) throws SQLException
	{
		/*cpds.setJdbcUrl(MyResourceReader.getInstance().getProperties().getProperty("database.url"));
		cpds.setUser(MyResourceReader.getInstance().getProperties().getProperty("database.username"));
		cpds.setPassword(MyResourceReader.getInstance().getProperties().getProperty("database.password"));*/
		
		try
		{
			cpds.setDriverClass(MyResourceReader.getInstance(requiredDatabase).getProperties().getProperty("database.dbClass"));
		} 
		catch (PropertyVetoException e)
		{
			e.printStackTrace();
		}
		
		Connection con = cpds.getConnection();
		return con;
	}
}
