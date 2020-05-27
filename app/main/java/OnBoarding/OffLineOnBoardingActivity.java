package OnBoarding;

import android.annotation.SuppressLint;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.lmh.mytraveldairy.OfflineMainActivity;
import com.lmh.mytraveldairy.R;

import MyProgressDialog.MyProgressDialogActivity;
import SignFolder.SignInActivity;

public class OffLineOnBoardingActivity extends AppCompatActivity {

    MyProgressDialogActivity myProgressDialogActivity;

    ViewPager viewPager;
    LinearLayout dotsLayout;
    Button btnskipOnBoarding;
    Button btnLetsGo;
    Button btnOnline;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_offline_on_boarding);


        // hook
        viewPager = findViewById(R.id.partial_onBoarding_ViewPager);
        dotsLayout = findViewById(R.id.partial_onBoarding_dots);
        btnskipOnBoarding = findViewById(R.id.btn_Skip);
        btnOnline = findViewById(R.id.btn_onBoarding_online);
        btnLetsGo = findViewById(R.id.btn_lets_go);


        //call adaptor
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        //dots
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    private void addDots(int position) {

        //변형된 특수문자 . 를 4개 그려주는 구문..좀더 확인 필요
        dots = new TextView[4];
        dotsLayout.removeAllViews();

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

        @SuppressLint("ResourceType")
        @Override
        public void onPageSelected(int position) {

            addDots(position);

            currentPos = position;

            if (position == 0) {
                animation = AnimationUtils.loadAnimation(OffLineOnBoardingActivity.this, R.animator.ani_onboarding_bottom_animation);
                btnskipOnBoarding.setAnimation(animation);
                btnskipOnBoarding.setVisibility(View.INVISIBLE);
                btnOnline.setAnimation(animation);
                btnOnline.setVisibility(View.VISIBLE);
                btnLetsGo.setAnimation(animation);
                btnLetsGo.setVisibility(View.VISIBLE);
            } else if (position == 1) {
                btnskipOnBoarding.setAnimation(animation);
                btnskipOnBoarding.setVisibility(View.VISIBLE);
                btnOnline.setAnimation(animation);
                btnOnline.setVisibility(View.VISIBLE);
                btnLetsGo.setAnimation(animation);
                btnLetsGo.setVisibility(View.INVISIBLE);
            } else if (position == 2) {
                btnskipOnBoarding.setAnimation(animation);
                btnskipOnBoarding.setVisibility(View.VISIBLE);
                btnOnline.setAnimation(animation);
                btnOnline.setVisibility(View.VISIBLE);
                btnLetsGo.setAnimation(animation);
                btnLetsGo.setVisibility(View.INVISIBLE);
            } else {
                animation = AnimationUtils.loadAnimation(OffLineOnBoardingActivity.this, R.animator.ani_onboarding_side_animation);
                btnskipOnBoarding.setAnimation(animation);
                btnskipOnBoarding.setVisibility(View.INVISIBLE);
                btnOnline.setAnimation(animation);
                btnOnline.setVisibility(View.VISIBLE);
                btnLetsGo.setAnimation(animation);
                btnLetsGo.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void btn_action_skip(View view) {
        Intent gotoSMainActivity = new Intent(OffLineOnBoardingActivity.this, OfflineMainActivity.class);
        gotoSMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(gotoSMainActivity);
        finish();
    }

    int i = 0;

    public void btn_action_next(View view) {
        viewPager.setCurrentItem(currentPos + 1);
    }

    @SuppressLint("Assert")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else {
            moveTaskToBack(true);                        // 태스크를 백그라운드로 이동
            finishAndRemoveTask();                        // 액티비티 종료 + 태스크 리스트에서 지우기
            android.os.Process.killProcess(android.os.Process.myPid());    // 앱 프로세스 종료
        }
    }

    public void btn_action_lets_go(View view) {
        Intent gotoOfflineMainActivity = new Intent(OffLineOnBoardingActivity.this, OfflineMainActivity.class);
        gotoOfflineMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(gotoOfflineMainActivity);
        finish();
    }

    public void btn_action_online_mode(View view) {
        Intent gotoSingInActivity = new Intent(OffLineOnBoardingActivity.this, SignInActivity.class);
        gotoSingInActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(gotoSingInActivity);
        finish();
    }
}
