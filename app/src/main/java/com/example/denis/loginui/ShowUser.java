package com.example.denis.loginui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowUser extends AppCompatActivity {

    Intent tStart;
    Intent tBook;

    Button back;
    Button contact;

    TextView fullName;
    TextView username;

    ImageView profilePic;

    ListView bookView;

    ArrayList<Book> bookList;

    RequestsManager requests;
    ArrayAdapter adapterBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);

        tStart = getIntent();
        requests = new RequestsManager(this);
        back= (Button) findViewById(R.id.btnBackShowUser);
        contact= (Button) findViewById(R.id.btnContactOwnerU);

        fullName= (TextView) findViewById(R.id.txtFullNameShowUser);
        username= (TextView) findViewById(R.id.txtUsernameShowUser);
        bookList = new ArrayList<Book>();
        adapterBook = new ArrayAdapter(this, android.R.layout.simple_list_item_1){
            @Override
            public boolean isEnabled(int position){
                return (!(adapterBook.getItem(position).equals("No results found")));
            }
        };
        profilePic = (ImageView) findViewById(R.id.userPicShowUser);

        //cosi oppure (preferibilmente) con l'ID prendento di dati dal DB gli setto fullname, username, profile pic e listBook
        fullName.setText(tStart.getStringExtra("FullName"));
        username.setText(tStart.getStringExtra("Username"));
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("owner",tStart.getStringExtra("Username") );
        requests.execRequest("selectBooks", params, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
               displayBooks(res);
            }
        });
        //anche l'immagine va settata

        bookView = (ListView) findViewById(R.id.lstSearchShowUser);

        bookView.setAdapter(adapterBook);

        //inserisco i dati dal database all'ArrayList bookList che poi setto sull'adapter

        adapterBook.notifyDataSetChanged();

        bookView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tBook= new Intent(ShowUser.this, ShowBook.class);

                /*negli extra devo mettere tutti i dettagli del libro presi dal DB

                    RICORDARSI DI METTERE ANCHE LO USERNAME DEL PROPRIETARIO
*/
                    tBook.putExtra("Username",tStart.getStringExtra("Username"));
                    tBook.putExtra("Title",bookList.get(position).getTitolo());
                    tBook.putExtra("Publisher",bookList.get(position).getCasaed());
                    tBook.putExtra("ISBN",bookList.get(position).getISBN());
                    tBook.putExtra("Amount",Integer.toString(bookList.get(position).getQuantita()));
                    tBook.putExtra("Description",bookList.get(position).getDescrizione());
                    tBook.putExtra("Price",Float.toString(bookList.get(position).getPrezzo()));
                    tBook.putExtra("Authors",bookList.get(position).getAutore());
                    tBook.putExtra("Condition", bookList.get(position).getCondizione());


                startActivity(tBook);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //apre un intent di una chat o qualcosa
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{tStart.getStringExtra("email")});
                startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void displayBooks(String result){
        try {
            JSONObject response = new JSONObject(result);
            if (!response.getString("status").equals("OK")) {
                if(adapterBook.getPosition("No results found")==-1)
                    adapterBook.add("No results found");

            } else {
                JSONArray data = response.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    bookList.add(new Book(data.getJSONObject(i)));
                    adapterBook.add(bookList.get(i).getTitolo() + "   "+bookList.get(i).getPrezzo() + "\n"+ bookList.get(i).getCondizione());
                }
            }
            //adapterBook.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.d("gx8", "Test");
        }

    }
}
