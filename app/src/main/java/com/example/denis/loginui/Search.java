package com.example.denis.loginui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.res.ResourcesCompat;
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
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import android.widget.LinearLayout;
import android.view.MotionEvent;


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
    Spinner order;
    LinearLayout linearLayoutOrder;
    String result;
    ListView infoView;
    RequestsManager requests;
    LinkedHashMap<String,Book> booksList;
    List<String> booksDisplayed;
    List<User> usersList;
    ArrayList infoList;
    int check;
    long delay = 500;
    long last_text_edit = 0;
    ArrayAdapter adapterInfo;
    Handler handler = new Handler();

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                adapterInfo.clear();
                Search(info, type, searchType, infoView);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        tStart = getIntent();
        check = 0;
        requests = new RequestsManager(this);
        searchType = true;
        booksList = new LinkedHashMap<String,Book>();
        usersList = new ArrayList<User>();
        bottomNav = (BottomNavigationView) findViewById(R.id.navigationS);
        booksDisplayed = new ArrayList<String>();
        BottomViewHelper.enableNavigation(Search.this, bottomNav);

        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
        linearLayoutOrder= (LinearLayout) findViewById(R.id.linlayOrder);


        infoView = (ListView) findViewById(R.id.lstSearch);
        adapterInfo = new ArrayAdapter(this, android.R.layout.simple_list_item_1, booksDisplayed){
            @Override
            public boolean isEnabled(int position){
                return (!(adapterInfo.getItem(position).equals("No results found")));
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
        adapterBooks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterUsers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapterBooks);
        type.setSelection(0);
        order = (Spinner) findViewById(R.id.spnOrder);
        ArrayAdapter<CharSequence> adapterOrder = ArrayAdapter.createFromResource(Search.this,
                R.array.orderBooks, R.layout.simple_spinner_item);
        adapterOrder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        order.setAdapter(adapterOrder);
        order.setSelection(0);

        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!searchType){
                    String infoStr = info.getText().toString();

                    books.setTextColor(ResourcesCompat.getColor(getResources(), R.color.orange, null));
                    users.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                    type.setAdapter(adapterBooks);
                    type.setSelection(0);
                    info.setHint(type.getSelectedItem().toString());
                    linearLayoutOrder.setVisibility(View.VISIBLE);
                    searchType = true;

                    if(infoStr.length()>0){
                        adapterInfo.clear();
                        Search(info, type, searchType, infoView);
                    }

                }
            }
        });


        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchType){
                    String infoStr = info.getText().toString();

                    books.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                    users.setTextColor(ResourcesCompat.getColor(getResources(), R.color.orange, null));
                    type.setAdapter(adapterUsers);
                    type.setSelection(0);
                    info.setHint(type.getSelectedItem().toString());
                    linearLayoutOrder.setVisibility(View.GONE);
                    searchType = false;

                    if(infoStr.length()>0){
                        adapterInfo.clear();
                        Search(info, type, searchType, infoView);
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

                handler.removeCallbacks(input_finish_checker);
            }

            @Override
            public void afterTextChanged(Editable s) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
            }
        });
        order.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }

        });
        order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               if(!booksDisplayed.isEmpty()) {
                   Log.d("gx8", "test8");
                   Collections.reverse(booksDisplayed);
                   adapterInfo.notifyDataSetChanged();
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        //quando cambia l'item selezionato su type, rifaccio la ricerca e li displayo in base al nuovo tipo che vuole cercare
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                info.setHint(type.getSelectedItem().toString());
                if(++check > 1) {
                    adapterInfo.clear();
                    Search(info, type, searchType, infoView);
                }
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
                    List<String> keys = new ArrayList(booksList.keySet());
                    tBook.putExtra("id", Integer.toString(booksList.get(keys.get(position)).getID()));
                    //Log.d("gx8",booksList.get(position).getID());
                    tBook.putExtra("Title", booksList.get(keys.get(position)).getTitolo());
                    tBook.putExtra("Publisher", booksList.get(keys.get(position)).getCasaed());
                    tBook.putExtra("ISBN", booksList.get(keys.get(position)).getISBN());
                    tBook.putExtra("Amount", Integer.toString(booksList.get(keys.get(position)).getQuantita()));
                    tBook.putExtra("Description", booksList.get(keys.get(position)).getDescrizione());
                    tBook.putExtra("Price", Float.toString(booksList.get(keys.get(position)).getPrezzo()));
                    tBook.putExtra("Authors", booksList.get(keys.get(position)).getAutore());
                    tBook.putExtra("Proprietario", booksList.get(keys.get(position)).getProprietario());
                    tBook.putExtra("Condition", booksList.get(keys.get(position)).getCondizione());
                    tBook.putExtra("Username", keys.get(position));
                    startActivity(tBook);



                }else{

                    tUser = new Intent(Search.this, ShowUser.class);
                    tUser.putExtra("Username",usersList.get(position).getUsername());
                    tUser.putExtra("FullName",usersList.get(position).getName()+ " "+ usersList.get(position).getSurname());
                    tUser.putExtra("email", usersList.get(position).getEmail());
                    //tBook.putExtra("ProfilePic","");
                    //tBook.putExtra("Books", "");


                    startActivity(tUser);
                }
            }


        });

    }


    public void Search(EditText info, Spinner type, boolean searchType, ListView listView){
        String infoStr = info.getText().toString();
        String typeStr = type.getSelectedItem().toString();
        result = null;
        HashMap<String,String> params = new HashMap<String,String>();




        if(searchType){


            if(order.getSelectedItem().toString().equals("Price down"))
                params.put("order", "0");
            else if(order.getSelectedItem().toString().equals("Price down"))
                params.put("order", "1");

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

                params.put("Titolo", infoStr);
                requests.execRequest("selectBooks", params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res){
                        displayBooks(res);
                    }
                });
            }

        }else{
            usersList.clear();
            if(typeStr.equals("Name and Surname")){

                if(is_Valid_Name(infoStr)){

                    params.put("fullname", infoStr);
                    requests.execRequest("searchByFullName", params, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String res){
                            displayUsers(res);
                        }
                    });
                }else{
                    if(adapterInfo.getPosition("No results found")==-1)
                        adapterInfo.add("No results found");
                }
            }else if(typeStr.equals("Username")){
                params.put("username", infoStr);
                requests.execRequest("searchByUsername", params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res){
                        displayUsers(res);
                    }
                });
            }

        }
    }
    public void displayBooks(String result){
        Log.d("gx8", result);
            try {
                JSONObject response = new JSONObject(result);
                if (!response.getString("status").equals("OK")) {
                    if(adapterInfo.getPosition("No results found")==-1)
                        adapterInfo.add("No results found");

                } else {
                    JSONArray data = response.getJSONArray("data");
                    Log.d("gx8", Integer.toString(data.length()));
                    for (int i = 0; i < data.length(); i++) {
                        booksList.put(data.getJSONObject(i).getString("OwnerUsername"),new Book(data.getJSONObject(i)));
                        String key = data.getJSONObject(i).getString("OwnerUsername");
                        booksDisplayed.add(booksList.get(key).getTitolo() + "   "+booksList.get(key).getPrezzo() + "\n"+ booksList.get(key).getCondizione());
                    }
                }
                adapterInfo.notifyDataSetChanged();
            } catch (JSONException e) {

            }

    }

    public void displayUsers(String result){
        try {
            JSONObject response = new JSONObject(result);
            if (!response.getString("status").equals("OK")) {
                if(adapterInfo.getPosition("No results found")==-1)
                    adapterInfo.add("No results found");

            } else {
                try {
                    JSONArray data = response.getJSONArray("data");
                    for(int i = 0; i<data.length(); i++){
                        usersList.add(new User(data.getJSONObject(i)));
                        adapterInfo.add(usersList.get(i).getUsername());
                    }
                }catch(JSONException i){
                    JSONObject arr = response.getJSONObject("data");
                    usersList.add(new User(arr));
                    adapterInfo.add(new User(arr).getUsername());
                }
                /*for(int i = 0; i<data.length(); i++){
                    usersList.add(new User(data.getJSONObject(i)));
                    adapterInfo.add(usersList.get(i).getUsername());
                }*/
            }
            adapterInfo.notifyDataSetChanged();
        } catch (JSONException e) {

        }
    }

}


