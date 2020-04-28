package com.lmh.mytraveldairyjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.lmh.mytraveldairyjava.SignInActivity.email;

public class ProfileActivity extends AppCompatActivity {
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilelayout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("설정");



        //db에 사용할 키 아이디 만들기

        //로그인 사용자
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            email = user.getEmail();
            String test1 = email.replaceAll("[.]", "");
            String emailpk = test1.replaceAll("[@]", "");
            TextView txemail = findViewById(R.id.txjoinTypeEmail);
            txemail.setText(email);


            Toast.makeText(ProfileActivity.this
                    , "접속중...." + emailpk, Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(ProfileActivity.this
                    , "접속이 해제되었읍니다.. 다시 로그인 시해주세요~~", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
            startActivity(intent);
        }

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


        //TextView tmp1 = (TextView) findViewById(R.id.selected_curr);
        //tmp1.setText(gcurr);
        //String selectedCurrency = tmp1.getText().toString().trim();
        //Toast.makeText(ProfileActivity.this, "전송화통화 : " + selectedCurrency, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ProfileActivity.this, CurrencySelect.class);
        //intent.putExtra("currencySelected", "selectedCurrency");
        startActivity(intent);

    }

    public void logoutStart(View view) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            FirebaseAuth.getInstance().signOut();

            Toast.makeText(ProfileActivity.this
                    , "로그아웃 하였읍니다." , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
            startActivity(intent);



        } else {
            Toast.makeText(ProfileActivity.this
                    , "로그아웃 상태입니다..", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
            // startActivity(intent);
        }


    }
}
