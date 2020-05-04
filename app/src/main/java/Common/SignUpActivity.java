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
import java.util.HashMap;
import java.util.Objects;


public class SignUpActivity extends AppCompatActivity {

    private Button regist_CallStart;
    private EditText repwd_join;
    public CheckBox signup_checkbox;
    private EditText email_join;
    private EditText pwd_join;

    String displayMailId;
    String loginuseremail;

    ProgressDialog dialog1;

    //FIREBASE 관련 선안
    FirebaseAuth firebaseAuth;
    FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    DatabaseReference UserRef;

    ActionBar actionBar;
    private Toolbar toolbar;

    String currentUserId;

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
        mAuth = FirebaseAuth.getInstance();
        //로근인 후 사용할수있는 유저 고유 id
        //currentUserId = firebaseAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Intent userdashboardIntent = new Intent(SignUpActivity.this, UserDashboard.class);
            userdashboardIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(userdashboardIntent);
            finish();
        }
    }

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
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            displayMailId = user.getEmail();
                            String tmps1 = displayMailId.replaceAll("[.]", "");
                            String tmps2 = tmps1.replaceAll("[@]", "");
                            String selectMailPrimaryKey = tmps2;
                            String timeStampUpdateTime = LocalDateTime.now().toString();
                            String timeStampCreateTime = LocalDateTime.now().toString();
                            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            //db노드 저장할 노드지정
                            UserRef = FirebaseDatabase.getInstance().getReference().child(selectMailPrimaryKey).child("userProfile");
                            HashMap userProfileMap = new HashMap();
                            userProfileMap.put("baseCurrencyCode","1");
                            userProfileMap.put("baseCurrencyName","KRW");
                            userProfileMap.put("coverPicture","NA");
                            userProfileMap.put("currentUserId",currentUserId);
                            userProfileMap.put("displayMailId",displayMailId);
                            userProfileMap.put("loginMethodStatus","1");
                            userProfileMap.put("nicName","닉네임");
                            userProfileMap.put("nowUserStatus","1");
                            userProfileMap.put("profilePicture","NA");
                            userProfileMap.put("pushAlarmSelected","N");
                            userProfileMap.put("selectMailPrimaryKey",selectMailPrimaryKey);
                            userProfileMap.put("timeStampUpdateTime",timeStampUpdateTime);
                            userProfileMap.put("timeStampCreateTime",timeStampCreateTime);
                            UserRef.updateChildren(userProfileMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        final String current_User_Id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        DatabaseReference RealUserCheckIdSave = FirebaseDatabase.getInstance().getReference();
                                        RealUserCheckIdSave.child(current_User_Id).child("realUserId").setValue("Y").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){
                                                    GotoUserDashboard();
                                                    MessageToast.message(SignUpActivity.this,"회원가입 및 기본정설정완료!!!!!!");
                                                    dialog1.dismiss();
                                                }else
                                                    {
                                                        String message = task.getException().getMessage();
                                                        Toast.makeText(SignUpActivity.this,"기본정보 설정실패,회원탈퇴후 다시 가입해주세요  : " + message,Toast.LENGTH_SHORT).show();
                                                        GotoUserDashboard();
                                                        dialog1.dismiss();
                                                }
                                            }
                                        });
                                    }
                                    else {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(SignUpActivity.this,"기본정보 설정실패,회원탈퇴후 다시 가입해주세요  : " + message,Toast.LENGTH_SHORT).show();
                                        GotoUserDashboard();
                                        dialog1.dismiss();
                                    }
                                }
                            });
                        } else {
                            String message = task.getException().getMessage();
                            Toast.makeText(SignUpActivity.this,"가입실패,다시 시도해주세요 : "+message,Toast.LENGTH_SHORT).show();
                            //MessageToast.message(SignUpActivity.this,"기존사용자입니다..비밀번호 분실시는 초기화해주세요");
                            dialog1.dismiss();

                        }
                    }
                });
    }

    private void GotoUserDashboard() {
        loginuseremail = String.format("%s", displayMailId);
        Intent signupIntent = new Intent(SignUpActivity.this, UserDashboard.class);
        signupIntent.putExtra("sendemailid", loginuseremail);
        signupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(signupIntent);
        finish();
        dialog1.dismiss();
    }

    //프로세스다이얼로그 start
    private void showProcessDialog1() {

        dialog1 = new ProgressDialog(SignUpActivity.this);
        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog1.setTitle("신규계정생성");
        dialog1.setMessage("신규계정을 생성중입니다..");
        dialog1.show();
        dialog1.setCanceledOnTouchOutside(true);
        //
    }

    public void naverSignUpStart(View view) {

        Toast.makeText(SignUpActivity.this, "아직 미구현!!!!", Toast.LENGTH_SHORT).show();
    }

    public void googleSignUpStart(View view) {
        Toast.makeText(SignUpActivity.this, "아직 미구현!!!!", Toast.LENGTH_SHORT).show();

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
        Intent intent = new Intent(SignUpActivity.this, OnBoarding.class);
        startActivity(intent);
        finish();
    }
}
