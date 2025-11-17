/**
 * NAME: Isabella Elge
 * CLASS: INFO 1531
 * ASSIGNMENT: Assignment 6 - Work Order Database
 * DATE:10/14/25
 * RESOURCES: I utilized the book and lecture for this weeks assignments
 *
 * PURPOSE: This is the DB file which will run the requested actions based off of the input from the OrderSystem.java file
 */

import java.sql.*;
import java.util.ArrayList;

public class WorkOrderDB {
    //establishes a connection to the database
    private static final String DB_URL = "jdbc:sqlite:1531-Assignment6-Starters/WorkOrders.db";
//throws an error if the db cant be reached
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
//a search method by ID
    public static WorkOrder searchID(int id) {
        String sql = "SELECT * FROM Jobs WHERE id = ?";
        //uses the above prepared statement and runs the query
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            //prints the result set to the console
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                //prints the work order with the names associated with the values
                return new WorkOrder(
                        rs.getInt("id"),
                        rs.getString("date"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("cost"),
                        rs.getInt("jobtype"),
                        rs.getInt("status")
                );
            }
        } catch (SQLException e) {
            //prints an error
            System.out.println("Error in searchID: " + e.getMessage());
        }
        return null;
    }
//searches the DB by name
    public static ArrayList<WorkOrder> searchName(String name) {
        ArrayList<WorkOrder> list = new ArrayList<>();
        String sql = "SELECT * FROM Jobs WHERE name = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new WorkOrder(
                        rs.getInt("id"),
                        rs.getString("date"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("cost"),
                        rs.getInt("jobtype"),
                        rs.getInt("status")
                ));
            }
        } catch (SQLException e) {
            //prints an error
            System.out.println("Error in searchName: " + e.getMessage());
        }
        return list;
    }
//searches the DB by job type
    public static ArrayList<WorkOrder> searchJobType(int jobTypeId) {
        ArrayList<WorkOrder> list = new ArrayList<>();
        String sql = "SELECT * FROM Jobs WHERE jobtype = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jobTypeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new WorkOrder(
                        rs.getInt("id"),
                        rs.getString("date"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("cost"),
                        rs.getInt("jobtype"),
                        rs.getInt("status")
                ));
            }
        } catch (SQLException e) {
            //Prints an error
            System.out.println("Error in searchJobType: " + e.getMessage());
        }
        return list;
    }
//searches for jobs based off of status
    public static ArrayList<WorkOrder> searchStatus(int statusId) {
        ArrayList<WorkOrder> list = new ArrayList<>();
        String sql = "SELECT * FROM Jobs WHERE status = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, statusId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new WorkOrder(
                        rs.getInt("id"),
                        rs.getString("date"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("cost"),
                        rs.getInt("jobtype"),
                        rs.getInt("status")
                ));
            }
        } catch (SQLException e) {
            //prints an error
            System.out.println("Error in searchStatus: " + e.getMessage());
        }
        return list;
    }
//a change status method for the DB based off of the ID
    public static boolean changeStatus(int id, int status) {
        String sql = "UPDATE Jobs SET status = ? WHERE id = ?";
        //tries to use the above statement to change it
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, status);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            //prints an error
            System.out.println("Error in changeStatus: " + e.getMessage());
            return false;
        }
    }
//a add work order method for the DB
    public static boolean addWorkOrder(WorkOrder order) {
        String sql = "INSERT INTO Jobs(id, date, name, description, cost, jobtype, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        //connects to the database and fills in the values based on their order and adds it to the DB
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, order.getId());
            stmt.setString(2, order.getDate());
            stmt.setString(3, order.getName());
            stmt.setString(4, order.getDescription());
            stmt.setDouble(5, order.getCost());
            stmt.setInt(6, order.getJobType().ordinal() + 1); // DB is 1-indexed
            stmt.setInt(7, 1); // New status
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            //prints an error
            System.out.println("Error in addWorkOrder: " + e.getMessage());
            return false;
        }
    }
//a delete statement
    public static boolean deleteWorkOrder(int id) {
        // First, check if it exists
        if (searchID(id) == null) return false;

        String sql = "DELETE FROM Jobs WHERE id = ?";
        //runs the SQL statement
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            //throws an error
            System.out.println("Error in deleteWorkOrder: " + e.getMessage());
            return false;
        }
    }
}
