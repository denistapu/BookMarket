package com.example.denis.loginui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Account extends AppCompatActivity {

    public static final String TAG = "Account" ;
    public static final int ACTIVITY_NUM = 2 ;

    Intent tStart;
    Intent tLogin;
    Intent tChangePsw;
    Intent tUserSet;
    SessionManager session;

    BottomNavigationView bottomNav;

    Button userSet;
    Button changePsw;
    Button logout;


    private Dialog logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.LightDialogTheme);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure to logout?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        tLogin = new Intent(Account.this, Login.class);
                        session.logout();
                        startActivity(tLogin);
                        finish();
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
        setContentView(R.layout.activity_account);

        tStart = getIntent();

        session = new SessionManager(this);
        bottomNav = (BottomNavigationView) findViewById(R.id.navigationA);

        BottomViewHelper.enableNavigation(Account.this, bottomNav);

        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

        userSet = (Button) findViewById(R.id.btnUserSettings);
        changePsw = (Button) findViewById(R.id.btnChangePass);
        logout = (Button) findViewById(R.id.btnLogout);

        userSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tUserSet = new Intent(Account.this, UserSettings.class);

                startActivity(tUserSet);

            }
        });

        changePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tChangePsw = new Intent(Account.this, ChangePassword.class);
                startActivity(tChangePsw);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog().show();
            }
        });

    }
}
