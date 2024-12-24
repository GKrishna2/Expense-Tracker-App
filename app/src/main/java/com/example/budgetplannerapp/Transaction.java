package com.example.budgetplannerapp;

public class Transaction {

    private String vendor;
    private String date;
    private Double price;
    private String card;
    private String type;
    private String item;
    private String id;

    public Transaction() {
    }

    public Transaction(String vendor, String date, Double price, String card, String type, String item) {
        this.vendor = vendor;
        this.date = date;
        this.price = price;
        this.card = card;
        this.type = type;
        this.item = item;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getId() {
        return id;
    }

    public void setId(String ids) {
        this.id = ids;
    }
}
