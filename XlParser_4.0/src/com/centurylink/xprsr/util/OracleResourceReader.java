package com.centurylink.xprsr.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class OracleResourceReader 
{
	private static OracleResourceReader  ref = null;
	private Properties properties;
	
	public Properties getProperties() {
		return properties;
	}

	private OracleResourceReader ()
	{
		properties = new Properties();
		InputStream is =null;
		is = getClass().getResourceAsStream("/"+"oracleDatabase.properties");
		try
		{
			properties.load(is);
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			if(is != null)
			{
				is.close();
			}
		}
		catch(IOException exception)
		{
			System.out.println("IO Exception Occurred");
		}
	}
	
	public static OracleResourceReader  getMyResourceReaderObject()
	{
		if(ref == null)
			ref = new OracleResourceReader ();
		return ref;
		
	}
}
