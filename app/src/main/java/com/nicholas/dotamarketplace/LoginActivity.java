package com.nicholas.dotamarketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin, btnRegister;
    EditText etxUsername, etxPassword;
    TextInputLayout tilUsername, tilPassword;

    // Ini menggunakan SQLite
    SQLiteDBHelper dbHelper;
    ArrayList<ArrayList<String>> registered;
    String user_id;

    private void initComponents() {
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        etxUsername = findViewById(R.id.etxUsername);
        etxPassword = findViewById(R.id.etxPassword);
        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPassword);
        dbHelper = new SQLiteDBHelper(LoginActivity.this);
    }

    // Import all registered user IDs
    private void initDatas() {
        registered = new ArrayList<>();
        registered = dbHelper.allUsernameData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();
        initDatas();

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        // AUTO LOGIN IF ALREADY LOGGED IN BEFORE
        SharedPreferences prefs = this.getSharedPreferences("rememberLogin", MODE_PRIVATE);

        String loginID = prefs.getString("loginUsername", "");

        if (loginID.length() > 0) {
            // REDIRECT TO MAINFORM IF ALREADY LOGGED IN BEFORE
            Intent a = new Intent(getApplicationContext(), MainFormActivity.class);
            startActivity(a);
            this.finish();
        }

        etxUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (tilUsername.isErrorEnabled()) {
                    validateFilled(tilUsername, etxUsername);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etxPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (tilPassword.isErrorEnabled()) {
                    validateFilled(tilPassword, etxPassword);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                boolean usrFilled = validateFilled(tilUsername, etxUsername);
                boolean pwdFilled = validateFilled(tilPassword, etxPassword);
                if (usrFilled  && pwdFilled) {
                    // PUT USER DATA TO SHAREDPREF FOR AUTO LOGIN
                    SharedPreferences userPrefs = this.getSharedPreferences("rememberLogin", MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPrefs.edit();
                    editor.putString("loginUsername", user_id);
                    editor.commit();
                    Intent i = new Intent(getApplicationContext(), MainFormActivity.class);
                    startActivity(i);
                    this.finish();
                } else {
                    Toast.makeText(this, "Please check all the required fields!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnRegister:
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }

    private boolean validateFilled(TextInputLayout til, EditText etx) {
        String username = etxUsername.getText().toString();
        String pwd = etxPassword.getText().toString();
        boolean valusername = false,
                valpassword = false;

        // Looping untuk mendapatkan data username dan password
        for (int i = 0; i < registered.size(); i++) {
            ArrayList<String> x = registered.get(i);
            // validate username
            if (username.equals(x.get(1))) {
                valusername = true;
                // validate password after username
                if (pwd.equals(x.get(2))) {
                    valpassword = true;
                    user_id = x.get(0);
                }
                break;
            }
        }

        if (etx.getText().toString().equals("")) {
            til.setError("must be filled");
            return false;
        } else if (etx.getId() == etxUsername.getId()) {
            if (!valusername) {
                til.setError("not registered");
                return false;
            } else {
                til.setError(null);
                return true;
            }
        } else if (etx.getId() == etxPassword.getId()) {
            if (!valpassword) {
                til.setError("not valid");
                return false;
            } else {
                til.setError(null);
                return true;
            }
        } else {
            til.setError(null);
            return true;
        }
    }
}