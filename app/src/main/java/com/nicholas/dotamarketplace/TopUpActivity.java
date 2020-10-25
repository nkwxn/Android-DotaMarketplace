package com.nicholas.dotamarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class TopUpActivity extends AppCompatActivity {
    ActionBar actionBar;
    TextView txtUserBalance;
    EditText etxTopUpAmt, etxPwd;
    TextInputLayout tilTopUpAmt, tilPwd;
    Button btnAddBal;
    SQLiteDBHelper dbHelper;
    long UserID;
    String usrpwd, balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        initUI();
        initDatas();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Top up");

        // User ID and balance stored here and set text in textview

        btnAddBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valTUAmt = validateFilled(tilTopUpAmt, etxTopUpAmt);
                boolean valPwd = validateFilled(tilPwd, etxPwd);
                if (valTUAmt && valPwd) {
                    // Update query goes here
                    int bal = Integer.parseInt(balance);
                    int add = Integer.parseInt(etxTopUpAmt.getText().toString());
                    ContentValues cv = new ContentValues();
                    cv.put(dbHelper.user_balance, (bal + add) + "");
                    dbHelper.updateUserBalance(cv, UserID);

                    initDatas();

                    Toast.makeText(TopUpActivity.this, "Balance has been added by Rp " + add, Toast.LENGTH_SHORT).show();
                }
            }
        });

        etxTopUpAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (tilTopUpAmt.isErrorEnabled()) {
                    validateFilled(tilTopUpAmt, etxTopUpAmt);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etxPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (tilPwd.isErrorEnabled()) {
                    validateFilled(tilPwd, etxPwd);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initUI() {
        actionBar = getSupportActionBar();
        txtUserBalance = findViewById(R.id.txtUserBalance);
        etxTopUpAmt = findViewById(R.id.etxAmount);
        etxPwd = findViewById(R.id.etxPasswordTU);
        tilTopUpAmt = findViewById(R.id.tilAmount);
        tilPwd = findViewById(R.id.tilPasswordTU);
        btnAddBal = findViewById(R.id.btnAddBalance);

        dbHelper = new SQLiteDBHelper(this);
    }

    private void initDatas() {
        UserID = getIntent().getLongExtra("user_id", 0);
        Cursor userInfo = dbHelper.getUsernameBalance(UserID);
        userInfo.moveToFirst();
        balance = userInfo.getString(1);
        usrpwd = userInfo.getString(2);
        txtUserBalance.setText("Rp " + balance);
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

    private boolean validateFilled(TextInputLayout til, EditText etx) {
        String etxData = etx.getText().toString();
        if (etxData.equals("")) {
            til.setError("must be filled");
            return false;
        } else if (etx.getId() == etxTopUpAmt.getId()) {
            if (Integer.parseInt(etxData) < 50000) {
                til.setError("must be more than equals 50000");
                return false;
            } else {
                til.setError(null);
                return true;
            }
        } else if (etx.getId() == etxPwd.getId()) {
            String pwd = etxPwd.getText().toString();
            String svdpwd = usrpwd;
            if (pwd.equals(svdpwd)) {
                til.setError(null);
                return true;
            } else {
                til.setError("not valid");
                return false;
            }
        } else {
            til.setError(null);
            return true;
        }
    }
}