package com.example.denis.loginui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class forgotPsw extends AppCompatActivity {

    Intent tStart;

    Button forgot;
    Button back;

    EditText email;

    TextView errorEmail;
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_psw);

        tStart= getIntent();

        forgot = (Button) findViewById(R.id.btnForgotF);
        back = (Button) findViewById(R.id.btnBack);

        email = (EditText) findViewById(R.id.edtEmailF);

        errorEmail = (TextView) findViewById(R.id.txtErrorEmailF);
        info = (TextView) findViewById(R.id.txtInfoForgot);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                errorEmail.setVisibility(View.GONE);
                info.setText("If you forgot your acoount's password, insert the corresponding Email address and we will provide a reset one.\n");
                String emailStr = email.getText().toString();

                Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
                Matcher matcher = pattern.matcher(emailStr);
                if(!matcher.matches() /* oppure se gia in uso */){
                    errorEmail.setVisibility(View.VISIBLE);
                }else{
                    //controlla se la mail è nel DB

                    //se non lo è mette errorEmail.setVisibility(View.VISIBLE);


                    //se lo è manda la mail e mette info.setText("We sent to the given Email a message containing its corresponding reset password !");
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();;
            }
        });
    }
}
