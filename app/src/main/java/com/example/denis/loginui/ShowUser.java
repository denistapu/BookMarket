package com.example.denis.loginui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowUser extends AppCompatActivity {

    Intent tStart;
    Intent tBook;

    Button back;
    Button contact;

    TextView fullName;
    TextView username;

    ImageView profilePic;

    ListView bookView;

    ArrayList bookList;

    ArrayAdapter adapterBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);

        tStart = getIntent();

        back= (Button) findViewById(R.id.btnBackShowUser);
        contact= (Button) findViewById(R.id.btnContactOwnerU);

        fullName= (TextView) findViewById(R.id.txtFullNameShowUser);
        username= (TextView) findViewById(R.id.txtUsernameShowUser);

        profilePic = (ImageView) findViewById(R.id.userPicShowUser);

        //cosi oppure (preferibilmente) con l'ID prendento di dati dal DB gli setto fullname, username, profile pic e listBook
        fullName.setText(tStart.getStringExtra("FullName"));
        username.setText(tStart.getStringExtra("Username"));
        //anche l'immagine va settata

        bookView = (ListView) findViewById(R.id.lstSearch);

        bookView.setAdapter(adapterBook);

        //inserisco i dati dal database all'ArrayList bookList che poi setto sull'adapter

        adapterBook.notifyDataSetChanged();

        bookView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tBook= new Intent(ShowUser.this, ShowBook.class);

                /*negli extra devo mettere tutti i dettagli del libro presi dal DB

                    RICORDARSI DI METTERE ANCHE LO USERNAME DEL PROPRIETARIO

                    tBook.putExtra("Username","");
                    tBook.putExtra("Title","");
                    tBook.putExtra("Publisher","");
                    tBook.putExtra("ISBN","");
                    tBook.putExtra("Amount","");
                    tBook.putExtra("Description","");
                    tBook.putExtra("Price","");
                    tBook.putExtra("Authors","");

                    */

                startActivity(tBook);
            }
        });

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
