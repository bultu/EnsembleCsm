package com.centurylink.xprsr.dao.impl;

import com.centurylink.xprsr.dao.IConnectDBDao;
import com.centurylink.xprsr.dto.ConnectDB;

public class ConnectDBDao implements IConnectDBDao {

    double startTime = 0;
    public boolean connect() {
        startTime = System.nanoTime(); 
        ConnectDB dbConnection = ConnectDB.getMyConnectionObject();
        double estimatedTime = System.nanoTime() - startTime;
        System.out.println("Re/Connected to databases in "+estimatedTime/1000000000+" second(s)");
        return dbConnection != null;
    }

}
