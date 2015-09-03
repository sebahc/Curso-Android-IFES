package com.example.clothingstore.model;

/**
 * Created by sebahc on 8/31/15.
 */
public class Clothing {

    private long ID=0;
    private String description="";
    private double price=0.0;
    private int quantityInStock=0;
    private char colorCode='N'; // R=Rojo, V=Verde, A=azul, N=NoSeteado

    public char getColorCode() {
        return colorCode;
    }

    public void setColorCode(char colorCode) {
        this.colorCode = colorCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
}
