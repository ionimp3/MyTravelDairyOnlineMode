package com.lmh.mytraveldairyjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.lmh.mytraveldairyjava.R;

public class UserDashboard extends AppCompatActivity {
    // BackPressHandler 객체 선언, 할당
    private BackPressHandler backPressHandler = new BackPressHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dashboardlayout);

    }
 @Override
    public void onBackPressed() {
        // Default
        //backPressHandler.onBackPressed();
        // Toast 메세지 사용자 지정
        backPressHandler.onBackPressed("뒤로가기 버튼 한번 더 누르면 앱을 종료");
        // 뒤로가기 간격 사용자 지정
        //backPressHandler.onBackPressed(3000);
        // Toast, 간격 사용자 지정
        //backPressHandler.onBackPressed("뒤로가기 버튼 한번 더 누르면 종료", 3000);

    }
}