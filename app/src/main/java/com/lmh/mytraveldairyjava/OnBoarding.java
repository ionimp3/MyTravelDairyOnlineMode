package com.lmh.mytraveldairyjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lmh.mytraveldairyjava.HelperClasses.SliderAdapter;

public class OnBoarding extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dots;
    Button bt_my01 , bt_my02;

    SliderAdapter sliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        // hook
        viewPager = findViewById(R.id.onBoarding_ViewPager);
        dots = findViewById(R.id.dots);
        bt_my01 = findViewById(R.id.mybt01);
        bt_my02 = findViewById(R.id.mybt02);

        //call adaptor
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);


    }
}
