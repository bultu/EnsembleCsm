package com.centurylink.xprsr.service.impl;

import com.centurylink.xprsr.dao.impl.RefreshDataDao;
import com.centurylink.xprsr.service.IRefreshDataService;


public class RefreshDataService implements IRefreshDataService
{
	@Override
	public boolean refreshData() {
		RefreshDataDao refreshDataDao = new RefreshDataDao(); 
		return refreshDataDao.refreshData();
	}
}
