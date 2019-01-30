package com.example.denis.loginui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book);

        tStart = getIntent();

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
                //apre un intent di una chat o qualcosa
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
