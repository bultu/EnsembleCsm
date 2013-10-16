package com.centurylink.xprsr.service.impl;

import java.util.ArrayList;
import java.util.TreeMap;

import com.centurylink.xprsr.dao.IFetchTicketDao;
import com.centurylink.xprsr.dao.impl.FetchTicketDao;
import com.centurylink.xprsr.service.IFetchTicketService;


public class FetchTicketService implements IFetchTicketService
{
    @Override
    public TreeMap<Integer, ArrayList<String>> fetchTickets() {
        IFetchTicketDao ticketsFetched = new FetchTicketDao();
        return ticketsFetched.fetchTickets();
    }

    @Override
    public ArrayList<String> fetchNames() {
        FetchTicketDao ticketsFetched = new FetchTicketDao();
        return ticketsFetched.fetchNames();
    }
}
