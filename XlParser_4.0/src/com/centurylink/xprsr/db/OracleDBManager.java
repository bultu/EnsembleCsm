package com.centurylink.xprsr.db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;



import com.centurylink.xprsr.util.OracleResourceReader;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class OracleDBManager {
	
	private static OracleDBManager ref = null;
	String dbUrl ,username ,password ,dbClass;
	private static ComboPooledDataSource cpds =new ComboPooledDataSource();
	
	private OracleDBManager()
	{
		OracleResourceReader  reader = OracleResourceReader.getMyResourceReaderObject();
		
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
	
	public static OracleDBManager getMyDBManagerObject()
	{
		if(ref == null)
			ref = new OracleDBManager();
		return ref;
		
	}
	
	public Connection getConnection()
	{
		Connection con =null;
		try {
			con = cpds.getConnection();
			System.out.println("Connection established to OracleDB");
		} catch (SQLException e) {
		    System.out.println("Cannot connect to OracleDB server");
		}
		
		
		return con;
	
	}

}
