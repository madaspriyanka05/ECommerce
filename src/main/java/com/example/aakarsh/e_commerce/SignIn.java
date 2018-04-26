package com.example.aakarsh.e_commerce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aakarsh.e_commerce.Common.Common;
import com.example.aakarsh.e_commerce.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity  {

    Button btnSignIn;
    EditText edtPhone, edtPassword;

    String phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnSignIn = findViewById(R.id.btnSignIn);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);

        phone = edtPhone.getText().toString();
        password = edtPassword.getText().toString();

        //initialise firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                table_user.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //check if user exists in database
                        if(dataSnapshot.child(edtPhone.getText().toString()).exists()) {

                            //get user information
                            phone = edtPhone.getText().toString();
                            User user = dataSnapshot.child(phone).getValue(User.class);

                            phone = edtPhone.getText().toString();
                            user.setPhone(phone);

                            password = edtPassword.getText().toString();
                            if (user.getPassword().equals(password)) {

                                Intent homeIntent = new Intent(SignIn.this,Home.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            }
                            else{
                                Toast.makeText(SignIn.this, "Wrong Id or Password...", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(SignIn.this, "User does not exists ... ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
