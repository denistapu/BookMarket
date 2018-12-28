package com.example.denis.loginui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Books" ;
    public static final int ACTIVITY_NUM = 0 ;

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = (BottomNavigationView) findViewById(R.id.navigationM);

        BottomViewHelper.enableNavigation(MainActivity.this, bottomNav);

        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
