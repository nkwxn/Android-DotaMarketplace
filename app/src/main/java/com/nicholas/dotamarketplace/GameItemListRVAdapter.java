package com.nicholas.dotamarketplace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class GameItemListRVAdapter extends RecyclerView.Adapter<GameItemListRVAdapter.ViewHolder> {
    public Context c;
    public ArrayList<GameItem> gameItems;

    public GameItemListRVAdapter(Context c, ArrayList<GameItem> gameItems) {
        this.c = c;
        this.gameItems = gameItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View GameItemx = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_item, parent, false);
        return new ViewHolder(GameItemx);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GameItem itemV = gameItems.get(position);
        int resID;
        final String itemFullName = itemV.getName();
        final int itemPrice = itemV.getPrice();
        final int itemStock = itemV.getStock();

        StringTokenizer namecatseparator = new StringTokenizer(itemFullName, "(");
        String itemName = namecatseparator.nextToken();
        String itemDesc = namecatseparator.nextToken();
        itemDesc = itemDesc.substring(0, itemDesc.length() - 1);

        holder.txtItemName.setText(itemName);
        holder.txtItemDesc.setText(itemDesc);
        holder.txtItemPrice.setText("Rp " + itemPrice);
        holder.txtItemQty.setText("" + itemStock);

        switch (itemFullName) {
            case "Inscribed Demon Eater (Arcana Shadow Fiend)":
                resID = R.drawable.demon_eater;
                break;
            case "Blades of Voth Domosh (Arcana Legion Commander)":
                resID = R.drawable.blades_of_voth_domosh;
                break;
            case "Maw of Eztzhok (Immortal TI7 Bloodseeker)":
                resID = R.drawable.maw_of_eztzhok;
                break;
            case "Chaos Fulcrum (Immortal TI7 Chaos Knight)":
                resID = R.drawable.chaos_fulcrum;
                break;
            case "Bitter Lineage (Immortal TI7 Troll Warlord)":
                resID = R.drawable.bitter_lineage;
                break;
            default:
                resID = R.drawable.ic_image_container;
                break;
        }
        holder.img.setImageResource(resID);
        final int resIDx = resID;

        holder.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(c, BuyItemActivity.class);
                i.putExtra("gameItem", itemV);
                i.putExtra("itemImg", resIDx);
                c.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return gameItems.size();
    }

    // Buat class ini sebelum extends RecyclerView.Adapter
    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView mcvRVA;
        ImageView img;
        TextView txtItemName, txtItemDesc, txtItemPrice, txtItemQty;
        Button btnBuy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mcvRVA = itemView.findViewById(R.id.mcvRVA);
            img = itemView.findViewById(R.id.imgIcon);
            txtItemName = itemView.findViewById(R.id.txtItemName);
            txtItemDesc = itemView.findViewById(R.id.txtItemDesc);
            txtItemPrice = itemView.findViewById(R.id.txtItemPrice);
            txtItemQty = itemView.findViewById(R.id.txtItemStock);
            btnBuy = itemView.findViewById(R.id.btnBuyItem);
        }
    }
}

