package com.example.denis.loginui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyBook extends AppCompatActivity {

    Boolean isModified;
    Boolean isNewBook;

    Intent tStart;

    Button back;
    Button save;

    EditText title;
    EditText publisher;
    EditText isbn;
    EditText amount;
    EditText desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book);

        tStart = getIntent();

        isModified = false;
        isNewBook = false;

        back= (Button) findViewById(R.id.btnBackMyBook);
        save= (Button) findViewById(R.id.btnSaveMyBook);

        title= (EditText) findViewById(R.id.edtTitle);
        publisher= (EditText) findViewById(R.id.edtPublishingHouse);
        isbn= (EditText) findViewById(R.id.edtISBN);
        amount= (EditText) findViewById(R.id.edtAmount);
        desc= (EditText) findViewById(R.id.edtDescription);

        title.setText(tStart.getStringExtra("Title"));
        publisher.setText(tStart.getStringExtra("Publisher"));
        isbn.setText(tStart.getStringExtra("ISBN"));
        amount.setText(tStart.getStringExtra("Amount"));
        desc.setText(tStart.getStringExtra("Description"));



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleStr = title.getText().toString();
                String publisherStr = publisher.getText().toString();
                String isbnStr = isbn.getText().toString();
                String amountStr = amount.getText().toString();
                String descStr = desc.getText().toString();

                int amountInt = -1;

                try{
                    amountInt = Integer.parseInt(amountStr);
                }catch(Exception e){
                    amountInt= -1;
                }


                if(!titleStr.equals(tStart.getStringExtra("Title")) && !titleStr.equals("")){
                    isModified=true;
                    isNewBook = true;
                }

                if(!publisherStr.equals(tStart.getStringExtra("Publisher"))){
                    isModified=true;
                }else{
                    isNewBook = false;
                }

                if(!isbnStr.equals(tStart.getStringExtra("ISBN")) && is_Valid_ISBN(isbnStr)){
                    isModified=true;
                }else{
                    isNewBook = false;
                }

                if(!amountStr.equals(tStart.getStringExtra("Amount")) && amountInt>=1){
                    isModified=true;
                }else{
                    isNewBook = false;
                }

                if(!descStr.equals(tStart.getStringExtra("Descriprion"))){
                    isModified=true;
                }else{
                    isNewBook = false;
                }

                if((isModified && !tStart.getStringExtra("Title").equals("")) || isNewBook){
                    //salva sul DB i dati del libro
                    Toast.makeText(getApplicationContext(),"Book saved correctly!", Toast.LENGTH_SHORT).show();
                    finish();
                }


                isModified=false;
                isNewBook = false;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private static boolean is_Valid_ISBN(String isbn){

        int n = isbn.length();
        if (n != 10)
            return false;


        int sum = 0;
        for (int i = 0; i < 9; i++)
        {
            int digit = isbn.charAt(i) - '0';
            if (0 > digit || 9 < digit)
                return false;
            sum += (digit * (10 - i));
        }


        char last = isbn.charAt(9);
        if (last != 'X' && (last < '0' ||
                last > '9'))
            return false;


        sum += ((last == 'X') ? 10 : (last - '0'));

        return (sum % 11 == 0);

    }
}
