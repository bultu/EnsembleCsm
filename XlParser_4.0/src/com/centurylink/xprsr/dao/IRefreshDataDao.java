package com.centurylink.xprsr.dao;



public interface IRefreshDataDao
{
	public boolean refreshData();
	public void getOracleConnection();
	public void getDerbyConnection();
}
