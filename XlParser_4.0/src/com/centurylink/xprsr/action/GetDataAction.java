package com.centurylink.xprsr.action;

import com.centurylink.xprsr.service.IGetDataService;
import com.centurylink.xprsr.service.impl.GetDataService;


public class GetDataAction {

    public static void main(String[] args) {
        IGetDataService getData = new GetDataService();
        getData.getData("310365267");
    }
}
