package com.example.denis.loginui;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.denis.loginui.CheckInput.is_Valid_ISBN;
import static com.example.denis.loginui.CheckInput.is_Valid_Name;

public class Search extends AppCompatActivity {

    public static final String TAG = "Search" ;
    public static final int ACTIVITY_NUM = 1 ;

    boolean searchType;    // true = books     false = users

    BottomNavigationView bottomNav;

    Intent tStart;
    Intent tBook;
    Intent tUser;

    Button users;
    Button books;

    EditText info;

    Spinner type;
    String result;
    ListView infoView;
    RequestsManager requests;
    List<Book> booksList;
    List<User> usersList;
    ArrayList infoList;

    ArrayAdapter adapterInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tStart = getIntent();
        requests = new RequestsManager(this);
        searchType = true;
        booksList = new ArrayList<Book>();
        usersList = new ArrayList<User>();
        bottomNav = (BottomNavigationView) findViewById(R.id.navigationS);

        BottomViewHelper.enableNavigation(Search.this, bottomNav);

        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

        infoView = (ListView) findViewById(R.id.lstSearch);
        adapterInfo = new ArrayAdapter(this, android.R.layout.simple_list_item_1){
            @Override
            public boolean isEnabled(int position){
                return ((adapterInfo.getItem(position).equals("No results found")) ? false : true);
            }
        };
        infoView.setAdapter(adapterInfo);

        users = (Button) findViewById(R.id.btnUserSearch);
        books = (Button) findViewById(R.id.btnBookSearch);

        info = (EditText) findViewById(R.id.edtInfo);

        type = (Spinner) findViewById(R.id.spnType);
        final ArrayAdapter<CharSequence> adapterBooks = ArrayAdapter.createFromResource(Search.this,
                R.array.typeBooks, R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> adapterUsers = ArrayAdapter.createFromResource(Search.this,
                R.array.typeUsers, R.layout.simple_spinner_item);
        type.setAdapter(adapterBooks);
        type.setSelection(0);


        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!searchType){
                    String infoStr = info.getText().toString();

                    books.setTextColor(Color.BLACK);
                    users.setTextColor(Color.WHITE);
                    type.setAdapter(adapterBooks);
                    info.setHint(type.getSelectedItem().toString());

                    searchType = true;

                    if(infoStr.length()>0){
                        //fare di nuovo ricerca con quello che cè su infoStr però su books
                        //e metterlo sulla listView
                    }

                }
            }
        });


        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchType){
                    String infoStr = info.getText().toString();

                    books.setTextColor(Color.WHITE);
                    users.setTextColor(Color.BLACK);
                    type.setAdapter(adapterUsers);
                    info.setHint(type.getSelectedItem().toString());

                    searchType = true;

                    if(infoStr.length()>0){
                        //fare di nuovo ricerca con quello che cè su infoStr però su users
                        //e metterlo sulla listView
                    }
                }
            }
        });


        info.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterInfo.clear();
                Search(info, type, searchType, infoView);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //quando cambia l'item selezionato su type, rifaccio la ricerca e li displayo in base al nuovo tipo che vuole cercare
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Search(info, type, searchType, infoView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        infoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(searchType){
                    tBook = new Intent(Search.this, ShowBook.class);
                    tBook.putExtra("id", Integer.toString(booksList.get(position).getID()));
                    //Log.d("gx8",booksList.get(position).getID());
                    tBook.putExtra("Title", booksList.get(position).getTitolo());
                    tBook.putExtra("Publisher", booksList.get(position).getCasaed());
                    tBook.putExtra("ISBN", booksList.get(position).getISBN());
                    tBook.putExtra("Amount", Integer.toString(booksList.get(position).getQuantita()));
                    tBook.putExtra("Description", booksList.get(position).getDescrizione());
                    tBook.putExtra("Price", Float.toString(booksList.get(position).getPrezzo()));
                    tBook.putExtra("Authors", booksList.get(position).getAutore());
                    tBook.putExtra("Proprietario", booksList.get(position).getProprietario());
                    startActivity(tBook);

                }else{

                    tUser = new Intent(Search.this, ShowUser.class);
                    /*negli extra devo mettere tutti i dettagli dell'utente presi dal DB


                    tBook.putExtra("Username","");
                    tBook.putExtra("FullName","");
                    tBook.putExtra("ProfilePic","");
                    tBook.putExtra("Books", "");

                    cosi oppure gli metto l'ID e poi nell'altra activity
                    gli prendo i libri il nome completo e lo username in base a quello (penso sia meglio così)
                    */

                    startActivity(tUser);
                }
            }


        });

    }


    private  void Search(EditText info, Spinner type, boolean searchType, ListView listView){
        String infoStr = info.getText().toString();
        String typeStr = type.getSelectedItem().toString();
        result = null;
        HashMap<String,String> params = new HashMap<String,String>();
        booksList.clear();
        //se la ricerca non trova nulla deve dare come output la stringa No Result Found che mette da sola sulla ListView

        //ricerche (bisogna anche displayarle sulla listView:


        if(searchType){                     //se devo cercare per libro
            if(typeStr.equals("ISBN")){
                if(is_Valid_ISBN(infoStr)){

                    params.put("ISBN", infoStr);
                    requests.execRequest("selectBooks", params, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String res){
                            displayBooks(res);
                        }
                    });
                }else{
                    if(adapterInfo.getPosition("No results found")==-1)
                        adapterInfo.add("No results found");
                }
            }else if(typeStr.equals("Title")){
                //faccio ricerca su titolo
                //se non trova nulla glielo dico
                params.put("Titolo", infoStr);
                requests.execRequest("selectBooks", params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res){
                        displayBooks(res);
                    }
                });
            }

        }else{              //se devo cercare per utente


            if(typeStr.equals("Nome e Cognome")){
                if(is_Valid_Name(infoStr)){
                    //faccio ricerca su nome e cognome
                }else{
                    if(adapterInfo.getPosition("No results found")==-1)
                        adapterInfo.add("No results found");
                }
            }else if(typeStr.equals("Username")){
                //faccio ricerca su utente
                //se non trova nulla glielo dico
            }

        }
    }
    public void displayBooks(String result){
            try {
                JSONObject response = new JSONObject(result);
                if (!response.getString("status").equals("OK")) {
                    if(adapterInfo.getPosition("No results found")==-1)
                        adapterInfo.add("No results found");

                } else {
                    JSONArray data = response.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        adapterInfo.add(data.getJSONObject(i));
                        booksList.add(new Book(data.getJSONObject(i)));
                    }
                }
                adapterInfo.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


