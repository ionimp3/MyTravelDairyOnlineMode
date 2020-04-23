package com.lmh.mytraveldairyjava;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpActivity extends AppCompatActivity {
    private EditText email_join;
    private EditText pwd_join;
    private Button signup_ok;
    private EditText repwd_join;
    //CheckBox signup_checkbox;
    ProgressBar progressBar1;
    ProgressDialog dialog1;
    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuplayout);

        email_join = (EditText) findViewById(R.id.signup_email);
        pwd_join = (EditText) findViewById(R.id.signup_password);
        repwd_join = (EditText) findViewById((R.id.re_password));
        signup_ok = (Button) findViewById(R.id.signupok);


        firebaseAuth = FirebaseAuth.getInstance();


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
    public void joinstart(View view) {

        if (!validateSignUpEmailid() | !validateSignUpPassword()| !reSignUpPassword())  {
            return;
        }
        final String joinemail = email_join.getText().toString().trim();
        String joinpwd = pwd_join.getText().toString().trim();
        String repwd = repwd_join.getText().toString().trim();

        if (!(joinpwd.equals(repwd))){
            repwd_join.setError("비밀번호가 동일하지 않습니다...");
            return;
        }

        showProcessDialog1();

        firebaseAuth.createUserWithEmailAndPassword(joinemail, joinpwd)

                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "등록 성공!!!!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, LoginEmail.class);
                            startActivity(intent);
                            finish();
                            dialog1.dismiss();
                        } else {
                            //signup_checkbox.setChecked(false);
                            Toast.makeText(SignUpActivity.this, "기존사용자입니다..비밀번호 분실시는 초기화해주세요", Toast.LENGTH_SHORT).show();
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



}