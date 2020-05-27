package ProfileFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lmh.mytraveldairy.R;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

import CommonFolder.BackPressHandler;
import MyProgressDialog.MyProgressDialogActivity;
import OnBoarding.OnBoardingActivity;

public class NicNameActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private Toolbar toolbar;

    private ProgressDialog dialog1;
    private MyProgressDialogActivity myProgressDialogActivity;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference UserRef;
    private FirebaseUser firebaseUser;

    private String nicNameFromProfileActivity, profileImageFromProfileActivity, coverImagefromProfileActivity;

    private String tmpedtext,currentUser;

    // BackPressHandler 객체 선언, 할당
    private BackPressHandler backPressHandler = new BackPressHandler(this);

    TextView nicnameView, nicmailView;
    ImageView profileImageView,coverImageView;
    EditText edPreEdNicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nic_name);

        Picasso.Builder builder = new Picasso.Builder(this);

        toolbar = findViewById(R.id.tb_nicname_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        nicmailView = (TextView) findViewById(R.id.text_pre_mail_id);
        nicnameView = (TextView) findViewById(R.id.text_pre_nic_name);
        edPreEdNicName = (EditText) findViewById(R.id.ed_pre_nic_name);
        profileImageView = (ImageView) findViewById(R.id.image_pre_profile);
        coverImageView = (ImageView) findViewById(R.id.image_header_cover_bg);
        currentUser = firebaseAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child(currentUser).child("UserTB");

        showAllNicNameData();

    }

    private void showAllNicNameData() {
        myProgressDialogActivity = new MyProgressDialogActivity(this);
        myProgressDialogActivity.show();

        Intent intent = getIntent();
        String currentUser = firebaseAuth.getCurrentUser().getEmail();
        String user = currentUser.toString();
        nicNameFromProfileActivity = intent.getStringExtra("UserTB_nicName_Send");
        profileImageFromProfileActivity = intent.getStringExtra("UserTB_profileImage_Send");
        coverImagefromProfileActivity = intent.getStringExtra("UserTB_coverImage_Send");

        nicnameView.setText(nicNameFromProfileActivity);
        nicmailView.setText(user);

        if (!profileImageFromProfileActivity.equals("NA")) {
            // 프로파일이미지교체
            Picasso.get().load(profileImageFromProfileActivity).into(profileImageView);
        }
        if (!coverImagefromProfileActivity.equals("NA")) {
            Picasso.get().load(coverImagefromProfileActivity).into(coverImageView);
            // 백그라운드 이미지교체
        }

        myProgressDialogActivity.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Intent goBackIntent = new Intent(this, OnBoardingActivity.class);
            goBackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(goBackIntent);
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
                    Intent udateNicNameIntent = new Intent(NicNameActivity.this, OnBoardingActivity.class);
                    udateNicNameIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(udateNicNameIntent);
                    finish();
                } else {
                    //MessageToast.message(UpdateNicName.this,"실제접속했어요");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showProcessDialog1() {
        dialog1 = new ProgressDialog(this);
        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog1.setTitle("닉네임변경");
        dialog1.setMessage("DB에 닉네임을 변경중입니다...");
        dialog1.show();
        dialog1.setCanceledOnTouchOutside(true);
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbarcustum, menu);
        return true;
    }

    public void btn_action_preview(View view) {
        tmpedtext = edPreEdNicName.getEditableText().toString();
        if (tmpedtext.length() > 0){
            nicnameView.setText(tmpedtext);
        } else
        {
            Toast.makeText(NicNameActivity.this, "미리보기 할 닉네임이 없읍니다...", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NewApi")
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
                    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    //db노드 저장할 노드지정
                    UserRef = FirebaseDatabase.getInstance().getReference().child(currentUserId).child("UserTB");
                    HashMap UpdateNicNameMap = new HashMap();
                    UpdateNicNameMap.put("UserTB_nicName", tmpedtext);
                    UpdateNicNameMap.put("UserTB_timeStampUpdateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH)));
                    UserRef.updateChildren(UpdateNicNameMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NicNameActivity.this, "닉네임변경완료!!!!!!", Toast.LENGTH_SHORT).show();
                                dialog1.dismiss();
                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(NicNameActivity.this, "변경실패!! 다시 시도해주세요  : " + message, Toast.LENGTH_SHORT).show();
                                dialog1.dismiss();
                            }
                        }
                    });
                } else {
                    Toast.makeText(NicNameActivity.this, "변경할 닉네임이 없읍니다...", Toast.LENGTH_SHORT).show();
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
        Intent updateIntent = new Intent(NicNameActivity.this, ProfileActivity.class);
        updateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(updateIntent);
    }
}