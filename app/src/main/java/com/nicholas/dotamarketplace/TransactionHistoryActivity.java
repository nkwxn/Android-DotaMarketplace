package com.nicholas.dotamarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TransactionHistoryActivity extends AppCompatActivity {
    ActionBar ab;
    RecyclerView rvHistory;
    Button btnClear;
    ArrayList<TransactionHistory> tranHistory = new ArrayList<>();

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        initUI();

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Transaction History");

        // will be replaced with a SQLite Data
        tranHistory.add(new TransactionHistory("03", "01", "Bitter Lineage (Immortal TI7 Troll Warlord)",  "2020-10-05 11:35:01", 3, 149100));
        tranHistory.add(new TransactionHistory("02", "01", "Inscribed Demon Eater (Arcana Shadow Fiend)", "2020-09-20 15:05:33", 5, 263300));
        tranHistory.add(new TransactionHistory("01", "01", "Inscribed Demon Eater (Arcana Shadow Fiend)", "2020-09-20 13:50:05", 5, 263300));


        // for the recyclerview
        TransactionHistoryListRVAdapter rvAdapter = new TransactionHistoryListRVAdapter(this, tranHistory);

        rvHistory.setHasFixedSize(true);
        rvHistory.setAdapter(rvAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvHistory.setLayoutManager(llm);

        rvHistory.addItemDecoration(new SpacesItemDecoration(76, 1));

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TransactionHistoryActivity.this); // to show an alert interaction
                builder.setTitle("Clear Transaction History"); // title
                builder.setMessage("Are you sure to clear your transaction history? \nThis action cannot be undone!");

                // Yes button clicked
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tranHistory.clear();
                        rvHistory.setAdapter(new TransactionHistoryListRVAdapter(TransactionHistoryActivity.this, tranHistory));
                        Toast.makeText(TransactionHistoryActivity.this, "Transaction History is cleared", Toast.LENGTH_SHORT).show();
                    }
                });

                // No button clicked
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(TransactionHistoryActivity.this, "Transaction History is not cleared", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show(); // show the alert
            }
        });
    }

    private void initUI() {
        ab = getSupportActionBar();
        rvHistory = findViewById(R.id.rvHistory);
        btnClear = findViewById(R.id.btnClearHistory);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}