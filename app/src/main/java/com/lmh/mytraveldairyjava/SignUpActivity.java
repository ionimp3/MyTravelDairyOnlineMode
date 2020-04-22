package com.lmh.mytraveldairyjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
    CheckBox signup_checkbox;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuplayout);

        email_join = (EditText) findViewById(R.id.signup_email);
        pwd_join = (EditText) findViewById(R.id.signup_password);
        signup_ok = (Button) findViewById(R.id.signupok);
        signup_checkbox = (CheckBox) findViewById(R.id.signupcheck);

        firebaseAuth = FirebaseAuth.getInstance();


        signup_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signup_checkbox.isChecked()) {
                    signup_ok.setEnabled(true);
                    // 버튼 사용가될때 버튼배경색 바꾸기
                    signup_ok.setBackgroundColor(getResources().getColor(android.R.color.white));

                } else {
                    signup_ok.setEnabled(false);
                    signup_ok.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }
            }
        });

        signup_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_join.getText().toString().trim();
                String pwd = pwd_join.getText().toString().trim();

                if (email.isEmpty()) {
                    signup_ok.setEnabled(false);
                    signup_checkbox.setChecked(false);
                    Toast.makeText(SignUpActivity.this, "메일아이디 확인해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (pwd.isEmpty()) {
                        signup_ok.setEnabled(false);
                        signup_checkbox.setChecked(false);
                        Toast.makeText(SignUpActivity.this, "패스워드 확인해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignUpActivity.this, "등록 성공!!!!!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignUpActivity.this, LoginEmail.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            signup_checkbox.setChecked(false);
                                            Toast.makeText(SignUpActivity.this, "등록 실패!!! 메일아이디와 패스워드를 재확인해주세요", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                });
                    }
                }
            }
        });


    }

}
