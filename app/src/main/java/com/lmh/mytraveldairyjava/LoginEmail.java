package com.lmh.mytraveldairyjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginEmail extends AppCompatActivity{
    private Button join;
    private Button login;
    private EditText email_login;
    private EditText pwd_login;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlaout);

        join = (Button) findViewById(R.id.new_regist);
        login = (Button) findViewById(R.id.login_ok);
        email_login = (EditText) findViewById(R.id.youremail);
        pwd_login = (EditText) findViewById(R.id.yourpassword);

        //초기화
        firebaseAuth = firebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_login.getText().toString().trim();
                String pwd = pwd_login.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(LoginEmail.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(LoginEmail.this, StartActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginEmail.this, "로그인 오류", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginEmail.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

}
