package com.nicholas.dotamarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.StringTokenizer;

public class BuyItemActivity extends AppCompatActivity implements View.OnClickListener {
    CoordinatorLayout parentV;
    AppBarLayout abl;
    CollapsingToolbarLayout toolbar;
    MaterialToolbar mtb;
    NestedScrollView nsv;
    LinearLayout llFields;
    TextView txtItemName, txtItemPrice, txtItemStock, txtTotalPrice;
    EditText etxQty;
    TextInputLayout tilQty;
    ImageView img;
    Button btnLoc, btnCheckout;
    int itemPrice, itemStock, resID;
    Bundle b;

    private void initLayout() {
        parentV = findViewById(R.id.parentLayout);
        abl = findViewById(R.id.appBarLayout);
        toolbar = findViewById(R.id.toolBarItemName);
        mtb = findViewById(R.id.toolbar);
        nsv = findViewById(R.id.nsv);
        llFields = findViewById(R.id.layoutFields);
        txtItemName = findViewById(R.id.txtitemNameB);
        txtItemPrice = findViewById(R.id.txtItemPriceB);
        txtItemStock = findViewById(R.id.txtItemStockB);
        txtTotalPrice = findViewById(R.id.txtTotalPriceB);
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

        b = getIntent().getExtras();
        String itemFullName = b.getString("itemName");
        itemPrice = b.getInt("itemPrice", 0);
        itemStock = b.getInt("itemStock", 0);

        StringTokenizer namecatseparator = new StringTokenizer(itemFullName, "(");
        String itemName = namecatseparator.nextToken();
        String itemDesc = namecatseparator.nextToken();
        itemDesc = itemDesc.substring(0, itemDesc.length() - 1);

        toolbar.setTitle(itemName);
        mtb.setNavigationIcon(R.drawable.ic_close);
        mtb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtItemName.setText(itemDesc);
        txtItemPrice.setText("Price per unit: Rp " + itemPrice);
        txtItemStock.setText("Stock: " + itemStock);

        abl.getLayoutParams().height = getScreenHeight() - getHeightOfView(llFields);

        resID = b.getInt("itemImg");
        img.setImageResource(resID);
        btnLoc.setOnClickListener(this);
        btnCheckout.setOnClickListener(this);

        etxQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String qty = etxQty.getText().toString();
                int itemTotalPrice;
                if (qty.length() == 0) {
                    itemTotalPrice = 0;
                } else {
                    itemTotalPrice = itemPrice * Integer.parseInt(qty);
                }
                txtTotalPrice.setText("Rp " + itemTotalPrice);
                if (tilQty.isErrorEnabled()) {
                    validateFilled(tilQty, etxQty);
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

    private int getHeightOfView(View v) {
        v.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return v.getMeasuredHeight();
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
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