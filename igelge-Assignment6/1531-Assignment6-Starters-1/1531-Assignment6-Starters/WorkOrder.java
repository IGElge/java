/**
 * NAME: Isabella Elge
 * CLASS: INFO 1531
 * ASSIGNMENT: Assignment 6 - Work Order Database
 * DATE:10/14/25
 * RESOURCES: I utilized the book and lecture for this weeks assignments
 *
 * PURPOSE: Establishes a work order to be used within the database for new order creation
 */


//creates a public class with the values for the work order
public class WorkOrder {
    private int id;
    private String date;
    private String name;
    private String description;
    private double cost;
    private JobType jobType;
    private Status status;

    //puts those values and their type to a variable name
    public WorkOrder(int id, String date, String name, String description, double cost, int jobTypeId, int statusId) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.jobType = JobType.values()[jobTypeId - 1];
        this.status = Status.values()[statusId - 1];
    }
//getters
    public int getId() { return id; }
    public String getDate() { return date; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getCost() { return cost; }
    public JobType getJobType() { return jobType; }
    public Status getStatus() { return status; }
//the status of the order
    public void setStatus(Status status) { this.status = status; }
//prints a tostring with all of the workorder information in a nice format
    @Override
    public String toString() {
        return "ID: " + id +
                "\nDate: " + date +
                "\nName: " + name +
                "\nDescription: " + description +
                "\nCost: $" + cost +
                "\nJob Type: " + jobType +
                "\nStatus: " + status;
    }
//compares workorders by their ids
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WorkOrder) {
            return this.id == ((WorkOrder) obj).id;
        }
        return false;
    }
}
