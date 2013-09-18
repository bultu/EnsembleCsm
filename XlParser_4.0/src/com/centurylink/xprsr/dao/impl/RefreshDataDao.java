package com.centurylink.xprsr.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.centurylink.xprsr.dao.IRefreshDataDao;
import com.centurylink.xprsr.db.DBManager;
import com.centurylink.xprsr.dto.TicketsData;

public class RefreshDataDao implements IRefreshDataDao {
    Connection con1 = null;
    Connection con2 = null;

    @Override
    public void refreshData() {
        String query1 = "TRUNCATE table ctli.tickets ; ";
        String query2 = "select CATEGORY,CH_DOC_ID,FULL_USER_NAME,STATUS,CREATE_DATE,TITLE from ctli.ogs_tickets;";
        String query3 = "INSERT into ctli.tickets (CATEGORY,CH_DOC_ID,FULL_USER_NAME,STATUS,CREATE_DATE,TITLE)" +
        		        "VALUES ?, ?, ?, ?, ?, ?";
        ArrayList<TicketsData> ticketsList = new ArrayList<TicketsData>();
        
        try {
            /*
             * Create a connection to Derby database and truncate the tickets
             * table before filling it with fresh data
             */
            con1 = DBManager.getInstance("Derby").getConnection("Derby");
            PreparedStatement pstmt1 = con1.prepareStatement(query1);
            pstmt1.executeQuery();

            /* Create a connection to Oracle database and get fresh tickets data */
            con2 = DBManager.getInstance("Derby").getConnection("Derby");
            PreparedStatement pstmt2 = con2.prepareStatement(query2);
            ResultSet rs = pstmt2.executeQuery();

            if (rs.next()) {
                
                TicketsData tempTicketData = new TicketsData();
                tempTicketData.setCategory(rs.getString("CATEGORY"));
                tempTicketData.setCh_doc_id(rs.getString("CH_DOC_ID"));
                tempTicketData.setFull_user_name(rs.getString("FULL_USER_NAME"));
                tempTicketData.setStatus(rs.getString("STATUS"));
                tempTicketData.setCreate_date(rs.getString("CREATE_DATE"));
                tempTicketData.setTitle(rs.getString("TITLE"));                
                ticketsList.add(tempTicketData);
            }
            
            PreparedStatement pstmt3 = con1.prepareStatement(query3);
            
            Iterator<TicketsData> iterator = ticketsList.iterator();
            while(iterator.hasNext()){
                TicketsData tempTicketData = iterator.next();
                pstmt3.setString(1, tempTicketData.getCategory());
                pstmt3.setString(2, tempTicketData.getCh_doc_id());
                pstmt3.setString(3, tempTicketData.getFull_user_name());
                pstmt3.setString(4, tempTicketData.getStatus());
                pstmt3.setString(5, tempTicketData.getCreate_date());
                pstmt3.setString(6, tempTicketData.getTitle());
                pstmt3.executeQuery();
            }

        }

        catch (SQLException e) {
            System.out.println("Data not found!");
        }

        finally {
            try {
                con1.close();
                con2.close();
            }

            catch (SQLException e) {
                System.out.println("Failed to close connection!");
            }
        }
    }

}
