package com.centurylink.xprsr.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.struts2.ServletActionContext;

import com.centurylink.xprsr.service.IAssignTickets;
import com.centurylink.xprsr.service.IReadXlService;
import com.centurylink.xprsr.service.impl.AssignTickets;
import com.centurylink.xprsr.service.impl.ReadXlService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Anurag Chowdhury
 * @author Prateek Gupta
 * @version 4.0
 * @since JUNE 2013
 */
public class AssignTicketsAction extends ActionSupport {

    private static final long serialVersionUID = 1L;
    IAssignTickets countTickets;
    
    @SuppressWarnings("unchecked")
    TreeMap<Integer, ArrayList<String>> parsedSheet = (TreeMap<Integer, ArrayList<String>>) ServletActionContext
            .getRequest().getSession().getAttribute("parsedSheet");
    
    public static String inputFilePath = (String) ServletActionContext
            .getRequest().getSession().getAttribute("inputFilePath");
    IReadXlService readXl = new ReadXlService();
    TreeMap<String, Integer[]> currentCount;
    TreeMap<Integer, ArrayList<String>> table = new TreeMap<Integer, ArrayList<String>>();
    TreeMap<Integer, ArrayList<String>> assignedTable = new TreeMap<Integer, ArrayList<String>>();
    TreeMap<Integer, ArrayList<String>> restrictedTicketsList = new TreeMap<Integer, ArrayList<String>>();
    ArrayList<String> nameList = new ArrayList<String>();
    Integer totalTickets = -1;
    Integer totalAssignedTickets = -1;
    Integer totalRestrictedTickets = 0;

    String mailTo = null;
    String cc = null;
    String subject = null;
    String body = null;
    
    public Integer getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(Integer totalTickets) {
        this.totalTickets = totalTickets;
    }

    public Integer getTotalAssignedTickets() {
        return totalAssignedTickets;
    }

    public void setTotalAssignedTickets(Integer totalAssignedTickets) {
        this.totalAssignedTickets = totalAssignedTickets;
    }

    public TreeMap<Integer, ArrayList<String>> getAssignedTable() {
        return assignedTable;
    }

    public void setAssignedTable(
            TreeMap<Integer, ArrayList<String>> assignedTable) {
        this.assignedTable = assignedTable;
    }

    public ArrayList<String> getNameList() {
        return nameList;
    }

    public void setNameList(ArrayList<String> nameList) {
        this.nameList = nameList;
    }

    public TreeMap<Integer, ArrayList<String>> getTable() {
        return table;
    }

    public void setTable(TreeMap<Integer, ArrayList<String>> table) {
        this.table = table;
    }

    public TreeMap<String, Integer[]> getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(TreeMap<String, Integer[]> currentCount) {
        this.currentCount = currentCount;
    }

    public TreeMap<Integer, ArrayList<String>> getParsedSheet() {
        return parsedSheet;
    }

    public void setParsedSheet(TreeMap<Integer, ArrayList<String>> parsedSheet) {
        this.parsedSheet = parsedSheet;
    }

    public String getMailTo() {
        return mailTo;
    }

    public String getCc() {
        return cc;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
    
    public Integer getTotalRestrictedTickets() {
        return totalRestrictedTickets;
    }
    
    /**
     * On "success", navigates to autoAssignedXl.jsp
     * 
     * @see AssignTickets#countTickets(TreeMap, TreeMap, TreeMap)
     * @see ReadXlService#read(String, Integer)
     * @see AssignTickets#getNames(TreeMap)
     */
    public String ticketCounter() {
        countTickets = new AssignTickets();

        try {
            table = countTickets.countTickets(parsedSheet,
                    readXl.read(inputFilePath, "THRESHOLD"),
                    readXl.read(inputFilePath, "CATEGORIES"),
                    readXl.read(inputFilePath, "TICKET_STATUS"),
                    readXl.read(inputFilePath, "RESTRICTED_KEYWORDS"));
                       
            for (Entry<Integer, ArrayList<String>> tableEntry : table
                    .entrySet()) {
                totalTickets++;
                if (!tableEntry.getValue().get(2).equalsIgnoreCase(" ")) {
                    totalAssignedTickets++;
                    assignedTable.put(tableEntry.getKey(),
                            tableEntry.getValue());
                }

            }
            /*ServletActionContext.getRequest().getSession()
                    .setAttribute("assignedTable", table);*/
            nameList = countTickets.getNames(readXl.read(inputFilePath, "THRESHOLD"));
            crushTreeMap(readXl.read(inputFilePath, "MAIL"));
            
            restrictedTicketsList = readXl.read(inputFilePath, "RESTRICTED_TICKETS");
            
            for (@SuppressWarnings("unused") Entry<Integer, ArrayList<String>> tableEntry : restrictedTicketsList
                    .entrySet()) {
                totalRestrictedTickets++;
            }
            return "success";

        }

        catch (IOException e) {
            System.out.println("Cannot find file!");
            return "error";
        }
    }

    public void crushTreeMap(TreeMap<Integer, ArrayList<String>> mail) {
        mailTo = mail.get(0).get(1);
        cc = mail.get(1).get(1);
        subject = mail.get(2).get(1);
        body = mail.get(3).get(1);
    }
}
