package com.centurylink.xprsr.service.impl;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.struts2.ServletActionContext;

import com.centurylink.xprsr.service.IAssignTickets;
import com.centurylink.xprsr.service.IWriteXlService;

/**
 * @author Anurag Chowdhury
 * @author Prateek Gupta
 * @version 1.0
 * @since JUNE 2013
 */
public class AssignTickets implements IAssignTickets {

    /**
     * 1) Counts the number of tickets assigned to each member of the team.<br>
     * 2) Sorts the team members in decreasing order of tickets to be assigned.<br>
     * 3) Assigns the requisite tickets
     * 
     * @see SortAssignment#sort(Object[], Comparator)
     * 
     * @see AssignTickets#assignTo(TreeMap, SortAssignment[], ArrayList)
     * 
     * @param dataSheet
     *            The "DATA" sheet of the excel workbook
     * @param userThreshold
     *            The max. no. of tickets to be assigned to a particular team
     *            member
     * @param typeOfTickets
     *            The different categories of tickets defined
     */

    private String browseFile = (String) ServletActionContext.getRequest()
            .getSession().getAttribute("inputFilePath");

    int assignedTicketCount = -1;
    int totalTicketCount = -1;

    public int getAssignedTicketCount() {
        return assignedTicketCount;
    }
    
    public int getTotalTicketCount() {
        return totalTicketCount;
    }

    private TreeMap<Integer, ArrayList<String>> restrictedTickets = new TreeMap<Integer, ArrayList<String>>();

    @Override
    public TreeMap<Integer, ArrayList<String>> countTickets(
            TreeMap<Integer, ArrayList<String>> dataSheet,
            TreeMap<Integer, ArrayList<String>> userThreshold,
            TreeMap<Integer, ArrayList<String>> typeOfTickets,
            TreeMap<Integer, ArrayList<String>> ticketStatus,
            TreeMap<Integer, ArrayList<String>> restrictedKeywords) {

        TreeMap<String, Integer[]> currentAssignedCount = new TreeMap<String, Integer[]>();
        Integer totalcount = 0;
        Integer categoryCount = 0;
        Integer threshold = 0;
        Integer counter = 0;
        ArrayList<String> tickStatList = new ArrayList<String>();
        ArrayList<String> resKeywordList = new ArrayList<String>();
        ArrayList<String> tempTTArr = new ArrayList<String>();

        for (Entry<Integer, ArrayList<String>> tempTSL : ticketStatus
                .entrySet()) {
            tickStatList.add(tempTSL.getValue().get(0));
        }

        for (Entry<Integer, ArrayList<String>> tempRKL : restrictedKeywords
                .entrySet()) {
            resKeywordList.add(tempRKL.getValue().get(0));
        }

        SortAssignment[] sortAssignmentList = new SortAssignment[userThreshold
                .entrySet().size() - 1];

        for (Entry<Integer, ArrayList<String>> tempTT : typeOfTickets
                .entrySet()) {
            tempTTArr.add(tempTT.getValue().get(0));
        }

        for (Entry<Integer, ArrayList<String>> userThresholdEntry : userThreshold
                .entrySet()) {
            Integer countArray[] = new Integer[tempTTArr.size()];
            counter = 0;
            while (tempTTArr.size() > counter) {
                countArray[counter] = 0;
                counter++;
            }

            if (userThresholdEntry.getKey() != 0) {
                totalcount = 0;

                for (Entry<Integer, ArrayList<String>> dataSheetEntry : dataSheet
                        .entrySet()) {
                    if (dataSheetEntry.getKey() != 0) {

                        if (userThresholdEntry
                                .getValue()
                                .get(0)
                                .equalsIgnoreCase(
                                        dataSheetEntry.getValue().get(2))
                                && !tickStatList.contains(dataSheetEntry
                                        .getValue().get(3))
                                && !containsResKeywordList(dataSheetEntry
                                        .getValue().get(5), resKeywordList)) {
                            totalcount++;
                            categoryCount = 0;

                            while (tempTTArr.size() > categoryCount) {
                                if (dataSheetEntry
                                        .getValue()
                                        .get(0)
                                        .equalsIgnoreCase(
                                                tempTTArr.get(categoryCount))) {
                                    countArray[categoryCount]++;
                                }
                                categoryCount++;
                            }
                        }
                    }
                }
                Double temp = Double.parseDouble(userThresholdEntry.getValue()
                        .get(1));
                threshold = temp.intValue();
            }

            countArray[0] = threshold - totalcount;

            if (userThresholdEntry.getKey() != 0) {
                sortAssignmentList[(userThresholdEntry.getKey() - 1)] = new SortAssignment(
                        userThresholdEntry.getValue().get(0), countArray);
                currentAssignedCount.put(userThresholdEntry.getValue().get(0),
                        countArray);
            }

        }

        SortAssignment.setSortAssignmentList(sortAssignmentList);
        SortAssignment.sort(SortAssignment.getSortAssignmentList(),
                SortAssignment.BY_TOTALREQUIREDTICKETS);

        TreeMap<Integer, ArrayList<String>> newAssignedList = assignTo(
                dataSheet, SortAssignment.getSortAssignmentList(), tempTTArr,
                resKeywordList);

        /*
         * for (Entry<Integer, ArrayList<String>> tempInputFile :
         * newAssignedList .entrySet()) { Object temp =
         * tempInputFile.getValue(); if (tempInputFile.getValue().get(3)
         * .equalsIgnoreCase("WAITING FOR VENDOR")) {
         * newAssignedList.remove(temp); }
         * 
         * }
         */

        TreeMap<Integer, ArrayList<String>> assignedListForDisplay = new TreeMap<Integer, ArrayList<String>>();
        int key = 0;
        for (Entry<Integer, ArrayList<String>> tempAssignedList : newAssignedList
                .entrySet()) {
            
            totalTicketCount++;
            if (!tempAssignedList.getValue().get(2).equalsIgnoreCase(" ")) {
                assignedTicketCount++;
            }

            if (!tempAssignedList.getValue().get(3)
                    .equalsIgnoreCase("WAITING FOR VENDOR")) {
                assignedListForDisplay.put(key++,
                        tempAssignedList.getValue());
            }
            
            

        }
        return assignedListForDisplay;
    }

