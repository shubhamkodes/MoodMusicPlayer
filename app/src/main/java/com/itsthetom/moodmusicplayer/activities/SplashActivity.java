package com.itsthetom.moodmusicplayer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import com.itsthetom.moodmusicplayer.R;

import org.jetbrains.annotations.NotNull;

public class SplashActivity extends AppCompatActivity {
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       i=new Intent(this,MainActivity.class);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(i);
                    finishAffinity();
                }
            },3000);
        }else{
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},69);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==69){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                startActivity(i);
            }else{
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},69);
            }
        }
    }
}