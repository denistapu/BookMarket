package com.example.denis.loginui;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
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

    ListView infoView;

    ArrayList infoList;

    ArrayAdapter adapterInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tStart = getIntent();

        searchType = true;

        bottomNav = (BottomNavigationView) findViewById(R.id.navigationS);

        BottomViewHelper.enableNavigation(Search.this, bottomNav);

        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

        infoView = (ListView) findViewById(R.id.lstSearch);

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
                Search.Search(info, type, searchType, infoView);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //quando cambia l'item selezionato su type, rifaccio la ricerca e li displayo in base al nuovo tipo che vuole cercare
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Search.Search(info, type, searchType, infoView);
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


    private static void Search(EditText info, Spinner type, boolean searchType, ListView listView){
        String infoStr = info.getText().toString();
        String typeStr = type.getSelectedItem().toString();


        //se la ricerca non trova nulla deve dare come output la stringa No Result Found che mette da sola sulla ListView

        //ricerche (bisogna anche displayarle sulla listView:


        if(searchType){                     //se devo cercare per libro
            if(typeStr.equals("ISBN")){
                if(is_Valid_ISBN(infoStr)){
                    //faccio ricerca su ISBN
                }else{
                    //dico che non ho trovato nulla
                }
            }else if(typeStr.equals("Titolo")){
                //faccio ricerca su titolo
                //se non trova nulla glielo dico
            }

            //posso fare altri else if se ci sono altri type per cui cercare (non so tipo case editrice ecc.)

            //se la ricerca non trova nulla deve dare come output la stringa No Result Found che mette da sola sulla ListView

        }else{              //se devo cercare per utente


            if(typeStr.equals("Nome e Cognome")){
                if(is_Valid_Name(infoStr)){
                    //faccio ricerca su nome e cognome
                }else{
                    //dico che non ho trovato nulla
                }
            }else if(typeStr.equals("Username")){
                //faccio ricerca su utente
                //se non trova nulla glielo dico
            }

        }
    }

}
