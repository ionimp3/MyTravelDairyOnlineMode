package com.lmh.mytraveldairyjava;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignInActivity extends AppCompatActivity {
    static String loginuseremail;
    static String email;
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

        appLoginCheck();


        login = (Button) findViewById(R.id.callSignIn);
        passwordfind = (Button) findViewById(R.id.callPasswordFind);
        // 버튼텍스트에 밑줄긋기
        //passwordfind.setPaintFlags(passwordfind.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //
        email_login = (EditText) findViewById(R.id.etEmailId);
        pwd_login = (EditText) findViewById(R.id.etPassword);

        //firebase 초기화
        firebaseAuth = FirebaseAuth.getInstance();

    }

    //로그인확인
    private void appLoginCheck() {
        //로그인확인
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(SignInActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(SignInActivity.this
                    , "로그인 해주세요..", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
            // startActivity(intent);
        }
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

    //

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
    //


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

                            // [START rtdb_enable_persistence]
                            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                            // [END rtdb_enable_persistence]
                            // [START rtdb_keep_synced]
                            DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("");
                            scoresRef.keepSynced(true);
                            // [END rtdb_keep_synced]


                            Intent intent = new Intent(SignInActivity.this, ProfileActivity.class);
                            //여기서 로그인한 메일아이디를 다른 액티비티로 넘겨줘야함..화면에 표시하기위해
                            //DB상에는 메일과패스워드 정보없음..나중에 자기가 쓴글만 서로 읽고,쓰기 가능하도록
                            //관리자도 내용확인 불가.(FIREBASE DB RULE 작성자만 읽고 쓰도록 설정
                            loginuseremail = String.format("%s", email);
                            Toast.makeText(SignInActivity.this, "로그인 성공!!!!", Toast.LENGTH_SHORT).show();
                            //메일 아이뒤 임시 전달
                            intent.putExtra("sendemailid", loginuseremail);
                            //
                            startActivity(intent);
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(SignInActivity.this, "등록되지 않은 사용자이거나, 메일아이디 또는 비밀번호가 틀렸읍니다.!!!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });

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
    //


    //프로세스다이얼로그 start
    private void showProcessDialog() {

        dialog = new ProgressDialog(SignInActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("데이터를 확인하는 중입니다.");
        dialog.show();

    }


    //네이버로 로그인 미구현
    public void naverLoginStart(View view) {

        Toast.makeText(SignInActivity.this, "아직 지원하지 않습니다....", Toast.LENGTH_SHORT).show();

    }

    //구글로 로그인 미구현
    public void googleLoginStart(View view) {

        Toast.makeText(SignInActivity.this, "아직 지원하지 않습니다....", Toast.LENGTH_SHORT).show();

    }
    //


}
