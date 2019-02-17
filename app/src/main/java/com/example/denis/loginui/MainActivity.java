package com.example.denis.loginui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.graphics.Color;

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
    int selected;

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
                            HashMap<String,String> params = new HashMap<String,String>();
                            params.put("username", session.getUser().getUsername());
                            params.put("auth", session.getUser().getAuth());
                            params.put("bookID", Integer.toString(booksData.get(removeList.get(i)).getID()));
                            booksData.remove(removeList.get(i));
                            adapterBooks.remove(adapterBooks.getItem(removeList.get(i)));
                            TextView tv = (TextView) getViewByPosition(removeList.get(i), books).findViewById(android.R.id.text1);
                            tv.setTextColor(Color.BLACK);
                            requests.execRequest("removeBook", params,new Response.Listener<String>() {

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
                                            Toast.makeText(MainActivity.this, "Book removed", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Couldn't remove the book", Toast.LENGTH_SHORT).show();
                                        }

                                        }catch(JSONException e){
                                    }
                                }});
                        }

                       // adapterBooks.notifyDataSetChanged();
                        removeList.clear();
                        isRemoving=false;
                    }
                });

        builder.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        getViewByPosition(selected,books).setSelected(false);
                        adapterBooks.notifyDataSetChanged();
                        removeList.clear();
                        isRemoving=false;
                        add.setEnabled(true);
                    }
                });

        AlertDialog alert = builder.create();
        return alert;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selected = -1;
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

        HashMap<String,String> args = new HashMap<String,String>();
        args.put("request", "getBooks");
        args.put("owner", session.getUser().getUsername());
        adapterBooks = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                Typeface textFont= ResourcesCompat.getFont(getApplicationContext(), R.font.bookshelves);

                text.setTypeface(textFont);
                text.setTextColor(Color.WHITE);


                return view;
            }
        };
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
                tMyBooks.putExtra("id", "");
                tMyBooks.putExtra("Title", "");
                tMyBooks.putExtra("Publisher", "");
                tMyBooks.putExtra("ISBN", "");
                tMyBooks.putExtra("Amount", "");
                tMyBooks.putExtra("Description", "");
                tMyBooks.putExtra("Price","");
                tMyBooks.putExtra("Authors", "");
               tMyBooks.putExtra("isNew", true);
                if(isRemoving){
                    for(int i=0; i<removeList.size(); i++){
                        TextView tv = (TextView) getViewByPosition(removeList.get(i), books).findViewById(android.R.id.text1);
                        tv.setTextColor(Color.BLACK);
                    }
                    isRemoving = false;
                    removeList.clear();
                }

                startActivity(tMyBooks);

                adapterBooks.notifyDataSetChanged();

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!removeList.isEmpty())
                    removeDialog().show();
                else{
                    Toast.makeText(MainActivity.this, "Select books you intend to remove first!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        books.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int i=position;

                if(isRemoving){
                    if(removeList.contains(i)){
                        removeList.remove(removeList.indexOf(i));

                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
                        tv.setTextColor(Color.BLACK);

                        if(removeList.isEmpty()){
                            isRemoving=false;
                        }
                    }else{
                        removeList.add(i);
                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
                        tv.setTextColor(Color.RED);
                    }

                }
                else{



                    tMyBooks.putExtra("id", Integer.toString(booksData.get(position).getID()));
                    Log.d("gx8","LMAO: "+Integer.toString(booksData.get(position).getID()));
                    tMyBooks.putExtra("Title", booksData.get(position).getTitolo());
                    tMyBooks.putExtra("Publisher", booksData.get(position).getCasaed());
                    tMyBooks.putExtra("ISBN", booksData.get(position).getISBN());
                    tMyBooks.putExtra("Amount", Integer.toString(booksData.get(position).getQuantita()));
                    tMyBooks.putExtra("Description", booksData.get(position).getDescrizione());
                    tMyBooks.putExtra("Price", Float.toString(booksData.get(position).getPrezzo()));
                    tMyBooks.putExtra("Authors", booksData.get(position).getAutore());
                    tMyBooks.putExtra("isNew", false);

                    startActivity(tMyBooks);

                    adapterBooks.notifyDataSetChanged();
                }

            }
        });

        books.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                int i=position;

                if(!isRemoving) {
                    removeList.add(i);
                    isRemoving = true;
                    selected = i;
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    tv.setTextColor(Color.RED);
                }
                return true;
            }
        });


    }

    @Override
    public void onBackPressed() {
        if(isRemoving){
            for(int i=0; i<removeList.size(); i++){
                TextView tv = (TextView) getViewByPosition(removeList.get(i), books).findViewById(android.R.id.text1);
                tv.setTextColor(Color.BLACK);
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



