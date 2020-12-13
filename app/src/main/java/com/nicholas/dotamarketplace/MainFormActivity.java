package com.nicholas.dotamarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainFormActivity extends AppCompatActivity {
    ActionBar actionBar;
    TextView txtLoggedInUsername, txtLoggedInBalance;
    RecyclerView rvItems;
    ArrayList<GameItem> itemsSold;
    SQLiteDBHelper dbHelper;
    RequestQueue requestQueue;
    long UserID;

    private void initComponents() {
        actionBar = getSupportActionBar();
        txtLoggedInUsername = findViewById(R.id.txtLoggedInUsername);
        txtLoggedInBalance = findViewById(R.id.txtUserBalance);
        rvItems = findViewById(R.id.rvItemsSold);
        dbHelper = new SQLiteDBHelper(this);
        requestQueue = Volley.newRequestQueue(this);
    }

    private void initDatas() {
        // Set Username and Balance to TextView
        SharedPreferences prefs = this.getSharedPreferences("rememberLogin", MODE_PRIVATE);
        UserID = Long.parseLong(prefs.getString("loginUsername", ""));
        Cursor userInfo = dbHelper.getUsernameBalance(UserID);
        userInfo.moveToFirst();
        String username = userInfo.getString(0);
        String balance = userInfo.getString(1);
        txtLoggedInUsername.setText(username);
        txtLoggedInBalance.setText("Rp " + balance);

        // Ambil data dari JSON Services ke Database SQLite
        int itemCount = dbHelper.getItemCount();
        if (itemCount == 0) {
            String URL = "https://raw.githubusercontent.com/acad600/JSONRepository/master/ISYS6203/O212-ISYS6203-RM01-00-DotaMarketplace.json";
            JsonArrayRequest jar = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            // get string names
                            JSONObject item = response.getJSONObject(i);
                            String name = item.getString("name");
                            int price = item.getInt("price");
                            int stock = item.getInt("stock");
                            double getLatitd = item.getDouble("latitude");
                            double getLongitd = item.getDouble("longitude");

                            // insert to database
                            dbHelper.insertGameItem(name, price, stock, getLatitd, getLongitd);
                            initRV();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(MainFormActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainFormActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            });
            requestQueue.add(jar);
        }
    }

    // game item from db to rv
    public void initRV() {
        itemsSold = new ArrayList<>();
        Cursor itemInfo = dbHelper.allItemsData(); // yang ini nanti aja buat data + validate json service
        while (itemInfo.moveToNext()) {
            if (itemsSold.size() == 0) {
                itemInfo.moveToFirst();
            }
            itemsSold.add(new GameItem(itemInfo.getInt(0), UserID, itemInfo.getString(1), itemInfo.getInt(2), itemInfo.getInt(3), itemInfo.getDouble(4), itemInfo.getDouble(5)));

        }
        GameItemListRVAdapter rvAdapter = new GameItemListRVAdapter(this, itemsSold);
        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvItems.setAdapter(rvAdapter);
        rvItems.setLayoutManager(sglm);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form);

        initComponents();

        rvItems.addItemDecoration(new SpacesItemDecoration(10, 0));

        // Intent Filter buat terima SMS
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(brorec, filter);
    }

    private BroadcastReceiver brorec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            SmsMessage[] smsMessages = null;

            if (!b.isEmpty()) {
                Object[] pdus = (Object[]) b.get("pdus");
                smsMessages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    String message = smsMessages[i].getMessageBody();
                    String from = smsMessages[i].getOriginatingAddress();

                    // Show notification on Toast
                    Toast.makeText(getApplicationContext(), "New SMS from " + from + "\nContents:\n" + message, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
        initRV();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_form_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miTopUp:
                Intent i = new Intent(getApplicationContext(), TopUpActivity.class);
                i.putExtra("user_id", UserID);
                startActivity(i);
                break;
            case R.id.miHistory:
                Intent j = new Intent(getApplicationContext(), TransactionHistoryActivity.class);
                j.putExtra("user_id", UserID);
                startActivity(j);
                break;
            case R.id.miLogOut:
                // GET AUTO LOGIN AND CLEAR DATA
                SharedPreferences prefs = this.getSharedPreferences("rememberLogin", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear(); //RESET
                editor.commit();
                Intent k = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(k);
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}