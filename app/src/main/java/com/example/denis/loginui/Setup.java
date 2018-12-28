package com.example.denis.loginui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class Setup extends AppCompatActivity {

    Boolean success;

    Intent tStart;

    RelativeLayout rellay1, rellay2;

    Button start;
    Button birthDay;

    EditText name;
    EditText surname;
    EditText city;

    TextView setupText;
    TextView profilePic;

    ImageView imgLogo;

    Spinner gender;

    Handler hStart = new Handler();
    Runnable rStart = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
            setupText.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imgLogo.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
            imgLogo.setLayoutParams(params);
            profilePic.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        tStart = getIntent();

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

        hStart.postDelayed(rStart, 1700);

        imgLogo = (ImageView) findViewById(R.id.imgView_logo);

        gender = (Spinner) findViewById(R.id.spnGender);

        start = (Button) findViewById(R.id.btnStart);
        birthDay = (Button) findViewById(R.id.btnDate);

        name = (EditText) findViewById(R.id.edtName);
        surname = (EditText) findViewById(R.id.edtSurname);
        city = (EditText) findViewById(R.id.edtCity);

        setupText = (TextView) findViewById(R.id.txtSetup);
        profilePic = (TextView) findViewById(R.id.txtPicture);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                success = false;



            }
        });

        birthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //controllo se username è valido
    private static boolean is_Valid_Name(String x) {

        if (x.length() == 0) return false;

        boolean notSpecial= true ;
        boolean notNumber= true;

        int i=0;

        while(notSpecial && notNumber && i < x.length()) {

            char ch = x.charAt(i);

            if (is_Special(ch)) notSpecial=false;
            if(is_Numeric(ch)) notNumber=false;
            i++;
        }


        return (notSpecial && notNumber);
    }

    //controllo se è un numero
    private static boolean is_Numeric(char ch) {

        return (ch >= '0' && ch <= '9');
    }

    //controllo se è un carattere speciale
    private static boolean is_Special(char ch){
        String aux = Character.toString(ch);
        return (aux.matches("[^A-Za-z0-9 ]"));
    }
}
