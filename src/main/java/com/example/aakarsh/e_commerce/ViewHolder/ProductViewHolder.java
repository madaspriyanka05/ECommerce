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

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView product_name;
    public ImageView product_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ProductViewHolder(View itemView) {
        super(itemView);

        product_name = itemView.findViewById(R.id.product_name);
        product_image = itemView.findViewById(R.id.product_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(),false);

    }
}
