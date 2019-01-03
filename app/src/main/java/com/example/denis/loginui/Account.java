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
    public static final int ACTIVITY_NUM = 3 ;

    Intent tStart;
    Intent tLogin;
    Intent tChangePsw;
    Intent tUserSet;

    BottomNavigationView bottomNav;

    Button userSet;
    Button changePsw;
    Button logout;


    private Dialog logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure to logout?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        tLogin = new Intent(Account.this, Login.class);
                        tLogin.putExtra("Logout", "Yes");
                        startActivity(tLogin);
                        //poi sul login bisogna togliere in qualche modo le preferences fatte col remember me
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

        bottomNav = (BottomNavigationView) findViewById(R.id.navigationM);

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

                /*nei rispettivi valori va messo il valore dal DB
                tUserSet.putExtra("ProfilePicture","");
                tUserSet.putExtra("Email","");
                tUserSet.putExtra("Username","");
                tUserSet.putExtra("Name","");
                tUserSet.putExtra("Surname","");
                tUserSet.putExtra("City","");
                tUserSet.putExtra("BirthDay","");
                tUserSet.putExtra("Gender","");
                */

                startActivity(tUserSet);

            }
        });

        changePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tChangePsw = new Intent(Account.this, ChangePassword.class);
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
