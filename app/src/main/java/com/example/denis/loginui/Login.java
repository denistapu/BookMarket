package com.example.denis.loginui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    int attempts;

    RelativeLayout rellay1, rellay2;

    ImageView imgLogo;

    Button login;
    Button signup;
    Button forgot;

    EditText user;
    EditText password;

    CheckBox remember;

    TextView error;
    TextView loginText;

    Intent tStart;
    Intent tMain;
    Intent tSetup;
    Intent tSignup;
    Intent tForgot;

    Boolean showPass;

    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    SessionManager session;
    Boolean saveLogin;
    RequestsManager requests;

    Handler hStart = new Handler();
    Runnable rStart = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
            loginText.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imgLogo.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
            imgLogo.setLayoutParams(params);
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

        //dovrebbe prendere l'intent dal logout
        tStart= getIntent();
        //se contiene l'extra Logout = Yes bisogna togliere le preferences

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        session = new SessionManager(this);
        if(session.isLoggedIn()){
            if(!session.getUser().isSetup()){
                tSetup = new Intent(Login.this, Setup.class);
                startActivity(tSetup);
                finish();
            } else {
                tMain = new Intent(Login.this, MainActivity.class);
                startActivity(tMain);
                finish();
            }
        }

        user= (EditText) findViewById(R.id.edtUser);
        password= (EditText) findViewById(R.id.edtPass);
        remember = (CheckBox) findViewById(R.id.chbRemember);
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            user.setText(loginPreferences.getString("username", ""));
            password.setText(loginPreferences.getString("password", ""));
            remember.setChecked(true);
        }

        requests = new RequestsManager(this);
        attempts = 5;

        showPass=false;

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

        imgLogo = (ImageView) findViewById(R.id.imgView_logo);

        hStart.postDelayed(rStart, 1700);

        login= (Button) findViewById(R.id.btnLogin);
        signup= (Button) findViewById(R.id.btnSignup);
        forgot = (Button) findViewById(R.id.btnForgot);

        remember = (CheckBox) findViewById(R.id.chbRemember);

        error = (TextView) findViewById(R.id.txtError);
        loginText = (TextView) findViewById(R.id.txtLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                error.setVisibility(View.GONE);

                final String userStr = user.getText().toString();
                final String passwordStr = password.getText().toString();

                String email = null;
                Map<String,String> params = new HashMap<String,String>();

                Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
                Matcher matcher = pattern.matcher(userStr);
                if(matcher.matches())
                    email=userStr;


                //controllo nel DB se user e password sono giusti
                if(email!=null) {
                    params.put("email", email);
                    params.put("password", passwordStr);
                }
                else{
                    params.put("username", userStr);
                    params.put("password", passwordStr);
                }
               requests.execRequest("login",params,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String res) {
                                JSONObject response = null;
                                try {
                                    response = new JSONObject(res);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    if(!response.getString("status").equals("OK")) {
                                        attempts--;
                                        if(attempts == 0){
                                            hDisableLogin.postDelayed(rDisableLogin, 10000);
                                            login.setEnabled(true);
                                            attempts=5;
                                        }
                                        error.setText("Username or password incorrect, number of attempts remaining: " + Integer.toString(attempts));
                                        error.setVisibility(View.VISIBLE);
                                    }else{

                                        JSONObject arr = response.getJSONObject("data");
                                        //JSONObject data = arr.getJSONObject(0);
                                        Log.d("gx8", arr.getString("Dcane"));
                                        session.createSession(new User(arr));
                                        if(remember.isChecked()){
                                            loginPrefsEditor.putBoolean("saveLogin", true);
                                            loginPrefsEditor.putString("username", userStr);
                                            loginPrefsEditor.putString("password", passwordStr);
                                            loginPrefsEditor.apply();
                                        } else {
                                            loginPrefsEditor.clear();
                                            loginPrefsEditor.apply();
                                        }
                                       // if(session.isLoggedIn())
                                        if(!session.getUser().isSetup()){
                                            tSetup = new Intent(Login.this, Setup.class);
                                            startActivity(tSetup);
                                            finish();
                                        } else {
                                            tMain = new Intent(Login.this, MainActivity.class);
                                            startActivity(tMain);
                                            finish();
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                });




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
