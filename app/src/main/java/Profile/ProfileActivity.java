package Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lmh.mytraveldairyjava.R;
import Common.SignInActivity;
import DashBoard.UserDashboard;

import Common.BackPressHandler;

public class ProfileActivity extends AppCompatActivity {
    Intent intent;
    private String psemail;
    String tmps1;
    String tmps2;

    String baseCurrencyName_FromDB;
    String baseCurrencyCode_FromDB;
    String coverPicture_FromDB;
    String displayMailId_FromDB;
    String loginMethodStatus_FromDB;
    String nicName_FromDB;
    String nowUserStatus_FromDB;
    String profilePicture_FromDB;
    String pushAlarmSelected_FromDB;
    String selectMailPrimaryKey_FromDB;
    String timeStampCreaeTime_FromDB;
    String timeStampUpdatgTime_FromDB;

    public static final int PICK_FROM_ALBUM = 1;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    private TextView textivewDelete;
    Toolbar toolbar;
    // BackPressHandler 객체 선언, 할당
    private BackPressHandler backPressHandler = new BackPressHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilelayout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Authentication, Database, Storage 초기화
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        //로그인 체크
        appLoginCheck3();
    }
    private void appLoginCheck3() {
        if (mAuth.getCurrentUser() == null) {
            //로그인요청
            Toast.makeText(ProfileActivity.this
                    , "접속이 해제되었읍니다.. 다시 로그인을 해주세요~~", Toast.LENGTH_SHORT).show();

            finish();
            Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();

        } else {

            //로그인 상태일떼 data 가져오기 호출
            isUserCallData();
        }
    }

    private void isUserCallData() {
        FirebaseUser user = mAuth.getCurrentUser();
        psemail = user.getEmail();
        tmps1 = psemail.replaceAll("[.]", "");
        tmps2 = tmps1.replaceAll("[@]", "");

        final TextView _txjoinTypeEmail = (TextView) findViewById(R.id.txjoinTypeEmail);
        final TextView _txjoinNic = (TextView) findViewById(R.id.txjoinNic);
        final TextView _txjoinCurr = (TextView) findViewById(R.id.selected_curr);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(tmps2 + "/userProfile");

        Query checkSeleMailPk = reference.orderByChild(tmps2);
        checkSeleMailPk.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    baseCurrencyName_FromDB = dataSnapshot.child("baseCurrencyName").getValue(String.class);
                    baseCurrencyCode_FromDB = dataSnapshot.child("baseCurrencyCode").getValue(String.class);
                    coverPicture_FromDB = dataSnapshot.child("coverPicture").getValue(String.class);
                    displayMailId_FromDB = dataSnapshot.child("displayMailId").getValue(String.class);
                    loginMethodStatus_FromDB = dataSnapshot.child("loginMethodStatus").getValue(String.class);
                    nicName_FromDB = dataSnapshot.child("nicName").getValue(String.class);
                    nowUserStatus_FromDB = dataSnapshot.child("nowUserStatus").getValue(String.class);
                    profilePicture_FromDB = dataSnapshot.child("profilePicture").getValue(String.class);
                    pushAlarmSelected_FromDB = dataSnapshot.child("pushAlarmSelected").getValue(String.class);
                    selectMailPrimaryKey_FromDB = dataSnapshot.child("selectMailPrimaryKey").getValue(String.class);
                    timeStampCreaeTime_FromDB = dataSnapshot.child("timeStampCreaeTime").getValue(String.class);
                    timeStampUpdatgTime_FromDB = dataSnapshot.child("timeStampUpdatgTime").getValue(String.class);

                    _txjoinTypeEmail.setText(displayMailId_FromDB);
                    _txjoinNic.setText(nicName_FromDB);
                    _txjoinCurr.setText(baseCurrencyName_FromDB);

                } else {
                    //
                    Log.d("tag", "불러오기 실패 " + tmps2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        Intent intent = new Intent(ProfileActivity.this, CurrencySelect.class);
        intent.putExtra("baseCurrencyCode_Send",baseCurrencyCode_FromDB);
        intent.putExtra("selectMailPrimaryKey_Send",selectMailPrimaryKey_FromDB);
        startActivity(intent);
        finish();
    }
    public void changeprofile_btn(View view) {
        Intent intent = new Intent(ProfileActivity.this, ChangeProfileImage.class);
        intent.putExtra("profilePicture_Send",profilePicture_FromDB);
        intent.putExtra("selectMailPrimaryKey_FromDB_Send",selectMailPrimaryKey_FromDB);
        startActivity(intent);
        finish();
    }
    public void changenicname_btn(View view) {
        //TextView nic_NAME_NM_FromDB = (TextView)findViewById(R.id.prenicname);
        //Toast.makeText(this, "데이터 넘겨 줍니다." + disp_MAIL_ID_FromDB, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ProfileActivity.this, UpdateNicName.class);
        intent.putExtra("nicName_Send", nicName_FromDB);
        intent.putExtra("displayMailId_Send", displayMailId_FromDB);
        intent.putExtra("selectMailPrimaryKey_FromDB_Send", selectMailPrimaryKey_FromDB);
        startActivity(intent);
        finish();
    }
    public void etcDescStart(View view) {
        Intent intent = new Intent(this, TravelDairyDescription.class);
        startActivity(intent);
        finish();
    }
    public void logoutStart(View view) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(ProfileActivity.this
                    , "로그아웃 하였읍니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(ProfileActivity.this
                    , "로그아웃 상태입니다..", Toast.LENGTH_SHORT).show();
        }
    }
    public void delUserStart(View view) {

        if (mAuth.getCurrentUser() != null) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ProfileActivity.this);
            alert_confirm.setMessage("정말 계정을 삭제 할까요? 탈퇴시 DB에 저장된 데이터도 삭제됩니다.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mAuth.getCurrentUser().delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //db는 선택된 사용자노드 기준 하위글 전체삭제 구문추가
                                            DatabaseReference referenceroot = FirebaseDatabase.getInstance().getReference(tmps2);
                                            referenceroot.setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(ProfileActivity.this, "DB,Storage 데이터 와 계정을 삭제하였읍니다.", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                            //storage 삭제 구문추가..사용자별 디렉토리 생성후 관리하다..삭제시 디렉토리 전체를 삭제
                                            //Toast.makeText(ProfileActivity.this, "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                            finish();
                                        }
                                    });
                        }
                    }
            );
            alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(ProfileActivity.this, "취소", Toast.LENGTH_LONG).show();
                }
            });
            alert_confirm.show();
        } else {
            Toast.makeText(ProfileActivity.this, "삭제 실패.", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// 뒤로가기 버튼 눌렀을 때
            Intent intent = new Intent(this, UserDashboard.class);
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
        finish();
    }
    @Override
    protected void onDestroy() {
        //Toast.makeText(ProfileActivity.this
        //        , "로그아웃 상태입니다..", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        Log.i("TAG", "onDestory");
    }
}
