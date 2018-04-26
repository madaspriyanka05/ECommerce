package com.example.aakarsh.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.aakarsh.e_commerce.Common.Common;
import com.example.aakarsh.e_commerce.Interface.ItemClickListener;
import com.example.aakarsh.e_commerce.Model.Banner;
import com.example.aakarsh.e_commerce.Model.Category;
import com.example.aakarsh.e_commerce.ViewHolder.CeramicViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category;

    TextView txtFullName;

    RecyclerView recycler_products;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category,CeramicViewHolder> adapter;

    //set image slider
    HashMap<String,String> image_list;
    SliderLayout mSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Products");
        setSupportActionBar(toolbar);

        //Initialize database firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(Home.this,Cart.class);
                startActivity(cartIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set name of user
        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getName());

        //Load products for the user
        recycler_products = findViewById(R.id.recycler_products);
        recycler_products.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_products.setLayoutManager(layoutManager);

        loadMenu();

        //setup slider
        setUpSlider();
    }

    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<Category, CeramicViewHolder>(Category.class,R.layout.ceramic_item,CeramicViewHolder.class,category) {
            @Override
            protected void populateViewHolder(CeramicViewHolder viewHolder, Category model, int position) {
                viewHolder.txtProductName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageView);
                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View v, int position, boolean isLongClicked) {

                        //Get Category of Ceramic Product
                        Intent productList = new Intent(Home.this,ProductList.class);

                        //Send category because CategoryId is key value in this table
                        productList.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(productList);
                    }
                });
            }
        };
        recycler_products.setAdapter(adapter);
    }

    private void setUpSlider() {

        mSlider = findViewById(R.id.slider);
        image_list = new HashMap<>();

        final DatabaseReference banner = database.getReference("Banner");

        banner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapShot : dataSnapshot.getChildren())
                {
                    Banner banner = postSnapShot.getValue(Banner.class);
                    image_list.put(banner.getName()+"_"+banner.getId(),banner.getImage());
                }
                for(String key : image_list.keySet())
                {
                    String[] keySplit = key.split("_");
                    String nameOfFood = keySplit[0];
                    String idOfFood = keySplit[1];

                    //create slider
                    final TextSliderView textSliderView = new TextSliderView(getBaseContext());
                    textSliderView
                            .description(nameOfFood)
                            .image(image_list.get(key))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {

                                    Intent intent = new Intent(Home.this,ProductDetail.class);

                                    //send foodId to foodDetail
                                    intent.putExtras(textSliderView.getBundle());
                                    startActivity(intent);
                                }
                            });

                    // Add bundle
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("ProductId",idOfFood);

                    mSlider.addSlider(textSliderView);

                    //Remove after finish
                    banner.removeEventListener(this);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSlider.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(4000);
    }



    @Override
    protected void onStop(){
        super.onStop();

        mSlider.stopAutoCycle();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_products) {

        } else if (id == R.id.nav_cart) {

            Intent cartIntent = new Intent(Home.this,Cart.class);
            startActivity(cartIntent);

        } else if (id == R.id.nav_orders) {

            Intent orderIntent = new Intent(Home.this,OrderStatus.class);
            startActivity(orderIntent);

        } else if (id == R.id.nav_log_out) {

            Intent signInIntent = new Intent(Home.this,SignIn.class);
            signInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signInIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
