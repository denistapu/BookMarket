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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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



        isNewBook = tStart.getBooleanExtra("isNew", false);
        title.setText(tStart.getStringExtra("Title"));
        publisher.setText(tStart.getStringExtra("Publisher"));
        isbn.setText(tStart.getStringExtra("ISBN"));
        amount.setText(tStart.getStringExtra("Amount"));
        desc.setText(tStart.getStringExtra("Description"));
        price.setText(tStart.getStringExtra("Price"));
        authors.setText(tStart.getStringExtra("Authors"));
        ArrayAdapter<String> array_spinner=(ArrayAdapter<String>)condition.getAdapter();
        condition.setSelection(array_spinner.getPosition(tStart.getStringExtra("Condition")));
        EditText[] etexts = new EditText[] {title,  isbn, price, publisher,amount, desc,  authors};
        Log.d("gx8", "1: "+isModified.toString());
            TextWatcher tw = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    isModified = true;
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };

            for(EditText t : etexts){
                t.addTextChangedListener(tw);
            }
        Log.d("gx8", "2: "+isModified.toString());
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
                Log.d("gx8", "3: "+isNewBook.toString());

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

                if(!conditionStr.equals(tStart.getStringExtra("Condition")) && !isNewBook)
                    isModified = true;
//-------------------------------------------------------------------------------------------------------------------------
                Log.d("gx8", "LMAO IM IN ");
                if((isModified || isNewBook) && editTextsNotEmpty(Arrays.copyOfRange(etexts,0,2)) ){
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
                        params.put("Condizione", conditionStr);

                    if(isNewBook){
                        Log.d("gx8", "Fake love");
                        params.put("ownerAuth", session.getUser().getAuth());
                        params.put("ownerID", Integer.toString(session.getUser().getID()));
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
                                        Toast.makeText(getApplicationContext(),"Book added correctly!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    } else  {
                        Log.d("gx8", "sossa");
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

                    }


                } else
                    Toast.makeText(getApplicationContext(), "One or more of the require fields are empty!", Toast.LENGTH_SHORT).show();

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
    public boolean editTextsNotEmpty(EditText[] et){
        for(EditText t: et){
            if(t.getText().toString().isEmpty())
                return false;
        }
        return true;
    }

}
