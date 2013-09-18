package com.centurylink.xprsr.service.impl;


import com.centurylink.xprsr.dao.IRefreshDataDao;
import com.centurylink.xprsr.dao.impl.RefreshDataDao;
import com.centurylink.xprsr.service.IRefreshDataService;


public class RefreshDataService implements IRefreshDataService
{
	@Override
	public void refreshData() {
		IRefreshDataDao refreshDataDao = new RefreshDataDao(); 
		refreshDataDao.refreshData();
	}
}
