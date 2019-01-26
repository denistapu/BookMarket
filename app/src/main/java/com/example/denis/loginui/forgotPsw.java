package com.example.denis.loginui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class forgotPsw extends AppCompatActivity {

    Intent tStart;

    Button forgot;
    Button back;

    EditText email;

    TextView errorEmail;
    TextView info;
    RequestsManager requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_psw);



        tStart= getIntent();
        requests = new RequestsManager(this);
        forgot = (Button) findViewById(R.id.btnForgotF);
        back = (Button) findViewById(R.id.btnBack);

        email = (EditText) findViewById(R.id.edtEmailF);

        errorEmail = (TextView) findViewById(R.id.txtErrorEmailF);
        info = (TextView) findViewById(R.id.txtInfoForgot);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                errorEmail.setVisibility(View.GONE);
                info.setText("If you forgot your account's password, insert the corresponding Email address  or Username and we will provide a reset one.\n");
                String emailStr = email.getText().toString();
                String username = null;
                Map<String,String> params = new HashMap<String,String>();

                Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
                Matcher matcher = pattern.matcher(emailStr);
                if(!matcher.matches()){
                    username=emailStr;
                }
                if(!emailStr.equals("")){

                    if(username!=null)
                        params.put("username", username);
                    else
                        params.put("email", emailStr);
                    requests.execRequest("forgotpsw",params,  new Response.Listener<String>() {
                        @Override
                        public void onResponse(String res) {
                            JSONObject response = null;
                            try {
                                response = new JSONObject(res);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                if(!response.getString("status").equals("OK"))
                                    errorEmail.setVisibility(View.VISIBLE);
                                 else
                                    info.setText("We sent to the given Email a message containing its corresponding reset password !");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                   /* RequestQueue mQueue = Volley.newRequestQueue(forgotPsw.this);
                    String url = "http:/192.168.1.119/AppAndroid/LoginSystem/api.php?request=forgotpsw&"+params;
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if(!response.getString("status").equals("OK")) {
                                            errorEmail.setVisibility(View.VISIBLE);
                                        } else {
                                            info.setText("We sent to the given Email a message containing its corresponding reset password !");
                                        }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                    mQueue.add(request);*/

                }
                    //controllo se la stringa username non è vuota
                    //se non è vuota controllo controllo che lo username esista nel DB
                    //se non esiste faccio errorEmail..setVisibility(View.VISIBLE);
                    //altrimenti invia la mail alla email corrispondente a quell user e mette info.setText("We sent to the given Email a message containing its corresponding reset password !");

                    //se invece username è vuoto
                    //controlla se la mail è nel DB

                    //se non lo è mette errorEmail.setVisibility(View.VISIBLE);


                    //se lo è manda la mail e mette info.setText("We sent to the given Email a message containing its corresponding reset password !");

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
