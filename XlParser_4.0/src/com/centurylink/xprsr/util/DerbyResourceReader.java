package com.centurylink.xprsr.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DerbyResourceReader 
{
	private static DerbyResourceReader  ref = null;
	private Properties properties;
	
	public Properties getProperties() {
		return properties;
	}

	private DerbyResourceReader ()
	{
		properties = new Properties();
		InputStream is =null;
		is = getClass().getResourceAsStream("/"+"derbyDatabase.properties");
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
	
	public static DerbyResourceReader  getMyResourceReaderObject()
	{
		if(ref == null)
			ref = new DerbyResourceReader ();
		return ref;
		
	}
}
