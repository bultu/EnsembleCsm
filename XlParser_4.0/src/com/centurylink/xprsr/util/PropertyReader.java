package com.centurylink.xprsr.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader 
{
	private static PropertyReader  ref = null;
	private Properties properties;
	
	public Properties getProperties() {
		return properties;
	}

	private PropertyReader ()
	{
		properties = new Properties();
		InputStream is =null;
		is = getClass().getResourceAsStream("/"+"database.properties");
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
	
	public static PropertyReader  getMyResourceReaderObject()
	{
		if(ref == null)
			ref = new PropertyReader ();
		return ref;
		
	}
}
