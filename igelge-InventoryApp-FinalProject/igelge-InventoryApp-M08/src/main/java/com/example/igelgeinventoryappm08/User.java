/**
 * NAME: Isabella Elge
 * CLASS: INFO 1531
 * ASSIGNMENT: Final Project
 * DATE: 11/17/2025
 * RESOURCES: I utilized my previous file with some changes to mirror the coding example
 *
 * PURPOSE: creates the User item with all of its attributes
 */
package com.example.igelgeinventoryappm08;

import java.io.Serializable;
//establishes the user item with 3 important fields
public class User implements Serializable {
    private String username;
    private String password;
    private UserType role;

    public User() {}

    public User(String username, String password, UserType role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
//getters and setters
    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }

    public String getPassword() { return password; }
    public void setPassword(String p) { this.password = p; }

    public UserType getRole() { return role; }
    public void setRole(UserType r) { this.role = r; }
}
