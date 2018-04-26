package com.example.aakarsh.e_commerce.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aakarsh.e_commerce.Interface.ItemClickListener;
import com.example.aakarsh.e_commerce.R;

/**
 * Created by aakar on 3/23/2018.
 */

public class CeramicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public CeramicViewHolder(View itemView) {

        super(itemView);

        txtProductName = itemView.findViewById(R.id.ceramic_name);
        imageView = itemView.findViewById(R.id.ceramic_image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(),false);

    }
}
