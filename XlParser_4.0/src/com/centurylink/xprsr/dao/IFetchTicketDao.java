package com.centurylink.xprsr.dao;

import java.util.ArrayList;
import java.util.TreeMap;

public interface IFetchTicketDao
{
	public TreeMap<Integer, ArrayList<String>> fetchTickets();
	public ArrayList<String> fetchNames();
}
