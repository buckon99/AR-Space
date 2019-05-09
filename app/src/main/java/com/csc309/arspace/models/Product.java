package com.csc309.arspace.models;

public class Product {

    private String title;
    private String type;
    private double width;
    private double height;
    private double length;
    private String imgURL;

    public Product(String title, String type,
                   double width, double height, double length,
                   String imgURL)
    {
        this.title = title;
        this.type = type;
        this.width = width;
        this.height = height;
        this.length = length;
        this.imgURL = imgURL;
    }

    public String getImgURL() {
        return imgURL;
    }

    public double getHeight()
    {
        return height;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public boolean equals(Product other) {
        return this.title.equals(other.title);
                /*&& this.type.equals(other.type)
                && this.height == other.height
                && this.width == other.width
                && this.length == other.length
                && this.imgURL.equals(other.imgURL);*/
    }
}
