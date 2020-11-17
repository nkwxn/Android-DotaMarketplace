package com.nicholas.dotamarketplace;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    public static final String transaction_id = "TransactionID",
                                transaction_user_id = "UserID",
                                transaction_item_id = "ItemID",
                                transaction_quantity = "TransactionQty",
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
                transaction_quantity + " INTEGER NOT NULL, " +
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

    // method untuk mendapatkan semua data Items
    public Cursor allItemsData() {
        Cursor c = database.rawQuery("SELECT * FROM " +
                TABLE_ITEM, null);
        return c;
    }

    // method untuk mendapatkan semua data History
    public Cursor allHistoryUserData(long UserID) {
        Cursor c = database.rawQuery("SELECT * FROM " +
                TABLE_TRANSACTION + " WHERE "
                + transaction_user_id + " = " +
                UserID, null);
        return c;
    }

    // method untuk mendapatkan semua data Username + password
    public Cursor allUsernameData() {
        Cursor c = database.rawQuery("SELECT " + user_username +
                ", " + user_pwd + ", " + user_id +
                " FROM " + TABLE_USER, null);
        return c;
    }

    // method untuk mendapatkan Username dan Balance setelah user login
    public Cursor getUsernameBalance(long UserID) {
        Cursor c = database.rawQuery("SELECT " + user_username + ", " + user_balance + ", " + user_pwd +
                " FROM " + TABLE_USER + " WHERE " + user_id + " = " + UserID, null);
        return c;
    }

    // method untuk memasukkan data user (sign up)
    public void insertUserData(ContentValues cv) {
        database.insert(TABLE_USER, null, cv);
    }

    // method untuk memperbarui saldo dompet
    public void updateUserBalance(ContentValues cv, long userID) {
        database.update(TABLE_USER, cv, user_id + " = " + userID, null);
    }

    // Method untuk get item count
    public int getItemCount() {
        Cursor c = database.rawQuery("SELECT COUNT(*) " +
                "FROM " + TABLE_ITEM, null);
        c.moveToFirst();
        return c.getInt(0);
    }

    // Method utk memasukkan data items ke db
    public void insertGameItem(String itemName, int price, int stock, double lattd, double longitd) {
        ContentValues cvItem = new ContentValues();
        cvItem.put(item_name, itemName);
        cvItem.put(item_price, price);
        cvItem.put(item_stock, stock);
        cvItem.put(item_lat, lattd);
        cvItem.put(item_long, longitd);
        database.insert(TABLE_ITEM, null, cvItem);
    }

    // method untuk memasukkan data transaksi dan update stock dari inventory
    public void makeTransaction(int itemQty, long itemID, long userID) {
        // Tanggal hari ini
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance(); // get now date and time
        String trandate = sdf.format(cal.getTime());

        ContentValues cvTrans, cvItems, cvUser;
        cvTrans = new ContentValues();
        cvItems = new ContentValues();
        cvUser = new ContentValues();

        cvTrans.put(transaction_user_id, userID);
        cvTrans.put(transaction_item_id, itemID);
        cvTrans.put(transaction_quantity, itemQty);
        cvTrans.put(transaction_date, trandate);

        // update jumlah stock barang
        Cursor c = database.rawQuery("SELECT " + item_stock + ", " + item_price +
                " FROM " + TABLE_ITEM + " WHERE " + item_id + " = " + itemID, null);
        c.moveToFirst();
        int cStock = c.getInt(0);
        int updatedStock = cStock - itemQty;
        cvItems.put(item_stock, updatedStock);

        // update saldo user
        Cursor cu = database.rawQuery
                ("SELECT " + user_balance +
                " FROM " + TABLE_USER +
                " WHERE " + user_id + " = "
                + userID, null);
        cu.moveToFirst();
        int cBalance = cu.getInt(0);
        int totalPrice = c.getInt(1) * itemQty;
        int updatedBalance = cBalance - totalPrice;
        cvUser.put(user_balance, updatedBalance);

        database.insert(TABLE_TRANSACTION, null, cvTrans); // Memasukkan data transaksi
        database.update(TABLE_ITEM, cvItems, item_id + " = " + itemID, null); // memperbarui data item qty
        database.update(TABLE_USER, cvUser, user_id + " = " + userID, null); // memperbarui data saldo milik user
    }

    // method untuk menghapus semua history milik 1 user
    public void deleteHistory(long userID) {
        database.delete(TABLE_TRANSACTION, transaction_user_id + " = " + userID, null);
    }
}
