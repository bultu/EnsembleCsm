package com.centurylink.xprsr.action;

import java.util.ArrayList;
import java.util.TreeMap;

import com.centurylink.xprsr.service.IFetchTicketService;
import com.centurylink.xprsr.service.impl.FetchTicketService;

public class FetchTicketAction {
    
    private String error;
    TreeMap<Integer, ArrayList<String>> table = new TreeMap<Integer, ArrayList<String>>();
    ArrayList<String> nameList = new ArrayList<String>();

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public TreeMap<Integer, ArrayList<String>> getTable() {
        return table;
    }

    public void setTable(TreeMap<Integer, ArrayList<String>> table) {
        this.table = table;
    }

    public ArrayList<String> getNameList() {
        return nameList;
    }

    public void setNameList(ArrayList<String> nameList) {
        this.nameList = nameList;
    }

    public String execute() {
        IFetchTicketService ticketsFetched = new FetchTicketService();
        table = ticketsFetched.fetchTickets();
        nameList = ticketsFetched.fetchNames();
        return "success";
    }
}
