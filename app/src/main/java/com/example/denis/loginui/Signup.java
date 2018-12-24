package com.example.denis.loginui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Signup extends AppCompatActivity {

    Intent tStart;

    EditText email;
    EditText username;
    EditText pass;
    EditText cPass;

    Button signup;
    Button login;
    Button forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tStart= getIntent();

        email = (EditText) findViewById(R.id.edtEmail);
        username = (EditText) findViewById(R.id.edtUsername);
        pass = (EditText) findViewById(R.id.edtPassword);
        cPass = (EditText) findViewById(R.id.edtConfirmPassword);

        login= (Button) findViewById(R.id.btnLoginS);
        signup= (Button) findViewById(R.id.btnSignupS);
        forgot = (Button) findViewById(R.id.btnForgotS);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString();
                String usernameStr = username.getText().toString();
                String passStr = pass.getText().toString();
                String cPassStr = cPass.getText().toString();

                //controllo formato della mail e se esiste
                //controllo se username esiste
                //controllo se password rispetta requisiti
                //controllo se cPass è uguale a pass

                //se ci sono errori attivo le textview errori in base al caso

                //se tutto va bene attivo textview successfully
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mi porta indietro alla activity di login
                finish();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mi porta alla activity di forget
            }
        });

    }
}
