package com.example.aakarsh.e_commerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.aakarsh.e_commerce.Common.Common;
import com.example.aakarsh.e_commerce.Model.Request;
import com.example.aakarsh.e_commerce.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_staus);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Request");

        recyclerView = findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());
    }

    private void loadOrders(String phone) {

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {

                viewHolder.order_id.setText(adapter.getRef(position).getKey());
                viewHolder.order_status.setText(convertCodeToStatus(model.getStatus()));
                viewHolder.order_address.setText(model.getAddress());
                viewHolder.order_phone.setText(model.getPhone());
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private String convertCodeToStatus(String status) {

        if(status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "Shipping";
        else
            return "shipped";
    }
}
