package com.nicholas.dotamarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainFormActivity extends AppCompatActivity {
    ActionBar actionBar;
    TextView txtLoggedInUsername, txtLoggedInBalance;
    RecyclerView rvItems;
    ArrayList<GameItem> itemsSold = new ArrayList<>();
    SQLiteDBHelper dbHelper;

    private void initComponents() {
        actionBar = getSupportActionBar();
        txtLoggedInUsername = findViewById(R.id.txtLoggedInUsername);
        txtLoggedInBalance = findViewById(R.id.txtUserBalance);
        rvItems = findViewById(R.id.rvItemsSold);
        dbHelper = new SQLiteDBHelper(this);

        itemsSold.add(new GameItem("ITM001", "01", "Inscribed Demon Eater (Arcana Shadow Fiend)", 263300, 515, 14.604847, 13.886719));
        itemsSold.add(new GameItem("ITM002", "01", "Blades of Voth Domosh (Arcana Legion Commander)", 300300, 4, 14.604847, 13.886719));
        itemsSold.add(new GameItem("ITM003", "01", "Maw of Eztzhok (Immortal TI7 Bloodseeker)", 5500, 12, 14.604847, 13.886719));
        itemsSold.add(new GameItem("ITM004", "01", "Chaos Fulcrum (Immortal TI7 Chaos Knight)", 3500, 844, 14.604847, 13.886719));
        itemsSold.add(new GameItem("ITM005", "01", "Bitter Lineage (Immortal TI7 Troll Warlord)", 49700, 4, 14.604847, 13.886719));
    }

    private void initDatas() {
        // Set Username and Balance to TextView
        long UserID = Long.parseLong(getIntent().getStringExtra("user_id"));
        Cursor userInfo = dbHelper.getUsernameBalance(UserID);
        userInfo.moveToFirst();
        String username = userInfo.getString(0);
        String balance = userInfo.getString(1);
        txtLoggedInUsername.setText(username);
        txtLoggedInBalance.setText("Rp " + balance);

        ArrayList<GameItem> items = new ArrayList<>();
        Cursor itemInfo = dbHelper.allItemsData(); // yang ini nanti aja buat data + validate json service
        itemInfo.moveToFirst();
        while (itemInfo.moveToNext()) {
//            if ()
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form);

        initComponents();
        initDatas();

        // List View Adapter + kawan2 nya dari sini
        GameItemListRVAdapter rvAdapter = new GameItemListRVAdapter(this, itemsSold);
        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvItems.setAdapter(rvAdapter);
        rvItems.setLayoutManager(sglm);
        rvItems.addItemDecoration(new SpacesItemDecoration(10, 0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
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
                startActivity(i);
                break;
            case R.id.miHistory:
                Intent j = new Intent(getApplicationContext(), TransactionHistoryActivity.class);
                startActivity(j);
                break;
            case R.id.miLogOut:
                Intent k = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(k);
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}