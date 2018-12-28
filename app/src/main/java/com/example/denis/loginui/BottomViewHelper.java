package com.example.denis.loginui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;


public class BottomViewHelper {

    private static final String TAG = "BottomNavigation";

    public static void enableNavigation(final Context context, BottomNavigationView view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.books:
                        Intent intent1 = new Intent(context, MainActivity.class);//ACTIVITY_NUM = 0
                        context.startActivity(intent1);
                        ((Activity) context).finish();
                        break;

                    case R.id.search:
                        Intent intent2  = new Intent(context, Search.class);//ACTIVITY_NUM = 1
                        context.startActivity(intent2);
                        ((Activity) context).finish();
                        break;

                    case R.id.chats:
                        Intent intent3 = new Intent(context, Chat.class);//ACTIVITY_NUM = 2
                        context.startActivity(intent3);
                        ((Activity) context).finish();
                        break;

                    case R.id.account:
                        Intent intent4 = new Intent(context, Account.class);//ACTIVITY_NUM = 3
                        context.startActivity(intent4);
                        ((Activity) context).finish();
                        break;
                }


                return false;
            }
        });
    }

}
