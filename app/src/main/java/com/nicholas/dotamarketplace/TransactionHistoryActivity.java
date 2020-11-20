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
    View llHistory, llNotFound;
    ActionBar ab;
    RecyclerView rvHistory;
    Button btnClear;
    ArrayList<TransactionHistory> tranHistory = new ArrayList<>();
    SQLiteDBHelper dbHelper;
    long userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        initUI();

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Transaction History");

        initData();

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
                        dbHelper.deleteHistory(userID);
                        initData();
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
        llHistory = findViewById(R.id.thereIsHistory);
        llNotFound = findViewById(R.id.historyNotFound);
        userID = getIntent().getLongExtra("user_id", 0);
        dbHelper = new SQLiteDBHelper(getApplicationContext());
    }

    private void initData() {
        tranHistory = dbHelper.allUserHistory(userID);

        if (tranHistory.size() == 0) {
            llNotFound.setVisibility(View.VISIBLE);
            llHistory.setVisibility(View.GONE);
        } else {
            // for the recyclerview
            TransactionHistoryListRVAdapter rvAdapter = new TransactionHistoryListRVAdapter(this, tranHistory);
            rvHistory.setHasFixedSize(true);
            rvHistory.setAdapter(rvAdapter);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rvHistory.setLayoutManager(llm);
        }
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