package com.centurylink.xprsr.action;

import com.centurylink.xprsr.service.IRefreshDataService;
import com.centurylink.xprsr.service.impl.RefreshDataService;
import com.opensymphony.xwork2.ActionSupport;

public class RefreshDataAction extends ActionSupport {

    private static final long serialVersionUID = 1L;
    private String error;
    
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String execute() {

        IRefreshDataService refreshDataService = new RefreshDataService();

        if (refreshDataService.refreshData())
            return "success";
        else
        {   error = "Error in Database Connection";
            return "error";
        }

    }
}
