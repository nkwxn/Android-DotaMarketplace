package com.nicholas.dotamarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    ActionBar actionBar;
    Button btnClear, btnRegister;
    TextView txtGenderError, txtAgreementError;
    EditText etxFullName, etxUsername, etxPwd, etxCPwd, etxPhone;
    TextInputLayout tilFullName, tilUsername, tilPwd, tilCPwd, tilPhone;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale;
    CheckBox chkAgreement;
    SQLiteDBHelper dbHelper;
    ArrayList<String> registeredUsernames = new ArrayList<>();

    private void initComponents() {
        // Initialize Action Bar's text and back button
        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.dm_register);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Initialize UI Components
        etxFullName = findViewById(R.id.etxFullName);
        etxUsername = findViewById(R.id.etxUsernameR);
        etxPwd = findViewById(R.id.etxPasswordR);
        etxCPwd = findViewById(R.id.etxConfirmPwd);
        etxPhone = findViewById(R.id.etxPhoneNumber);

        tilFullName = findViewById(R.id.tilFullName);
        tilUsername = findViewById(R.id.tilUsernameR);
        tilPwd = findViewById(R.id.tilPaswordR);
        tilCPwd = findViewById(R.id.tilConfirmPwd);
        tilPhone = findViewById(R.id.tilPhoneNumber);

        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);

        chkAgreement = findViewById(R.id.chkAgreement);
        txtGenderError = findViewById(R.id.txtGenderError);
        txtAgreementError = findViewById(R.id.txtAgreementError);
        txtGenderError.setVisibility(View.GONE);
        txtAgreementError.setVisibility(View.INVISIBLE);

        btnClear = findViewById(R.id.btnClear);
        btnRegister = findViewById(R.id.btnRegister);

        dbHelper = new SQLiteDBHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initComponents();

        btnRegister.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        rbMale.setOnClickListener(this);
        rbFemale.setOnClickListener(this);
        chkAgreement.setOnClickListener(this);

        // Cursor to get registered username data
        Cursor cUsernames = dbHelper.allUsernameData();
        cUsernames.moveToFirst();
        while (cUsernames.moveToNext()) {
            registeredUsernames.add(cUsernames.getString(0));
        }
        cUsernames.close();

        etxFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (tilFullName.isErrorEnabled()) {
                    validateFilled(tilFullName, etxFullName);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
        etxCPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (tilCPwd.isErrorEnabled()) {
                    validateFilled(tilCPwd, etxCPwd);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etxPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (tilPhone.isErrorEnabled()) {
                    validateFilled(tilPhone, etxPhone);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                boolean vName = validateFilled(tilFullName, etxFullName);
                boolean vUsername = validateFilled(tilUsername, etxUsername);
                boolean vPwd = validateFilled(tilPwd, etxPwd);
                boolean vCPwd = validateFilled(tilCPwd, etxCPwd);
                boolean vPhone = validateFilled(tilPhone, etxPhone);
                boolean genderChecked = rbMale.isChecked() || rbFemale.isChecked();
                boolean agreementChecked = chkAgreement.isChecked();
                int checkedGender = rgGender.getCheckedRadioButtonId();
                String strChkGend = "";

                if (checkedGender == -1) {
                    txtGenderError.setVisibility(View.VISIBLE);
                } else {
                    if (checkedGender == rbMale.getId()) {
                        strChkGend = rbMale.getText().toString();
                    }
                    if (checkedGender == rbFemale.getId()) {
                        strChkGend = rbFemale.getText().toString();
                    }
                }
                if (!agreementChecked) {
                    txtAgreementError.setVisibility(View.VISIBLE);
                }
                // When user clicks the register button and everything validated as true, save and redir to login
                if (vName && vUsername && vPwd && vCPwd && vPhone && genderChecked && agreementChecked) {
                    String name = etxFullName.getText().toString(),
                            gender = strChkGend,
                            username = etxUsername.getText().toString(),
                            password = etxPwd.getText().toString(),
                            phone_number = etxPhone.getText().toString();

                    // Memasukkan data user kedalam sqlite database
                    ContentValues cvUser = new ContentValues();
                    cvUser.put(dbHelper.user_name, name);
                    cvUser.put(dbHelper.user_username, username);
                    cvUser.put(dbHelper.user_pwd, password);
                    cvUser.put(dbHelper.user_phone_num, phone_number);
                    cvUser.put(dbHelper.user_gender, gender);
                    cvUser.put(dbHelper.user_balance, 0);
                    dbHelper.insertUserData(cvUser);
                    Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show();
                    this.finish();
                } else {
                    Toast.makeText(this, "Please check all the required fields!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnClear:
                txtGenderError.setVisibility(View.GONE);
                txtAgreementError.setVisibility(View.INVISIBLE);
                rgGender.clearCheck();
                chkAgreement.setChecked(false);
                chkAgreement.setChecked(false);
                etxFullName.setText("");
                tilFullName.setError(null);
                etxUsername.setText("");
                tilUsername.setError(null);
                etxPwd.setText("");
                tilPwd.setError(null);
                etxCPwd.setText("");
                tilCPwd.setError(null);
                etxPhone.setText("");
                tilPhone.setError(null);
                break;
            case R.id.rbMale:
            case R.id.rbFemale:
                txtGenderError.setVisibility(View.GONE);
                break;
            case R.id.chkAgreement:
                txtAgreementError.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private boolean validateFilled(TextInputLayout til, EditText etx) {
        if (etx.getText().toString().equals("")) {
            til.setError("must be filled");
            return false;
        } else if (etx.getId() == etxFullName.getId()) {
            String fullName = etx.getText().toString();
            StringTokenizer tokenizer = new StringTokenizer(fullName, " ");
            int tokenCount = tokenizer.countTokens();
            if (tokenCount < 2) {
                til.setError("must be consists of 2 words or more");
                return false;
            } else  {
                til.setError(null);
                return true;
            }
        } else if (etx.getId() == etxUsername.getId()) {
            String username = etx.getText().toString();
            til.setErrorEnabled(true);
            boolean valUsrnmLength = username.length() < 5 || username.length() > 25;
            boolean usrnContainSpace = username.contains(" ");
            boolean usrnRegistered = false;

            // Looping dengan username terdaftar di SQLite Database
            for (int i = 0; i < registeredUsernames.size(); i++) {
                if (username.equals(registeredUsernames.get(i))) {
                    usrnRegistered = true;
                    break;
                }
            }

            if (valUsrnmLength) {
                til.setError("must be between 5 and 25 characters long");
                return false;
            } else if (usrnContainSpace) {
                til.setError("cannot contain space");
                return false;
            } else if (usrnRegistered) {
                tilUsername.setError("has to be unique");
                return false;
            } else {
                til.setError(null);
                return true;
            }
        } else if (etx.getId() == etxPwd.getId()) {
            String pass = etx.getText().toString();
            boolean lengthVal = pass.length() > 15;
            boolean containUppercase = false;
            boolean containLowerCase = false;
            boolean containSpChar = false;
            boolean containNumVal = false;
            String errTxt = "";
            String specialChars = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?"; // Ini bukan regex ya
            for (int i = 0; i < pass.length(); i++) {
                char current = pass.charAt(i);
                if (Character.isDigit(current)) {
                    containNumVal = true;
                } else if (Character.isUpperCase(current)) {
                    containUppercase = true;
                } else if (Character.isLowerCase(current)) {
                    containLowerCase = true;
                } else if (specialChars.contains(String.valueOf(current))) {
                    containSpChar = true;
                }
            }
            if (lengthVal) {
                errTxt = "must be less than 15 characters";
            } else if (!containUppercase) {
                errTxt = "must contains at least 1 uppercase character";
            } else if (!containLowerCase) {
                errTxt = "must contains at least 1 lowercase character";
            } else if (!containSpChar) {
                errTxt = "must contains at least 1 special character";
            } else if (!containNumVal) {
                errTxt = "must contains at least 1 numeric value";
            } else {
                errTxt = null;
            }
            til.setError(errTxt);
            if (!lengthVal && containUppercase && containLowerCase && containSpChar && containNumVal) {
                return true;
            } else {
                return false;
            }
        } else if (etx.getId() == etxCPwd.getId()) {
            String pass1 = etxPwd.getText().toString();
            String passC = etx.getText().toString();
            boolean pwdEqualCPwd = pass1.equals(passC);
            if (pwdEqualCPwd) {
                til.setError(null);
                return true;
            } else {
                til.setError("must be same as password");
                return false;
            }
        } else if (etx.getId() == etxPhone.getId()) {
            String phoneNum = etxPhone.getText().toString();
            String errText = "";
            boolean phoneIsNumeric = false;
            boolean phoneBegins62 = phoneNum.startsWith("+62");
            boolean phoneMoreEq12D = phoneNum.length() >= 12;
            for (int i = 0; i < phoneNum.length(); i++) {
                char c = phoneNum.charAt(i);
                if (Character.isDigit(c) || c == 43) {
                    phoneIsNumeric = true;
                }
            }
            if (!phoneIsNumeric) {
                errText = "must be numeric";
            } else if (!phoneBegins62) {
                errText = "must begin with +62";
            } else if (!phoneMoreEq12D) {
                errText = "must be more than equals 12 digits";
            } else {
                errText = null;
            }
            til.setError(errText);
            if (phoneIsNumeric && phoneBegins62 && phoneMoreEq12D) {
                return true;
            } else {
                return false;
            }
        } else {
            til.setError(null);
            return true;
        }
    }
}