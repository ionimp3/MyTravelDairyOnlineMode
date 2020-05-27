package CommonFolder;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.lmh.mytraveldairy.R;

import java.util.List;

import OnBoarding.OffLineOnBoardingActivity;


public class IntroAnimateActivity extends AppCompatActivity {

    private static int sSPLASH_SCREEN = 2000;
    SharedPreferences PREF_OnBoardingScreen;


    //애니메이션
    Animation getAni_first_position, getAni_Second_position;
    ImageView imageFirstPosition, imageSecondPosition;
    TextView textFirstSentence, textSecondSentence;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro_animate);


        //animation
        getAni_first_position = AnimationUtils.loadAnimation(this, R.animator.ani_top_animator);
        getAni_Second_position = AnimationUtils.loadAnimation(this, R.animator.ani_bottom_animator);

        //hook
        imageFirstPosition = findViewById(R.id.ani_loading_intro);
        textFirstSentence = findViewById(R.id.text_intro_first_message);
        textSecondSentence = findViewById(R.id.text_intro_second_message);

        imageFirstPosition.setAnimation(getAni_first_position);
        textFirstSentence.setAnimation(getAni_Second_position);
        textSecondSentence.setAnimation(getAni_Second_position);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //애니메이션후 글자색,바탕색 변경
/*              textFirstSentence.setTextColor(Color.parseColor("#ffffff"));
                textSecondSentence.setTextColor(Color.parseColor("#ffffff"));
*/
                //타이틀색상 변형 유지시간 핸들링
                Handler tHandler = new Handler();
                tHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PREF_OnBoardingScreen = getSharedPreferences("OnBoardingScreen", MODE_PRIVATE);
                        boolean isFirstTime = PREF_OnBoardingScreen.getBoolean("firstTime", true);

                        if (isFirstTime) {

                            SharedPreferences.Editor editor = PREF_OnBoardingScreen.edit();
                            editor.putBoolean("firstTime", false);
                            editor.commit();
                            checkCameraStorageLocationPermission();

                        } else {
                            checkCameraStorageLocationPermission();
                        }
                    }
                }, 500);

            }
        }, sSPLASH_SCREEN);
    }

    private void checkCameraStorageLocationPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent;
                intent = new Intent(IntroAnimateActivity.this, OffLineOnBoardingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(IntroAnimateActivity.this, "권한 허용을 하지 않아, 앱을 종료합니다.",Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                moveTaskToBack(true);                        // 태스크를 백그라운드로 이동
                finishAndRemoveTask();                        // 액티비티 종료 + 태스크 리스트에서 지우기
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        };

        TedPermission.with(IntroAnimateActivity.this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("권한을 허용 하지 않으면, 앱을 종료합니다. 권한을 허용해주세요[설정 - 앱 - 권한]")
                .setPermissions(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET,Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }


    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(true);                        // 태스크를 백그라운드로 이동
        finishAndRemoveTask();                        // 액티비티 종료 + 태스크 리스트에서 지우기
        android.os.Process.killProcess(android.os.Process.myPid());    // 앱 프로세스 종료
    }
}
