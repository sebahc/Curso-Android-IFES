package com.example.clothingweb.model;


public class Shirt extends Clothing{

    public void addShirt(String description,char colorCode,double price,int quantityInStock){
        setDescription(description);
        setColorCode(colorCode);
        setPrice(price);
        setQuantityInStock(quantityInStock);
    }
}
