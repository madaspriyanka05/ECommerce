package com.example.aakarsh.e_commerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.aakarsh.e_commerce.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    MaterialEditText edtPhone, edtName, edtPassword;
    Button btnSignUp;
    String phone, name, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtPassword = findViewById(R.id.edtPassword);
        edtName= findViewById(R.id.edtName);
        edtPhone =findViewById(R.id.edtPhone);

        btnSignUp = findViewById(R.id.btnSignUp);

        phone = edtPhone.getText().toString();
        name = edtName.getText().toString();
        password = edtPassword.getText().toString();

        //initialise firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //check if user already exists
                        phone = edtPhone.getText().toString();
                        if(dataSnapshot.child(phone).exists())
                        {
                            Toast.makeText(SignUp.this, "User already exists...", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            phone = edtPhone.getText().toString();
                            name = edtName.getText().toString();
                            password = edtPassword.getText().toString();

                            User user = new User(name, password);
                            table_user.child(phone).setValue(user);
                            Toast.makeText(SignUp.this, "Sign Up successful !!!", Toast.LENGTH_SHORT).show();
                            finish();
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
