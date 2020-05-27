package SignFolder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lmh.mytraveldairy.MainActivity;
import com.lmh.mytraveldairy.R;
import com.lmh.mytraveldairy.databinding.ActivitySignInBinding;
import com.lmh.mytraveldairy.databinding.ActivitySignUpBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

import MyProgressDialog.MyProgressDialogActivity;
import OnBoarding.OnBoardingActivity;
import ProfileFolder.ProfileImageActivity;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding activitySignUpBinding;
    String displayMailId;
    String loginuseremail;

    ProgressDialog dialog1;
    MyProgressDialogActivity myProgressDialogActivity;

    //FIREBASE 관련 선안
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference uploadeFileListRef;
    DatabaseReference databaseReference;


   private String currentUserId,fileUpLoadKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpBinding.getRoot());
        //setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        //로근인 후 사용할수있는 유저 고유 id
        //currentUserId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //uploadeFileListRef = FirebaseDatabase.getInstance().getReference().child(currentUserId).child("UpLoadFileListTB");


    }
    @Override
    protected void onStart()
    {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            Intent userdashboardIntent = new Intent(SignUpActivity.this, MainActivity.class);
            userdashboardIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(userdashboardIntent);
            finish();
        }
    }

    private Boolean validateSignUpEmailid() {
        String joinvalidemail = activitySignUpBinding.editSignUpEmailId.getEditableText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]";

        if (joinvalidemail.isEmpty()) {
            activitySignUpBinding.editSignUpEmailId.setError("공백은 허용하지 않습니다.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(joinvalidemail).matches()) {
            activitySignUpBinding.editSignUpEmailId.setError("메일형식이 잘 못 되었읍니다.");
            return false;
        } else {
            activitySignUpBinding.editSignUpEmailId.setError(null);
            return true;
        }

    }

    private Boolean validateSignUpPassword() {
        String joinvalidPassword = activitySignUpBinding.editSignUpPassword.getEditableText().toString().trim();
        String passwordPattern = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z@#$%^&+=_~!])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$";

        if (joinvalidPassword.isEmpty()) {
            activitySignUpBinding.editSignUpPassword.setError("공백은 허용하지 않습니다.");
            return false;
        } else if (!joinvalidPassword.matches(passwordPattern)) {
            activitySignUpBinding.editSignUpPassword.setError("패스워드는 최소6자리이상입니다..");
            return false;
        } else {
            activitySignUpBinding.editSignUpPassword.setError(null);
            return true;
        }

    }

    public Boolean reSignUpPassword() {
        String joinrePassword = activitySignUpBinding.editSignUpRepassword.getEditableText().toString().trim();
        String passwordPattern = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z@#$%^&+=_~!])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$";

        if (joinrePassword.isEmpty()) {
            activitySignUpBinding.editSignUpRepassword.setError("공백은 허용하지 않습니다.");
            return false;
        } else if (!joinrePassword.matches(passwordPattern)) {
            activitySignUpBinding.editSignUpRepassword.setError("패스워드는 최소6자리이상입니다..");
            return false;
        } else {
            activitySignUpBinding.editSignUpRepassword.setError(null);
            return true;
        }

    }

    private void GotoMain() {
        //특수문자만제게?
        //loginuseremail = String.format("%s", displayMailId);
        Intent signupIntent = new Intent(SignUpActivity.this, MainActivity.class);
        signupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(signupIntent);
        finish();
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

    //화면 back 버튼 눌렀을때처리
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //맨처음화면으로 이동
        Intent intent = new Intent(SignUpActivity.this, OnBoardingActivity.class);
        startActivity(intent);
        finish();
    }

    public void btn_action_sign_up(View view) {
        if (!validateSignUpEmailid() | !validateSignUpPassword() | !reSignUpPassword()) {
            return;
        }
        final String joinemail = activitySignUpBinding.editSignUpEmailId.getText().toString().trim();
        String joinpwd = activitySignUpBinding.editSignUpPassword.getText().toString().trim();
        String repwd = activitySignUpBinding.editSignUpRepassword.getText().toString().trim();

        if (!(joinpwd.equals(repwd))) {
            activitySignUpBinding.editSignUpRepassword.setError("비밀번호가 동일하지 않습니다...");
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
                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            String timeStampUpdateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy",Locale.ENGLISH));
                            String timeStampCreateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy",Locale.ENGLISH));
                            fileUpLoadKey = (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            //displayMailId = user.getEmail();

                            //db노드 저장할 노드지정
                            databaseReference = FirebaseDatabase.getInstance().getReference().child(currentUserId).child("UserTB");
                            HashMap userProfileMap = new HashMap();
                            userProfileMap.put("UserTB_Id",currentUserId);
                            userProfileMap.put("UserTB_coverImage","NA");
                            userProfileMap.put("UserTB_coverImageKey",fileUpLoadKey+"cover");
                            userProfileMap.put("UserTB_emailId",firebaseUser.getEmail());
                            userProfileMap.put("UserTB_nicName","닉네임");
                            userProfileMap.put("UserTB_nowUserStatus","1");
                            userProfileMap.put("UserTB_profileImage","NA");
                            userProfileMap.put("UserTB_profileImageKey",fileUpLoadKey+"profileimage");
                            userProfileMap.put("UserTB_pushAlarmSelected","N");
                            userProfileMap.put("UserTB_timeStampCreateTime",timeStampCreateTime);
                            userProfileMap.put("UserTB_timeStampUpdateTime",timeStampUpdateTime);
                            databaseReference.updateChildren(userProfileMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        if (task.isSuccessful()){
                                            saveToUploadFileListTB1();
                                            saveToUploadFileListTB2();
                                            GotoMain();
                                            Toast.makeText(getApplicationContext(),"회원가입 및 기본정설정완료!!!!!!",Toast.LENGTH_SHORT).show();
                                            dialog1.dismiss();
                                        }else {
                                            String message = task.getException().getMessage();
                                            Toast.makeText(SignUpActivity.this, "기본정보 설정실패,회원탈퇴후 다시 가입해주세요  : " + message, Toast.LENGTH_SHORT).show();
                                            GotoMain();
                                            dialog1.dismiss();
                                        }
                                    }
                                    else {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(SignUpActivity.this,"기본정보 설정실패,회원탈퇴후 다시 가입해주세요  : " + message,Toast.LENGTH_SHORT).show();
                                        GotoMain();
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
    private void saveToUploadFileListTB1() {
        //showProcessDialog1();
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        HashMap saveToFileListMap = new HashMap();
        saveToFileListMap.put("UpLoadFileListTB_fileName","profileimage.jpg");
        saveToFileListMap.put("UpLoadFileListTB_filePath","/UserTB");
        saveToFileListMap.put("UpLoadFileListTB_UserTB_profileImageKey",fileUpLoadKey+"profileimage");
        saveToFileListMap.put("UpLoadFileListTB_timeStampUpdateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH)));
        uploadeFileListRef = FirebaseDatabase.getInstance().getReference().child(currentUserId).child("UpLoadFileListTB").child(fileUpLoadKey+"profileimage");
        uploadeFileListRef.updateChildren(saveToFileListMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "fil list 저장!!", Toast.LENGTH_SHORT).show();
                   // dialog1.dismiss();
                } else {
                    Toast.makeText(SignUpActivity.this, "file list 저장 실패 다시 진행해주세요" , Toast.LENGTH_SHORT).show();
                   // dialog1.dismiss();
                }
            }
        });
    }
    private void saveToUploadFileListTB2() {
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        //showProcessDialog1();
        HashMap saveToFileListMap = new HashMap();
        saveToFileListMap.put("UpLoadFileListTB_fileName","coverimage.jpg");
        saveToFileListMap.put("UpLoadFileListTB_filePath","/UserTB");
        saveToFileListMap.put("UpLoadFileListTB_UserTB_coverImageKey",fileUpLoadKey+"cover");
        saveToFileListMap.put("UpLoadFileListTB_timeStampUpdateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH)));
        uploadeFileListRef = FirebaseDatabase.getInstance().getReference().child(currentUserId).child("UpLoadFileListTB").child(fileUpLoadKey+"cover");
        uploadeFileListRef.updateChildren(saveToFileListMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "업로드파일리스트 생성완료!!", Toast.LENGTH_SHORT).show();
                    //dialog1.dismiss();
                } else {
                    Toast.makeText(SignUpActivity.this, "업로드파일리스트 생성실패, 다시 진행해주세요" , Toast.LENGTH_SHORT).show();
                   // dialog1.dismiss();
                }
            }
        });
    }

    public void btn_action_google_sign_up(View view) {
    }

    public void btn_action_sign_in(View view) {
        Intent goToSignInIntent = new Intent(SignUpActivity.this, SignInActivity.class);
        goToSignInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToSignInIntent);
        finish();
    }
}
