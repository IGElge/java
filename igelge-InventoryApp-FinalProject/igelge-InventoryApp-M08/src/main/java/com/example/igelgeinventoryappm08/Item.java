/**
 * NAME: Isabella Elge
 * CLASS: INFO 1531
 * ASSIGNMENT: Final Project
 * DATE: 11/17/2025
 * RESOURCES: I utilized my previous file with some changes to mirror the coding example
 *
 * PURPOSE: creates the Item item with all of its attributes
 */
package com.example.igelgeinventoryappm08;

import java.io.Serializable;
//establishes what is part of the item
public class Item implements Serializable {
    private String id;
    private String name;
    private String manufacturer;
    private double price;
    private int inventory;
    private ItemType type;
    private Image image;

    public Item() {
        image = new Image();
    }

    public Item(String id, String name, String manufacturer, double price, int inventory, ItemType type) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.inventory = inventory;
        this.type = type;
        this.image = new Image();
    }

    // getters/setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String m) { this.manufacturer = m; }
    public double getPrice() { return price; }
    public void setPrice(double p) { this.price = p; }
    public int getInventory() { return inventory; }
    public void setInventory(int i) { this.inventory = i; }
    public ItemType getType() { return type; }
    public void setType(ItemType t) { this.type = t; }
    public Image getImage() { return image; }
    public void setImage(Image img) { this.image = img; }
}
