/**
 * NAME: Isabella Elge
 * CLASS: INFO 1531
 * ASSIGNMENT: Assignment 6 - Work Order Database
 * DATE:10/14/25
 * RESOURCES: I utilized the book and lecture for this weeks assignments
 *
 * PURPOSE: The user facing code that processes the system, including the menu
 */

import java.util.ArrayList;
import java.util.Scanner;

public class OrderSystem {
    static private Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        int operation = 0;
        while (operation != 5) {
            System.out.println("*** Custom Marvels Contractor ***");
            System.out.println("""
                    1. Search Work Orders
                    2. Change Status
                    3. Add a New Work Order
                    4. Delete a Work Order
                    5. Exit""");
            operation = getInt("Operation: ", 1, 5);

            switch (operation) {
                case 1 -> searchWorkOrders();
                case 2 -> changeStatus();
                case 3 -> addNewWorkOrder();
                case 4 -> deleteWorkOrder();
                case 5 -> System.out.println("System Exiting, Goodbye.");
            }
            System.out.println(); // blank line for each new run of main
        }
    }

    private static void searchWorkOrders() {
        ArrayList<WorkOrder> searchedOrders = new ArrayList<>();
        while (true) {
            System.out.println("\n** Search for Work Orders **");
            System.out.println("""
                    1. Search via ID
                    2. Search via Name
                    3. Search via Job Type
                    4. Search via Status
                    5. Back to Main Menu""");
            int operation = getInt("Search choice: ", 1, 5);

            // use the below opString for a header to print out what type of job we are doing.
            System.out.println(); // blank after space

            // back to main menu
            if (operation == 5) {
                break; // get out of loop
            }

            //************************************* Searching ********************************************
            String opString = ""; // use for printing out a header for what was searched.
            switch (operation) {
                case 1 -> { // id search
                    opString = "ID";
                    int id = getInt("Enter work ID: ");
                    searchedOrders.add(WorkOrderDB.searchID(id)); // will only be one order so add to list to print out below
                }
                case 2 -> { // name search
                    opString = "Name";
                    System.out.print("Enter name on work order(last, first): ");
                    String name = input.nextLine();
                    searchedOrders = WorkOrderDB.searchName(name); // get search results from db.
                }
                case 3 -> { // type search
                    int counter = 1;
                    System.out.println("Job Types to Search:");
                    for (JobType jt : JobType.values()) {
                        System.out.println(counter++ + ". " + jt);
                    }
                    int type = getInt("Enter job type: ", 1, JobType.values().length); // use the length as we are off by one

                    opString += "Job Type of " + JobType.values()[type-1].toString(); // make printout pretty with actual type
                    searchedOrders = WorkOrderDB.searchJobType(type); // get search results from db

                }
                case 4 -> { // status search
                    int counter = 1;
                    System.out.println("Job Status to Search:");
                    for (Status s : Status.values()) {
                        System.out.println(counter++ + ". " + s);
                    }
                    int status = getInt("Enter status: ", 1, Status.values().length); // off by one so use length
                    opString = "Status of " + Status.values()[status-1].toString(); // add in the status for header

                    searchedOrders = WorkOrderDB.searchStatus(status);
                }
            }

            // *******************************************************************************************
            // now loop through searchedItems to print out
            System.out.println("\n* Work Orders filtered via " + opString + " *");
            if (searchedOrders.isEmpty() || searchedOrders.get(0) == null) {
                System.out.println("* No orders found.");
            }
            else {
                for (WorkOrder wo : searchedOrders) {
                    System.out.println("\n" + wo);
                }
            }

        }

    }

    private static void changeStatus() {
        //gets a work id to change the status
        System.out.println("\n** Change Status **");
        int id = getInt("Enter work ID: ");
//searches in the DB for the id
        WorkOrder wo = WorkOrderDB.searchID(id);
//if the id isnt found itll post an error
        if (wo != null) {
            System.out.println("\n* Work Order Found *");
            System.out.println(wo+"\n");

            //gives the user the options of status'
            System.out.println("Status options");
            int counter = 1;
            for (Status s : Status.values()) {
                System.out.println(counter++ + ". " + s);
            }
            //gets the user input for what status they want it to be changed to
            int status = getInt("Enter status to change: ", 1, Status.values().length);

            if (WorkOrderDB.changeStatus(id, status)) {
                //presents a succeed message
                System.out.println("\n* Work Order Status Changed.");
            }
            else {
                //posts an error if there was a problem with changing the status
                System.out.println("\n* Error on changing status.");
            }

        }
        else {
            //posts an error if the ID is not found
            System.out.println("\n* No work order found. Check for the correct ID.");
        }
    }

    private static void addNewWorkOrder() {
        //prints an add new work order method and gets the relevant info
        System.out.println("\n** Add New Work Order **");
        int id = getInt("Enter ID: ");
        System.out.print("Enter date: ");
        String date = input.nextLine();
        System.out.print("Enter name on work order(last, first): ");
        String name = input.nextLine();
        System.out.print("Enter job description: ");
        String jobDescription = input.nextLine();
        double cost = getDouble("Enter cost: ");
        int counter = 1;
        for (JobType jt : JobType.values()) {
            System.out.println("   " + counter++ + ". " + jt);
        }
        int type = getInt("Enter type: ", 1, JobType.values().length); // off by one
//sets the status as 1 for new orders
        WorkOrder wo = new WorkOrder(id, date, name, jobDescription, cost, type, 1); // status is always 1 for new
//prints a succeed message
        if (WorkOrderDB.addWorkOrder(wo)) {
            System.out.println("\n* New Work Order added.");
        }
        else {
            //prints an error message
            System.out.println("\n* Error adding new work order.");
        }

    }
//a delete work order method
    private static void deleteWorkOrder() {
        //gets the ID of the order
        System.out.println("\n** Delete Work Order **");
        int id = getInt("Enter ID: ");
//deletes the work order
        if (WorkOrderDB.deleteWorkOrder(id)) {
            System.out.println("\n* Work Order deleted.");
        }
        else { //prints an error message
            System.out.println("\n* Error deleting work order. Check work order ID.");
        }
    }

//a get int method
    private static int getInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(input.nextLine());
            }
            catch (Exception e) {
                //prints a not valid number error
                System.out.println("Error, not a valid number");
            }
        }
    }
//works with get int
    private static int getInt(String prompt, int min, int max) {
        while (true) {
            int value = getInt(prompt);
            //makes sure the value is between a min and a max
            if (value < min || value > max) {
                //prints an error if not
                System.out.println("Error, must be between " + min + " and " + max);
                continue; // get another input
            }
            return value;
        }
    }
//getdouble method that operates like the getint method
    private static double getDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(input.nextLine());
            }
            catch (Exception e) {
                //prints an error
                System.out.println("Error, not a valid number");
            }
        }
    }
}
