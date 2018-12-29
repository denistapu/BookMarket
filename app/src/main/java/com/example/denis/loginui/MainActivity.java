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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Books" ;
    public static final int ACTIVITY_NUM = 0 ;

    Intent tStart;
    Intent tMyBooks;

    BottomNavigationView bottomNav;

    Boolean isRemoving;

    ArrayList bookList;

    ListView books;

    Button add;
    Button remove;

    ArrayAdapter adapterBooks;

    List <Integer> removeList;

    private Dialog removeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove");
        builder.setMessage("Are you sure to remove those book?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for(int i =0; i<removeList.size(); i++){
                            bookList.remove(removeList.get(i));
                            getViewByPosition(removeList.get(i), books).setSelected(false);
                            //devo cancellarlo dal DB
                        }
                        adapterBooks.notifyDataSetChanged();
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

        books.setAdapter(adapterBooks);

        //inserisco i dati dal database all'ArrayList bookList che poi setto sull'adapter

        adapterBooks.notifyDataSetChanged();

        add = (Button) findViewById(R.id.btnAddBook);
        remove = (Button) findViewById(R.id.btnRemoveBooks);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tMyBooks.putExtra("Title", "");
                tMyBooks.putExtra("Publisher", "");
                tMyBooks.putExtra("ISBN", "");
                tMyBooks.putExtra("Amount", "");
                tMyBooks.putExtra("Description", "");
                tMyBooks.putExtra("Pice", "");
                tMyBooks.putExtra("Authors", "");

                startActivity(tMyBooks);

                adapterBooks.notifyDataSetChanged();

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                    /*
                    qua negli extra va messo in ciascun campo il corrispettivo preso dal DB

                    tMyBooks.putExtra("Title", "");
                    tMyBooks.putExtra("Publisher", "");
                    tMyBooks.putExtra("ISBN", "");
                    tMyBooks.putExtra("Amount", "");
                    tMyBooks.putExtra("Description", "");
                    tMyBooks.putExtra("Pice", "");
                    tMyBooks.putExtra("Authors", "");
                    */

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
