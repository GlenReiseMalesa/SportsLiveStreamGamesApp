package com.reise.hessportsportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {

    public static final int PERMISSION_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashScreen();
    }

    private void splashScreen() {

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {

                        //ask for permission
                        permissions();


                        startActivity(new Intent(Splash.this,MainActivity.class));
                        finish();
                    }
                },2000
        );
    }

    private void permissions() {


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)){
                //request permission
                ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.INTERNET},PERMISSION_REQUEST);
            }else{
                //request permission
                ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.INTERNET},PERMISSION_REQUEST);
            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{
                    finish();
                }
                return;
            }
        }
    }

}
