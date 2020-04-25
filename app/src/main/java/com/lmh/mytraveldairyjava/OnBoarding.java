package com.lmh.mytraveldairyjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lmh.mytraveldairyjava.HelperClasses.SliderAdapter;

public class OnBoarding extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;
    Button bt_my01 , bt_my02;

    SliderAdapter sliderAdapter;
    TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        // hook
        viewPager = findViewById(R.id.onBoarding_ViewPager);
        dotsLayout = findViewById(R.id.dots);
        bt_my01 = findViewById(R.id.mybt01);
        bt_my02 = findViewById(R.id.mybt02);

        //call adaptor
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        addDots(0);;
        viewPager.addOnPageChangeListener(changeListener);
    }

    private  void  addDots(int position){

        //변형된 특수문자 . 를 4개 그려주는 구문..좀더 확인 필요
        dots = new TextView[4];
        dotsLayout.removeAllViews();;

        for (int i=0; i<dots.length;i++){
            dots[i] = new TextView(this);
            // &#8226 내장된 특수문자(변형된 .)
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }

        //슬라이드 페이지 변할때 아래 . 글자 색상 바꾸기
        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.whiteColor));
        }
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDots(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };




}
