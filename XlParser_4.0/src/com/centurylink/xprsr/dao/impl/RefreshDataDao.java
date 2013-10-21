package com.centurylink.xprsr.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.centurylink.xprsr.dao.IParallelInsertDerby;
import com.centurylink.xprsr.dao.IRefreshDataDao;
import com.centurylink.xprsr.db.DerbyDBManager;
import com.centurylink.xprsr.db.OracleDBManager;
import com.centurylink.xprsr.dto.ConnectDB;
import com.centurylink.xprsr.dto.TicketsData;

public class RefreshDataDao implements IRefreshDataDao {
    Connection derbyCon = null;
    Connection oracleCon = null;
    double startTime = 0;
    int threadId = 1;

    @Override
    public boolean refreshData() {
        String query0 = "select CATEGORY,CH_DOC_ID,FULL_USER_NAME,STATUS,CREATE_DATE,TITLE from ctli.tickets";
        String query1 = "DELETE from ctli.tickets";
        String query2 = "select CATEGORY,CH_DOC_ID,FULL_USER_NAME,STATUS,CREATE_DATE,TITLE from cuappc.ogs_tickets";

        ArrayList<String> oldTicketsID = new ArrayList<String>();
        ArrayList<String> newTicketsID = new ArrayList<String>();

        ArrayList<TicketsData> ticketsList = new ArrayList<TicketsData>();
        ArrayList<TicketsData> oldTicketsList = new ArrayList<TicketsData>();
        ArrayList<TicketsData> resolvedTicketsList = new ArrayList<TicketsData>();

        try {
            /*
             * Create a connection to Derby database and truncate the tickets
             * table before filling it with fresh data
             */

            startTime = System.nanoTime();
            ConnectDB dbConnection = ConnectDB.getMyConnectionObject();
            derbyCon = dbConnection.getDerbyCon();
            oracleCon = dbConnection.getOracleCon();

            PreparedStatement pstmt0 = derbyCon.prepareStatement(query0);
            ResultSet rs0 = pstmt0.executeQuery();
            
            System.out.println(rs0.getFetchSize());
            
            PreparedStatement pstmt1 = derbyCon.prepareStatement(query1);
            pstmt1.executeUpdate();
            
            
            System.out.println(rs0.getFetchSize());

            PreparedStatement pstmt2 = oracleCon.prepareStatement(query2);
            ResultSet rs1 = pstmt2.executeQuery();

            while (rs0.next()) {

                TicketsData tempTicketData = new TicketsData();
                tempTicketData.setCategory(rs0.getString("CATEGORY"));
                tempTicketData.setCh_doc_id(rs0.getString("CH_DOC_ID"));
                oldTicketsID.add(tempTicketData.getCh_doc_id());
                tempTicketData.setFull_user_name(rs0
                        .getString("FULL_USER_NAME"));
                tempTicketData.setStatus(rs0.getString("STATUS"));
                tempTicketData.setCreate_date(rs0.getString("CREATE_DATE"));
                tempTicketData.setTitle(rs0.getString("TITLE"));
                oldTicketsList.add(tempTicketData);

            }

            int i = 0;
            System.out.println("Fetching data from Oracle db..");
            double istartTime = System.nanoTime();
            while (rs1.next()) {

                TicketsData tempTicketData = new TicketsData();
                tempTicketData.setCategory(rs1.getString("CATEGORY"));
                tempTicketData.setCh_doc_id(rs1.getString("CH_DOC_ID"));
                newTicketsID.add(tempTicketData.getCh_doc_id());
                tempTicketData.setFull_user_name(rs1
                        .getString("FULL_USER_NAME"));
                tempTicketData.setStatus(rs1.getString("STATUS"));
                tempTicketData.setCreate_date(rs1.getString("CREATE_DATE"));
                tempTicketData.setTitle(rs1.getString("TITLE"));
                ticketsList.add(tempTicketData);
            }

            double ielapsedTime = (System.nanoTime() - istartTime) / 1000000000;
            System.out.println("Fetched Data from Oracle DB in " + ielapsedTime
                    + " second(s)");

            Iterator<String> oldIterator = oldTicketsID.iterator();
            while (oldIterator.hasNext()) {
                String tempOldTicketID = oldIterator.next();
                if (!newTicketsID.contains(tempOldTicketID)) {
                    resolvedTicketsList.add(getOldTicketbyID(tempOldTicketID,
                            oldTicketsList));
                }

            }

            if (!resolvedTicketsList.isEmpty()) {
                System.out.println("No. of Resolved Tickets : "
                        + resolvedTicketsList.size());
                Iterator<TicketsData> resolvedIterator = resolvedTicketsList
                        .iterator();
                String colNames = "CH_DOC_ID, STATUS, CREATE_DATE, TITLE, FULL_USER_NAME";
                String tableName = "ctli.tickets_report";
                while (resolvedIterator.hasNext()) {
                    TicketsData tempResolvedTicketData = resolvedIterator
                            .next();

                    String values = "'" + tempResolvedTicketData.getCh_doc_id()
                            + "','" + tempResolvedTicketData.getStatus()
                            + "','" + tempResolvedTicketData.getCreate_date()
                            + "','" + tempResolvedTicketData.getTitle() + "','"
                            + tempResolvedTicketData.getFull_user_name() + "'";

                    ParallelInsertDerby parallelInsert = new ParallelInsertDerby(
                            derbyCon, colNames, values, tableName, threadId++);
                    parallelInsert.start();

                }
            }

            Iterator<TicketsData> iterator1 = ticketsList.iterator();

            System.out.println("Inserting data into Derby db");
            i = 0;

            String colNames1 = "CATEGORY,CH_DOC_ID,FULL_USER_NAME,STATUS,CREATE_DATE,TITLE";
            String tableName = "ctli.tickets";
            threadId = 0;
            while (iterator1.hasNext()) {
                TicketsData tempTicketData = iterator1.next();

                String values = "'" + tempTicketData.getCategory() + "','"
                        + tempTicketData.getCh_doc_id() + "','"
                        + tempTicketData.getFull_user_name() + "','"
                        + tempTicketData.getStatus() + "','"
                        + tempTicketData.getCreate_date() + "','"
                        + tempTicketData.getTitle() + "'";

                ParallelInsertDerby parallelInsert = new ParallelInsertDerby(
                        derbyCon, colNames1, values, tableName, threadId++);
                parallelInsert.start();

            }

        }

        catch (SQLException e) {
            System.out.println("Data not found!");
        }

        finally {
            try {
                /*
                 * derbyCon.close(); oracleCon.close();
                 */
                double estimatedTime = System.nanoTime() - startTime;
                System.out.println("Data Refreshed from OGS Tickets in "
                        + estimatedTime / 1000000000 + " second(s)");
            } catch (NullPointerException e) {
                System.out.println("Connection doesn't exist");
                return false;

            }
        }
        return true;
    }

    public void getDerbyConnection() {
        DerbyDBManager derbyDb = DerbyDBManager.getMyDBManagerObject();
        derbyCon = derbyDb.getConnection();
    }

    public void getOracleConnection() {
        OracleDBManager oracleDb = OracleDBManager.getMyDBManagerObject();
        oracleCon = oracleDb.getConnection();
    }

    public TicketsData getOldTicketbyID(String tempOldTicketID,
            ArrayList<TicketsData> oldTicketsList) {
        Iterator<TicketsData> oldIterator = oldTicketsList.iterator();
        while (oldIterator.hasNext()) {
            TicketsData tempOldTicket = oldIterator.next();
            if (tempOldTicket.getCh_doc_id().equalsIgnoreCase(tempOldTicketID)) {
                return tempOldTicket;
            }
        }
        return null;

    }

}
