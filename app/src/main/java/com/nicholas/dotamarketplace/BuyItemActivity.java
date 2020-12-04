package com.nicholas.dotamarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.widget.NestedScrollView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
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
    SQLiteDBHelper dbHelper;
    String itemFullName;
    int itemPrice, itemStock, resID;
    double latd, longtd;
    long userId, itemId;
    GameItem gitem;

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
        dbHelper = new SQLiteDBHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_item);

        initLayout();

        gitem = getIntent().getParcelableExtra("gameItem");
        itemFullName = gitem.getName();
        itemPrice = gitem.getPrice();
        itemStock = gitem.getStock();
        longtd = gitem.getLongitude();
        latd = gitem.getLatitude();
        userId = gitem.getUserID();
        itemId = gitem.getItemID();

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

        resID = getIntent().getIntExtra("itemImg", 0);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLocation:
                Intent i = new Intent(getApplicationContext(), SellerLocationMapsActivity.class);
                i.putExtra("long", longtd);
                i.putExtra("lat", latd);
                startActivity(i);
                break;
            case R.id.btnCheckOut:
                boolean valQty = validateFilled(tilQty, etxQty);
                if (valQty) {
                    int purchaseQty = Integer.parseInt(etxQty.getText().toString());
                    dbHelper.makeTransaction(purchaseQty, itemId, userId);
                    Toast.makeText(this, "Transaction recorded", Toast.LENGTH_SHORT).show();

                    String title = "Purchase Transaction";
                    String message = "Your transaction for " + purchaseQty + " " + itemFullName + " with a total price of Rp " + (purchaseQty * itemPrice) + " has been recorded.";
                    NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), TransactionHistoryActivity.class);
                    intent.putExtra("user_id", userId);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                    if(Build.VERSION.SDK_INT >= 26) {
                        // Ketika versi SDK diatas sama dengan 26 (Android 8.0 Oreo)
                        String id = "dm_1";
                        String desc = "Transaction";
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel channel = new NotificationChannel(id, desc, importance);
                        channel.enableVibration(true);
                        manager.createNotificationChannel(channel);
                        Notification notification = new Notification.Builder(getApplicationContext(), id)
                                .setCategory(Notification.CATEGORY_MESSAGE)
                                .setSmallIcon(R.drawable.ic_dm_notif)
                                .setContentTitle(title)
                                .setContentText(message)
                                .setStyle(new Notification.BigTextStyle().bigText(message + ""))
                                .setContentIntent(pendingIntent)
                                .build();
                        manager.notify(1, notification);
                    } else {
                        // Ketika versi SDK di bawah 26 (Android 8.0 Oreo)
                        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                                .setCategory(Notification.CATEGORY_MESSAGE)
                                .setSmallIcon(R.drawable.ic_dm_notif)
                                .setContentTitle(title)
                                .setContentText(message)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(message + ""))
                                .setContentIntent(pendingIntent)
                                .build();
                        manager.notify(1, notification);
                    }
                    BuyItemActivity.this.finish();
                }
                break;
        }
    }

    private boolean validateFilled(TextInputLayout til, EditText etx) {
        String data = etx.getText().toString();
        Cursor cvaluserbal = dbHelper.getUsernameBalance(userId);
        cvaluserbal.moveToFirst();
        int currentBal = cvaluserbal.getInt(1);
        if (etx.getText().toString().equals("")) {
            til.setError("must be filled");
            return false;
        } else if (etx.getId() == etxQty.getId()) {
            int purchaseqty = Integer.parseInt(data);
            int totalPrice = itemPrice * purchaseqty;
            boolean vuserbalance = totalPrice > currentBal;
            if (purchaseqty <= 0) {
                til.setError("cannot be zero");
                return false;
            } else if (itemStock < purchaseqty) {
                til.setError("cannot be more than item stock");
                return false;
            } else if (vuserbalance) {
                til.setError("insufficient balance");
                Toast.makeText(this, "Balance: Rp " + currentBal + "\nInsufficient Balance", Toast.LENGTH_SHORT).show();
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