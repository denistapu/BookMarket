package com.example.denis.loginui;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Account extends AppCompatActivity {

    public static final String TAG = "Account" ;
    public static final int ACTIVITY_NUM = 3 ;

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        bottomNav = (BottomNavigationView) findViewById(R.id.navigationM);

        BottomViewHelper.enableNavigation(Account.this, bottomNav);

        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }
}
