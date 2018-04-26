package com.example.aakarsh.e_commerce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.aakarsh.e_commerce.Interface.ItemClickListener;
import com.example.aakarsh.e_commerce.Model.Product;
import com.example.aakarsh.e_commerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseDatabase database;
    DatabaseReference productList;

    String categoryId="";

    FirebaseRecyclerAdapter<Product,ProductViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        database = FirebaseDatabase.getInstance();
        productList = database.getReference("Products");

        recyclerView = findViewById(R.id.recycler_product_list);
        recyclerView.setHasFixedSize(true);
        //layoutManager = new LinearLayoutManager(this);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        //Get Intent with CategoryId
        if(getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty() && categoryId != null){
            loadListProduct(categoryId);
        }
    }

    private void loadListProduct(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class,
                R.layout.product_item,
                ProductViewHolder.class,
                productList.orderByChild("MenuId").equalTo(categoryId)) { //Query for two tables ie product and category
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {

                viewHolder.product_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.product_image);

                final Product local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View v, int position, boolean isLongClicked) {

                        //Start new activity for displaying product details
                        Intent productDetail = new Intent(ProductList.this,ProductDetail.class);
                        //send productId to new activity
                        productDetail.putExtra("ProductId",adapter.getRef(position).getKey());
                        startActivity(productDetail);
                    }
                });

            }
        };

        //set adapter
        recyclerView.setAdapter(adapter);
    }
}
