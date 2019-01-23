package com.example.denis.loginui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.denis.loginui.CheckInput.DRAWABLE_RIGHT;
import static com.example.denis.loginui.CheckInput.is_Valid_Password;

public class ChangePassword extends AppCompatActivity {

    Boolean success;
    Boolean showOldPass;
    Boolean showNewPass;
    Boolean showConfPsw;

    Intent tStart;

    Button back;
    Button change;

    EditText oldP;
    EditText newP;
    EditText confP;
    RequestsManager requests;
    TextView errorOld;
    TextView errorNew;
    TextView errorConfN;

    private Dialog changeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Password");
        builder.setMessage("Are you sure to change passowrd?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),"Password saved correctly!", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_change_password);

        tStart = getIntent();

        back = (Button) findViewById(R.id.btnBackChangePsw);
        change = (Button) findViewById(R.id.btnBackChangePsw);

        oldP = (EditText) findViewById(R.id.edtOldP);
        newP = (EditText) findViewById(R.id.edtNewP);
        confP = (EditText) findViewById(R.id.edtConfP);

        errorOld = (TextView) findViewById(R.id.txtErrorOldPassword);
        errorNew = (TextView) findViewById(R.id.txtErrorNewPassword);
        errorConfN = (TextView) findViewById(R.id.txtErrorConfirmNewPassword);
        requests = new RequestsManager(this);
        showOldPass=false;
        showNewPass=false;
        showConfPsw=false;

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldStr = oldP.getText().toString();
                String newStr = newP.getText().toString();
                String confStr = confP.getText().toString();

                errorOld.setVisibility(View.GONE);
                errorNew.setVisibility(View.GONE);
                errorConfN.setVisibility(View.GONE);

                success = true;

                //controllo se old password rispetta requisiti e sia quella dell'utente
                if(!is_Valid_Password(oldStr) /*|| oppure se non è quella dell'utente*/){
                    errorOld.setVisibility(View.VISIBLE);
                    success=false;
                }

                //controllo se new rispetta requisiti
                if(!is_Valid_Password(newStr) || newStr.equals(oldStr)){
                    errorNew.setVisibility(View.VISIBLE);
                    success=false;
                }

                //controllo se confirm è uguale a new
                if(!newStr.equals(confStr)){
                    errorConfN.setVisibility(View.VISIBLE);
                    success=false;
                }

                //se tutto va bene attivo faccio apparire dialogAlert
                if(success){
                    changeDialog().show();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        oldP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (oldP.getRight() - oldP.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if(showOldPass){
                            oldP.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_off, 0);
                            showOldPass=false;
                            oldP.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            return true;
                        }else{
                            oldP.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_on, 0);
                            showOldPass=true;
                            oldP.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            return false;
                        }


                    }
                }

                return false;
            }
        });

        newP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (newP.getRight() - newP.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if(showNewPass){
                            newP.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_off, 0);
                            showNewPass=false;
                            newP.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            return true;
                        }else{
                            newP.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_on, 0);
                            showNewPass=true;
                            newP.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            return false;
                        }


                    }
                }

                return false;
            }
        });

        confP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (confP.getRight() - confP.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if(showConfPsw){
                            confP.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_off, 0);
                            showConfPsw=false;
                            confP.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            return true;
                        }else{
                            confP.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye_on, 0);
                            showConfPsw=true;
                            confP.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            return false;
                        }


                    }
                }

                return false;
            }
        });


    }
}
