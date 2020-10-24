package com.nicholas.dotamarketplace;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    // Nama Database dan Tabel
    public static final String DB_NAME = "db_dotamarketplace",
                                TABLE_USER = "User",
                                TABLE_ITEM = "Item",
                                TABLE_TRANSACTION = "UserItemTransaction";

    // Nama column untuk tabel user
    public static final String user_id = "UserID",
                                user_name = "Name",
                                user_username = "UserName",
                                user_pwd = "Password",
                                user_phone_num = "PhoneNumber",
                                user_gender = "Gender",
                                user_balance = "Balance";

    // Nama column untuk tabel transaction
    public static final String transaction_id = "UserID",
                                transaction_user_id = "UserID",
                                transaction_item_id = "ItemID",
                                transaction_date = "TransactionDate";

    // Nama column untuk tabel transaction
    public static final String item_id = "ItemID",
                                item_name = "Name",
                                item_price = "Price",
                                item_stock = "Stock",
                                item_lat = "Latitude",
                                item_long = "Longitude";

    private SQLiteDatabase database;

    // Constructor class SQLiteDBHelper
    public SQLiteDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 3);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qUser, qItem, qTrans;

        // Query create table user
        qUser = "CREATE TABLE " + TABLE_USER + " (" +
                user_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                user_name + " TEXT NOT NULL, " +
                user_username + " TEXT NOT NULL, " +
                user_pwd + " TEXT NOT NULL, " +
                user_phone_num + " TEXT NOT NULL, " +
                user_gender + " TEXT NOT NULL, " +
                user_balance + " INTEGER NOT NULL)";

        // Query create table item
        qItem = "CREATE TABLE " + TABLE_ITEM + " (" +
                item_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                item_name + " TEXT NOT NULL, " +
                item_price + " INTEGER NOT NULL, " +
                item_stock + " INTEGER NOT NULL, " +
                item_lat + " REAL NOT NULL, " +
                item_long + " REAL NOT NULL)";

        // Query create table transaction
        qTrans = "CREATE TABLE " + TABLE_TRANSACTION + " (" +
                transaction_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                transaction_user_id + " INTEGER NOT NULL, " +
                transaction_item_id + " INTEGER NOT NULL, " +
                transaction_date + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + transaction_user_id + ") " +
                "REFERENCES " + TABLE_USER + " (" + user_id + "), " +
                "FOREIGN KEY (" + transaction_item_id + ") " +
                "REFERENCES " + TABLE_ITEM + " (" + item_id + "))";

        sqLiteDatabase.execSQL(qUser);
        sqLiteDatabase.execSQL(qItem);
        sqLiteDatabase.execSQL(qTrans);
    }

    // Ketika versi SQLite di upgrade, drop semua table
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
    }

    // method untuk memanggil semua data Items
    public Cursor allItemsData() {
        Cursor c = database.rawQuery("SELECT * FROM " +
                TABLE_ITEM, null);
        return c;
    }

    // method untuk memanggil semua data History
    public Cursor allHistoryUserData(long UserID) {
        Cursor c = database.rawQuery("SELECT * FROM " +
                TABLE_TRANSACTION + " WHERE "
                + transaction_user_id + " = " +
                UserID, null);
        return c;
    }

    // method untuk memasukkan data user (sign up)
    public void insertUserData(ContentValues cv) {
        database.insert(TABLE_USER, null, cv);
    }

    // method untuk memasukkan data transaksi dan update stock dari inventory
    public void makeTransaction(ContentValues cvTrans, ContentValues cvItems, ContentValues cvUser, long itemID, long userID) {
        database.insert(TABLE_TRANSACTION, null, cvTrans); // Memasukkan data transaksi
        database.update(TABLE_ITEM, cvItems, item_id + " = " + itemID, null); // memperbarui data item qty
        database.update(TABLE_USER, cvUser, user_id + " = " + userID, null); // memperbarui data saldo milik user
    }

    // method untuk menghapus semua history milik 1 user
    public void deleteHistory(long userID) {
        database.delete(TABLE_TRANSACTION, transaction_user_id + " = " + userID, null);
    }
}