    /**
     * Assigns the requisite tickets depending upon the category of the ticket
     * already assigned/to be assigned to a particular team member.
     * 
     * @param inputFile
     *            The "DATA" sheet of the excel workbook
     * @param sortAssignmentList
     *            No. of tickets, of each category, to be assigned to a team
     *            member sorted in decreasing order of total no. of tickets to
     *            be assigned
     * @param tempTTArr
     *            different categories of tickets
     * @param tickStatList
     */
    @Override
    public TreeMap<Integer, ArrayList<String>> assignTo(
            TreeMap<Integer, ArrayList<String>> inputFile,
            SortAssignment[] sortAssignmentList, ArrayList<String> tempTTArr,
            ArrayList<String> resKeywordList) {

        String leastTickHolder = new String();

        while (sortAssignmentList[0].getCountArray()[0] > 0
                && ticketsAvailableToAssign(inputFile, resKeywordList)) {

            Integer status = new Integer(0);
            leastTickHolder = sortAssignmentList[0].getName();

            System.out.println("Starting assignment for " + leastTickHolder);

            for (Entry<Integer, ArrayList<String>> dataSheetEntry : inputFile
                    .entrySet()) {

                if (sortAssignmentList[0].getCountArray()[0] <= 0)
                    break;

                if (dataSheetEntry.getValue().get(2).equalsIgnoreCase(" ")) {

                    if (containsResKeywordList(
                            dataSheetEntry.getValue().get(5), resKeywordList)) {
                        restrictedTickets.put(dataSheetEntry.getKey(),
                                dataSheetEntry.getValue());
                        continue;
                    }

                    boolean flag = false;

                    int tempVar = 0;

                    for (int k = 1; k < tempTTArr.size(); k++) {
                        if (inputFile.get(dataSheetEntry.getKey()).get(0)
                                .equalsIgnoreCase(tempTTArr.get(k))) {
                            tempVar = k;
                            break;
                        }
                    }

                    if (sortAssignmentList[0].getCountArray()[tempVar] == 0) {
                        inputFile.get(dataSheetEntry.getKey()).set(2,
                                sortAssignmentList[0].getName());

                        System.out.println("Assigned "
                                + dataSheetEntry.getValue().get(1) + " to "
                                + sortAssignmentList[0].getName());

                        sortAssignmentList[0].getCountArray()[tempVar]++;
                        sortAssignmentList[0].getCountArray()[0]--;
                    } else {
                        flag = true;
                    }

                    if (!flag) {
                        SortAssignment.sort(sortAssignmentList,
                                SortAssignment.BY_TOTALREQUIREDTICKETS);
                        if (!sortAssignmentList[0].getName().equalsIgnoreCase(
                                leastTickHolder)) {
                            status = 1;
                            break;
                        }
                    }
                }

            }

            /*
             * System.out.println("Start of While loop....");
             * System.out.println(sortAssignmentList[0].getCountArray()[0] +
             * ":D");
             */

            while (status != 1 && sortAssignmentList[0].getCountArray()[0] > 0
                    && ticketsAvailableToAssign(inputFile, resKeywordList)) {
                leastTickHolder = sortAssignmentList[0].getName();
                for (Entry<Integer, ArrayList<String>> dataSheetEntry : inputFile
                        .entrySet()) {
                    int tempVar = 0;

                    if (sortAssignmentList[0].getCountArray()[0] <= 0)
                        break;

                    if (dataSheetEntry.getValue().get(2).equalsIgnoreCase(" ")) {

                        if (containsResKeywordList(dataSheetEntry.getValue()
                                .get(5), resKeywordList)) {
                            restrictedTickets.put(dataSheetEntry.getKey(),
                                    dataSheetEntry.getValue());
                            continue;
                        }

                        for (int k = 1; k < tempTTArr.size(); k++) {
                            if (inputFile.get(dataSheetEntry.getKey()).get(0)
                                    .equalsIgnoreCase(tempTTArr.get(k))) {
                                tempVar = k;
                                break;
                            }
                        }

                        inputFile.get(dataSheetEntry.getKey()).set(2,
                                sortAssignmentList[0].getName());

                        /*
                         * System.out.println("Assigned " +
                         * dataSheetEntry.getValue().get(1) + " to " +
                         * sortAssignmentList[0].getName() + " w/o category");
                         */

                        sortAssignmentList[0].getCountArray()[tempVar]++;
                        sortAssignmentList[0].getCountArray()[0]--;
                        SortAssignment.sort(sortAssignmentList,
                                SortAssignment.BY_TOTALREQUIREDTICKETS);
                        if (!sortAssignmentList[0].getName().equalsIgnoreCase(
                                leastTickHolder)) {
                            status = 1;
                            break;
                        }

                    }
                }
            }
        }

        IWriteXlService writeXL = new WriteXlService();
        writeXL.writeToNewSheet(browseFile, restrictedTickets,
                "RESTRICTED_TICKETS");

        return inputFile;
    }

