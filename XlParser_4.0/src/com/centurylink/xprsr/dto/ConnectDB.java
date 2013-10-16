package com.centurylink.xprsr.dto;

import java.sql.Connection;

import com.centurylink.xprsr.db.DerbyDBManager;
import com.centurylink.xprsr.db.OracleDBManager;

public class ConnectDB {

    static Connection derbyCon = null;
    static Connection oracleCon = null;

    private static ConnectDB ref = null;

    private ConnectDB() {

    }

    public Connection getDerbyCon() {
        return derbyCon;
    }

    public Connection getOracleCon() {
        return oracleCon;
    }

    public static ConnectDB getMyConnectionObject() {
        if (ref == null) {
            try {
                getDerbyConnection();
                getOracleConnection();
            } catch (Exception e) {
                System.out
                        .println("ReconnectDB.java -> Failed to connect to DB");
                return ref;
            }
            ref = new ConnectDB();
        }
        return ref;

    }

    private static void getDerbyConnection() {
        DerbyDBManager derbyDb = DerbyDBManager.getMyDBManagerObject();
        derbyCon = derbyDb.getConnection();
    }

    private static void getOracleConnection() {
        OracleDBManager oracleDb = OracleDBManager.getMyDBManagerObject();
        oracleCon = oracleDb.getConnection();
    }

}
