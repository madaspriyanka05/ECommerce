package com.example.aakarsh.e_commerce;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.aakarsh.e_commerce.Database.Database;
import com.example.aakarsh.e_commerce.Model.Order;
import com.example.aakarsh.e_commerce.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetail extends AppCompatActivity {

    TextView txt_product_name, product_price, product_description;
    ImageView img_product;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Button btnCart;
    ElegantNumberButton number_button;

    String productId="";

    FirebaseDatabase database;
    DatabaseReference products;

    Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //initialise firebase
        database = FirebaseDatabase.getInstance();
        products = database.getReference("Products");

        number_button = findViewById(R.id.number_button);
        btnCart = findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        productId,
                        currentProduct.getName(),
                        number_button.getNumber(),
                        currentProduct.getPrice(),
                        currentProduct.getDiscount()
                ));

                Toast.makeText(ProductDetail.this, "Added To Cart !!", Toast.LENGTH_SHORT).show();
            }
        });

        img_product = findViewById(R.id.img_product);
        txt_product_name = findViewById(R.id.txt_product_name);
        product_description = findViewById(R.id.product_description);
        product_price = findViewById(R.id.product_price);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get foodId from intent
        if(getIntent() != null)
            productId = getIntent().getStringExtra("ProductId");
        if(!productId.isEmpty())
        {
            getDetailProduct(productId);
        }
    }

    private void getDetailProduct(String productId) {

        products.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentProduct = dataSnapshot.getValue(Product.class);

                //set Image
                Picasso.with(getBaseContext()).load(currentProduct.getImage()).into(img_product);

                collapsingToolbarLayout.setTitle(currentProduct.getName());

                product_price.setText(currentProduct.getPrice());

                txt_product_name.setText(currentProduct.getName());

                product_description.setText(currentProduct.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