    /**
     * Gets name of all the team members
     * 
     * @param assignedTickets
     *            TreeMap of assigned list
     * 
     */
    @Override
    public ArrayList<String> getNames(
            TreeMap<Integer, ArrayList<String>> assignedTickets) {
        ArrayList<String> tempTTArr = new ArrayList<String>();
        for (int i = 0; i < assignedTickets.size(); i++) {
            if (i == 0) {
                tempTTArr.add("Not Assigned");
            } else {
                tempTTArr.add(assignedTickets.get(i).get(0));
            }
        }
        return tempTTArr;
    }

    /**
     * Returns re-assigned list
     * 
     * @param theParsedSheet
     *            TreeMap of assigned list
     * @param theElement
     *            The name of the team member to whom the ticket has to be
     *            re-assigned
     * @param button
     *            The hidden value of the "save" button
     * @see manualAssignedXl.jsp
     * 
     */
    @Override
    public TreeMap<Integer, ArrayList<String>> reAssignTo(
            TreeMap<Integer, ArrayList<String>> theParsedSheet,
            String theElement, String button) {
        if (theElement.equalsIgnoreCase("Not Assigned")) {
            theParsedSheet.get(Integer.valueOf(button)).set(2, " ");
        } else {
            theParsedSheet.get(Integer.valueOf(button)).set(2, theElement);
        }

        return theParsedSheet;
    }

    /**
     * Gets name of all the statuses
     * 
     * @param typeOfStati
     *            TreeMap of status list
     * 
     */
    @Override
    public ArrayList<String> getStati(
            TreeMap<Integer, ArrayList<String>> typeOfStati) {
        ArrayList<String> tempTTArr = new ArrayList<String>();
        for (int i = 0; i < typeOfStati.size(); i++) {
            if (i == 0) {
                tempTTArr.add("New/In Progress");
            } else {
                tempTTArr.add(typeOfStati.get(i).get(0));
            }
        }
        return tempTTArr;
    }

    /**
     * Returns the assigned list with modified status
     * 
     * @param theParsedSheet
     *            TreeMap of assigned list
     * @param theElement
     *            The New status
     * @param button
     *            The hidden value of the "assign" button
     * @see manualAssignedXl.jsp
     */
    @Override
    public TreeMap<Integer, ArrayList<String>> changeStatus(
            TreeMap<Integer, ArrayList<String>> theParsedSheet,
            String theElement, String button) {
        theParsedSheet.get(Integer.valueOf(button)).set(0, theElement);
        return theParsedSheet;
    }

    @Override
    public boolean containsResKeywordList(String titleList,
            ArrayList<String> restrictedKeywords) {
        boolean flagKeyword = false;
        for (int l = 0; l < restrictedKeywords.size(); l++) {

            if (!restrictedKeywords.get(l).equalsIgnoreCase(" ")) {
                if (titleList.contains(restrictedKeywords.get(l))) {
                    flagKeyword = true;
                    break;
                }
            }
        }
        return flagKeyword;
    }

    @Override
    public boolean ticketsAvailableToAssign(
            TreeMap<Integer, ArrayList<String>> dataSheet,
            ArrayList<String> restrictedKeywords) {

        for (Entry<Integer, ArrayList<String>> dataSheetEntry : dataSheet
                .entrySet()) {

            if (dataSheetEntry.getValue().get(2).equalsIgnoreCase(" ")
                    && !containsResKeywordList(
                            dataSheetEntry.getValue().get(5),
                            restrictedKeywords)) {
                return true;
            } else if (containsResKeywordList(dataSheetEntry.getValue().get(5),
                    restrictedKeywords)) {
                restrictedTickets.put(dataSheetEntry.getKey(),
                        dataSheetEntry.getValue());
            }
        }
        return false;
    }
}
