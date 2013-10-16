package com.centurylink.xprsr.dao.impl;


import java.sql.*;
import java.util.*;

import com.centurylink.xprsr.dao.IFetchTicketDao;
import com.centurylink.xprsr.dto.ConnectDB;

public class FetchTicketDao implements IFetchTicketDao
{
    ConnectDB dbConnection = ConnectDB.getMyConnectionObject();
    Connection derbyCon = null;
    
    @Override
	public TreeMap<Integer, ArrayList<String>> fetchTickets()
	{
	    int noOfRecords = 0;
	    TreeMap<Integer, ArrayList<String>> table = new TreeMap<Integer, ArrayList<String>>();
	    ArrayList<String> columnList = null;
	            
	    columnList = new ArrayList<String>();
	    columnList.add("CATEGORY");
        columnList.add("CH_DOC_ID");
        columnList.add("PRIORITY");
        columnList.add("FULL_USER_NAME");
        columnList.add("STATUS");
        columnList.add("CREATE_DATE");
        columnList.add("TITLE");
        table.put(noOfRecords, columnList);
        
        String query = "SELECT CATEGORY,CH_DOC_ID,PRIORITY,FULL_USER_NAME,STATUS,CREATE_DATE,TITLE from ctli.tickets";
		
		try
		{
		    derbyCon = dbConnection.getDerbyCon();
            PreparedStatement pstmt = derbyCon.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
			    noOfRecords++;
			    columnList = new ArrayList<String>();
                columnList.add(rs.getString("CATEGORY"));
                columnList.add(rs.getString("CH_DOC_ID"));
                columnList.add(rs.getString("PRIORITY"));
                columnList.add(rs.getString("FULL_USER_NAME"));
                columnList.add(rs.getString("STATUS"));
                columnList.add(rs.getString("CREATE_DATE"));
                columnList.add(rs.getString("TITLE"));
			    table.put(noOfRecords, columnList);
			}
			
		} 
		
		catch (SQLException e)
		{
			System.out.println("Data not found!");
			return null;
		}
		
		return table;
	}

    public ArrayList<String> fetchNames() {
        
        ArrayList<String> nameList = new ArrayList<String>();
        String query = "SELECT FULL_USER_NAME from ctli.ref_dev_threshold";
        
        try
        {
            derbyCon = dbConnection.getDerbyCon();
            PreparedStatement pstmt = derbyCon.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next())
            {
                nameList.add(rs.getString("FULL_USER_NAME"));
            }
            
        } 
        
        catch (SQLException e)
        {
            System.out.println("Data not found!");
            return null;
        }
        
        return nameList;
    }
}
