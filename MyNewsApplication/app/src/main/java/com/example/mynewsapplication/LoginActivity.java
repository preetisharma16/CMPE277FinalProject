package com.example.mynewsapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaCodec;
import android.os.Bundle;
import android.util.Patterns;
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

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        register = (TextView)findViewById(R.id.register);
        register.setOnClickListener(this);

        //initialize
        signIn = (Button)findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText)findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
       // editTextPassword = (EditText) findViewById(R.id.password);

        //Initialize mAuth
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this,Register.class));
               /* Intent intent1
                        = new Intent(LoginActivity.this,
                        Register.class);
                startActivity(intent1);*/
                break;

            case R.id.signIn:
                userLogin();
                break;
                
        }

    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is Required!! PLease provide an Email!");
            editTextEmail.requestFocus();
            return;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Enter Valid Password");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password Required");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError(("Minimum password Length is 6!"));
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //redirect to home page
                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Toast.makeText(getApplicationContext(),
                            "Login successful!!",
                            Toast.LENGTH_LONG)
                            .show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    // hide the progress bar
                    progressBar.setVisibility(View.GONE);

                    // if sign-in is successful
                    // intent to home activity
                   /* Intent intent
                            = new Intent(LoginActivity.this,
                            MainActivity.class);
                    startActivity(intent);*/

                }else{
                    Toast.makeText(LoginActivity.this, "Failed to Login! Check Credentials!",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }
}