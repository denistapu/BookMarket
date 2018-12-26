//Prova123
package com.example.denis.loginui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    int attempts;

    RelativeLayout rellay1, rellay2;

    Button login;
    Button signup;
    Button forgot;

    EditText user;
    EditText password;

    CheckBox remember;

    TextView error;

    Intent tLogin;
    Intent tSignup;
    Intent tForgot;

    Boolean showPass;


    Handler hStart = new Handler();
    Runnable rStart = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    Handler hDisableLogin = new Handler();
    Runnable rDisableLogin = new Runnable() {
        @Override
        public void run() {
            login.setEnabled(false);
            error.setText("Username or password incorrect, number of attempts remaining: " + Integer.toString(attempts) + ".  Wait 10 seconds.");
        }
    };


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        attempts = 5;

        showPass=false;

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

        hStart.postDelayed(rStart, 1700);

        login= (Button) findViewById(R.id.btnLogin);
        signup= (Button) findViewById(R.id.btnSignup);
        forgot = (Button) findViewById(R.id.btnForgot);

        user= (EditText) findViewById(R.id.edtUser);
        password= (EditText) findViewById(R.id.edtPass);

        remember = (CheckBox) findViewById(R.id.chbRemember);

        error = (TextView) findViewById(R.id.txtError);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                error.setVisibility(View.GONE);

                String userStr = user.getText().toString();
                String passwordStr = password.getText().toString();

                String email = null;

                //tLogin = new Intent(Login.this, MainActivity.class);
                Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
                Matcher matcher = pattern.matcher(userStr);
                if(matcher.matches())
                    email=userStr;


                //controllo se user e password sono giusti


                //se non sono giusti faccio comparire una textView che dice che non sono giusti, con il numero
                // di tentativi rimanenti, che quando scaduti disabilitano il bottone di login per 10 secondi

                /*
                attempts--;
                if(attempts == 0){
                    hDisableLogin.postDelayed(rDisableLogin, 10000);
                    login.setEnabled(true);
                    attempts=5;
                }
                error.setText("Username or password incorrect, number of attempts remaining: " + Integer.toString(attempts));
                error.setVisibility(View.VISIBLE);

                */


                //controllo se il remember è spuntato e in caso io abbia la combinazione giusta di user e password
                //la prossima volta che accendo la app non devo fare il login
                if(remember.isChecked()){

                }





            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tSignup = new Intent(Login.this, Signup.class);
                startActivity(tSignup);

            }
        });


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tForgot = new Intent(Login.this, forgotPsw.class);
                startActivity(tForgot);
            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if(showPass){
                            password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_off, 0);
                            showPass=false;
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            return true;
                        }else{
                            password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_on, 0);
                            showPass=true;
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            return false;
                        }


                    }
                }

                return false;
            }
        });

    }
}
