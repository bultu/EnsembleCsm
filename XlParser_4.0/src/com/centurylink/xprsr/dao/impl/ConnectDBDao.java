package com.centurylink.xprsr.dao.impl;

import com.centurylink.xprsr.dao.IConnectDBDao;
import com.centurylink.xprsr.dto.ConnectDB;

public class ConnectDBDao implements IConnectDBDao {

    public boolean connect() {
        ConnectDB dbConnection = ConnectDB.getMyConnectionObject();

        return dbConnection != null;
    }

}
