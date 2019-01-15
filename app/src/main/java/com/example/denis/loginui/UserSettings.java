package com.example.denis.loginui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.denis.loginui.CheckInput.is_Valid_Name;
import static com.example.denis.loginui.CheckInput.is_Valid_Usrrname;

public class UserSettings extends AppCompatActivity {

    Boolean isModified;

    Intent tStart;

    int REQUEST_CODE = 1;

    DatePickerDialog.OnDateSetListener datePicker;

    Button back;
    Button save;
    Button birthDay;


    EditText email;
    EditText username;
    EditText name;
    EditText surname;
    EditText city;

    TextView errorEmail;
    TextView errorUsername;
    TextView error;

    ImageView imgLogo;

    Spinner gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        tStart = getIntent();

        isModified= false;

        imgLogo = (ImageView) findViewById(R.id.userPic);

        gender = (Spinner) findViewById(R.id.spnGenderU);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(UserSettings.this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);
        gender.setSelection(1);


        save = (Button) findViewById(R.id.btnSaveUserSet);
        back = (Button) findViewById(R.id.btnBackUserSet);
        birthDay = (Button) findViewById(R.id.btnDate);

        email = (EditText) findViewById(R.id.edtEmailU);
        username = (EditText) findViewById(R.id.edtUsernameU);
        name = (EditText) findViewById(R.id.edtNameU);
        surname = (EditText) findViewById(R.id.edtSurnameU);
        city = (EditText) findViewById(R.id.edtCityU);

        errorEmail = (TextView) findViewById(R.id.txtErrorEmailU);
        errorUsername = (TextView) findViewById(R.id.txtErrorUsernameU);
        error = (TextView) findViewById(R.id.txtErrorU);


        email.setText(tStart.getStringExtra("Email"));
        username.setText(tStart.getStringExtra("Username"));
        name.setText(tStart.getStringExtra("Name"));
        surname.setText(tStart.getStringExtra("Surname"));
        city.setText(tStart.getStringExtra("City"));
        birthDay.setText(tStart.getStringExtra("BirthDay"));
        gender.setSelection(Integer.parseInt(tStart.getStringExtra("BirthDay")));
        /*trovare un modo per far prendere all'imageView la foto tramite quello che gli passo nell'Extra
        imgLogo.setImageBitmap(tStart.getStringExtra("ProfilePicture"));
        */

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorEmail.setVisibility(View.GONE);
                errorUsername.setVisibility(View.GONE);
                error.setVisibility(View.GONE);

                String emailStr = email.getText().toString();
                String usernameStr = username.getText().toString();
                String nameStr = name.getText().toString();
                String surnameStr = surname.getText().toString();
                String cityStr = city.getText().toString();
                String genderStr = gender.getSelectedItem().toString();
                String birthStr = birthDay.getText().toString();

                //devo trovare un modo per prendere anche la foto profilo

//-------------------------------------------------------------------------------------------------------------------------
                Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
                Matcher matcher = pattern.matcher(emailStr);
                if(!matcher.matches() /* oppure se gia in uso */){
                    errorEmail.setVisibility(View.VISIBLE);

                }else if(!emailStr.equals(tStart.getStringExtra("Email"))){
                    isModified=true;
                }

//-------------------------------------------------------------------------------------------------------------------------

                if(!is_Valid_Usrrname(usernameStr)/*oppure username gia un uso*/){
                    errorUsername.setVisibility(View.VISIBLE);
                }else if(!usernameStr.equals(tStart.getStringExtra("Username"))){
                    isModified = true;
                }


//-------------------------------------------------------------------------------------------------------------------------

                if(!is_Valid_Name(nameStr)){

                    error.setVisibility(View.VISIBLE);
                }else if(!nameStr.equals(tStart.getStringExtra("Name"))){
                    isModified = true;
                }

//-------------------------------------------------------------------------------------------------------------------------

                if(!is_Valid_Name(surnameStr)){

                    error.setVisibility(View.VISIBLE);
                }else if(!surnameStr.equals(tStart.getStringExtra("Surname"))){
                    isModified = true;

                }

//-------------------------------------------------------------------------------------------------------------------------

                if(!is_Valid_Name(cityStr)){

                    error.setVisibility(View.VISIBLE);
                }else if(!cityStr.equals(tStart.getStringExtra("City"))){
                    isModified = true;
                }

//-------------------------------------------------------------------------------------------------------------------------

               if(!genderStr.equals(tStart.getStringExtra("Gender"))){
                    isModified = true;
                }

//-------------------------------------------------------------------------------------------------------------------------

                if(!birthStr.equals(tStart.getStringExtra("BirthDay"))){
                    isModified = true;
                }

//-------------------------------------------------------------------------------------------------------------------------

                //bisogna controllare allo stesso modo l'immagine
//-------------------------------------------------------------------------------------------------------------------------


                if(isModified){
                    //salva sul DB i dati del account
                    Toast.makeText(getApplicationContext(),"User settings saved correctly!", Toast.LENGTH_SHORT).show();
                    finish();
                }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                        UserSettings.this,
                        android.R.style.Theme_Material_Light_DarkActionBar,
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
