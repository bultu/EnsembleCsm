package com.centurylink.xprsr.service.impl;

import com.centurylink.xprsr.dao.IConnectDBDao;
import com.centurylink.xprsr.dao.impl.ConnectDBDao;
import com.centurylink.xprsr.service.IConnectDBService;

public class ConnectDBService implements IConnectDBService {

    @Override
    public boolean connectDB() {
        IConnectDBDao connectDB = new ConnectDBDao();
        return connectDB.connect();
    }

}
