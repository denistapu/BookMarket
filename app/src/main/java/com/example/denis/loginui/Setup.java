package com.example.denis.loginui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.Calendar;

import static com.example.denis.loginui.CheckInput.is_Valid_Name;

public class Setup extends AppCompatActivity {

    Boolean success;

    int REQUEST_CODE = 1;

    DatePickerDialog.OnDateSetListener datePicker;

    Intent tStart;
    Intent tGo;

    RelativeLayout rellay1, rellay2;

    Button start;
    Button birthDay;

    EditText name;
    EditText surname;
    EditText city;

    TextView setupText;
    TextView profilePic;
    TextView error;

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
            imgLogo.setClickable(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        tStart = getIntent();

        rellay1 = (RelativeLayout) findViewById(R.id.rellaySetup);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay1Setup);
        Log.d("testgx8", Boolean.toString(rellay1!=null));

        hStart.postDelayed(rStart, 1700);

        imgLogo = (ImageView) findViewById(R.id.imgView_setup);

        gender = (Spinner) findViewById(R.id.spnGender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Setup.this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);
        gender.setSelection(0);


        start = (Button) findViewById(R.id.btnStart);
        birthDay = (Button) findViewById(R.id.btnDate);

        name = (EditText) findViewById(R.id.edtName);
        surname = (EditText) findViewById(R.id.edtSurname);
        city = (EditText) findViewById(R.id.edtCity);

        setupText = (TextView) findViewById(R.id.txtSetup);
        profilePic = (TextView) findViewById(R.id.txtPicture);
        error = (TextView) findViewById(R.id.txtErrorSetup);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                error.setVisibility(View.GONE);

                String nameStr = name.getText().toString();
                String surnameStr = surname.getText().toString();
                String cityStr = city.getText().toString();
                String genderStr = gender.getSelectedItem().toString();
                String birthStr = birthDay.getText().toString();

                success = true;

                if(!is_Valid_Name(nameStr)){
                    success = false;
                    error.setVisibility(View.VISIBLE);
                }

                if(!is_Valid_Name(surnameStr)){
                    success = false;
                    error.setVisibility(View.VISIBLE);
                }

                if(!is_Valid_Name(cityStr)){
                    success = false;
                    error.setVisibility(View.VISIBLE);
                }


                if(birthStr.equals("Birth Day")){
                    success=false;
                }

                if(success){
                    //salva i dati sul DB (non dimenticarsi della foto profilo)
                    //imposta il booleano setup sul DB a true
                    tGo = new Intent(Setup.this, MainActivity.class);
                    finish();
                }

            }
        });

        birthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year= calendar.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(
                        Setup.this,
                        android.R.style.Theme_DeviceDefault_Light_DarkActionBar,
                        datePicker,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                birthDay.setText(date);
            }
        };

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent t = new Intent();
                t.setType("image/*");
                t.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(t, "Select Profile Pirture"), REQUEST_CODE);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK  && data != null && data.getData() != null){
            Uri uri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imgLogo.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }




}
