package Profile;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Common.BackPressHandler;

import com.lmh.mytraveldairyjava.R;

import Common.SignInActivity;

public class UpdateNicName<tmpedtext> extends AppCompatActivity {
    ActionBar actionBar;
    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    String brview;
    String atview;
    private String ckey;
    private String tmpedtext;

    // BackPressHandler 객체 선언, 할당
    private BackPressHandler backPressHandler = new BackPressHandler(this);

    Intent intent;

    TextView nicnameView, nicmailView;
    ImageView coverImageView, profileImageView;
    EditText edPreEdNicName;
    LinearLayout Prestacklayout1, Prestacklayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatenicnamelayout);

        //이미지투명도처리
        Drawable alpha = ((LinearLayout) findViewById(R.id.stacklayout2)).getBackground();
        alpha.setAlpha(70);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setTitle("닉네임변경");

        // Authentication, Database, Storage 초기화
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        nicmailView = (TextView) findViewById(R.id.premailid);
        nicnameView = (TextView) findViewById(R.id.prenicname);
        edPreEdNicName = (EditText) findViewById(R.id.preEdNicName);
        Prestacklayout1 = (LinearLayout) findViewById(R.id.stacklayout1);
        Prestacklayout2 = (LinearLayout) findViewById(R.id.stacklayout2);

        //로그인 체크
        appLoginCheck3();
        //
        showAllNicNameData();

    }

    private void showAllNicNameData() {

        Intent intent = getIntent();
        atview = intent.getStringExtra("disp_MAIL_ID_Send");
        brview = intent.getStringExtra("nic_NAME_NM_Send");
        ckey = intent.getStringExtra("sele_MAIL_PK_Send");
        //Toast.makeText(this, "프로파일액티비티에 데이터 넘겨 받음" +  atview , Toast.LENGTH_SHORT).show();
        nicnameView.setText(brview);
        nicmailView.setText(atview);

    }

    private void appLoginCheck3() {
        if (mAuth.getCurrentUser() == null) {
            //로그인요청
            Toast.makeText(this
                    , "접속이 해제되었읍니다.. 다시 로그인을 해주세요~~", Toast.LENGTH_SHORT).show();

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
            case R.id.curr_changed: { // 오른쪽 상단 버튼 눌렀을 때
                Intent intent = getIntent();
                tmpedtext = edPreEdNicName.getEditableText().toString();
                ckey = intent.getStringExtra("sele_MAIL_PK_Send");
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference((ckey + "/FDB_SETTING_TB/"));

                if (mAuth.getCurrentUser() != null) {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
                    alert_confirm.setMessage("닉네임을 변경하시겠읍니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    reference.child("nic_NAME_NM").setValue(tmpedtext).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(UpdateNicName.this, "DB에 변경완료하였읍니다..", Toast.LENGTH_LONG).show();
                                            nicnameView.setText(edPreEdNicName.getEditableText().toString().trim());
                                            //액티비티강제갱신
                                           /* Intent intent = getIntent();
                                            finish();
                                            startActivity(intent);*/
                                            //
                                        }
                                    });
                                }

                            }
                    );
                    alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(UpdateNicName.this, "취소", Toast.LENGTH_LONG).show();
                        }
                    });
                    alert_confirm.show();
                } else {
                    Toast.makeText(this, "변경 실패.", Toast.LENGTH_LONG).show();
                }
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
        Log.i("TAG", "onDestory");
    }

    public void PreViewStart(View view) {
        tmpedtext = edPreEdNicName.getEditableText().toString();
        nicnameView.setText(tmpedtext);
    }

}
