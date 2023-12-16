package com.example.expensetrackerjavagroovy.model;

public class Record {
    Double amount;
    String description;
    String date;
    String type;

    public Record(Double amount, String description, String date, String type) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
