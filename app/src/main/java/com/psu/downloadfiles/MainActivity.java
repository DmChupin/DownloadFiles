package com.psu.downloadfiles;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
/*
  Права на создание файла
*/
    private void getPermissions(){

        int MY_WRITE_EXTERNAL_REQUEST  = 1;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_WRITE_EXTERNAL_REQUEST);
            }
        }
    }

    public void onDownloadBtClick(View view){
        getPermissions();

        EditText editURL = findViewById(R.id.editURL);

        Intent intent = new Intent(this, downloadService.class);
        intent.putExtra("url",editURL.getText().toString());

        startService(intent);
    }

    public void onStopDownloadBtClick(View view){
        Intent intent = new Intent(this, downloadService.class);
        stopService(intent);
    }
}
