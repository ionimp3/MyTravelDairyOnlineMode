package Common;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import DashBoard.UserDashboard;
import Profile.ProfileActivity;
import com.lmh.mytraveldairyjava.R;

import java.time.LocalDateTime;


public class SignUpActivity extends AppCompatActivity {

    private Button regist_CallStart;
    private EditText repwd_join;
    public CheckBox signup_checkbox;
    ProgressBar progressBar1;
    ProgressDialog dialog1;
    static String loginuseremail;
    private EditText email_join;
    private EditText pwd_join;
    //FIREBASE 관련 선안
    FirebaseAuth firebaseAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    ActionBar actionBar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuplayout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 툴바 왼쪽 버튼 설정
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        //supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)  // 왼쪽 버튼 이미지 설정
        //supportActionBar!!.setDisplayShowTitleEnabled(false)    // 타이틀 안보이게 하기


        email_join = (EditText) findViewById(R.id.regEmailId);
        pwd_join = (EditText) findViewById(R.id.regPassword);
        repwd_join = (EditText) findViewById((R.id.regRePassword));
        regist_CallStart = (Button) findViewById(R.id.callSignUp);
        //signup_checkbox = (CheckBox) findViewById(R.id.checkbox_agree);

        firebaseAuth = FirebaseAuth.getInstance();

        appLoginCheck2();

    }

    //validation 메일
    private Boolean validateSignUpEmailid() {
        String joinvalidemail = email_join.getEditableText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]";

        if (joinvalidemail.isEmpty()) {
            email_join.setError("공백은 허용하지 않습니다.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(joinvalidemail).matches()) {
            email_join.setError("메일형식이 잘 못 되었읍니다.");
            return false;
        } else {
            email_join.setError(null);
            return true;
        }

    }

    //
    //validation  패스워드
    private Boolean validateSignUpPassword() {
        String joinvalidPassword = pwd_join.getEditableText().toString().trim();
        String passwordPattern = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z@#$%^&+=_~!])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$";

        if (joinvalidPassword.isEmpty()) {
            pwd_join.setError("공백은 허용하지 않습니다.");
            return false;
        } else if (!joinvalidPassword.matches(passwordPattern)) {
            pwd_join.setError("패스워드는 최소6자리이상입니다..");
            return false;
        } else {
            pwd_join.setError(null);
            return true;
        }

    }

    //
    //validation 비밀번호재입력
    public Boolean reSignUpPassword() {
        String joinrePassword = repwd_join.getEditableText().toString().trim();
        String passwordPattern = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z@#$%^&+=_~!])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$";

        if (joinrePassword.isEmpty()) {
            repwd_join.setError("공백은 허용하지 않습니다.");
            return false;
        } else if (!joinrePassword.matches(passwordPattern)) {
            repwd_join.setError("패스워드는 최소6자리이상입니다..");
            return false;
        } else {
            repwd_join.setError(null);
            return true;
        }

    }

    // 등록실행
    public void regStart(View view) {

        if (!validateSignUpEmailid() | !validateSignUpPassword() | !reSignUpPassword()) {
            return;
        }
        final String joinemail = email_join.getText().toString().trim();
        String joinpwd = pwd_join.getText().toString().trim();
        String repwd = repwd_join.getText().toString().trim();

        if (!(joinpwd.equals(repwd))) {
            repwd_join.setError("비밀번호가 동일하지 않습니다...");
            return;
        }

        showProcessDialog1();

        firebaseAuth.createUserWithEmailAndPassword(joinemail, joinpwd)

                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            //회원가입 완료 후 후 DB_SETTING_TB 신규 레크드 등록
                            //넘겨줄 data 정의

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            //
                            String base_CURR_CD = "1";
                            String base_CURR_NM = "KRW";
                            String cover_PIC = "";
                            String disp_MAIL_ID = user.getEmail();
                            String tmps1 = disp_MAIL_ID.replaceAll("[.]", "");
                            String tmps2 = tmps1.replaceAll("[@]", "");
                            String login_MAT_ID = "1";
                            String nic_NAME_NM = "닉네임";
                            String now_USER_ST = "1";
                            String profile_PIC = "";
                            String push_ALAR_ST = "N";
                            String sele_MAIL_PK = tmps2;
                            String tstamp_UP_DT = LocalDateTime.now().toString();
                            String tstamp_CR_DT = LocalDateTime.now().toString();


                            //db노드 지정
                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference((sele_MAIL_PK + "/FDB_SETTING_TB"));

                            //데이터 저장
                            SettingHelperClass settingHelperClass;
                            settingHelperClass = new SettingHelperClass( base_CURR_CD,  base_CURR_NM,  login_MAT_ID,  now_USER_ST,  disp_MAIL_ID,  cover_PIC,  nic_NAME_NM,  profile_PIC,  push_ALAR_ST,  sele_MAIL_PK,  tstamp_UP_DT,  tstamp_CR_DT);
                            reference.setValue(settingHelperClass);

                            //

                            loginuseremail = String.format("%s", disp_MAIL_ID);
                            Toast.makeText(SignUpActivity.this, "등록 성공!!!!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, UserDashboard.class);
                            intent.putExtra("sendemailid", loginuseremail);
                            startActivity(intent);
                            finish();
                            dialog1.dismiss();
                        } else {

                            Toast.makeText(SignUpActivity.this, "기존사용자입니다..비밀번호 분실시는 초기화해주세요", Toast.LENGTH_LONG).show();
                            dialog1.dismiss();

                        }
                    }
                });

    }

    //프로세스다이얼로그 start
    private void showProcessDialog1() {

        dialog1 = new ProgressDialog(SignUpActivity.this);
        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog1.setMessage("데이터를 확인하는 중입니다.");
        dialog1.show();
        //
    }

    public void naverSignUpStart(View view) {

        Toast.makeText(SignUpActivity.this, "아직 미구현!!!!", Toast.LENGTH_SHORT).show();
    }

    public void googleSignUpStart(View view) {
        Toast.makeText(SignUpActivity.this, "아직 미구현!!!!", Toast.LENGTH_SHORT).show();

    }
    //로그인확인
    private void appLoginCheck2() {
        //로그인확인
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseAuth.getInstance().signOut();
            /*Intent intent = new Intent(SignUpActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();*/
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// 뒤로가기 버튼 눌렀을 때
            Intent intent = new Intent(this, OnBoarding.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    //화면 back 버튼 눌렀을때처리
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //맨처음화면으로 이동
        Intent intent = new Intent(SignUpActivity.this,OnBoarding.class);
        startActivity(intent);
        finish();
    }
}
