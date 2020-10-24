package com.nicholas.dotamarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
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
    SharedPreferences spref;
    private final String FILE_NAME = "com.nicholas.dotamarketplace.UserDatas";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        initUI();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Top up");

        // User ID and balance stored here and set text in textview

        btnAddBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valTUAmt = validateFilled(tilTopUpAmt, etxTopUpAmt);
                boolean valPwd = validateFilled(tilPwd, etxPwd);
                if (valTUAmt && valPwd) {
                    Toast.makeText(TopUpActivity.this, "Balance has been added by Rp " + etxTopUpAmt.getText().toString(), Toast.LENGTH_SHORT).show();
                    // Update query goes here
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

        spref = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
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
            String svdpwd = spref.getString("password", "");
            if (pwd.equals(svdpwd)) {
                til.setError(null);
                return true;
            } else {
                til.setError("incorrect");
                return false;
            }
        } else {
            til.setError(null);
            return true;
        }
    }
}