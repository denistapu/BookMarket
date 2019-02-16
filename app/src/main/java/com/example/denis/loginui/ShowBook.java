package com.example.denis.loginui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ShowBook extends AppCompatActivity {

    Intent tStart;

    Button back;
    Button contact;



    TextView owner;
    TextView title;
    TextView publisher;
    TextView isbn;
    TextView amount;
    TextView desc;
    TextView price;
    TextView authors;
    TextView condition;
    RequestsManager requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book);

        tStart = getIntent();
        requests = new RequestsManager(this);
        back= (Button) findViewById(R.id.btnBackShowBook);
        contact= (Button) findViewById(R.id.btnContactOwnerB);

        owner= (TextView) findViewById(R.id.txtOwner);
        title= (TextView) findViewById(R.id.txtTitle);
        publisher= (TextView) findViewById(R.id.txtPublishingHouse);
        isbn= (TextView) findViewById(R.id.txtISBN);
        amount= (TextView) findViewById(R.id.txtAmount);
        desc= (TextView) findViewById(R.id.txtDescription);
        price= (TextView) findViewById(R.id.txtPrice);
        authors= (TextView) findViewById(R.id.txtAuthors);
        condition= (TextView) findViewById(R.id.txtCondition);

        owner.setText(tStart.getStringExtra("Username"));
        title.setText(tStart.getStringExtra("Title"));
        publisher.setText(tStart.getStringExtra("Publisher"));
        isbn.setText(tStart.getStringExtra("ISBN"));
        amount.setText(tStart.getStringExtra("Amount"));
        desc.setText(tStart.getStringExtra("Description"));
        price.setText(tStart.getStringExtra("Price"));
        authors.setText(tStart.getStringExtra("Authors"));
        condition.setText(tStart.getStringExtra("Condition"));

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,String> params = new HashMap<String,String>();
                params.put("username", tStart.getStringExtra("Username"));
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                requests.execRequest("searchByUsername", params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res){
                        try {
                            JSONObject response = new JSONObject(res);
                            if (!response.getString("status").equals("OK")) {
                                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{""});

                            } else {

                                JSONObject arr = response.getJSONObject("data");
                                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{arr.getString("Email")});
                            }
                            startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
                        }catch(JSONException e){

                        }
                    }
                });


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
