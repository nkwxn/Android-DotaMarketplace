package com.nicholas.dotamarketplace;

import android.os.Parcel;
import android.os.Parcelable;

public class GameItem implements Parcelable {
    private long itemID, userID;
    private String name;
    private int price, stock;
    private double latitude, longitude;

    public GameItem(long itemID, long userID, String name, int price, int stock, double latitude, double longitude) {
        this.itemID = itemID;
        this.userID = userID;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected GameItem(Parcel in) {
        itemID = in.readLong();
        userID = in.readLong();
        name = in.readString();
        price = in.readInt();
        stock = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<GameItem> CREATOR = new Creator<GameItem>() {
        @Override
        public GameItem createFromParcel(Parcel in) {
            return new GameItem(in);
        }

        @Override
        public GameItem[] newArray(int size) {
            return new GameItem[size];
        }
    };

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.itemID);
        parcel.writeLong(this.userID);
        parcel.writeString(this.name);
        parcel.writeInt(this.price);
        parcel.writeInt(this.stock);
        parcel.writeDouble(this.latitude);
        parcel.writeDouble(this.longitude);
    }
}
