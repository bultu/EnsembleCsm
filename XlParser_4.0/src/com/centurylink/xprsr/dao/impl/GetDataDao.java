package com.centurylink.xprsr.dao.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.centurylink.xprsr.dao.IGetDataDao;
import com.centurylink.xprsr.db.DBManager;

public class GetDataDao implements IGetDataDao
{
	Connection con = null;
	
	@Override
	public void getData(String customer_id)
	{
	    String query = "SELECT cc  FROM ctli.ref_mail WHERE mailto  = 'CTLI_ENS_CSM'";
		
		try
		{
			con = DBManager.getInstance("Derby").getConnection("Derby");
			PreparedStatement pstmt = con.prepareStatement(query);
			/*pstmt.setString(1, customer_id);*/
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				System.out.println(" CC " + rs.getString("CC"));
			}
		} 
		
		catch (SQLException e)
		{
			System.out.println("Data not found!");
		}
		
		finally
		{
		    try 
		    {
                con.close();
            } 
		    
		    catch (SQLException e) 
		    {
                System.out.println("Failed to close connection!");
            }
		}
	}
	
	
	public static void main(String[] args) {
        
	    GetDataDao gDao = new GetDataDao();
	    gDao.getData("Anurag");
    }
}
