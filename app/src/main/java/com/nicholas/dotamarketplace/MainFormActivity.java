package com.nicholas.dotamarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
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

    private void initComponents() {
        actionBar = getSupportActionBar();
        txtLoggedInUsername = findViewById(R.id.txtLoggedInUsername);
        txtLoggedInBalance = findViewById(R.id.txtUserBalance);
        rvItems = findViewById(R.id.rvItemsSold);

        itemsSold.add(new GameItem("ITM001", "01", "Inscribed Demon Eater (Arcana Shadow Fiend)", 263300, 515, 14.604847, 13.886719));
        itemsSold.add(new GameItem("ITM002", "01", "Blades of Voth Domosh (Arcana Legion Commander)", 300300, 4, 14.604847, 13.886719));
        itemsSold.add(new GameItem("ITM003", "01", "Maw of Eztzhok (Immortal TI7 Bloodseeker)", 5500, 12, 14.604847, 13.886719));
        itemsSold.add(new GameItem("ITM004", "01", "Chaos Fulcrum (Immortal TI7 Chaos Knight)", 3500, 844, 14.604847, 13.886719));
        itemsSold.add(new GameItem("ITM005", "01", "Bitter Lineage (Immortal TI7 Troll Warlord)", 49700, 4, 14.604847, 13.886719));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form);

        initComponents();

        txtLoggedInUsername.setText(getIntent().getStringExtra("user_id"));
        GameItemListRVAdapter rvAdapter = new GameItemListRVAdapter(this, itemsSold);
        rvItems.setHasFixedSize(true);
        rvItems.setAdapter(rvAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvItems.setLayoutManager(llm);

        rvItems.addItemDecoration(new SpacesItemDecoration(dpToPx(10)));
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
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