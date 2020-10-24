package com.nicholas.dotamarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class BuyItemActivity extends AppCompatActivity implements View.OnClickListener {
    ActionBar actionBar;
    TextView txtItemName, txtItemPrice, txtItemStock;
    EditText etxQty;
    TextInputLayout tilQty;
    ImageView img;
    Button btnLoc, btnCheckout;
    int itemPrice, itemStock, resID;
    Bundle b;

    private void initLayout() {
        actionBar = getSupportActionBar();
        txtItemName = findViewById(R.id.txtitemNameB);
        txtItemPrice = findViewById(R.id.txtItemPriceB);
        txtItemStock = findViewById(R.id.txtItemStockB);
        img = findViewById(R.id.imgItemB);
        etxQty = findViewById(R.id.etxQty);
        tilQty = findViewById(R.id.tilQty);
        btnLoc = findViewById(R.id.btnLocation);
        btnCheckout = findViewById(R.id.btnCheckOut);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_item);

        initLayout();

        // Set action bar back button to close
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        actionBar.setTitle("Buy Item");

        b = getIntent().getExtras();
        String itemName = b.getString("itemName");
        itemPrice = b.getInt("itemPrice", 0);
        itemStock = b.getInt("itemStock", 0);

        txtItemName.setText(itemName);
        txtItemPrice.setText("Price: Rp " + itemPrice);
        txtItemStock.setText("Stock: " + itemStock);

        resID = b.getInt("itemImg");
        img.setImageResource(resID);
        btnLoc.setOnClickListener(this);
        btnCheckout.setOnClickListener(this);
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
            case R.id.btnLocation:
                Intent i = new Intent(getApplicationContext(), SellerLocationActivity.class);
                i.putExtra("long", b.getDouble("itemLongtd", 0));
                i.putExtra("lat", b.getDouble("itemLattd", 0));
                startActivity(i);
                break;
            case R.id.btnCheckOut:
                boolean valQty = validateFilled(tilQty, etxQty);
                String userId = b.getString("userId"); // String digunakan untuk merecord transaksi
                String itemId = b.getString("itemId");
                if (valQty) {
                    Toast.makeText(this, "Transaction recorded", Toast.LENGTH_SHORT).show();
                    BuyItemActivity.this.finish();
                } else {
                    Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean validateFilled(TextInputLayout til, EditText etx) {
        String data = etx.getText().toString();
        if (etx.getText().toString().equals("")) {
            til.setError("must be filled");
            return false;
        } else if (etx.getId() == etxQty.getId()) {
            int qty = Integer.parseInt(data);
            if (qty <= 0) {
                til.setError("cannot be zero");
                return false;
            } else if (itemStock < qty) {
                til.setError("cannot be more than item stock");
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