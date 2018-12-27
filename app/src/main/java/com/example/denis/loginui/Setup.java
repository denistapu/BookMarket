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
import android.widget.TextView;

public class Setup extends AppCompatActivity {

    public static int stato;  //stato del setup

    Intent tStart;

    RelativeLayout rellay1, rellay2;

    Button next;
    Button previous;

    EditText name;
    EditText surname;

    ImageView imgLogo;

    TextView setupText;

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
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        stato = 1;

        tStart = getIntent();

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

        imgLogo = (ImageView) findViewById(R.id.imgView_logo);

        hStart.postDelayed(rStart, 1700);

        setupText = (TextView) findViewById(R.id.txtSetup);

        next = (Button) findViewById(R.id.btnNext);
        previous = (Button) findViewById(R.id.btnPrevious);

        name = (EditText) findViewById(R.id.edtName);
        surname = (EditText) findViewById(R.id.edtSurname);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
