package com.example.denis.loginui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.denis.loginui.CheckInput.is_Valid_ISBN;

public class MyBook extends AppCompatActivity {

    Boolean isModified;
    Boolean isNewBook;

    Intent tStart;
    Spinner condition;
    Button back;
    Button save;

    EditText title;
    EditText publisher;
    EditText isbn;
    EditText amount;
    EditText desc;
    EditText price;
    EditText authors;
    RequestsManager requests;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book);

        tStart = getIntent();

        isModified = false;
        isNewBook = false;

        back= (Button) findViewById(R.id.btnBackMyBook);
        save= (Button) findViewById(R.id.btnSaveMyBook);
        condition = (Spinner) findViewById(R.id.spnConditionMyBook);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MyBook.this,
                R.array.bookCondition, R.layout.simple_spinner_item);
        condition.setAdapter(adapter);
        condition.setSelection(0);
        requests = new RequestsManager(this);
        session = new SessionManager(this);
        title= (EditText) findViewById(R.id.edtTitle);
        publisher= (EditText) findViewById(R.id.edtPublishingHouse);
        isbn= (EditText) findViewById(R.id.edtISBN);
        amount= (EditText) findViewById(R.id.edtAmount);
        desc= (EditText) findViewById(R.id.edtDescription);
        price= (EditText) findViewById(R.id.edtPrice);
        authors= (EditText) findViewById(R.id.edtAuthors);
        if(!tStart.hasExtra("isNew")) {
            title.setText(tStart.getStringExtra("Title"));
            publisher.setText(tStart.getStringExtra("Publisher"));
            isbn.setText(tStart.getStringExtra("ISBN"));
            amount.setText(tStart.getStringExtra("Amount"));
            desc.setText(tStart.getStringExtra("Description"));
            price.setText(tStart.getStringExtra("Price"));
            authors.setText(tStart.getStringExtra("Authors"));
            ArrayAdapter<String> array_spinner=(ArrayAdapter<String>)condition.getAdapter();
            condition.setSelection(array_spinner.getPosition(tStart.getStringExtra("Condition")));
        } else
            isNewBook = true;



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleStr = title.getText().toString();
                String publisherStr = publisher.getText().toString();
                String isbnStr = isbn.getText().toString();
                String amountStr = amount.getText().toString();
                String descStr = desc.getText().toString();
                String priceStr = price.getText().toString();
                String authorsStr = authors.getText().toString();
                String conditionStr = condition.getSelectedItem().toString();


                int amountInt = -1;
                double priceDb = -1;

//-------------------------------------------------------------------------------------------------------------------------

                try{
                    amountInt = Integer.parseInt(amountStr);
                }catch(Exception e){
                    amountInt= -1;
                }

                try{
                    priceDb = Double.parseDouble(priceStr);
                }catch(Exception e){
                    priceDb= -1;
                }

//-------------------------------------------------------------------------------------------------------------------------

                if(!isNewBook){
                    if(!titleStr.equals(tStart.getStringExtra("Title")) && !titleStr.equals("")){
                        isModified=true;
                    }
//-------------------------------------------------------------------------------------------------------------------------

                    if(!publisherStr.equals(tStart.getStringExtra("Publisher"))){
                        isModified=true;
                    }
                    if(!conditionStr.equals(tStart.getStringExtra("Condition"))){
                        isModified=true;
                    }
//-------------------------------------------------------------------------------------------------------------------------

                    if(!authorsStr.equals(tStart.getStringExtra("Authors"))){
                        isModified=true;
                    }
//-------------------------------------------------------------------------------------------------------------------------

                    if(!isbnStr.equals(tStart.getStringExtra("ISBN")) && is_Valid_ISBN(isbnStr)){
                        isModified=true;
                    }
//-------------------------------------------------------------------------------------------------------------------------

                    if(!amountStr.equals(tStart.getStringExtra("Amount")) && amountInt>=1){
                        isModified=true;
                    }
//-------------------------------------------------------------------------------------------------------------------------

                    if(!priceStr.equals(tStart.getStringExtra("Price")) && priceDb>=0){
                        isModified=true;
                    }
//-------------------------------------------------------------------------------------------------------------------------

                    if(!descStr.equals(tStart.getStringExtra("Descriprion"))){
                        isModified=true;
                    }
                }
               /* */
//-------------------------------------------------------------------------------------------------------------------------

                if((isModified && !tStart.getStringExtra("Title").equals("")) || isNewBook){
                    //salva sul DB i dati del libro
                    Log.d("gx8", isModified.toString());
                        HashMap<String,String> params = new HashMap<String,String>();

                        params.put("Titolo", titleStr);
                        params.put("Prezzo",priceStr);
                        params.put("Quantita",amountStr);
                        params.put("ISBN",isbnStr);
                        params.put("Descrizione",descStr);
                        params.put("CasaEd", publisherStr);
                        params.put("Autore",authorsStr);

                    if(isModified){
                        params.put("ID", tStart.getStringExtra("id"));
                        params.put("ownerHash", session.getUser().getAuth());
                        requests.execRequest("editBook", params,  new Response.Listener<String>() {
                            @Override
                            public void onResponse(String res){
                                Log.d("gx8", res);
                                JSONObject response = null;
                                try {
                                    response = new JSONObject(res);
                                    if(!response.getString("status").equals("OK")) {
                                        Toast.makeText(getApplicationContext(),"Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(),"Book saved correctly!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } else  {
                        params.put("ownerAuth", session.getUser().getAuth());
                        params.put("ownerID", Integer.toString(session.getUser().getID()));
                        requests.execRequest("addBook", params,  new Response.Listener<String>() {
                            @Override
                            public void onResponse(String res){
                                Log.d("gx8", res);
                                JSONObject response = null;
                                try {
                                    response = new JSONObject(res);
                                    if(!response.getString("status").equals("OK")) {
                                        Toast.makeText(getApplicationContext(),"Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(),"Book added correctly!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }


                }

//-------------------------------------------------------------------------------------------------------------------------

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


}
