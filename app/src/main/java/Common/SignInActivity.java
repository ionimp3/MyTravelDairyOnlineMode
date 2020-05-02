package Common;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.lmh.mytraveldairyjava.R;

import DashBoard.UserDashboard;



public class SignInActivity extends AppCompatActivity {
    ActionBar actionBar;
    private Toolbar toolbar;
    public static String loginuseremail;
    public static String email;
    private Button login;
    private Button passwordfind;
    private EditText email_login;
    private EditText pwd_login;

    ProgressBar progressBar;
    ProgressDialog dialog;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signinlayout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //firebase 초기화
        firebaseAuth = FirebaseAuth.getInstance();

        login = (Button) findViewById(R.id.callSignIn);
        passwordfind = (Button) findViewById(R.id.callPasswordFind);
        email_login = (EditText) findViewById(R.id.etEmailId);
        pwd_login = (EditText) findViewById(R.id.etPassword);
        // 버튼텍스트에 밑줄긋기
        //passwordfind.setPaintFlags(passwordfind.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //
    }


    //validation 메일
    private Boolean validateEmailid() {
        String validemail = email_login.getEditableText().toString().trim();
        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]";

        if (validemail.isEmpty()) {
            email_login.setError("공백은 허용하지 않습니다.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(validemail).matches()) {
            email_login.setError("메일형식이 잘 못 되었읍니다.");
            return false;
        } else {
            email_login.setError(null);
            return true;
        }

    }
    //validation  패스워드
    private Boolean validatePassword() {
        String validPassword = pwd_login.getEditableText().toString().trim();
        String passwordPattern = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z@#$%^&+=_~!])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$";

        if (validPassword.isEmpty()) {
            pwd_login.setError("공백은 허용하지 않습니다.");
            return false;
        } else if (!validPassword.matches(passwordPattern)) {
            pwd_login.setError("패스워드는 최소6자리이상입니다..");
            return false;
        } else {
            pwd_login.setError(null);
            return true;
        }

    }
    //로그인 버튼 클릭시..xml파일에서 onClick설정하여 생성
    public void loginStart(View view) {

        if (!validateEmailid() | !validatePassword()) {
            return;
        }
        final String email = email_login.getText().toString().trim();
        String pwd = pwd_login.getText().toString().trim();
        showProcessDialog();
        firebaseAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //이전화면 기억하기...로그인,로그아웃 시 두번로그인.. 조금 문제있음..
                            //기존로그인했던 아이디에서 다른 아이디로 로그인하면 crash 남.
                            // [START rtdb_enable_persistence]
                            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                            // [END rtdb_enable_persistence]
                            // [START rtdb_keep_synced]
                            //DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("");
                            // scoresRef.keepSynced(true);
                            // [END rtdb_keep_synced]
                            MessageToast.message(SignInActivity.this,"로그인 성공 !!!");
                            //Toast.makeText(SignInActivity.this, "로그인 성공 !!! : " + email, Toast.LENGTH_SHORT).show();
                            Intent signinIntent = new Intent(SignInActivity.this, UserDashboard.class);
                            signinIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(signinIntent);
                            finish();
                            dialog.dismiss();
                        } else {
                            String message = task.getException().getMessage();
                            Toast.makeText(SignInActivity.this,"로그인 실패 오류내용 : "+ message,Toast.LENGTH_SHORT).show();
                            MessageToast.message(SignInActivity.this,"등록되지 않은 사용자이거나, 메일아이디 또는 비밀번호가 틀렸읍니다.!!!");
                            //Toast.makeText(SignInActivity.this, "등록되지 않은 사용자이거나, 메일아이디 또는 비밀번호가 틀렸읍니다.!!!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Intent userdashboardIntent = new Intent(SignInActivity.this, UserDashboard.class);
            userdashboardIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(userdashboardIntent);
            finish();
        }
    }

    //
    //패스워드 리셋 요청화면 다이얼로그
    public void passwordFindStart(View view) {

        // 다이얼로그 띄우기
        AlertDialog.Builder alertpassword = new AlertDialog.Builder(SignInActivity.this);
        View sView = getLayoutInflater().inflate(R.layout.alert_password_find, null);
        alertpassword.setView(sView);

        final EditText sfindsendemailid = (EditText) sView.findViewById(R.id.findsendemailid);
        Button sbt_sendmail = (Button) sView.findViewById(R.id.bt_sendmail);

        final AlertDialog passwordDialog = alertpassword.create();
        passwordDialog.setCanceledOnTouchOutside(false);
        //

        //다이얼로그내 버튼 클릭시
        sbt_sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailAddress = "" + sfindsendemailid.getEditableText().toString();
                // [START send_password_reset]
                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    passwordDialog.dismiss();
                                    Toast.makeText(SignInActivity.this, "비밀번호재설정메일을 발송하였읍니다....", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignInActivity.this, "존재하지 않는 사용자 메일입니다...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                // [END send_password_reset]

            }
        });

        passwordDialog.show();
    }
    //네이버로 로그인 미구현
    public void naverLoginStart(View view) {

        Toast.makeText(SignInActivity.this, "아직 지원하지 않습니다....", Toast.LENGTH_SHORT).show();

    }
    //구글로 로그인 미구현
    public void googleLoginStart(View view) {

        Toast.makeText(SignInActivity.this, "아직 지원하지 않습니다....", Toast.LENGTH_SHORT).show();

    }
    //프로세스다이얼로그 start
    private void showProcessDialog() {
        dialog = new ProgressDialog(SignInActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("로그인");
        dialog.setMessage("로그인진행중입니다...");
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
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
        Intent intent = new Intent(SignInActivity.this, OnBoarding.class);
        startActivity(intent);
        finish();
    }

}
