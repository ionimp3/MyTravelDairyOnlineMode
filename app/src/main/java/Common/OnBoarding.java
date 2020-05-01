package Common;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lmh.mytraveldairyjava.R;

public class OnBoarding extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;
    Button bt_my01, bt_my02, play_Onboarding, skip_Onboarding;

    SliderAdapter sliderAdapter;
    TextView[] dots;
    Animation animation;

    int currentPos;

    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private Toast toast;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.onboardinglayout);

        ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

        //appLoginCheck();


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

/*    private void appLoginCheck() {

            Intent intent = new Intent(OnBoarding.this, ProfileActivity.class);
            startActivity(intent);
            finish();

        //
    }*/

    //skip 버튼

    public void skip(View view) {
        //SKIP 시 로그인 화면으로
        Intent intent = new Intent(OnBoarding.this, SignInActivity.class);
        startActivity(intent);
        finish();
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
        finish();

    }

    //로그인버튼 클릭시
    public void callLogin(View view) {
        Intent intent = new Intent(OnBoarding.this, SignInActivity.class);
        startActivity(intent);
        finish();

    }

    @SuppressLint("Assert")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        //뒤로가기버튼 처음 실행안되게 최기화 : 3초

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            // finish();
            assert false;
            toast.cancel();
            // app을 완전히 종료
            moveTaskToBack(true);						// 태스크를 백그라운드로 이동
            finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
            android.os.Process.killProcess(android.os.Process.myPid());	// 앱 프로세스 종료
            //
        }
    }
}
