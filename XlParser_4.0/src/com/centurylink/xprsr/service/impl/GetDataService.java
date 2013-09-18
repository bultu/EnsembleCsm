package com.centurylink.xprsr.service.impl;

import com.centurylink.xprsr.dao.IGetDataDao;
import com.centurylink.xprsr.dao.impl.GetDataDao;
import com.centurylink.xprsr.service.IGetDataService;


public class GetDataService implements IGetDataService
{
	@Override
	public void getData(String customer_id) {
		IGetDataDao getDataDao = new GetDataDao(); 
		getDataDao.getData(customer_id);
	}
}
