package com.lmh.mytraveldairyjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.lmh.mytraveldairyjava.SignInActivity.email;

public class ProfileActivity extends AppCompatActivity {
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilelayout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("설정");


        //로그인 체크
        appLoginCheck3();
        //


    }

    private void appLoginCheck3() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(ProfileActivity.this
                    , "접속이 해제되었읍니다.. 다시 로그인을 해주세요~~", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }
    }


    public void pushAlarmSelect(View view) {
        Toast.makeText(ProfileActivity.this, "아직 미구현!!!!", Toast.LENGTH_SHORT).show();

    }

    public void passwordRestSelect(View view) {
        Toast.makeText(ProfileActivity.this, "아직 미구현!!!!", Toast.LENGTH_SHORT).show();

    }

    public void googleJoinedSelect(View view) {
        Toast.makeText(ProfileActivity.this, "아직 미구현!!!!", Toast.LENGTH_SHORT).show();
    }

    public void naverJoinedSelect(View view) {
        Toast.makeText(ProfileActivity.this, "아직 미구현!!!!", Toast.LENGTH_SHORT).show();

    }

    public void selectCurrency(View view) {


        //TextView tmp1 = (TextView) findViewById(R.id.selected_curr);
        //tmp1.setText(gcurr);
        //String selectedCurrency = tmp1.getText().toString().trim();
        //Toast.makeText(ProfileActivity.this, "전송화통화 : " + selectedCurrency, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ProfileActivity.this, CurrencySelect.class);
        //intent.putExtra("currencySelected", "selectedCurrency");
        startActivity(intent);

    }

    public void logoutStart(View view) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            FirebaseAuth.getInstance().signOut();

            Toast.makeText(ProfileActivity.this
                    , "로그아웃 하였읍니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
            startActivity(intent);


        } else {
            Toast.makeText(ProfileActivity.this
                    , "로그아웃 상태입니다..", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
            // startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// 뒤로가기 버튼 눌렀을 때
            Intent intent = new Intent(this,UserDashboard.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    //화면 back 버튼 눌렀을때처리
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(ProfileActivity.this, UserDashboard.class);
        startActivity(intent);
        // 현재액티비의 루트 액티비티까지 종료시켜라
        // 루트 설정화면을 부른 이전 메뉴 액티비티
        // 드로워 화면 만들면 드로워 화면으로 변경해라..아니면 그대로 대시보드로 이동
        finishAffinity();
    }

    public void etcDescStart(View view) {
        Intent intent = new Intent(this,TravelDairyDescription.class);
        startActivity(intent);
        finish();
    }
}
