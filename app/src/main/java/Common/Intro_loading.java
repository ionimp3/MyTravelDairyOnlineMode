package Common;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.lmh.mytraveldairyjava.R;

public class Intro_loading extends AppCompatActivity {
    private static int SPLASH_SCREEN = 2000;

    //animation : ani1
    Animation ani1_topAnim, ani1_bottomAnim;

    //변수
    ImageView introlmage;
    TextView introLogo, introComment;

    SharedPreferences OnBoardingScreen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //화면전체를 사용한다고 정의, 액션바설정은 표시 그러므로 액션바 부분까지 표시할려면 STYLE에서 NOACTIONBAR 테마로
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.intro_loading);

        ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

        //animation
        ani1_topAnim = AnimationUtils.loadAnimation(this, R.anim.ani1_top_animation);
        ani1_bottomAnim = AnimationUtils.loadAnimation(this, R.anim.ani1_bottom_animation);

        //hook
        introlmage = findViewById(R.id.loading_intro);
        introLogo = findViewById(R.id.intro_logo);
        introComment = findViewById(R.id.intro_comment);

        introlmage.setAnimation(ani1_topAnim);
        introLogo.setAnimation(ani1_bottomAnim);
        introComment.setAnimation(ani1_bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //애니메이션후 글자색,바탕색 변경
                introLogo.setTextColor(Color.parseColor("#ffffff"));
                introComment.setTextColor(Color.parseColor("#ffffff"));
                introLogo.setBackgroundColor(Color.parseColor("#2C2A2A"));
                introComment.setBackgroundColor(Color.parseColor("#2C2A2A"));
                //

                //타이틀색상 변형 유지시간 핸들링
                Handler tHandler = new Handler();
                tHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        OnBoardingScreen = getSharedPreferences("OnBoardingScreen",MODE_PRIVATE);
                        boolean isFirstTime = OnBoardingScreen.getBoolean("firstTime",true);

                        if (isFirstTime){

                            SharedPreferences.Editor editor = OnBoardingScreen.edit();
                            editor.putBoolean("firstTime",false);
                            editor.commit();

                            Intent intent = new Intent(Intro_loading.this, OnBoarding.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Intent intent = new Intent(Intro_loading.this, OnBoarding.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                },1000);

            }
        }, SPLASH_SCREEN);
    }
}




