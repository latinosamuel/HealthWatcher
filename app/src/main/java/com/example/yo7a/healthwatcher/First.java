package com.example.yo7a.healthwatcher;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class First extends AppCompatActivity {

    public ImageButton Meas;
    public Button acc;
    public EditText ed1,ed2;
    private Toast mainToast;
    public static String passStr,usrStr,checkpassStr,usrStrlow;
    UserDB check = new UserDB(this);
    CheckBox chkRememberMe;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 555;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Meas = (ImageButton) findViewById(R.id.prime);
        acc = (Button) findViewById(R.id.newacc);
        ed1 = (EditText) findViewById(R.id.edtu1);
        ed2 = (EditText) findViewById(R.id.edtp1);
        chkRememberMe = (CheckBox) findViewById(R.id.checkBoxRemember);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);


        if (saveLogin == true) {
            ed1.setText(loginPreferences.getString("username", ""));
            ed2.setText(loginPreferences.getString("password", ""));
           chkRememberMe.setChecked(true);
        }

        Meas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(First.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(First.this, new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                }else {
                    login();
                }
            }
        });

        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), Login.class);
                startActivity(i);
                finish();

            }
        });

    }

    private void login(){
        usrStrlow = ed1.getText().toString();
        passStr = ed2.getText().toString();
        usrStr=usrStrlow.toLowerCase();


        if (usrStr.length() < 3 || usrStr.length() > 20) {
            mainToast = Toast.makeText(getApplicationContext(), "Username length must be between 3-20 characters", Toast.LENGTH_SHORT);
            mainToast.show();
        }

        if (passStr.length() < 3 || passStr.length() > 20) {
            mainToast = Toast.makeText(getApplicationContext(), "Password length must be between 3-20 characters", Toast.LENGTH_SHORT);
            mainToast.show();
        }

        else if ( passStr.isEmpty() || usrStr.isEmpty()) {

            mainToast = Toast.makeText(getApplicationContext(), "Please enter your Username and Password ", Toast.LENGTH_SHORT);
            mainToast.show();


        }

        else{

            checkpassStr = check.checkPass(usrStr);

            if(passStr.equals(checkpassStr))
            {

                if (chkRememberMe.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", usrStr);
                    loginPrefsEditor.putString("password", passStr);
                    loginPrefsEditor.apply();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }

                Intent i = new Intent(First.this, Primary.class);
                i.putExtra("Usr",usrStr);
                startActivity(i);
                finish();

            }

            else {
                //Toast something
                mainToast = Toast.makeText(getApplicationContext(), "Username/Password is incorrect", Toast.LENGTH_SHORT);
                mainToast.show();
            }
        }
    }

    private void  requestPermissionCamera(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    login();
                } else {
                    requestPermissionCamera(this);
                }
            }
        }
    }
}
