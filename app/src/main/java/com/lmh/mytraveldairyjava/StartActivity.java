package com.lmh.mytraveldairyjava;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class StartActivity extends AppCompatActivity {

//테스트...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

       ImageButton imageButton = (ImageButton) findViewById(R.id.btnnewplan);

       imageButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(), DairyNew.class);
               startActivity(intent);
           }
       });

    }
}
