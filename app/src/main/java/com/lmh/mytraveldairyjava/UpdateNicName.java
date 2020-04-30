package com.lmh.mytraveldairyjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class UpdateNicName extends AppCompatActivity {
    ActionBar actionBar;
    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    // BackPressHandler 객체 선언, 할당
    private BackPressHandler backPressHandler = new BackPressHandler(this);

    TextView nicnameView, nicmailView;
    ImageView coverImageView, profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatenicnamelayout);

        //이미지투명도처리
        Drawable alpha = ((LinearLayout)findViewById(R.id.stacklayout2)).getBackground();
        alpha.setAlpha(50);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("닉네임변경");

        // Authentication, Database, Storage 초기화
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        nicmailView = (TextView) findViewById(R.id.premailid);
        nicnameView = (TextView) findViewById(R.id.prenicname);

        //로그인 체크
        appLoginCheck3();
        //
        showAllNicNameData();

    }

    private void showAllNicNameData() {

        Intent intent = getIntent();
        String atview = intent.getStringExtra("disp_MAIL_ID_Send");
        String brview = intent.getStringExtra("nic_NAME_NM_Send");
        //Toast.makeText(this, "프로파일액티비티에 데이터 넘겨 받음" +  atview , Toast.LENGTH_SHORT).show();
        nicnameView.setText(brview);
        nicmailView.setText(atview);

    }

    private void appLoginCheck3() {
        if (mAuth.getCurrentUser() == null) {
            //로그인요청
            Toast.makeText(this
                    , "접속이 해제되었읍니다.. 다시 로그인을 해주세요~~", Toast.LENGTH_SHORT).show();

            finish();
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarcustum, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 뒤로가기 버튼 눌렀을 때
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            case R.id.action_btn1: { // 오른쪽 상단 버튼 눌렀을 때
                Toast.makeText(this, "DB에 저장진행한다", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //화면 back 버튼 눌렀을때처리
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        // 현재액티비의 루트 액티비티까지 종료시켜라
        // 루트 설정화면을 부른 이전 메뉴 액티비티
        // 드로워 화면 만들면 드로워 화면으로 변경해라..아니면 그대로 대시보드로 이동
        finish();
    }


    @Override
    protected void onDestroy() {
        //Toast.makeText(ProfileActivity.this
        //        , "로그아웃 상태입니다..", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        Log.i("TAG","onDestory");
    }
}
