package com.example.denis.loginui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity{

    public static final String TAG = "Books" ;
    public static final int ACTIVITY_NUM = 0 ;

    Boolean isRemoving;

    Intent tStart;
    Intent tMyBooks;

    BottomNavigationView bottomNav;

    Button add;
    Button remove;

    ListView books;

    ArrayList<String> bookList ;

    ArrayAdapter adapterBooks;

    List <Integer> removeList;
    ArrayList<Book> booksData;

   // RequestsManager requests;
   RequestsManager requests;
    SessionManager session;

    private Dialog removeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.LightDialogTheme);
        builder.setTitle("Remove");
        builder.setMessage("Are you sure to remove those book?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for(int i =0; i<removeList.size(); i++){
                            Log.d("gx8", booksData.get(removeList.get(i)).getTitolo());
                            booksData.remove(removeList.get(i));
                            getViewByPosition(removeList.get(i), books).setSelected(false);
                            adapterBooks.remove(adapterBooks.getItem(removeList.get(i)));
                            //devo cancellarlo dal DB
                        }

                       // adapterBooks.notifyDataSetChanged();
                        removeList.clear();
                        isRemoving=false;
                        add.setEnabled(true);
                    }
                });

        builder.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        return alert;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requests = new RequestsManager(this);
        session = new SessionManager(this);
        tStart=getIntent();
        tMyBooks = new Intent(MainActivity.this, MyBook.class);

        removeList = new ArrayList<Integer>();

        isRemoving = false;

        bottomNav = (BottomNavigationView) findViewById(R.id.navigationM);

        BottomViewHelper.enableNavigation(MainActivity.this, bottomNav);

        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

        books = (ListView) findViewById(R.id.lstBooks);
        booksData = new ArrayList<Book>();
       /* bookList.add("Test1");
        bookList.add("Test2");
        bookList.add("Test3");
        bookList.add("Test4");
        bookList.add("Test4");
        bookList.add("Test4");
        bookList.add("Test4");
        bookList.add("Test4");*/

        //inserisco i dati dal database all'ArrayList bookList che poi setto sull'adapter
        HashMap<String,String> args = new HashMap<String,String>();
        args.put("request", "getBooks");
        args.put("owner", session.getUser().getUsername());
        adapterBooks = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        //requests.setParams(args);
        requests.execRequest("selectBooks", args, new Response.Listener<String>() {

            @Override
            public void onResponse(String res) {
                JSONObject response = null;
                try {
                    response = new JSONObject(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (response.getString("status").equals("OK")) {
                        Log.d("aa", res);
                        JSONArray data =response.getJSONArray("data");


                        for(int i = 0; i<data.length(); i++){
                            Book b = new Book();
                            b.JsonToBook(data.getJSONObject(i));
                            booksData.add(b);
                            Log.d("aa",b.getCondizione());
                            adapterBooks.add(b.getTitolo());
                        }


                        //adapterBooks.notifyDataSetChanged();

                    } else {
                        //try again, problema con il db
                    }
                } catch (Exception e) {

                }
            }});



        books.setAdapter(adapterBooks);

        books.setVisibility(View.VISIBLE);

        add = (Button) findViewById(R.id.btnAddBook);
        remove = (Button) findViewById(R.id.btnRemoveBooks);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               tMyBooks.putExtra("isNew", true);

                startActivity(tMyBooks);

                adapterBooks.notifyDataSetChanged();

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(removeList.size()>0)
                    removeDialog().show();
            }
        });


        books.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int i=position;

                if(isRemoving){
                    if(removeList.contains(i)){
                        removeList.remove(i);
                        view.setSelected(false);
                        if(removeList.isEmpty()){
                            isRemoving=false;
                            add.setEnabled(true);
                        }
                    }else{
                        removeList.add(i);
                        view.setSelected(true);
                    }

                }
                else{



                    tMyBooks.putExtra("id", Integer.toString(booksData.get(position).getID()));
                    //Log.d("gx8",booksData.get(position).getID());
                    tMyBooks.putExtra("Title", booksData.get(position).getTitolo());
                    tMyBooks.putExtra("Publisher", booksData.get(position).getCasaed());
                    tMyBooks.putExtra("ISBN", booksData.get(position).getISBN());
                    tMyBooks.putExtra("Amount", Integer.toString(booksData.get(position).getQuantita()));
                    tMyBooks.putExtra("Description", booksData.get(position).getDescrizione());
                    tMyBooks.putExtra("Price", Float.toString(booksData.get(position).getPrezzo()));
                    tMyBooks.putExtra("Authors", booksData.get(position).getAutore());


                    startActivity(tMyBooks);

                    adapterBooks.notifyDataSetChanged();
                }

            }
        });

        books.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                int i=position;

                removeList.add(i);

                isRemoving=true;

                add.setEnabled(false);

                view.setSelected(true);

                return true;
            }
        });


    }

    @Override
    public void onBackPressed() {
        if(isRemoving){
            for(int i=0; i<removeList.size(); i++){
                getViewByPosition(removeList.get(i), books).setSelected(false);
            }
            isRemoving = false;
            removeList.clear();
        }else{
            finish();
        }
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
       }



