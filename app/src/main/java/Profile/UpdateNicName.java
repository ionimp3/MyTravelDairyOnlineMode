package Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Common.BackPressHandler;

import com.google.firebase.database.ValueEventListener;
import com.lmh.mytraveldairyjava.R;

import java.time.LocalDateTime;
import java.util.HashMap;

import Common.MessageToast;
import Common.OnBoarding;
import Common.SignInActivity;
import Common.SignUpActivity;
import DashBoard.UserDashboard;

public class UpdateNicName<tmpedtext> extends AppCompatActivity {
    ActionBar actionBar;
    Toolbar toolbar;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    DatabaseReference UserRef;
    private FirebaseUser currentUser;

    ProgressDialog dialog1;

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

        showAllNicNameData();
    }

    private void showAllNicNameData() {

        Intent intent = getIntent();
        atview = intent.getStringExtra("displayMailId_Send");
        brview = intent.getStringExtra("nicName_Send");
        ckey = intent.getStringExtra("selectMailPrimaryKey_Send");
        //Toast.makeText(this, "프로파일액티비티에 데이터 넘겨 받음" +  atview , Toast.LENGTH_SHORT).show();
        nicnameView.setText(brview);
        nicmailView.setText(atview);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent udateNicNameIntent = new Intent(UpdateNicName.this, OnBoarding.class);
            udateNicNameIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(udateNicNameIntent);
            finish();
        } else {
            CheckUserExistance();
        }
    }

    private void CheckUserExistance() {
        final String current_User_Id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference RealUserCheck = FirebaseDatabase.getInstance().getReference();
        RealUserCheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //실제 데이터베이스를 조회해서 이메일 id 가 있는지 확인
                if (!dataSnapshot.hasChild(current_User_Id)) {
                    Intent udateNicNameIntent = new Intent(UpdateNicName.this, OnBoarding.class);
                    udateNicNameIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(udateNicNameIntent);
                    finish();
                }
                else{
                    //MessageToast.message(UpdateNicName.this,"실제접속했어요");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showProcessDialog1() {

        dialog1 = new ProgressDialog(UpdateNicName.this);
        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog1.setTitle("닉네임변경");
        dialog1.setMessage("DB에 닉네임을 변경중입니다...");
        dialog1.show();
        dialog1.setCanceledOnTouchOutside(true);
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarcustum, menu);
        return true;
    }

    public void PreViewStart(View view) {
        tmpedtext = edPreEdNicName.getEditableText().toString();
        nicnameView.setText(tmpedtext);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 뒤로가기 버튼 눌렀을 때
                Intent UpdateNicNameiIntent = new Intent(this, ProfileActivity.class);
                UpdateNicNameiIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(UpdateNicNameiIntent);
                return true;
            }
            case R.id.curr_changed: { // 오른쪽 상단 버튼 눌렀을 때
                Intent intent = getIntent();
                showProcessDialog1();
                tmpedtext = edPreEdNicName.getEditableText().toString();
                if (tmpedtext.length() >= 1) {
                    ckey = intent.getStringExtra("selectMailPrimaryKey_Send");
                    //db노드 저장할 노드지정
                    UserRef = FirebaseDatabase.getInstance().getReference().child(ckey).child("userProfile");
                    HashMap UpdateNicNameMap = new HashMap();
                    UpdateNicNameMap.put("nicName", tmpedtext);
                    UpdateNicNameMap.put("timeStampUpdateTime", LocalDateTime.now().toString());
                    UserRef.updateChildren(UpdateNicNameMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                MessageToast.message(UpdateNicName.this, "닉네임변경완료!!!!!!");
                                dialog1.dismiss();
                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(UpdateNicName.this, "변경실패!! 다시 시도해주세요  : " + message, Toast.LENGTH_SHORT).show();
                                dialog1.dismiss();
                            }
                        }
                    });
                } else
                {
                    MessageToast.message(UpdateNicName.this, "변경할 닉네임이 없읍니다...");
                    dialog1.dismiss();
                }

            }
        }
        return super.onOptionsItemSelected(item);
    }

    //화면 back 버튼 눌렀을때처리
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent updateIntent = new Intent(UpdateNicName.this, ProfileActivity.class);
        updateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(updateIntent);
        dialog1.dismiss();
    }
}
