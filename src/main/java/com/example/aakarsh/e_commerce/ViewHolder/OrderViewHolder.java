package com.example.aakarsh.e_commerce.ViewHolder;

import android.content.ClipData;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.aakarsh.e_commerce.Interface.ItemClickListener;
import com.example.aakarsh.e_commerce.R;

/**
 * Created by aakar on 3/26/2018.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView order_id, order_status, order_phone, order_address;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        order_address = itemView.findViewById(R.id.order_address);
        order_id = itemView.findViewById(R.id.order_id);
        order_status = itemView.findViewById(R.id.order_status);
        order_phone = itemView.findViewById(R.id.order_phone);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
