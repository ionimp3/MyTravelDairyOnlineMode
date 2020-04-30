package Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.lmh.mytraveldairyjava.R;
import com.lmh.mytraveldairyjava.SignInActivity;

public class ChangeProfileImage extends AppCompatActivity {

    FirebaseStorage fStore ;
    FirebaseAuth fAuth ;
    FirebaseUser fUser ;

    String userId;
    String tX_1,tx_2,tx_3;
    long tI_1,tI_2;
    Button chUpBtn;

    DatabaseReference mReference;
    FirebaseDatabase rootNode;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeprofileimage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("읽기테스트");

        //로그인 체크
        appLoginCheck4();
        //

        chUpBtn = findViewById(R.id.read_test);



    }


    private void appLoginCheck4() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(ChangeProfileImage.this
                    , "접속이 해제되었읍니다.. 다시 로그인을 해주세요~~", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChangeProfileImage.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// 뒤로가기 버튼 눌렀을 때
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    //화면 back 버튼 눌렀을때처리
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent intent = new Intent(ProfileActivity.this, UserDashboard.class);
        //startActivity(intent);
        // 현재액티비의 루트 액티비티까지 종료시켜라
        // 루트 설정화면을 부른 이전 메뉴 액티비티
        // 드로워 화면 만들면 드로워 화면으로 변경해라..아니면 그대로 대시보드로 이동
        //finish();
    }




}

