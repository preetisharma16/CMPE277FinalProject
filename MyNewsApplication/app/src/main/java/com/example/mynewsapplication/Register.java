package com.example.mynewsapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private TextView banner, registerUser;
    private EditText edTextName, edTextEmail, edTextPassword;
    private ProgressBar progressBar;
    private Button button;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        banner = (TextView) findViewById(R.id.banner);
        //banner.setOnClickListener(this);

        //initialize private variable
        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);
        //button = findViewById(R.id.registerUser);

        edTextName = (EditText) findViewById(R.id.fullName);
        edTextEmail = (EditText) findViewById(R.id.email);
        edTextPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //button.setOnClickListener(new View.OnClickListener(){

            /*@Override
            public void onClick(View v)
            {
                 registerUser();
            }

    });*/


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {

        //vallidation for all the fields
        String fullName = edTextName.getText().toString().trim();
        String email = edTextEmail.getText().toString().trim();
        String password = edTextPassword.getText().toString().trim();

        if (fullName.isEmpty()) {
            edTextName.setError("Name is required");
            edTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edTextEmail.setError("Email is required");
            edTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            edTextPassword.setError("password is required");
            edTextPassword.requestFocus();
            return;
        }
        //since firebase does not accept password less than 6
        if (password.length() < 6) {
            edTextPassword.setError("Minimum password Length is 6");
            edTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        /*mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Registration successful!",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressBar.setVisibility(View.GONE);

                            // if the user created intent to login activity
                            Intent intent
                                    = new Intent(Register.this,
                                    MainActivity.class);
                            startActivity(intent);
                        }*/
        //Updating data in firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            UserDetails userDetails = new UserDetails(fullName, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NotNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register.this, "User is registered!!!", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(Register.this, LoginActivity.class));

                                    } else {
                                        Toast.makeText(Register.this, "Registration failed! Please Try again!!", Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });

                        } else {
                            Toast.makeText(Register.this, "Failed to Register!!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });


    }


}