package com.nicholas.dotamarketplace;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public ArrayList<GameItem> allItemsData(long UserID) {
        Cursor c = database.rawQuery("SELECT * FROM " +
                TABLE_ITEM, null);
        ArrayList<GameItem> itemsSold = new ArrayList<>();
        while (c.moveToNext()) {
            if (itemsSold.size() == 0) {
                c.moveToFirst();
            }
            itemsSold.add(new GameItem(c.getInt(0), UserID, c.getString(1), c.getInt(2), c.getInt(3), c.getDouble(4), c.getDouble(5)));

        }
        return itemsSold;
    }

    public int countUserHistoryData(long UserID) {
        Cursor c = database.rawQuery("SELECT COUNT(*) FROM " +
                TABLE_TRANSACTION + " WHERE "
                + transaction_user_id + " = " +
                UserID, null);
        c.moveToFirst();
        return c.getInt(0);
    }

    // method untuk ambil user phone number
    public String userPhoneNum(long UserID){
        Cursor c = database.rawQuery("SELECT " +
                user_phone_num + " FROM " + TABLE_USER + " WHERE " +
                user_id + " = " + UserID, null);
        c.moveToFirst();
        String phoneNum = c.getString(0);
        return phoneNum;
    }

    // method untuk mendapatkan semua data History
    public ArrayList<TransactionHistory> allUserHistory(long UserID) {
        ArrayList<TransactionHistory> th = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT " + transaction_id + ", " +
                item_name + ", " + transaction_date + ", " +
                transaction_quantity + ", " + item_price +
                " FROM " + TABLE_TRANSACTION +
                " INNER JOIN " + TABLE_ITEM + " ON " +
                TABLE_ITEM + "." + item_id + " = " +
                TABLE_TRANSACTION + "." + transaction_item_id +
                " WHERE " + transaction_user_id + " = " +
                UserID, null);
        while (c.moveToNext()) {
            if (th.size() == 0) {
                c.moveToFirst();
            }
            long transID = c.getLong(0);
            String itemName = c.getString(1),
                    tranDate = c.getString(2);
            int tranQty = c.getInt(3),
                    priceperpcs = c.getInt(4);
            th.add(0, new TransactionHistory(transID, UserID, itemName,  tranDate, tranQty, priceperpcs));
        }
        return th;
    }

    // method untuk mendapatkan semua data Username + password
    public ArrayList<ArrayList<String>> allUsernameData() {
        ArrayList<ArrayList<String>> arr = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT " + user_username +
                ", " + user_pwd + ", " + user_id +
                " FROM " + TABLE_USER, null);
        ArrayList<String> user; // Urutan: UserID, Username, Password
        while (c.moveToNext()) {
            if (arr.size() == 0) {
                c.moveToFirst();
            }
            user = new ArrayList<>();
            user.add(c.getString(2));
            user.add(c.getString(0));
            user.add(c.getString(1));
            arr.add(user);
        }
        return arr;
    }

    // Method buat menu register cm buat dapetin User ID nya
    public ArrayList<String> allUsernames() {
        ArrayList<ArrayList<String>> coreData = allUsernameData();
        ArrayList<String> usernames = new ArrayList<>();
        for (int i = 0; i < coreData.size(); i++) {
            ArrayList<String> dataFromArray = coreData.get(i);
            usernames.add(dataFromArray.get(1)); // Get username
        }
        return usernames;
    }

    // method untuk mendapatkan Username dan Balance setelah user login
    public String[] getUsernameBalance(long UserID) {
        Cursor c = database.rawQuery("SELECT " + user_username + ", " + user_balance + ", " + user_pwd +
                " FROM " + TABLE_USER + " WHERE " + user_id + " = " + UserID, null);
        c.moveToFirst();
        String username = c.getString(0);
        String balance = c.getString(1);
        String password = c.getString(2);
        String[] userbalance = {username, balance, password}; // array statis String, username: index 0, balance: index 1
        return userbalance;
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
        int[] cisp = itemStockPrice(itemID);
        int cStock = cisp[0];
        int updatedStock = cStock - itemQty;
        cvItems.put(item_stock, updatedStock);

        // update saldo user
        String[] ub = getUsernameBalance(userID);
        int cBalance = Integer.parseInt(ub[1]);
        int totalPrice = cisp[1] * itemQty;
        int updatedBalance = cBalance - totalPrice;
        cvUser.put(user_balance, updatedBalance);

        database.insert(TABLE_TRANSACTION, null, cvTrans); // Memasukkan data transaksi
        database.update(TABLE_ITEM, cvItems, item_id + " = " + itemID, null); // memperbarui data item qty
        database.update(TABLE_USER, cvUser, user_id + " = " + userID, null); // memperbarui data saldo milik user
    }

    public int[] itemStockPrice(long itemID) {
        Cursor c = database.rawQuery("SELECT " + item_stock + ", " + item_price +
                " FROM " + TABLE_ITEM + " WHERE " + item_id + " = " + itemID, null);
        c.moveToFirst();
        int[] x = {c.getInt(0), c.getInt(1)}; // Index 0: Item stock, Index 1: Item price
        return x;
    }

    // method untuk menghapus semua history milik 1 user
    public void deleteHistory(long userID) {
        database.delete(TABLE_TRANSACTION, transaction_user_id + " = " + userID, null);
    }
}
