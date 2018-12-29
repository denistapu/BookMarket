package com.example.denis.loginui;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Chat extends AppCompatActivity {

    public static final String TAG = "Chat" ;
    public static final int ACTIVITY_NUM = 2 ;

    BottomNavigationView bottomNav;

    Intent tStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tStart= getIntent();

        bottomNav = (BottomNavigationView) findViewById(R.id.navigationM);

        BottomViewHelper.enableNavigation(Chat.this, bottomNav);

        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
