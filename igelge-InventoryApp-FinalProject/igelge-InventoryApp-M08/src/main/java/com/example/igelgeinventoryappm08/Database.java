/***
 * NAME:Isabella Elge
 * CLASS: INFO 1531
 * ASSIGNMENT: Final Project
 * DATE: 11/17/2025
 * RESOURCES: Previous lecture, slides, coding examples, textbook, and the following website
 * https://teamtreehouse.com/community/connect-to-a-database-by-loading-the-driver-contained-in-orgsqlitejdbc-2
 *
 * PURPOSE: This file creates the database and plants information into it for users to pull from
 * it also allows users to add their own items and create sessions when logging in.
 */

package com.example.igelgeinventoryappm08;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Database {
//establish the DB location
    private static final String DB_URL = "jdbc:sqlite:bookstoreInventory.db";

    public Database() {
        // Explicitly load the SQLite JDBC driver, because it wasn't working the other way
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found!");
            e.printStackTrace();
        }
        initializeDatabase();
    }
//establish a connection with the DB
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
//Add tables
    private void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS items (
                    id TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    manufacturer TEXT,
                    price REAL NOT NULL,
                    inventory INTEGER,
                    type TEXT NOT NULL,
                    image_name TEXT,
                    image_content BLOB
                );
            """);

            // Add manufacturer column if missing (had issues when launching)
            try {
                stmt.execute("ALTER TABLE items ADD COLUMN manufacturer TEXT");
            } catch (SQLException e) {
                if (!e.getMessage().contains("duplicate column name")) {
                    throw e;
                }
            }

            // Add inventory column if missing (had issues when launching)
            try {
                stmt.execute("ALTER TABLE items ADD COLUMN inventory INTEGER");
            } catch (SQLException e) {
                if (!e.getMessage().contains("duplicate column name")) {
                    throw e;
                }
            }

            // Create users table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    username TEXT PRIMARY KEY,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL
                );
            """);

            // Create user_sessions table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS user_sessions (
                    username TEXT,
                    session_id TEXT,
                    created_at INTEGER,
                    FOREIGN KEY(username) REFERENCES users(username)
                );
            """);

            // Seed default users
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS c FROM users")) {
                if (rs.next() && rs.getInt("c") == 0) {
                    stmt.execute("INSERT INTO users(username, password, role) VALUES('admin','admin123','ADMIN')");
                    stmt.execute("INSERT INTO users(username, password, role) VALUES('manager','manager123','MANAGER')");
                    stmt.execute("INSERT INTO users(username, password, role) VALUES('user','user123','REGULAR')");
                }
            }

            // Seed default items
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS c FROM items")) {
                if (rs.next() && rs.getInt("c") == 0) {
                    stmt.execute("INSERT INTO items(id,name,manufacturer,price,inventory,type) VALUES('1','Coca Cola','Coca‑Cola Co',1.99,50,'FOOD_DRINK')");
                    stmt.execute("INSERT INTO items(id,name,manufacturer,price,inventory,type) VALUES('2','MCC Hoodie','MCC Brand',39.99,20,'APPAREL')");
                    stmt.execute("INSERT INTO items(id,name,manufacturer,price,inventory,type) VALUES('3','JanSport Backpack','JanSport',29.99,15,'SCHOOL_MATERIAL')");
                }
            }
//print an exception if an issue occurs
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//adds all the attributes to the Item
    public boolean createItem(Item item) throws SQLException {
        String sql = "INSERT INTO items(id, name, manufacturer, price, inventory, type, image_name, image_content) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getId());
            ps.setString(2, item.getName());
            ps.setString(3, item.getManufacturer());
            ps.setDouble(4, item.getPrice());
            ps.setInt(5, item.getInventory());
            ps.setString(6, item.getType().name());
            if (item.getImage() != null) {
                ps.setString(7, item.getImage().getName());
                ps.setBytes(8, item.getImage().getContents());
            } else {
                ps.setString(7, null);
                ps.setBytes(8, null);
            }
            return ps.executeUpdate() == 1;
        }
    }
//gets all items from the database
    public Map<String, Item> getAllItems() throws SQLException {
        Map<String, Item> map = new LinkedHashMap<>();
        String sql = "SELECT * FROM items ORDER BY name";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Item it = mapResultSetToItem(rs);
                map.put(it.getId(), it);
            }
        }
        return map;
    }
//gets id by its ID
    public Item getItemById(String id) throws SQLException {
        String sql = "SELECT * FROM items WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResultSetToItem(rs);
            }
        }
        return null;
    }
//updates the item by providing the same screen as the add item
    public boolean updateItem(Item item) throws SQLException {
        String sql = "UPDATE items SET name=?, manufacturer=?, price=?, inventory=?, type=?, image_name=?, image_content=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getManufacturer());
            ps.setDouble(3, item.getPrice());
            ps.setInt(4, item.getInventory());
            ps.setString(5, item.getType().name());
            if (item.getImage() != null) {
                ps.setString(6, item.getImage().getName());
                ps.setBytes(7, item.getImage().getContents());
            } else {
                ps.setString(6, null);
                ps.setBytes(7, null);
            }
            ps.setString(8, item.getId());
            return ps.executeUpdate() == 1;
        }
    }
//allows users to delete items from the db
    public boolean deleteItem(String id) throws SQLException {
        String sql = "DELETE FROM items WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() == 1;
        }
    }
//search string for items in the database
    public Map<String, Item> searchItemsByName(String q) throws SQLException {
        Map<String, Item> map = new LinkedHashMap<>();
        String sql = "SELECT * FROM items WHERE LOWER(name) LIKE ? OR LOWER(manufacturer) LIKE ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String p = "%" + q.toLowerCase() + "%";
            ps.setString(1, p);
            ps.setString(2, p);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Item it = mapResultSetToItem(rs);
                    map.put(it.getId(), it);
                }
            }
        }
        return map;
    }
//filters items by type using a dropdown
    public Map<String, Item> filterItemsByType(ItemType type) throws SQLException {
        Map<String, Item> map = new LinkedHashMap<>();
        String sql = "SELECT * FROM items WHERE type = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Item it = mapResultSetToItem(rs);
                    map.put(it.getId(), it);
                }
            }
        }
        return map;
    }
//creates a user session
    public boolean createSession(String username, String sessionId) throws SQLException {
        String sql = "INSERT INTO user_sessions(username, session_id, created_at) VALUES(?,?,?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, sessionId);
            ps.setLong(3, System.currentTimeMillis());
            return ps.executeUpdate() == 1;
        }
    }
//deletes the previously created session if called
    public boolean removeSession(String sessionId) throws SQLException {
        String sql = "DELETE FROM user_sessions WHERE session_id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sessionId);
            return ps.executeUpdate() >= 1;
        }
    }
//allows administrators to get the user by session without knowing the user id
    public User getUserBySession(String sessionId) throws SQLException {
        String sql = "SELECT u.* FROM users u JOIN user_sessions s ON u.username = s.username WHERE s.session_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("username"), rs.getString("password"), UserType.valueOf(rs.getString("role")));
                }
            }
        }
        return null;
    }
//allows users to sign in
    public User authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("username"), rs.getString("password"), UserType.valueOf(rs.getString("role")));
                }
            }
        }
        return null;
    }
//prints the Item with all of the below attributes
    private Item mapResultSetToItem(ResultSet rs) throws SQLException {
        Item i = new Item();
        i.setId(rs.getString("id"));
        i.setName(rs.getString("name"));
        i.setManufacturer(rs.getString("manufacturer"));
        i.setPrice(rs.getDouble("price"));
        i.setInventory(rs.getInt("inventory"));
        i.setType(ItemType.valueOf(rs.getString("type")));
        byte[] image = rs.getBytes("image_content");
        if (image != null) {
            Image img = new Image();
            img.setName(rs.getString("image_name"));
            img.setContents(image);
            i.setImage(img);
        }
        return i;
    }
}
