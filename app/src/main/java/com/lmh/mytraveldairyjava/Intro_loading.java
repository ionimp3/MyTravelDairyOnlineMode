package com.lmh.mytraveldairyjava;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Intro_loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_loading);

        //액션바제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 3000); // 1초 후에 hd handler 실행  3000ms = 3초


    }

    private class splashhandler implements Runnable {

        public void run() {
            startActivity(new Intent(getApplication(), LoginEmail.class)); //로딩이 끝난 후, ChoiceFunction 이동
            Intro_loading.this.finish(); // 로딩페이지 Activity stack에서 제거

        }
    }
}
