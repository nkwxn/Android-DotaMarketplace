package com.nicholas.dotamarketplace;

public class TransactionHistory {
    private String transID, userID, itemName, transDate;
    private int transQty;

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    private int itemPrice;

    public TransactionHistory(String transID, String userID, String itemName, String transDate, int transQty, int itemPrice) {
        this.transID = transID;
        this.userID = userID;
        this.itemName = itemName;
        this.transDate = transDate;
        this.transQty = transQty;
        this.itemPrice = itemPrice;
    }

    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public int getTransQty() {
        return transQty;
    }

    public void setTransQty(int transQty) {
        this.transQty = transQty;
    }
}
