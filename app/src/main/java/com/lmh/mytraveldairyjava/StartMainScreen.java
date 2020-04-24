package com.lmh.mytraveldairyjava;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class StartMainScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_main_screen);


    }


    public void callRegist(View view) {
        Toast.makeText(StartMainScreen.this, "로그인", Toast.LENGTH_SHORT).show();

    }

    public void callLogin(View view) {
        Toast.makeText(StartMainScreen.this, "회원가입", Toast.LENGTH_SHORT).show();
    }
}
