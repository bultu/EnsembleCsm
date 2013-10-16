package com.centurylink.xprsr.service;

import java.util.ArrayList;
import java.util.TreeMap;

public interface IFetchTicketService
{
	public TreeMap<Integer, ArrayList<String>>  fetchTickets();

    public ArrayList<String> fetchNames();
}
