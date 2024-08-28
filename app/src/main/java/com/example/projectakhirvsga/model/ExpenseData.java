package com.example.projectakhirvsga.model;

public class ExpenseData {
    private String id;
    private String amount;
    private String category;
    private String date;

    public ExpenseData() {
    }

    public ExpenseData(String id, String amount, String category, String date) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getCategory() {return category; }
    public void setCategory(String category) {this.category = category; }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }


}
