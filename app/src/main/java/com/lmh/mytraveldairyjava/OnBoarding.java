package com.lmh.mytraveldairyjava;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lmh.mytraveldairyjava.SignInActivity;

public class OnBoarding extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;
    Button bt_my01, bt_my02, play_Onboarding, skip_Onboarding;

    SliderAdapter sliderAdapter;
    TextView[] dots;
    Animation animation;

    int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.onboardinglayout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        appLoginCheck();


        // hook
        viewPager = findViewById(R.id.onBoarding_ViewPager);
        dotsLayout = findViewById(R.id.dots);
        bt_my01 = findViewById(R.id.mybtLogin);
        bt_my02 = findViewById(R.id.mybtRegist);
        skip_Onboarding = findViewById(R.id.onBoarding_Skip);
        play_Onboarding = findViewById(R.id.onBoarding_play);


        //call adaptor
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        //dots
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }

    private void appLoginCheck() {
        //로그인확인
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(OnBoarding.this, ProfileActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(OnBoarding.this
                    , "로그인 해주세요..", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
            // startActivity(intent);
        }
        //
    }

    //skip 버튼

    public void skip(View view) {
        //SKIP 시 로그인 화면으로
        Intent intent = new Intent(OnBoarding.this, SignInActivity.class);
        startActivity(intent);
    }

    public void playon(View view) {

        viewPager.setCurrentItem(currentPos + 1);

    }


    private void addDots(int position) {

        //변형된 특수문자 . 를 4개 그려주는 구문..좀더 확인 필요
        dots = new TextView[4];
        dotsLayout.removeAllViews();
        ;

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            // &#8226 내장된 특수문자(변형된 .)
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }

        //슬라이드 페이지 변할때 아래 . 글자 색상 바꾸기
        if (dots.length > 0) {
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

            currentPos = position;

            if (position == 0) {
                animation = AnimationUtils.loadAnimation(OnBoarding.this, R.anim.an2_bottom_animation);
                bt_my01.setAnimation(animation);
                bt_my02.setAnimation(animation);
                play_Onboarding.setVisibility(View.VISIBLE);
                bt_my01.setVisibility(View.VISIBLE);
                bt_my02.setVisibility(View.VISIBLE);
            } else if (position == 1) {
                play_Onboarding.setVisibility(View.VISIBLE);
                bt_my01.setVisibility(View.INVISIBLE);
                bt_my02.setVisibility(View.INVISIBLE);
            } else if (position == 2) {
                play_Onboarding.setVisibility(View.VISIBLE);
                bt_my01.setVisibility(View.INVISIBLE);
                bt_my02.setVisibility(View.INVISIBLE);
            } else {
                animation = AnimationUtils.loadAnimation(OnBoarding.this, R.anim.an2_side_animation);
                bt_my01.setAnimation(animation);
                bt_my02.setAnimation(animation);

                play_Onboarding.setVisibility(View.VISIBLE);
                bt_my01.setVisibility(View.VISIBLE);
                bt_my02.setVisibility(View.VISIBLE);

            }
        }


        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    //회원가입버튼 클릭
    public void callRegist(View view) {

        Intent intent = new Intent(OnBoarding.this, SignUpActivity.class);
        startActivity(intent);

    }

    //로그인버튼 클릭시
    public void callLogin(View view) {
        Intent intent = new Intent(OnBoarding.this, SignInActivity.class);
        startActivity(intent);

    }
}
