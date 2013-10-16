package com.centurylink.xprsr.db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;


import com.centurylink.xprsr.util.DerbyResourceReader;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DerbyDBManager {
	
	private static DerbyDBManager ref = null;
	String dbUrl ,username ,password ,dbClass;
	private static ComboPooledDataSource cpds =new ComboPooledDataSource();
	
	private DerbyDBManager()
	{
		DerbyResourceReader  reader = DerbyResourceReader.getMyResourceReaderObject();
		
		try
		{
			cpds.setDriverClass(reader.getProperties().getProperty("database.dbClass"));
			cpds.setUser(reader.getProperties().getProperty("database.username"));
			cpds.setPassword(reader.getProperties().getProperty("database.password"));
			cpds.setJdbcUrl(reader.getProperties().getProperty("database.url"));
			cpds.setAutomaticTestTable(reader.getProperties().getProperty("database.automaticTestTable"));
			cpds.setInitialPoolSize(Integer.parseInt(reader.getProperties().getProperty("database.initialPoolSize")));
			cpds.setMaxConnectionAge(Integer.parseInt(reader.getProperties().getProperty("database.maxConnectionAge")));
			cpds.setMaxIdleTime(Integer.parseInt(reader.getProperties().getProperty("database.maxIdleTime")));
			cpds.setMaxPoolSize(Integer.parseInt(reader.getProperties().getProperty("database.maxPoolSize")));
			cpds.setMinPoolSize(Integer.parseInt(reader.getProperties().getProperty("database.minPoolSize")));
			cpds.setAcquireIncrement(Integer.parseInt(reader.getProperties().getProperty("database.acquireIncrement")));
			cpds.setTestConnectionOnCheckout(Boolean.parseBoolean(reader.getProperties().getProperty("database.testConnectionOncheckout")));
			
		}
		catch(PropertyVetoException e)
		{
			e.printStackTrace();
		}

	}
	
	public static DerbyDBManager getMyDBManagerObject()
	{
		if(ref == null)
			ref = new DerbyDBManager();
		return ref;
		
	}
	
	public Connection getConnection()
	{
		Connection con = null;
		try {
			con = cpds.getConnection();
			System.out.println("Connection established to DerbyDB");
		} catch (SQLException e) {
		    System.out.println("Cannot connect to DerbyDB server");
		}
		
		
		return con;
	
	}

}
