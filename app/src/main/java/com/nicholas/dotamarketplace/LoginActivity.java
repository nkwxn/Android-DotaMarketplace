package com.nicholas.dotamarketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin, btnRegister;
    EditText etxUsername, etxPassword;
    TextInputLayout tilUsername, tilPassword;
    SharedPreferences spref;
    private final String FILE_NAME = "com.nicholas.dotamarketplace.UserDatas";

    private void initComponents() {
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        etxUsername = findViewById(R.id.etxUsername);
        etxPassword = findViewById(R.id.etxPassword);
        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPassword);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

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
                    String username = etxUsername.getText().toString();
                    spref = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
                    Intent i = new Intent(getApplicationContext(), MainFormActivity.class);
                    i.putExtra("loginusername", username);
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

    private boolean validateFilled(TextInputLayout til, EditText etx) {

        // SharedPreference get untuk mendapatkan data login
        spref = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        String username = etxUsername.getText().toString(),
                password = etxPassword.getText().toString();
        String savedusername = spref.getString("username", "");
        String savedpassword = spref.getString("password", "");

        if (etx.getText().toString().equals("")) {
            til.setError("must be filled");
            return false;
        } else if (etx.getId() == etxUsername.getId()) {
            boolean valusername = username.equals(savedusername);
            if (!valusername) {
                til.setError("not registered");
                return false;
            } else {
                til.setError(null);
                return true;
            }
        } else if (etx.getId() == etxPassword.getId()) {
            boolean valpassword = password.equals(savedpassword);
            if (!valpassword) {
                til.setError("incorrect");
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