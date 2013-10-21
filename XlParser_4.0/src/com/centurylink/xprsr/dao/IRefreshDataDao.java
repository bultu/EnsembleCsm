package com.centurylink.xprsr.dao;

import java.util.ArrayList;

import com.centurylink.xprsr.dto.TicketsData;



public interface IRefreshDataDao
{
	public boolean refreshData();
	public void getOracleConnection();
	public void getDerbyConnection();
	public TicketsData getOldTicketbyID(String tempOldTicketID , ArrayList<TicketsData> oldTicketsList);
}
