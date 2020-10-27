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
    long UserID;

    private void initComponents() {
        actionBar = getSupportActionBar();
        txtLoggedInUsername = findViewById(R.id.txtLoggedInUsername);
        txtLoggedInBalance = findViewById(R.id.txtUserBalance);
        rvItems = findViewById(R.id.rvItemsSold);
        dbHelper = new SQLiteDBHelper(this);
    }

    private void initDatas() {
        // Set Username and Balance to TextView
        UserID = Long.parseLong(getIntent().getStringExtra("user_id"));
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

        itemsSold.add(new GameItem(001, UserID, "Inscribed Demon Eater (Arcana Shadow Fiend)", 263300, 515, 14.604847, 13.886719));
        itemsSold.add(new GameItem(002, UserID, "Blades of Voth Domosh (Arcana Legion Commander)", 300300, 4, 8.233237, 47.329102));
        itemsSold.add(new GameItem(003, UserID, "Maw of Eztzhok (Immortal TI7 Bloodseeker)", 5500, 12, 14.604847, 13.886719));
        itemsSold.add(new GameItem(004, UserID, "Chaos Fulcrum (Immortal TI7 Chaos Knight)", 3500, 844, 61.606396, 253.828125));
        itemsSold.add(new GameItem(005, UserID, "Bitter Lineage (Immortal TI7 Troll Warlord)", 49700, 4, 14.604847, 13.886719));

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
                i.putExtra("user_id", UserID);
                startActivity(i);
                break;
            case R.id.miHistory:
                Intent j = new Intent(getApplicationContext(), TransactionHistoryActivity.class);
                j.putExtra("user_id", UserID);
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