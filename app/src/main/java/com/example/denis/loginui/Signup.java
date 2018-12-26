package com.example.denis.loginui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    public static final int PASSWORD_LENGTH = 8;

    Intent tStart;
    Intent tForgot;

    EditText email;
    EditText username;
    EditText pass;
    EditText cPass;

    TextView errorEmail;
    TextView errorUsername;
    TextView errorPassword;
    TextView errorConfirmPassword;
    TextView successfulMsg;

    Button signup;
    Button login;
    Button forgot;

    Boolean successful;
    Boolean showPass;
    Boolean showConfPsw;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tStart= getIntent();

        email = (EditText) findViewById(R.id.edtEmail);
        username = (EditText) findViewById(R.id.edtUsername);
        pass = (EditText) findViewById(R.id.edtPassword);
        cPass = (EditText) findViewById(R.id.edtConfirmPassword);

        errorEmail = (TextView) findViewById(R.id.txtErrorEmail);
        errorUsername = (TextView) findViewById(R.id.txtErrorUsername);
        errorPassword = (TextView) findViewById(R.id.txtErrorPassword);
        errorConfirmPassword = (TextView) findViewById(R.id.txtErrorConfirmPassword);
        successfulMsg = (TextView) findViewById(R.id.txtSignupSuccessful);

        login= (Button) findViewById(R.id.btnLoginS);
        signup= (Button) findViewById(R.id.btnSignupS);
        forgot = (Button) findViewById(R.id.btnForgotS);

        showPass=false;
        showConfPsw=false;


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString();
                String usernameStr = username.getText().toString();
                String passStr = pass.getText().toString();
                String cPassStr = cPass.getText().toString();

                errorEmail.setVisibility(View.GONE);
                errorUsername.setVisibility(View.GONE);
                errorPassword.setVisibility(View.GONE);
                errorConfirmPassword.setVisibility(View.GONE);
                successfulMsg.setVisibility(View.GONE);

                successful = true;

                //se ci sono errori attivo le textview errori in base al caso
                //controllo formato della mail e se esiste
                Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
                Matcher matcher = pattern.matcher(emailStr);
                if(!matcher.matches() /* oppure se gia in uso */){
                    errorEmail.setVisibility(View.VISIBLE);
                    successful=false;
                }

                //controllo se username esiste
                if(!is_Valid_Usrrname(usernameStr)/*oppure username gia un uso?*/){
                    errorUsername.setVisibility(View.VISIBLE);
                    successful=false;
                }

                //controllo se password rispetta requisiti
                if(!is_Valid_Password(passStr)){
                    errorPassword.setVisibility(View.VISIBLE);
                    successful=false;
                }

                //controllo se cPass è uguale a pass
                if(!passStr.equals(cPassStr)){
                    errorConfirmPassword.setVisibility(View.VISIBLE);
                    successful=false;
                }

                //se tutto va bene attivo textview successfully e caricon nel DB i dati
                if(successful){
                    successfulMsg.setVisibility(View.VISIBLE);
                    //carica dati su DB
                }
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
                tForgot = new Intent(Signup.this, forgotPsw.class);
                startActivity(tForgot);
            }
        });

        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (pass.getRight() - pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if(showPass){
                            pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_off, 0);
                            showPass=false;
                            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            return true;
                        }else{
                            pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_on, 0);
                            showPass=true;
                            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            return false;
                        }


                    }
                }

                return false;
            }
        });

        cPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (cPass.getRight() - cPass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if(showConfPsw){
                            cPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_off, 0);
                            showConfPsw=false;
                            cPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            return true;
                        }else{
                            cPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_on, 0);
                            showConfPsw=true;
                            cPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            return false;
                        }


                    }
                }

                return false;
            }
        });

    }

    //controllo se username è valido
    private static boolean is_Valid_Usrrname(String username) {

        if (username.length() == 0) return false;

        boolean notSpecial= true ;

        int i=0;

        while(notSpecial && i < username.length()) {

            char ch = username.charAt(i);

            if (is_Special(ch)) notSpecial=false;

            i++;
        }


        return (notSpecial);
    }


    //controllo se password è valida
    private static boolean is_Valid_Password(String password) {

        if (password.length() < PASSWORD_LENGTH) return false;

        int charCount = 0;
        int numCount = 0;

        int i=0;

        while ((charCount == 0 || numCount <2) && i < password.length()) {

            char ch = password.charAt(i);

            if (is_Numeric(ch)) numCount++;
            else if (is_Letter(ch)) charCount++;

            i++;

        }

        return (charCount >= 1 && numCount >= 2);
    }



    //controllo se è lettera in maiuscolo
    private static boolean is_Letter(char ch) {
        return (ch >= 'A' && ch <= 'Z');
    }

    //controllo se è un numero
    private static boolean is_Numeric(char ch) {

        return (ch >= '0' && ch <= '9');
    }

    //controllo se è un carattere speciale
    private static boolean is_Special(char ch){
        String aux = Character.toString(ch);
        return (aux.matches("[^A-Za-z0-9 ]"));
    }
}
