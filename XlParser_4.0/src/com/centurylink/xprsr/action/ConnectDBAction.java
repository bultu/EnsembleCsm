package com.centurylink.xprsr.action;

import com.centurylink.xprsr.service.IConnectDBService;
import com.centurylink.xprsr.service.impl.ConnectDBService;

public class ConnectDBAction {

    String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String execute() {
        IConnectDBService connectDb = new ConnectDBService();

        if (connectDb.connectDB())
            return "success";
        else
        {   error = "Error in Database Connection";
            return "error";
        }

    }

}
