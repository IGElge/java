/**
 * NAME: Isabella Elge
 * CLASS: INFO 1531
 * ASSIGNMENT: Final Project
 * DATE: 11/17/2025
 * RESOURCES: I utilized the coding example version of this file
 *
 * PURPOSE: Processes images
 */
package com.example.igelgeinventoryappm08;

import java.util.Base64;

public class Image {
    private String name;
    private byte[] contents;
    private String base64Image;

    public Image() {
        name = "";
        contents = new byte[0];
        base64Image = "";
    }

    public String getName() { return name; }
    public byte[] getContents() { return contents; }
    public String getBase64Image() { return base64Image; }

    public void setName(String name) { this.name = name; }

    public void setContents(byte[] contents) {
        this.contents = contents;
        this.base64Image = Base64.getEncoder().encodeToString(contents);
    }
}
