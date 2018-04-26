package com.example.aakarsh.e_commerce.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.aakarsh.e_commerce.Interface.ItemClickListener;
import com.example.aakarsh.e_commerce.Model.Order;
import com.example.aakarsh.e_commerce.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by aakar on 3/26/2018.
 */

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView cart_item_name, cart_item_price;
    public ImageView cart_item_count;

    private ItemClickListener itemClickListener;

    public void setCart_item_name(TextView cart_item_name) {
        this.cart_item_name = cart_item_name;
    }

    public CartViewHolder(View itemView) {
        super(itemView);

        cart_item_name = itemView.findViewById(R.id.cart_item_name);
        cart_item_price = itemView.findViewById(R.id.cart_item_price);
        cart_item_count = itemView.findViewById(R.id.cart_item_count);
    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Order> listData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+listData.get(position).getQuantity(), Color.RED);
        holder.cart_item_count.setImageDrawable(drawable);

        Locale locale = new Locale("en","IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
        holder.cart_item_price.setText(fmt.format(price));
        holder.cart_item_name.setText(listData.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
