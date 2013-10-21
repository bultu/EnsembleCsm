package com.centurylink.xprsr.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.centurylink.xprsr.dao.IParallelInsertDerby;

public class ParallelInsertDerby extends Thread implements IParallelInsertDerby  {
    
    Connection derbyCon = null;
    String columnNames = null;
    String values = null;    
    String tableName = null;
    int threadId = 0;
    
    public ParallelInsertDerby (Connection derbyCon  ,String colNames , String values  , String tableName, int threadId) {
        this.derbyCon = derbyCon;
        this.columnNames = colNames;
        this.values = values;
        this.tableName = tableName;
        this.threadId = threadId;
    }
    
    public void run() { 
        double istartTime = System.nanoTime();
        System.out.println("Thread #"+threadId+" started at "+ istartTime);
        String query  = "INSERT into "+tableName+" ("+columnNames+")"
                + " VALUES (" +values+ ")";
        
        try {
            PreparedStatement pstmt = derbyCon.prepareStatement(query);
            pstmt.executeUpdate();
            System.out.println("Thread #"+ threadId +" completed in "+((System.nanoTime() - istartTime)/1000000000)+" second(s)");
        } catch (SQLException e) {
            System.out.println("Data not inserted!");
        }
      } 


}
