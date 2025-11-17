/**
 * NAME: Isabella Elge
 * CLASS: INFO 1531
 * ASSIGNMENT: Final Project
 * DATE: 11/17/2025
 * RESOURCES: I utilized my previous file with some changes to mirror the coding example
 *
 * PURPOSE: Enumeration for the item type, minor changes here
 */
package com.example.igelgeinventoryappm08;

public enum ItemType {
    FOOD_DRINK, APPAREL, ACCESSORY, BOOK, SCHOOL_MATERIAL;

    @Override
    public String toString() {
        return switch (this) {
            case FOOD_DRINK -> "Food & Drink";
            case APPAREL -> "Apparel";
            case ACCESSORY -> "Accessory";
            case BOOK -> "Book";
            default -> "School Material";
        };
    }
}
